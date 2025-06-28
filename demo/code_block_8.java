// File: DashboardPage.java
package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DashboardPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By withdrawalLink = By.id("withdrawalLink");

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 10);
    }

    public void navigateToWithdrawalPage() {
        driver.findElement(withdrawalLink).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("withdrawalForm")));
    }
}