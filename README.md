<div align="center">

# 📇 Contact Book API

<p>
  <a href="https://github.com/oliver-Victorxavier/contactbook/actions/workflows/prod.yml">
    <img src="https://github.com/oliver-Victorxavier/contactbook/actions/workflows/prod.yml/badge.svg" alt="Build Status">
  </a>
  <a href="https://github.com/oliver-Victorxavier/contactbook/blob/main/LICENSE">
    <img src="https://img.shields.io/badge/License-MIT-blue.svg" alt="License">
  </a>
</p>

</div>

A RESTful API for contact management, built with Java and Spring Boot. The project is fully containerized with Docker and features a CI/CD pipeline configured with GitHub Actions for automated builds and deployments.

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
  <img src="URL_TO_YOUR_IMAGE_HERE" alt="Swagger UI Preview" width="80%">
</div>

---

## ✨ Features

-   **✅ Full CRUD:** Create, Read, Update, and Delete operations for contacts.
-   **📍 Automatic Address Population:** Integration with the [ViaCEP](https://viacep.com.br/) API to fill in the complete address from a postal code.
-   **🔍 Search and Pagination:** List contacts with support for pagination, sorting, and searching by name.
-   **⬆️ Import / ⬇️ Export:** Import contacts from a `.csv` file and export the complete list to `.pdf` and `.xlsx` (Excel) files.
-   **🐳 Containerized Environment:** The entire environment (API, Database, Admin) is orchestrated with Docker Compose.
-   **🚀 Automated CI/CD:** A pipeline with GitHub Actions that tests, builds, and publishes the application image to Docker Hub.
-   **📚 Interactive Documentation:** The API is documented with Swagger (OpenAPI 3.0).

---

## 📂 Project Architecture

The project is structured using principles of **Clean Architecture** and **Domain-Driven Design (DDD)**. This approach aims for low coupling and high cohesion by separating responsibilities into four main layers:

-   `presentation`: The outermost layer, responsible for handling requests (Controllers) and presenting data.
-   `application`: Orchestrates the application's use cases, acting as a mediator between the presentation and domain layers.
-   `domain`: The core of the application. It contains the entities, core business logic, and repository interfaces.
-   `infrastructure`: The innermost layer, containing implementations for external technologies, such as the HTTP client for ViaCEP, persistence configuration, and exception handling.

<details>
  <summary>Click to expand and view the code structure</summary>
  
```bash
└── src/main/java/com/victorxavier/contactbook
    ├── presentation
    │   └── controller      # Presentation Layer (REST Controllers)
    ├── application
    │   ├── dto             # Data Transfer Objects (Requests/Responses)
    │   ├── mapper          # Mappers between DTOs and Entities
    │   └── service         # Application use case orchestration
    ├── domain
    │   ├── entity          # Domain entities and business rules
    │   ├── repository      # Repository interfaces (persistence contracts)
    │   └── service         # Core domain logic services
    ├── infrastructure
    │   ├── client          # Clients for external services (e.g., ViaCEP)
    │   ├── exception       # Global exception handling
    │   └── service         # Infrastructure service implementations (e.g., export)
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
  <summary>Click to expand and view the main endpoints</summary>
  
| Method   | Endpoint                     | Description                               |
| :------- | :--------------------------- | :---------------------------------------- |
| `GET`    | `/api/contacts`              | Lists contacts with pagination and search. |
| `POST`   | `/api/contacts`              | Creates a new contact.                    |
| `GET`    | `/api/contacts/{id}`         | Finds a contact by ID.                    |
| `PUT`    | `/api/contacts/{id}`         | Updates a contact by ID.                  |
| `DELETE` | `/api/contacts/{id}`         | Deletes a contact by ID.                  |
| `GET`    | `/api/contacts/export/pdf`   | Exports all contacts to PDF.              |
| `GET`    | `/api/contacts/export/excel` | Exports all contacts to Excel.            |
| `POST`   | `/api/contacts/import`       | Imports contacts from a CSV file.         |

</details>

---

## 👨‍💻 Author and Contact

Made with ❤️ by **Victor Xavier**. Feel free to get in touch!

<a href="https://www.linkedin.com/in/victor-xavier-/" target="_blank">
  <img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" alt="LinkedIn logo"  />
</a>
<a href="mailto:victor.xavier.oliver@gmail.com">
  <img src="https://img.shields.io/badge/Email-D14836?style=for-the-badge&logo=gmail&logoColor=white" alt="Email logo" />
</a>

---

## 📜 License

This project is licensed under the MIT License. See the [LICENSE](https://github.com/oliver-Victorxavier/contactbook/blob/main/LICENSE) file for more details.