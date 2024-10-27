package tests;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.ConfigManager;

import static io.restassured.RestAssured.given;

public class CommentsTest {

    // Base URL for the API
    static String baseUrl = ConfigManager.getProperty("baseUrl");

    // Test for getting comments by post ID
    @Test(priority = 1)
    public void testGetCommentsByPostId_Positive() {
        int postId = 1; // Valid post ID
        Response response = given()
                .baseUri(baseUrl)
                .when()
                .get("/posts/" + postId + "/comments")
                .then()
                .extract().response();

        // Validate status code and response body
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200");
        Assert.assertTrue(response.jsonPath().getList("$").size() > 0, "Comments list should not be empty");
    }

    // Test for getting comments by post ID (negative case)
    @Test(priority = 2)
    public void testGetCommentsByPostId_Negative() {
        int postId = 999; // Invalid post ID
        Response response = given()
                .baseUri(baseUrl)
                .when()
                .get("/posts/" + postId + "/comments")
                .then()
                .extract().response();

        // Validate status code
        Assert.assertEquals(response.getStatusCode(), 404, "Expected status code 404 for invalid post ID");
    }

    // Test for getting comments with query parameter (postId)
    @Test(priority = 3)
    public void testGetCommentsByPostIdQuery_Positive() {
        Response response = given()
                .baseUri(baseUrl)
                .queryParam("postId", 1) // Valid post ID
                .when()
                .get("/comments")
                .then()
                .extract().response();

        // Validate status code and response body
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200");
        Assert.assertTrue(response.jsonPath().getList("$").size() > 0, "Comments list should not be empty");
    }

    // Test for getting comments with an invalid postId query parameter
    @Test(priority = 4)
    public void testGetCommentsByInvalidPostIdQuery_Negative() {
        Response response = given()
                .baseUri(baseUrl)
                .queryParam("postId", 999) // Invalid post ID
                .when()
                .get("/comments")
                .then()
                .extract().response();

        // Validate status code
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200 even for invalid postId");
        Assert.assertTrue(response.jsonPath().getList("$").isEmpty(), "Comments list should be empty for invalid postId");
    }
}
