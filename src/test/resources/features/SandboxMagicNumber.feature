@magicNumbers
Feature: DRAG-2068 Magic Numbers - Payment Request and check status for magic numbers
  As a user
  I want to make request and check pending, initiated, expired, and failed cases with some specific numbers call magic numbers

  Background: Retrieving access Token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

    # Magic number scenarios would only be run on SIT-Developer

  @regression
  Scenario Outline: Positive flow- A march can initiate payment request and check status for successful, initiated and error in status for 1.81 , 1.81, 1.45 magic numbers
    Given I am an authorized user
    And I have payment details "<totalAmount>","<currency>","<notificationURL>","<appSuccessCallback>","<appFailCallback>","<effectiveDuration>"
    And I have merchant data "<description>","<orderId>","<additionalData>"
    And I have valid payment details with amount "<totalAmount>"
    When I make a request for the payment
    Then I should receive a successful payment response
    And the response body should contain valid payment request id, business logos, created timestamp, web link, app link, totalAmount, currencyCode, statusDescription, statusCode, effectiveDuration
    And the response body should also have notification URI, app success callback URL, app fail Callback Url if applicable
    And the payment request response should be signed
    And I am an authorized user
    And I have a valid payment id
    When I make a request for the check status
    Then I should receive a successful check status response for amount "<totalAmount>"
    And validate payment status response for amount "<totalAmount>"

    Examples:
      | totalAmount | currency | notificationURL                                           | description           | orderId  | effectiveDuration | appSuccessCallback | appFailCallback | additionalData                                                                |
      | 1.81        | HKD      | https://webhook.site/cb082ee4-bdb8-4ca3-82ba-7d771365e57f | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD |
      | 1.80        | HKD      | https://webhook.site/cb082ee4-bdb8-4ca3-82ba-7d771365e57f | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD |
      | 1.45        | HKD      | https://webhook.site/cb082ee4-bdb8-4ca3-82ba-7d771365e57f | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD |


  @regression
  Scenario Outline: Positive flow- A merchant POST payment request with 1.44 magic number would return server error
    Given I am an authorized user
    And I have payment details "<totalAmount>","<currency>","<notificationURL>","<appSuccessCallback>","<appFailCallback>","<effectiveDuration>"
    And I have shopping cart details
      | sku            | name            | quantity | price | currency | category1 | category2  | category3 |
      | pizzapepperoni | pepperoni pizza | 1        | 60    | HKD      | Pizza     | Meat Pizza | Pepperoni |
    And I have merchant data "<description>","<orderId>","<additionalData>"
    When I make a request for the payment
    Then error message should be displayed within the response
    Examples:
      | totalAmount | currency | notificationURL                                           | description           | orderId  | effectiveDuration | appSuccessCallback | appFailCallback | additionalData                                                                |
      | 1.44        | HKD      | https://webhook.site/cb082ee4-bdb8-4ca3-82ba-7d771365e57f | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD |
