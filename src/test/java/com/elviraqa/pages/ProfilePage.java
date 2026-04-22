package com.elviraqa.pages;

import com.microsoft.playwright.Page;

public class ProfilePage {
    private final Page page;

    public ProfilePage(Page page) {
        this.page = page;
    }

    public boolean isUsernameVisible() {
        return this.page.isVisible("#userName-value");
    }
}
