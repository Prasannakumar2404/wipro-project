package Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.Random;

public class RegistrationPage {
    private WebDriver driver;
    private WebDriverWait wait;
    
    @FindBy(id = "gender-male")
    private WebElement genderMale;
    
    @FindBy(id = "FirstName")
    private WebElement firstName;
    
    @FindBy(id = "LastName")
    private WebElement lastName;
    
    @FindBy(id = "Email")
    private WebElement email;
    
    @FindBy(id = "Password")  
    private WebElement password;
    
    @FindBy(id = "ConfirmPassword")
    private WebElement confirmPassword;
    
    @FindBy(id = "register-button")
    private WebElement registerButton;
    
    @FindBy(css = ".result")
    private WebElement successMessage;

    public RegistrationPage(WebDriver driver, WebDriverWait wait) {
        this.setDriver(driver);
        this.wait = wait;
        PageFactory.initElements(driver, this);
    }

    public void register(String passwordValue, String[] credentials) {  
        String randomString = generateRandomString(8);
        String regEmail = "test_" + randomString + "@example.com";
        
        wait.until(ExpectedConditions.elementToBeClickable(genderMale)).click();
        firstName.sendKeys("Test");
        lastName.sendKeys("User");
        email.sendKeys(regEmail);
        password.sendKeys(passwordValue);  
        confirmPassword.sendKeys(passwordValue);
        registerButton.click();
        
        credentials[0] = regEmail;
        credentials[1] = passwordValue;
    }

    public boolean isRegistrationSuccessful() {
        String message = wait.until(ExpectedConditions.visibilityOf(successMessage)).getText();
        return message.contains("Your registration completed");
    }

    private String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }

	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}
}