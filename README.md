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
- **Mapeo de Objetos:** MapStruct y Lombok
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

1. **CLIENTE_PERSONA_NATURAL**: Consulta y opera sus propios productos (cuentas, préstamos). Crea transferencias.
2. **CLIENTE_EMPRESA** (Representante): Visualiza todos los productos de la empresa.
3. **EMPLEADO_VENTANILLA**: Consulta saldo y estado para realizar transacciones de caja. Abre nuevas cuentas. No ve riesgos.
4. **EMPLEADO_COMERCIAL**: Gestiona clientes, crea solicitudes de productos (préstamos), pero no aprueba ni modifica saldos.
5. **EMPLEADO_EMPRESA**: (Operativo) Crea transferencias empresariales. Alto monto va a aprobación.
6. **SUPERVISOR_EMPRESA**: Aprueba o rechaza transferencias de alto monto creadas por el *Empleado de Empresa*.
7. **ANALISTA_INTERNO**: Aprueba/Rechaza y desembolsa préstamos. Accede a la bitácora completa. No realiza transferencias ni caja.

## ⚙️ Reglas de Negocio Destacadas

* **Doble Base de Datos**: El estado actual (Saldos, Estados de cuentas) se rige por SQL. Todo evento/modificación relevante se registra en la **Bitácora de Operaciones (MongoDB)**.
* **Flujo de Préstamos**: Solicitud (`EN_ESTUDIO`) -> Aprobación (`APROBADO` o `RECHAZADO` por el Analista) -> Desembolso (`DESEMBOLSADO` validando cuenta destino y aumentando saldo).
* **Flujo de Transferencias de Empresa**: Las transferencias de alto monto entran en estado `EN_ESPERA_APROBACION`. Si no son aprobadas por el *Supervisor de Empresa* en menos de 1 hora (60 mins), el sistema las cambia a `VENCIDA`.
* **Seguridad y Accesos**: Total restricción entre clientes; un cliente jamás podrá ver u operar la cuenta de otro.

## 🚀 Cómo Ejecutar el Proyecto

### Requisitos Previos
- Java 17 JDK
- Maven 3.8+
- MongoDB corriendo en `localhost:27017`

### Paso 1: Levantar la Aplicación
Abrir una terminal en la raíz del proyecto y ejecutar:
```bash
mvn spring-boot:run
```

*Nota: Para ejecutar sin MongoDB temporalmente, puedes excluir la autoconfiguración en el `application.properties`.*

### Paso 2: Explorar la API
- **Swagger UI (Documentación Interactiva):** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **Consola H2 (Base de Datos en Memoria):** [http://localhost:8080/h2-console](http://localhost:8080/h2-console) (JDBC URL: `jdbc:h2:mem:bancodb`)

## 🧪 Datos de Prueba y Flujo de Uso

El sistema incluye un script de inicialización (`DataSeeder`) que precarga los siguientes usuarios y cuentas:

### Usuarios Precargados (Contraseña para todos: `[usuario]123`, ej: `juan123`)
| Usuario | Rol |
|---|---|
| juan | CLIENTE_PERSONA_NATURAL |
| maria | CLIENTE_PERSONA_NATURAL |
| techsolutions | CLIENTE_EMPRESA |
| cajero | EMPLEADO_VENTANILLA |
| comercial | EMPLEADO_COMERCIAL |
| analista | ANALISTA_INTERNO |
| pedro_emp | EMPLEADO_EMPRESA |
| sofia_sup | SUPERVISOR_EMPRESA |

### Cuentas Precargadas
| Número | Titular | Saldo |
|---|---|---|
| 1001-0001 | Juan Pérez | $5,000,000 |
| 1001-0002 | María López | $2,000,000 |
| 2001-0001 | Tech Solutions | $50,000,000 |

### Flujo Típico para Pruebas (Postman / Swagger)

1. **Login (Obtener JWT):** 
   `POST /api/auth/login` con `{"username": "juan", "password": "juan123"}`. Copia el token de la respuesta.
2. **Transferencia Estándar:**
   Como `juan`, llama a `POST /api/transferencias` con cuenta origen `1001-0001` y destino `1001-0002`. Se ejecuta de inmediato.
3. **Solicitud de Préstamo:**
   Como `juan`, llama a `POST /api/prestamos` para crear una solicitud en estado `EN_ESTUDIO`.
4. **Aprobación de Préstamo:**
   Haz Login como `analista` (`analista123`), copia el nuevo token y llama a `PUT /api/prestamos/1/aprobar`. Luego `PUT /api/prestamos/1/desembolsar`.
5. **Aprobación de Transferencia de Empresa:**
   Haz login como `pedro_emp`, intenta transferir 10 millones desde la cuenta `2001-0001`. Quedará `EN_ESPERA_APROBACION`. Luego haz login como `sofia_sup` y apruébala en `PUT /api/transferencias/1/aprobar`.
6. **Auditoría NoSQL:**
   Como `analista`, revisa todos los movimientos llamando a `GET /api/bitacora`.
