package com.example.tests;

import com.example.pages.LoginPage;
import com.example.pages.HomePage;
import com.example.pages.CartPage;
import com.example.pages.CheckoutPage;
import com.example.utils.TestDataProvider;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class PurchaseTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private HomePage homePage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.example.com/");
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test(dataProvider = "purchaseData", dataProviderClass = TestDataProvider.class)
    public void testDiscountApplication(String username, String password, String discountCode, int expectedTotal) {
        loginPage.login(username, password);
        homePage.addBooksToCart(10);
        cartPage.verifyTotalPrice(300);
        cartPage.proceedToCheckout();
        checkoutPage.applyDiscount(discountCode);
        checkoutPage.verifyDiscountedPrice(expectedTotal);
        checkoutPage.completePurchase();
        checkoutPage.verifyOrderConfirmationEmail();
    }
}