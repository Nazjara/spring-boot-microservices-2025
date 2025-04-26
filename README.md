# Spring Boot Microservices Project

This repository contains a set of microservices built with Spring Boot that demonstrate a banking application architecture.

## Project Overview

This project consists of three microservices and a parent POM configuration:

1. **Accounts Microservice**: Manages customer information and their associated accounts
2. **Cards Microservice**: Manages credit/debit card information
3. **Loans Microservice**: Manages loan information
4. **Parent POM**: Provides common configuration and dependency management for all microservices

## Technology Stack

- Java 21
- Spring Boot 3.4.4
- Spring Data JPA
- RESTful APIs
- OpenAPI (Swagger) for API documentation
- Maven for dependency management
- Lombok for reducing boilerplate code

## Microservices Architecture

### Common Features

All microservices follow a similar architecture with:

- REST Controllers for API endpoints
- Service layer for business logic
- Repository layer for data access
- Entity classes for data modeling
- DTOs for data transfer
- Mappers for entity-DTO conversion
- Exception handling
- Consistent API design

### API Endpoints

Each microservice provides the following REST endpoints:

- `POST /api/create` - Create a new resource
- `GET /api/fetch` - Retrieve a resource
- `PUT /api/update` - Update an existing resource
- `DELETE /api/delete` - Delete a resource

### Accounts Microservice

The Accounts microservice manages customer information and their associated bank accounts. It provides APIs to:

- Create new customers with accounts
- Fetch account details
- Update account information
- Delete accounts

### Cards Microservice

The Cards microservice manages credit and debit card information. It provides APIs to:

- Create new card records
- Fetch card details
- Update card information
- Delete card records

### Loans Microservice

The Loans microservice manages loan information. It provides APIs to:

- Create new loan records
- Fetch loan details
- Update loan information
- Delete loan records

## Project Structure

```
spring-boot-microservices-2025/
├── accounts/                  # Accounts microservice
├── cards/                     # Cards microservice
├── loans/                     # Loans microservice
├── parent-pom/                # Parent POM configuration
└── README.md                  # Project documentation
```

Each microservice follows a standard Spring Boot project structure:

```
microservice/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/nazjara/
│   │   │       ├── controller/    # REST controllers
│   │   │       ├── service/       # Business logic
│   │   │       ├── repository/    # Data access
│   │   │       ├── entity/        # JPA entities
│   │   │       ├── dto/           # Data Transfer Objects
│   │   │       ├── mapper/        # Entity-DTO mappers
│   │   │       ├── exception/     # Exception handling
│   │   │       └── *Application.java  # Main application class
│   │   └── resources/
│   │       └── application.properties  # Configuration
│   └── test/                      # Test classes
└── pom.xml                        # Maven configuration
```

## How to Run

Each microservice can be run independently using Maven:

```bash
cd <microservice-directory>
mvn spring-boot:run
```

## API Documentation

API documentation is available via Swagger UI at:

```
http://localhost:<port>/swagger-ui.html
```

Replace `<port>` with the port number of the respective microservice.