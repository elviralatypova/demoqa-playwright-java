package com.elviraqa;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.microsoft.playwright.options.WaitUntilState;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FormDataSubmitParameterizedTest extends AbstractPlaywrightTest {

    // Record to hold test data for the form
    record FormData(String fullName, String email, String currentAddress, String permanentAddress) {}

    // Test data provider for parameterized test
    static Stream<Arguments> formDataProvider() {
        return Stream.of(
                Arguments.of(new FormData("Peter Parker", "peter@test.com", "Dogs Street 5A","Cats Street 6B")),
                Arguments.of(new FormData("Anna Smith", "anna@example.com", "Baker Street 221B", "Red Street 250"))
        );
    }

    // Parameterized test that runs once for each FormData set
    @ParameterizedTest(name = "Form test: {0}")
    @MethodSource("formDataProvider")
    void shouldSuccessfullySubmitFormData(FormData data) {
        // Navigate to the main page and wait for DOM content to be loaded
        page.navigate("https://demoqa.com/",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));

        // Open Elements section
        page.locator("'Elements'").click();

        // Open Text Box page
        page.locator("'Text Box'").click();

        // Wait for form attachment to DOM
        page.locator("#userName")
                .waitFor(new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.ATTACHED));

        // Wait until form is visible before interacting
        page.waitForSelector("#userName",
                new Page.WaitForSelectorOptions()
                        .setState(WaitForSelectorState.VISIBLE));

        // Fill the form fields
        page.locator("#userName").fill(data.fullName());
        page.locator("#userEmail").fill(data.email());
        page.locator("#currentAddress").fill(data.currentAddress());
        page.locator("#permanentAddress").fill(data.permanentAddress());

        // Click submit button (with potential loader handling)
        Locator submitButton = page.locator("#submit");
        submitButton.click();

        // Explicitly wait for the output block to appear
        Locator output = page.locator("#output");
        output.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(5000)
        );

        // Get result text
        String resultText = output.innerText();

        // Assertions verifying submitted data is displayed correctly
        assertTrue(resultText.contains(data.fullName()),
                "Full name not found in output");

        assertTrue(resultText.contains(data.email()),
                "Email not found in output");

        assertTrue(resultText.contains(data.currentAddress()),
                "Current address not found in output");

        assertTrue(resultText.contains(data.permanentAddress()),
                "Permanent address not found in output");
    }
}
