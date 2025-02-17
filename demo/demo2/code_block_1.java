package com.saucedemo.tests;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class SauceDemoTest {
    private AppiumDriver<MobileElement> driver;

    @BeforeClass
    public void setUp() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android"); // change to "iOS" for iOS
        caps.setCapability(MobileCapabilityType.PLATFORM_VERSION, "10.0"); // Change version as needed
        caps.setCapability(MobileCapabilityType.DEVICE_NAME, "emulator-5554"); // change to appropriate device name
        caps.setCapability(MobileCapabilityType.APP, "/path/to/your/app.apk"); // Change path to your app
        caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2"); // change to "XCUITest" for iOS

        driver = new AndroidDriver<>(new URL("http://localhost:4723/wd/hub"), caps);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        return new Object[][] {
                { "standard_user", "secret_sauce" }
        };
    }

    @Test(priority = 1)
    public void launchApp() {
        // Test case ID : TC_001
        // Test case description : Validate the Login functionality of the saucedemo ecommerce site
        // Test Step : Launch the saucedemo app
        // Input test data : app url
        // Expected Result : User should be able to see the saucedemo home screen

        // Verify home screen
        MobileElement homeScreen = driver.findElement(By.xpath("//android.widget.TextView[@text='Swag Labs']"));
        Assert.assertTrue(homeScreen.isDisplayed(), "SauceDemo home screen is not displayed.");
    }

    @Test(priority = 2, dependsOnMethods = "launchApp")
    public void verifyLoginFields() {
        // Test case ID : nan
        // Test case description : nan
        // Test Step : Verify the username and password fields available
        // Input test data : nan
        // Expected Result : Username and password fields should be active

        // Verify username and password fields
        MobileElement usernameField = driver.findElement(By.xpath("//android.widget.EditText[@content-desc='test-Username']"));
        MobileElement passwordField = driver.findElement(By.xpath("//android.widget.EditText[@content-desc='test-Password']"));

        Assert.assertTrue(usernameField.isDisplayed(), "Username field is not displayed.");
        Assert.assertTrue(passwordField.isDisplayed(), "Password field is not displayed.");
    }

    @Test(priority = 3, dependsOnMethods = "verifyLoginFields", dataProvider = "loginData")
    public void enterUsername(String username, String password) {
        // Test case ID : nan
        // Test case description : nan
        // Test Step : Enter the username as given in the test data column
        // Input test data : standard_user
        // Expected Result : user should be able to enter the user name

        // Enter username
        MobileElement usernameField = driver.findElement(By.xpath("//android.widget.EditText[@content-desc='test-Username']"));
        usernameField.sendKeys(username);
    }

    @Test(priority = 4, dependsOnMethods = "enterUsername", dataProvider = "loginData")
    public void enterPassword(String username, String password) {
        // Test case ID : nan
        // Test case description : nan
        // Test Step : Enter the password as given in the test data column
        // Input test data : secret_sauce
        // Expected Result : user should be able to enter the password

        // Enter password
        MobileElement passwordField = driver.findElement(By.xpath("//android.widget.EditText[@content-desc='test-Password']"));
        passwordField.sendKeys(password);
    }

    @Test(priority = 5, dependsOnMethods = "enterPassword", dataProvider = "loginData")
    public void login(String username, String password) {
        // Test case ID : nan
        // Test case description : nan
        // Test Step : tap on Login and verify the user has successfully logged in
        // Input test data : nan
        // Expected Result : User should be successfully logged in as standard user

        // Tap on login button
        MobileElement loginButton = driver.findElement(By.xpath("//android.view.ViewGroup[@content-desc='test-LOGIN']"));
        loginButton.click();

        // Verify successful login
        MobileElement productsPage = driver.findElement(By.xpath("//android.widget.TextView[@text='PRODUCTS']"));
        Assert.assertTrue(productsPage.isDisplayed(), "User is not successfully logged in.");
    }

    @Test(priority = 6, dependsOnMethods = "login", dataProvider = "loginData")
    public void validateShoppingFunctionality(String username, String password) {
        // Test case ID : TC_002
        // Test case description : Validate the shopping functionality
        // Test Step : Access the saucedemo site and login using the standard user.
        // Input test data : Username: standard_user Password: secret_sauce
        // Expected Result : User should be successfully logged in as standard user

        // Select 1 item to add to cart
        MobileElement addItem = driver.findElement(By.xpath("//android.view.ViewGroup[@content-desc='test-ADD TO CART']"));
        addItem.click();

        // Verify item added to cart
        MobileElement cartBadge = driver.findElement(By.xpath("//android.view.ViewGroup[@content-desc='test-Cart']"));
        Assert.assertTrue(cartBadge.isDisplayed(), "Item not added to cart.");

        // Tap on cart icon
        cartBadge.click();

        // Verify chosen items in cart
        MobileElement cartItem = driver.findElement(By.xpath("//android.widget.TextView[@text='Sauce Labs Backpack']"));
        Assert.assertTrue(cartItem.isDisplayed(), "Chosen item is not shown in the cart.");

        // Tap on Checkout
        MobileElement checkoutButton = driver.findElement(By.xpath("//android.view.ViewGroup[@content-desc='test-CHECKOUT']"));
        checkoutButton.click();

        // Enter first name, last name, and zip code
        MobileElement firstNameField = driver.findElement(By.xpath("//android.widget.EditText[@content-desc='test-First Name']"));
        MobileElement lastNameField = driver.findElement(By.xpath("//android.widget.EditText[@content-desc='test-Last Name']"));
        MobileElement zipCodeField = driver.findElement(By.xpath("//android.widget.EditText[@content-desc='test-Zip/Postal Code']"));

        firstNameField.sendKeys("John");
        lastNameField.sendKeys("Doe");
        zipCodeField.sendKeys("12345");

        // Tap on Continue
        MobileElement continueButton = driver.findElement(By.xpath("//android.view.ViewGroup[@content-desc='test-CONTINUE']"));
        continueButton.click();

        // Verify total cost
        MobileElement totalCost = driver.findElement(By.xpath("//android.widget.TextView[@content-desc='test-Total: $32.39']"));
        Assert.assertTrue(totalCost.isDisplayed(), "Total cost is not as expected.");

        // Tap on Finish
        MobileElement finishButton = driver.findElement(By.xpath("//android.view.ViewGroup[@content-desc='test-FINISH']"));
        finishButton.click();

        // Validate "Thank you for your order!" message
        MobileElement thankYouMessage = driver.findElement(By.xpath("//android.widget.TextView[@content-desc='test-THANK YOU FOR YOUR ORDER']"));
        Assert.assertTrue(thankYouMessage.isDisplayed(), "Thank you message not displayed.");

        // Capture screenshot on success
        // Take screenshot code here (dependent on your setup)
    }
}