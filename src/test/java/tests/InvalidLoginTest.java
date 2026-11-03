
package tests;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.LoginPage;
import utils.ScreenshotUtil;

public class InvalidLoginTest extends BaseTest {

    @Test
    public void invalidLoginTest() {

        String username = System.getenv("LINKEDIN_USERNAME");

        Assert.assertNotNull(
            username,
            "LinkedIn username is missing."
        );

        String invalidPassword = "WrongPassword123!";

        System.out.println("STEP 1: Opening LinkedIn login.");

        LoginPage loginPage = new LoginPage(driver, wait);
        loginPage.openLoginPage();

        System.out.println("STEP 2: Entering invalid credentials.");

        loginPage.enterCredentials(username, invalidPassword);
        loginPage.clickSignIn();

        System.out.println("STEP 3: Waiting for LinkedIn response.");

        boolean responseDetected = wait.until(d -> {

            String url = d.getCurrentUrl();

            String bodyText = d
                .findElement(By.tagName("body"))
                .getText()
                .toLowerCase();

            boolean securityChallenge =
                url.contains("/checkpoint/challenge")
                || bodyText.contains("i'm not a robot")
                || bodyText.contains("security verification")
                || bodyText.contains("nocaptcha");

            boolean invalidCredentials =
                bodyText.contains("wrong email or password.");

            return securityChallenge || invalidCredentials;
        });

        Assert.assertTrue(
            responseDetected,
            "No recognisable login response."
        );

        String currentUrl = driver.getCurrentUrl();

        String pageText = driver
            .findElement(By.tagName("body"))
            .getText();

        String lowerPageText = pageText.toLowerCase();

        System.out.println("Current URL: " + currentUrl);
        System.out.println("Page title: " + driver.getTitle());

        // CASE 1: CAPTCHA / security challenge detected
        if (currentUrl.contains("/checkpoint/challenge")
                || lowerPageText.contains("i'm not a robot")
                || lowerPageText.contains("security verification")
                || lowerPageText.contains("nocaptcha")) {

            System.out.println("CAPTCHA DETECTED!");
            System.out.println("Please complete the CAPTCHA manually.");
            System.out.println(
                "Waiting up to 2 minutes for LinkedIn's response..."
            );

            try {

                // Wait for an actual response from LinkedIn.
                new WebDriverWait(driver, Duration.ofMinutes(2))
                    .until(d -> {

                        String text = d
                            .findElement(By.tagName("body"))
                            .getText()
                            .toLowerCase();

                        return text.contains("wrong email or password.")
                            || text.contains("nocaptcha")
                            || text.contains("response code");
                    });

                String finalText = driver
                    .findElement(By.tagName("body"))
                    .getText()
                    .toLowerCase();

                // Check whether invalid credentials were rejected.
                if (finalText.contains("wrong email or password.")) {

                    System.out.println(
                        "RESULT: Invalid credentials message detected."
                    );

                    ScreenshotUtil.captureScreenshot(
                        driver,
                        "InvalidLoginTest-PASSED"
                    );

                } else {

                    System.out.println(
                        "RESULT: LinkedIn CAPTCHA verification error detected."
                    );

                    ScreenshotUtil.captureScreenshot(
                        driver,
                        "InvalidLoginTest-CAPTCHA"
                    );

                    throw new SkipException(
                        "Test skipped: LinkedIn CAPTCHA response "
                        + "was missing or invalid. Invalid credentials "
                        + "could not be verified."
                    );
                }

            } catch (TimeoutException e) {

                System.out.println(
                    "No recognisable response appeared within 2 minutes."
                );

                ScreenshotUtil.captureScreenshot(
                    driver,
                    "InvalidLoginTest-CAPTCHA"
                );

                throw new SkipException(
                    "Test skipped: LinkedIn did not provide a "
                    + "recognisable response after CAPTCHA verification."
                );
            }
        }

        // CASE 2: Invalid credentials message detected immediately
        else if (lowerPageText.contains("wrong email or password.")) {

            System.out.println(
                "RESULT: Invalid credentials message detected."
            );

            ScreenshotUtil.captureScreenshot(
                driver,
                "InvalidLoginTest-PASSED"
            );
        }

        // CASE 3: Unexpected response
        else {

            ScreenshotUtil.captureScreenshot(
                driver,
                "InvalidLoginTest-UNEXPECTED"
            );

            Assert.fail(
                "LinkedIn returned an unexpected login response."
            );
        }

        System.out.println(
            "STEP 4: Login response inspection completed."
        );
    }
}
