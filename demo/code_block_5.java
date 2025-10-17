package utils;

import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConfigReader {
    public static String getConfigData(String key) {
        try {
            String content = new String(Files.readAllBytes(Paths.get("config/config.json")));
            JSONObject jsonObject = new JSONObject(content);
            return jsonObject.getString(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}