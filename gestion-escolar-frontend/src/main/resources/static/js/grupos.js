let paginaActual = 0;
let totalPaginas = 0;
let busquedaActual = '';
let ciclosCache = [];

document.addEventListener('DOMContentLoaded', async () => {
    if (!verificarAutenticacion()) return;
    if (!verificarPermisoPagina()) return;   // 🎯 NUEVA LÍNEA
    ajustarMenuPorRol();                     // 🎯 NUEVA LÍNEA


    document.getElementById('usernameDisplay').textContent = getUsername();
    document.getElementById('rolDisplay').textContent = getRol();

    await cargarCiclos();
    cargarGrupos();

    document.getElementById('grupoForm').addEventListener('submit', guardarGrupo);
});

async function cargarCiclos() {
    try {
        const response = await fetchConAuth(`${API_URL}/api/ciclos/activos`);
        if (!response || !response.ok) return;
        ciclosCache = await response.json();
        const select = document.getElementById('cicloId');
        select.innerHTML = '<option value="">-- Seleccionar Ciclo --</option>' +
            ciclosCache.map(c => `<option value="${c.id}">${c.nombre}</option>`).join('');
    } catch (error) {
        console.error('Error cargando ciclos:', error);
    }
}

async function cargarGrupos(pagina = 0) {
    const tbody = document.getElementById('tablaBody');
    tbody.innerHTML = '<tr><td colspan="9" style="text-align: center;">Cargando...</td></tr>';

    try {
        const url = `${API_URL}/api/grupos/paginado?page=${pagina}&size=10&sortBy=id&sortDir=asc`;
        const response = await fetchConAuth(url);
        if (!response) return;
        if (!response.ok) throw new Error('Error al cargar grupos');

        const data = await response.json();
        const grupos = data.content || [];

        paginaActual = data.page?.number ?? 0;
        totalPaginas = data.page?.totalPages ?? 0;

        if (grupos.length === 0) {
            tbody.innerHTML = '<tr><td colspan="9" style="text-align: center;">No hay grupos registrados</td></tr>';
        } else {
            tbody.innerHTML = grupos.map(g => `
                <tr>
                    <td>${g.id}</td>
                    <td><strong>${g.nombre}</strong></td>
                    <td>${g.grado ?? '-'}</td>
                    <td>${g.letra ?? '-'}</td>
                    <td>${g.turno ?? '-'}</td>
                    <td>${g.inscritos ?? 0}</td>
                    <td>${g.capacidad ?? '-'}</td>
                    <td>${g.cicloNombre ?? '-'}</td>
                    <td>
					
						<button class="btn btn-info btn-sm" onclick="verGrupo(${g.id})">📋 Listar Grupo</button>
                        <button class="btn btn-warning btn-sm" onclick="editarGrupo(${g.id})">✏️ Editar</button>
                        <button class="btn btn-danger btn-sm" onclick="eliminarGrupo(${g.id})">🗑️ Eliminar</button>
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

function abrirModal(grupo = null) {
    const modal = document.getElementById('modal');
    const titulo = document.getElementById('modalTitulo');
    const form = document.getElementById('grupoForm');
    form.reset();

    if (grupo) {
        titulo.textContent = 'Editar Grupo';
        document.getElementById('grupoId').value = grupo.id;
        document.getElementById('grado').value = grupo.grado || '';
        document.getElementById('letra').value = grupo.letra || '';
        document.getElementById('turno').value = grupo.turno || '';
        document.getElementById('capacidad').value = grupo.capacidad || 30;
        document.getElementById('cicloId').value = grupo.cicloId || '';
    } else {
        titulo.textContent = 'Nuevo Grupo';
        document.getElementById('grupoId').value = '';
        document.getElementById('capacidad').value = 30;
    }
    modal.style.display = 'flex';
}

function cerrarModal() {
    document.getElementById('modal').style.display = 'none';
}

async function guardarGrupo(e) {
    e.preventDefault();
    const id = document.getElementById('grupoId').value;

    const grupo = {
        grado: parseInt(document.getElementById('grado').value),
        letra: document.getElementById('letra').value.trim().toUpperCase(),
        turno: document.getElementById('turno').value,
        capacidad: parseInt(document.getElementById('capacidad').value),
        cicloId: parseInt(document.getElementById('cicloId').value),
        activo: true
    };

    const errores = [];
    if (!grupo.grado) errores.push('El grado es obligatorio');
    if (!grupo.letra) errores.push('La letra es obligatoria');
    if (!grupo.turno) errores.push('El turno es obligatorio');
    if (!grupo.cicloId) errores.push('El ciclo es obligatorio');

    if (errores.length > 0) {
        alert('❌ Errores:\n\n' + errores.join('\n'));
        return;
    }

    try {
        let url = `${API_URL}/api/grupos`;
        let method = 'POST';
        if (id) { url += `/${id}`; method = 'PUT'; }

        const response = await fetchConAuth(url, { method, body: JSON.stringify(grupo) });
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
        cargarGrupos(paginaActual);
        mostrarMensaje(id ? '✅ Grupo actualizado' : '✅ Grupo creado');
    } catch (error) {
        alert('❌ ' + error.message);
    }
}

async function editarGrupo(id) {
    try {
        const response = await fetchConAuth(`${API_URL}/api/grupos/${id}`);
        if (!response || !response.ok) throw new Error('Grupo no encontrado');
        const grupo = await response.json();
        abrirModal(grupo);
    } catch (error) {
        alert('❌ ' + error.message);
    }
}

async function eliminarGrupo(id) {
    if (!confirm('¿Eliminar este grupo?')) return;
    try {
        const response = await fetchConAuth(`${API_URL}/api/grupos/${id}`, { method: 'DELETE' });
        if (!response || !response.ok) throw new Error('Error al eliminar');
        cargarGrupos(paginaActual);
        mostrarMensaje('✅ Grupo eliminado');
    } catch (error) {
        alert('❌ ' + error.message);
    }
}

function paginaAnterior() { if (paginaActual > 0) cargarGrupos(paginaActual - 1); }
function paginaSiguiente() { if (paginaActual < totalPaginas - 1) cargarGrupos(paginaActual + 1); }

async function buscar() {
    const termino = document.getElementById('busqueda').value.trim();
    if (!termino) { cargarGrupos(0); return; }

    const tbody = document.getElementById('tablaBody');
    tbody.innerHTML = '<tr><td colspan="9" style="text-align: center;">Buscando...</td></tr>';

    try {
        const url = `${API_URL}/api/grupos/buscar?termino=${encodeURIComponent(termino)}`;
        const response = await fetchConAuth(url);
        if (!response || !response.ok) throw new Error('Error al buscar');

        const grupos = await response.json();
        if (grupos.length === 0) {
            tbody.innerHTML = '<tr><td colspan="9" style="text-align: center;">No se encontraron resultados</td></tr>';
        } else {
            tbody.innerHTML = grupos.map(g => `
                <tr>
                    <td>${g.id}</td>
                    <td><strong>${g.nombre}</strong></td>
                    <td>${g.grado ?? '-'}</td>
                    <td>${g.letra ?? '-'}</td>
                    <td>${g.turno ?? '-'}</td>
                    <td>${g.inscritos ?? 0}</td>
                    <td>${g.capacidad ?? '-'}</td>
                    <td>${g.cicloNombre ?? '-'}</td>
                    <td>
                        <button class="btn btn-warning btn-sm" onclick="editarGrupo(${g.id})">✏️ Editar</button>
                        <button class="btn btn-danger btn-sm" onclick="eliminarGrupo(${g.id})">🗑️ Eliminar</button>


                    </td>
                </tr>
            `).join('');
        }
        document.getElementById('paginaActual').textContent = '1';
        document.getElementById('totalPaginas').textContent = '1';
        // 🎯 OCULTAR BOTONES SI NO TIENE PERMISO (NUEVA LÍNEA)
        ocultarBotonesSiNoTienePermiso('ADMIN,DIRECTOR,SECRETARIO');
    } catch (error) {
        tbody.innerHTML = `<tr><td colspan="9" style="text-align: center; color: red;">Error: ${error.message}</td></tr>`;
    }
}


function verGrupo(id) {
    // Redirigir a la pantalla de detalle con el ID del grupo
    window.location.href = `grupo-detalle.html?id=${id}`;
}

function limpiarBusqueda() {
    document.getElementById('busqueda').value = '';
    cargarGrupos(0);
}