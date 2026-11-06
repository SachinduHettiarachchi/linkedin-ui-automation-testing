# LinkedIn UI Automation Testing

A UI test automation project for LinkedIn login and search functionality, developed using **Java, Selenium WebDriver, TestNG, Maven, and the Page Object Model (POM)**.

## Project Overview

This project demonstrates how Selenium WebDriver can automate common user interactions on LinkedIn and verify expected application behaviour.

The automation covers:
- Successful login using valid credentials.
- Searching for a LinkedIn profile.
- Selecting a search result and verifying profile navigation.
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
| TC-02 | Search for a LinkedIn profile | Relevant search results are displayed |
| TC-03 | Select the first search result | The selected profile page opens |
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

The latest successful full test execution produced the following results:

```text
Tests run: 3
Failures: 0
Errors: 0
Skipped: 0

BUILD SUCCESS
```

All three automated test classes completed successfully.

##