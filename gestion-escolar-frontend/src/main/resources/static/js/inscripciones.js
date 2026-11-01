// ==========================================
// CONFIGURACIÓN
// ==========================================

let paginaActual = 0;
let totalPaginas = 0;
let busquedaActual = '';

// ==========================================
// CARGA INICIAL
// ==========================================

document.addEventListener('DOMContentLoaded', async () => {
    if (!verificarAutenticacion()) return;
    if (!verificarPermisoPagina()) return;   // 🎯 NUEVA LÍNEA
    ajustarMenuPorRol();                     // 🎯 NUEVA LÍNEA


    document.getElementById('usernameDisplay').textContent = getUsername();
    document.getElementById('rolDisplay').textContent = getRol();

    await cargarAlumnos();
    await cargarGrupos();
    cargarInscripciones();

    document.getElementById('inscripcionForm').addEventListener('submit', guardarInscripcion);
});

// ==========================================
// CARGAR DROPDOWNS
// ==========================================

async function cargarAlumnos() {
    try {
        const response = await fetchConAuth(`${API_URL}/api/alumnos/activos`);
        if (!response || !response.ok) return;
        const alumnos = await response.json();
        const select = document.getElementById('alumnoId');
        select.innerHTML = '<option value="">-- Seleccionar Alumno --</option>' +
            alumnos.map(a =>
                `<option value="${a.id}">${a.nombre} ${a.apellidoP} ${a.apellidoM} (${a.matricula})</option>`
            ).join('');
    } catch (error) {
        console.error('Error cargando alumnos:', error);
    }
}

async function cargarGrupos() {
    try {
        const response = await fetchConAuth(`${API_URL}/api/grupos/activos`);
        if (!response || !response.ok) return;
        const grupos = await response.json();
        const select = document.getElementById('grupoId');
        select.innerHTML = '<option value="">-- Seleccionar Grupo --</option>' +
            grupos.map(g =>
                `<option value="${g.id}">${g.nombre} (${g.inscritos}/${g.capacidad}) - ${g.cicloNombre || ''}</option>`
            ).join('');
    } catch (error) {
        console.error('Error cargando grupos:', error);
    }
}

// ==========================================
// CARGAR INSCRIPCIONES
// ==========================================

