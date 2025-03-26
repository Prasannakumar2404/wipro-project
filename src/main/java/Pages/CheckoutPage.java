package Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.By;
import java.util.List;

public class CheckoutPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(id = "termsofservice")
    private WebElement termsCheckbox;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    @FindBy(css = "input.button-1.checkout-as-guest-button")
    private WebElement checkoutAsGuestButton;

    @FindBy(id = "BillingNewAddress_FirstName")
    private WebElement firstNameField;

    @FindBy(id = "BillingNewAddress_LastName")
    private WebElement lastNameField;

    @FindBy(id = "BillingNewAddress_Email")
    private WebElement emailField;

    @FindBy(id = "BillingNewAddress_Company")
    private WebElement companyField;

    @FindBy(id = "BillingNewAddress_CountryId")
    private WebElement countryDropdown;

    @FindBy(id = "BillingNewAddress_StateProvinceId")
    private WebElement stateProvinceDropdown;

    @FindBy(id = "BillingNewAddress_City")
    private WebElement cityField;

    @FindBy(id = "BillingNewAddress_Address1")
    private WebElement address1Field;

    @FindBy(id = "BillingNewAddress_Address2")
    private WebElement address2Field;

    @FindBy(id = "BillingNewAddress_ZipPostalCode")
    private WebElement zipField;

    @FindBy(id = "BillingNewAddress_PhoneNumber")
    private WebElement phoneNumberField;

    @FindBy(id = "BillingNewAddress_FaxNumber")
    private WebElement faxNumberField;

    @FindBy(css = "input.button-1.new-address-next-step-button")
    private WebElement billingContinueButton;

    @FindBy(id = "PickUpInStore")
    private WebElement inStorePickupCheckbox;

    @FindBy(xpath = "//div[@id='shipping-buttons-container']//input[@value='Continue']")
    private WebElement shippingContinueButton;

    @FindBy(id = "paymentmethod_0")
    private WebElement cashOnDeliveryRadio;

    @FindBy(xpath = "//div[@id='payment-method-buttons-container']//input[@type='button' and @value='Continue']")
    private WebElement paymentContinueButton;

    @FindBy(xpath = "//div[@id='confirm-order-buttons-container']//input[@value='Confirm']")
    private WebElement confirmOrderButton;

    @FindBy(css = ".order-completed .title")
    private WebElement orderConfirmationTitle;

    @FindBy(css = ".validation-summary-errors")
    private WebElement errorSummary;

    @FindBy(css = ".field-validation-error")
    private WebElement fieldError;

    public CheckoutPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        PageFactory.initElements(driver, this);
    }

    public void acceptTermsAndConditions() {
        wait.until(ExpectedConditions.elementToBeClickable(termsCheckbox)).click();
        System.out.println("Accepted terms and conditions");
    }

    public void clickCheckoutButton() {
        wait.until(ExpectedConditions.elementToBeClickable(checkoutButton)).click();
        System.out.println("Clicked checkout button");
    }

    public void clickCheckoutAsGuest() {
        wait.until(ExpectedConditions.elementToBeClickable(checkoutAsGuestButton)).click();
        System.out.println("Clicked Checkout as Guest button");
    }

    public void enterInvalidBillingDetails(String firstName, String lastName, String email, String company, 
                                           String countryId, String stateId, String city, String address1, 
                                           String address2, String zip, String phone, String fax) {
        wait.until(ExpectedConditions.visibilityOf(firstNameField));
        
        firstNameField.clear();
        firstNameField.sendKeys(firstName);
        lastNameField.clear();
        lastNameField.sendKeys(lastName);
        emailField.clear();
        emailField.sendKeys(email);
        companyField.clear();
        companyField.sendKeys(company);
        
        Select countrySelect = new Select(countryDropdown);
        countrySelect.selectByValue(countryId);
        System.out.println("Selected country with ID: " + countryId);
        
        wait.until(ExpectedConditions.elementToBeClickable(stateProvinceDropdown));
        Select stateSelect = new Select(stateProvinceDropdown);
        
        wait.until(driver -> {
            List<WebElement> options = stateSelect.getOptions();
            return options.size() > 1;
        });
        
        List<WebElement> stateOptions = stateSelect.getOptions();
        System.out.println("Available state options after selecting country " + countryId + ": " + stateOptions.size());
        for (int i = 0; i < stateOptions.size(); i++) {
            System.out.println("State Option " + i + ": value=" + stateOptions.get(i).getAttribute("value") + 
                               ", text=" + stateOptions.get(i).getText());
        }

        try {
            stateSelect.selectByValue(stateId);
            System.out.println("Selected state with ID: " + stateId);
        } catch (Exception e) {
            stateSelect.selectByIndex(1);
            String selectedState = stateSelect.getFirstSelectedOption().getAttribute("value");
            System.out.println("Fallback: Selected first available state with ID: " + selectedState);
        }
        
        wait.until(ExpectedConditions.visibilityOf(cityField));
        cityField.clear();
        cityField.sendKeys(city);
        address1Field.clear();
        address1Field.sendKeys(address1);
        address2Field.clear();
        address2Field.sendKeys(address2);
        zipField.clear();
        zipField.sendKeys(zip);
        phoneNumberField.clear();
        phoneNumberField.sendKeys(phone);
        faxNumberField.clear();
        faxNumberField.sendKeys(fax);
        
        System.out.println("Entered billing details: " +
                "firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + 
                ", company=" + company + ", countryId=" + countryId + ", stateId=" + stateId + 
                ", city=" + city + ", address1=" + address1 + ", address2=" + address2 + 
                ", zip=" + zip + ", phone=" + phone + ", fax=" + fax);
    }

    public void clickContinueOnBillingPage() {
        wait.until(ExpectedConditions.elementToBeClickable(billingContinueButton)).click();
        System.out.println("Clicked continue on billing page");
        System.out.println("Current URL after billing continue: " + driver.getCurrentUrl());
    }

    public void selectInStorePickup() {
        wait.until(ExpectedConditions.elementToBeClickable(inStorePickupCheckbox)).click();
        System.out.println("Selected In-Store Pickup");
        WebDriverWait longerWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(20));
        longerWait.until(ExpectedConditions.visibilityOf(shippingContinueButton));
        System.out.println("Shipping continue button is visible after selecting In-Store Pickup");
    }

    public void clickContinueOnShippingPage() {
        WebDriverWait longerWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(20));
        longerWait.until(ExpectedConditions.elementToBeClickable(shippingContinueButton)).click();
        System.out.println("Clicked continue on shipping page");
        System.out.println("Current URL after shipping continue: " + driver.getCurrentUrl());
    }

    public void selectCashOnDelivery() {
        wait.until(ExpectedConditions.elementToBeClickable(cashOnDeliveryRadio)).click();
        System.out.println("Selected Cash on Delivery");
    }

    public void clickContinueOnPaymentPage() {
        WebDriverWait longerWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(30));
        try {
            longerWait.until(ExpectedConditions.visibilityOf(paymentContinueButton));
            System.out.println("Payment continue button is visible");
            longerWait.until(ExpectedConditions.elementToBeClickable(paymentContinueButton));
            System.out.println("Payment continue button is clickable");
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", paymentContinueButton);
            paymentContinueButton.click();
            System.out.println("Clicked continue on payment page");
            longerWait.until(ExpectedConditions.elementToBeClickable(confirmOrderButton));
            System.out.println("Successfully advanced to confirmation step - Confirm button is clickable");
            System.out.println("Current URL after payment continue: " + driver.getCurrentUrl());
        } catch (Exception e) {
            System.out.println("Failed to click payment continue button or advance: " + e.getMessage());
            System.out.println("Current page source for debugging: " + driver.getPageSource());
            throw e;
        }
    }

    public void clickConfirmOrderButton() {
        WebDriverWait longerWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(30));
        try {
            longerWait.until(ExpectedConditions.elementToBeClickable(confirmOrderButton));
            System.out.println("Confirm order button is clickable");
            confirmOrderButton.click();
            System.out.println("Clicked confirm order button");
            System.out.println("Current URL after confirm: " + driver.getCurrentUrl());
        } catch (Exception e) {
            System.out.println("Failed to click confirm order button: " + e.getMessage());
            System.out.println("Current page source for debugging: " + driver.getPageSource());
            throw e;
        }
    }

    public boolean isOrderConfirmationDisplayed() {
        WebDriverWait longerWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(20));
        try {
            WebElement confirmationElement = longerWait.until(ExpectedConditions.visibilityOf(orderConfirmationTitle));
            String confirmationText = confirmationElement.getText().trim();
            System.out.println("Order confirmation displayed: " + confirmationText);
            boolean isConfirmed = confirmationText.toLowerCase().contains("order") && 
                                 confirmationText.toLowerCase().contains("processed");
            System.out.println("Confirmation check result: " + isConfirmed);
            return isConfirmed;
        } catch (Exception e) {
            System.out.println("Order confirmation not found: " + e.getMessage());
            System.out.println("Current URL: " + driver.getCurrentUrl());
            System.out.println("Page source for debugging: " + driver.getPageSource());
            return false;
        }
    }

    public boolean isErrorMessageDisplayed() {
        try {
            WebDriverWait longerWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(20));
            String errorText = longerWait.until(ExpectedConditions.visibilityOf(errorSummary)).getText();
            System.out.println("Validation error displayed: " + errorText);
            return !errorText.isEmpty() && 
                   (errorText.toLowerCase().contains("invalid") || 
                    errorText.toLowerCase().contains("required") || 
                    errorText.toLowerCase().contains("valid"));
        } catch (Exception e) {
            System.out.println("No error message found or timeout occurred: " + e.getMessage());
            try {
                String fieldErrorText = wait.until(ExpectedConditions.visibilityOf(fieldError)).getText();
                System.out.println("Field-specific error displayed: " + fieldErrorText);
                return !fieldErrorText.isEmpty();
            } catch (Exception fieldEx) {
                System.out.println("No field-specific error found either: " + fieldEx.getMessage());
                return false;
            }
        }
    }
}