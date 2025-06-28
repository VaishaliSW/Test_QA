// File: ConfigReader.java
package utils;

import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class ConfigReader {
    private static JSONObject configData;

    static {
        try {
            String content = new String(Files.readAllBytes(Paths.get("config/config.json")));
            configData = new JSONObject(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getConfigData(String key) {
        return configData.getString(key);
    }
}