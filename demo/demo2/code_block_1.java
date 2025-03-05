package com.ecommerce.tests;

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

import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

public class ECommerceTests {

    private AppiumDriver<MobileElement> driver;
    private DesiredCapabilities capabilities = new DesiredCapabilities();
    private String platform = System.getProperty("platform", "android"); // default to android

    @BeforeClass
    public void setup() throws MalformedURLException {
        if (platform.equalsIgnoreCase("android")) {
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Emulator");
            capabilities.setCapability(MobileCapabilityType.APP, "path/to/android/app.apk");
            driver = new AndroidDriver<>(new URL("http://localhost:4723/wd/hub"), capabilities);
        } else if (platform.equalsIgnoreCase("ios")) {
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone Simulator");
            capabilities.setCapability(MobileCapabilityType.APP, "path/to/ios/app.ipa");
            driver = new IOSDriver<>(new URL("http://localhost:4723/wd/hub"), capabilities);
        }
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @DataProvider(name = "userData")
    public Object[][] getUserData() throws IOException, CsvException {
        CSVReader reader = new CSVReader(new FileReader("path/to/data.csv"));
        return reader.readAll().toArray(new Object[0][]);
    }

    @Test(priority = 1)
    public void testSelectiPhone15() {
        MobileElement product = driver.findElement(By.xpath("//android.widget.TextView[@text='iPhone 15']"));
        product.click();
        MobileElement addToCart = driver.findElement(By.id("com.ecommerce.app:id/addToCartButton"));
        addToCart.click();
        MobileElement cartItem = driver.findElement(By.xpath("//android.widget.TextView[@text='iPhone 15']"));
        Assert.assertNotNull(cartItem, "iPhone 15 should be added to the cart");
    }

    @Test(priority = 2, dataProvider = "userData")
    public void testProvideDeliveryDetails(String name, String address, String pinCode) {
        MobileElement nameField = driver.findElement(By.id("com.ecommerce.app:id/nameField"));
        MobileElement addressField = driver.findElement(By.id("com.ecommerce.app:id/addressField"));
        MobileElement pinCodeField = driver.findElement(By.id("com.ecommerce.app:id/pinCodeField"));

        nameField.sendKeys(name);
        addressField.sendKeys(address);
        pinCodeField.sendKeys(pinCode);

        MobileElement submitButton = driver.findElement(By.id("com.ecommerce.app:id/submitButton"));
        submitButton.click();

        // Validation for successful submission
        MobileElement successMessage = driver.findElement(By.xpath("//android.widget.TextView[@text='Address saved successfully']"));
        Assert.assertNotNull(successMessage, "Address should be saved successfully");
    }

    @Test(priority = 3)
    public void testPaymentOption() {
        MobileElement paymentOptions = driver.findElement(By.id("com.ecommerce.app:id/paymentOptions"));
        paymentOptions.click();
        MobileElement rupeesOption = driver.findElement(By.xpath("//android.widget.TextView[@text='Rupees']"));
        rupeesOption.click();
        MobileElement selectedOption = driver.findElement(By.xpath("//android.widget.TextView[@text='Rupees']"));
        Assert.assertNotNull(selectedOption, "Rupees should be selected as the payment option");
    }

    @Test(priority = 4)
    public void testPostDeliveryReview() {
        // Assuming the delivery has been completed and now checking for the review option
        MobileElement reviewButton = driver.findElement(By.id("com.ecommerce.app:id/reviewButton"));
        reviewButton.click();
        MobileElement reviewForm = driver.findElement(By.id("com.ecommerce.app:id/reviewForm"));
        Assert.assertNotNull(reviewForm, "Review form should be displayed");

        MobileElement ratingField = driver.findElement(By.id("com.ecommerce.app:id/ratingField"));
        MobileElement commentsField = driver.findElement(By.id("com.ecommerce.app:id/commentsField"));

        ratingField.sendKeys("5");
        commentsField.sendKeys("Excellent product!");

        MobileElement submitReview = driver.findElement(By.id("com.ecommerce.app:id/submitReviewButton"));
        submitReview.click();

        // Validation for successful review submission
        MobileElement successMessage = driver.findElement(By.xpath("//android.widget.TextView[@text='Review submitted successfully']"));
        Assert.assertNotNull(successMessage, "Review should be submitted successfully");
    }

    @AfterClass
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}