let paginaActual = 0;
let totalPaginas = 0;
let busquedaActual = '';

// ==========================================
// 1. CARGAR TABLA
// ==========================================
async function cargarAlumnos(pagina = 0) {
    const tbody = document.getElementById('tablaBody');
    if (!tbody) return;

    tbody.innerHTML = '<tr><td colspan="7" style="text-align: center;">Cargando...</td></tr>';

    try {
        const size = 10;
        const url = `${API_URL}/api/alumnos/paginado?page=${pagina}&size=${size}&sortBy=id&sortDir=asc`;

        const response = await fetchConAuth(url);
        if (!response) return;

        if (!response.ok) throw new Error('Error al cargar alumnos');

        const data = await response.json();
        const alumnos = data.content || [];

        paginaActual = data.page?.number ?? 0;
        totalPaginas = data.page?.totalPages ?? 0;

        if (alumnos.length === 0) {
            tbody.innerHTML = '<tr><td colspan="7" style="text-align: center;">No hay alumnos registrados</td></tr>';
        } else {
            tbody.innerHTML = alumnos.map(a => `
                <tr>
                    <td>${a.id}</td>
                    <td>${a.nombre} ${a.apellidoP} ${a.apellidoM}</td>
                    <td>${a.email}</td>
                    <td>${a.matricula}</td>
                    <td>${a.telefono ?? '-'}</td>
                    <td>${a.estatus ?? '-'}</td>
                    <td>
                        <a href="boletas.html?alumnoId=${a.id}" class="btn btn-primary btn-sm" style="text-decoration: none; color: white;">📄 Boleta</a>
                        <button class="btn btn-warning btn-sm" onclick="editarAlumno(${a.id})">✏️ Editar</button>
                        <button class="btn btn-danger btn-sm" onclick="eliminarAlumno(${a.id})">🗑️ Eliminar</button>
                    </td>
                </tr>
            `).join('');
        }

        // Actualizar paginación
        if (document.getElementById('paginaActual')) document.getElementById('paginaActual').textContent = paginaActual + 1;
        if (document.getElementById('totalPaginas')) document.getElementById('totalPaginas').textContent = totalPaginas;
        if (document.getElementById('btnAnterior')) document.getElementById('btnAnterior').disabled = paginaActual === 0;
        if (document.getElementById('btnSiguiente')) document.getElementById('btnSiguiente').disabled = paginaActual >= totalPaginas - 1;

        if (typeof ocultarBotonesSiNoTienePermiso === 'function') {
            ocultarBotonesSiNoTienePermiso('ADMIN,DIRECTOR,SECRETARIO');
        }

    } catch (error) {
        console.error('Error:', error);
        tbody.innerHTML = `<tr><td colspan="7" style="text-align: center; color: red;">Error: ${error.message}</td></tr>`;
    }
}

// ==========================================
// 2. FUNCIÓN PARA EDITAR (La que falta)
// ==========================================
async function editarAlumno(id) {
    console.log("Clic en editar alumno ID:", id); // Esto debe salir en consola
    try {
        const response = await fetchConAuth(`${API_URL}/api/alumnos/${id}`);
        
        if (!response) return;
        if (!response.ok) throw new Error('Error al obtener datos');

        const alumno = await response.json();
        abrirModal(alumno);

    } catch (error) {
        console.error('Error al editar:', error);
        alert('❌ ' + error.message);
    }
}

