# DemoQA Playwright Java Test Automation

A comprehensive test automation framework for [DemoQA](https://demoqa.com) website built with Playwright and Java, demonstrating modern test automation practices and the Page Object Model design pattern.

## Test Reports

Automated test reports are generated with Allure and published on every push:

**[View Latest Test Reports](https://elviralatypova.github.io/demoqa-playwright-java)**

## Technologies & Tools

- **Java 25** - Programming language
- **Playwright 1.58.0** - Browser automation framework
- **JUnit 5** - Testing framework
- **Allure 2.33.0** - Test reporting
- **Maven** - Build and dependency management
- **GitHub Actions** - CI/CD pipeline

## Features Demonstrated

- **Page Object Model (POM)** - Clean separation between test logic and page interactions
- **Base Test Class Pattern** - Reusable test setup and teardown
- **Mobile Device Emulation** - Testing with iPhone 12 Pro configuration
- **Dialog Handling** - Managing browser alerts and confirmations
- **Form Interactions** - Complex form filling with dropdowns, date pickers, file uploads
- **Continuous Integration** - Automated test execution on every push
- **Test Reporting** - Allure reports with test history and trends

## Test Coverage

The framework includes tests for:

- User registration forms with validation
- Login functionality
- Dialog/alert handling
- Mobile responsive design
- Form interactions (dropdowns, date pickers, checkboxes, file uploads)

## Prerequisites

- Java 25 or higher
- Maven 3.6+
- Git

## Setup

1. Clone the repository:
```bash
git clone https://github.com/elviralatypova/demoqa-playwright-java.git
cd demoqa-playwright-java
```

2. Install dependencies:
```bash
mvn clean install
```

3. Install Playwright browsers:
```bash
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"
```

## Running Tests

**Run all tests:**
```bash
mvn clean test
```

**Run a specific test class:**
```bash
mvn test -Dtest=RegistrationFormTest
```

**Run a specific test method:**
```bash
mvn test -Dtest=RegistrationFormTest#shouldSuccessfullySubmitRegistrationForm
```

## Project Structure

```
src/test/java/com/elviraqa/
├── AbstractPlaywrightTest.java      # Base class with Playwright setup/teardown
├── RegistrationFormTest.java        # Registration form tests
├── LoginTest.java                   # Login functionality tests
├── DialogHandlingTest.java          # Alert/dialog handling tests
├── EmulationIPhone12ProTest.java    # Mobile emulation tests
├── PlaywrightTitleTest.java         # Basic navigation tests
└── pages/                           # Page Object Model classes
    ├── HomePage.java
    ├── FormsPage.java
    ├── PracticeFormPage.java
    └── ResultModal.java
```

## CI/CD Pipeline

The project uses GitHub Actions for continuous integration:

- Tests run automatically on every push
- Headless browser execution with Xvfb
- Maven dependency caching for faster builds
- Allure reports generated and published to GitHub Pages
- Test history maintained across builds

## Test Reports

Allure reports are automatically generated and deployed to GitHub Pages after each test run. The reports include:

- Test execution results
- Test history and trends
- Detailed test steps
- Screenshots and logs (when applicable)
- Execution time metrics

Access the latest reports: **https://elviralatypova.github.io/demoqa-playwright-java**

## Design Patterns

### Page Object Model
Each page is represented by a dedicated class that encapsulates:
- Page locators
- Page-specific actions
- Element interactions

### Base Test Class
`AbstractPlaywrightTest` provides:
- One-time Playwright and Browser initialization
- Per-test Browser Context and Page creation
- Automatic cleanup after tests
