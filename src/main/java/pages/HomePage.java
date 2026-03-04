package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage {

    // Optimized CSS Selectors (Explicitly defined instead of using @FindBy proxy)
    private final By navigationBar = By.cssSelector("nav");
    private final By footer = By.cssSelector("footer");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void goTo() {
        driver.get("https://insiderone.com/");
    }

    public boolean isLoaded() {
        return isElementDisplayed(navigationBar) && isElementDisplayed(footer);
    }
}
