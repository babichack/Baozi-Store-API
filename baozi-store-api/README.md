# Baozi Store API

Java 17+ REST API built with Spring Boot, Spring Data JPA, and H2.

## Run

```bash
mvn spring-boot:run
```

The API listens on port `8080` by default. Set `PORT` to use another port. H2 is an in-memory database, so data is cleared when the application stops.

## Endpoints

Each entity supports `POST` to create, `GET` to list, `GET /{id}` to retrieve by ID, and `DELETE /{id}` to delete.

- `/api/clientes`
- `/api/produtos`
- `/api/pedidos`

## Request bodies

Create a customer:

```json
{
  "nome": "Ana Silva",
  "clienteDesde": "2026-10-12"
}
```

Create a product:

```json
{
  "nome": "Baozi de porco",
  "preco": 12.50,
  "estoque": true
}
```

Create an order using existing IDs:

```json
{
  "clienteId": 1,
  "produtoId": 1,
  "quantidade": 2
}
```
