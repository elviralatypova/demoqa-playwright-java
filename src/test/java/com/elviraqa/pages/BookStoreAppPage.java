package com.elviraqa.pages;

import com.microsoft.playwright.Page;

public class BookStoreAppPage {
    private final Page page;

    public BookStoreAppPage(Page page) {
        this.page = page;
    }

    public void openLoginForm() {
        page.locator("span:has-text('Login')").click();
    }
}
