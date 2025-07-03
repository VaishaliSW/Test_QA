// File: ConfigReader.java
package utils;

import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConfigReader {
    private JSONObject configData;

    public ConfigReader(String filePath) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            configData = new JSONObject(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getConfigData(String key) {
        return configData.getString(key);
    }
}