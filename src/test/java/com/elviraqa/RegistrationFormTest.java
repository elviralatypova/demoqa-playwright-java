package com.elviraqa;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.junit.jupiter.api.*;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegistrationFormTest {

    private static Playwright playwright;

    private static Browser browser;

    private BrowserContext context;

    private Page page;

    @BeforeAll
    static void beforeAll() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
    }

    @AfterAll
    static void afterAll() {
        browser.close();
        playwright.close();
    }

    @BeforeEach
    void setUp() {
        context = browser.newContext();
        page = browser.newPage();
    }

    @AfterEach
    void tearDown() {
        page.close();
        context.close();
    }

    @Test
    void shouldSuccessfullySubmitRegistrationForm() {
        // NAVIGATION AND WAITING

        // Open the main page of the demo website
        page.navigate("https://demoqa.com");

        // Wait until the cards on the main page are visible
        page.waitForSelector(".card", new Page.WaitForSelectorOptions().setTimeout(10000));

        // LOCATING ELEMENTS

        // Find and click the "Forms" section card
        Locator elementsCard = page.locator("div.card:has-text('Forms')");
        elementsCard.click();

        // Open the "Practice Form" page
        Locator textBoxListItem = page.locator("li.btn-light:has-text('Practice Form')");
        textBoxListItem.click();

        //INTERACTION WITH PAGE ELEMENTS

        // Fill in the First Name field
        Locator firstNameLabel = page.getByRole(AriaRole.TEXTBOX,
                new Page.GetByRoleOptions().setName("First Name"));
        firstNameLabel.fill("Valera");

        // Fill in the Last Name field
        Locator lastNameLabel = page.getByRole(AriaRole.TEXTBOX,
                new Page.GetByRoleOptions().setName("Last Name"));
        lastNameLabel.fill("Antipov");

        // Type email into the Email input field
        Locator emailInput = page.locator("#userEmail");
        emailInput.pressSequentially("test@example.com");

        // Select the "Male" radio button
        Locator maleRadio = page.locator("#gender-radio-1");
        maleRadio.check();
        assertTrue(maleRadio.isChecked(), "Male radio button should be selected");

        // Fill in the mobile phone number
        Locator numberInput = page.locator("#userNumber");
        numberInput.fill("8976564677");

        // Open the date picker
        Locator dateOfBirth = page.locator("#dateOfBirthInput");
        dateOfBirth.click();

        // Select year, month, and day
        page.locator(".react-datepicker__year-select").selectOption("1989");
        page.locator(".react-datepicker__month-select").selectOption("3");
        page.locator(".react-datepicker__day--003").first().click();

        // Select the "Reading" hobby checkbox
        Locator readingCheckbox = page.getByRole(AriaRole.CHECKBOX,
                new Page.GetByRoleOptions().setName("Reading"));
        readingCheckbox.check();
        assertTrue(readingCheckbox.isChecked(), "Reading checkbox should be selected");

        // Upload a file
        page.setInputFiles("#uploadPicture", Paths.get("src/test/resources/test-upload.txt"));

        // Fill in the current address
        Locator addressArea = page.locator("#currentAddress");
        addressArea.fill("Kennedi street, 4");

        // WORKING WITH DROPDOWN LISTS

        // Open the State dropdown
        Locator stateDropdown = page.locator("#state");
        stateDropdown.click();

        // Select the NCR option
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("NCR")).click();

        // Open the City dropdown
        Locator cityDropdown = page.locator("#city");
        cityDropdown.click();

        // Select the Delhi option
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("Delhi")).click();

        // Submit the form
        Locator submitButton = page.locator("#submit");
        submitButton.click();

        // Wait until the confirmation modal window appears
        page.waitForSelector("#example-modal-sizes-title-lg", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));

        // VALIDATION OF SUBMITTED DATA IN THE RESULT TABLE
        assertEquals("Valera Antipov", getTableValueByLabel("Student Name"));
        assertEquals("test@example.com", getTableValueByLabel("Student Email"));
        assertEquals("Male", getTableValueByLabel("Gender"));
        assertEquals("8976564677", getTableValueByLabel("Mobile"));
        assertEquals("03 April,1989", getTableValueByLabel("Date of Birth"));
        assertEquals("", getTableValueByLabel("Subjects"));
        assertEquals("Reading", getTableValueByLabel("Hobbies"));
        assertEquals("test-upload.txt", getTableValueByLabel("Picture"));
        assertEquals("Kennedi street, 4", getTableValueByLabel("Address"));
        assertEquals("NCR Delhi", getTableValueByLabel("State and City"));
    }

    //Helper method that finds a row in the result table by its label
    //and returns the value from the second column.
    private String getTableValueByLabel(String label) {
        Locator row = page.locator("tr:has(td:nth-child(1):has-text('" + label + "'))");
        return row.locator("td:nth-child(2)").textContent();
    }
}


