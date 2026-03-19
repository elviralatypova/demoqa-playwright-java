package com.elviraqa;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.ViewportSize;
import com.microsoft.playwright.options.WaitUntilState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmulationIPhone12ProTest extends AbstractPlaywrightTest {
    private BrowserContext mobileContext;

    private Page mobilePage;

    @BeforeEach
    void setUp() {
        // Manually configure iPhone 12 Pro emulation parameters
        Browser.NewContextOptions iPhone12ProOptions = new Browser.NewContextOptions()
                .setUserAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 14_0 like Mac OS X) " +
                        "AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0 Mobile/15E148 Safari/604.1")
                .setViewportSize(390, 844)
                .setDeviceScaleFactor(3)
                .setIsMobile(true)
                .setHasTouch(true);

        // Create a new browser context configured for mobile device emulation
        mobileContext = browser.newContext(iPhone12ProOptions);

        // Create a new page inside the mobile context
        mobilePage = mobileContext.newPage();
    }

    @AfterEach
    void tearDown() {
        mobilePage.close();
        mobileContext.close();
    }

    @Test
    void shouldSuccessfullyEmulateIPhone12Pro() {
        // Navigate to the target website and wait until DOM is loaded
        mobilePage.navigate("https://demoqa.com", new Page.NavigateOptions()
                .setWaitUntil(WaitUntilState.DOMCONTENTLOADED));

        // Get viewport size to verify mobile resolution
        ViewportSize size = mobilePage.viewportSize();

        // Validate viewport width
        assertEquals(390, size.width,
                "Viewport width should match iPhone 12 Pro resolution");

        // Validate viewport height
        assertEquals(844, size.height,
                "Viewport height should match iPhone 12 Pro resolution");

        // Simulate touch interaction on mobile device
        mobilePage.tap("text=Elements");

        // Wait until mobile menu becomes visible
        mobilePage.waitForSelector(".element-list", new Page.WaitForSelectorOptions()
                .setTimeout(10000));

        // Verify mobile menu is displayed
        assertTrue(mobilePage.isVisible(".element-list"),
                "Mobile menu should be opened");
    }
}
