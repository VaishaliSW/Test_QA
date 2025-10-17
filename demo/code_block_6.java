package utils;

import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestDataReader {
    public static JSONObject getTestData(String key) {
        try {
            String content = new String(Files.readAllBytes(Paths.get("config/testdata.json")));
            JSONObject jsonObject = new JSONObject(content);
            return jsonObject.getJSONObject(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}