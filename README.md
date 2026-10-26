# Appwrite API Automation Test Suite

An API test suite for the [Appwrite](https://appwrite.io/) Databases REST API, written in Java with **REST Assured**, **TestNG**, and **Allure** reporting. It runs against a self-hosted Appwrite instance and checks document creation, listing, retrieval, input validation, and API-key enforcement.

---

## Tech Stack

| Tool | Version | Purpose |
|------|---------|---------|
| Java | 11+ | Language (TestNG 7.9 requires Java 11 or later) |
| Maven | 3.x | Build and dependency management |
| REST Assured | 5.4.0 | HTTP requests and response assertions |
| TestNG | 7.9.0 | Test runner and ordering |
| Hamcrest | 2.2 | Matchers for response body checks |
| Allure (TestNG adapter) | 2.27.0 | Test reporting, with Epic/Feature/Story/Severity labels |
| AspectJ Weaver | 1.9.22 | Java agent Allure uses to capture test steps |

---

## Test Cases

All tests live in `AppwriteDocumentTest.java` and target
`/v1/databases/{databaseId}/collections/{collectionId}/documents`.
They run in the order below (`priority` 1–5).

| # | Test | Request | Expected result | Allure story / severity |
|---|------|---------|-----------------|-------------------------|
| 1 | `testCreateDocument_Returns201` | `POST` a document with `name: "Test"` | `201`, body `name` equals `"Test"`, `$id` is present | Create Document / Critical |
| 2 | `testListDocuments_Returns200` | `GET` the collection's documents | `200`, `documents` not empty, `total` > 0 | Read Documents / Critical |
| 3 | `testListDocuments_NoKey_Returns401` | `GET` documents with the project header but **no API key** | `401` | Security / Blocker |
| 4 | `testCreateDocument_MissingRequiredField_Returns400` | `POST` a document with an empty `data` object | `400` | Validation / Normal |
| 5 | `testGetDocument_InvalidId_Returns404` | `GET` a document ID that does not exist | `404` | Read Documents / Normal |

Coverage summary: one positive create, one positive list, one auth-negative, one validation-negative, and one not-found case. Update and delete are not covered yet.

---

## Project Structure

```text
appwrite-api-tests
├── pom.xml              # Dependencies, Surefire (runs testng.xml), Allure Maven plugin
├── testng.xml           # Suite definition: "Document CRUD Tests"
└── src/test/java/com/gabriel
    ├── AppwriteConfig.java        # Project, key, database and collection IDs
    └── AppwriteDocumentTest.java  # The five API tests
```

---

## Prerequisites

1. **Java 11+** and **Maven 3.x**
2. **A self-hosted Appwrite instance** reachable at `http://127.0.0.1:80/v1` (for example, Appwrite's Docker install)
3. In that instance:
   - A **project**
   - A **database** with one **collection**
   - A collection attribute **`name`** (string) marked **required**. Test 1 writes it, and test 4 depends on it being required to get a `400`.
   - Collection permissions that do **not** allow guest/`any` read access, so test 3 gets a `401`
   - An **API key** with document read and write scopes

---

## Configuration

Set your IDs and key in `src/test/java/com/gabriel/AppwriteConfig.java`:

```java
public static final String PROJECT_ID    = "YOUR_PROJECT_ID";
public static final String API_KEY       = "YOUR_API_KEY";
public static final String DATABASE_ID   = "YOUR_DATABASE_ID";
public static final String COLLECTION_ID = "YOUR_COLLECTION_ID";
```

The target host is set in `AppwriteDocumentTest.setup()`, not in `AppwriteConfig.BASE_URL` (that constant is currently unused):

```java
RestAssured.baseURI  = "http://127.0.0.1";
RestAssured.port     = 80;
RestAssured.basePath = "/v1";
```

`setup()` also clears JVM proxy settings so requests to the local instance aren't routed through a system proxy.

> **Don't commit a real API key.** Keep placeholder values in the repo and set real ones only in your local copy. Moving these values to environment variables is on the roadmap below.

---

## Running the Tests

```bash
git clone https://github.com/jjr6639/appwrite-api-tests.git
cd appwrite-api-tests
mvn clean test
```

Surefire is configured in `pom.xml` to run `testng.xml`, so `mvn test` runs the whole suite. The tests need a running Appwrite instance; without one they fail with connection errors. To compile and download dependencies without running tests, use `mvn clean install -DskipTests`.

---

## Allure Reports

After a test run:

```bash
mvn allure:serve    # build the report and open it in a browser
mvn allure:report   # build a static report only
```

Tests are labeled with Allure annotations (Epic: *Appwrite REST API*, Feature: *Document CRUD*, plus a Story, Severity, and Description on each test), so the report groups results by story and severity.

---

## Known Limitations

- **Test order matters.** Test 2 expects at least one document, which test 1 creates. Run individually against an empty collection, test 2 fails.
- **No cleanup.** Each run leaves the document created by test 1 in the collection.
- **Hard-coded configuration.** IDs, key, and host live in source code rather than environment variables.
- **No update or delete tests** yet.

## Roadmap

- Load configuration from environment variables
- Add `PATCH` (update) and `DELETE` tests, and delete created documents in an `@AfterClass` cleanup
- Make tests independent of execution order
- Run the suite in GitHub Actions against an Appwrite container

---

## Author

**Jordan J. Runyon** · [GitHub](https://github.com/jjr6639) · [LinkedIn](https://www.linkedin.com/in/jordan-runyon)

Built as a QA automation portfolio project.
