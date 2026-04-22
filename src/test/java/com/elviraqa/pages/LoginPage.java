package com.elviraqa.pages;

import com.microsoft.playwright.Page;

public class LoginPage {
    private final Page page;

    public LoginPage(Page page) {
        this.page = page;
    }

    public void fillUsername(String username) {
        page.fill("#userName", username);
    }

    public void fillPassword(String password) {
        page.fill("#password", password);
    }

    public void login() {
        page.click("#login");
    }

    public String getErrorMessage() {
        return page.locator("#output").textContent();
    }
}
