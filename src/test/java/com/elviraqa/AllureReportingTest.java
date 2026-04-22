package com.elviraqa;

import com.elviraqa.pages.BookStoreAppPage;
import com.elviraqa.pages.HomePage;
import com.elviraqa.pages.LoginPage;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Video;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestWatcher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AllureReportingTest extends AbstractPlaywrightTest {
    private BrowserContext testContext;

    private Page testPage;

    private Video video;

    private Path screenshotDir;

    @BeforeEach
    void initContext() {
        screenshotDir = Paths.get("screenshots/");
        try {
            Files.createDirectories(screenshotDir);
        } catch (IOException e) {
            throw new RuntimeException("Cannot create screenshots directory", e);
        }

        testContext = browser.newContext(new Browser.NewContextOptions()
                .setRecordVideoDir(Paths.get("videos/")));
        testPage = testContext.newPage();
        video = testPage.video();
    }

    @RegisterExtension
    TestWatcher watcher = new TestWatcher() {
        @Override
        public void testFailed(ExtensionContext extensionContext, Throwable cause) {
            try {
                if (testPage != null && !testPage.isClosed()) {
                    // Generate file name
                    String testName = extensionContext.getDisplayName();
                    Path screenshotPath = screenshotDir.resolve(testName + ".png");

                    // Take and save screenshot
                    byte[] screenshot = testPage.screenshot(new Page.ScreenshotOptions()
                            .setPath(screenshotPath) // Save to disk
                            .setFullPage(true));

                    // Attach to Allure report
                    saveScreenshotToAllure(screenshot, testName);
                    System.out.println("Screenshot saved: " + screenshotPath);
                }
            } catch (Exception e) {
                System.err.println("Error while taking screenshot: " + e.getMessage());
            }
        }

        @Attachment(value = "Screenshot on failure: {name}", type = "image/png")
        private byte[] saveScreenshotToAllure(byte[] screenshot, String name) {
            return screenshot;
        }
    };


    @Test
    void testLoginWithScreenshots() {
        Allure.step("1. Open login page", () -> {
            HomePage homePage = new HomePage(testPage);
            homePage.open();
            homePage.openBookStoreAppSection();

            BookStoreAppPage bookStoreAppPage = new BookStoreAppPage(testPage);
            bookStoreAppPage.openLoginForm();
        });

        LoginPage loginPage = new LoginPage(testPage);

        Allure.step("2. Fill in credentials", () -> {
            loginPage.fillUsername("cosmos_luna");
            loginPage.fillPassword("INVALID_PASSWORD");
        });


        Allure.step("3. Click login button", () -> {
            loginPage.login();
        });

//        Allure.step("4. Verify successful login", () -> {
//            ProfilePage profilePage = new ProfilePage(testPage);
//            assertTrue(profilePage.isUsernameVisible(), "User is not shown after login");
//        });

        Allure.step("4. Verify failed login", () -> {
            assertEquals("Invalid username or password!", loginPage.getErrorMessage(),
                    "User is not shown after login");
        });
    }

    @AfterEach
    void tearDown(TestInfo testInfo) throws IOException {
        if (testContext != null) {
            testPage.close();
            if (video != null) {
                String videoName = testInfo.getDisplayName() + ".webm";
                Path videoPath = Paths.get("videos/", videoName);
                video.saveAs(videoPath);
                attachVideo(videoName);
            }
            testContext.close();
        }
    }

    @Attachment(value = "Test video {name}", type = "video/webm")
    private byte[] attachVideo(String name) throws IOException {
        return Files.readAllBytes(
                Paths.get("videos/" + name)
        );
    }
}


