// Definimos las horas base del horario de Telesecundaria
const HORAS = [
    { inicio: "08:00", fin: "09:10" },
    { inicio: "09:10", fin: "10:20" },
	{ inicio: "10:20", fin: "10:35"}, // 
	{ inicio: "10:35", fin: "10:55", tipo: "receso" }, // 👈 NUEVO
    { inicio: "10:55", fin: "11:30" }, // Después del receso
    { inicio: "11:30", fin: "13:00" },
    { inicio: "13:00", fin: "14:00" }
];

const DIAS = ["LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES"];

document.addEventListener('DOMContentLoaded', async () => {
    if (!verificarAutenticacion()) return;
    if (!verificarPermisoPagina()) return;
    ajustarMenuPorRol();

    document.getElementById('usernameDisplay').textContent = getUsername();
    document.getElementById('rolDisplay').textContent = getRol();

    // Cargar los grupos en el select
    await cargarGrupos();
});

async function cargarGrupos() {
    try {
        const response = await fetchConAuth(`${API_URL}/api/grupos/activos`);
        if (!response || !response.ok) throw new Error('Error al cargar grupos');
        
        const grupos = await response.json();
        const select = document.getElementById('selectGrupo');
        
        select.innerHTML = '<option value="">-- Seleccionar Grupo --</option>' +
            grupos.map(g => `<option value="${g.id}">${g.nombre}</option>`).join('');
    } catch (error) {
        console.error(error);
        alert("Error al cargar la lista de grupos");
    }
}

async function cargarHorario() {
    const grupoId = document.getElementById('selectGrupo').value;
    const contenedor = document.getElementById('contenedor-horario');

    if (!grupoId) {
        contenedor.innerHTML = '<p style="grid-column: 1 / -1; text-align: center; padding: 20px;">Selecciona un grupo para ver su horario.</p>';
        return;
    }

    contenedor.innerHTML = '<p style="grid-column: 1 / -1; text-align: center; padding: 20px;">Cargando horario...</p>';

    try {
        // Traemos todas las asignaciones (podrías optimizar creando un endpoint que filtre por grupo)
        const response = await fetchConAuth(`${API_URL}/api/asignaciones/paginado?page=0&size=100`);
        if (!response || !response.ok) throw new Error('Error al cargar asignaciones');
        
        const data = await response.json();
        // Filtramos las asignaciones que pertenecen a este grupo
        const asignacionesGrupo = data.content.filter(a => a.grupoId == grupoId);

        dibujarTabla(asignacionesGrupo);

    } catch (error) {
        console.error(error);
        contenedor.innerHTML = `<p style="grid-column: 1 / -1; text-align: center; color: red; padding: 20px;">Error: ${error.message}</p>`;
    }
}

function dibujarTabla(asignaciones) {
    const contenedor = document.getElementById('contenedor-horario');
    contenedor.innerHTML = '';

    // 1. Cabecera (Días)
    contenedor.innerHTML += `<div class="horario-header">Hora</div>`;
    DIAS.forEach(dia => {
        contenedor.innerHTML += `<div class="horario-header">${dia}</div>`;
    });

    // 2. Filas por cada hora
    HORAS.forEach(hora => {
        
        // Si es el receso, pintamos una fila especial de lado a lado
        if (hora.tipo === 'receso') {
            contenedor.innerHTML += `
                <div class="horario-hora" style="background-color: #333; color: white; font-weight: bold;">
                    ${hora.inicio}<br>${hora.fin}
                </div>
                <div style="grid-column: span 5; background-color: #333; color: white; display: flex; justify-content: center; align-items: center; font-weight: bold; letter-spacing: 5px;">
                    R E C E S O
                </div>
            `;
            return; // Saltamos el resto del bucle para esta hora
        }

        // Si es clase normal, pintamos la hora
        contenedor.innerHTML += `<div class="horario-hora">${hora.inicio}<br>${hora.fin}</div>`;

        // Columnas de los días
        DIAS.forEach(dia => {
            const clase = asignaciones.find(a => 
                a.diaSemana === dia && 
                a.horaInicio && a.horaInicio.startsWith(hora.inicio)
            );

            if (clase) {
                let claseColor = '';
                if (clase.materiaNombre.toLowerCase().includes('espa') || clase.materiaNombre.toLowerCase().includes('ingl')) claseColor = 'LENGUAJES';
                else if (clase.materiaNombre.toLowerCase().includes('mate') || clase.materiaNombre.toLowerCase().includes('bio')) claseColor = 'SABERES';
                else if (clase.materiaNombre.toLowerCase().includes('geo') || clase.materiaNombre.toLowerCase().includes('his')) claseColor = 'ETICA';
                else claseColor = 'HUMANO';

                contenedor.innerHTML += `
                    <div class="horario-celda ${claseColor}">
                        <span class="materia-nombre">${clase.materiaNombre}</span>
                        <span class="profesor-nombre">${clase.profesorNombreCompleto.split(' ')[0]} ${clase.profesorNombreCompleto.split(' ')[1]}</span>
                    </div>
                `;
            } else {
                contenedor.innerHTML += `<div class="horario-celda"></div>`;
            }
        });
    });
}