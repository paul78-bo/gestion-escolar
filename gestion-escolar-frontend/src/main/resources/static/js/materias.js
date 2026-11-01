let paginaActual = 0;
let totalPaginas = 0;
let busquedaActual = '';

document.addEventListener('DOMContentLoaded', () => {
    if (document.getElementById('tablaBody')) {
        cargarMaterias();
    }
    const form = document.getElementById('materiaForm');
    if (form) {
        form.addEventListener('submit', guardarMateria);
    }
});

function formatearCampo(campo) {
    if (!campo) return '-';
    return campo.replace(/_/g, ' ').toLowerCase().replace(/\b\w/g, l => l.toUpperCase());
}

async function cargarMaterias(pagina = 0) {
    const tbody = document.getElementById('tablaBody');
    if (!tbody) return;
    
    tbody.innerHTML = '<tr><td colspan="6" style="text-align: center;">Cargando...</td></tr>';

    try {
        const size = 10;
        const url = `${API_URL}/api/materias/paginado?page=${pagina}&size=${size}&sortBy=id&sortDir=asc`;

        const response = await fetchConAuth(url);
        if (!response) return;
        if (!response.ok) throw new Error('Error al cargar materias');

        const data = await response.json();
        const materias = data.content || [];

        paginaActual = data.page?.number ?? 0;
        totalPaginas = data.page?.totalPages ?? 0;

        if (materias.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" style="text-align: center;">No hay materias registradas</td></tr>';
        } else {
            tbody.innerHTML = materias.map(m => `
                <tr>
                    <td>${m.id}</td>
                    <td>${m.nombre}</td>
                    <td>${m.codigo}</td>
                    <td>${formatearCampo(m.campo)}</td>
                    <td>${m.descripcion || '-'}</td>
                    <td>
                        <button class="btn btn-warning btn-sm" onclick="editarMateria(${m.id})">✏️ Editar</button>
                        <button class="btn btn-danger btn-sm" onclick="eliminarMateria(${m.id})">🗑️ Eliminar</button>
                    </td>
                </tr>
            `).join('');
        }

        if (document.getElementById('paginaActual')) document.getElementById('paginaActual').textContent = paginaActual + 1;
        if (document.getElementById('totalPaginas')) document.getElementById('totalPaginas').textContent = totalPaginas;
        if (document.getElementById('btnAnterior')) document.getElementById('btnAnterior').disabled = paginaActual === 0;
        if (document.getElementById('btnSiguiente')) document.getElementById('btnSiguiente').disabled = paginaActual >= totalPaginas - 1;

        if (typeof ocultarBotonesSiNoTienePermiso === 'function') {
            ocultarBotonesSiNoTienePermiso('ADMIN,DIRECTOR,SECRETARIO');
        }

    } catch (error) {
        console.error('Error:', error);
        tbody.innerHTML = `<tr><td colspan="6" style="text-align: center; color: red;">Error: ${error.message}</td></tr>`;
    }
}

function abrirModal(materia = null) {
    const modal = document.getElementById('modal');
    const form = document.getElementById('materiaForm');
    if (!modal || !form) return;

    form.reset();

    if (materia) {
        document.getElementById('modalTitulo').textContent = 'Editar Materia';
        document.getElementById('materiaId').value = materia.id;
        document.getElementById('nombre').value = materia.nombre || '';
        document.getElementById('codigo').value = materia.codigo || '';
        document.getElementById('campo').value = materia.campo || '';
        document.getElementById('descripcion').value = materia.descripcion || '';
    } else {
        document.getElementById('modalTitulo').textContent = 'Nueva Materia';
        document.getElementById('materiaId').value = '';
    }

    modal.style.display = 'flex';
}

function cerrarModal() {
    document.getElementById('modal').style.display = 'none';
}

