package com.example.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;

public class LoanApplicationTest {
    private WebDriver driver;
    private Properties prop;

    @BeforeMethod
    public void setUp() throws IOException {
        prop = new Properties();
        FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
        prop.load(fis);

        driver = WebDriverFactory.getDriver(prop.getProperty("browser"));
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(prop.getProperty("baseUrl"));
    }

    @Test(dataProvider = "documentChecklistData")
    public void testLoanDocumentChecklist(String loginId, String password, String[] documents) {
        // Login
        WebElement usernameField = driver.findElement(By.id("loginId"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("loginButton"));

        usernameField.sendKeys(loginId);
        passwordField.sendKeys(password);
        loginButton.click();

        // Navigate to Loan Application
        WebElement loanAppLink = driver.findElement(By.id("loanApplication"));
        loanAppLink.click();

        // Verify all documents
        for (String doc : documents) {
            WebElement docElement = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOfElementLocated(By.id(doc)));
            docElement.click();  // Open document

            WebElement checkBox = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.elementToBeClickable(By.id("checkBox" + doc)));
            checkBox.click();  // Verify document
        }

        // Complete verification
        WebElement completeVerification = driver.findElement(By.id("completeVerification"));
        completeVerification.click();

        // Assert completion
        WebElement status = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("verificationStatus")));
        assert status.getText().equals("Complete"): "Verification process is not completed successfully.";
    }

    @DataProvider(name = "documentChecklistData")
    public Object[][] documentChecklistData() {
        return new Object[][]{
                {"user123", "abcabd", new String[]{"addressProof", "propertyStateProof", "incomeProof", "creditScoreDoc"}}
        };
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}