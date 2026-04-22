package com.elviraqa.pages;

import com.microsoft.playwright.Page;

public class HomePage {
    private final Page page;

    public HomePage(Page page) {
        this.page = page;
    }

    public void open() {
        page.navigate("https://demoqa.com");
    }

    public void openFormsSection() {
        page.locator("div.card:has-text('Forms')").click();
    }

    public void openBookStoreAppSection() {
        page.locator("div.card:has-text('Book Store Application')").click();
    }
}
