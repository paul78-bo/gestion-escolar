// ==========================================
// CONFIGURACIÓN
// ==========================================

let grupoId = null;
let grupoActual = null;
let asignaciones = [];
let alumnos = [];
let calificacionesPorAlumno = {};
let filtroMateriaActual = '';
let busquedaAlumnoActual = '';

// ==========================================
// CARGA INICIAL
// ==========================================

document.addEventListener('DOMContentLoaded', async () => {
    if (!verificarAutenticacion()) return;
    ajustarMenuPorRol();

    document.getElementById('usernameDisplay').textContent = getUsername();
    document.getElementById('rolDisplay').textContent = getRol();

    // Obtener el ID del grupo desde la URL
    const params = new URLSearchParams(window.location.search);
    grupoId = params.get('id');

    if (!grupoId) {
        alert('❌ No se especificó un grupo');
        window.location.href = 'grupos.html';
        return;
    }

    await cargarTodo();
});

// ==========================================
// CARGAR TODO
// ==========================================

async function cargarTodo() {
    const tbody = document.getElementById('tablaBody');
    tbody.innerHTML = '<tr><td colspan="5" style="text-align: center;">Cargando datos del grupo...</td></tr>';

    try {
        // 1. Cargar el grupo
        await cargarGrupo();

        // 2. Cargar las asignaciones del grupo
        await cargarAsignaciones();

        // 3. Cargar los alumnos del grupo (vía inscripciones)
        await cargarAlumnos();

        // 4. Cargar las calificaciones de cada alumno
        await cargarCalificacionesDeTodos();

        // 5. Renderizar todo
        renderizarHeader();
        renderizarTabla();
        renderizarFiltroMateria();
        calcularEstadisticas();

    } catch (error) {
        console.error('Error:', error);
        tbody.innerHTML = `<tr><td colspan="5" style="text-align: center; color: red;">Error: ${error.message}</td></tr>`;
    }
}

// ==========================================
// CARGAR GRUPO
// ==========================================

async function cargarGrupo() {
    const response = await fetchConAuth(`${API_URL}/api/grupos/${grupoId}`);
    if (!response || !response.ok) throw new Error('Grupo no encontrado');

    grupoActual = await response.json();

    document.getElementById('tituloGrupo').textContent = `📚 Grupo ${grupoActual.nombre}`;
    document.getElementById('grupoNombre').textContent = grupoActual.nombre || '-';
    document.getElementById('grupoGrado').textContent = grupoActual.grado || '-';
    document.getElementById('grupoLetra').textContent = grupoActual.letra || '-';
    document.getElementById('grupoTurno').textContent = grupoActual.turno || '-';
    document.getElementById('grupoCiclo').textContent = grupoActual.cicloNombre || '-';
    document.getElementById('grupoInscritos').textContent = `${grupoActual.inscritos || 0} / ${grupoActual.capacidad || 0}`;
}

// ==========================================
// CARGAR ASIGNACIONES (MATERIAS)
// ==========================================

async function cargarAsignaciones() {
    const response = await fetchConAuth(`${API_URL}/api/asignaciones/grupo/${grupoId}`);
    if (!response || !response.ok) throw new Error('Error al cargar asignaciones');

    asignaciones = await response.json();
    console.log('📚 Asignaciones:', asignaciones);
}

// ==========================================
// CARGAR ALUMNOS (VÍA INSCRIPCIONES)
// ==========================================

async function cargarAlumnos() {
    const response = await fetchConAuth(`${API_URL}/api/inscripciones/grupo/${grupoId}`);
    if (!response || !response.ok) throw new Error('Error al cargar inscripciones');

    const inscripciones = await response.json();
    alumnos = inscripciones.map(i => ({
        alumnoId: i.alumnoId,
        alumnoNombreCompleto: i.alumnoNombreCompleto,
        alumnoMatricula: i.alumnoMatricula,
        inscripcionId: i.id
    }));
    console.log(`👥 Alumnos del grupo (${alumnos.length}):`, alumnos);
}

// ==========================================
// CARGAR CALIFICACIONES DE CADA ALUMNO
// ==========================================

async function cargarCalificacionesDeTodos() {
    calificacionesPorAlumno = {};

    const promesas = alumnos.map(async (alumno) => {
        try {
            const response = await fetchConAuth(`${API_URL}/api/calificaciones/alumno/${alumno.alumnoId}`);
            if (!response || !response.ok) return;

            const calificaciones = await response.json();

            const asignacionIds = asignaciones.map(a => a.id);
            const calificacionesDelGrupo = calificaciones.filter(c =>
                asignacionIds.includes(c.asignacionId)
            );

            calificacionesPorAlumno[alumno.alumnoId] = calificacionesDelGrupo;
        } catch (error) {
            console.error(`Error cargando calificaciones de ${alumno.alumnoNombreCompleto}:`, error);
        }
    });

    await Promise.all(promesas);
    console.log('📊 Calificaciones cargadas:', calificacionesPorAlumno);
}

