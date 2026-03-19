package com.elviraqa;

import com.elviraqa.pages.FormsPage;
import com.elviraqa.pages.HomePage;
import com.elviraqa.pages.PracticeFormPage;
import com.elviraqa.pages.ResultModal;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegistrationFormTest extends AbstractPlaywrightTest {
    @Test
    void shouldSuccessfullySubmitRegistrationForm() {
        HomePage homePage = new HomePage(page);
        homePage.open();
        homePage.openFormsSection();

        FormsPage formsPage = new FormsPage(page);
        formsPage.openPracticeForm();

        PracticeFormPage practiceFormPage = new PracticeFormPage(page);
        practiceFormPage.fillName("John", "Smith");
        practiceFormPage.fillEmail("test@example.com");
        practiceFormPage.selectMale();
        practiceFormPage.fillPhone("8976564677");
        practiceFormPage.fillDateOfBirth(3, 3, 1989);
        practiceFormPage.selectReading();
        practiceFormPage.uploadFile(Paths.get("src/test/resources/test-upload.txt"));
        practiceFormPage.fillAddress("Kennedy street, 4");
        practiceFormPage.setState("NCR");
        practiceFormPage.setCity("Delhi");
        practiceFormPage.submit();

        ResultModal resultModal = new ResultModal(page);
        assertEquals("John Smith", resultModal.getValue("Student Name"));
        assertEquals("test@example.com", resultModal.getValue("Student Email"));
        assertEquals("Male", resultModal.getValue("Gender"));
        assertEquals("8976564677", resultModal.getValue("Mobile"));
        assertEquals("03 April,1989", resultModal.getValue("Date of Birth"));
        assertEquals("", resultModal.getValue("Subjects"));
        assertEquals("Reading", resultModal.getValue("Hobbies"));
        assertEquals("test-upload.txt", resultModal.getValue("Picture"));
        assertEquals("Kennedy street, 4", resultModal.getValue("Address"));
        assertEquals("NCR Delhi", resultModal.getValue("State and City"));
    }
}


