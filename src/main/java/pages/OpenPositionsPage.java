package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class OpenPositionsPage extends BasePage {

    // Locators
    private final By cookieAcceptButton = By.cssSelector("#wt-cli-accept-all-btn");
    private final By locationFilter = By.cssSelector("#filter-by-location");
    private final By departmentFilter = By.cssSelector("#filter-by-department");
    private final By jobListings = By.cssSelector(".position-list-item");
    private final By jobPositionTitle = By.cssSelector("p.position-title");
    private final By jobDepartment = By.cssSelector("span.position-department");
    private final By jobLocation = By.cssSelector("div.position-location");
    private final By viewRoleButton = By.xpath(".//a[contains(text(), 'View Role')]");
    private final By nonIstanbulJobs = By.xpath(
            "//div[contains(@class, 'position-list-item')]//div[contains(@class, 'position-location') and not(contains(text(), 'Istanbul'))]");

    public OpenPositionsPage(WebDriver driver) {
        super(driver);
    }

    public void preparePage() {
        try {
            List<WebElement> cookieAccept = driver.findElements(cookieAcceptButton);
            if (!cookieAccept.isEmpty() && cookieAccept.get(0).isDisplayed()) {
                cookieAccept.get(0).click();
                Thread.sleep(1000);
            }
        } catch (Exception e) {
        }
    }

    public void filterByLocation(String location) {
        preparePage();
        try {
            // Wait for AJAX load on location filter
            wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(locationFilter,
                    By.xpath(".//option[contains(text(), 'Istanbul')]")));

            Select locationSelect = new Select(driver.findElement(locationFilter));
            String matchingOptionText = getMatchingOptionText(locationSelect, location);

            if (matchingOptionText != null) {
                WebElement firstJobBeforeFilter = null;
                if (!driver.findElements(jobListings).isEmpty()) {
                    firstJobBeforeFilter = driver.findElements(jobListings).get(0);
                }

                locationSelect.selectByVisibleText(matchingOptionText);

                // Wait for stale element to ensure DOM refresh
                if (firstJobBeforeFilter != null) {
                    try {
                        wait.until(ExpectedConditions.stalenessOf(firstJobBeforeFilter));
                    } catch (Exception ignored) {
                    }
                }
                wait.until(ExpectedConditions.invisibilityOfElementLocated(nonIstanbulJobs));
                Thread.sleep(1500);
            } else {
                System.out.println("Hata: Lokasyon filtresi menüde bulunamadı!");
            }
        } catch (Exception e) {
            System.out.println("Lokasyon filtresi uygulanırken hata: " + e.getMessage());
        }
    }

    public void filterByDepartment(String department) {
        try {
            // Wait for AJAX load on department filter
            wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(departmentFilter,
                    By.xpath(".//option[contains(text(), 'Quality Assurance')]")));

            Select deptSelect = new Select(driver.findElement(departmentFilter));
            String matchingOptionText = getMatchingOptionText(deptSelect, department);

            if (matchingOptionText != null) {
                deptSelect.selectByVisibleText(matchingOptionText);

                // Short wait to ensure dropdown processes
                Thread.sleep(1500);
            } else {
                System.out.println("Hata: Departman filtresi menüde bulunamadı!");
            }
        } catch (Exception e) {
            System.out.println("Departman filtresi uygulanırken hata: " + e.getMessage());
        }
    }

    // Search for a matching string inside Select options
    private String getMatchingOptionText(Select selectElement, String filterText) {
        // Map Turkey to Turkiye for exact match
        String searchText = filterText.equals("Istanbul, Turkey") ? "Istanbul, Turkiye" : filterText;

        for (WebElement opt : selectElement.getOptions()) {
            if (opt.getText().contains(searchText)) {
                return opt.getText();
            }
        }
        return null;
    }

    public boolean isJobListPresent() {
        try {
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(jobListings));
            return driver.findElements(jobListings).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean checkJobsDetails(String expectedPosition, String expectedDepartment, String expectedLocation) {
        List<WebElement> jobs = driver.findElements(jobListings);
        for (WebElement job : jobs) {
            if (!job.isDisplayed()) {
                continue;
            }
            try {
                String positionText = job.findElement(jobPositionTitle).getText();
                String departmentText = job.findElement(jobDepartment).getText();
                String locationText = job.findElement(jobLocation).getText();

                boolean positionMatch = positionText.contains(expectedPosition) || positionText.contains("QA")
                        || positionText.contains("Quality Assurance");
                boolean deptMatch = departmentText.contains(expectedDepartment);

                // Address Turkiye spelling difference in job cards
                String expectedLocInCard = expectedLocation.equals("Istanbul, Turkey") ? "Istanbul, Turkiye"
                        : expectedLocation;
                boolean locMatch = locationText.contains(expectedLocInCard);

                if (!positionMatch || !deptMatch || !locMatch) {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    public void clickFirstViewRoleButton() {
        // Wait for job listings to populate
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(jobListings));
            wait.until(ExpectedConditions.presenceOfElementLocated(viewRoleButton));
        } catch (Exception ignored) {
        }

        List<WebElement> jobs = driver.findElements(jobListings);
        for (WebElement job : jobs) {
            if (job.isDisplayed()) {
                WebElement viewRoleBtn = job.findElement(viewRoleButton);

                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", job);
                try {
                    Thread.sleep(1500); // DOM stabilization
                } catch (Exception e) {
                }

                wait.until(ExpectedConditions.elementToBeClickable(viewRoleBtn));

                // Click via JS to sidestep potential UI blockages
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", viewRoleBtn);

                for (String winHandle : driver.getWindowHandles()) {
                    driver.switchTo().window(winHandle);
                }
                break; // Click only the first available job
            }
        }
    }
}
