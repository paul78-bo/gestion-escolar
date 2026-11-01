# Usamos Nginx como servidor web
FROM nginx:alpine

# Copiamos todos nuestros archivos estáticos a la carpeta de Nginx
COPY . /usr/share/nginx/html

# Exponemos el puerto 80 (el estándar de la web)
EXPOSE 80

# Nginx arranca automáticamente