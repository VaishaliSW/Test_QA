package com.example.tests;

import com.example.pages.LoginPage;
import com.example.pages.LoanApplicationPage;
import com.example.pages.CreditScorePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.time.Duration;

public class LoanApplicationTests {
    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    public void verifyChecklistFunctionality() {
        driver.get("https://loan-application-system.com");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterUsername("user123");
        loginPage.enterPassword("abcabd");
        loginPage.clickLogin();

        LoanApplicationPage loanApplicationPage = new LoanApplicationPage(driver);
        loanApplicationPage.navigateToLoanApplication();

        Assert.assertTrue(loanApplicationPage.areDocumentsVerified(), "Documents are not verified");
    }

    @Test
    public void confirmCreditScoreAbove500() {
        driver.get("https://loan-application-system.com");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterUsername("user123");
        loginPage.enterPassword("abcabd");
        loginPage.clickLogin();

        LoanApplicationPage loanApplicationPage = new LoanApplicationPage(driver);
        loanApplicationPage.navigateToLoanApplication();

        CreditScorePage creditScorePage = loanApplicationPage.openCreditScoreReport();
        creditScorePage.loginToCreditScore("user123", "abcabd");
        creditScorePage.navigateToCreditScorePage();
        creditScorePage.searchCustomerID("acb1243");

        int creditScore = creditScorePage.getCreditScore();
        Assert.assertTrue(creditScore > 500, "Credit score is not above 500");

        creditScorePage.updateVerificationStatus();
        creditScorePage.logout();
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}