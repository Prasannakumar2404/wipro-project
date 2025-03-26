Feature: Demo Web Shop Registration, Login, and Checkout

  Scenario: Registration and Login with Registered Credentials
    Given user is on Demo Shop Home Page
    When user navigates to registration page
    And user registers with random email and password "Test@123"
    Then user registration is successful
    And user logs out
    When user navigates to login page
    And user logs in with registered credentials
    Then user logged in!

  Scenario Outline: Login with Predefined Credentials
    Given user is on Demo Shop Home Page
    And user navigates to login page
    When user enters email "<email>"
    And user enters password "<password>"
    And user clicks on Login button
    Then login outcome is "<outcome>"

    Examples:
      | email               | password    | outcome       |
      | wipro234@gmail.com  | Murali@2404 | successful    |
      | invalid@credential.com | password1 | error message |

  Scenario: Add Product to Shopping Cart
    Given user is on Demo Shop Home Page
    When user searches for a product "laptop"
    And user clicks on the product from search results
    And user clicks Add to Cart
    Then product is added to the cart successfully

  Scenario: Adding Multiple Products to Cart & Validating Cart Summary
    Given user is on Demo Shop Home Page
    When user searches for a product "laptop"
    And user clicks on the product from search results
    And user clicks Add to Cart
    And user searches for a product "Smartphone"
    And user clicks on the product from search results
    And user clicks Add to Cart
    And user navigates to cart page
    Then cart contains correct products and total price

  Scenario: Checkout Process with Complete Flow
    Given user is on Demo Shop Home Page
    And user adds a laptop to the cart
    When user navigates to cart page
    And user clicks Checkout
    And user selects Checkout as Guest
    And user enters invalid shipping details with first name "12", last name "3", email "wipro234@gmail.com", company "TestCo", country id "2", state id "0", city "vk", address1 "a", address2 "b", zip "12", phone "123", and fax "456"
    And user clicks continue on checkout page
    And user selects in-store pickup
    And user clicks continue on shipping page
    And user selects cash on delivery
    And user clicks continue on payment page
    And user clicks confirm order button
    Then order confirmation is displayed

  Scenario: Applying an Invalid Coupon Code and Verifying Error Message
    Given user is on Demo Shop Home Page
    When the user adds a product "Computing and Internet" to the cart for coupon testing
    And the user navigates to the shopping cart page
    And the user enters the discount code "INVALID123"
    And the user clicks the apply coupon button
    Then the coupon error message "The coupon code you entered couldn't be applied to your order" should be displayed