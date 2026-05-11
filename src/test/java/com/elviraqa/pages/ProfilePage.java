package com.elviraqa.pages;

import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ProfilePage {
    private final Page page;

    public ProfilePage(Page page) {
        this.page = page;
    }

    public void isUsernameVisible() {
        assertThat(page.locator("#userName-value"))
                .isVisible();
    }
}
