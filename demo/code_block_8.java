// File: AgeBasedDiscountTest.java
package tests;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.HomePage;
import utils.DriverManager;
import utils.ConfigReader;
import utils.TestDataReader;

public class AgeBasedDiscountTest {
    private WebDriver driver;
    private ConfigReader configReader;
    private TestDataReader testDataReader;

    @BeforeMethod
    public void setUp() {
        configReader = new ConfigReader();
        testDataReader = new TestDataReader();
        driver = DriverManager.getDriver(configReader.getConfigData("browser"));
        driver.get(configReader.getConfigData("baseUrl"));
    }

    @Test
    public void testAgeBasedDiscount() {
        HomePage homePage = new HomePage(driver);
        int age = (int) testDataReader.getTestData("age");
        double expectedDiscount = (double) testDataReader.getTestData("expectedDiscount");

        homePage.enterAge(age);
        homePage.calculateDiscount();
        double actualDiscount = homePage.getDiscountResult();

        Assert.assertEquals(actualDiscount, expectedDiscount, "Discount does not match expected value");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}