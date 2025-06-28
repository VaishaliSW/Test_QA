// File: HomePage.java
package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {
    private WebDriver driver;
    private By ageInput = By.id("ageInput");
    private By calculateButton = By.id("calculateButton");
    private By discountResult = By.id("discountResult");

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterAge(int age) {
        driver.findElement(ageInput).sendKeys(String.valueOf(age));
    }

    public void calculateDiscount() {
        driver.findElement(calculateButton).click();
    }

    public double getDiscountResult() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement discountElement = wait.until(ExpectedConditions.visibilityOfElementLocated(discountResult));
        return Double.parseDouble(discountElement.getText());
    }
}