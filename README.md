# Appwrite API Automation Test Suite

A REST API automation framework built with **Java**, **Rest Assured**, **TestNG**, and **Allure Reports** for testing Appwrite Database Document APIs.

This project demonstrates API automation skills through functional, validation, and security testing of Appwrite document operations.

---

## Features

- REST API testing with Rest Assured
- TestNG test execution and organization
- Allure reporting integration
- Positive and negative test scenarios
- Authentication and authorization validation
- Database document CRUD-related testing
- Response validation using Hamcrest assertions

---

## Tech Stack

- Java
- Maven
- Rest Assured
- TestNG
- Hamcrest
- Allure Reports

---

## Project Structure

```text
appwrite-api-tests-main
│
├── pom.xml
├── testng.xml
│
└── src
    └── test
        └── java
            └── com
                └── gabriel
                    ├── AppwriteConfig.java
                    └── AppwriteDocumentTest.java
```

---

## Test Coverage

### Functional Tests

- Create a document successfully
- Retrieve document collections
- Verify document data returned from API

### Validation Tests

- Missing required field validation
- Invalid request payload validation

### Security Tests

- Unauthorized request handling
- Missing API key validation

### Error Handling Tests

- Retrieve non-existent document
- Verify appropriate HTTP error responses

---

## Prerequisites

Before running the tests, ensure you have:

- Java 8 or later
- Maven installed
- Running Appwrite instance
- Existing Appwrite project
- Database and collection created
- Valid Appwrite API key

---

## Configuration

Update the values in `AppwriteConfig.java`:

```java
public static final String BASE_URL = "http://localhost/v1";
public static final String PROJECT_ID = "YOUR_PROJECT_ID";
public static final String API_KEY = "YOUR_API_KEY";
public static final String DATABASE_ID = "YOUR_DATABASE_ID";
public static final String COLLECTION_ID = "YOUR_COLLECTION_ID";
```

### Important

Do not commit real API keys to source control.

For production-quality projects, use:

- Environment variables
- Configuration files
- Secret managers

---

## Installation

Clone the repository:

```bash
git clone https://github.com/your-username/appwrite-api-tests.git
```

Navigate to the project:

```bash
cd appwrite-api-tests
```

Install dependencies:

```bash
mvn clean install
```

---

## Running Tests

Run all tests:

```bash
mvn test
```

Clean and run tests:

```bash
mvn clean test
```

Execute using the TestNG suite:

```bash
mvn test -DsuiteXmlFile=testng.xml
```

---

## Allure Reporting

Generate the report:

```bash
mvn allure:report
```

Open the report locally:

```bash
mvn allure:serve
```

The report includes:

- Test execution summary
- Pass/fail statistics
- Detailed test results
- Severity levels
- Feature and story grouping

---

## Example Validation

Example document creation validation:

```java
given()
    .contentType(ContentType.JSON)
.when()
    .post(endpoint)
.then()
    .statusCode(201)
    .body("name", equalTo("Test"))
    .body("$id", notNullValue());
```

---

## Project Goals

This project was created to demonstrate:

- API test automation skills
- REST Assured framework knowledge
- TestNG test organization
- API validation techniques
- Automated reporting with Allure
- QA Automation engineering fundamentals

---

## Future Improvements

Potential enhancements include:

- Environment-based configuration
- Update document test cases
- Delete document test cases
- Data-driven testing
- Request/response logging
- GitHub Actions CI/CD integration
- API schema validation
- Parallel test execution
- Test data cleanup utilities

---

## Author

Jordan Runyon

---

## License

This project is intended for educational, portfolio, and QA automation learning purposes.
