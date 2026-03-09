package com.elviraqa;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlaywrightTitleTest {

    private Playwright playwright;

    private Browser browser;

    private Page page;

    @BeforeEach
    void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        page = browser.newPage();
    }

    @Test
    public void testTitleOnPlaywrightDev() {
        page.navigate("https://playwright.dev/");
        String title = page.title();
        assertEquals("Fast and reliable end-to-end testing for modern web apps | Playwright", title);
    }

    @AfterEach
    void tearDown() {
        browser.close();
        playwright.close();
    }
}
