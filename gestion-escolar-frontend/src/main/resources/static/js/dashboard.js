document.addEventListener('DOMContentLoaded', async () => {
    if (!verificarAutenticacion()) return;
    if (!verificarPermisoPagina()) return;   // 🎯 NUEVA LÍNEA
    ajustarMenuPorRol();                     // 🎯 NUEVA LÍNEA


    document.getElementById('usernameDisplay').textContent = getUsername();
    document.getElementById('rolDisplay').textContent = getRol();

    await cargarContadores();
});

async function cargarContadores() {
    try {
        const response = await fetchConAuth(`${API_URL}/api/dashboard/contadores`);
        if (!response) return;
        if (!response.ok) throw new Error('Error al cargar contadores');

        const data = await response.json();
        console.log('📊 Contadores:', data);

        // Totales
        document.getElementById('totalAlumnos').textContent = data.totalAlumnos ?? 0;
        document.getElementById('totalProfesores').textContent = data.totalProfesores ?? 0;
        document.getElementById('totalMaterias').textContent = data.totalMaterias ?? 0;
        document.getElementById('totalGrupos').textContent = data.totalGrupos ?? 0;
        document.getElementById('totalCiclos').textContent = data.totalCiclos ?? 0;
        document.getElementById('totalAsignaciones').textContent = data.totalAsignaciones ?? 0;
        document.getElementById('totalInscripciones').textContent = data.totalInscripciones ?? 0;
        document.getElementById('totalCalificaciones').textContent = data.totalCalificaciones ?? 0;

        // Activos
        document.getElementById('alumnosActivos').textContent = (data.alumnosActivos ?? 0) + ' activos';
        document.getElementById('profesoresActivos').textContent = (data.profesoresActivos ?? 0) + ' activos';
        document.getElementById('materiasActivas').textContent = (data.materiasActivas ?? 0) + ' activas';
        document.getElementById('gruposActivos').textContent = (data.gruposActivos ?? 0) + ' activos';
        document.getElementById('ciclosActivos').textContent = (data.ciclosActivos ?? 0) + ' activos';

    } catch (error) {
        console.error('Error cargando contadores:', error);
    }
}