package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonDataProvider {
    private static final Logger logger = LogManager.getLogger(JsonDataProvider.class);

    // Method to read data from JSON file and return as Object[][]
    public static Object[][] getTestData(String filePath) {
        List<Object[]> testData = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(new File(filePath));

            for (JsonNode node : rootNode) {
                // Convert each JSON object to a map and add it as an array of Objects
                testData.add(new Object[]{node});
            }
        } catch (IOException e) {
                logger.error("Error reading JSON file", e);
        }
        return testData.toArray(new Object[0][]);
    }

    @DataProvider(name = "dynamicDataProvider")
    public static Object[][] dynamicDataProvider() {
        String filePath = "src/test/resources/testdata/postData.json"; // Path to JSON file
        return getTestData(filePath);
    }
}