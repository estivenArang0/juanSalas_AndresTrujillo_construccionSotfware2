# EVALUACIÓN - juanSalas_AndresTrujillo_construccionSotfware2

## Información General
- **Estudiante(s):** Juan Salas / Andrés Trujillo (estivenArang0)
- **Rama evaluada:** develop
- **Fecha de evaluación:** 2026-03-23

---

## Tabla de Calificación

| # | Criterio | Peso | Puntaje (1–5) | Nota ponderada |
|---|---|---|---|---|
| 1 | Modelado de dominio | 25% | 2 | 0.50 |
| 2 | Relaciones entre entidades | 15% | 2 | 0.30 |
| 3 | Uso de Enums | 15% | 5 | 0.75 |
| 4 | Manejo de estados | 5% | 4 | 0.20 |
| 5 | Tipos de datos | 5% | 3 | 0.15 |
| 6 | Separación Usuario vs Cliente | 10% | 3 | 0.30 |
| 7 | Bitácora | 5% | 5 | 0.25 |
| 8 | Reglas básicas de negocio | 5% | 2 | 0.10 |
| 9 | Estructura del proyecto | 10% | 3 | 0.30 |
| 10 | Repositorio | 10% | 1 | 0.10 |
| **TOTAL** | | **100%** | | **2.95** |

## Penalizaciones
- Ninguna (código en inglés).

## Bonus
- Sin herencia completa (`Customers` extiende `Person` pero es clase vacía).
- +0.1 reconocimiento por `AuditLog` bien modelado con `Map<String, Object>` y `@Builder`.

## Nota Final: 3.0 / 5.0

---

## Análisis por Criterio

### 1. Modelado de dominio — 2/5
Clases presentes: `Person`, `Customers` (vacía), `User`, `Loan`, `Transfer`, `AuditLog`, `BankProduct`. **Falta entidad `BankAccount`** — existe `AccountStatus` y `AccountType` como enums pero no hay clase `BankAccount`. La clase `Customers` está vacía (solo extiende `Person`). Identificación mínima del dominio.

### 2. Relaciones entre entidades — 2/5
No hay relaciones visibles entre `Customers`, `BankAccount` (inexistente), `Loan` y `Transfer`. La clase `AuditLog` tiene `userId` y `affectedProductId` como longs/strings. Las relaciones son prácticamente inexistentes.

### 3. Uso de Enums — 5/5
Conjunto de enums muy completo: `AccountStatus`, `AccountType`, `Currency`, `LoanStatus`, `LoanType`, `OperationType`, `ProductCategory`, `Role`, `TransferStatus`, `UserStatus`. Todos los catálogos esperados están definidos correctamente como enum.

### 4. Manejo de estados — 4/5
Los enums de estado están bien definidos. Sin embargo, la entidad `BankAccount` no existe, por lo que no se puede verificar que se usen en contexto. Los demás usan enums (`AuditLog` con `OperationType` ✓).

### 5. Tipos de datos — 3/5
`AuditLog` usa `Timestamp operationDateTime` (aceptable, aunque se prefiere `LocalDateTime`). `Person` usa `java.util.Date birthDate` en lugar de `LocalDate`. No se puede evaluar `BankAccount` por su ausencia.

### 6. Separación Usuario vs Cliente — 3/5
Tanto `Customers` como `User` extienden `Person`. Hay intención de separación: `User` tiene `username`, `password`, `role`, `status` (atributos de sistema), mientras `Customers` agrupa el concepto de cliente. Sin embargo, `Customers` está vacía y no hay una jerarquía `NaturalPersonClient`/`CompanyClient`.

### 7. Bitácora — 5/5
`AuditLog` es un modelo excelente: `OperationType operationType` (enum ✓), `Timestamp operationDateTime`, `Map<String, Object> details` ✓, con `@Builder` para construcción fluida, y comentarios que explican la estrategia de almacenamiento flexible (referenciando MongoDB). Incluso hay comentarios de ejemplo con datos de antes/después.

### 8. Reglas básicas de negocio — 2/5
Los comentarios referencian "Reglas" (R-001, R-022, R-026, R-038) indicando pensamiento en reglas de negocio. Sin embargo, no hay implementación de lógica de negocio en las entidades.

### 9. Estructura del proyecto — 3/5
Todo en `domain/models` flat. No hay sub-paquetes separados (ni enums en sub-paquete separado, ni jerarquías). Spring Boot configurado correctamente con Maven.

### 10. Repositorio — 1/5
- **Nombre:** `juanSalas_AndresTrujillo_construccionSotfware2` — correcto (aunque tiene typo en "Sotfware").
- **README:** Solo "proyectoEntregable" y el nombre del repositorio. Sin información de materia ni cómo ejecutar.
- **Commits:** En español ("actualización del proyecto"). Sin formato ADD/CHG.
- **Ramas:** Tiene `develop` ✓.
- **Tag:** No hay tag.

---

## Fortalezas
- Conjunto completo de enums — el más extenso del grupo.
- `AuditLog` excelentemente modelado con `Map<String, Object>` y `@Builder`.
- `Person` como clase base, `User` extiende `Person` correctamente.
- Los comentarios muestran pensamiento sobre reglas de negocio.
- Código completamente en inglés.

## Oportunidades de mejora
- **Agregar la clase `BankAccount`** — es la entidad más importante del dominio.
- Completar la clase `Customers` con campos de identificación.
- Crear jerarquía `NaturalPersonClient`/`CorporateClient` que extienda `Customers`.
- Cambiar `java.util.Date` por `LocalDate` y `Timestamp` por `LocalDateTime`.
- Agregar relaciones entre entidades (Cliente → Cuenta, Cuenta → Transferencia).
- Mejorar README con información de materia, integrantes y cómo ejecutar.
- Agregar commits con formato ADD/CHG y tag de entrega.
