let alumnosDelGrupo = [];
let calificacionesActuales = [];
let contexto = { grupoId: null, asignacionId: null, periodo: null, materiaNombre: '' };
let alumnoSeleccionado = null;
let calificacionIdActual = null;

document.addEventListener('DOMContentLoaded', async () => {
    if (!verificarAutenticacion()) return;
    ajustarMenuPorRol();
    document.getElementById('usernameDisplay').textContent = getUsername();
    document.getElementById('rolDisplay').textContent = getRol();
    // No cargamos grupos al inicio, se cargan cuando se encuentra al alumno
});

// --- VISTA 1: BÚSQUEDA INICIAL ---
async function buscarInicial() {
    const termino = document.getElementById('inputBuscarInicial').value.toLowerCase().trim();
    if (!termino) return alert('Ingresa nombre o matrícula');

    // Buscamos en TODOS los alumnos del sistema (no solo de un grupo)
    // Necesitamos un endpoint que busque alumnos por nombre o matrícula
    const r = await fetchConAuth(`${API_URL}/api/alumnos/buscar?termino=${termino}`);

    if (!r || !r.ok) return alert('Error al buscar alumno');
    const alumnos = await r.json();

    if (alumnos.length === 0) return alert('Alumno no encontrado en el sistema.');

    // Tomamos el primer resultado
    const alumnoEncontrado = alumnos[0];

    // Ahora que tenemos al alumno, cargamos su información en la vista de trabajo
    alumnoSeleccionado = {
        alumnoId: alumnoEncontrado.id,
        alumnoNombreCompleto: `${alumnoEncontrado.nombre} ${alumnoEncontrado.apellidoP || ''} ${alumnoEncontrado.apellidoM || ''}`.trim(),
        alumnoMatricula: alumnoEncontrado.matricula
    };

    // Mostrar info del alumno
    document.getElementById('info-alumno-calificar').innerHTML = `
        <h4 style="margin: 0; color: #004080;">${alumnoSeleccionado.alumnoNombreCompleto}</h4>
        <p style="margin: 5px 0 0 0; font-size: 12px;">Matrícula: <strong>${alumnoSeleccionado.alumnoMatricula}</strong></p>
    `;

    // Ocultar buscador y mostrar vista de trabajo
    document.getElementById('vista-buscador').style.display = 'none';
    document.getElementById('vista-trabajo').style.display = 'grid';

    // Cargar grupos, calificaciones y boleta
    await cargarGruposDelAlumno(alumnoEncontrado.id);
    await cargarVistaPreviaBoleta(alumnoEncontrado.id);
}

// --- VISTA 2: CARGA DE DATOS DEL ALUMNO ---
async function cargarGruposDelAlumno(alumnoId) {
    // Obtenemos la inscripción del alumno para saber a qué grupo pertenece
    const r = await fetchConAuth(`${API_URL}/api/inscripciones/alumno/${alumnoId}`);
    if (!r || !r.ok) return;
	

    const inscripciones = await r.json();

    const selectGrupo = document.getElementById('selectGrupo');

    if (inscripciones.length === 0) {
        selectGrupo.innerHTML = '<option value="">Sin inscripción</option>';
        return;
    }

    // Cargamos los grupos en el select (normalmente solo hay uno activo)
    selectGrupo.innerHTML = inscripciones.map(i =>
        `<option value="${i.grupoId}">${i.grupoNombre}</option>`
    ).join('');

    // Disparamos la carga de materias del primer grupo
    await cargarMateriasDelGrupo();
}

async function cargarMateriasDelGrupo() {
    const grupoId = document.getElementById('selectGrupo').value;
    const selectAsignacion = document.getElementById('selectAsignacion');

    if (!grupoId) return;

    const r = await fetchConAuth(`${API_URL}/api/asignaciones/grupo/${grupoId}`);
    if (!r || !r.ok) return;
    const asignaciones = await r.json();

    selectAsignacion.innerHTML = '<option value="">-- Selecciona Materia --</option>' +
        asignaciones.map(a => `<option value="${a.id}" data-materia="${a.materiaNombre}">${a.materiaNombre}</option>`).join('');

    // Al cargar las materias, buscamos si ya tiene una nota para la primera
    await cargarNotaExistente();
}

async function cargarNotaExistente() {
    const asignacionId = document.getElementById('selectAsignacion').value;
    const periodo = document.getElementById('selectPeriodo').value;

    if (!asignacionId || !periodo) {
        document.getElementById('inputNota').value = '';
        document.getElementById('inputObs').value = '';
        return;
    }

    // Buscamos si el alumno ya tiene calificación en esta asignatura/trimestre
    const resCalifs = await fetchConAuth(`${API_URL}/api/calificaciones/asignacion/${asignacionId}`);
    calificacionesActuales = (resCalifs && resCalifs.ok) ? await resCalifs.json() : [];

    const califExistente = calificacionesActuales.find(c =>
        c.alumnoId === alumnoSeleccionado.alumnoId && c.periodo == periodo
    );

    calificacionIdActual = califExistente ? califExistente.id : null;
    document.getElementById('inputNota').value = califExistente ? (califExistente.calificacion ?? '') : '';
    document.getElementById('inputObs').value = califExistente ? (califExistente.observaciones ?? '') : '';
}

