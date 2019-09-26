Feature: DRAG-2068 Payment request and check status for magic number
  As a user
  I want to check failed, pending, expired status by using some specific numbers

  Background: Retrieving access Token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token


    @regression @payRequest
  Scenario Outline: Positive flow- A merchant is able to create a payment request with all the valid inputs
    Given I am an authorized user
    And I have payment details "<totalamount>","<currency>","<notificationURL>","<appSuccessCallback>","<appFailCallback>","<effectiveDuration>"
    And I have shopping cart details
      | sku            | name             | quantity | price | currency | category1 | category2  | category3 |
      | pizzapepperoni | pepperoni pizza  | 1        | 60    | HKD      | Pizza     | Meat Pizza | Pepperoni |

    And I have merchant data "<description>","<orderId>","<additionalData>"
    When I make a request for the payment
    Then I should receive a successful payment response
    And the response body should contain valid payment request id, business logos, created timestamp, web link, app link, totalAmount, currencyCode, statusDescription, statusCode, effectiveDuration
    And the response body should also have notification URI, app success callback URL, app fail Callback Url if applicable
    And the payment request response should be signed

    Examples:
      | totalamount | currency | notificationURL | description           | orderId  | effectiveDuration | appSuccessCallback | appFailCallback | additionalData                                                                |
      | 1.81      | HKD      | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD |
