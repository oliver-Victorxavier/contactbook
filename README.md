<div align="center">

# 📇 Contact Book API

<p>
  <a href="https://github.com/oliver-Victorxavier/contactbook/actions/workflows/main.yml">
    <img src="https://github.com/oliver-Victorxavier/contactbook/actions/workflows/main.yml/badge.svg" alt="Build Status">
  </a>
  <a href="https://github.com/oliver-Victorxavier/contactbook/blob/main/LICENSE">
    <img src="https://img.shields.io/badge/License-MIT-blue.svg" alt="License">
  </a>
</p>

</div>

A RESTful API for contact management, built with Java and Spring Boot. This project exemplifies the principles of **Clean Architecture** and **Ports & Adapters**, is fully containerized with Docker, and features a robust CI/CD pipeline with GitHub Actions for automated builds, testing, and deployments.

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Project Architecture](#-project-architecture)
- [Technologies Used](#-technologies-used)
- [Setup and Execution](#-setup-and-execution)
- [API Documentation](#-api-documentation)
- [Author and Contact](#-author-and-contact)
- [License](#-license)

---

## 🖼️ Overview

Below is a preview of the interactive API documentation generated with Swagger.

<div align="center">
  <img src="https://github.com/user-attachments/assets/b63ca037-d4ef-4fa2-81de-9c9b3b3afadf" alt="Swagger UI Preview" width="80%">
</div>

---

## ✨ Features

-   **✅ Full CRUD:** Create, Read, Update, and Delete operations for contacts.
-   **📍 Automatic Address Population:** Integration with the [ViaCEP](https://viacep.com.br/) API to fill in the complete address from a postal code.
-   **🔍 Search and Pagination:** List contacts with support for pagination, sorting, and searching by name or location.
-   **⬆️ Import / ⬇️ Export:** Import contacts from a `.csv` file and export the complete list to `.pdf` and `.xlsx` (Excel) formats via a unified endpoint.
-   **🐳 Containerized Environment:** The entire environment (API, Database, Admin) is orchestrated with Docker Compose.
-   **🚀 Automated CI/CD:** A pipeline with GitHub Actions that tests, builds, and publishes a uniquely tagged application image to Docker Hub on every push to `main`.
-   **📚 Interactive Documentation:** The API is documented with Swagger (OpenAPI 3.0), with schemas defined at the DTO level to ensure a clean domain.

---

## 📂 Project Architecture

The project is strictly structured following the **Clean Architecture** and **Ports & Adapters** patterns. This design isolates the core business logic from external concerns like frameworks, databases, and APIs, ensuring high maintainability and testability.

-   `domain`: The absolute core of the application. It contains pure, framework-agnostic business entities and the interfaces (`Ports`) that define the contracts for external interactions (like persistence).
-   `application`: This layer orchestrates the use cases (Application Services). It depends on the `domain` layer's interfaces and defines its own `Ports` for driving the application (e.g., `ContactService`).
-   `infrastructure`: Provides the concrete implementations (`Adapters`) for the `Ports` defined in the inner layers. This includes database repositories, HTTP clients for external services, and implementations for file import/export.
-   `presentation`: The outermost layer, responsible for handling web requests (REST Controllers) and presenting data to the user. It depends only on the `application` layer.

<details>
  <summary><strong>Click to expand and view the code structure</strong></summary>

```bash
└── src/main/java/com/victorxavier/contactbook
    ├── presentation
    │   └── controller          # Presentation Layer (REST Controllers)
    ├── application
    │   ├── dto                 # Data Transfer Objects (Request/Response)
    │   ├── mapper              # Mappers (DTO <-> Domain)
    │   ├── port                # Ports (Interfaces defining contracts)
    │   │   ├── in              #   - Driving Ports (for use cases)
    │   │   └── out             #   - Driven Ports (for external services)
    │   └── service             # Application Services (Use Case Implementation)
    ├── domain
    │   ├── entity              # Domain Entities (Pure Business Objects)
    │   ├── repository          # Domain Repository Interfaces (Persistence Port)
    │   └── service             # Domain Services (Core Business Logic)
    ├── infrastructure
    │   ├── client              # HTTP Clients for external APIs (e.g., ViaCEP)
    │   ├── exception           # Global Exception Handling
    │   ├── persistence         # JPA/Database specific implementations
    │   │   ├── adapter         #   - Persistence Adapter (Implements Domain Repository)
    │   │   ├── entity          #   - JPA Entities (@Entity)
    │   │   ├── mapper          #   - Mappers (JPA Entity <-> Domain)
    │   │   └── repository      #   - Spring Data JPA Repositories
    │   └── service             # Infrastructure Services (Export Adapters)
    └── ContactbookApplication.java
```

</details>

---

## 💻 Technologies Used

| Category                 | Technology                                                              |
| ------------------------ | ----------------------------------------------------------------------- |
| **Back-end**             | Java 21, Spring Boot 3, Spring Data JPA, Hibernate, Maven               |
| **Database**             | PostgreSQL                                                              |
| **DevOps & Documentation** | Docker, Docker Compose, GitHub Actions (CI/CD), OpenAPI 3.0 (Springdoc) |

---

## 🚀 Setup and Execution

The development environment is fully managed with Docker, ensuring consistency and simplicity.

### Prerequisites
-   [Git](https://git-scm.com/)
-   [Docker](https://www.docker.com/) & [Docker Compose](https://docs.docker.com/compose/)

### Step-by-Step

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/oliver-Victorxavier/contactbook.git
    cd contactbook
    ```

2.  **Start the environment with Docker Compose:**
    This command will build the application image and start all defined services in the `compose.yml` file (API, Database, and PgAdmin) in detached mode (-d).
    ```bash
    docker-compose up -d
    ```

3.  **Verify that the containers are running:**
    ```bash
    docker-compose ps
    ```
    You should see the three services (`contactbook-app`, `postgres-db`, `pgadmin`) with an `Up` status.

4.  **Access the services:**
    -   **API Documentation (Swagger):** `http://localhost:8080/swagger-ui/index.html`
    -   **PgAdmin (Database Interface):** `http://localhost:16543` (login: `admin@gmail.com`, password: `admin`)

    <details>
      <summary><strong>How to Connect PgAdmin to the Database (Step-by-Step)</strong></summary>

    1. After logging into PgAdmin, right-click on **Servers** in the side menu and go to **Register -> Server...**.
    2. In the **General** tab, give your connection a name (e.g., `contactbook-local`).
    3. Switch to the **Connection** tab and fill in the following details (from the `compose.yml` file):
        - **Host name/address:** `postgres-db`
        - **Port:** `5432`
        - **Maintenance database:** `contactbook`
        - **Username:** `admin`
        - **Password:** `admin`
    4. Click **Save**. You can now browse the tables in the `contactbook` database.
       </e_details>

---

## 📖 API Documentation

The complete and interactive endpoint documentation is available via Swagger UI after starting the project. You can test all API features directly from your browser.

<details>
  <summary><strong>Click to expand and view the main endpoints</strong></summary>

| Method   | Endpoint                     | Description                               |
| :------- | :--------------------------- | :---------------------------------------- |
| `GET`    | `/api/contacts`              | Lists contacts with pagination and search. |
| `POST`   | `/api/contacts`              | Creates a new contact.                    |
| `GET`    | `/api/contacts/{id}`         | Finds a contact by ID.                    |
| `PUT`    | `/api/contacts/{id}`         | Updates a contact by ID.                  |
| `DELETE` | `/api/contacts/{id}`         | Deletes a contact by ID.                  |
| `GET`    | `/api/contacts/export`       | Exports contacts. Use `?format=pdf` or `?format=excel`. |
| `POST`   | `/api/contacts/import`       | Imports contacts from a CSV file.         |

</details>

---

## 👨‍💻 Author and Contact

by **Victor Xavier**. Feel free to get in touch!

<a href="https://www.linkedin.com/in/victor-xavier-/" target="_blank">
  <img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" alt="LinkedIn logo"  />
</a>
<a href="mailto:victor.xavier.oliver@gmail.com">
  <img src="https://img.shields.io/badge/Email-D14836?style=for-the-badge&logo=gmail&logoColor=white" alt="Email logo" />
</a>

---

## 📜 License

This project is licensed under the MIT License. See the [LICENSE](https://github.com/oliver-Victorxavier/contactbook/blob/main/LICENSE) file for more details.