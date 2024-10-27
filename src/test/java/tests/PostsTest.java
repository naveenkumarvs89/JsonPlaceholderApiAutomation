package tests;

import com.aventstack.extentreports.ExtentTest;
import com.fasterxml.jackson.databind.JsonNode;
import core.ApiClient;
import core.Endpoints;
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

    /**
     * Positive Test: Validate successful POST /posts with a valid Post object and schema validation.
     */
    @Test(description = "Validate successful POST /posts with valid Post object and schema validation")
    public void createPostPositiveTest() {
        ExtentTest test = ExtentReportManager.createTest("createPostPositiveTest");

        // Create a Post object with the required data
        Post post = new Post();
        post.setTitle("foo");
        post.setBody("bar");
        post.setUserId(1);

        // Log the request payload
        test.info("Request Payload: " + post);

        // Send POST request and capture the response
        Response response = given().contentType("application/json")
                .body(post)
                .when()
                .post(Endpoints.POSTS)
                .then()
                .extract().response();

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
    }

    /**
     * Negative Test: Validate POST /posts with invalid/missing fields returns a 400 status code or error response.
     */
/*    @Test(description = "Validate POST /posts with invalid/missing fields returns an error")
    public void createPostNegativeTest() {
        ExtentTest test = ExtentReportManager.createTest("createPostNegativeTest");

        // Create a Post object with missing required fields (e.g., no title)
        Post invalidPost = new Post();
        invalidPost.setBody("This body has no title");
        invalidPost.setUserId(1); // Missing "title" to simulate a negative test case

        // Log the invalid request payload
        test.info("Invalid Request Payload: " + invalidPost);

        // Send POST request with invalid data and capture the response
        Response response = given().contentType("application/json")
                .body(invalidPost)
                .when()
                .post(Endpoints.POSTS)
                .then()
                .extract().response();

        String responseBody = response.getBody().asString();
        test.info("Response Body: " + responseBody);

        // Validate response status code is 400 or similar (depends on API behavior)
        test.info("Validating status code for invalid request");
        Assert.assertTrue(
                response.statusCode() == 400 || response.statusCode() == 422,
                "Expected 400 or 422 status code for invalid request"
        );

        // Validate response contains error message (if applicable)
        if (response.statusCode() == 400 || response.statusCode() == 422) {
            test.info("Validating presence of error message in response");
            Assert.assertTrue(
                    responseBody.contains("error") || responseBody.contains("Invalid"),
                    "Expected error message in response for invalid input"
            );
            test.pass("Error message validation passed");
        } else {
            test.fail("Unexpected status code received: " + response.statusCode());
        }

        test.pass("Negative test for POST /posts with invalid input completed");
    }*/

    @Test(dataProvider = "dynamicDataProvider", dataProviderClass = JsonDataProvider.class, description = "Validate PUT /posts/{id} returns 200 status code and correct response structure")
    public void updatePostTest(JsonNode testData) {
        int putId = testData.get("putId").asInt();
        ExtentTest test = ExtentReportManager.createTest("updatePostTest");

        // Create a Post object with updated data
        Post updatedPost = new Post();
        updatedPost.setId(1);
        updatedPost.setTitle("foo");
        updatedPost.setBody("bar");
        updatedPost.setUserId(1);

        // Log the updated request payload
        test.info("Updated Request Payload: " + updatedPost + putId);

        // Send PUT request to update the post and capture the response
        Response response = given().contentType("application/json")
                .pathParam("id", putId)
                .body(updatedPost)
                .when()
                .put(Endpoints.POSTS+ "/{id}")
                .then()
                .extract().response();

        String responseBody = response.getBody().asString();
        test.info("Response Body: " + responseBody);

        // Validate response status code
        test.info("Validating status code for update request");
        Assert.assertEquals(response.statusCode(), 200, "Expected 200 status code for successful update");

        // Schema validation for the response structure
        test.info("Validating response schema for PUT /posts/{id}");
        SchemaValidator.validate(response, "schemas/post-schema.json");
        test.pass("Schema validation for updated post response passed");

        // Additional assertions to check response data matches the updated data
        test.info("Validating response data matches the updated post data");
        Assert.assertEquals(response.jsonPath().getInt("id"), 1, "Expected post ID to be 1");
        Assert.assertEquals(response.jsonPath().getString("title"), "foo", "Expected title to be 'foo'");
        Assert.assertEquals(response.jsonPath().getString("body"), "bar", "Expected body to be 'bar'");
        Assert.assertEquals(response.jsonPath().getInt("userId"), 1, "Expected userId to be 1");

        test.pass("Test for PUT /posts/{id} with post ID 1 completed successfully");
    }

}
