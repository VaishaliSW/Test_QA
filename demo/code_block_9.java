package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AccountDetailsPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By welcomeMessage = By.id("welcomeMessage");
    private By balanceDisplay = By.id("balanceDisplay");

    public AccountDetailsPage(WebDriver driver, int timeout) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, timeout);
    }

    public void verifyWelcomeMessage(String expectedMessage) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(welcomeMessage));
        assert driver.findElement(welcomeMessage).getText().contains(expectedMessage);
    }

    public void verifyAccountDetails(String expectedBalance) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(balanceDisplay));
        assert driver.findElement(balanceDisplay).getText().equals(expectedBalance);
    }
}