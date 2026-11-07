# AssetOps

AssetOps is a REST API for tracking industrial equipment and its maintenance lifecycle. It uses Java 21, Spring Boot, Spring Data JPA, Bean Validation, and PostgreSQL.

## Features

- Register equipment with unique asset tags and filter inventory by status or category.
- Create maintenance work orders with priority and equipment association.
- Enforce work-order state transitions and automatically reflect active maintenance on the equipment.
- Track work start and completion timestamps; keep equipment under maintenance while any active order remains.
- Return clear validation, conflict, and not-found responses.

## Run locally

Create a PostgreSQL database named `assetops_db`, then configure `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` as needed. The defaults connect to `jdbc:postgresql://localhost:5432/assetops_db` as `postgres`. Set `DDL_AUTO=validate` when using a managed schema.

```bash
./mvnw spring-boot:run
```

Run the unit tests with:

```bash
./mvnw test
```

## API

The server listens on port `8080`.

| Method | Endpoint | Purpose |
|---|---|---|
| `POST` | `/api/equipment` | Register equipment |
| `GET` | `/api/equipment` | List equipment; optional `status` and `category` filters |
| `GET` | `/api/equipment/{id}` | Get equipment details |
| `PATCH` | `/api/equipment/{id}/status` | Change an asset's operational status |
| `POST` | `/api/work-orders` | Open a maintenance work order |
| `GET` | `/api/work-orders` | List work orders; optional `equipmentId` filter |
| `GET` | `/api/work-orders/{id}` | Get a work order |
| `PATCH` | `/api/work-orders/{id}/status` | Move a work order through its lifecycle |

Example equipment request:

```json
{
  "assetTag": "PUMP-042",
  "name": "Cooling pump",
  "category": "PUMP",
  "location": "Plant A",
  "serialNumber": "SN-88420",
  "installationDate": "2023-05-18"
}
```

Example work-order request:

```json
{
  "equipmentId": "equipment-id",
  "title": "Replace bearing",
  "description": "Excessive vibration detected during inspection",
  "priority": "HIGH"
}
```

Allowed work-order transitions are `OPEN -> IN_PROGRESS -> COMPLETED`, with cancellation available from `OPEN` or `IN_PROGRESS`. Completed and cancelled orders are terminal.
Equipment can be marked `AVAILABLE`, `IN_USE`, or `DECOMMISSIONED`; maintenance status is controlled by work orders, and the previous operational status is restored after the last active order closes.
