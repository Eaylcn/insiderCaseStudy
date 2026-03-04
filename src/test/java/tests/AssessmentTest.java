package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.OpenPositionsPage;
import pages.QACareersPage;

import java.util.Set;

public class AssessmentTest extends BaseTest {

    @Test
    public void testQaAssessment() {
        // Navigate to Home page and verify load
        HomePage homePage = new HomePage(driver);
        homePage.goTo();
        Assert.assertTrue(homePage.isLoaded(), "Home page is not loaded successfully or main blocks are missing.");

        // Navigate to QA Careers page and click See All QA Jobs
        QACareersPage qaCareersPage = new QACareersPage(driver);
        qaCareersPage.goTo();
        qaCareersPage.clickSeeAllQaJobs();

        OpenPositionsPage openPositionsPage = new OpenPositionsPage(driver);

        // Wait for page animations and elements to settle
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        openPositionsPage.filterByLocation("Istanbul, Turkey");
        openPositionsPage.filterByDepartment("Quality Assurance");

        // Wait for job list DOM updates from AJAX
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(openPositionsPage.isJobListPresent(), "Job list is empty after applying filters.");

        // Verify all listed jobs match the filter criteria
        boolean areJobsValid = openPositionsPage.checkJobsDetails(
                "Quality Assurance",
                "Quality Assurance",
                "Istanbul, Turkey");
        Assert.assertTrue(areJobsValid, "Some job entries do not match the expected filter criteria.");

        // Click View Role and switch to Leverage Application window
        String originalWindow = driver.getWindowHandle();
        openPositionsPage.clickFirstViewRoleButton();

        // Switch to the newly opened window
        Set<String> allWindows = driver.getWindowHandles();
        for (String windowHandle : allWindows) {
            if (!windowHandle.equals(originalWindow)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }

        // Wait for Lever form page to load
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("jobs.lever.co"),
                "Did not redirect to Lever Application form page. Current URL: " + currentUrl);
    }
}
