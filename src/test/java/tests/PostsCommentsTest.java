package tests;

import com.aventstack.extentreports.ExtentTest;
import com.fasterxml.jackson.databind.JsonNode;
import core.ApiClient;
import core.Endpoints;
import io.restassured.response.Response;
import listeners.CustomTestListener;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import utils.ExtentReportManager;
import utils.JsonDataProvider;
import utils.SchemaValidator;

import static io.restassured.RestAssured.given;

@Listeners(CustomTestListener.class)
public class PostsCommentsTest {

    @Test(dataProvider = "dynamicDataProvider", dataProviderClass = JsonDataProvider.class,
            description = "Validate GET /posts/{id}/comments returns correct status codes and data")
    public void getCommentsByPostIdTest(JsonNode testData) {
        int postId = testData.get("postId").asInt();
        ExtentTest test = ExtentReportManager.createTest("getPostByIdTest for Post ID: " + postId);

        test.info("Requesting comments for Post ID: " + postId);

        // Send GET request
        Response response = given().spec(ApiClient.getRequestSpecification())
                .pathParam("id", postId)
                .when()
                .get(Endpoints.POSTS + "/{id}/comments")
                .then()
                .extract().response();

        String responseBody = response.getBody().asString();
        test.info("Response Body: " + responseBody);

        // Validate response status
        test.info("Validating status code");
        Assert.assertEquals(response.statusCode(), 200, "Expected 200 status code");

        // Schema validation for valid Post IDs
        test.info("Validating response schema for GET /posts/{id}/comments");
        SchemaValidator.validate(response, "schemas/comment-schema.json");
        test.pass("Schema validation for comments passed");

        // Validate response contains an array of comments
        test.info("Validating response contains an array of comments");
        Assert.assertFalse(response.jsonPath().getList("$").isEmpty(), "Expected comments for valid Post ID");
        test.pass("Array of comments validation passed");

        test.pass("Test for GET /posts/{id}/comments with Post ID " + postId + " completed successfully");
    }

    @Test(dataProvider = "dynamicDataProvider", dataProviderClass = JsonDataProvider.class,
            description = "Validate GET /comments?postId=1 returns correct status codes and data")
    public void getCommentsByQueryParamPostIdTest(JsonNode testData) {
        int postId = testData.get("postId").asInt();
        ExtentTest test = ExtentReportManager.createTest("getCommentsByPostIdTest for Post ID: " + postId);

        test.info("Requesting comments for Post ID using query parameter: " + postId);

        // Send GET request with query parameter
        Response response = given().spec(ApiClient.getRequestSpecification())
                .queryParam("postId", postId)
                .when()
                .get(Endpoints.COMMENTS)
                .then()
                .extract().response();

        String responseBody = response.getBody().asString();
        test.info("Response Body: " + responseBody);

        // Validate response status
        test.info("Validating status code");
        Assert.assertEquals(response.statusCode(), 200, "Expected 200 status code");

        // Schema validation for comments response
        test.info("Validating response schema for GET /comments?postId=1");
        SchemaValidator.validate(response, "schemas/comment-schema.json");
        test.pass("Schema validation for comments passed");

        // Validate response contains an array of comments for the given postId
        test.info("Validating response contains an array of comments for the given Post ID");
        Assert.assertFalse(response.jsonPath().getList("$").isEmpty(), "Expected comments for valid Post ID");
        test.pass("Array of comments validation passed");

        test.pass("Test for GET /comments?postId=" + postId + " completed successfully");
    }
}
