let paginaActual = 0;
let totalPaginas = 0;

document.addEventListener('DOMContentLoaded', async () => {
    if (!verificarAutenticacion()) return;
    if (!verificarPermisoPagina()) return;
    ajustarMenuPorRol();

    document.getElementById('usernameDisplay').textContent = getUsername();
    document.getElementById('rolDisplay').textContent = getRol();

    await Promise.all([cargarGrupos(), cargarMaterias(), cargarProfesores()]);
    cargarAsignaciones();

    document.getElementById('asignacionForm').addEventListener('submit', guardarAsignacion);
});

async function cargarGrupos() {
    const r = await fetchConAuth(`${API_URL}/api/grupos/activos`);
    if (!r || !r.ok) return;
    const grupos = await r.json();
    document.getElementById('grupoId').innerHTML = '<option value="">-- Seleccionar Grupo --</option>' +
        grupos.map(g => `<option value="${g.id}">${g.nombre} - ${g.cicloNombre || ''}</option>`).join('');
}

async function cargarMaterias() {
    const r = await fetchConAuth(`${API_URL}/api/materias/activas`);
    if (!r || !r.ok) return;
    const materias = await r.json();
    
    // Llenamos el contenedor con checkboxes
    document.getElementById('materiasCheckboxes').innerHTML = materias.map(m => `
        <div style="margin-bottom: 5px;">
            <label style="display: flex; align-items: center; cursor: pointer; font-weight: normal;">
                <input type="checkbox" name="materiasSeleccionadas" value="${m.id}" style="margin-right: 10px; width: 18px; height: 18px;">
                ${m.codigo} - ${m.nombre}
            </label>
        </div>
    `).join('');
}


async function cargarProfesores() {
    const r = await fetchConAuth(`${API_URL}/api/profesores/activos`);
    if (!r || !r.ok) return;
    const profesores = await r.json();
    document.getElementById('profesorId').innerHTML = '<option value="">-- Seleccionar Profesor --</option>' +
        profesores.map(p => `<option value="${p.id}">${p.nombre} ${p.apellidoP} ${p.apellidoM}</option>`).join('');
}

async function cargarAsignaciones(pagina = 0) {
    const tbody = document.getElementById('tablaBody');
    tbody.innerHTML = '<tr><td colspan="7" style="text-align: center;">Cargando...</td></tr>';

    try {
        const url = `${API_URL}/api/asignaciones/paginado?page=${pagina}&size=10&sortBy=id&sortDir=asc`;
        const response = await fetchConAuth(url);
        if (!response) return;
        if (!response.ok) throw new Error('Error al cargar asignaciones');

        const data = await response.json();
        const asignaciones = data.content || [];

        paginaActual = data.page?.number ?? 0;
        totalPaginas = data.page?.totalPages ?? 0;

        if (asignaciones.length === 0) {
            tbody.innerHTML = '<tr><td colspan="7" style="text-align: center;">No hay asignaciones registradas</td></tr>';
        } else {
            tbody.innerHTML = asignaciones.map(a => `
                <tr>
                    <td>${a.id}</td>
                    <td>${a.grupoNombre ?? '-'}</td>
                    <td>${a.materiaNombre ?? '-'}</td>
                    <td>${a.profesorNombreCompleto ?? '-'}</td>
                    <td>${a.diaSemana ?? '-'}</td>
                    <td>${a.horaInicio ? a.horaInicio.substring(0, 5) : ''} - ${a.horaFin ? a.horaFin.substring(0, 5) : ''}</td>
                    <td>
                        <button class="btn btn-warning btn-sm" onclick="editarAsignacion(${a.id})">✏️ Editar</button>
                        <button class="btn btn-danger btn-sm" onclick="eliminarAsignacion(${a.id})">🗑️ Eliminar</button>
                    </td>
                </tr>
            `).join('');
        }

        document.getElementById('paginaActual').textContent = paginaActual + 1;
        document.getElementById('totalPaginas').textContent = totalPaginas;
        document.getElementById('btnAnterior').disabled = paginaActual === 0;
        document.getElementById('btnSiguiente').disabled = paginaActual >= totalPaginas - 1;

        ocultarBotonesSiNoTienePermiso('ADMIN,DIRECTOR,SECRETARIO');
    } catch (error) {
        console.error('Error:', error);
        tbody.innerHTML = `<tr><td colspan="7" style="text-align: center; color: red;">Error: ${error.message}</td></tr>`;
    }
}

