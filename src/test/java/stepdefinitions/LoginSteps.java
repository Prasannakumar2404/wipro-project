package stepdefinitions;

import Base.BaseClass;
import Base.DriverManager;
import Base.configReader;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import Pages.HomePage;
import Pages.LoginPage;
import Pages.RegistrationPage;
import Pages.SearchPage;
import Pages.CartPage;
import Pages.CheckoutPage;
import Pages.CouponPage;

public class LoginSteps extends BaseClass {
    private WebDriver driver;
    private WebDriverWait wait;
    private HomePage homePage;
    private RegistrationPage registrationPage;
    private LoginPage loginPage;
    private SearchPage searchPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;
    private CouponPage couponPage;
    private String[] registeredCredentials = new String[2];
    private String[] addedProducts = new String[2];
    private int productIndex = 0;

    @Before
    public void setUp() {
        String browser = DriverManager.getBrowserType();
        if (browser == null) {
            throw new IllegalStateException("Browser type not set for this thread! Ensure TestRunner sets it.");
        }
        System.out.println("LoginSteps: Initializing browser " + browser + " for thread " + Thread.currentThread().getId());
        DriverManager.initializeDriver();
        driver = DriverManager.getDriver();
        wait = BaseClass.createWait(driver);
        driver.manage().window().maximize();

        homePage = new HomePage(driver, wait);
        registrationPage = new RegistrationPage(driver, wait);
        loginPage = new LoginPage(driver, wait);
        searchPage = new SearchPage(driver, wait);
        cartPage = new CartPage(driver, wait);
        checkoutPage = new CheckoutPage(driver, wait);
        couponPage = new CouponPage(driver, wait);

        if (homePage.isLoggedIn()) {
            homePage.logout();
        }
    }

    @After
    public void cleanup() {
        if (homePage != null && homePage.isLoggedIn()) {
            homePage.logout();
        }
        if (driver != null) {
            driver.get(configReader.getProperty("url")); 
            System.out.println("Cleanup: Returned to home page");
            DriverManager.quitDriver();
        }
    }

    @Given("user is on Demo Shop Home Page")
    public void user_is_on_demo_shop_home_page() {
        
    }

    @When("user navigates to registration page")
    public void user_navigates_to_registration_page() {
        homePage.navigateToRegistration();
    }
    
    @And("user registers with random email and password {string}")
    public void user_registers_with_random_email_and_password(String password) {
        registrationPage.register(password, registeredCredentials);
    }

    @Then("user registration is successful")
    public void user_registration_is_successful() {
        if (!registrationPage.isRegistrationSuccessful()) {
            throw new AssertionError("Registration failed!");
        }
    }

    @And("user logs out")
    public void user_logs_out() {
        homePage.logout();
    }

    @When("user navigates to login page")
    public void user_navigates_to_login_page() {
        homePage.navigateToLogin();
    }
    
    @And("user logs in with registered credentials")
    public void user_logs_in_with_registered_credentials() {
        loginPage.login(registeredCredentials[0], registeredCredentials[1]);
    }

    @When("user enters email {string}")
    public void user_enters_email(String email) {
        loginPage.enterEmail(email);
    }

    @And("user enters password {string}")
    public void user_enters_password(String password) {
        loginPage.enterPassword(password);
    }

    @And("user clicks on Login button")
    public void user_clicks_on_login_button() {
        loginPage.clickLoginButton();
    }
    
    @Then("user logged in!")
    public void user_logged_in() {
        if (!loginPage.isLoginSuccessful()) {
            throw new AssertionError("Login failed!");
        }
        homePage.logout();
    }

    @Then("login outcome is {string}")
    public void login_outcome_is(String outcome) {
        if ("successful".equals(outcome)) {
            if (!loginPage.isLoginSuccessful()) {
                System.out.println("Assertion failed - Expected successful login but it failed");
                throw new AssertionError("Login should have been successful!");
            }
            System.out.println("Login successful - Proceeding to logout");
            homePage.logout();
        } else if ("error message".equals(outcome)) {
            if (!loginPage.isLoginFailed()) {
                System.out.println("Assertion failed - Expected error message but login succeeded");
                throw new AssertionError("Login should have failed with error message!");
            }
            System.out.println("Login failed with error message as expected");
        } else {
            throw new AssertionError("Unknown login outcome: " + outcome);
        }
    }

    @When("user searches for a product {string}")
    public void user_searches_for_a_product(String product) {
        if (productIndex < addedProducts.length) {
            addedProducts[productIndex] = product;
            System.out.println("Adding product to expected list: " + product + " at index " + productIndex);
        }
        searchPage.enterSearchTerm(product);
        searchPage.clickSearchButton();
    }

    @And("user clicks on the product from search results")
    public void user_clicks_on_the_product_from_search_results() {
        String currentProduct = addedProducts[productIndex];
        searchPage.selectProduct(currentProduct);
    }

    @And("user clicks Add to Cart")
    public void user_clicks_add_to_cart() {
        searchPage.clickAddToCart();
        productIndex++;
    }

    @And("user navigates to cart page")
    public void user_navigates_to_cart_page() {
        cartPage.navigateToCart();
    }
    
