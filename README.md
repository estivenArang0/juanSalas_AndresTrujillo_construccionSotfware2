# 🏦 Sistema de Gestión Bancaria — Arquitectura Hexagonal

## Introducción y Objetivo del Proyecto
Este sistema es una aplicación robusta, segura y escalable para la gestión de clientes, productos y operaciones clave de una entidad bancaria. Funciona como el *core transaccional* del banco, permitiendo gestionar cuentas, préstamos y transferencias bajo estrictas reglas de negocio y flujos de aprobación.

El proyecto está diseñado bajo los principios de **Arquitectura Hexagonal (Puertos y Adaptadores)** y **Domain-Driven Design (DDD)**, lo cual garantiza la separación de responsabilidades y facilita el mantenimiento a largo plazo. Además, implementa un modelo de datos híbrido: **Relacional (SQL)** para el estado actual de las cuentas y productos, y **No Relacional (NoSQL)** para la inmutabilidad de la bitácora de operaciones (auditoría).  

## 🛠 Tecnologías y Arquitectura

- **Lenguaje:** Java 17
- **Framework:** Spring Boot 3.2
- **Base de Datos SQL (Transaccional):** PostgreSQL (producción) / H2 (desarrollo/pruebas)
- **Base de Datos NoSQL (Auditoría):** MongoDB
- **Seguridad:** Spring Security con JWT (JSON Web Tokens)
- **Mapeo de Objetos:** MapStruct (Lombok fue removido para mayor estabilidad en el proceso de compilación)
- **Documentación de API:** Swagger UI / OpenAPI 3

### Estructura de Directorios (Arquitectura Hexagonal)
```text
src/main/java/com/banco/
├── domain/              ← Núcleo puro (Cero dependencias externas o de Spring)
│   ├── model/           ← Entidades y Objetos de Valor (Reglas de negocio intrínsecas)
│   ├── repository/      ← Interfaces (Puertos de salida hacia bases de datos)
│   ├── service/         ← Servicios de Dominio (Orquestación puramente de negocio)
│   └── exception/       ← Excepciones de Dominio
├── application/         ← Capa de Aplicación (Casos de Uso)
│   ├── usecase/         ← Implementación de los flujos del sistema y orquestación
│   ├── port/            ← Puertos de entrada (Input Ports) y salida (Output Ports)
│   └── dto/             ← DTOs para Request y Response
├── adapter/             ← Capa de Infraestructura (Adaptadores)
│   ├── in/web/          ← Controladores REST (Driving Adapters)
│   │   ├── controller/  ← Endpoints de la API
│   │   └── security/    ← Filtros de JWT y configuración de autenticación
│   └── out/             ← Persistencia (Driven Adapters)
│       ├── persistence/ ← Entidades JPA (SQL), Repositorios de Spring Data y Mappers
│       └── nosql/       ← Documentos MongoDB (NoSQL) y Repositorios
├── config/              ← Configuración global de Spring (Beans, Seguridad, OpenAPI, CORS)
└── shared/              ← Clases de utilidad, GlobalExceptionHandler
```

## 👥 Roles y Permisos (Seguridad)

El sistema emplea seguridad basada en roles (RBAC) gestionada vía JWT:

1. **NATURAL_PERSON_CLIENT**: Consulta y opera sus propios productos. Crea transferencias.
2. **COMPANY_CLIENT**: (Representante) Visualiza todos los productos de la empresa.
3. **TELLER_EMPLOYEE**: Consulta saldo y estado para realizar transacciones de caja. Abre nuevas cuentas.
4. **COMMERCIAL_EMPLOYEE**: Gestiona clientes, crea solicitudes de productos (préstamos).
5. **COMPANY_EMPLOYEE**: (Operativo) Crea transferencias empresariales. Alto monto va a aprobación.
6. **COMPANY_SUPERVISOR**: Aprueba o rechaza transferencias de alto monto.
7. **INTERNAL_ANALYST**: Aprueba/Rechaza y desembolsa préstamos. Accede a la bitácora completa.

## ⚙️ Reglas de Negocio Destacadas

* **Doble Base de Datos**: Estado actual en SQL, auditoría inmutable en MongoDB.
* **Flujo de Préstamos**: Solicitud -> Aprobación -> Desembolso (validando cuenta destino).
* **Flujo de Transferencias de Empresa**: Montos altos (> 1M) requieren aprobación del Supervisor y expiran en 1 hora.
* **Seguridad**: Aislamiento total de datos entre clientes.

## 🚀 Cómo Ejecutar el Proyecto

### Requisitos Previos
- **Java 17 JDK**
- **Maven 3.8+**
- **MongoDB** corriendo en `localhost:27017` (opcional si se desactiva en `application.properties`)

### Paso 1: Configuración de Base de Datos
Por defecto, el proyecto usa **H2 (en memoria)** para facilitar el desarrollo rápido. Si deseas usar PostgreSQL:
1. Crea una base de datos llamada `bancodb`.
2. Actualiza `src/main/resources/application.properties` con tus credenciales.

### Paso 2: Levantar la Aplicación
Abrir una terminal en la carpeta `proyecto` y ejecutar:
```bash
mvn clean compile spring-boot:run
```

### Paso 3: Explorar la API
- **Swagger UI:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **Consola H2:** [http://localhost:8080/h2-console](http://localhost:8080/h2-console) (JDBC URL: `jdbc:h2:mem:bancodb`)

## 🧪 Datos de Prueba y Flujo de Uso

El sistema incluye un `DataSeeder` que precarga usuarios. La contraseña para todos es `[usuario]123`.

### Usuarios de Prueba
- **Clientes**: `juan`, `maria`, `techsolutions`
- **Empleados**: `cajero`, `comercial`, `analista`
- **Empresa**: `pedro_emp` (Operativo), `sofia_sup` (Supervisor)

### Flujo de Prueba Recomendado
1. **Autenticación**: `POST /api/auth/login` para obtener el JWT.
2. **Transferencia**: Prueba una transferencia de bajo monto (< 1M) y una de alto monto.
3. **Préstamos**: Crea una solicitud como `juan` y apruébala/desembólsala como `analista`.
4. **Auditoría**: Consulta la bitácora como `analista` en `GET /api/bitacora`.
