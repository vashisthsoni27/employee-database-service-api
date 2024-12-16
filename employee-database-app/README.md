# Employee Management Service

## Overview
This project provides a RESTful API for managing employee data, roles, and projects. It utilizes Spring Boot for backend services and Springdoc OpenAPI to generate interactive API documentation.

## Prerequisites
- JDK 17 or higher
- Maven
- A running instance of the application
- Postman (for API testing)

## Setup

1. **Clone the repository**:
    ```bash
    git clone https://github.com/vashisthsoni27/employee-database-service-api.git
    cd employee-database-app
    ```

2. **Build the project**:
   If using Maven:
    ```bash
    mvn clean install
    ```

3. **Run the application**:
    ```bash
    mvn spring-boot:run
    ```

   The application will start at `http://localhost:8082`.

---

## Accessing the API Documentation

Once the server is running, you can access the generated API documentation in two formats:

### 1. OpenAPI JSON/YAML
- The raw OpenAPI specification (in JSON format) can be accessed at: http://localhost:8082/v3/api-docs


This provides a detailed structure of all available API endpoints.

### 2. Swagger UI (Interactive API Documentation)
- You can view and interact with the API using Swagger UI at: http://localhost:8082/swagger-ui.html

This page provides a user-friendly interface where you can:
- Explore the available endpoints.
- View request/response formats.
- Execute API calls directly from the browser.

### Example:

1. Open your browser and navigate to: http://localhost:8082/swagger-ui.html

2. You will see all available endpoints listed there, and you can interact with them directly.

---

## Using Postman to Test the API

You can use Postman to interact with the API endpoints directly. Here are the steps to import the Postman collection:

### 1. Download Postman Collection
- Download the Postman collection file (JSON format) from the project repository or https://github.com/vashisthsoni27/employee-database-service-api/blob/main/employee-database-app/Employee%20Database%20Service%20API.json.

### 2. Import the Collection into Postman
- Open Postman and click on the **Import** button in the top left corner.
- Select **Choose Files** and navigate to the downloaded Postman collection JSON file.
- Click **Open**, and the collection will be imported into Postman.

### 3. Configure Postman Environment
- You may need to configure your Postman environment to point to your local instance of the API. You can create a new environment with the following variables:
- **API_BASE_URL**: `http://localhost:8082`

### 4. Make Requests
Once the collection is imported and the environment is set up, you can start making requests. The Postman collection will include various pre-configured requests, including:

- **GET /api/employees** – Retrieve a list of all employees.
- **GET /api/employees/{id}** – Retrieve an employee by their ID.
- **POST /api/employees** – Add a new employee.
- **PUT /api/employees/{id}** – Update an existing employee.
- **DELETE /api/employees/{id}** – Delete an employee by ID.

You can execute these requests directly from Postman by selecting the request and clicking **Send**.

---

## API Endpoints

Here is a brief list of API endpoints available:

### Employees

- `GET /api/employees` - Get all employees
- `GET /api/employees/{id}` - Get employee by ID
- `POST /api/employees` - Add a new employee
- `PUT /api/employees/{id}` - Update employee information
- `DELETE /api/employees/{id}` - Delete employee by ID

### Roles

- `GET /api/roles` - Get all roles
- `GET /api/roles/{id}` - Get role by ID
- `POST /api/roles` - Add a new role
- `PUT /api/roles/{id}` - Update role
- `DELETE /api/roles/{id}` - Delete role by ID

### Projects

- `GET /api/projects` - Get all projects
- `GET /api/projects/{id}` - Get project by ID
- `POST /api/projects` - Add a new project
- `PUT /api/projects/{id}` - Update project
- `DELETE /api/projects/{id}` - Delete project by ID

---

## Code Coverage Report

The code coverage report can be found here (target/site/index.html).

Current code coverage report is here: https://github.com/vashisthsoni27/employee-database-service-api/blob/main/employee-database-app/Code_Coverage.png

To generate the report yourself, run:

```bash
mvn clean test
```

## Troubleshooting

- **Swagger UI not loading**: Ensure that `springdoc-openapi-ui` is correctly configured in the `pom.xml`. If the page is still not loading, check the server logs for any errors related to Springdoc.

- **CORS Issues**: If you're using Swagger UI from a different origin, ensure CORS (Cross-Origin Resource Sharing) is properly configured in your application.

---

## Conclusion

You now have a fully functional employee management service with a well-documented API! You can interact with the API using Swagger UI for a web-based interface or Postman for more advanced API testing.


