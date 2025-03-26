package Pages;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CartPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(css = ".cart-item-row .product-name")
    private List<WebElement> cartProductNames;

    @FindBy(css = ".cart-total-right .product-price")
    private WebElement totalPrice;
    @FindBy(css = "#topcartlink a")
    private WebElement cartLink;

    public CartPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        PageFactory.initElements(driver, this);
    }

    public void navigateToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(cartLink)).click();
        System.out.println("Navigated to cart page");
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
            org.openqa.selenium.By.cssSelector(".cart-item-row .product-name")));
    }
    public boolean verifyCartContents(String[] expectedProducts) {
        try {
            List<WebElement> products = wait.until(ExpectedConditions.visibilityOfAllElements(cartProductNames));
            System.out.println("Number of items found in cart: " + products.size());
            if (products.size() != expectedProducts.length) {
                System.out.println("Cart item count mismatch. Expected: " + expectedProducts.length + 
                                 ", Found: " + products.size());
                for (WebElement product : products) {
                    System.out.println("Found cart item: " + product.getText());
                }
                return false;
            }
            for (String product : expectedProducts) {
                boolean found = false;
                for (WebElement item : products) {
                    String itemText = item.getText().toLowerCase();
                    System.out.println("Checking cart item: " + itemText);
                    if (itemText.contains(product.toLowerCase())) {
                        found = true;
                        System.out.println("Found product in cart: " + product);
                        break;
                    }
                }
                if (!found) {
                    System.out.println("Product not found in cart: " + product);
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println("Error verifying cart contents: " + e.getMessage());
            return false;
        }
    }

    public boolean verifyTotalPrice() {
        try {
            String priceText = wait.until(ExpectedConditions.visibilityOf(totalPrice)).getText();
            System.out.println("Raw total price text: " + priceText);
            double total = Double.parseDouble(priceText.replaceAll("[^0-9.]", ""));
            boolean isValid = total > 0;
            System.out.println("Cart total price: " + total + " (Valid: " + isValid + ")");
            return isValid;
        } catch (Exception e) {
            System.out.println("Error verifying total price - Element not found or invalid format: " + e.getMessage());
            return false;
        }
    }
}
