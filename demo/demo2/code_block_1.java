import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class SauceDemoTest {
    private AppiumDriver<MobileElement> driver;

    @BeforeClass
    @Parameters({"platformName", "deviceName", "appUrl", "appPath"})
    public void setUp(String platformName, String deviceName, String appUrl, String appPath) throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", platformName);
        capabilities.setCapability("deviceName", deviceName);
        capabilities.setCapability("app", appPath);
        driver = platformName.equalsIgnoreCase("Android") ?
                new AndroidDriver<>(new URL(appUrl), capabilities) :
                new IOSDriver<>(new URL(appUrl), capabilities);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @Test(priority = 1)
    public void validateLoginFunctionality() {
        // Launch the saucedemo app
        MobileElement homeScreen = driver.findElement(By.xpath("//android.widget.TextView[@content-desc='SauceDemo home screen']"));
        Assert.assertTrue(homeScreen.isDisplayed(), "User should be able to see the saucedemo home screen");

        // Verify the username and password fields are available
        MobileElement usernameField = driver.findElement(By.xpath("//android.widget.EditText[@content-desc='test-Username']"));
        MobileElement passwordField = driver.findElement(By.xpath("//android.widget.EditText[@content-desc='test-Password']"));
        Assert.assertTrue(usernameField.isEnabled(), "Username field should be active");
        Assert.assertTrue(passwordField.isEnabled(), "Password field should be active");

        // Enter the username and password
        usernameField.sendKeys("standard_user");
        Assert.assertEquals(usernameField.getText(), "standard_user", "User should be able to enter the username");

        passwordField.sendKeys("secret_sauce");
        Assert.assertEquals(passwordField.getText(), "secret_sauce", "User should be able to enter the password");

        // Tap on Login and verify the user has successfully logged in
        MobileElement loginButton = driver.findElement(By.xpath("//android.widget.Button[@content-desc='test-LOGIN']"));
        loginButton.click();
        MobileElement loggedInScreen = driver.findElement(By.xpath("//android.widget.TextView[@content-desc='Logged in as standard user']"));
        Assert.assertTrue(loggedInScreen.isDisplayed(), "User should be successfully logged in as standard user");
    }

    @Test(priority = 2)
    public void validateShoppingFunctionality() {
        // Access the saucedemo site and login using the standard user
        validateLoginFunctionality();

        // Select any 1 item to the cart
        MobileElement item = driver.findElement(By.xpath("//android.widget.TextView[@content-desc='Item 1']"));
        item.click();
        MobileElement cartIcon = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc='Cart Icon']"));
        Assert.assertEquals(cartIcon.getAttribute("quantity"), "1", "The item should be added in the cart and the cart icon shows the qty");

        // Tap on the top right corner on the cart icon to proceed to the cart
        cartIcon.click();
        MobileElement cartPage = driver.findElement(By.xpath("//android.widget.TextView[@content-desc='Cart Page']"));
        Assert.assertTrue(cartPage.isDisplayed(), "User should be navigated to the cart page");

        // Verify if the chosen items are shown in the cart
        MobileElement cartItem = driver.findElement(By.xpath("//android.widget.TextView[@content-desc='Item 1 in cart']"));
        Assert.assertTrue(cartItem.isDisplayed(), "The items added should be shown in the cart page");

        // Tap on "Checkout"
        MobileElement checkoutButton = driver.findElement(By.xpath("//android.widget.Button[@content-desc='Checkout']"));
        checkoutButton.click();
        MobileElement checkoutPage = driver.findElement(By.xpath("//android.widget.TextView[@content-desc='Checkout Page']"));
        Assert.assertTrue(checkoutPage.isDisplayed(), "User should be navigated to the checkout page");

        // Enter the first name, last name and zip code
        MobileElement firstNameField = driver.findElement(By.xpath("//android.widget.EditText[@content-desc='test-First Name']"));
        MobileElement lastNameField = driver.findElement(By.xpath("//android.widget.EditText[@content-desc='test-Last Name']"));
        MobileElement zipCodeField = driver.findElement(By.xpath("//android.widget.EditText[@content-desc='test-Zip Code']"));

        firstNameField.sendKeys("John");
        lastNameField.sendKeys("Doe");
        zipCodeField.sendKeys("12345");

        Assert.assertEquals(firstNameField.getText(), "John", "User should be able to enter values in the given fields");
        Assert.assertEquals(lastNameField.getText(), "Doe", "User should be able to enter values in the given fields");
        Assert.assertEquals(zipCodeField.getText(), "12345", "User should be able to enter values in the given fields");

        // Tap on "Continue" and then verify the total cost if it is the expected and print the status
        MobileElement continueButton = driver.findElement(By.xpath("//android.widget.Button[@content-desc='Continue']"));
        continueButton.click();
        MobileElement totalCost = driver.findElement(By.xpath("//android.widget.TextView[@content-desc='Total Cost']"));
        Assert.assertEquals(totalCost.getText(), "$29.99", "User should be navigated to the next page");
        System.out.println("Total cost is as expected: " + totalCost.getText());

        // Tap on "Finish" and verify if the thank you message is shown and print the status
        MobileElement finishButton = driver.findElement(By.xpath("//android.widget.Button[@content-desc='Finish']"));
        finishButton.click();
        MobileElement thankYouMessage = driver.findElement(By.xpath("//android.widget.TextView[@content-desc='Thank you for your order!']"));
        Assert.assertTrue(thankYouMessage.isDisplayed(), "Order should be placed");
        System.out.println("Thank you message is displayed: " + thankYouMessage.getText());

        // Capture screenshot upon test failure
        if (!thankYouMessage.isDisplayed()) {
            File screenshot = driver.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshot, new File("screenshots/thank_you_message.png"));
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}