async function cargarInscripciones(pagina = 0) {
    const tbody = document.getElementById('tablaBody');
    tbody.innerHTML = '<tr><td colspan="9" style="text-align: center;">Cargando...</td></tr>';

    try {
        const size = 10;
        const url = `${API_URL}/api/inscripciones/paginado?page=${pagina}&size=${size}&sortBy=id&sortDir=asc`;

        const response = await fetchConAuth(url);
        if (!response) return;

        if (!response.ok) throw new Error('Error al cargar inscripciones');

        const data = await response.json();
        const inscripciones = data.content || [];

        paginaActual = data.page?.number ?? 0;
        totalPaginas = data.page?.totalPages ?? 0;

        if (inscripciones.length === 0) {
            tbody.innerHTML = '<tr><td colspan="9" style="text-align: center;">No hay inscripciones registradas</td></tr>';
        } else {
            tbody.innerHTML = inscripciones.map(i => `
                <tr>
                    <td>${i.id}</td>
                    <td>${i.alumnoNombreCompleto ?? '-'}</td>
                    <td>${i.alumnoMatricula ?? '-'}</td>
                    <td>${i.grupoNombre ?? '-'}</td>
                    <td>${i.grupoGrado ?? '-'}</td>
                    <td>${i.grupoTurno ?? '-'}</td>
                    <td>${i.cicloNombre ?? '-'}</td>
                    <td>${i.estatus ?? '-'}</td>
                    <td>
                        <button class="btn btn-warning btn-sm" onclick="editarInscripcion(${i.id})">✏️ Editar</button>
                        <button class="btn btn-danger btn-sm" onclick="eliminarInscripcion(${i.id})">🗑️ Eliminar</button>
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
        tbody.innerHTML = `<tr><td colspan="9" style="text-align: center; color: red;">Error: ${error.message}</td></tr>`;
    }
}

// ==========================================
// MODAL
// ==========================================

function abrirModal(inscripcion = null) {
    const modal = document.getElementById('modal');
    const titulo = document.getElementById('modalTitulo');
    const form = document.getElementById('inscripcionForm');

    form.reset();

    if (inscripcion) {
        titulo.textContent = 'Editar Inscripción';
        document.getElementById('inscripcionId').value = inscripcion.id;
        document.getElementById('alumnoId').value = inscripcion.alumnoId || '';
        document.getElementById('grupoId').value = inscripcion.grupoId || '';
        document.getElementById('fechaInscripcion').value = inscripcion.fechaInscripcion || '';
        document.getElementById('estatus').value = inscripcion.estatus || 'ACTIVA';
    } else {
        titulo.textContent = 'Nueva Inscripción';
        document.getElementById('inscripcionId').value = '';
        document.getElementById('estatus').value = 'ACTIVA';
    }

    modal.style.display = 'flex';
}

function cerrarModal() {
    document.getElementById('modal').style.display = 'none';
}

// ==========================================
// GUARDAR
// ==========================================

async function guardarInscripcion(e) {
    e.preventDefault();

    const id = document.getElementById('inscripcionId').value;
    const alumnoIdRaw = document.getElementById('alumnoId').value;
    const grupoIdRaw = document.getElementById('grupoId').value;
    const fechaInscripcion = document.getElementById('fechaInscripcion').value;
    const estatus = document.getElementById('estatus').value;

    const errores = [];
    if (!alumnoIdRaw) errores.push('Debes seleccionar un alumno');
    if (!grupoIdRaw) errores.push('Debes seleccionar un grupo');

    if (errores.length > 0) {
        alert('❌ Errores:\n\n' + errores.join('\n'));
        return;
    }

    const inscripcion = {
        alumnoId: parseInt(alumnoIdRaw),
        grupoId: parseInt(grupoIdRaw),
        fechaInscripcion: fechaInscripcion || null,
        estatus: estatus || 'ACTIVA'
    };

    console.log('📤 Enviando inscripción:', JSON.stringify(inscripcion, null, 2));

    try {
        let url = `${API_URL}/api/inscripciones`;
        let method = 'POST';

        if (id) {
            url += `/${id}`;
            method = 'PUT';
        }

        const response = await fetchConAuth(url, {
            method,
            body: JSON.stringify(inscripcion)
        });

        if (!response) return;

        if (!response.ok) {
            const errorText = await response.text();
            console.error('❌ Error del backend:', errorText);

            try {
                const errorJson = JSON.parse(errorText);

                if (errorJson.validationErrors && typeof errorJson.validationErrors === 'object') {
                    const detalle = Object.entries(errorJson.validationErrors)
                        .map(([campo, msg]) => `  • ${campo}: ${msg}`)
                        .join('\n');
                    throw new Error(`Error de validación:\n\n${detalle}`);
                }

                throw new Error(errorJson.message || 'Error al guardar');
            } catch (e) {
                if (e.message.includes('Error de validación')) throw e;
                throw new Error('Error ' + response.status + ': ' + errorText);
            }
        }

        cerrarModal();
        cargarInscripciones(paginaActual);
        mostrarMensaje(id ? '✅ Inscripción actualizada' : '✅ Inscripción creada');

    } catch (error) {
        alert('❌ ' + error.message);
    }
}

// ==========================================
// EDITAR
// ==========================================

async function editarInscripcion(id) {
    try {
        const response = await fetchConAuth(`${API_URL}/api/inscripciones/${id}`);
        if (!response) return;
        if (!response.ok) throw new Error('Inscripción no encontrada');
        const inscripcion = await response.json();
        abrirModal(inscripcion);
    } catch (error) {
        alert('❌ ' + error.message);
    }
}

// ==========================================
// ELIMINAR
// ==========================================

async function eliminarInscripcion(id) {
    if (!confirm('¿Eliminar esta inscripción?')) return;

    try {
        const response = await fetchConAuth(`${API_URL}/api/inscripciones/${id}`, {
            method: 'DELETE'
        });

        if (!response) return;
        if (!response.ok) throw new Error('Error al eliminar');

        cargarInscripciones(paginaActual);
        mostrarMensaje('✅ Inscripción eliminada');
    } catch (error) {
        alert('❌ ' + error.message);
    }
}

// ==========================================
// PAGINACIÓN
// ==========================================

function paginaAnterior() {
    if (paginaActual > 0) cargarInscripciones(paginaActual - 1);
}

function paginaSiguiente() {
    if (paginaActual < totalPaginas - 1) cargarInscripciones(paginaActual + 1);
}

// ==========================================
// BÚSQUEDA
// ==========================================

async function buscar() {
    const termino = document.getElementById('busqueda').value.trim();
    if (!termino) {
        cargarInscripciones(0);
        return;
    }

    busquedaActual = termino;
    const tbody = document.getElementById('tablaBody');
    tbody.innerHTML = '<tr><td colspan="9" style="text-align: center;">Buscando...</td></tr>';

    try {
        const primeraPalabra = termino.split(/\s+/)[0];
        const url = `${API_URL}/api/inscripciones/buscar?termino=${encodeURIComponent(primeraPalabra)}`;
        const response = await fetchConAuth(url);
        if (!response) return;
        if (!response.ok) throw new Error(`Error ${response.status} al buscar`);

        const inscripciones = await response.json();

        if (!inscripciones || inscripciones.length === 0) {
            tbody.innerHTML = '<tr><td colspan="9" style="text-align: center;">No se encontraron resultados</td></tr>';
        } else {
            tbody.innerHTML = inscripciones.map(i => `
                <tr>
                    <td>${i.id}</td>
                    <td>${i.alumnoNombreCompleto ?? '-'}</td>
                    <td>${i.alumnoMatricula ?? '-'}</td>
                    <td>${i.grupoNombre ?? '-'}</td>
                    <td>${i.grupoGrado ?? '-'}</td>
                    <td>${i.grupoTurno ?? '-'}</td>
                    <td>${i.cicloNombre ?? '-'}</td>
                    <td>${i.estatus ?? '-'}</td>
                    <td>
                        <button class="btn btn-warning btn-sm" onclick="editarInscripcion(${i.id})">✏️ Editar</button>
                        <button class="btn btn-danger btn-sm" onclick="eliminarInscripcion(${i.id})">🗑️ Eliminar</button>
                    </td>
                </tr>
            `).join('');
        }

        document.getElementById('paginaActual').textContent = '1';
        document.getElementById('totalPaginas').textContent = '1';
    } catch (error) {
        console.error('Error:', error);
        tbody.innerHTML = `<tr><td colspan="9" style="text-align: center; color: red;">Error: ${error.message}</td></tr>`;
    }
}

function limpiarBusqueda() {
    document.getElementById('busqueda').value = '';
    busquedaActual = '';
    cargarInscripciones(0);
}