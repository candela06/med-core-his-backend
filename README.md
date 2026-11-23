# MedCore HIS - Sistema Integral de Información Hospitalaria

MedCore HIS es un sistema backend modular diseñado para centralizar la gestión clínica y administrativa de una institución sanitaria. El proyecto simula el circuito completo de un paciente: desde su registro y la asignación de turnos, hasta la admisión, consulta médica, internación y seguimiento de enfermería.

Además de la lógica de negocio hospitalaria, el sistema pone un fuerte énfasis en la seguridad, la integridad de los datos y la administración de infraestructura IT.

## Stack Tecnológico

El proyecto fue construido como un monolito modular utilizando las siguientes tecnologías:

- Lenguaje: Java 21
- Framework: Spring Boot 3 (Web, Data JPA, Security)
- Base de Datos: PostgreSQL (Versionado con Flyway)
- Seguridad: JWT (JSON Web Tokens) y BCrypt
- Documentación: Springdoc OpenAPI (Swagger UI)
- Monitoreo: Spring Boot Actuator
- Tests: JUnit 5 y Mockito
- Gestor de dependencias: Maven

## Requisitos Previos

Antes de levantar el proyecto, asegurate de tener instalado:

1. Java 21 (JDK).
2. PostgreSQL corriendo localmente.
3. Maven (o usar el wrapper ./mvnw incluido en el proyecto).

## Instalación y Ejecución

### 1. Preparar la Base de Datos
Ingresá a tu consola de PostgreSQL (o pgAdmin) y creá una base de datos vacía llamada medcore_db. Flyway se encargará de crear todas las tablas automáticamente al iniciar la aplicación.

CREATE DATABASE medcore_db;

### 2. Configurar Variables de Entorno (.env)
El proyecto utiliza variables de entorno para proteger las credenciales. En la raíz del proyecto (donde está el pom.xml), creá un archivo llamado .env con tu configuración local:

DB_PASSWORD=tu_contraseña_de_postgres
JWT_SECRET=medcore-desarrollo-cambiar-en-produccion-2026

### 3. Levantar la Aplicación
Abrí una terminal en la raíz del proyecto y ejecutá:

./mvnw spring-boot:run

(También podés ejecutar la clase BackendApplication.java directamente desde tu IDE).

La aplicación estará disponible en http://localhost:8080.

## Usuario Administrador Inicial

Flyway insertará automáticamente un usuario con rol ADMIN para que puedas empezar a probar el sistema sin bloqueos.

- Usuario: admin
- Contraseña: admin123

(Asegurate de cambiar esta contraseña si el sistema se despliega en un entorno real).

## Documentación de la API (Swagger)

Una vez que la aplicación esté corriendo, podés explorar y probar todos los endpoints interactivamente a través de Swagger UI:

http://localhost:8080/swagger-ui.html

## Módulos del Sistema

- Auth: Login y generación de tokens JWT. Control de acceso por roles (ADMIN, RECEPCION, MEDICO, ENFERMERIA).
- Pacientes: Registro centralizado, validación de documentos únicos y estado (ACTIVO / INACTIVO).
- Profesionales: Gestión de médicos con especialidades y matrículas únicas.
- Turnos: Asignación de citas con validación de superposición (horarios) y control de estados.
- Admisiones: Circuito de ingreso hospitalario (Programado o Espontáneo) con control de múltiples ingresos abiertos.
- Consultas: Registro de atención médica (Anamnesis, Diagnóstico). Los registros clínicos finalizados son inmutables.
- Internaciones: Asignación de camas con bloqueo pesimista (evita colisiones), alta médica y liberación automática de camas.
- Enfermería: Registro de observaciones clínicas exclusivamente para pacientes con internaciones activas.
- Auditoría: Trazabilidad de acciones críticas (login, modificaciones, accesos) sin exponer datos sensibles.

## Administración IT (Monitoreo)

El sistema incluye Spring Boot Actuator para la administración técnica. Los endpoints están protegidos y requieren el token de un usuario con rol ADMIN.

- Estado de salud y BD: GET /actuator/health
- Métricas de la JVM: GET /actuator/metrics

(Nota: Asegurate de enviar el token en el Header Authorization: Bearer <token> al consultar estas rutas).

## Ejecución de Tests

El proyecto cuenta con pruebas unitarias y de integración (usando base de datos H2 en memoria para no ensuciar la base de datos principal). Para ejecutarlas:

./mvnw test

## Backup y Restauración (Documentación IT)

Para respaldar la base de datos de forma lógica en PostgreSQL, ejecutá en tu terminal:

pg_dump -U postgres -d medcore_db -F c -b -v -f "backup_medcore.backup"

Para restaurar el respaldo en una base de datos limpia:

pg_restore -U postgres -d medcore_db -v "backup_medcore.backup"

## Diagramas y Modelo de Datos

El modelo de entidades y relaciones (ERD) y el diagrama de flujo del circuito de atención (desde el ingreso hasta el alta) se encuentran documentados en la carpeta /docs del repositorio.

## Licencia

Este proyecto está bajo la Licencia MIT. Consultá el archivo LICENSE para más detalles.

Desarrollado por Candela Silva.