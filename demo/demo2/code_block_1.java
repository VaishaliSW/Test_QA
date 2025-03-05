package com.example.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.Properties;

public class RealTimeTrackingTest {
    private WebDriver driver;
    private Properties prop;
    
    @BeforeMethod
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @DataProvider(name = "trackingData")
    public Object[][] trackingData() {
        return new Object[][]{
            {"/tracking", "TRK1234567890", "In Transit, New York"},
            {"/tracking", "TRK0987654321", "Out for Delivery, LA", "2023-10-15"},
            {"/tracking", "TRK1122334455", "In Transit, Miami", "5 items"},
            {"/tracking", "TRK9988776655", "In Customs, Tokyo", "Cleared"},
            {"/tracking", "TRK6677889900", "In Transit, Chicago", "Handle with Care"}
        };
    }

    @Test(dataProvider = "trackingData")
    public void testRealTimeTracking(String url, String trackingNumber, String expectedStatus, String... additionalDetails) {
        driver.get("https://www.example.com" + url);
        
        WebElement trackingNumberField = driver.findElement(By.id("trackingNumber"));
        WebElement trackButton = driver.findElement(By.id("trackButton"));
        
        trackingNumberField.sendKeys(trackingNumber);
        trackButton.click();
        
        WebElement statusElement = driver.findElement(By.id("status"));
        String actualStatus = statusElement.getText();
        
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(actualStatus, expectedStatus, "Status mismatch");
        
        if (additionalDetails.length > 0) {
            WebElement additionalDetailElement = driver.findElement(By.id("additionalDetail"));
            String actualDetail = additionalDetailElement.getText();
            softAssert.assertEquals(actualDetail, additionalDetails[0], "Additional detail mismatch");
        }

        softAssert.assertAll();
    }
}