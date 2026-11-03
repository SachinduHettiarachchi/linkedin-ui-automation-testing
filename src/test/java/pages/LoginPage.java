
package pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // LinkedIn username field
    private By usernameField = By.cssSelector(
        "input[autocomplete='username webauthn']"
    );

    // LinkedIn password field
    private By passwordField = By.cssSelector(
        "input[autocomplete='current-password']"
    );

    // Actual LinkedIn Sign in button
    // It is type="button", not type="submit".
    private By signInButton = By.xpath(
        "//button[@type='button' and .//span[normalize-space()='Sign in']]"
    );

    // Element available after successful login
    private By searchBox = By.cssSelector(
        "input[data-testid='typeahead-input']"
    );

    // Constructor - KEEP THIS TWO-ARGUMENT VERSION
    public LoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    // Open LinkedIn login page
    public void openLoginPage() {

        System.out.println("Opening LinkedIn login page...");

        driver.get("https://www.linkedin.com/login");

        wait.until(driver ->
            driver.findElements(usernameField)
                .stream()
                .anyMatch(WebElement::isDisplayed)
        );

        System.out.println("Username field detected.");
        System.out.println("LinkedIn login page opened.");
    }

    // Enter username and password
    public void enterCredentials(String username, String password) {

        // Find the visible username field
        WebElement usernameInput = wait.until(driver ->
            driver.findElements(usernameField)
                .stream()
                .filter(WebElement::isDisplayed)
                .findFirst()
                .orElse(null)
        );

        usernameInput.clear();
        usernameInput.sendKeys(username);

        System.out.println("Username entered.");

        // Find the visible password field
        WebElement passwordInput = wait.until(driver ->
            driver.findElements(passwordField)
                .stream()
                .filter(WebElement::isDisplayed)
                .findFirst()
                .orElse(null)
        );

        passwordInput.clear();
        passwordInput.sendKeys(password);

        System.out.println("Password entered.");
        System.out.println("Login credentials entered.");
    }

    // Click the actual visible Sign in button
    public void clickSignIn() {

        System.out.println("Waiting for Sign in button...");

        WebElement button = wait.until(driver -> {

            List<WebElement> buttons =
                driver.findElements(signInButton);

            return buttons.stream()
                .filter(WebElement::isDisplayed)
                .filter(WebElement::isEnabled)
                .findFirst()
                .orElse(null);
        });

        button.click();

        System.out.println("Sign in button clicked.");
    }

    // Wait for successful login
    
public void waitForSuccessfulLogin() {

    System.out.println("Waiting for successful login...");

    WebDriverWait loginWait = new WebDriverWait(
        driver,
        Duration.ofMinutes(2)
    );

    loginWait.ignoring(
        org.openqa.selenium.StaleElementReferenceException.class
    );

    loginWait.until(driver -> {

        List<WebElement> searchBoxes =
            driver.findElements(searchBox);

        for (WebElement searchInput : searchBoxes) {

            try {

                if (searchInput.isDisplayed()) {
                    return true;
                }

            } catch (
                org.openqa.selenium.StaleElementReferenceException e
            ) {
                // LinkedIn replaced the element.
                // Retry using a fresh element.
            }
        }

        return false;
    });

    System.out.println("Login successful.");
    System.out.println("LinkedIn search box detected.");
}


    // Complete login process
    public void login(String username, String password) {

        openLoginPage();

        enterCredentials(username, password);

        clickSignIn();

        waitForSuccessfulLogin();
    }
}
