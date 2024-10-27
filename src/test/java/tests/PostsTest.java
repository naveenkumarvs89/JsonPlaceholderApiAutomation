package tests;

import com.aventstack.extentreports.ExtentTest;
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

import static io.restassured.RestAssured.given;

@Listeners(CustomTestListener.class) // Register the listener
public class PostsTest {
    private static final Logger logger = LogManager.getLogger(PostsTest.class);

    @Test(description = "Validate GET /posts returns 200 status code")
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
        test.pass("Test completed successfully");
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
