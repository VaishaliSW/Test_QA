// File: TestDataReader.java
package utils;

import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.InputStream;

public class TestDataReader {
    private JSONObject testData;

    public TestDataReader() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("config/testdata.json")) {
            if (is == null) {
                throw new RuntimeException("Test data file not found");
            }
            JSONTokener tokener = new JSONTokener(is);
            testData = new JSONObject(tokener);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load test data", e);
        }
    }

    public Object getTestData(String key) {
        return testData.get(key);
    }
}