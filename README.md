# Agenda Deportistas Service

API REST para la gestión de deportistas, profesores, grupos, cursos, asistencias, agendas y recordatorios en entornos deportivos.

## Tecnologías

- Java 17
- Spring Boot 3.0.6
- Spring Security + JWT
- Spring Data JPA / Hibernate
- MySQL
- Maven

## Requisitos previos

- JDK 17 o superior
- Maven 3.8+ (o usar el wrapper incluido `mvnw`)
- MySQL 8.0+

## Configuración de base de datos

Crear la base de datos en MySQL antes de ejecutar la aplicación:

```sql
CREATE DATABASE agendadeportistas;
```

La configuración de conexión se encuentra en [src/main/resources/application.properties](src/main/resources/application.properties):

```
spring.datasource.url=jdbc:mysql://localhost:3306/agendadeportistas
spring.datasource.username=root
spring.datasource.password=<tu_contraseña>
spring.jpa.hibernate.ddl-auto=update
```

> El esquema se genera automáticamente al iniciar la aplicación (`ddl-auto=update`).

## Ejecución

### Con Maven wrapper (recomendado)

```bash
./mvnw spring-boot:run
```

En Windows:

```cmd
mvnw.cmd spring-boot:run
```

### Con Maven instalado

```bash
mvn spring-boot:run
```

### Generando el JAR

```bash
mvn clean package
java -jar target/agendaservices-0.0.1-SNAPSHOT.jar
```

La aplicación queda disponible en `http://localhost:8080`.

## Autenticación

La API usa JWT. Para acceder a los endpoints protegidos:

1. Registrar un usuario o usar uno existente.
2. Hacer login para obtener el token.
3. Incluir el token en el header de cada petición:

```
Authorization: Bearer <token>
```

## Endpoints principales

### Autenticación

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/auth/login` | Iniciar sesión |
| POST | `/api/auth/registro` | Registrar usuario |
| POST | `/api/auth/registroAdm` | Registrar administrador |

### Deportistas

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/deportistas/listar` | Listar deportistas |
| POST | `/api/deportistas/crear` | Crear deportista |
| PUT | `/api/deportistas/actualizar` | Actualizar deportista |
| DELETE | `/api/deportistas/eliminar/{id}` | Eliminar deportista |
| GET | `/api/deportistas/buscarNombre/{nombre}` | Buscar por nombre |

### Profesores

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/profesores/listar` | Listar profesores |
| POST | `/api/profesores/crear` | Crear profesor |
| POST | `/api/profesores/actualizar` | Actualizar profesor |
| DELETE | `/api/profesores/eliminar/{id}` | Eliminar profesor |

### Grupos

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/grupos/listar` | Listar grupos |
| POST | `/api/grupos/crear` | Crear grupo |
| POST | `/api/grupos/actualizar` | Actualizar grupo |
| DELETE | `/api/grupos/eliminar/{id}` | Eliminar grupo |

### Cursos

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/cursos/listar` | Listar cursos |
| POST | `/api/cursos/crear` | Crear curso |
| PUT | `/api/cursos/actualizar` | Actualizar curso |
| DELETE | `/api/cursos/eliminar/{id}` | Eliminar curso |

### Asistencias

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/asistencias/guardar` | Registrar asistencia |
| GET | `/api/asistencias/listar/{fecha}` | Listar asistencias por fecha |

### Agendas

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/agendas/listar` | Listar agendas |
| POST | `/api/agendas/crear` | Crear agenda |
| DELETE | `/api/agendas/eliminar/{id}` | Eliminar agenda |

### Recordatorios

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/recordatorios/listar` | Listar recordatorios |
| GET | `/api/recordatorios/listarHoy` | Recordatorios de hoy |
| POST | `/api/recordatorios/crear` | Crear recordatorio |
| POST | `/api/recordatorios/actualizar` | Actualizar recordatorio |
| DELETE | `/api/recordatorios/eliminar/{id}` | Eliminar recordatorio |
| DELETE | `/api/recordatorios/eliminarExpirados` | Eliminar expirados |

### Ubicaciones

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/ubicaciones/listar` | Listar ubicaciones |
| POST | `/api/ubicaciones/crear` | Crear ubicación |
| DELETE | `/api/ubicaciones/eliminar/{id}` | Eliminar ubicación |

### Posts

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/post/listar` | Listar posts |
| GET | `/api/post/last` | Último post |
| POST | `/api/post/crear` | Crear post |

## CORS

El frontend habilitado por defecto es `http://localhost:5173` (Vite). Para cambiar la URL del frontend, modificar la propiedad `frontend.path` en `application.properties`.
