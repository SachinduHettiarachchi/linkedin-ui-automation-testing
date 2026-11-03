# LinkedIn UI Automation

A Java + Selenium WebDriver + TestNG Maven project for automating selected LinkedIn UI workflows.

## Requirements
- Java JDK 17 or later
- Apache Maven
- Google Chrome
- A compatible ChromeDriver (Selenium Manager may resolve the driver automatically, depending on your setup)

## Configure credentials

Do not hard-code your LinkedIn credentials in Java files or commit them to Git.

In Windows PowerShell, set environment variables for the current terminal session:

```powershell
$env:LINKEDIN_USERNAME='your LinkedIn email'
$env:LINKEDIN_PASSWORD='your LinkedIn password'
```

The tests read these values using `System.getenv()`.

## Run tests

Open PowerShell in the project root (the folder containing `pom.xml`).

Run the search test:

```powershell
mvn clean test -Dtest=SearchTest
```

Run an individual login test if its class is named `LoginTest`:

```powershell
mvn clean test -Dtest=LoginTest
```

Run all configured TestNG tests:

```powershell
mvn clean test
```

## Screenshots and reports

The project saves screenshots under the `screenshots/` directory when the test framework's screenshot capture logic runs. Check the console output for the exact screenshot filename.

The Maven Surefire test reports are generated under:

```text
target/surefire-reports/
```

## Important notes

- LinkedIn is a third-party production website. Its page structure, locators, search results and security controls may change.
- LinkedIn may show CAPTCHA/security verification during login attempts. Do not attempt to bypass CAPTCHA. A CAPTCHA interruption means an invalid-password result may be inconclusive.
- Keep personal information hidden in screenshots and screen recordings.
- Do not commit credentials, browser profiles, or private account data to the repository.