    @Then("cart contains correct products and total price")
    public void cart_contains_correct_products_and_total_price() {
        System.out.println("Verifying cart contents for products: " + 
            String.join(", ", addedProducts));
        if (!cartPage.verifyCartContents(addedProducts)) {
            throw new AssertionError("Cart does not contain the expected products (laptop, Smartphone)!");
        }
        if (!cartPage.verifyTotalPrice()) {
            throw new AssertionError("Total price verification failed!");
        }
        System.out.println("Cart contents and total price verified successfully");
    }

    @Then("product is added to the cart successfully")
    public void product_is_added_to_the_cart_successfully() {
        if (!searchPage.isProductAddedToCart()) {
            throw new AssertionError("Product was not added to cart!");
        }
        System.out.println("Product successfully added to cart");
    }
    
    @Given("user adds a laptop to the cart")
    public void user_adds_a_laptop_to_the_cart() {
        searchPage.enterSearchTerm("laptop");
        searchPage.clickSearchButton();
        searchPage.selectProduct("laptop");
        searchPage.clickAddToCart();
        if (!searchPage.isProductAddedToCart()) {
            throw new AssertionError("Laptop was not added to cart!");
        }
        addedProducts[productIndex++] = "laptop";
        System.out.println("Laptop added to cart");
    }

    @When("user clicks Checkout")
    public void user_clicks_checkout() {
        cartPage.navigateToCart();
        checkoutPage.acceptTermsAndConditions();
        checkoutPage.clickCheckoutButton();
    }
    
    @And("user selects Checkout as Guest")
    public void user_selects_checkout_as_guest() {
        checkoutPage.clickCheckoutAsGuest();
    }

    @And("user enters invalid shipping details with first name {string}, last name {string}, email {string}, company {string}, country id {string}, state id {string}, city {string}, address1 {string}, address2 {string}, zip {string}, phone {string}, and fax {string}")
    public void user_enters_invalid_shipping_details(String firstName, String lastName, String email, String company, 
                                                    String countryId, String stateId, String city, String address1, 
                                                    String address2, String zip, String phone, String fax) {
        checkoutPage.enterInvalidBillingDetails(firstName, lastName, email, company, countryId, stateId, city, 
                                               address1, address2, zip, phone, fax);
        System.out.println("Filled billing details");
    }
    
    @And("user clicks continue on checkout page")
    public void user_clicks_continue_on_checkout_page() {
        checkoutPage.clickContinueOnBillingPage();
    }

    @And("user selects in-store pickup")
    public void user_selects_in_store_pickup() {
        checkoutPage.selectInStorePickup();
    }

    @And("user clicks continue on shipping page")
    public void user_clicks_continue_on_shipping_page() {
        checkoutPage.clickContinueOnShippingPage();
    }

    @And("user selects cash on delivery")
    public void user_selects_cash_on_delivery() {
        checkoutPage.selectCashOnDelivery();
    }
    
    @And("user clicks continue on payment page")
    public void user_clicks_continue_on_payment_page() {
        checkoutPage.clickContinueOnPaymentPage();
    }

    @And("user clicks confirm order button")
    public void user_clicks_confirm_order_button() {
        checkoutPage.clickConfirmOrderButton();
    }

    @Then("order confirmation is displayed")
    public void order_confirmation_is_displayed() {
        if (!checkoutPage.isOrderConfirmationDisplayed()) {
            throw new AssertionError("Order confirmation not displayed!");
        }
    }

    // Coupon Code Steps
    @Given("the user is logged in with previous credentials for coupon testing")
    public void the_user_is_logged_in_with_previous_credentials_for_coupon_testing() {
        if (registeredCredentials[0] == null || registeredCredentials[1] == null) {
            homePage.navigateToRegistration();
            registrationPage.register("Test@123", registeredCredentials);
            homePage.logout();
        }
        homePage.navigateToLogin();
        loginPage.login(registeredCredentials[0], registeredCredentials[1]);
        if (!loginPage.isLoginSuccessful()) {
            throw new AssertionError("Login with previous credentials failed!");
        }
        System.out.println("User logged in successfully for coupon testing");
    }

    @When("the user adds a product {string} to the cart for coupon testing")
    public void the_user_adds_a_product_to_the_cart_for_coupon_testing(String product) {
        searchPage.enterSearchTerm(product);
        searchPage.clickSearchButton();
        searchPage.selectProduct(product);
        searchPage.clickAddToCart();
        if (!searchPage.isProductAddedToCart()) {
            throw new AssertionError("Failed to add " + product + " to cart for coupon testing!");
        }
        System.out.println(product + " added to cart for coupon testing");
    }

    @And("the user navigates to the shopping cart page")
    public void the_user_navigates_to_the_shopping_cart_page() {
        couponPage.navigateToCart();
    }

    @And("the user enters the discount code {string}")
    public void the_user_enters_the_discount_code(String couponCode) {
        couponPage.enterCouponCode(couponCode);
        System.out.println("Entered coupon code: " + couponCode);
    }

    @And("the user clicks the apply coupon button")
    public void the_user_clicks_the_apply_coupon_button() {
        couponPage.clickApplyCouponButton();
    }

    @Then("the coupon error message {string} should be displayed")
    public void the_coupon_error_message_should_be_displayed(String expectedErrorMessage) {
        String actualErrorMessage = couponPage.getCouponErrorMessage();
        if (!actualErrorMessage.equals(expectedErrorMessage)) {
            throw new AssertionError("Expected error message: '" + expectedErrorMessage + 
                "' but got: '" + actualErrorMessage + "'");
        }
        System.out.println("Coupon error message verified: " + expectedErrorMessage);
    }
}