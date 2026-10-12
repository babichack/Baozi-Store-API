# Baozi Store API

A Java 17+ Spring Boot REST API for customers, products, and orders at the fictional Baozi Store.

## Run

From this directory:

```bash
mvn spring-boot:run
```

The API listens on port `8080` by default. Set `PORT` to use another port.

## REST endpoints

All endpoints use JSON.

| Resource | Endpoints |
| --- | --- |
| Customers | `GET/POST /api/clientes`, `GET/PUT/DELETE /api/clientes/{id}` |
| Products | `GET/POST /api/produtos`, `GET/PUT/DELETE /api/produtos/{id}` |
| Orders | `GET/POST /api/pedidos`, `GET/PUT/DELETE /api/pedidos/{id}` |

Example customer:

```json
{
  "nome": "Ana Silva",
  "email": "ana@example.com",
  "telefone": "+55 11 99999-0000"
}
```

Example product:

```json
{
  "nome": "Baozi de porco",
  "descricao": "Pão chinês cozido no vapor, recheado com porco",
  "preco": 12.50,
  "estoque": 20
}
```

Example order (use existing customer and product IDs):

```json
{
  "clienteId": 1,
  "produtoIds": [1],
  "status": "RECEBIDO"
}
```

The H2 database is in-memory and resets when the application stops. The H2 console is available at `/h2-console` while the application is running (JDBC URL: `jdbc:h2:mem:baozistore`, username: `sa`, blank password).
