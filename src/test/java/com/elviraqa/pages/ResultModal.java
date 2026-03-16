package com.elviraqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class ResultModal {
    private final Page page;

    public ResultModal(Page page) {
        this.page = page;
    }

    public String getValue(String label) {
        Locator row = page.locator("tr:has(td:nth-child(1):has-text('" + label + "'))");
        return row.locator("td:nth-child(2)").textContent();
    }
}
