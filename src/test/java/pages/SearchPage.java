
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

    // Job cards in LinkedIn's unified search page. The first card may link to
    // the jobs search-results route with a currentJobId, rather than /jobs/view/.
    private By jobResults = By.cssSelector(
        "a[href*='/jobs/view/'], " +
        "a[href*='/jobs/search-results/'][href*='currentJobId=']"
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

    // Inspect visible job results in page order
    public void inspectFirstSearchResults() {

        System.out.println("===== FIRST JOB SEARCH RESULTS =====");

        List<WebElement> results = driver.findElements(jobResults);

        int count = 0;

        for (WebElement result : results) {

            try {

                if (result.isDisplayed()
                    && !result.getText().trim().isEmpty()) {
                    System.out.println("Result: " + (++count));
                    System.out.println("Text: " + result.getText().trim());
                    System.out.println("URL: " + result.getAttribute("href"));
                    System.out.println("----------------------------");
                }

                if (count == 10) {
                    break;
                }

            } catch (StaleElementReferenceException e) {
                // Ignore results replaced by LinkedIn while inspecting.
            }
        }
    }

    // Click the first visible job result
    public String clickFirstSearchResult() {

        System.out.println("Waiting for job search results...");

        WebElement firstResult = wait.until(d -> {
            for (WebElement result : d.findElements(jobResults)) {
                try {
                    if (result.isDisplayed()
                        && !result.getText().trim().isEmpty()
                        && result.getAttribute("href") != null) {
                        return result;
                    }
                } catch (StaleElementReferenceException e) {
                    // Retry with fresh results during the next poll.
                }
            }
            return null;
        });

        String expectedURL = firstResult.getAttribute("href");
        System.out.println("Clicking first job result.");
        System.out.println("Result text: " + firstResult.getText().trim());
        System.out.println("Expected URL: " + expectedURL);

        wait.until(ExpectedConditions.elementToBeClickable(firstResult));
        firstResult.click();
        return expectedURL;
    }

    // Verify that the selected result page opened
    public boolean verifyResultPageOpened(String expectedURL) {

        System.out.println("Waiting for destination page...");

        String expectedJobId = extractJobId(expectedURL);
        requireJobId(expectedJobId, expectedURL);

        wait.until(d -> {

            String currentURL = d.getCurrentUrl();

            return currentURL.contains("currentJobId=" + expectedJobId)
                || currentURL.contains("/jobs/view/" + expectedJobId);
        });

        String actualURL = driver.getCurrentUrl();

        System.out.println("Expected URL: " + expectedURL);
        System.out.println("Actual URL: " + actualURL);

        return actualURL.contains("currentJobId=" + expectedJobId)
            || actualURL.contains("/jobs/view/" + expectedJobId);
    }

    private String extractJobId(String url) {
        String marker = "currentJobId=";
        int idStart = url.indexOf(marker);

        if (idStart >= 0) {
            idStart += marker.length();
            int idEnd = url.indexOf('&', idStart);
            return idEnd >= 0
                ? url.substring(idStart, idEnd)
                : url.substring(idStart);
        }

        marker = "/jobs/view/";
        int pathStart = url.indexOf(marker);
        if (pathStart >= 0) {
            pathStart += marker.length();
            int pathEnd = url.indexOf('/', pathStart);
            int queryStart = url.indexOf('?', pathStart);
            int idEnd = pathEnd >= 0 && (queryStart < 0 || pathEnd < queryStart)
                ? pathEnd
                : queryStart;
            return idEnd >= 0
                ? url.substring(pathStart, idEnd)
                : url.substring(pathStart);
        }

        return null;
    }

    private void requireJobId(String jobId, String url) {
        if (jobId == null || jobId.isBlank()) {
            throw new IllegalArgumentException(
                "Could not identify the job in result URL: " + url
            );
        }
    }

    public String getCurrentURL() {
        return driver.getCurrentUrl();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }
}
