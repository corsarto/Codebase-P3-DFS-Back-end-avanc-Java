# EquipIT

EquipIT is a Spring Boot API and React client for managing internal IT equipment loans.

## Prerequisites

- JDK 25
- Maven 3.9+
- Node.js LTS

## Run the API

```bash
mvn spring-boot:run
```

The API is available at `http://localhost:8080` and Swagger UI at `http://localhost:8080/swagger-ui.html`.

## Run the client

```bash
cd frontend
npm install
npm run dev
```

## API

- `GET /api/equipment`
- `PUT /api/equipment/{id}/borrow`
- `PUT /api/equipment/{id}/return`

## Structure

The backend is organized around the equipment domain: entity and repository, service layer, DTO, and REST controller. The React client is kept separate and communicates with the API over HTTP.

## Features 

**Borrowing an item**: records the borrower's first and last name along with the current date, and marks the item as unavailable.
Rejected if the item does not exist (`EquipmentNotFoundException`) or if it is already on loan (`EquipmentUnavailableException`).

**Returning an item**: records the return date, clears the borrower and the loan date, and makes the item available again.
Denied if the item does not exist (`EquipmentNotFoundException`) or if it is already available (`EquipmentAvailableException`).