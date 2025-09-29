import { Page } from '@playwright/test';

export class InventoryPage {
  private page: Page;

  constructor(page: Page) {
    this.page = page;
  }

  async addItemToCart(itemName: string) {
    await this.page.click(`//div[text()="${itemName}"]/ancestor::div[@class="inventory_item"]//button[contains(@id, "add-to-cart")]`);
  }

  async goToCart() {
    await this.page.click('.shopping_cart_link');
  }
}