package com.elviraqa;

import com.elviraqa.pages.BookStoreAppPage;
import com.elviraqa.pages.HomePage;
import com.elviraqa.pages.LoginPage;
import com.elviraqa.pages.ProfilePage;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Tracing;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TracingDebugTest extends AbstractPlaywrightTest {
    private BrowserContext testContext;

    private Page testPage;

    @BeforeEach
    void initContext() {
        testContext = browser.newContext();
        testContext.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true)
        );
        testPage = testContext.newPage();
    }

    @Test
    void successfulLoginTest() {

        Allure.step("1. Open login page", () -> {
            HomePage homePage = new HomePage(testPage);
            homePage.open();
            homePage.openBookStoreAppSection();

            BookStoreAppPage bookStoreAppPage = new BookStoreAppPage(testPage);
            bookStoreAppPage.openLoginForm();
        });

        LoginPage loginPage = new LoginPage(testPage);

        Allure.step("2. Fill in credentials", () -> {
            loginPage.fillUsername("testuser");
            loginPage.fillPassword("Test@123");
        });


        Allure.step("3. Click login button", () -> {
            loginPage.login();
        });

        Allure.step("4. Verify successful login", () -> {
            ProfilePage profilePage = new ProfilePage(testPage);
            profilePage.isUsernameVisible();
        });


    }

    @AfterEach
    void saveTrace(TestInfo testInfo) throws IOException {
        String testName = testInfo.getTestMethod().get().getName();
        Path tracesDir = Paths.get("traces");
        Files.createDirectories(tracesDir);

        Path tracePath = tracesDir.resolve(testName + ".zip");
        testContext.tracing().stop(new Tracing.StopOptions().setPath(tracePath));
        attachTrace(tracePath.getFileName().toString());

        testContext.close();
    }

    @Attachment(value = "Tracing: {name}", type = "application/zip")
    private byte[] attachTrace(String name) throws IOException {
        return Files.readAllBytes(Paths.get("traces/" + name));
    }
}
