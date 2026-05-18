package com.gabriel;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("Appwrite REST API")
@Feature("Document CRUD")
public class AppwriteDocumentTest {

    private static final String DOCS_PATH =
        "/databases/" + AppwriteConfig.DATABASE_ID +
        "/collections/" + AppwriteConfig.COLLECTION_ID +
        "/documents";

    @BeforeClass
    public void setup() {
        System.setProperty("socksProxyHost", "");
        System.setProperty("socksProxyPort", "");
        System.setProperty("http.proxyHost", "");
        System.setProperty("http.proxyPort", "");
        System.setProperty("java.net.useSystemProxies", "false");
        RestAssured.baseURI  = "http://127.0.0.1";
        RestAssured.port     = 80;
        RestAssured.basePath = "/v1";
    }

    @Test(priority = 1)
    @Story("Create Document")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that a valid document creation request returns 201")
    public void testCreateDocument_Returns201() {
        given()
            .header("X-Appwrite-Project", AppwriteConfig.PROJECT_ID)
            .header("X-Appwrite-Key", AppwriteConfig.API_KEY)
            .contentType(ContentType.JSON)
            .body("{\"documentId\":\"unique()\",\"data\":{\"name\":\"Test\"}}")
        .when()
            .post(DOCS_PATH)
        .then()
            .statusCode(201)
            .body("name", equalTo("Test"))
            .body("$id", notNullValue());
    }

    @Test(priority = 2)
    @Story("Read Documents")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that listing documents returns 200 with results")
    public void testListDocuments_Returns200() {
        given()
            .header("X-Appwrite-Project", AppwriteConfig.PROJECT_ID)
            .header("X-Appwrite-Key", AppwriteConfig.API_KEY)
        .when()
            .get(DOCS_PATH)
        .then()
            .statusCode(200)
            .body("documents", not(empty()))
            .body("total", greaterThan(0));
    }

    @Test(priority = 3)
    @Story("Security")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify that requests without API key are rejected with 401")
    public void testListDocuments_NoKey_Returns401() {
        given()
            .header("X-Appwrite-Project", AppwriteConfig.PROJECT_ID)
        .when()
            .get(DOCS_PATH)
        .then()
            .statusCode(401);
    }

    @Test(priority = 4)
    @Story("Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that missing required fields return 400")
    public void testCreateDocument_MissingRequiredField_Returns400() {
        given()
            .header("X-Appwrite-Project", AppwriteConfig.PROJECT_ID)
            .header("X-Appwrite-Key", AppwriteConfig.API_KEY)
            .contentType(ContentType.JSON)
            .body("{\"documentId\":\"unique()\",\"data\":{}}")
        .when()
            .post(DOCS_PATH)
        .then()
            .statusCode(400);
    }

    @Test(priority = 5)
    @Story("Read Documents")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that requesting a non-existent document returns 404")
    public void testGetDocument_InvalidId_Returns404() {
        given()
            .header("X-Appwrite-Project", AppwriteConfig.PROJECT_ID)
            .header("X-Appwrite-Key", AppwriteConfig.API_KEY)
        .when()
            .get(DOCS_PATH + "/nonexistent-id-xyz")
        .then()
            .statusCode(404);
    }
}
