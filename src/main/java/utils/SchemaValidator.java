package utils;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SchemaValidator {
    private static final Logger logger = LogManager.getLogger(SchemaValidator.class);

    public static void validate(Response response, String schemaPath) {
        logger.info("Validating response against schema: " + schemaPath);
        response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath));
    }
}
