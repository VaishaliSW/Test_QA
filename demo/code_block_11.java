// File: WithdrawalTest.java
package tests;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.WithdrawalPage;
import utils.DriverManager;
import utils.ConfigReader;
import utils.TestDataReader;

public class WithdrawalTest {
    private WebDriver driver;
    private DashboardPage dashboardPage;
    private WithdrawalPage withdrawalPage;

    @BeforeMethod
    public void setUp() {
        driver = DriverManager.getDriver(ConfigReader.getConfigData("browser"));
        dashboardPage = new DashboardPage(driver);
        withdrawalPage = new WithdrawalPage(driver);
    }

    @Test
    public void testWithdrawal() {
        String baseUrl = ConfigReader.getConfigData("baseUrl");
        String amount = TestDataReader.getTestData("withdrawal.amount");

        dashboardPage.navigateToWithdrawalPage();
        withdrawalPage.enterAmount(amount);
        withdrawalPage.clickWithdrawButton();
        withdrawalPage.waitForWithdrawalConfirmation();
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}