async function abrirModal(asignacion = null) {
    const modal = document.getElementById('modal');
    const titulo = document.getElementById('modalTitulo');
    const form = document.getElementById('asignacionForm');
    form.reset();
    
    // Desmarcar todo primero
    document.querySelectorAll('input[name="materiasSeleccionadas"], input[name="diasSeleccionados"]').forEach(cb => {
        cb.checked = false;
        cb.disabled = false;
    });

    if (asignacion) {
        titulo.textContent = 'Editar Asignaciones del Profesor';
        document.getElementById('asignacionId').value = asignacion.id;
        document.getElementById('grupoId').value = asignacion.grupoId || '';
        document.getElementById('profesorId').value = asignacion.profesorId || '';
        document.getElementById('horaInicio').value = asignacion.horaInicio || '';
        document.getElementById('horaFin').value = asignacion.horaFin || '';
        
        // 🔥 TRAER TODAS LAS ASIGNACIONES DE ESTE PROFESOR EN ESTE GRUPO
        try {
            const res = await fetchConAuth(`${API_URL}/api/asignaciones/grupo/${asignacion.grupoId}`);
            if (res && res.ok) {
                const asignacionesGrupo = await res.json();
                // Filtrar solo las del profesor que estamos editando
                const delProfesor = asignacionesGrupo.filter(a => a.profesorId === asignacion.profesorId);
                
                // Marcar cada materia y día que ya tenga asignado
                delProfesor.forEach(a => {
                    const cbMateria = document.querySelector(`input[name="materiasSeleccionadas"][value="${a.materiaId}"]`);
                    if (cbMateria) cbMateria.checked = true;
                    
                    const cbDia = document.querySelector(`input[name="diasSeleccionados"][value="${a.diaSemana}"]`);
                    if (cbDia) cbDia.checked = true;
                });
            }
        } catch (error) {
            console.error("Error cargando asignaciones previas:", error);
        }

    } else {
        titulo.textContent = 'Nueva Asignación';
        document.getElementById('asignacionId').value = '';
    }
    modal.style.display = 'flex';
}

async function guardarAsignacion(e) {
    e.preventDefault();
    const id = document.getElementById('asignacionId').value;
    const grupoVal = document.getElementById('grupoId').value;
    const profesorVal = document.getElementById('profesorId').value;
    const horaInicio = document.getElementById('horaInicio').value || null;
    const horaFin = document.getElementById('horaFin').value || null;
    
    const materiasSeleccionadas = Array.from(document.querySelectorAll('input[name="materiasSeleccionadas"]:checked')).map(cb => parseInt(cb.value));
    const diasSeleccionados = Array.from(document.querySelectorAll('input[name="diasSeleccionados"]:checked')).map(cb => cb.value);
    
    if (!grupoVal || !profesorVal || materiasSeleccionadas.length === 0 || diasSeleccionados.length === 0 || !horaInicio || !horaFin) {
        alert('❌ Debes seleccionar un grupo, un profesor, al menos una materia, al menos un día y el horario.');
        return;
    }

    try {
        // Si estamos editando, primero borramos TODAS las asignaciones de este profesor en este grupo
        if (id) {
            const resExistentes = await fetchConAuth(`${API_URL}/api/asignaciones/grupo/${grupoVal}`);
            if (resExistentes && resExistentes.ok) {
                const existentes = await resExistentes.json();
                const delProfesor = existentes.filter(a => a.profesorId === parseInt(profesorVal));
                
                // Borramos una por una
                for (const a of delProfesor) {
                    await fetchConAuth(`${API_URL}/api/asignaciones/${a.id}`, { method: 'DELETE' });
                }
            }
        }

        // Ahora creamos todas las nuevas seleccionadas
        let exitos = 0;
        for (const materiaId of materiasSeleccionadas) {
            for (const dia of diasSeleccionados) {
                const asignacion = {
                    grupoId: parseInt(grupoVal),
                    materiaId: materiaId,
                    profesorId: parseInt(profesorVal),
                    diaSemana: dia,
                    horaInicio, horaFin,
                    activo: true
                };
                const response = await fetchConAuth(`${API_URL}/api/asignaciones`, { method: 'POST', body: JSON.stringify(asignacion) });
                if (response && response.ok) exitos++;
            }
        }
        
        mostrarMensaje(`✅ Se guardaron ${exitos} asignaciones correctamente.`);

        cerrarModal();
        cargarAsignaciones(paginaActual);
    } catch (error) {
        alert('❌ ' + error.message);
    }
}

