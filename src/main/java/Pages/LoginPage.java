package Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
    private WebDriver driver;
    private WebDriverWait wait;
    
    @FindBy(id = "Email")
    private WebElement emailField;
    
    @FindBy(id = "Password")
    private WebElement passwordField;
    
    @FindBy(css = "input.button-1.login-button")
    private WebElement loginButton;
    
    @FindBy(xpath = "//a[@href='/logout']")
    private WebElement logoutLink;
    
    @FindBy(css = ".validation-summary-errors")
    private WebElement errorMessage;

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        this.setDriver(driver);
        this.wait = wait;
        PageFactory.initElements(driver, this);
    }

    public void enterEmail(String email) {
        WebElement emailInput = wait.until(ExpectedConditions.elementToBeClickable(emailField));
        emailInput.clear();
        emailInput.sendKeys(email);
        System.out.println("Entered email: " + email);
    }

    public void enterPassword(String password) {
        WebElement passwordInput = wait.until(ExpectedConditions.elementToBeClickable(passwordField));
        passwordInput.clear();
        passwordInput.sendKeys(password);
        System.out.println("Entered password: " + password);
    }

    public void clickLoginButton() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
        System.out.println("Clicked login button");
    }

    public void login(String email, String password) { 
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
    }

    public boolean isLoginSuccessful() {
        try {
            wait.until(ExpectedConditions.visibilityOf(logoutLink));
            System.out.println("Login successful - Logout link found");
            return true;
        } catch (Exception e) {
            System.out.println("Login failed - Logout link not found: " + e.getMessage());
            return false;
        }
    }

    public boolean isLoginFailed() {
        try {
            String errorText = wait.until(ExpectedConditions.visibilityOf(errorMessage)).getText();
            System.out.println("Login failed as expected - Error message: " + errorText);
            return true;
        } catch (Exception e) {
            System.out.println("No error message found: " + e.getMessage());
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