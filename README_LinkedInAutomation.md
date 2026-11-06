# LinkedIn UI Automation Testing

A UI test automation project for LinkedIn login and search functionality, developed using **Java, Selenium WebDriver, TestNG, Maven, and the Page Object Model (POM)**.

## Project Overview

This project demonstrates how Selenium WebDriver can automate common user interactions on LinkedIn and verify expected application behaviour.

The automation covers:
- Successful login using valid credentials.
- Searching LinkedIn for a job.
- Opening the first job result and verifying its job page.
- Testing login behaviour with invalid credentials.
- Automatically capturing screenshots during test execution.

## Technologies Used

| Technology | Purpose |
|---|---|
| Java 21 | Programming language |
| Selenium WebDriver 4.27.0 | Browser automation |
| TestNG 7.10.2 | Test execution and assertions |
| Maven | Dependency and project management |
| ChromeDriver | Chrome browser automation |
| Page Object Model (POM) | Test code organisation |

## Project Structure

```text
LinkedInAutomation/
│
├── pom.xml
├── README.md
├── screenshots/
│
└── src/
    └── test/
        └── java/
            ├── base/
            │   └── BaseTest.java
            │
            ├── pages/
            │   ├── LoginPage.java
            │   └── SearchPage.java
            │
            ├── tests/
            │   ├── LoginTest.java
            │   ├── SearchTest.java
            │   └── InvalidLoginTest.java
            │
            └── utils/
                └── ScreenshotUtil.java
```

## Automated Test Scenarios

| Test Case | Description | Expected Result |
|---|---|---|
| TC-01 | Login with valid credentials | User successfully logs in |
| TC-02 | Search for a job | Relevant job results are displayed |
| TC-03 | Select the first job result | The selected job page opens |
| TC-04 | Login with invalid credentials | An invalid-credentials message is displayed |

## Prerequisites

Before running the project, make sure you have installed:

- Java Development Kit (JDK 17 or later)
- Apache Maven
- Google Chrome
- Visual Studio Code or another Java IDE

## Setup Instructions

### 1. Clone the repository

```bash
git clone https://github.com/SachinduHettiarachchi/linkedin-ui-automation-testing.git
```

### 2. Open the project directory

```bash
cd linkedin-ui-automation-testing
```

### 3. Configure LinkedIn credentials

The project uses environment variables to avoid storing login credentials directly in the source code.

In Windows PowerShell, configure your credentials:

```powershell
$env:LINKEDIN_USERNAME="your-email@example.com"
$env:LINKEDIN_PASSWORD="your-password"
```

Replace the example values with your own LinkedIn login credentials.

**Security note:** Never commit your actual credentials to GitHub.

### 4. Run the automated tests

Execute the following Maven command:

```bash
mvn clean test
```

Maven will download the required dependencies, compile the project, and execute the TestNG test suite.

## Test Results

Run the tests with valid `LINKEDIN_USERNAME` and `LINKEDIN_PASSWORD` environment variables. The tests use LinkedIn's live website, so results can vary with page changes and security checks such as CAPTCHA.

## Screenshot Evidence

Screenshots are saved in the `screenshots/` directory. Review them for personal information before sharing the repository.

## Design Approach – Page Object Model

- **BaseTest:** Starts and closes the browser.
- **LoginPage:** Handles LinkedIn login interactions.
- **SearchPage:** Searches for jobs and opens the first visible job result.
- **LoginTest, SearchTest, InvalidLoginTest:** Exercise the login and search flows.
- **ScreenshotUtil:** Captures screenshots during test execution.

## Challenges and Limitations

LinkedIn can change its page structure and may show CAPTCHA or other security checks. The tests do not bypass these checks, and a challenge may prevent a test from completing.

This project is intended for educational purposes and demonstrates UI test automation using Selenium WebDriver.
