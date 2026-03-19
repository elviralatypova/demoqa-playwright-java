package com.elviraqa;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlaywrightTitleTest extends AbstractPlaywrightTest {
    @Test
    public void testTitleOnDemoQA() {
        page.navigate("https://demoqa.com/");
        String title = page.title();
        assertEquals("demosite", title);
    }
}
