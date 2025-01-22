import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

import java.io.FileReader;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class SaucedemoTest {

    private AppiumDriver<MobileElement> driver;

    @BeforeClass
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "emulator-5554");
        capabilities.setCapability(MobileCapabilityType.APP, "path/to/saucedemo.apk");
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
        driver = new AndroidDriver<>(new URL("http://localhost:4723/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @BeforeMethod
    public void beforeMethod() {
        // Code to reset app to initial state if needed
    }

    @AfterMethod
    public void afterMethod() {
        // Code to capture screenshot on failure
    }

    @DataProvider(name = "loginData")
    public Object[][] getData() throws Exception {
        JSONParser parser = new JSONParser();
        JSONArray dataArray = (JSONArray) parser.parse(new FileReader("path/to/data.json"));
        Object[][] data = new Object[dataArray.size()][2];
        for (int i = 0; i < dataArray.size(); i++) {
            JSONObject jsonObject = (JSONObject) dataArray.get(i);
            data[i][0] = jsonObject.get("username");
            data[i][1] = jsonObject.get("password");
        }
        return data;
    }

    @Test(dataProvider = "loginData")
    public void testLoginFunctionality(String username, String password) {
        driver.get("https://www.saucedemo.com");

        // Validate the home screen
        WebElement homeScreen = driver.findElement(By.xpath("//div[@class='login_logo']"));
        Assert.assertTrue(homeScreen.isDisplayed(), "Home screen is not displayed");

        // Verify username and password fields are available
        WebElement usernameField = driver.findElement(By.xpath("//input[@id='user-name']"));
        WebElement passwordField = driver.findElement(By.xpath("//input[@id='password']"));
        Assert.assertTrue(usernameField.isDisplayed(), "Username field is not displayed");
        Assert.assertTrue(passwordField.isDisplayed(), "Password field is not displayed");

        // Enter username and password
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);

        // Tap on login button
        WebElement loginButton = driver.findElement(By.xpath("//input[@id='login-button']"));
        loginButton.click();

        // Verify login success
        WebElement productsPage = driver.findElement(By.xpath("//span[@class='title']"));
        Assert.assertTrue(productsPage.isDisplayed(), "Login failed");
    }

    @Test
    public void testShoppingFunctionality() {
        testLoginFunctionality("standard_user", "secret_sauce");

        // Select an item to the cart
        WebElement addItemButton = driver.findElement(By.xpath("//button[@class='btn btn_primary btn_small btn_inventory']"));
        addItemButton.click();

        // Verify item added to the cart
        WebElement cartBadge = driver.findElement(By.xpath("//span[@class='shopping_cart_badge']"));
        Assert.assertTrue(cartBadge.isDisplayed(), "Item not added to cart");

        // Proceed to cart
        WebElement cartIcon = driver.findElement(By.xpath("//a[@class='shopping_cart_link']"));
        cartIcon.click();

        // Verify chosen items in cart
        WebElement cartItem = driver.findElement(By.xpath("//div[@class='inventory_item_name']"));
        Assert.assertTrue(cartItem.isDisplayed(), "Chosen item not shown in cart");

        // Tap on Checkout
        WebElement checkoutButton = driver.findElement(By.xpath("//button[@id='checkout']"));
        checkoutButton.click();

        // Enter first name, last name, and zip code
        WebElement firstNameField = driver.findElement(By.xpath("//input[@id='first-name']"));
        WebElement lastNameField = driver.findElement(By.xpath("//input[@id='last-name']"));
        WebElement zipCodeField = driver.findElement(By.xpath("//input[@id='postal-code']"));
        firstNameField.sendKeys("John");
        lastNameField.sendKeys("Doe");
        zipCodeField.sendKeys("12345");

        // Tap on Continue
        WebElement continueButton = driver.findElement(By.xpath("//input[@id='continue']"));
        continueButton.click();

        // Verify total cost and print status
        WebElement totalCost = driver.findElement(By.xpath("//div[@class='summary_total_label']"));
        Assert.assertTrue(totalCost.isDisplayed(), "Total cost is not displayed");
        System.out.println("Total cost: " + totalCost.getText());

        // Tap on Finish
        WebElement finishButton = driver.findElement(By.xpath("//button[@id='finish']"));
        finishButton.click();

        // Verify thank you message
        WebElement thankYouMessage = driver.findElement(By.xpath("//h2[@class='complete-header']"));
        Assert.assertTrue(thankYouMessage.isDisplayed(), "Thank you message not displayed");
        Assert.assertEquals(thankYouMessage.getText(), "THANK YOU FOR YOUR ORDER");
    }
}