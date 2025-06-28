// File: TestDataReader.java
package utils;

import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class TestDataReader {
    private static JSONObject testData;

    static {
        try {
            String content = new String(Files.readAllBytes(Paths.get("config/testdata.json")));
            testData = new JSONObject(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getTestData(String key) {
        return testData.getJSONObject(key).toString();
    }
}