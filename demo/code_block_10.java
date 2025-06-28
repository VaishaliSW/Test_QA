// File: LoginTest.java
package tests;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.DashboardPage;
import utils.DriverManager;
import utils.ConfigReader;
import utils.TestDataReader;

public class LoginTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private DashboardPage dashboardPage;

    @BeforeMethod
    public void setUp() {
        driver = DriverManager.getDriver(ConfigReader.getConfigData("browser"));
        loginPage = new LoginPage(driver);
        dashboardPage = new DashboardPage(driver);
    }

    @Test
    public void testLogin() {
        String baseUrl = ConfigReader.getConfigData("baseUrl");
        String username = TestDataReader.getTestData("login.username");
        String password = TestDataReader.getTestData("login.password");

        loginPage.navigateToLoginPage(baseUrl);
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.clickLoginButton();
        loginPage.waitForDashboardPage();
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}