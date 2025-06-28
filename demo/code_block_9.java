package tests;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.WithdrawalPage;
import utils.DriverManager;
import utils.ConfigReader;
import utils.TestDataReader;

public class ATMTests {
    private WebDriver driver;
    private LoginPage loginPage;
    private WithdrawalPage withdrawalPage;

    @BeforeMethod
    public void setUp() {
        driver = DriverManager.getDriver();
        loginPage = new LoginPage(driver);
        withdrawalPage = new WithdrawalPage(driver);
    }

    @AfterMethod
    public void tearDown() {
        DriverManager.quitDriver();
    }

    @Test
    public void testUserAuthentication() {
        loginPage.navigateToLoginPage(ConfigReader.getConfigData("baseUrl"));
        loginPage.enterUsername(TestDataReader.getTestData("login.username"));
        loginPage.enterPassword(TestDataReader.getTestData("login.password"));
        loginPage.clickLoginButton();
        Assert.assertTrue(loginPage.isLoginSuccessful(), "Login was not successful");
    }

    @Test
    public void testCashWithdrawal() {
        withdrawalPage.navigateToWithdrawalPage(ConfigReader.getConfigData("baseUrl"));
        withdrawalPage.enterWithdrawalAmount(TestDataReader.getTestData("withdrawal.amount"));
        withdrawalPage.clickWithdrawalButton();
        Assert.assertTrue(withdrawalPage.isWithdrawalSuccessful(), "Withdrawal was not successful");
    }
}