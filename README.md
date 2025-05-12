# Bank Transaction Management System

A Spring Boot-based RESTful API for managing financial transactions. This application allows users to create, retrieve, update, and delete transactions with robust validation, caching, and in-memory storage.

---

## Features
- **Transaction Management**: Create, read, update, and delete transactions.
- **Validation**: Enforces constraints (e.g., positive amounts, non-blank types).
- **Pagination**: List transactions with `page` and `size` parameters.
- **Caching**: Uses Spring Cache to optimize read operations.
- **Error Handling**: Custom exceptions (e.g., duplicate transactions, missing transactions).
- **Containerization**: Docker support for easy deployment.
- **No Authentication**: Simplified for demonstration purposes.

---

## Technologies
- **Java 21**
- **Spring Boot 3.2.0**
- **Maven** (Build tool)
- **Docker** (Containerization)
- **Lombok** (Code simplification)

---

## Project Structure
```text
src/
├── main/
│   ├── java/com/example/bank/
│   │   ├── controller/      # REST API endpoints
│   │   ├── exception/       # Custom exceptions and global handler
│   │   ├── model/           # Transaction entity
│   │   ├── repository/      # In-memory data storage
│   │   └── service/         # Business logic
│   └── resources/           # Configuration files
└── test/                    # Unit and integration tests
```

---

## 🐋 Docker Usage

### Prerequisites
- [Docker](https://www.docker.com/get-started) installed
- JDK 21 (for local development builds)


### **1. Build the Image**
```bash
docker build -t transaction-app:1.0.0 .
```

### **2. Run the Container**
```bash
docker run -d -p 8080:8080 --name transaction-service transaction-app:1.0.0
```

### **3. Verify Running Container**
```bash
docker ps
```

### **4. Access the Application**
   API Endpoint: http://localhost:8080/api/transactions

View logs:

```bash
docker logs -f transaction-service
```

---

## External Libraries
### Lombok
Lombok is used to reduce boilerplate code (e.g., auto-generating `getter`/`setter`, `equals`, `hashCode`, and `toString` methods).  
**Key Annotations**:
- `@Data`: Generates all boilerplate for POJOs.
- Requires IDE plugin for compilation (see [setup instructions](#setup)).


