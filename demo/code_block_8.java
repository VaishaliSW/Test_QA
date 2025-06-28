package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WithdrawalPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By withdrawalAmountField = By.id("withdrawalAmount");
    private By withdrawalButton = By.id("withdrawalButton");
    private By withdrawalSuccessMessage = By.id("withdrawalSuccessMessage");

    public WithdrawalPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 10);
    }

    public void navigateToWithdrawalPage(String baseUrl) {
        driver.get(baseUrl + "/withdrawal");
        wait.until(ExpectedConditions.visibilityOfElementLocated(withdrawalAmountField));
    }

    public void enterWithdrawalAmount(String amount) {
        driver.findElement(withdrawalAmountField).sendKeys(amount);
    }

    public void clickWithdrawalButton() {
        driver.findElement(withdrawalButton).click();
    }

    public boolean isWithdrawalSuccessful() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(withdrawalSuccessMessage));
        WebElement successMessage = driver.findElement(withdrawalSuccessMessage);
        return successMessage.isDisplayed();
    }
}