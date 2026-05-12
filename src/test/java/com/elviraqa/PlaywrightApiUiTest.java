package com.elviraqa;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.Cookie;
import com.microsoft.playwright.options.RequestOptions;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.microsoft.playwright.options.WaitUntilState;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.*;

import javax.swing.*;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PlaywrightApiUiTest {
    // Fragment 1: Constants and variables
    private static final String BASE_URL = "https://demoqa.com";

    private static final String LOGIN_URL = BASE_URL + "/Account/v1/Login";

    private static final String USERNAME = "testuser";

    private static final String PASSWORD = "Test@123";

    private static final int TIMEOUT = 30000;

    private static final long COOKIE_EXPIRATION_DAYS = 1;

    static Playwright playwright;

    static Browser browser;

    static APIRequestContext apiRequestContext;

    static String authToken;

    static String userId;

    static String userName;

    BrowserContext context;

    Page page;

    // Fragment 2: Global setup @BeforeAll
    @BeforeAll
    static void globalSetup() {
        playwright = Playwright.create();
        authenticateUser();
        setupApiContext();
        launchBrowser();
    }

    // Fragment 3: Context setup @BeforeEach
    @BeforeEach
    void setupBrowserContext() {
        context = browser.newContext();
        addAuthCookiesToContext();
        page = context.newPage();
    }

    // Fragment 4: Test method structure
    @Test
    void testProfileAfterApiLogin() {
        navigateToProfile(); // UI actions
        verifyUserProfileData(); // Compare UI and API data
    }

    // Fragment 5: Navigate to profile (UI)
    private void navigateToProfile() {
        Allure.step( "1. Navigate to profile via UI", () -> {
            page.navigate(BASE_URL + "/profile",
                    new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));

            waitForLoaderToDisappear(); // Wait for loader to disappear
            waitForProfileWrapper(); // Wait for profile container

            Locator userNameLocator = page.locator("#userName-value");
            assertTrue(userNameLocator.isVisible(), "Element #userName-value is not visible");

            String actualUsername = userNameLocator.textContent();
            assertNotNull(actualUsername, "Username is not displayed");
        });
    }

    // Fragment 6: Profile data verification
    private void verifyUserProfileData() {
        Allure.step("2. Verify profile data", () -> {
            JsonObject apiUserData = fetchUserDataFromApi(); // API request
            String apiUsername = apiUserData.get("username").getAsString();

            String uiUsername = page.textContent("#userName-value"); // UI data
            assertEquals(apiUsername, uiUsername, "Username in UI does not match API");
        });
    }

    @AfterEach
    void closeBrowserContext() {
        context.close();
    }

    @AfterAll
    static void globalCleanup() {
        closeResources();
    }

    // Fragment 7: Fetch user data via API
    private JsonObject fetchUserDataFromApi() {
        APIResponse apiResponse = apiRequestContext.get("/Account/v1/User/" + userId);
        assertEquals(200, apiResponse.status(), "API request failed");
        return JsonParser.parseString(apiResponse.text()).getAsJsonObject();
    }

    // Fragment 8: Close resources
    private static void closeResources() {
        if (apiRequestContext != null) {
            apiRequestContext.dispose();
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    // Fragment 9: API authentication
    private static void authenticateUser() {
        // Create temporary context for authentication
        APIRequestContext tempContext = playwright.request().newContext();

        try {
            RequestOptions requestOptions = RequestOptions.create()
                    .setData(
                            Map.of(
                                    "userName", USERNAME,
                                    "password", PASSWORD
                            )
                    );

            APIResponse authResponse = tempContext.post(LOGIN_URL, requestOptions);

            assertEquals(200, authResponse.status(), "Authentication failed");

            JsonObject responseJson = JsonParser.parseString(authResponse.text()).getAsJsonObject();
            authToken = responseJson.get("token").getAsString();
            userId = responseJson.get("userId").getAsString();
            userName = responseJson.get("username").getAsString();

            assertNotNull(authToken, "Authorization token was not received");
            assertNotNull(userId, "User ID was not received");
        } finally {
            // Ensure context is closed even if exception occurs
            tempContext.dispose();
        }
    }

    // Fragment 10: API context setup
    private static void setupApiContext() {
        apiRequestContext = playwright.request().newContext(
                new APIRequest.NewContextOptions()
                        .setBaseURL(PlaywrightApiUiTest.BASE_URL)
                        .setExtraHTTPHeaders(Map.of("Authorization", "Bearer " + PlaywrightApiUiTest.authToken))
        );
    }

    // Fragment 11: Launch browser
    private static void launchBrowser() {
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false)
                        .setSlowMo(500)
        );
    }

    // Fragment 12: Add authentication cookies
    private void addAuthCookiesToContext() {
        long expirationTime = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(COOKIE_EXPIRATION_DAYS);
        Date expirationDate = new Date(expirationTime);

        context.addCookies(Arrays.asList(
                createCookie("token", PlaywrightApiUiTest.authToken, expirationDate),
                createCookie("userID", userId, expirationDate),
                createCookie("userName", userName, expirationDate),
                createCookie("expires", expirationDate.toString(), expirationDate)
        ));
    }

    // Fragment 13: Create cookie object
    private Cookie createCookie(String name, String value, Date expires) {
        return new Cookie(name, value)
                .setDomain("demoqa.com")
                .setPath("/")
                .setExpires((double) expires.getTime() / 1000);
    }

    // Fragment 14: Wait for loader to disappear
    private void waitForLoaderToDisappear() {
        page.locator(".loader:has-text('Loading')")
                .waitFor(new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.HIDDEN)
                        .setTimeout(PlaywrightApiUiTest.TIMEOUT));
    }

    // Fragment 15: Wait for profile container to appear
    private void waitForProfileWrapper() {
        page.locator(".profile-wrapper")
                .waitFor(new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(PlaywrightApiUiTest.TIMEOUT));
    }
}
