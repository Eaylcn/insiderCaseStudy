package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class QACareersPage extends BasePage {

    // Locators
    private final By seeAllQaJobsButton = By.xpath("//a[contains(text(), 'See all QA jobs')]");

    public QACareersPage(WebDriver driver) {
        super(driver);
    }

    public void goTo() {
        driver.get("https://useinsider.com/careers/quality-assurance/");
    }

    public void clickSeeAllQaJobs() {
        WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(seeAllQaJobsButton));
        // Use JS Click to avoid banner/cookie overlay interceptions
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
    }
}