// ==========================================
// 3. ABRIR MODAL Y LLENAR DATOS
// ==========================================
function abrirModal(alumno = null) {
    const modal = document.getElementById('modal');
    const form = document.getElementById('alumnoForm');
    if (!modal || !form) return;

    form.reset();

    if (alumno) {
        // Modo Edición
        document.getElementById('modalTitulo').textContent = 'Editar Alumno';
        document.getElementById('alumnoId').value = alumno.id || '';
        
        // --- DATOS PERSONALES ---
        document.getElementById('nombre').value = alumno.nombre || '';
        document.getElementById('apellidoP').value = alumno.apellidoP || '';
        document.getElementById('apellidoM').value = alumno.apellidoM || '';
        document.getElementById('curp').value = alumno.curp || '';
        document.getElementById('email').value = alumno.email || '';
        document.getElementById('telefono').value = alumno.telefono || '';
        document.getElementById('genero').value = alumno.genero || '';
        document.getElementById('fechaNacimiento').value = alumno.fechaNacimiento || '';
        document.getElementById('estadoCivil').value = alumno.estadoCivil || '';
        document.getElementById('nacionalidad').value = alumno.nacionalidad || '';
        document.getElementById('paisNacimiento').value = alumno.paisNacimiento || '';
        document.getElementById('entidadNacimiento').value = alumno.entidadNacimiento || '';
		document.getElementById('lugarNacimiento').value = alumno.lugarNacimiento || '';

        
        // --- DOMICILIO ---
        document.getElementById('calle').value = alumno.calle || '';
        document.getElementById('numeroExterior').value = alumno.numeroExterior || '';
        document.getElementById('numeroInterior').value = alumno.numeroInterior || '';
        document.getElementById('codigoPostal').value = alumno.codigoPostal || '';
        document.getElementById('colonia').value = alumno.colonia || '';
        document.getElementById('municipio').value = alumno.municipio || '';
        document.getElementById('direccion').value = alumno.direccion || '';

        // --- DATOS ACADÉMICOS ---
        document.getElementById('matricula').value = alumno.matricula || '';
        document.getElementById('fechaIngreso').value = alumno.fechaIngreso || '';
        document.getElementById('estatus').value = alumno.estatus || 'ACTIVO';

        // --- TUTOR ---
        document.getElementById('tutorNombre').value = alumno.tutorNombre || '';
        document.getElementById('tutorTelefono').value = alumno.tutorTelefono || '';
        document.getElementById('tutorEmail').value = alumno.tutorEmail || '';
        
        // --- OBSERVACIONES ---
        document.getElementById('observaciones').value = alumno.observaciones || '';

    } else {
        // Modo Nuevo
        document.getElementById('modalTitulo').textContent = 'Nuevo Alumno';
        document.getElementById('alumnoId').value = '';
        document.getElementById('estatus').value = 'ACTIVO';
    }

    modal.style.display = 'flex';
}



function cerrarModal() {
    document.getElementById('modal').style.display = 'none';
}

// ==========================================
// 4. GUARDAR (Crear o Actualizar)
// ==========================================
async function guardarAlumno(e) {
    e.preventDefault();
    
    const id = document.getElementById('alumnoId').value;
    
    const alumno = {
        nombre: document.getElementById('nombre').value.trim(),
        apellidoP: document.getElementById('apellidoP').value.trim(),
        apellidoM: document.getElementById('apellidoM').value.trim(),
        curp: document.getElementById('curp').value.trim(),
        email: document.getElementById('email').value.trim(),
        telefono: document.getElementById('telefono').value.trim(),
        genero: document.getElementById('genero').value,
        fechaNacimiento: document.getElementById('fechaNacimiento').value,
        estadoCivil: document.getElementById('estadoCivil').value,
        nacionalidad: document.getElementById('nacionalidad').value.trim(),
        paisNacimiento: document.getElementById('paisNacimiento').value.trim(),
        entidadNacimiento: document.getElementById('entidadNacimiento').value.trim(),
		lugarNacimiento: document.getElementById('lugarNacimiento').value.trim(),
        
        calle: document.getElementById('calle').value.trim(),
        numeroExterior: document.getElementById('numeroExterior').value.trim(),
        numeroInterior: document.getElementById('numeroInterior').value.trim(),
        codigoPostal: document.getElementById('codigoPostal').value.trim(),
        colonia: document.getElementById('colonia').value.trim(),
        municipio: document.getElementById('municipio').value.trim(),
        direccion: document.getElementById('direccion').value.trim(),
        
        matricula: document.getElementById('matricula').value.trim(),
        fechaIngreso: document.getElementById('fechaIngreso').value,
        estatus: document.getElementById('estatus').value,
        
        tutorNombre: document.getElementById('tutorNombre').value.trim(),
        tutorTelefono: document.getElementById('tutorTelefono').value.trim(),
        tutorEmail: document.getElementById('tutorEmail').value.trim(),
        
        observaciones: document.getElementById('observaciones').value.trim()
    };

    try {
        let url = `${API_URL}/api/alumnos`;
        let method = 'POST';
        
        if (id) {
            url += `/${id}`;
            method = 'PUT';
        }

        const response = await fetchConAuth(url, { method, body: JSON.stringify(alumno) });
        if (!response) return;

        if (!response.ok) {
            const errorData = await response.text();
            throw new Error(`Error ${response.status}: ${errorData}`);
        }

        cerrarModal();
        cargarAlumnos(paginaActual);
        alert(id ? '✅ Alumno actualizado' : '✅ Alumno creado');
        
    } catch (error) {
        console.error('Error al guardar:', error);
        alert('❌ ' + error.message);
    }
}

