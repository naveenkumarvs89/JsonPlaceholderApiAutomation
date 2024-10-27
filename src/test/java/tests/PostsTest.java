package tests;

import com.aventstack.extentreports.ExtentTest;
import com.fasterxml.jackson.databind.JsonNode;
import core.ApiClient;
import core.Endpoints;
import io.restassured.response.Response;
import listeners.CustomTestListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import utils.ExtentReportManager;
import utils.JsonDataProvider;
import utils.SchemaValidator;

import static io.restassured.RestAssured.given;

@Listeners(CustomTestListener.class) // Register the listener
public class PostsTest {
    private static final Logger logger = LogManager.getLogger(PostsTest.class);

    @Test(description = "Validate GET /posts returns 200 status code and matches schema")
    public void getAllPostsTest() {
        ExtentTest test = ExtentReportManager.createTest("getAllPostsTest");
        String payload = "{}"; // Set payload if applicable
        test.info("Payload: " + payload);

        Response response = given().spec(ApiClient.getRequestSpecification())
                .when()
                .get(Endpoints.POSTS)
                .then()
                .extract().response();

        String responseBody = response.getBody().asString();
        test.info("Response Body: " + responseBody);

        test.info("Validating status code");
        Assert.assertEquals(response.statusCode(), 200);
        test.pass("Status code validation passed with "+response.statusCode());

        // Schema validation using utility
        test.info("Validating response schema");
        SchemaValidator.validate(response, "schemas/post-schema.json");
        test.pass("Schema validation passed!");
    }

    @Test(dataProvider = "dynamicDataProvider", dataProviderClass = JsonDataProvider.class,
            description = "Validate GET /posts/{id} returns 200 status code and conforms to schema")
    public void getPostByIdTest(JsonNode testData) {
        int postId = testData.get("postId").asInt();
        ExtentTest test = ExtentReportManager.createTest("getPostByIdTest for Post ID: " + postId);

        test.info("Requesting Post ID: " + postId);

        // Send GET request
        Response response = given().spec(ApiClient.getRequestSpecification())
                .pathParam("id", postId)
                .when()
                .get(Endpoints.POSTS + "/{id}")
                .then()
                .extract().response();

        // Log the response
        String responseBody = response.getBody().asString();
        test.info("Response Body: " + responseBody);

        // Validate status code
        test.info("Validating status code");
        Assert.assertEquals(response.statusCode(), 200, "Status code is not 200");
        test.pass("Status code validation passed");

        // Validate the response schema
        test.info("Validating response schema for GET /posts/{id}");
        SchemaValidator.validate(response, "schemas/post-schema.json");
        test.pass("Schema validation passed for GET /posts/{id}");

        test.pass("Test for GET /posts/{id} with Post ID " + postId + " completed successfully");
    }



//    @Test(description = "Validate POST /posts with valid payload")
//    public void createPostTest() {
//        Post post = new PostPayloadBuilder().withTitle("foo").withBody("bar").withUserId(1).build();
//        logger.info("Executing createPostTest");
//
//        Response response = given().spec(ApiClient.getRequestSpecification())
//                .body(post)
//                .when()
//                .post(Endpoints.POSTS)
//                .then()
//                .extract().response();
//
//        logger.info("Validating status code");
//        Assert.assertEquals(response.statusCode(), 201);
//        logger.info("Validating created post");
//        Assert.assertEquals(response.jsonPath().getString("title"), "foo");
//    }

    // Additional methods for other endpoints (PUT, PATCH, DELETE) and negative scenarios
}
