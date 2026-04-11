# EVALUACION 2 - juanSalas_AndresTrujillo_construccionSotfware2

## Informacion general
- Estudiante(s): Juan Salas, Andres Trujillo (usuarios GitHub: estivenArang0 — Juan Estiven Salas A.)
- Rama evaluada: develop
- Commit evaluado: 9d49601961818b71f812fd6f6237dc4867882716
- Fecha: 2026-04-11
- Nota: README.md solo contiene el titulo del repositorio sin listar integrantes por nombre completo.

---

## Tabla de calificacion

| # | Criterio | Peso | Puntaje (1-5) | Parcial |
|---|---|---|---|---|
| 1 | Modelado de dominio | 20% | 3 | 0.60 |
| 2 | Modelado de puertos | 20% | 1 | 0.20 |
| 3 | Modelado de servicios de dominio | 20% | 1 | 0.20 |
| 4 | Enums y estados | 10% | 3 | 0.30 |
| 5 | Reglas de negocio criticas | 10% | 1 | 0.10 |
| 6 | Bitacora y trazabilidad | 5% | 2 | 0.10 |
| 7 | Estructura interna de dominio | 10% | 2 | 0.20 |
| 8 | Calidad tecnica base en domain | 5% | 3 | 0.15 |
| | **Total base** | | | **1.85** |

### Calculo
Nota base = (3*20 + 1*20 + 1*20 + 3*10 + 1*10 + 2*5 + 2*10 + 3*5) / 100 = 185 / 100 = **1.85**

---

## Penalizaciones aplicadas

Ninguna penalizacion mayor aplicable.

---

## Nota final
**1.9 / 5.0**

---

## Hallazgos

### Criterio 1 - Modelado de dominio (3/5)
- Modelos en `domain/models/`: `AuditLog`, `BankProduct`, `Customers`, `Loan`, `Person`, `Transfer`, `User`.
- Clases de tipo/estado: `AccountStatus`, `AccountType`, `Currency`, `LoanStatus`, `LoanType`, `OperationType`, `ProductCategory`, `Role`, `TransferStatus`, `UserStatus`.
- **Problema:** Clase `Customers` (plural) — nombre inconsistente con convencion de entidades singulares (`Customer`).
- Falta: jerarquia explicita `NaturalPerson / CompanyClient` (solo hay `Person`).
- Falta: entidad `BankAccount` separada de `Customers` — la cuenta bancaria no esta modelada como entidad propia.
- `Person` es muy generica; no queda claro si extiende a cliente natural o empresa.

### Criterio 2 - Modelado de puertos (1/5)
- **No existe ninguna interfaz de puerto en el dominio.**
- No hay carpeta `domain/ports/` ni interfaces `*Port`.

### Criterio 3 - Servicios de dominio (1/5)
- **No existe ninguna clase de servicio de dominio.**

### Criterio 4 - Enums y estados (3/5)
- Clases de estado presentes: `AccountStatus`, `AccountType`, `Currency`, `LoanStatus`, `LoanType`, `OperationType`, `ProductCategory`, `Role`, `TransferStatus`, `UserStatus`.
- Se descuenta porque no se puede confirmar que todas sean `enum` Java — estan en `domain/models/` sin subcarpeta `enums/` separada.
- Si son enums correctamente declarados, la cobertura es muy completa.

### Criterio 5 - Reglas de negocio criticas (1/5)
- Sin servicios no hay reglas implementadas.

### Criterio 6 - Bitacora y trazabilidad (2/5)
- `AuditLog` presente como modelo de dominio.
- Sin puerto ni servicio de bitacora.

### Criterio 7 - Estructura interna de dominio (2/5)
- Solo existe `domain/models/` — sin `ports/` ni `services/`.
- El proyecto esta en carpeta `demo/` con estructura Maven, con una clase `Main.java` — sugiere que es un proyecto de consola/demo.

### Criterio 8 - Calidad tecnica (3/5)
- Nomenclatura en ingles.
- `Customers` (plural) es inconsistente.
- Sin typos graves detectados.

---

## Recomendaciones
1. Dividir `Person` en `NaturalPerson` y `CompanyClient` con jerarquia comun `Client`.
2. Crear entidad `BankAccount` como agregado central del dominio.
3. Renombrar `Customers` → `Customer` (singular).
4. Mover clases de estado a `domain/models/enums/` y asegurar que sean `enum`.
5. Crear `domain/ports/` con interfaces de contratos de salida.
6. Crear `domain/services/` con todos los casos de uso del enunciado.
7. Incluir nombres completos de integrantes en `README.md`.
