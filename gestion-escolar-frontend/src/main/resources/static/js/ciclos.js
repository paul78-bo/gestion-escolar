// ==========================================
// CONFIGURACIÓN
// ==========================================

let paginaActual = 0;
let totalPaginas = 0;
let busquedaActual = '';

// ==========================================
// CARGA INICIAL
// ==========================================

document.addEventListener('DOMContentLoaded', () => {
    if (!verificarAutenticacion()) return;
    if (!verificarPermisoPagina()) return;   // 🎯 NUEVA LÍNEA
    ajustarMenuPorRol();                     // 🎯 NUEVA LÍNEA


    document.getElementById('usernameDisplay').textContent = getUsername();
    document.getElementById('rolDisplay').textContent = getRol();

    cargarCiclos();

    document.getElementById('cicloForm').addEventListener('submit', guardarCiclo);
});

// ==========================================
// CARGAR CICLOS
// ==========================================

async function cargarCiclos(pagina = 0) {
    const tbody = document.getElementById('tablaBody');
    tbody.innerHTML = '<tr><td colspan="8" style="text-align: center;">Cargando...</td></tr>';

    try {
        const size = 10;
        const url = `${API_URL}/api/ciclos/paginado?page=${pagina}&size=${size}&sortBy=id&sortDir=desc`;

        const response = await fetchConAuth(url);
        if (!response) return;

        if (!response.ok) throw new Error('Error al cargar ciclos');

        const data = await response.json();
        const ciclos = data.content || [];

        paginaActual = data.page?.number ?? 0;
        totalPaginas = data.page?.totalPages ?? 0;

        if (ciclos.length === 0) {
            tbody.innerHTML = '<tr><td colspan="8" style="text-align: center;">No hay ciclos registrados</td></tr>';
        } else {
            tbody.innerHTML = ciclos.map(c => `
                <tr>
                    <td>${c.id}</td>
                    <td><strong>${c.nombre}</strong></td>
                    <td>${c.nivelEducativo ?? '-'}</td>
                    <td>${c.tipoPeriodo ?? '-'}</td>
                    <td>${c.fechaInicio ?? '-'}</td>
                    <td>${c.fechaFin ?? '-'}</td>
                    <td>
                        <span style="padding: 4px 8px; border-radius: 4px; font-size: 12px; 
                                     background: ${c.activo ? '#27ae60' : '#95a5a6'}; color: white;">
                            ${c.activo ? 'ACTIVO' : 'INACTIVO'}
                        </span>
                    </td>
                    <td>
                        <button class="btn btn-warning btn-sm" onclick="editarCiclo(${c.id})">✏️ Editar</button>
                        <button class="btn btn-danger btn-sm" onclick="eliminarCiclo(${c.id})">🗑️ Eliminar</button>
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
        tbody.innerHTML = `<tr><td colspan="8" style="text-align: center; color: red;">Error: ${error.message}</td></tr>`;
    }
}

// ==========================================
// MODAL
// ==========================================

function abrirModal(ciclo = null) {
    const modal = document.getElementById('modal');
    const titulo = document.getElementById('modalTitulo');
    const form = document.getElementById('cicloForm');

    form.reset();

    if (ciclo) {
        titulo.textContent = 'Editar Ciclo Escolar';
        document.getElementById('cicloId').value = ciclo.id;
        document.getElementById('nombre').value = ciclo.nombre || '';
        document.getElementById('nivelEducativo').value = ciclo.nivelEducativo || 'TELESECUNDARIA';
        document.getElementById('fechaInicio').value = ciclo.fechaInicio || '';
        document.getElementById('fechaFin').value = ciclo.fechaFin || '';
        document.getElementById('tipoPeriodo').value = ciclo.tipoPeriodo || 'ANUAL';
        document.getElementById('activo').value = ciclo.activo !== false ? 'true' : 'false';
        document.getElementById('descripcion').value = ciclo.descripcion || '';
        document.getElementById('observaciones').value = ciclo.observaciones || '';
    } else {
        titulo.textContent = 'Nuevo Ciclo Escolar';
        document.getElementById('cicloId').value = '';
        document.getElementById('nivelEducativo').value = 'TELESECUNDARIA';
        document.getElementById('tipoPeriodo').value = 'ANUAL';
        document.getElementById('activo').value = 'true';
    }

    modal.style.display = 'flex';
}

function cerrarModal() {
    document.getElementById('modal').style.display = 'none';
}

// ==========================================
// GUARDAR
// ==========================================

async function guardarCiclo(e) {
    e.preventDefault();

    const id = document.getElementById('cicloId').value;

    const ciclo = {
        nombre: document.getElementById('nombre').value.trim(),
        fechaInicio: document.getElementById('fechaInicio').value,
        fechaFin: document.getElementById('fechaFin').value,
        nivelEducativo: document.getElementById('nivelEducativo').value,
        tipoPeriodo: document.getElementById('tipoPeriodo').value,
        descripcion: document.getElementById('descripcion').value.trim() || null,
        observaciones: document.getElementById('observaciones').value.trim() || null,
        activo: document.getElementById('activo').value === 'true'
    };

    // Validaciones
    const errores = [];
    if (!ciclo.nombre) errores.push('El nombre es obligatorio');
    if (!/^\d{4}-\d{4}$/.test(ciclo.nombre)) errores.push('El nombre debe tener formato YYYY-YYYY');
    if (!ciclo.fechaInicio) errores.push('La fecha de inicio es obligatoria');
    if (!ciclo.fechaFin) errores.push('La fecha de fin es obligatoria');
    if (ciclo.fechaInicio && ciclo.fechaFin && ciclo.fechaInicio >= ciclo.fechaFin) {
        errores.push('La fecha de fin debe ser posterior a la fecha de inicio');
    }

    if (errores.length > 0) {
        alert('❌ Errores:\n\n' + errores.join('\n'));
        return;
    }

    console.log('📤 Enviando ciclo:', JSON.stringify(ciclo, null, 2));

    try {
        let url = `${API_URL}/api/ciclos`;
        let method = 'POST';

        if (id) {
            url += `/${id}`;
            method = 'PUT';
        }

        const response = await fetchConAuth(url, {
            method,
            body: JSON.stringify(ciclo)
        });

        if (!response) return;

        if (!response.ok) {
            const errorText = await response.text();
            console.error('❌ Error del backend:', errorText);

            try {
                const errorJson = JSON.parse(errorText);
                if (errorJson.validationErrors) {
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
        cargarCiclos(paginaActual);
        mostrarMensaje(id ? '✅ Ciclo actualizado' : '✅ Ciclo creado');

    } catch (error) {
        alert('❌ ' + error.message);
    }
}

// ==========================================
// EDITAR
// ==========================================

async function editarCiclo(id) {
    try {
        const response = await fetchConAuth(`${API_URL}/api/ciclos/${id}`);
        if (!response) return;
        if (!response.ok) throw new Error('Ciclo no encontrado');
        const ciclo = await response.json();
        abrirModal(ciclo);
    } catch (error) {
        alert('❌ ' + error.message);
    }
}

// ==========================================
// ELIMINAR
// ==========================================

async function eliminarCiclo(id) {
    if (!confirm('¿Eliminar este ciclo escolar? (Se marcará como inactivo)')) return;

    try {
        const response = await fetchConAuth(`${API_URL}/api/ciclos/${id}`, {
            method: 'DELETE'
        });

        if (!response) return;
        if (!response.ok) throw new Error('Error al eliminar');

        cargarCiclos(paginaActual);
        mostrarMensaje('✅ Ciclo eliminado');
    } catch (error) {
        alert('❌ ' + error.message);
    }
}

// ==========================================
// PAGINACIÓN
// ==========================================

function paginaAnterior() {
    if (paginaActual > 0) cargarCiclos(paginaActual - 1);
}

function paginaSiguiente() {
    if (paginaActual < totalPaginas - 1) cargarCiclos(paginaActual + 1);
}

// ==========================================
// BÚSQUEDA
// ==========================================

async function buscar() {
    const termino = document.getElementById('busqueda').value.trim();
    if (!termino) {
        cargarCiclos(0);
        return;
    }

    busquedaActual = termino;
    const tbody = document.getElementById('tablaBody');
    tbody.innerHTML = '<tr><td colspan="8" style="text-align: center;">Buscando...</td></tr>';

    try {
        const url = `${API_URL}/api/ciclos/buscar?termino=${encodeURIComponent(termino)}`;
        const response = await fetchConAuth(url);
        if (!response) return;
        if (!response.ok) throw new Error(`Error ${response.status} al buscar`);

        const ciclos = await response.json();

        if (!ciclos || ciclos.length === 0) {
            tbody.innerHTML = '<tr><td colspan="8" style="text-align: center;">No se encontraron resultados</td></tr>';
        } else {
            tbody.innerHTML = ciclos.map(c => `
                <tr>
                    <td>${c.id}</td>
                    <td><strong>${c.nombre}</strong></td>
                    <td>${c.nivelEducativo ?? '-'}</td>
                    <td>${c.tipoPeriodo ?? '-'}</td>
                    <td>${c.fechaInicio ?? '-'}</td>
                    <td>${c.fechaFin ?? '-'}</td>
                    <td>
                        <span style="padding: 4px 8px; border-radius: 4px; font-size: 12px; 
                                     background: ${c.activo ? '#27ae60' : '#95a5a6'}; color: white;">
                            ${c.activo ? 'ACTIVO' : 'INACTIVO'}
                        </span>
                    </td>
                    <td>
                        <button class="btn btn-warning btn-sm" onclick="editarCiclo(${c.id})">✏️ Editar</button>
                        <button class="btn btn-danger btn-sm" onclick="eliminarCiclo(${c.id})">🗑️ Eliminar</button>
                    </td>
                </tr>
            `).join('');
        }

        document.getElementById('paginaActual').textContent = '1';
        document.getElementById('totalPaginas').textContent = '1';
        // 🎯 OCULTAR BOTONES SI NO TIENE PERMISO (NUEVA LÍNEA)
        ocultarBotonesSiNoTienePermiso('ROLES_PERMITIDOS');
    } catch (error) {
        console.error('Error:', error);
        tbody.innerHTML = `<tr><td colspan="8" style="text-align: center; color: red;">Error: ${error.message}</td></tr>`;
    }
}

function limpiarBusqueda() {
    document.getElementById('busqueda').value = '';
    busquedaActual = '';
    cargarCiclos(0);
}