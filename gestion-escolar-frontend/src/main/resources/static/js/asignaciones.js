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
    document.getElementById('materiaId').innerHTML = '<option value="">-- Seleccionar Materia --</option>' +
        materias.map(m => `<option value="${m.id}">${m.codigo} - ${m.nombre}</option>`).join('');
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

function abrirModal(asignacion = null) {
    const modal = document.getElementById('modal');
    const titulo = document.getElementById('modalTitulo');
    const form = document.getElementById('asignacionForm');
    form.reset();

    if (asignacion) {
        titulo.textContent = 'Editar Asignación';
        document.getElementById('asignacionId').value = asignacion.id;
        document.getElementById('grupoId').value = asignacion.grupoId || '';
        document.getElementById('materiaId').value = asignacion.materiaId || '';
        document.getElementById('profesorId').value = asignacion.profesorId || '';
        document.getElementById('diaSemana').value = asignacion.diaSemana || '';
        document.getElementById('horaInicio').value = asignacion.horaInicio || '';
        document.getElementById('horaFin').value = asignacion.horaFin || '';
    } else {
        titulo.textContent = 'Nueva Asignación';
        document.getElementById('asignacionId').value = '';
    }
    modal.style.display = 'flex';
}

function cerrarModal() {
    document.getElementById('modal').style.display = 'none';
}

async function guardarAsignacion(e) {
    e.preventDefault();
    const id = document.getElementById('asignacionId').value;

    const asignacion = {
        grupoId: parseInt(document.getElementById('grupoId').value),
        materiaId: parseInt(document.getElementById('materiaId').value),
        profesorId: parseInt(document.getElementById('profesorId').value),
        diaSemana: document.getElementById('diaSemana').value || null,
        horaInicio: document.getElementById('horaInicio').value || null,
        horaFin: document.getElementById('horaFin').value || null,
        activo: true
    };

    const errores = [];
    if (!asignacion.grupoId) errores.push('El grupo es obligatorio');
    if (!asignacion.materiaId) errores.push('La materia es obligatoria');
    if (!asignacion.profesorId) errores.push('El profesor es obligatorio');
    if (!asignacion.diaSemana) errores.push('El día es obligatorio');
    if (!asignacion.horaInicio || !asignacion.horaFin) errores.push('El horario es obligatorio');
    
    if (errores.length > 0) {
        alert('❌ Errores:\n\n' + errores.join('\n'));
        return;
    }

    try {
        let url = `${API_URL}/api/asignaciones`;
        let method = 'POST';
        if (id) { url += `/${id}`; method = 'PUT'; }

        const response = await fetchConAuth(url, { method, body: JSON.stringify(asignacion) });
        if (!response) return;

        if (!response.ok) {
            const errorText = await response.text();
            try {
                const errorJson = JSON.parse(errorText);
                if (errorJson.validationErrors) {
                    const detalle = Object.entries(errorJson.validationErrors)
                        .map(([campo, msg]) => `  • ${campo}: ${msg}`).join('\n');
                    throw new Error(`Error de validación:\n\n${detalle}`);
                }
                throw new Error(errorJson.message || 'Error al guardar');
            } catch (e) {
                if (e.message.includes('Error de validación')) throw e;
                throw new Error('Error ' + response.status + ': ' + errorText);
            }
        }

        cerrarModal();
        cargarAsignaciones(paginaActual);
        mostrarMensaje(id ? '✅ Asignación actualizada' : '✅ Asignación creada');
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
            // 👇 AQUÍ ESTABA EL ERROR, AHORA YA PINTA DÍA Y HORARIO
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