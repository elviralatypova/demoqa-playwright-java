package com.elviraqa;

import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DialogHandlingTest extends AbstractPlaywrightTest {
    @Test
    void shouldSuccessfullyHandleAlerts() {
        // Navigate to the alerts test page
        page.navigate("https://demoqa.com/alerts");

        // Handle simple Alert
        // Register a dialog handler BEFORE clicking the button
        page.onceDialog(dialog -> {
            assertEquals("alert", dialog.type()); // Verify dialog type
            assertEquals("You clicked a button", dialog.message()); // Verify message text
            dialog.accept(); // Click "OK"
        });

        // Click button that triggers the alert
        page.click("#alertButton");

        // Handle Confirm (Accept)
        // Handle confirm dialog and press OK
        page.onceDialog(dialog -> {
            assertEquals("confirm", dialog.type());
            assertEquals("Do you confirm action?", dialog.message());
            dialog.accept(); // Click "OK"
        });
        page.click("#confirmButton");

        // Verify result after accepting confirm dialog
        assertThat(page.locator("#confirmResult")).hasText("You selected Ok");

        // Handle Confirm (Dismiss)
        // Handle confirm dialog but press Cancel
        page.onceDialog(dialog -> {
            assertEquals("confirm", dialog.type());
            dialog.dismiss(); // Click "Cancel"
        });
        page.click("#confirmButton");

        // Verify result after dismissing confirm dialog
        assertThat(page.locator("#confirmResult")).hasText("You selected Cancel");

        // Handle Prompt
        // Prompt dialog allows entering text
        page.onceDialog(dialog -> {
            assertEquals("prompt", dialog.type());
            assertEquals("Please enter your name", dialog.message());
            dialog.accept("John Doe"); // Enter text and confirm
        });
        page.click("#promtButton");

        // Verify entered value is displayed
        assertThat(page.locator("#promptResult")).hasText("You entered John Doe");
        // Trigger prompt again WITHOUT handling it
        page.click("#promtButton");

        // Check that result is hidden (depends on page behavior)
        assertThat(page.locator("#promptResult")).isHidden();

        // Global dialog handler
        // This handler will be triggered for every dialog
        page.onDialog(dialog -> {
            if ("alert".equals(dialog.type())) {
                dialog.accept(); // Automatically accept all alerts
            }
        });

        // Trigger delayed alert (appears after a few seconds)
        page.click("#timerAlertButton");

        // Wait for alert to appear and be handled
        page.waitForTimeout(6000);
    }
}
