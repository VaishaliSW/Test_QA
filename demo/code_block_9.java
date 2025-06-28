// File: WithdrawalPage.java
package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WithdrawalPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By amountField = By.id("amount");
    private By withdrawButton = By.id("withdrawButton");

    public WithdrawalPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 10);
    }

    public void enterAmount(String amount) {
        driver.findElement(amountField).sendKeys(amount);
    }

    public void clickWithdrawButton() {
        driver.findElement(withdrawButton).click();
    }

    public void waitForWithdrawalConfirmation() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("confirmationMessage")));
    }
}