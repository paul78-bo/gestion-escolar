// ==========================================
// CONFIGURACIÓN
// ==========================================

const API_URL = 'http://74.208.219.237:8083';


// ==========================================
// UTILIDADES DE AUTENTICACIÓN
// ==========================================

function getToken() {
    return localStorage.getItem('token');
}

function getRol() {
    return localStorage.getItem('rol');
}

function getUsername() {
    return localStorage.getItem('username');
}

function logout() {
    // Detener el heartbeat si está activo
    if (heartbeatInterval) clearInterval(heartbeatInterval);
    
    // Limpiar TODO el localStorage para no dejar datos sueltos (rol, username, etc.)
    localStorage.clear();
    sessionStorage.clear();
    
    window.location.href = 'login.html';
}

function verificarAutenticacion() {
    if (!getToken()) {
        window.location.href = 'login.html';
        return false;
    }

    // Iniciar el heartbeat al entrar a cualquier página
    iniciarHeartbeat();
    return true;
}

// ==========================================
// FETCH CON AUTENTICACIÓN
// ==========================================

async function fetchConAuth(url, options = {}) {
    const token = getToken();

    const headers = {
        'Content-Type': 'application/json',
        ...options.headers
    };

    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }

    try {
        const response = await fetch(url, { ...options, headers });

        // Si el token expiró, fue revocado o no hay permisos, cerramos sesión
        if (response.status === 401 || response.status === 403) {
            // Evitar hacer logout si ya estamos en login
            if (!window.location.pathname.includes('login.html')) {
                logout();
            }
            return null;
        }

        return response;
    } catch (error) {
        // Si hay un error de red (servidor caído), el heartbeat se encargará
        console.error("Error de red en fetchConAuth:", error);
        throw error;
    }
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

// ==========================================
// 🎯 PERMISOS POR LINK (SIDEBAR)
// ==========================================
const PERMISOS_POR_LINK = {
    'dashboard.html': ['ADMIN', 'DIRECTOR', 'SECRETARIO', 'PROFESOR', 'ALUMNO', 'INTENDENTE'],
    'alumnos.html': ['ADMIN', 'DIRECTOR', 'SECRETARIO', 'PROFESOR'],
    'profesores.html': ['ADMIN', 'DIRECTOR', 'SECRETARIO'],
    'materias.html': ['ADMIN', 'DIRECTOR', 'SECRETARIO', 'PROFESOR', 'ALUMNO'],
    'grupos.html': ['ADMIN', 'DIRECTOR', 'SECRETARIO', 'PROFESOR', 'ALUMNO'],
    'ciclos.html': ['ADMIN', 'DIRECTOR', 'SECRETARIO', 'PROFESOR', 'ALUMNO'],
    'asignaciones.html': ['ADMIN', 'DIRECTOR', 'SECRETARIO', 'PROFESOR'],
    'inscripciones.html': ['ADMIN', 'DIRECTOR', 'SECRETARIO', 'PROFESOR'],
    'calificaciones.html': ['ADMIN', 'DIRECTOR', 'SECRETARIO', 'PROFESOR', 'ALUMNO'],
    'boletas.html': ['ADMIN', 'DIRECTOR', 'SECRETARIO', 'PROFESOR', 'ALUMNO'] // 👈 Boletas
};

// ==========================================
// 🎯 PERMISOS POR PÁGINA (ACCESO DIRECTO)
// ==========================================
const PERMISOS_POR_PAGINA = {
    'dashboard.html': ['ADMIN', 'DIRECTOR', 'SECRETARIO', 'PROFESOR', 'ALUMNO', 'INTENDENTE'],
    'alumnos.html': ['ADMIN', 'DIRECTOR', 'SECRETARIO', 'PROFESOR'],
    'profesores.html': ['ADMIN', 'DIRECTOR', 'SECRETARIO'],
    'materias.html': ['ADMIN', 'DIRECTOR', 'SECRETARIO', 'PROFESOR', 'ALUMNO'],
    'grupos.html': ['ADMIN', 'DIRECTOR', 'SECRETARIO', 'PROFESOR', 'ALUMNO'],
    'ciclos.html': ['ADMIN', 'DIRECTOR', 'SECRETARIO', 'PROFESOR', 'ALUMNO'],
    'asignaciones.html': ['ADMIN', 'DIRECTOR', 'SECRETARIO', 'PROFESOR'],
    'inscripciones.html': ['ADMIN', 'DIRECTOR', 'SECRETARIO', 'PROFESOR'],
    'calificaciones.html': ['ADMIN', 'DIRECTOR', 'SECRETARIO', 'PROFESOR', 'ALUMNO'],
    'boletas.html': ['ADMIN', 'DIRECTOR', 'SECRETARIO', 'PROFESOR', 'ALUMNO']
};

