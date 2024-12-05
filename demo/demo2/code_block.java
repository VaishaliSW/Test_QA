package com.example.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;

public class LoanApplicationTests {
    private WebDriver driver;
    private Properties prop;
    private Logger log = LogManager.getLogger(LoanApplicationTests.class);

    @BeforeMethod
    public void setUp() throws IOException {
        prop = new Properties();
        FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
        prop.load(fis);

        String browser = prop.getProperty("browser");
        if (browser.equalsIgnoreCase("chrome")) {
            driver = new ChromeDriver();
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

    @Test
    public void verifyDocumentChecklist() {
        driver.get(prop.getProperty("baseUrl"));
        driver.findElement(By.id("username")).sendKeys("user123");
        driver.findElement(By.id("password")).sendKeys("abcabd");
        driver.findElement(By.id("loginButton")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement loanApplication = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loanApplication")));
        loanApplication.click();

        String[] documents = {"Address proof", "property completion state proof", "income proof", "credit score document"};
        for (String doc : documents) {
            WebElement documentElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='" + doc + "']")));
            documentElement.click();
            WebElement openDocument = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("openDocument")));
            openDocument.click();
            WebElement checkbox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='checkbox' and @value='" + doc + "']")));
            checkbox.click();
        }

        WebElement completeVerification = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("completeVerification")));
        completeVerification.click();
    }

    @DataProvider(name = "creditScoreData")
    public Object[][] creditScoreData() {
        return new Object[][]{
            {"www.creditscore.com", "user123", "abcabd", "acb1243"}
        };
    }

    @Test(dataProvider = "creditScoreData")
    public void verifyCreditScore(String url, String username, String password, String customerId) {
        driver.get(url);
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("loginButton")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("searchBox")));
        searchBox.sendKeys(customerId);
        WebElement searchButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("searchButton")));
        searchButton.click();

        WebElement creditScoreElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("creditScore")));
        String creditScore = creditScoreElement.getText();
        assert Integer.parseInt(creditScore) > 500 : "Credit score is below 500";

        WebElement verificationStatus = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("verificationStatus")));
        verificationStatus.sendKeys(creditScore);

        WebElement logoutButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logoutButton")));
        logoutButton.click();
    }
}