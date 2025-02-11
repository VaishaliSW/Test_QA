package com.care4today.tests;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class Care4TodayTests {

    private AppiumDriver<MobileElement> driver;
    private String platform = System.getProperty("platform", "Android");
    private String deviceName = System.getProperty("deviceName", "emulator-5554");
    private String appPath = System.getProperty("app", "path/to/your/app.apk");

    @BeforeSuite
    public void setupSuite() {
        System.out.println("Setting up test suite");
    }

    @AfterSuite
    public void teardownSuite() {
        System.out.println("Tearing down test suite");
    }

    @BeforeMethod
    public void setupTest() throws MalformedURLException {
        System.out.println("Setting up test case");

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);

        if (platform.equalsIgnoreCase("Android")) {
            caps.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
            caps.setCapability(MobileCapabilityType.APP, appPath);
            driver = new AndroidDriver<>(new URL("http://localhost:4723/wd/hub"), caps);
        } else if (platform.equalsIgnoreCase("iOS")) {
            caps.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
            caps.setCapability(MobileCapabilityType.APP, appPath);
            driver = new IOSDriver<>(new URL("http://localhost:4723/wd/hub"), caps);
        }

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @AfterMethod
    public void teardownTest() {
        System.out.println("Tearing down test case");
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testLoginFunctionality() {
        System.out.println("Executing testLoginFunctionality");
        MobileElement usernameField = driver.findElement(By.id("com.example:id/username"));
        MobileElement passwordField = driver.findElement(By.id("com.example:id/password"));
        MobileElement loginButton = driver.findElement(By.id("com.example:id/login_button"));

        usernameField.sendKeys("testuser");
        passwordField.sendKeys("password123");
        loginButton.click();

        MobileElement homeView = driver.findElement(By.id("com.example:id/home_view"));
        Assert.assertTrue(homeView.isDisplayed(), "Login failed, home view not visible");
    }

    @Test
    public void testLogoutFunctionality() {
        System.out.println("Executing testLogoutFunctionality");
        MobileElement profileButton = driver.findElement(By.id("com.example:id/profile_button"));
        MobileElement logoutButton = driver.findElement(By.id("com.example:id/logout_button"));

        profileButton.click();
        logoutButton.click();

        MobileElement loginView = driver.findElement(By.id("com.example:id/login_view"));
        Assert.assertTrue(loginView.isDisplayed(), "Logout failed, login view not visible");
    }
}