// ==========================================
// 🎯 FUNCIÓN: Verificar permiso de la página
// ==========================================
function verificarPermisoPagina() {
    const rol = getRol();
    const paginaActualUrl = window.location.pathname.split('/').pop();

    if (PERMISOS_POR_PAGINA[paginaActualUrl] &&
        !PERMISOS_POR_PAGINA[paginaActualUrl].includes(rol)) {
        alert('❌ No tienes permiso para acceder a esta página');
        window.location.href = 'dashboard.html';
        return false;
    }
    return true;
}

// ==========================================
// 🎯 FUNCIÓN: Ajustar menú según el rol
// ==========================================
function ajustarMenuPorRol() {
    const rol = getRol();
    console.log('🎯 Ajustando menú para rol:', rol);

    document.querySelectorAll('.sidebar a').forEach(a => {
        const href = a.getAttribute('href');

        if (!href || href === '#' || href.startsWith('#')) {
            return;
        }

        const rolesPermitidos = PERMISOS_POR_LINK[href];

        if (rolesPermitidos) {
            if (!rolesPermitidos.includes(rol)) {
                a.style.display = 'none';
            }
        }
    });
}

// ==========================================
// 🎯 FUNCIÓN: Mostrar/ocultar acciones según el rol
// ==========================================
function botonesAccion(id, tipo, rolesConPermiso = 'ADMIN,DIRECTOR,SECRETARIO') {
    const rol = getRol();
    const rolesPermitidos = rolesConPermiso.split(',');
    const puedeEditar = rolesPermitidos.includes(rol);

    if (!puedeEditar) {
        return '<span style="color: #95a5a6; font-size: 12px;">Solo lectura</span>';
    }

    return `
        <button class="btn btn-warning btn-sm" onclick="editar${capitalize(tipo)}(${id})">✏️ Editar</button>
        <button class="btn btn-danger btn-sm" onclick="eliminar${capitalize(tipo)}(${id})">🗑️ Eliminar</button>
    `;
}

function capitalize(str) {
    return str.charAt(0).toUpperCase() + str.slice(1);
}

function ocultarBotonesSiNoTienePermiso(rolesConPermiso) {
    const rol = getRol();
    const rolesPermitidos = rolesConPermiso.split(',');

    if (!rolesPermitidos.includes(rol)) {
        document.querySelectorAll('table .btn-warning, table .btn-danger').forEach(btn => {
            btn.style.display = 'none';
        });

        document.querySelectorAll('.btn-success').forEach(btn => {
            if (!btn.closest('#modal')) {
                btn.style.display = 'none';
            }
        });
    }
}

// ==========================================
// LÓGICA DE HEARTBEAT (Detectar caída del server)
// ==========================================
// ==========================================
// LÓGICA DE HEARTBEAT (Detectar caída del server)
// ==========================================
let heartbeatInterval = null;

// 👇 Constante para definir si estamos en desarrollo o producción
// Cambia a false cuando subas a producción para desactivarlo automáticamente
const ES_ENTORNO_DESARROLLO = true; 

function iniciarHeartbeat() {
    // 🚫 CONDICIÓN CLAVE: Si no es desarrollo, no hacemos nada
    if (!ES_ENTORNO_DESARROLLO) {
        console.log("ℹ️ Heartbeat desactivado (entorno de producción).");
        return;
    }

    // 🚫 SEGUNDA VALIDACIÓN: Asegurarnos que estamos en localhost o 127.0.0.1
    const host = window.location.hostname;
    if (host !== 'localhost' && host !== '127.0.0.1') {
        console.log("ℹ️ Heartbeat desactivado (no es un entorno local).");
        return;
    }

    if (heartbeatInterval) clearInterval(heartbeatInterval);

    // Revisa cada 3 segundos si el servidor está vivo
    heartbeatInterval = setInterval(async () => {
        try {
            const controller = new AbortController();
            // Si en 2 segundos el servidor no responde, asumimos que se cayó
            const timeoutId = setTimeout(() => controller.abort(), 2000);

            const response = await fetch(`${API_URL}/api/ping`, {
                method: 'GET',
                signal: controller.signal
            });

            clearTimeout(timeoutId);

        } catch (error) {
            // Si hay error (el servidor no responde), cerramos sesión inmediatamente
            console.warn("¡Servidor caído! Cerrando sesión por seguridad...");
            detenerSesionPorCaida();
        }
    }, 3000); // Se ejecuta cada 3 segundos
}