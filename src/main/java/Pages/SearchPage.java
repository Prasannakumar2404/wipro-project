package Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;

public class SearchPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(id = "small-searchterms")
    private WebElement searchField;

    @FindBy(css = "input.button-1.search-box-button")
    private WebElement searchButton;

    // Removed hardcoded product links since we'll find them dynamically
    @FindBy(css = "input.button-1.add-to-cart-button") 
    private WebElement addToCartButton;

    @FindBy(css = ".bar-notification.success")
    private WebElement successMessage;

    @FindBy(css = ".cart-qty")
    private WebElement cartQuantity;

    public SearchPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        PageFactory.initElements(driver, this);
    }

    public void enterSearchTerm(String searchTerm) {
        wait.until(ExpectedConditions.elementToBeClickable(searchField)).sendKeys(searchTerm);
        System.out.println("Entered search term: " + searchTerm);
    }

    public void clickSearchButton() {
        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
        System.out.println("Clicked search button");
    }

    public void selectProduct(String productName) {
        // Dynamically locate the product link based on the product name
        String xpath = "//div[contains(@class, 'product-item')]//a[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" 
                     + productName.toLowerCase() + "')]";
        WebElement productLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        
        try {
            productLink.click();
            System.out.println("Selected product: " + productName);
        } catch (Exception e) {
            System.out.println("Failed to find product '" + productName + "': " + e.getMessage());
            throw new IllegalArgumentException("Product not found on page: " + productName, e);
        }
    }

    public void clickAddToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton)).click();
        System.out.println("Clicked Add to Cart button");
    }

    public boolean isProductAddedToCart() {
        try {
            String message = wait.until(ExpectedConditions.visibilityOf(successMessage)).getText();
            String quantity = wait.until(ExpectedConditions.visibilityOf(cartQuantity)).getText();
            boolean isAdded = message.contains("The product has been added to your shopping cart") 
                            && !quantity.equals("(0)");
            System.out.println("Cart verification - Message: " + message + ", Quantity: " + quantity);
            return isAdded;
        } catch (Exception e) {
            System.out.println("Failed to verify cart: " + e.getMessage());
            return false;
        }
    }
}