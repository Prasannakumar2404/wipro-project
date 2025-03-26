package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class CouponPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By cartLink = By.linkText("Shopping cart");
    private By couponCodeField = By.name("discountcouponcode");
    private By applyCouponButton = By.name("applydiscountcouponcode");
    private By couponErrorMessage = By.cssSelector(".message-error"); // Keep this, but we'll handle it better
    private By cartItems = By.cssSelector(".cart-item-row");

    public CouponPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void navigateToCart() {
        WebElement cartLinkElement = wait.until(ExpectedConditions.elementToBeClickable(cartLink));
        cartLinkElement.click();
        wait.until(ExpectedConditions.titleContains("Shopping Cart"));
        wait.until(ExpectedConditions.presenceOfElementLocated(cartItems));
        wait.until(ExpectedConditions.visibilityOfElementLocated(couponCodeField));
        System.out.println("Navigated to cart page. Title: " + driver.getTitle());
    }

    public void enterCouponCode(String couponCode) {
        WebElement couponField = wait.until(ExpectedConditions.elementToBeClickable(couponCodeField));
        couponField.clear();
        couponField.sendKeys(couponCode);
        System.out.println("Entered coupon code: " + couponCode);
    }

    public void clickApplyCouponButton() {
        WebElement applyButton = wait.until(ExpectedConditions.elementToBeClickable(applyCouponButton));
        applyButton.click();
        System.out.println("Clicked Apply Coupon button");

        // Wait for any update on the page (e.g., error message or page refresh)
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            shortWait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(couponErrorMessage),
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".message")) // Fallback from friend's version
            ));
            System.out.println("Message (error or otherwise) appeared within 10 seconds.");
        } catch (Exception e) {
            System.out.println("No message appeared within 10 seconds, checking page source for debugging.");
            System.out.println("Page source snippet: " + driver.findElement(By.tagName("body")).getText().substring(0, 200));
        }
    }

    public String getCouponErrorMessage() {
        // Try multiple possible locators for the error message
        By[] possibleLocators = {
            By.cssSelector(".message-error"), // Your original
            By.cssSelector(".message"),       // Friend's version
            By.xpath("//div[contains(@class, 'message-error')]//li") // More specific if nested
        };

        for (By locator : possibleLocators) {
            try {
                WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                String message = errorElement.getText().trim();
                System.out.println("Retrieved coupon error message from " + locator + ": " + message);
                return message;
            } catch (Exception e) {
                System.out.println("No message found with locator " + locator + ": " + e.getMessage());
            }
        }
        
        throw new RuntimeException("No coupon error message found with any locator after 15 seconds.");
    }
}