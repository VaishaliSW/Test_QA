package com.example.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.io.File;
import org.apache.commons.io.FileUtils;

public class ECommerceTest {
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

    @DataProvider(name = "purchaseData")
    public Object[][] purchaseData() {
        return new Object[][]{
            {"https://example.com", "iPhone 15", "INR", "123456", "Great product!", "John Doe", "john.doe@example.com"}
        };
    }

    @Test(dataProvider = "purchaseData")
    public void testECommercePurchase(String url, String productName, String currency, String pinCode, String reviewMessage, String userName, String userEmail) {
        driver.get(url);

        // Select product
        WebElement productElement = driver.findElement(By.xpath("//h2[text()='" + productName + "']"));
        productElement.click();

        // Add to cart
        WebElement addToCartButton = driver.findElement(By.id("add-to-cart"));
        addToCartButton.click();

        // Proceed to checkout
        WebElement checkoutButton = driver.findElement(By.id("checkout"));
        checkoutButton.click();

        // Select currency
        WebElement currencyDropdown = driver.findElement(By.id("currency"));
        currencyDropdown.sendKeys(currency);

        // Enter delivery details
        WebElement pinCodeField = driver.findElement(By.id("pin-code"));
        pinCodeField.sendKeys(pinCode);

        WebElement addressField = driver.findElement(By.id("address"));
        addressField.sendKeys("123, Elm Street");

        WebElement nameField = driver.findElement(By.id("name"));
        nameField.sendKeys(userName);

        WebElement emailField = driver.findElement(By.id("email"));
        emailField.sendKeys(userEmail);

        WebElement placeOrderButton = driver.findElement(By.id("place-order"));
        placeOrderButton.click();

        // Validate order confirmation
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement orderConfirmation = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("order-confirmation")));
        Assert.assertTrue(orderConfirmation.isDisplayed(), "Order confirmation not displayed");

        // Check delivery tracking option
        WebElement trackDeliveryOption = driver.findElement(By.id("track-delivery"));
        Assert.assertTrue(trackDeliveryOption.isDisplayed(), "Track delivery option not available");

        // Submit review
        WebElement reviewLink = driver.findElement(By.id("submit-review"));
        reviewLink.click();

        WebElement reviewTextArea = driver.findElement(By.id("review-text"));
        reviewTextArea.sendKeys(reviewMessage);

        WebElement submitReviewButton = driver.findElement(By.id("submit-review-btn"));
        submitReviewButton.click();

        // Validate review submission
        WebElement reviewConfirmation = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("review-confirmation")));
        Assert.assertTrue(reviewConfirmation.isDisplayed(), "Review confirmation not displayed");

        // Capture screenshot
        captureScreenshot("testECommercePurchase");
    }

    public void captureScreenshot(String testName) {
        File source = ((org.openqa.selenium.TakesScreenshot) driver).getScreenshotAs(org.openqa.selenium.OutputType.FILE);
        String destination = System.getProperty("user.dir") + "/Screenshots/" + testName + ".png";
        try {
            FileUtils.copyFile(source, new File(destination));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}