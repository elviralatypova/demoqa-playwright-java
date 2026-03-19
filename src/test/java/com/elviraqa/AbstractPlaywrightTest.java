package com.elviraqa;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public abstract class AbstractPlaywrightTest {
    private static Playwright playwright;

    protected static Browser browser;

    private BrowserContext context;

    protected Page page;

    @BeforeAll
    static void beforeAll() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
    }

    @AfterAll
    static void afterAll() {
        browser.close();
        playwright.close();
    }

    @BeforeEach
    void setUp() {
        context = browser.newContext();
        page = browser.newPage();
    }

    @AfterEach
    void tearDown() {
        page.close();
        context.close();
    }
}
