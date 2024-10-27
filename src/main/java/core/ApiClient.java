package core;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.ConfigManager;

public class ApiClient {
    private static final Logger logger = LogManager.getLogger(ApiClient.class);

//    String apiUrl = ConfigManager.getProperty("baseUrl") + "/posts";
    static String baseUrl = ConfigManager.getProperty("baseUrl");

    public static RequestSpecification getRequestSpecification() {
        logger.info("Initializing request specification with base URI");
        return RestAssured
                .given()
                .baseUri(baseUrl)
                .contentType("application/json");
    }
}
