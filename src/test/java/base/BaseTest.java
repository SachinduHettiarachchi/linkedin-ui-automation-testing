
package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

import utils.ScreenshotUtil;

public class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeMethod
    public void setUp() {

        driver = new ChromeDriver();

        driver.manage().window().maximize();

        // Increased wait time for LinkedIn page loading
        wait = new WebDriverWait(
            driver, Duration.ofSeconds(45)
        );

        System.out.println("Browser started");
    }


@AfterMethod
public void tearDown() {

    if (driver != null) {

        System.out.println(
            "Test completed. Keeping browser open for 30 seconds..."
        );

        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        driver.quit();

        System.out.println("Browser closed.");
    }
}


    protected void printPageDiagnostics(String testName) {

        System.out.println(
            "\n========== " + testName + " DIAGNOSTICS =========="
        );

        System.out.println("Current URL: " + driver.getCurrentUrl());
        System.out.println("Page title: " + driver.getTitle());

        System.out.println("Page text:");

        System.out.println(
            driver.findElement(
                org.openqa.selenium.By.tagName("body")
            ).getText()
        );

        System.out.println("Search inputs found: " +
            driver.findElements(
                org.openqa.selenium.By.cssSelector(
                    "input[data-testid='typeahead-input']"
                )
            ).size()
        );

        System.out.println("Alert elements found: " +
            driver.findElements(
                org.openqa.selenium.By.cssSelector("[role='alert']")
            ).size()
        );

        System.out.println(
            "===============================================\n"
        );
    }
}
