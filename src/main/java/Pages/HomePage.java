package Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {
    private WebDriver driver;
    private WebDriverWait wait;
    
    @FindBy(xpath = "//a[text()='Register']")
    private WebElement registerLink;
    
    @FindBy(xpath = "//a[text()='Log in']")
    private WebElement loginLink;
    
    @FindBy(xpath = "//a[text()='Log out']")
    private WebElement logoutLink;

    public HomePage(WebDriver driver, WebDriverWait wait) {
        this.setDriver(driver);
        this.wait = wait;
        PageFactory.initElements(driver, this);
    }

    public void navigateToRegistration() {
        wait.until(ExpectedConditions.elementToBeClickable(registerLink)).click();
    }

    public void navigateToLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(loginLink)).click();
    }

    public void logout() {
        wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();
    }

    public boolean isLoggedIn() {
        try {
            wait.until(ExpectedConditions.visibilityOf(logoutLink));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}
}