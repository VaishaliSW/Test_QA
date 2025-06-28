package utils;

import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ConfigReader {
    private static JSONObject configData;

    static {
        try {
            JSONParser parser = new JSONParser();
            configData = (JSONObject) parser.parse(new FileReader("config/config.json"));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static String getConfigData(String key) {
        return (String) configData.get(key);
    }
}