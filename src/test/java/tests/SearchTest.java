
package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.LoginPage;
import pages.SearchPage;
import utils.ScreenshotUtil;

public class SearchTest extends BaseTest {

    @Test
    public void searchTest() {

        // Read credentials from environment variables
        String username =
            System.getenv("LINKEDIN_USERNAME");

        String password =
            System.getenv("LINKEDIN_PASSWORD");

        Assert.assertNotNull(
            username,
            "Username is missing."
        );

        Assert.assertNotNull(
            password,
            "Password is missing."
        );

        // STEP 1: Login
        System.out.println("STEP 1: Starting login.");

        LoginPage loginPage =
            new LoginPage(driver, wait);

        loginPage.login(username, password);

        System.out.println("STEP 2: Login successful.");

        // STEP 2: Create SearchPage object
        SearchPage searchPage =
            new SearchPage(driver, wait);

        // STEP 3: Search
        System.out.println(
            "STEP 3: Searching for Software Engineer."
        );

        searchPage.searchFor("Software Engineer");

        // STEP 4: Inspect search results
        System.out.println(
            "STEP 4: Inspecting job search results."
        );

        searchPage.inspectFirstSearchResults();

        // STEP 5: Click the first result
        System.out.println(
            "STEP 5: Clicking the first job result."
        );

        String expectedURL =
            searchPage.clickFirstSearchResult();

        // STEP 6: Verify destination page
        System.out.println(
            "STEP 6: Verifying destination page."
        );

        boolean resultOpened =
            searchPage.verifyResultPageOpened(expectedURL);

        Assert.assertTrue(
            resultOpened,
            "The first job result page did not open."
        );

        System.out.println(
            "SUCCESS: Search result page opened."
        );

        System.out.println(
            "Final URL: " + searchPage.getCurrentURL()
        );

        System.out.println(
            "Page title: " + searchPage.getPageTitle()
        );

        // STEP 7: Capture screenshot
        System.out.println(
            "STEP 7: Capturing screenshot."
        );

        ScreenshotUtil.captureScreenshot(
            driver,
            "SearchTest-PASSED"
        );
    }
}
