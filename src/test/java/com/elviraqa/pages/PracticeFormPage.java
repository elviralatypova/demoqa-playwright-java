package com.elviraqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.nio.file.Path;

public class PracticeFormPage {
    private final Page page;
    private final Locator firstName;
    private final Locator lastName;
    private final Locator email;
    private final Locator maleRadio;
    private final Locator phone;
    private final Locator dateOfBirth;
    private final Locator readingCheckbox;
    private final Locator address;
    private final Locator submitButton;
    private final Locator stateDropdown;
    private final Locator cityDropdown;

    public PracticeFormPage(Page page) {
        this.page = page;

        firstName = page.getByRole(AriaRole.TEXTBOX,
                new Page.GetByRoleOptions().setName("First Name"));

        lastName = page.getByRole(AriaRole.TEXTBOX,
                new Page.GetByRoleOptions().setName("Last Name"));

        email = page.locator("#userEmail");

        maleRadio = page.locator("#gender-radio-1");

        phone = page.locator("#userNumber");

        dateOfBirth = page.locator("#dateOfBirthInput");

        readingCheckbox = page.getByRole(AriaRole.CHECKBOX,
                new Page.GetByRoleOptions().setName("Reading"));

        address = page.locator("#currentAddress");

        stateDropdown = page.locator("#state");

        cityDropdown = page.locator("#city");

        submitButton = page.locator("#submit");
    }

    public void fillName(String first, String last) {
        firstName.fill(first);
        lastName.fill(last);
    }

    public void fillEmail(String value) {
        email.fill(value);
    }

    public void selectMale() {
        maleRadio.check();
    }

    public void fillPhone(String number) {
        phone.fill(number);
    }

    public void fillDateOfBirth(int day, int month, int year) {
        dateOfBirth.click();
        page.locator(".react-datepicker__year-select").selectOption(String.valueOf(year));
        page.locator(".react-datepicker__month-select").selectOption(String.valueOf(month));
        page.locator(".react-datepicker__day--" + String.format("%03d", day)).first().click();
    }

    public void selectReading() {
        readingCheckbox.check();
    }

    public void uploadFile(Path file) {
        page.setInputFiles("#uploadPicture", file);
    }

    public void fillAddress(String text) {
        address.fill(text);
    }

    public void setState(String state) {
        stateDropdown.click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(state)).click();
    }

    public void setCity(String city) {
        cityDropdown.click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(city)).click();
    }

    public void submit() {
        submitButton.click();
    }
}
