
package pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SearchPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Main LinkedIn search bar
    private By searchBox =
        By.cssSelector("input[data-testid='typeahead-input']");

    // Existing job titles locator
    private By jobTitles =
        By.xpath(
            "//span[starts-with(" +
            "normalize-space(text()), 'Selected,')]"
        );

    // Person and company profile links
    private By searchResults = By.xpath(
        "//a[contains(@href, '/in/') or " +
        "contains(@href, '/company/')]"
    );

    public SearchPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    // Search using LinkedIn's main search bar
    public void searchFor(String keyword) {

        System.out.println("Waiting for main search bar...");

        WebElement searchInput = wait.until(
            ExpectedConditions.elementToBeClickable(searchBox)
        );

        searchInput.click();

        // Clear existing search text
        searchInput.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        searchInput.sendKeys(Keys.BACK_SPACE);

        System.out.println(
            "Entering search keyword character by character..."
        );

        StringBuilder enteredKeyword = new StringBuilder();

        for (char character : keyword.toCharArray()) {

            enteredKeyword.append(character);

            String expectedText = enteredKeyword.toString();

            WebElement currentInput = wait.until(
                ExpectedConditions.elementToBeClickable(searchBox)
            );

            currentInput.sendKeys(String.valueOf(character));

            // Verify entered text
            wait.until(d -> {

                List<WebElement> inputs =
                    d.findElements(searchBox);

                for (WebElement input : inputs) {

                    try {
                        if (input.isDisplayed()) {

                            String actualText =
                                input.getDomProperty("value");

                            return expectedText.equals(actualText);
                        }
                    } catch (StaleElementReferenceException e) {
                        return false;
                    }
                }

                return false;
            });

            System.out.println("Entered: " + expectedText);
        }

        System.out.println(
            "Complete keyword entered: " + keyword
        );

        // Submit search
        WebElement finalInput = wait.until(
            ExpectedConditions.elementToBeClickable(searchBox)
        );

        finalInput.sendKeys(Keys.ENTER);

        // Wait for general search results
        wait.until(
            ExpectedConditions.urlContains("/search/results/all")
        );

        System.out.println("General search results loaded.");
    }

    // Inspect Jobs elements on the general search results page
    public void inspectJobsElements() {

        By candidates = By.xpath(
            "//a[normalize-space(.)='Jobs']" +
            " | //button[normalize-space(.)='Jobs']" +
            " | //*[@role='tab' and normalize-space(.)='Jobs']" +
            " | //*[@role='button' and normalize-space(.)='Jobs']"
        );

        List<WebElement> elements =
            driver.findElements(candidates);

        System.out.println(
            "===== JOBS ELEMENT INSPECTION ====="
        );

        System.out.println(
            "Current URL: " + driver.getCurrentUrl()
        );

        System.out.println(
            "Page title: " + driver.getTitle()
        );

        System.out.println(
            "Elements found: " + elements.size()
        );

        for (WebElement element : elements) {

            System.out.println("Tag: " + element.getTagName());
            System.out.println("Text: " + element.getText());
            System.out.println("Href: " + element.getAttribute("href"));
            System.out.println("Role: " + element.getAttribute("role"));
            System.out.println(
                "Aria label: " + element.getAttribute("aria-label")
            );
            System.out.println("HTML: " + element.getAttribute("outerHTML"));
            System.out.println("--------------------------------");
        }
    }

    // Wait for actual job titles to appear
    public void waitForJobListings() {

        System.out.println("Waiting for job vacancies...");

        wait.until(d -> {

            List<WebElement> titles =
                d.findElements(jobTitles);

            for (WebElement title : titles) {

                if (title.isDisplayed()
                    && !title.getText().trim().isEmpty()) {

                    return true;
                }
            }

            return false;
        });

        System.out.println("Job vacancies loaded.");
    }

    // Count visible job listings
    public int getJobListingCount() {

        List<WebElement> titles =
            driver.findElements(jobTitles);

        int count = 0;

        for (WebElement title : titles) {

            if (title.isDisplayed()
                && !title.getText().trim().isEmpty()) {

                count++;
            }
        }

        return count;
    }

    // Print available job titles
    public void printJobTitles() {

        List<WebElement> titles =
            driver.findElements(jobTitles);

        System.out.println("Available job vacancies:");

        for (WebElement title : titles) {

            if (title.isDisplayed()) {

                String jobTitle = title.getText()
                    .replace("Selected,", "")
                    .trim();

                System.out.println("- " + jobTitle);
            }
        }
    }

    // Inspect general search results page
    public void inspectSearchResultsPage() {

        System.out.println("===== SEARCH RESULTS PAGE =====");

        System.out.println("Current URL: " + driver.getCurrentUrl());

        System.out.println("Page title: " + driver.getTitle());

        System.out.println("Visible page text:");

        System.out.println(
            driver.findElement(By.tagName("body")).getText()
        );

        System.out.println("===============================");
    }

    // Inspect first person/company search results
    public void inspectFirstSearchResults() {

        System.out.println("===== FIRST SEARCH RESULTS =====");

        List<WebElement> results =
            driver.findElements(searchResults);

        int count = 0;

        for (WebElement result : results) {

            try {

                if (result.isDisplayed()) {

                    String text = result.getText().trim();
                    String href = result.getAttribute("href");

                    if (!text.isEmpty() && href != null) {

                        System.out.println("Result: " + (++count));
                        System.out.println("Text: " + text);
                        System.out.println("URL: " + href);
                        System.out.println("----------------------------");

                        if (count == 10) {
                            break;
                        }
                    }
                }

            } catch (StaleElementReferenceException e) {
                // Ignore elements replaced by LinkedIn.
            }
        }
    }

    // Click the first visible person/company result
    public String clickFirstSearchResult() {

        System.out.println("Waiting for search results...");

        wait.until(d -> {

            List<WebElement> results =
                d.findElements(searchResults);

            for (WebElement result : results) {

                try {

                    if (result.isDisplayed()
                        && !result.getText().trim().isEmpty()
                        && result.getAttribute("href") != null) {

                        return true;
                    }

                } catch (StaleElementReferenceException e) {
                    // Retry with fresh elements.
                }
            }

            return false;
        });

        // Get fresh search result elements
        List<WebElement> results =
            driver.findElements(searchResults);

        for (WebElement result : results) {

            try {

                if (result.isDisplayed()
                    && !result.getText().trim().isEmpty()) {

                    String resultText =
                        result.getText().trim();

                    String expectedURL =
                        result.getAttribute("href");

                    if (expectedURL == null) {
                        continue;
                    }

                    System.out.println("First result selected.");
                    System.out.println("Result text: " + resultText);
                    System.out.println("Expected URL: " + expectedURL);

                    wait.until(
                        ExpectedConditions.elementToBeClickable(result)
                    );

                    result.click();

                    return expectedURL;
                }

            } catch (StaleElementReferenceException e) {
                // Try another fresh element.
            }
        }

        throw new RuntimeException(
            "Could not find a clickable person or company result."
        );
    }

    // Verify that the selected result page opened
    public boolean verifyResultPageOpened(String expectedURL) {

        System.out.println("Waiting for destination page...");

        wait.until(d -> {

            String currentURL = d.getCurrentUrl();

            return currentURL.contains("/in/")
                || currentURL.contains("/company/");
        });

        String actualURL = driver.getCurrentUrl();

        System.out.println("Expected URL: " + expectedURL);
        System.out.println("Actual URL: " + actualURL);

        return actualURL.contains("/in/")
            || actualURL.contains("/company/");
    }

    public String getCurrentURL() {
        return driver.getCurrentUrl();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }
}
