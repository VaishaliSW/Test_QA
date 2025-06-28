// File: ConfigReader.java
package utils;

import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.InputStream;

public class ConfigReader {
    private JSONObject configData;

    public ConfigReader() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("config/config.json")) {
            if (is == null) {
                throw new RuntimeException("Config file not found");
            }
            JSONTokener tokener = new JSONTokener(is);
            configData = new JSONObject(tokener);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config", e);
        }
    }

    public String getConfigData(String key) {
        return configData.getString(key);
    }
}