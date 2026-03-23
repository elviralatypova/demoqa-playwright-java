package com.elviraqa;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Download;
import com.microsoft.playwright.Page;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileDownloadTest extends AbstractPlaywrightTest {
    private BrowserContext context;

    private Page page;

    @BeforeEach
    void createContextAndPage() {
        // Configure browser context with automatic download handling enabled
        context = browser.newContext(new Browser.NewContextOptions()
                .setAcceptDownloads(true));

        // Create a new page
        page = context.newPage();
    }

    @AfterEach
    void closeContext() {
        page.close();
        context.close();
    }

    @Test
    void shouldSuccessfullyDownloadFile() throws Exception {
        // Navigate to download page
        page.navigate("https://demoqa.com/upload-download");

        // Handle download event
        // Wait for the download to start after clicking the download button
        Download download = page.waitForDownload(() -> page.click("#downloadButton"));

        // Save file
        // Create temporary directory for downloaded file
        Path tempDir = Files.createTempDirectory("playwright-downloads");
        Path filePath = tempDir.resolve(download.suggestedFilename());

        // Save downloaded file to temporary location
        download.saveAs(filePath);
        System.out.println("File saved to: " + filePath);

        // File validation
        // Verify file exists
        assertTrue(Files.exists(filePath), "File should exist");

        // Verify file is not empty
        long fileSize = Files.size(filePath);
        assertTrue(fileSize > 0, "File should not be empty");

        // Verify file extension
        assertTrue(filePath.toString().endsWith(".jpeg"), "File should have JPEG extension");

        // Content type validation
        // Verify MIME type
        String mimeType = Files.probeContentType(filePath);
        assertEquals("image/jpeg", mimeType, "MIME type should be image/jpeg");

        // Additional content validation
        // Read file content and validate size
        byte[] fileContent = Files.readAllBytes(filePath);
        assertEquals(4096, fileContent.length, "File size should match expected value");

        // Cleanup temporary files (optional for demo purposes)
        Files.deleteIfExists(filePath);
    }
}