async function guardarCalificacion() {
    const nota = document.getElementById('inputNota').value;
    const obs = document.getElementById('inputObs').value;
    const asignacionId = document.getElementById('selectAsignacion').value;
    const periodo = document.getElementById('selectPeriodo').value;

    if (!nota) return alert('Ingresa una calificación');
    if (!asignacionId) return alert('Selecciona una materia');

    const dto = {
        alumnoId: alumnoSeleccionado.alumnoId,
        asignacionId: asignacionId,
        calificacion: parseFloat(nota),
        observaciones: obs.trim() || null,
        periodo: parseInt(periodo),
        activo: true
    };

    const method = calificacionIdActual ? 'PUT' : 'POST';
    const url = calificacionIdActual
        ? `${API_URL}/api/calificaciones/${calificacionIdActual}`
        : `${API_URL}/api/calificaciones`;

    try {
        const response = await fetchConAuth(url, { method, body: JSON.stringify(dto) });
        if (!response || !response.ok) throw new Error('Error al guardar');

        // Si era nueva, actualizamos el ID
        if (!calificacionIdActual) {
            const resGuardada = await response.json();
            calificacionIdActual = resGuardada.id;
        }

        // 👇 FORZAMOS LA ACTUALIZACIÓN DE LA VISTA PREVIA
        // Esperamos un momento a que el backend procese y luego recargamos
        setTimeout(async () => {
            await cargarVistaPreviaBoleta(alumnoSeleccionado.alumnoId);
            alert('✅ Calificación guardada y boleta actualizada');
        }, 300); // 300 milisegundos de margen

    } catch (error) {
        alert('❌ ' + error.message);
    }
}
// --- VISTA 3: LÓGICA DE LA BOLETA ---
// Lógica de la tabla de la boleta (derecha)
// --- VISTA 3: LÓGICA DE LA BOLETA ---
async function cargarVistaPreviaBoleta(alumnoId) {
    try {
        const response = await fetchConAuth(`${API_URL}/api/boletas/alumno/${alumnoId}`);
        if (!response || !response.ok) throw new Error('Error al cargar boleta');
        const data = await response.json();

        // Limpiar todas las celdas antes de pintar
        document.querySelectorAll('.boleta-preview td').forEach(td => td.textContent = '-');

        // Pintar las materias
        data.camposFormativos.forEach(campo => {
            campo.materias.forEach(m => {
                // 👇 SOLUCIÓN: Leemos el nombre de cualquier propiedad que exista
                let nombreMateria = m.nombreMateria || m.nombre || m.materia || "";
                let nombre = nombreMateria.toLowerCase();

                const pintarPV = (id, t1, t2, t3) => {
                    document.getElementById(`pv-t1-${id}`).textContent = (t1 != null) ? Number(t1).toFixed(1) : '-';
                    document.getElementById(`pv-t2-${id}`).textContent = (t2 != null) ? Number(t2).toFixed(1) : '-';
                    document.getElementById(`pv-t3-${id}`).textContent = (t3 != null) ? Number(t3).toFixed(1) : '-';
                };

                // Ahora las comparaciones funcionarán
                if (nombre.includes('español') || nombre.includes('espanol')) pintarPV('espanol', m.trimestre1, m.trimestre2, m.trimestre3);
                if (nombre.includes('inglés') || nombre.includes('ingles')) pintarPV('ingles', m.trimestre1, m.trimestre2, m.trimestre3);
                if (nombre.includes('artes')) pintarPV('artes', m.trimestre1, m.trimestre2, m.trimestre3);
                if (nombre.includes('matemátic') || nombre.includes('matematic')) pintarPV('matematicas', m.trimestre1, m.trimestre2, m.trimestre3);
                if (nombre.includes('biolog')) pintarPV('biologia', m.trimestre1, m.trimestre2, m.trimestre3);
                if (nombre.includes('geograf')) pintarPV('geografia', m.trimestre1, m.trimestre2, m.trimestre3);
                if (nombre.includes('historia')) pintarPV('historia', m.trimestre1, m.trimestre2, m.trimestre3);
                if (nombre.includes('cívica') || nombre.includes('etica') || nombre.includes('formacion')) pintarPV('formacion', m.trimestre1, m.trimestre2, m.trimestre3);
                if (nombre.includes('tecnolog') || nombre.includes('mecatronica')) pintarPV('tecnologia', m.trimestre1, m.trimestre2, m.trimestre3);
                if (nombre.includes('física') || nombre.includes('educacion')) pintarPV('educacion', m.trimestre1, m.trimestre2, m.trimestre3);
            });
        });

        // Pintamos los promedios
        document.getElementById('pv-prom-t1').textContent = (data.promedioT1 != null) ? Number(data.promedioT1).toFixed(1) : 'En curso';
        document.getElementById('pv-prom-t2').textContent = (data.promedioT2 != null) ? Number(data.promedioT2).toFixed(1) : 'En curso';
        document.getElementById('pv-prom-t3').textContent = (data.promedioT3 != null) ? Number(data.promedioT3).toFixed(1) : 'En curso';
        document.getElementById('pv-prom-general').textContent = (data.promedioFinal != null) ? Number(data.promedioFinal).toFixed(1) : 'En curso';

    } catch (error) {
        console.error('Error cargando vista previa:', error);
    }
}
function regresarBuscador() {
    document.getElementById('vista-trabajo').style.display = 'none';
    document.getElementById('vista-buscador').style.display = 'block';
    document.getElementById('inputBuscarInicial').value = '';
}