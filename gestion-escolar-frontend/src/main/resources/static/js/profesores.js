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

    cargarProfesores();

    document.getElementById('profesorForm').addEventListener('submit', guardarProfesor);
});

// ==========================================
// CARGAR PROFESORES
// ==========================================

async function cargarProfesores(pagina = 0) {
    const tbody = document.getElementById('tablaBody');
    tbody.innerHTML = '<tr><td colspan="7" style="text-align: center;">Cargando...</td></tr>';

    try {
        const size = 10;
        const url = `${API_URL}/api/profesores/paginado?page=${pagina}&size=${size}&sortBy=id&sortDir=asc`;

        const response = await fetchConAuth(url);
        if (!response) return;

        if (!response.ok) throw new Error('Error al cargar profesores');

        const data = await response.json();
        const profesores = data.content || [];

        paginaActual = data.page?.number ?? 0;
        totalPaginas = data.page?.totalPages ?? 0;

        if (profesores.length === 0) {
            tbody.innerHTML = '<tr><td colspan="7" style="text-align: center;">No hay profesores registrados</td></tr>';
        } else {
            tbody.innerHTML = profesores.map(p => `
                <tr>
                    <td>${p.id}</td>
                    <td>${p.numeroEmpleado ?? '-'}</td>
                    <td>${p.nombre} ${p.apellidoP} ${p.apellidoM}</td>
                    <td>${p.email}</td>
                    <td>${p.especialidad ?? '-'}</td>
                    <td>${p.estatus ?? '-'}</td>
                    <td>
                        <button class="btn btn-warning btn-sm" onclick="editarProfesor(${p.id})">✏️ Editar</button>
                        <button class="btn btn-danger btn-sm" onclick="eliminarProfesor(${p.id})">🗑️ Eliminar</button>
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

// ==========================================
// MODAL
// ==========================================

function abrirModal(profesor = null) {
    const modal = document.getElementById('modal');
    const titulo = document.getElementById('modalTitulo');
    const form = document.getElementById('profesorForm');

    form.reset();

    if (profesor) {
        titulo.textContent = 'Editar Profesor';
        document.getElementById('profesorId').value = profesor.id;
        document.getElementById('nombre').value = profesor.nombre || '';
        document.getElementById('apellidoP').value = profesor.apellidoP || '';
        document.getElementById('apellidoM').value = profesor.apellidoM || '';
        document.getElementById('curp').value = profesor.curp || '';
        document.getElementById('email').value = profesor.email || '';
        document.getElementById('telefono').value = profesor.telefono || '';
        document.getElementById('genero').value = profesor.genero || '';
        document.getElementById('estadoCivil').value = profesor.estadoCivil || '';
        document.getElementById('nacionalidad').value = profesor.nacionalidad || '';
        document.getElementById('paisNacimiento').value = profesor.paisNacimiento || '';
        document.getElementById('entidadNacimiento').value = profesor.entidadNacimiento || '';
        document.getElementById('calle').value = profesor.calle || '';
        document.getElementById('numeroExterior').value = profesor.numeroExterior || '';
        document.getElementById('numeroInterior').value = profesor.numeroInterior || '';
        document.getElementById('codigoPostal').value = profesor.codigoPostal || '';
        document.getElementById('colonia').value = profesor.colonia || '';
        document.getElementById('municipio').value = profesor.municipio || '';
        document.getElementById('direccion').value = profesor.direccion || '';
        document.getElementById('especialidad').value = profesor.especialidad || '';
        document.getElementById('gradoAcademico').value = profesor.gradoAcademico || '';
        document.getElementById('cedulaProfesional').value = profesor.cedulaProfesional || '';
        document.getElementById('fechaIngreso').value = profesor.fechaIngreso || '';
        document.getElementById('tipoContrato').value = profesor.tipoContrato || '';
        document.getElementById('salario').value = profesor.salario || '';
        document.getElementById('numeroIne').value = profesor.numeroIne || '';
        document.getElementById('rfc').value = profesor.rfc || '';
        document.getElementById('nss').value = profesor.nss || '';
        document.getElementById('horarioLaboral').value = profesor.horarioLaboral || '';
        document.getElementById('estatus').value = profesor.estatus || 'ACTIVO';
        document.getElementById('contactoEmergenciaNombre').value = profesor.contactoEmergenciaNombre || '';
        document.getElementById('contactoEmergenciaTelefono').value = profesor.contactoEmergenciaTelefono || '';
        document.getElementById('contactoEmergenciaParentesco').value = profesor.contactoEmergenciaParentesco || '';
        document.getElementById('observaciones').value = profesor.observaciones || '';
    } else {
        titulo.textContent = 'Nuevo Profesor';
        document.getElementById('profesorId').value = '';
        document.getElementById('estatus').value = 'ACTIVO';
        document.getElementById('nacionalidad').value = 'MEXICANA';
        document.getElementById('paisNacimiento').value = 'MÉXICO';
    }

    modal.style.display = 'flex';
}

function cerrarModal() {
    document.getElementById('modal').style.display = 'none';
}

// ==========================================
// GUARDAR
// ==========================================

async function guardarProfesor(e) {
    e.preventDefault();

    const id = document.getElementById('profesorId').value;

    const profesor = {
        nombre: document.getElementById('nombre').value.trim(),
        apellidoP: document.getElementById('apellidoP').value.trim(),
        apellidoM: document.getElementById('apellidoM').value.trim(),
        curp: document.getElementById('curp').value.trim(),
        email: document.getElementById('email').value.trim(),
        telefono: document.getElementById('telefono').value.trim(),
        direccion: document.getElementById('direccion').value.trim(),
        genero: document.getElementById('genero').value,
        estadoCivil: document.getElementById('estadoCivil').value,
        nacionalidad: document.getElementById('nacionalidad').value.trim(),
        paisNacimiento: document.getElementById('paisNacimiento').value.trim(),
        entidadNacimiento: document.getElementById('entidadNacimiento').value.trim(),
        municipio: document.getElementById('municipio').value.trim(),
        colonia: document.getElementById('colonia').value.trim(),
        codigoPostal: document.getElementById('codigoPostal').value.trim(),
        calle: document.getElementById('calle').value.trim(),
        numeroExterior: document.getElementById('numeroExterior').value.trim(),
        numeroInterior: document.getElementById('numeroInterior').value.trim() || null,
        especialidad: document.getElementById('especialidad').value.trim(),
        gradoAcademico: document.getElementById('gradoAcademico').value,
        cedulaProfesional: document.getElementById('cedulaProfesional').value.trim(),
        fechaIngreso: document.getElementById('fechaIngreso').value,
        tipoContrato: document.getElementById('tipoContrato').value,
        salario: parseFloat(document.getElementById('salario').value),
        numeroIne: document.getElementById('numeroIne').value.trim(),
        rfc: document.getElementById('rfc').value.trim().toUpperCase(),
        nss: document.getElementById('nss').value.trim(),
        horarioLaboral: document.getElementById('horarioLaboral').value.trim() || null,
        estatus: document.getElementById('estatus').value,
        contactoEmergenciaNombre: document.getElementById('contactoEmergenciaNombre').value.trim(),
        contactoEmergenciaTelefono: document.getElementById('contactoEmergenciaTelefono').value.trim(),
        contactoEmergenciaParentesco: document.getElementById('contactoEmergenciaParentesco').value.trim(),
        observaciones: document.getElementById('observaciones').value.trim() || null,
        activo: true
    };

    // Validaciones básicas
    const errores = [];
    if (!profesor.nombre) errores.push('El nombre es obligatorio');
    if (!profesor.apellidoP) errores.push('El apellido paterno es obligatorio');
    if (!profesor.apellidoM) errores.push('El apellido materno es obligatorio');
    if (!profesor.curp || profesor.curp.length !== 18) errores.push('La CURP debe tener 18 caracteres');
    if (!profesor.email) errores.push('El email es obligatorio');
    if (!profesor.telefono) errores.push('El teléfono es obligatorio');
    if (!profesor.genero) errores.push('El género es obligatorio');
    if (!profesor.estadoCivil) errores.push('El estado civil es obligatorio');
    if (!profesor.rfc || (profesor.rfc.length < 12 || profesor.rfc.length > 13)) errores.push('El RFC debe tener 12 o 13 caracteres');
    if (!profesor.nss || profesor.nss.length !== 11) errores.push('El NSS debe tener 11 caracteres');
    if (!profesor.contactoEmergenciaNombre) errores.push('El nombre del contacto de emergencia es obligatorio');
    if (!profesor.contactoEmergenciaTelefono) errores.push('El teléfono del contacto de emergencia es obligatorio');
    if (!profesor.contactoEmergenciaParentesco) errores.push('El parentesco del contacto de emergencia es obligatorio');

    if (errores.length > 0) {
        alert('❌ Errores:\n\n' + errores.join('\n'));
        return;
    }

    console.log('📤 Enviando profesor:', JSON.stringify(profesor, null, 2));

    try {
        let url = `${API_URL}/api/profesores`;
        let method = 'POST';

        if (id) {
            url += `/${id}`;
            method = 'PUT';
        }

        const response = await fetchConAuth(url, {
            method,
            body: JSON.stringify(profesor)
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
        cargarProfesores(paginaActual);
        mostrarMensaje(id ? '✅ Profesor actualizado' : '✅ Profesor creado');

    } catch (error) {
        alert('❌ ' + error.message);
    }
}

// ==========================================
// EDITAR
// ==========================================

async function editarProfesor(id) {
    try {
        const response = await fetchConAuth(`${API_URL}/api/profesores/${id}`);
        if (!response) return;
        if (!response.ok) throw new Error('Profesor no encontrado');
        const profesor = await response.json();
        abrirModal(profesor);
    } catch (error) {
        alert('❌ ' + error.message);
    }
}

// ==========================================
// ELIMINAR
// ==========================================

async function eliminarProfesor(id) {
    if (!confirm('¿Eliminar este profesor?')) return;

    try {
        const response = await fetchConAuth(`${API_URL}/api/profesores/${id}`, {
            method: 'DELETE'
        });

        if (!response) return;
        if (!response.ok) throw new Error('Error al eliminar');

        cargarProfesores(paginaActual);
        mostrarMensaje('✅ Profesor eliminado');
    } catch (error) {
        alert('❌ ' + error.message);
    }
}

// ==========================================
// PAGINACIÓN
// ==========================================

function paginaAnterior() {
    if (paginaActual > 0) cargarProfesores(paginaActual - 1);
}

function paginaSiguiente() {
    if (paginaActual < totalPaginas - 1) cargarProfesores(paginaActual + 1);
}

// ==========================================
// BÚSQUEDA
// ==========================================

async function buscar() {
    const termino = document.getElementById('busqueda').value.trim();
    if (!termino) {
        cargarProfesores(0);
        return;
    }

    busquedaActual = termino;
    const tbody = document.getElementById('tablaBody');
    tbody.innerHTML = '<tr><td colspan="7" style="text-align: center;">Buscando...</td></tr>';

    try {
        const primeraPalabra = termino.split(/\s+/)[0];
        const url = `${API_URL}/api/profesores/buscar?termino=${encodeURIComponent(primeraPalabra)}`;
        const response = await fetchConAuth(url);
        if (!response) return;
        if (!response.ok) throw new Error(`Error ${response.status} al buscar`);

        const profesores = await response.json();

        if (!profesores || profesores.length === 0) {
            tbody.innerHTML = '<tr><td colspan="7" style="text-align: center;">No se encontraron resultados</td></tr>';
        } else {
            tbody.innerHTML = profesores.map(p => `
                <tr>
                    <td>${p.id}</td>
                    <td>${p.numeroEmpleado ?? '-'}</td>
                    <td>${p.nombre} ${p.apellidoP} ${p.apellidoM}</td>
                    <td>${p.email}</td>
                    <td>${p.especialidad ?? '-'}</td>
                    <td>${p.estatus ?? '-'}</td>
                    <td>
                        <button class="btn btn-warning btn-sm" onclick="editarProfesor(${p.id})">✏️ Editar</button>
                        <button class="btn btn-danger btn-sm" onclick="eliminarProfesor(${p.id})">🗑️ Eliminar</button>
                    </td>
                </tr>
            `).join('');
        }

        document.getElementById('paginaActual').textContent = '1';
        document.getElementById('totalPaginas').textContent = '1';
    } catch (error) {
        console.error('Error:', error);
        tbody.innerHTML = `<tr><td colspan="7" style="text-align: center; color: red;">Error: ${error.message}</td></tr>`;
    }
}

function limpiarBusqueda() {
    document.getElementById('busqueda').value = '';
    busquedaActual = '';
    cargarProfesores(0);
}