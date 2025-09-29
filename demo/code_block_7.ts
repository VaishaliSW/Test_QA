import { test, expect } from '@playwright/test';
import { LoginPage } from '../pages/LoginPage';
import { InventoryPage } from '../pages/InventoryPage';
import { CartPage } from '../pages/CartPage';
import { CheckoutPage } from '../pages/CheckoutPage';
import testData from '../testdata.json';

test('End-to-end shopping test', async ({ page }) => {
  const loginPage = new LoginPage(page);
  const inventoryPage = new InventoryPage(page);
  const cartPage = new CartPage(page);
  const checkoutPage = new CheckoutPage(page);

  // Login
  await loginPage.navigate();
  await loginPage.login(testData.username, testData.password);
  
  // Verify successful login
  await expect(page).toHaveURL(/inventory.html/);
  await page.screenshot({ path: 'screenshots/logged-in.png' });

  // Add item to cart
  await inventoryPage.addItemToCart('Sauce Labs Backpack');
  
  // Go to cart
  await inventoryPage.goToCart();
  await expect(page).toHaveURL(/cart.html/);
  
  // Proceed to checkout
  await cartPage.proceedToCheckout();
  await expect(page).toHaveURL(/checkout-step-one.html/);
  
  // Fill checkout information
  await checkoutPage.fillInformation(testData.firstName, testData.lastName, testData.zipCode);
  await expect(page).toHaveURL(/checkout-step-two.html/);
  
  // Finish order
  await checkoutPage.finishOrder();
  
  // Verify order completion
  await expect(page.locator('.complete-header')).toHaveText('Thank you for your order!');
  await page.screenshot({ path: 'screenshots/order-complete.png' });
});