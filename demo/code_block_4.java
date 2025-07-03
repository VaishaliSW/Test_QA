// File: DriverManager.java
package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.edge.EdgeDriver;
import java.util.concurrent.TimeUnit;
import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DriverManager {
    private WebDriver driver;
    private String browser;
    private String driverPath;

    public DriverManager() {
        loadConfig();
        initializeDriver();
    }

    private void loadConfig() {
        try {
            String configData = new String(Files.readAllBytes(Paths.get("config/config.json")));
            JSONObject config = new JSONObject(configData);
            browser = config.getString("browser");
            driverPath = config.getJSONObject("driverPath").getString(browser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeDriver() {
        switch (browser.toLowerCase()) {
            case "chrome":
                System.setProperty("webdriver.chrome.driver", driverPath);
                driver = new ChromeDriver();
                break;
            case "firefox":
                System.setProperty("webdriver.gecko.driver", driverPath);
                driver = new FirefoxDriver();
                break;
            case "edge":
                System.setProperty("webdriver.edge.driver", driverPath);
                driver = new EdgeDriver();
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void quitDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}