// ==========================================
// RENDERIZAR HEADER (COLUMNAS DINÁMICAS)
// ==========================================

function renderizarHeader() {
    const thead = document.getElementById('tablaHeader');

    let asignacionesAMostrar = asignaciones;
    if (filtroMateriaActual) {
        asignacionesAMostrar = asignaciones.filter(a => a.id.toString() === filtroMateriaActual);
    }

    let headerHTML = `
        <tr>
            <th>Matrícula</th>
            <th>Alumno</th>
    `;

    asignacionesAMostrar.forEach(a => {
        headerHTML += `<th style="text-align: center;">${a.materiaNombre}</th>`;
    });

    headerHTML += `
            <th style="text-align: center;">Promedio</th>
            <th style="text-align: center;">Acciones</th>
        </tr>
    `;

    thead.innerHTML = headerHTML;
}

// ==========================================
// RENDERIZAR TABLA DE ALUMNOS
// ==========================================

function renderizarTabla() {
    const tbody = document.getElementById('tablaBody');

    let asignacionesAMostrar = asignaciones;
    if (filtroMateriaActual) {
        asignacionesAMostrar = asignaciones.filter(a => a.id.toString() === filtroMateriaActual);
    }

    let alumnosAMostrar = alumnos;
    if (busquedaAlumnoActual) {
        const busqueda = busquedaAlumnoActual.toLowerCase();
        alumnosAMostrar = alumnos.filter(a =>
            a.alumnoNombreCompleto.toLowerCase().includes(busqueda) ||
            a.alumnoMatricula.toLowerCase().includes(busqueda)
        );
    }

    const colspan = 4 + asignacionesAMostrar.length;

    if (alumnosAMostrar.length === 0) {
        tbody.innerHTML = `<tr><td colspan="${colspan}" style="text-align: center;">No hay alumnos para mostrar</td></tr>`;
        return;
    }

    tbody.innerHTML = alumnosAMostrar.map(alumno => {
        const calificacionesAlumno = calificacionesPorAlumno[alumno.alumnoId] || [];

        let filaHTML = `
            <tr>
                <td>${alumno.alumnoMatricula}</td>
                <td><strong>${alumno.alumnoNombreCompleto}</strong></td>
        `;

        asignacionesAMostrar.forEach(asignacion => {
            const cal = calificacionesAlumno.find(c => c.asignacionId === asignacion.id);

            if (cal && cal.calificacion != null) {
                const color = cal.calificacion >= 6 ? '#27ae60' : '#e74c3c';
                filaHTML += `
                    <td style="text-align: center;">
                        <span style="font-weight: bold; color: ${color}; cursor: pointer;"
                              onclick="abrirModalCalificacion(${cal.id}, '${alumno.alumnoNombreCompleto}', '${asignacion.materiaNombre}', ${cal.calificacion}, '${(cal.observaciones || '').replace(/'/g, "\\'")}')">
                            ${cal.calificacion.toFixed(1)}
                        </span>
                    </td>
                `;
            } else if (cal) {
                filaHTML += `
                    <td style="text-align: center;">
                        <span style="color: #95a5a6; cursor: pointer;"
                              onclick="abrirModalCalificacion(${cal.id}, '${alumno.alumnoNombreCompleto}', '${asignacion.materiaNombre}', null, '')">
                            Sin calificar
                        </span>
                    </td>
                `;
            } else {
                filaHTML += `<td style="text-align: center; color: #95a5a6;">-</td>`;
            }
        });

        const calificacionesValidas = calificacionesAlumno.filter(c => c.calificacion != null);
        const promedio = calificacionesValidas.length > 0
            ? (calificacionesValidas.reduce((sum, c) => sum + parseFloat(c.calificacion), 0) / calificacionesValidas.length).toFixed(2)
            : '-';

        const promedioColor = promedio !== '-' && parseFloat(promedio) >= 6 ? '#27ae60' : '#e74c3c';

        filaHTML += `
                <td style="text-align: center;">
                    <strong style="color: ${promedioColor};">${promedio}</strong>
                </td>
                <td style="text-align: center;">
                    <button class="btn btn-warning btn-sm" onclick="verCalificacionesAlumno(${alumno.alumnoId})">
                        📊 Ver Calificaciones
                    </button>
                </td>
            </tr>
        `;

        return filaHTML;
    }).join('');
}

