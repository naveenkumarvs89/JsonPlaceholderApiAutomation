package tests;

import com.aventstack.extentreports.ExtentTest;
import com.fasterxml.jackson.databind.JsonNode;
import core.ApiClient;
import core.Endpoints;
import domain.models.Patch;
import domain.models.Post;
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
public class ApiTest {
    private static final Logger logger = LogManager.getLogger(ApiTest.class);

    @Test(description = "Validate GET /posts returns 200 status code and matches schema")
    public void getAllPostsTest() {
        ExtentTest test = ExtentReportManager.createTest("getAllPostsTest");
        String payload = "{}"; // Set payload if applicable
        test.info("Payload: " + payload);

        Response response = given().spec(ApiClient.getRequestSpecification()).when().get(Endpoints.POSTS).then().extract().response();

        String responseBody = response.getBody().asString();
        test.info("Response Body: " + responseBody);

        test.info("Validating status code");
        Assert.assertEquals(response.statusCode(), 200);
        test.pass("Status code validation passed with " + response.statusCode());

        // Schema validation using utility
        test.info("Validating response schema");
        SchemaValidator.validate(response, "schemas/post-schema.json");
        test.pass("Schema validation passed!");
    }

    @Test(dataProvider = "dynamicPostDataProvider", dataProviderClass = JsonDataProvider.class, description = "Validate GET /posts/{id} returns 200 status code and conforms to schema")
    public void getPostByIdTest(JsonNode testData) {
        int postId = testData.get("postId").asInt();
        ExtentTest test = ExtentReportManager.createTest("getPostByIdTest");

        test.info("Requesting Post ID: " + postId);

        // Send GET request
        Response response = given().spec(ApiClient.getRequestSpecification()).pathParam("id", postId).when().get(Endpoints.POSTS + "/{id}").then().extract().response();

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

    /**
     * Positive Test: Validate successful POST /posts with a valid Post object and schema validation.
     */
    @Test(description = "Validate successful POST /posts with valid Post object and schema validation")
    public void createResourceTest() {
        ExtentTest test = ExtentReportManager.createTest("createResourceTest");

        // Create a Post object with the required data
        Post post = new Post();
        post.setTitle("foo");
        post.setBody("bar");
        post.setUserId(3);

        // Log the request payload
        test.info("Request Payload: " + post);

        try {
            // Send POST request and capture the response
            Response response = given().spec(ApiClient.getRequestSpecification()).body(post).when().post(Endpoints.POSTS).then().extract().response();

            String responseBody = response.getBody().asString();
            test.info("Response Body: " + responseBody);

            // Deserialize the response into a Post object
            Post responsePost = response.as(Post.class);

            // Validate response status code
            test.info("Validating status code");
            Assert.assertEquals(response.statusCode(), 201, "Expected 201 status code");

            // Validate fields in the response object
            test.info("Validating response fields");
            Assert.assertEquals(responsePost.getTitle(), post.getTitle(), "Title mismatch");
            Assert.assertEquals(responsePost.getBody(), post.getBody(), "Body mismatch");
            Assert.assertEquals(responsePost.getUserId(), post.getUserId(), "User ID mismatch");

            // Schema validation
            test.info("Validating response against schema");
            SchemaValidator.validate(response, "schemas/create-resource-schema.json"); // Assume post-schema.json exists at specified path
            test.pass("Schema validation for POST /posts passed successfully");
            test.pass("Positive test for POST /posts completed successfully");
        } catch (Exception e) {
            test.info("An error occurred: " + e.getMessage());
            test.fail("Test failed due to unexpected error");
        }
    }

    @Test(dataProvider = "dynamicPutDataProvider", dataProviderClass = JsonDataProvider.class, description = "Validate PUT /posts/{id} returns 200 status code and correct response structure")
    public void updateResourceTest(JsonNode testData) {
        int putId = testData.get("putId").asInt();
        ExtentTest test = ExtentReportManager.createTest("updateResourceTest");

        // Create a Post object with updated data
        Post updatedPost = new Post();
        updatedPost.setTitle("foo");
        updatedPost.setBody("bar");
        updatedPost.setUserId(2);
        updatedPost.setId(2);

        // Log the updated request payload
        test.info("Request Payload: " + updatedPost);

        // Send PUT request to update the post and capture the response
        try {
            // Send PUT request and capture the response
            Response response = given().spec(ApiClient.getRequestSpecification()).pathParam("id", putId).body(updatedPost).when().put(Endpoints.POSTS + "/{id}").then().extract().response();

            String responseBody = response.getBody().asString();
            test.info("Response Body: " + responseBody);

            // Validate response status code
            test.info("Validating status code for update request");
            Assert.assertEquals(response.statusCode(), 200, "Expected 200 status code for successful update");

            // Schema validation for the response structure
            test.info("Validating response schema for PUT /posts/{id}");
            SchemaValidator.validate(response, "schemas/put-schema.json");
            test.pass("Schema validation for updated post response passed");

            // Additional assertions to check response data matches the updated data
            test.info("Validating response data matches the updated post data");
            Assert.assertEquals(response.jsonPath().getInt("id"), 2, "Expected post ID to be 2");
            Assert.assertEquals(response.jsonPath().getString("title"), "foo", "Expected title to be 'foo'");
            Assert.assertEquals(response.jsonPath().getString("body"), "bar", "Expected body to be 'bar'");
            Assert.assertEquals(response.jsonPath().getInt("userId"), 2, "Expected userId to be 2");
            test.pass("Test for PUT /posts/{id} with post ID 2 completed successfully");
        } catch (Exception e) {
            test.info("An error occurred: " + e.getMessage());
            test.fail("Test failed due to unexpected error");
        }
    }

    @Test(dataProvider = "dynamicPatchDataProvider", dataProviderClass = JsonDataProvider.class, description = "Validate PATCH /posts/{id} returns 200 status code and correct response structure")
    public void patchResourceTest(JsonNode testData) {
        int patchId = testData.get("patchId").asInt();
        ExtentTest test = ExtentReportManager.createTest("patchResourceTest");

        // Create a Post object with updated patch data
        Patch updatedPatch = new Patch();
        updatedPatch.setId(1);
        updatedPatch.setUserId(1);

        // Log the updated request payload
        test.info("Request Payload: " + updatedPatch);

        // Send PATCH request to update the post and capture the response
        try {
            // Send PATCH request and capture the response
            Response response = given().spec(ApiClient.getRequestSpecification()).pathParam("id", patchId).body(updatedPatch).when().patch(Endpoints.POSTS + "/{id}").then().extract().response();

            String responseBody = response.getBody().asString();
            test.info("Response Body: " + responseBody);

            // Validate response status code
            test.info("Validating status code for patch request");
            Assert.assertEquals(response.statusCode(), 200, "Expected 200 status code for successful update");

            // Schema validation for the response structure
            test.info("Validating response schema for PATCH /posts/{id}");
            SchemaValidator.validate(response, "schemas/patch-schema.json");
            test.pass("Schema validation for updated post response passed");

            // Additional assertions to check response data matches the updated data
            test.info("Validating response data matches the updated post data");
            Assert.assertEquals(response.jsonPath().getInt("id"), 1, "Expected post ID to be 1");
            Assert.assertEquals(response.jsonPath().getInt("userId"), 1, "Expected userId to be 1");
            test.pass("Test for PATCH /posts/{id} with post ID 1 completed successfully");
        } catch (Exception e) {
            test.info("An error occurred: " + e.getMessage());
            test.fail("Test failed due to unexpected error");
        }
    }

    @Test(dataProvider = "dynamicDeleteDataProvider", dataProviderClass = JsonDataProvider.class, description = "Validate DELETE /posts/{id} returns 200 status code and empty response body")
    public void deletePostTest(JsonNode testData) {
        int deleteId = testData.get("deleteId").asInt();

        ExtentTest test = ExtentReportManager.createTest("deletePostTest");
        String payload = "{}"; // No payload is required for DELETE
        test.info("Payload: " + payload);

        Response response = given().spec(ApiClient.getRequestSpecification()).pathParam("id", deleteId).when().delete(Endpoints.POSTS + "/{id}").then().extract().response();

        String responseBody = response.getBody().asString();
        test.info("Response Body: " + responseBody);

        test.info("Validating status code");
        Assert.assertEquals(response.statusCode(), 200);
        test.pass("Status code validation passed with " + response.statusCode());

        // Validate empty response body
        test.info("Validating response body is empty");
        Assert.assertTrue(responseBody.contains("{}"));
        test.pass("Response body validation passed!");
    }

    @Test(dataProvider = "dynamicCommentDataProvider", dataProviderClass = JsonDataProvider.class, description = "Validate GET /comments?postId={id} returns 200 status code and conforms to schema")
    public void getCommentsByPostIdTest(JsonNode testData) {
        int postId = testData.get("postId").asInt();
        ExtentTest test = ExtentReportManager.createTest("getCommentsByPostIdTest");

        test.info("Requesting Comments for Post ID: " + postId);

        // Send GET request
        Response response = given().spec(ApiClient.getRequestSpecification()).queryParam("postId", postId).when().get(Endpoints.COMMENTS).then().extract().response();

        // Log the response
        String responseBody = response.getBody().asString();
        test.info("Response Body: " + responseBody);

        // Validate status code
        test.info("Validating status code");
        Assert.assertEquals(response.statusCode(), 200, "Status code is not 200");
        test.pass("Status code validation passed");

        // Validate the response schema
        test.info("Validating response schema for GET /comments?postId={id}");
        SchemaValidator.validate(response, "schemas/comment-schema.json");
        test.pass("Schema validation passed for GET /comments?postId={id}");

        test.pass("Test for GET /comments?postId={id} with Post ID " + postId + " completed successfully");
    }
}