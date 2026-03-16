package com.elviraqa.pages;

import com.microsoft.playwright.Page;

public class FormsPage {
    private final Page page;

    public FormsPage(Page page) {
        this.page = page;
    }

    public void openPracticeForm() {
        page.locator("li.btn-light:has-text('Practice Form')").click();
    }
}
