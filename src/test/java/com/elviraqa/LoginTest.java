package com.elviraqa;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitUntilState;
import org.junit.jupiter.api.*;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginTest extends AbstractPlaywrightTest {
    @Test
    void shouldSuccessfullyLoginWithCorrectUsernameAndPassword() {
        // Navigate to the login page with a custom page load strategy
        page.navigate("https://demoqa.com/login", new Page.NavigateOptions()
                .setWaitUntil(WaitUntilState.DOMCONTENTLOADED)); // Wait until DOM is loaded

        // Enter username and password
        page.fill("#userName", "testuser");
        page.fill("#password", "Test123!");

        // Click the login button
        page.click("#login");

        // Verify that the correct user is logged in
        Locator userNameLabel = page.locator("#userName-value");
        assertThat(userNameLabel).containsText("testuser");
    }
}
