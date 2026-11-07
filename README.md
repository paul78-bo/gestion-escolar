# 🎓 Sistema de Gestión Escolar Full-Stack

Sistema completo para la gestión de alumnos, profesores, grupos, asignaciones y boletas de calificaciones.

## 🛠️ Tecnologías Utilizadas
- **Backend:** Java 17, Spring Boot 3, Spring Security (JWT), Hibernate/JPA, MySQL 8.0, JavaMail (SMTP).
- **Frontend:** HTML5, CSS3, JavaScript Vanilla (Fetch API).
- **Infraestructura:** Docker, Nginx (Proxy Inverso), IONOS VPS (Ubuntu).

## 🚀 Características Principales
- **Autenticación y Autorización:** Login con JWT y control de acceso por roles (ADMIN, DIRECTOR, PROFESOR, ALUMNO).
- **Recuperación de Contraseña:** Envío de correos reales con tokens temporales (expiración de 24 hrs).
- **Validaciones de Negocio:** Regla de edad para inscripción de alumnos (11 a 16 años) y prevención de duplicados en asignaciones.
- **Generación de Boletas:** Cálculo automático de promedios por trimestre y promedio general.
- **Despliegue en Producción:** Configuración de proxy inverso con Nginx para eliminar problemas de CORS y proteger el backend.

## 📦 Cómo levantar el proyecto localmente
1. Clona el repositorio.
2. Ejecuta `docker compose up -d --build`.
3. Abre `http://localhost:8084` en tu navegador.
4. Usuario por defecto: `superfelix` / Contraseña: `felix78`.

## 🌐 Demo en Vivo
Puedes ver el proyecto funcionando en: `http://74.208.219.237:8084`