// ==========================================
// FILTRO POR MATERIA
// ==========================================

function renderizarFiltroMateria() {
    const select = document.getElementById('filtroMateria');
    select.innerHTML = '<option value="">-- Todas las materias --</option>' +
        asignaciones.map(a => `<option value="${a.id}">${a.materiaNombre}</option>`).join('');
}

function filtrarPorMateria() {
    filtroMateriaActual = document.getElementById('filtroMateria').value;
    renderizarHeader();
    renderizarTabla();
}

// ==========================================
// BÚSQUEDA DE ALUMNOS
// ==========================================

function filtrarAlumnos() {
    busquedaAlumnoActual = document.getElementById('busquedaAlumno').value.trim();
    renderizarTabla();
}

function limpiarBusquedaAlumno() {
    document.getElementById('busquedaAlumno').value = '';
    busquedaAlumnoActual = '';
    renderizarTabla();
}

// ==========================================
// MODAL PARA EDITAR CALIFICACIÓN
// ==========================================

function abrirModalCalificacion(id, alumnoNombre, materiaNombre, calificacion, observaciones) {
    document.getElementById('modalCalificacionTitulo').textContent = 'Editar Calificación';
    document.getElementById('calificacionId').value = id;
    document.getElementById('calificacionAlumno').value = alumnoNombre;
    document.getElementById('calificacionMateria').value = materiaNombre;
    document.getElementById('calificacionValor').value = calificacion != null ? calificacion : '';
    document.getElementById('calificacionObservaciones').value = observaciones || '';

    document.getElementById('modalCalificacion').style.display = 'flex';
}

function cerrarModalCalificacion() {
    document.getElementById('modalCalificacion').style.display = 'none';
}

async function guardarCalificacion() {
    const id = document.getElementById('calificacionId').value;
    const calificacionValor = document.getElementById('calificacionValor').value;
    const observaciones = document.getElementById('calificacionObservaciones').value.trim();

    if (calificacionValor && (parseFloat(calificacionValor) < 0 || parseFloat(calificacionValor) > 10)) {
        alert('❌ La calificación debe estar entre 0 y 10');
        return;
    }

    const dto = {
        calificacion: calificacionValor ? parseFloat(calificacionValor) : null,
        observaciones: observaciones || null
    };

    try {
        const response = await fetchConAuth(`${API_URL}/api/calificaciones/${id}`, {
            method: 'PUT',
            body: JSON.stringify(dto)
        });

        if (!response) return;
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || 'Error al guardar');
        }

        cerrarModalCalificacion();

        await cargarCalificacionesDeTodos();
        renderizarTabla();
        calcularEstadisticas();

        mostrarMensaje('✅ Calificación actualizada');

    } catch (error) {
        alert('❌ ' + error.message);
    }
}

// ==========================================
// VER CALIFICACIONES DE UN ALUMNO
// ==========================================

function verCalificacionesAlumno(alumnoId) {
    window.location.href = `calificaciones.html?alumnoId=${alumnoId}`;
}

// ==========================================
// ESTADÍSTICAS
// ==========================================

function calcularEstadisticas() {
    document.getElementById('statTotalAlumnos').textContent = alumnos.length;
    document.getElementById('statMaterias').textContent = asignaciones.length;

    let sumaPromedios = 0;
    let alumnosConPromedio = 0;
    let aprobados = 0;
    let reprobados = 0;

    alumnos.forEach(alumno => {
        const calificaciones = calificacionesPorAlumno[alumno.alumnoId] || [];
        const validas = calificaciones.filter(c => c.calificacion != null);

        if (validas.length > 0) {
            const promedio = validas.reduce((sum, c) => sum + parseFloat(c.calificacion), 0) / validas.length;
            sumaPromedios += promedio;
            alumnosConPromedio++;

            if (promedio >= 6) {
                aprobados++;
            } else {
                reprobados++;
            }
        }
    });

    const promedioGrupal = alumnosConPromedio > 0
        ? (sumaPromedios / alumnosConPromedio).toFixed(2)
        : '-';

    document.getElementById('statPromedio').textContent = promedioGrupal;
    document.getElementById('statAprobados').textContent = aprobados;
    document.getElementById('statReprobados').textContent = reprobados;
}

// ==========================================
// UTILIDADES
// ==========================================

function mostrarMensaje(mensaje, tipo = 'success') {
    const div = document.getElementById('mensaje');
    if (!div) return;

    div.className = `alert alert-${tipo}`;
    div.textContent = mensaje;
    div.style.display = 'block';

    setTimeout(() => {
        div.style.display = 'none';
    }, 3000);
}