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
    public void testTitleOnDemoQA() {
        page.navigate("https://demoqa.com/");
        String title = page.title();
        assertEquals("demosite", title);
    }

    @AfterEach
    void tearDown() {
        browser.close();
        playwright.close();
    }
}
