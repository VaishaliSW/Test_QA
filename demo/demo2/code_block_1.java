package com.example.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class VaccinationAppointmentTest {
    private WebDriver driver;
    private Properties prop;

    @BeforeMethod
    public void setUp() throws IOException {
        prop = new Properties();
        FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
        prop.load(fis);

        String browser = prop.getProperty("browser");
        if (browser.equalsIgnoreCase("chrome")) {
            ChromeOptions options = new ChromeOptions();
            if (Boolean.parseBoolean(prop.getProperty("chromeHeadless"))) {
                options.addArguments("--headless=new");
            }
            driver = new ChromeDriver(options);
        }
        // Implement for other browsers similarly

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @DataProvider(name = "appointmentData")
    public Object[][] appointmentData() {
        return new Object[][]{
            {"https://vaccinationportal.com/schedule", "JohnDoe", "Pass@123", "Alex Doe", "2023-11-15", "Appointment confirmed for vaccination"},
            {"https://vaccinationportal.com/schedule", "JohnDoe", "Pass@123", "Alex Doe", "2023-12-10", "Appointment confirmed"},
            {"https://vaccinationportal.com/schedule", "JohnDoe", "Pass@123", "Alex Doe", "2023-11-20", "2023-11-22", "Appointment confirmed"},
            {"https://vaccinationportal.com/schedule", "SarahSmith", "SafePass@456", "Emma Smith", "2023-12-25", "2023-12-26", "Appointment confirmed"}
        };
    }

    @Test(dataProvider = "appointmentData")
    public void testScheduleAppointment(String url, String username, String password, String childName, String date, String expectedMessage) {
        driver.get(url);

        WebElement usernameField = driver.findElement(By.id("username"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("loginButton"));

        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        loginButton.click();

        WebElement childList = driver.findElement(By.id("childList"));
        WebElement child = childList.findElement(By.xpath("//span[text()='" + childName + "']"));
        child.click();

        WebElement calendar = driver.findElement(By.id("calendar"));
        WebElement dateElement = calendar.findElement(By.xpath("//td[text()='" + date + "']"));
        dateElement.click();

        WebElement confirmButton = driver.findElement(By.id("confirmButton"));
        confirmButton.click();

        WebElement confirmationMessage = driver.findElement(By.id("confirmationMessage"));
        Assert.assertEquals(confirmationMessage.getText(), expectedMessage);
    }

    @Test(dataProvider = "appointmentData")
    public void testAlternativeAppointment(String url, String username, String password, String childName, String unavailableDate, String alternativeDate, String expectedMessage) {
        driver.get(url);

        WebElement usernameField = driver.findElement(By.id("username"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("loginButton"));

        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        loginButton.click();

        WebElement childList = driver.findElement(By.id("childList"));
        WebElement child = childList.findElement(By.xpath("//span[text()='" + childName + "']"));
        child.click();

        WebElement calendar = driver.findElement(By.id("calendar"));
        WebElement unavailableDateElement = calendar.findElement(By.xpath("//td[text()='" + unavailableDate + "']"));
        unavailableDateElement.click();

        WebElement alternativeDateElement = driver.findElement(By.xpath("//td[text()='" + alternativeDate + "']"));
        alternativeDateElement.click();

        WebElement confirmButton = driver.findElement(By.id("confirmButton"));
        confirmButton.click();

        WebElement confirmationMessage = driver.findElement(By.id("confirmationMessage"));
        Assert.assertEquals(confirmationMessage.getText(), expectedMessage);
    }
}