package com.elviraqa;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Route;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class NetworkInterceptionTest extends AbstractPlaywrightTest{
    @Test
    void shouldDisplayInTableDataReturnedByBooksApi() {
        String mockedResponse = """
            {
                "books": [
                    {
                        "isbn": "test-1sbn-123",
                        "title": "Playwright for QA",
                        "subTitle": "Testing",
                        "author": "David Newton",
                        "publish_date": "2020-08-16T08:48:39.000Z",
                        "publisher": "Cosmos Publishing",
                        "pages": 350,
                        "description": "The important book about testing with Playwright",
                        "website": "https://playwrighttesting.com"
                    }
                ]
            }
        """;

        // Mock API BEFORE navigation
        page.route(
                "**/BookStore/v1/Books",
                route -> route.fulfill(
                        new Route.FulfillOptions()
                                .setStatus(200)
                                .setContentType("application/json")
                                .setBody(mockedResponse)
                )
        );

        // Open the test page
        page.navigate("https://demoqa.com/");

        // Click "Book Store Application"
        page.locator("text=Book Store Application").click();

        // Find the first row in the table
        Locator firstRow = page.locator("tbody tr").first();

        // Get the second column (index 1)
        Locator secondColumn = firstRow.locator("td").nth(1);

        // Verify title value
        assertThat(secondColumn).hasText("Playwright for QA");
    }
}