async function editarAsignacion(id) {
    try {
        const response = await fetchConAuth(`${API_URL}/api/asignaciones/${id}`);
        if (!response || !response.ok) throw new Error('Asignación no encontrada');
        const asignacion = await response.json();
        abrirModal(asignacion);
    } catch (error) {
        alert('❌ ' + error.message);
    }
}

async function eliminarAsignacion(id) {
    if (!confirm('¿Eliminar esta asignación?')) return;
    try {
        const response = await fetchConAuth(`${API_URL}/api/asignaciones/${id}`, { method: 'DELETE' });
        if (!response || !response.ok) throw new Error('Error al eliminar');
        cargarAsignaciones(paginaActual);
        mostrarMensaje('✅ Asignación eliminada');
    } catch (error) {
        alert('❌ ' + error.message);
    }
}

function paginaAnterior() { if (paginaActual > 0) cargarAsignaciones(paginaActual - 1); }
function paginaSiguiente() { if (paginaActual < totalPaginas - 1) cargarAsignaciones(paginaActual + 1); }

async function buscar() {
    const termino = document.getElementById('busqueda').value.trim();
    if (!termino) { cargarAsignaciones(0); return; }

    const tbody = document.getElementById('tablaBody');
    tbody.innerHTML = '<tr><td colspan="7" style="text-align: center;">Buscando...</td></tr>';

    try {
        const url = `${API_URL}/api/asignaciones/buscar?termino=${encodeURIComponent(termino)}`;
        const response = await fetchConAuth(url);
        if (!response || !response.ok) throw new Error('Error al buscar');
        const asignaciones = await response.json();

        if (asignaciones.length === 0) {
            tbody.innerHTML = '<tr><td colspan="7" style="text-align: center;">No se encontraron resultados</td></tr>';
        } else {
            tbody.innerHTML = asignaciones.map(a => `
                <tr>
                    <td>${a.id}</td>
                    <td>${a.grupoNombre ?? '-'}</td>
                    <td>${a.materiaNombre ?? '-'} ${a.materiaCodigo ? '(' + a.materiaCodigo + ')' : ''}</td>
                    <td>${a.profesorNombreCompleto ?? '-'}</td>
                    <td>${a.diaSemana ?? '-'}</td>
                    <td>${a.horaInicio ? a.horaInicio.substring(0, 5) : ''} - ${a.horaFin ? a.horaFin.substring(0, 5) : ''}</td>
                    <td>
                        <button class="btn btn-warning btn-sm" onclick="editarAsignacion(${a.id})">✏️ Editar</button>
                        <button class="btn btn-danger btn-sm" onclick="eliminarAsignacion(${a.id})">🗑️ Eliminar</button>
                    </td>
                </tr>
            `).join('');
        }
        document.getElementById('paginaActual').textContent = '1';
        document.getElementById('totalPaginas').textContent = '1';
    } catch (error) {
        tbody.innerHTML = `<tr><td colspan="7" style="text-align: center; color: red;">Error: ${error.message}</td></tr>`;
    }
}

function limpiarBusqueda() {
    document.getElementById('busqueda').value = '';
    cargarAsignaciones(0);
}