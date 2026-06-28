# FoodLink Backend

Plataforma de gestión y redistribución de excedentes alimentarios. Conecta comercios con excedentes próximos a caducar con beneficiarios y compradores, mediante venta, donación o retiro directo.

![SCRUM-18]

## Stack tecnológico

| Tecnología | Versión | Uso |
|---|---|---|
| Spring Boot | 3.2.5 | Framework principal |
| Java | 21 LTS | Lenguaje (records, sealed classes, pattern matching) |
| PostgreSQL + PostGIS | 16 | Persistencia relacional y geoespacial |
| Redis | 7 | Caché de reservas |
| Flyway | — | Migraciones de base de datos |
| MapStruct | 1.5.5.Final | Mapeo entre capas |
| Lombok | 1.18.32 | Reducción de boilerplate |
| Spring AI MCP Server | 1.0.0-M6 | Exposición de capacidades vía Model Context Protocol |
| SpringDoc OpenAPI | 2.5.0 | Documentación de la API REST |
| JJWT | 0.12.5 | Autenticación basada en JWT |
| Testcontainers | 1.19.8 | Tests de integración con PostgreSQL real |

## Arquitectura

El proyecto sigue **Arquitectura Hexagonal (Ports & Adapters)**, organizada en tres capas con dependencias en una sola dirección: `infrastructure` → `application` → `domain`.

- **`domain`** — Núcleo del negocio. Entidades, value objects, eventos, servicios de dominio y puertos (interfaces). No depende de Spring, JPA ni ningún framework externo.
- **`application`** — Casos de uso. Orquesta el dominio, expone DTOs y mappers. No contiene lógica de negocio ni de HTTP.
- **`infrastructure`** — Adaptadores. Implementa los puertos del dominio: controladores REST, servidor MCP, persistencia JPA, Redis, pagos, notificaciones e IA.

## Estructura de carpetas

```
src/main/java/com/foodlink/
├── domain/
│   ├── model/
│   │   ├── lote/
│   │   │   └── exception/
│   │   ├── comercio/
│   │   ├── beneficiario/
│   │   ├── comprador/
│   │   └── shared/
│   ├── port/
│   │   ├── input/
│   │   └── output/
│   ├── service/
│   └── event/
├── application/
│   ├── usecase/
│   │   ├── lote/
│   │   ├── comercio/
│   │   ├── beneficiario/
│   │   ├── comprador/
│   │   ├── pago/
│   │   └── impacto/
│   ├── dto/
│   │   ├── request/
│   │   └── response/
│   └── mapper/
└── infrastructure/
    ├── adapter/
    │   ├── input/
    │   │   ├── rest/
    │   │   │   ├── controller/
    │   │   │   └── mapper/
    │   │   └── mcp/
    │   └── output/
    │       ├── persistence/
    │       │   ├── entity/
    │       │   ├── repository/
    │       │   └── mapper/
    │       ├── notification/
    │       ├── payment/
    │       └── ai/
    └── config/

src/main/resources/
└── db/migration/

src/test/java/com/foodlink/
├── domain/
│   ├── model/
│   │   ├── lote/
│   │   └── comercio/
│   └── service/
├── application/
│   └── usecase/
│       ├── lote/
│       └── comercio/
└── infrastructure/
    └── adapter/
        ├── input/rest/
        └── output/persistence/

src/test/resources/
```

## SCRUM-18 — Estructura base de la arquitectura hexagonal

**Qué se hizo:** se definió la estructura base de carpetas del proyecto siguiendo Arquitectura Hexagonal, junto con el `pom.xml` inicial.

**Qué incluye:**
- `pom.xml` con todas las dependencias del stack (web, seguridad, persistencia, caché, IA, documentación, testing).
- Estructura completa de paquetes para `domain`, `application` e `infrastructure`, con `.gitkeep` en cada carpeta vacía.
- Separación clara de capas respetando el sentido de las dependencias.

**Criterios de aceptación cumplidos:**
- ✅ La estructura respeta la Arquitectura Hexagonal (Ports & Adapters).
- ✅ Separación clara entre `domain`, `application` e `infrastructure`.
- ✅ Convenciones de nombres en español, siguiendo el lenguaje ubicuo del dominio.
- ✅ Dependencias iniciales configuradas en `pom.xml`.

## Prerrequisitos

- Java 21
- Maven 3.8+
- PostgreSQL 16 (con extensión PostGIS)
- Redis 7

## Cómo clonar y compilar

```bash
git clone <url-del-repositorio>
cd foodlink-backend
mvn clean install -DskipTests
```

## Variables de entorno

| Variable | Descripción | Ejemplo |
|---|---|---|
| `DB_HOST` | Host del servidor PostgreSQL | `localhost` |
| `DB_PORT` | Puerto del servidor PostgreSQL | `5432` |
| `DB_NAME` | Nombre de la base de datos | `foodlink` |
| `DB_USER` | Usuario de la base de datos | `foodlink` |
| `DB_PASSWORD` | Contraseña de la base de datos | `foodlink` |
| `REDIS_HOST` | Host del servidor Redis | `localhost` |
| `REDIS_PORT` | Puerto del servidor Redis | `6379` |
| `JWT_SECRET` | Clave secreta para firmar tokens JWT | `cambia-este-valor-en-produccion` |
| `JWT_EXPIRATION` | Tiempo de expiración del token en milisegundos | `3600000` |
| `OPENAI_API_KEY` | Clave de API para Spring AI / OpenAI | `sk-...` |