async function guardarMateria(e) {
    e.preventDefault();

    const id = document.getElementById('materiaId').value;

    const nombre = document.getElementById('nombre').value.trim();
    const codigo = document.getElementById('codigo').value.trim();
    const campo = document.getElementById('campo').value;
    const descripcion = document.getElementById('descripcion').value.trim();

    const errores = [];
    if (!nombre) errores.push('El nombre es obligatorio');
    if (!codigo) errores.push('El código es obligatorio');
    if (!campo) errores.push('El Campo Formativo es obligatorio');
    if (descripcion && descripcion.length > 500) errores.push('La descripción es muy larga');

    if (errores.length > 0) {
        alert('❌ Errores:\n\n' + errores.join('\n'));
        return;
    }

    // Enviamos 0 o null en creditos y horasSemana porque ya no se piden en el form
    const materia = {
        nombre: nombre,
        codigo: codigo,
        campo: campo,
        descripcion: descripcion ? descripcion : null,
        creditos: null, 
        horasSemana: null,
        activo: true
    };

    try {
        let url = `${API_URL}/api/materias`;
        let method = 'POST';

        if (id) {
            url += `/${id}`;
            method = 'PUT';
        }

        const response = await fetchConAuth(url, {
            method,
            body: JSON.stringify(materia)
        });

        if (!response) return;

        if (!response.ok) {
            const errorText = await response.text();
            try {
                const errorJson = JSON.parse(errorText);
                throw new Error(errorJson.message || 'Error al guardar la materia');
            } catch (err) {
                throw new Error('Error ' + response.status + ': ' + errorText);
            }
        }

        cerrarModal();
        cargarMaterias(paginaActual);
        if (typeof mostrarMensaje === 'function') {
            mostrarMensaje(id ? '✅ Materia actualizada' : '✅ Materia creada');
        } else {
            alert(id ? '✅ Materia actualizada' : '✅ Materia creada');
        }

    } catch (error) {
        alert('❌ ' + error.message);
    }
}

async function editarMateria(id) {
    try {
        const response = await fetchConAuth(`${API_URL}/api/materias/${id}`);
        if (!response) return;
        if (!response.ok) throw new Error('Materia no encontrada');

        const materia = await response.json();
        abrirModal(materia);

    } catch (error) {
        alert('❌ ' + error.message);
    }
}

async function eliminarMateria(id) {
    if (!confirm('¿Estás seguro de eliminar esta materia?')) return;

    try {
        const response = await fetchConAuth(`${API_URL}/api/materias/${id}`, {
            method: 'DELETE'
        });

        if (!response) return;
        if (!response.ok) throw new Error('Error al eliminar');

        cargarMaterias(paginaActual);
        if (typeof mostrarMensaje === 'function') {
            mostrarMensaje('✅ Materia eliminada');
        }

    } catch (error) {
        alert('❌ ' + error.message);
    }
}

function paginaAnterior() {
    if (paginaActual > 0) cargarMaterias(paginaActual - 1);
}

function paginaSiguiente() {
    if (paginaActual < totalPaginas - 1) cargarMaterias(paginaActual + 1);
}

async function buscar() {
    const termino = document.getElementById('busqueda').value.trim();
    if (!termino) {
        cargarMaterias(0);
        return;
    }

    busquedaActual = termino;
    const tbody = document.getElementById('tablaBody');
    tbody.innerHTML = '<tr><td colspan="6" style="text-align: center;">Buscando...</td></tr>';

    try {
        const primeraPalabra = termino.split(/\s+/)[0];
        const url = `${API_URL}/api/materias/buscar?termino=${encodeURIComponent(primeraPalabra)}`;
        const response = await fetchConAuth(url);
        if (!response) return;

        if (!response.ok) throw new Error(`Error ${response.status} al buscar`);

        const materias = await response.json();

        if (!materias || materias.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" style="text-align: center;">No se encontraron resultados</td></tr>';
        } else {
            tbody.innerHTML = materias.map(m => `
                <tr>
                    <td>${m.id}</td>
                    <td>${m.nombre}</td>
                    <td>${m.codigo}</td>
                    <td>${formatearCampo(m.campo)}</td>
                    <td>${m.descripcion || '-'}</td>
                    <td>
                        <button class="btn btn-warning btn-sm" onclick="editarMateria(${m.id})">✏️ Editar</button>
                        <button class="btn btn-danger btn-sm" onclick="eliminarMateria(${m.id})">🗑️ Eliminar</button>
                    </td>
                </tr>
            `).join('');
        }

        document.getElementById('paginaActual').textContent = '1';
        document.getElementById('totalPaginas').textContent = '1';

    } catch (error) {
        console.error('Error:', error);
        tbody.innerHTML = `<tr><td colspan="6" style="text-align: center; color: red;">Error: ${error.message}</td></tr>`;
    }
}

function limpiarBusqueda() {
    document.getElementById('busqueda').value = '';
    busquedaActual = '';
    cargarMaterias(0);
}