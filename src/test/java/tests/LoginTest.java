
package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.LoginPage;
import utils.ScreenshotUtil;

public class LoginTest extends BaseTest {

    @Test
    public void validLoginTest() {

        // Get credentials from environment variables
        String username = System.getenv("LINKEDIN_USERNAME");
        String password = System.getenv("LINKEDIN_PASSWORD");

        // Check credentials exist
        Assert.assertNotNull(
                username,
                "LinkedIn username is missing."
        );

        Assert.assertNotNull(
                password,
                "LinkedIn password is missing."
        );

        Assert.assertFalse(username.isBlank(), "Username is empty.");
        Assert.assertFalse(password.isEmpty(), "Password is empty.");

        System.out.println("Username loaded successfully.");
        System.out.println("Password loaded successfully.");
        System.out.println("Password length: " + password.length());

        // Create LoginPage object
        LoginPage loginPage = new LoginPage(driver, wait);

        // Perform login
        loginPage.login(username, password);

        // Wait for successful login
        try {

            wait.until(d -> {

                if (d.getCurrentUrl().contains("/feed")) {
                    return true;
                }

                for (WebElement element :
                        d.findElements(
                                By.cssSelector(
                                        "input[placeholder*='Search']"
                                )
                        )) {

                    if (element.isDisplayed()) {
                        return true;
                    }
                }

                return false;
            });

        } catch (Exception e) {

            // Capture screenshot if login fails
            ScreenshotUtil.captureScreenshot(
                    driver,
                    "login-failed"
            );

            System.out.println(
                    "Login verification failed."
            );

            System.out.println(
                    "Current URL: " + driver.getCurrentUrl()
            );

            throw e;
        }

        // Verify login
        Assert.assertTrue(
                driver.getCurrentUrl().contains("/feed")
                || driver.findElements(
                        By.cssSelector(
                                "input[placeholder*='Search']"
                        )
                ).stream().anyMatch(WebElement::isDisplayed),
                "Login was not successful."
        );

        System.out.println("LinkedIn login successful!");
    }
}