// ==========================================
// 5. BÚSQUEDA Y PAGINACIÓN
// ==========================================
async function buscar() {
    const termino = document.getElementById('busqueda').value.trim();
    if (!termino) { cargarAlumnos(0); return; }

    const tbody = document.getElementById('tablaBody');
    tbody.innerHTML = '<tr><td colspan="7" style="text-align: center;">Buscando...</td></tr>';

    try {
        const url = `${API_URL}/api/alumnos/buscar?termino=${encodeURIComponent(termino)}`;
        const response = await fetchConAuth(url);
        if (!response || !response.ok) throw new Error('Error al buscar');

        const alumnos = await response.json();
        if (alumnos.length === 0) {
            tbody.innerHTML = '<tr><td colspan="7" style="text-align: center;">No se encontraron resultados</td></tr>';
        } else {
            tbody.innerHTML = alumnos.map(a => `
                <tr>
                    <td>${a.id}</td>
                    <td>${a.nombre} ${a.apellidoP} ${a.apellidoM}</td>
                    <td>${a.email}</td>
                    <td>${a.matricula}</td>
                    <td>${a.telefono ?? '-'}</td>
                    <td>${a.estatus ?? '-'}</td>
                    <td>
                        <a href="boletas.html?alumnoId=${a.id}" class="btn btn-primary btn-sm" style="text-decoration: none; color: white;">📄 Boleta</a>
                        <button class="btn btn-warning btn-sm" onclick="editarAlumno(${a.id})">✏️ Editar</button>
                        <button class="btn btn-danger btn-sm" onclick="eliminarAlumno(${a.id})">🗑️ Eliminar</button>
                    </td>
                </tr>
            `).join('');
        }
    } catch (error) {
        tbody.innerHTML = `<tr><td colspan="7" style="text-align: center; color: red;">Error: ${error.message}</td></tr>`;
    }
}



async function eliminarAlumno(id) {
    // 1. Pedir confirmación al usuario (buena práctica)
    const confirmar = confirm(`¿Estás seguro de que deseas eliminar al alumno con ID ${id}?`);
    if (!confirmar) return; // Si dice que no, cancelamos

    try {
        // 2. Hacer la petición DELETE al backend
        const response = await fetchConAuth(`${API_URL}/api/alumnos/${id}`, {
            method: 'DELETE'
        });

        if (!response) return; // Si falla el token

        // 3. Validar la respuesta
        if (!response.ok) {
            throw new Error(`Error ${response.status} al intentar eliminar`);
        }

        // 4. Si todo sale bien, avisar y recargar la tabla
        alert('✅ Alumno eliminado correctamente');
        cargarAlumnos(paginaActual); // Recarga la tabla en la página actual

    } catch (error) {
        console.error('Error al eliminar:', error);
        alert('❌ ' + error.message);
    }
}
function limpiarBusqueda() {
    document.getElementById('busqueda').value = '';
    cargarAlumnos(0);
}

function paginaAnterior() { if (paginaActual > 0) cargarAlumnos(paginaActual - 1); }
function paginaSiguiente() { if (paginaActual < totalPaginas - 1) cargarAlumnos(paginaActual + 1); }

// Inicialización
document.addEventListener('DOMContentLoaded', () => {
    if (document.getElementById('tablaBody')) {
        cargarAlumnos(0);
    }
});