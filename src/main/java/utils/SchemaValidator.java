package utils;

import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SchemaValidator {
    private static final Logger logger = LogManager.getLogger(SchemaValidator.class);

    /**
     * Validates the response against the specified JSON schema file.
     *
     * @param response   The API response to be validated.
     * @param schemaPath The path to the JSON schema file located in the resources folder.
     */
    public static void validate(Response response, String schemaPath) {
        if (SchemaValidator.class.getClassLoader().getResource(schemaPath) == null) {
            logger.error("Schema file not found: " + schemaPath);
            throw new IllegalArgumentException("Schema file not found: " + schemaPath);
        }
        // ...
    }

}

