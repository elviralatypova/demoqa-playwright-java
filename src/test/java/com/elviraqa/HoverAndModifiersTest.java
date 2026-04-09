package com.elviraqa;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.KeyboardModifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HoverAndModifiersTest extends AbstractPlaywrightTest {

    @BeforeEach
    void setUp() {
        context = browser.newContext();
        page = context.newPage();
    }

    @Test
    void shouldDisplayCascadingSubMenuOnHover() {
        page.navigate("https://demoqa.com/menu");
        // Hover main item
        Locator mainItem = page.locator("'Main Item 2'");
        mainItem.hover(); // Simulate mouse hover

        // Verify submenu is visible
        Locator subMenu = page.locator("'SUB SUB LIST »'");
        assertTrue(subMenu.isVisible(), "Submenu should be visible after hover");

        // Hover submenu
        subMenu.hover(); // Cascading hover effect

        // Verify nested item is visible
        Locator subItem = page.locator("'Sub Sub Item 1'");
        assertTrue(subItem.isVisible(), "Nested menu should be visible after hover");
    }

    @Test
    void shouldHandleClickWithModifiers() {
        page.navigate("https://demoqa.com/links");

        // Shift + click opens new tab
        Page newPage = context.waitForPage(() -> {
            page.locator("#simpleLink").click(new Locator.ClickOptions()
                    .setModifiers(Arrays.asList(KeyboardModifier.SHIFT)));
        });

        // Verify new tab opened
        page.context().waitForCondition(() -> newPage.url().equals("https://demoqa.com/"));
        assertEquals("https://demoqa.com/", newPage.url(),
                "Link should open in a new tab with Shift");

        // Return focus to original page
        page.bringToFront();

        // Ctrl/META + click
        page.locator("#dynamicLink").click(new Locator.ClickOptions()
                .setModifiers(Arrays.asList(KeyboardModifier.META)));

        // Verify no navigation
        assertEquals("https://demoqa.com/links", page.url(),
                "Page should not navigate on Ctrl+Click");
    }

    @Test
    void shouldSelectMultipleCheckboxes() {
        page.navigate("https://demoqa.com/checkbox");

        // Expand tree nodes
        page.locator("span.rc-tree-switcher").first().click(); // Expand root node
        page.locator("span.rc-tree-switcher").nth(2).click(); // Expand Documents node
        page.locator("span.rc-tree-switcher").nth(3).click(); // Expand WorkSpace

        Locator desktopCheckbox = page.locator("div.rc-tree-treenode:has-text('Desktop') > span.rc-tree-checkbox");
        desktopCheckbox.click();
        assertThat(desktopCheckbox).containsClass("rc-tree-checkbox-checked");

        Locator documentsCheckbox = page.locator("div.rc-tree-treenode:has-text('Documents') > span.rc-tree-checkbox");
        documentsCheckbox.click();
        assertThat(desktopCheckbox).containsClass("rc-tree-checkbox-checked");
        assertThat(documentsCheckbox).containsClass("rc-tree-checkbox-checked");
    }
}
