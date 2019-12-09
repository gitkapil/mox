@posPayment
Feature: DRAG-2123 - Post Payment transaction successful with Integrated POS role
  As a user
  I want to Make payment transaction with Integrated POS role

  Background: Retrieving access Token
    Given I am an user with POS role
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

  @trial @regression @skiponsandbox @one
  Scenario Outline: SC1 - Positive flow- A merchant is able to post a payment successfully with all the valid inputs
    Given I am an authorized user
    And I have payment details "<totalAmount>","<currency>","<notificationURL>","<appSuccessCallback>","<appFailCallback>","<effectiveDuration>"
    And I have shopping cart details
      | sku            | name            | quantity | price | currency | category1 | category2  | category3 |
      | pizzapepperoni | pepperoni pizza | 1        | 60    | HKD      | Pizza     | Meat Pizza | Pepperoni |
    And I have merchant data "<description>","<orderId>","<additionalData>"
    When I make a request for the payment with POS role
    Then I should receive a successful payment response
    And the response body should contain valid payment request id, business logos, created timestamp, web link, app link, totalAmount, currencyCode, statusDescription, statusCode, effectiveDuration
    And the response body should also have notification URI, app success callback URL, app fail Callback Url if applicable
    And the payment request response should be signed
    When I make transaction successful with "<mobileNo>", "<pin>" and "<environment>"
    Given I am an authorized user
    And I have a valid payment id
    When I make a request for the check status for amount "<totalAmount>"
    Then the response body should contain a list of transactions
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token
    Given I am an authorized user
    When I query for a list of transactions
    Then I should receive a successful transaction list response after payment done
    Examples:

      | totalAmount | currency | mobileNo    | pin    | environment | notificationURL | description           | orderId  | effectiveDuration | appSuccessCallback | appFailCallback | additionalData                                                                |
     #SIT
      | 1.400       | HKD      | 85282822828 | 142434 |             | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD |
     #UAT1
    #  | 2.500       | HKD      | 85251493020 | 142434 |             | /return3        | message from merchant | random  | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD |
     #POC
     # | 1.400       | HKD      | 85240002083 | 123456 |             | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD |

  @trial @regression @skiponsandbox
  Scenario Outline: SC2- Negative flow- QR code should be expired after effective duration
    Given I am an authorized user
    And I have payment details "<totalAmount>","<currency>","<notificationURL>","<appSuccessCallback>","<appFailCallback>","<effectiveDuration>"
    And I have shopping cart details
      | sku            | name            | quantity | price | currency | category1 | category2  | category3 |
      | pizzapepperoni | pepperoni pizza | 1        | 60    | HKD      | Pizza     | Meat Pizza | Pepperoni |
    And I have merchant data "<description>","<orderId>","<additionalData>"
    When I make a request for the payment with POS role
    Then I should receive a successful payment response
    And the response body should contain valid payment request id, business logos, created timestamp, web link, app link, totalAmount, currencyCode, statusDescription, statusCode, effectiveDuration
    And the response body should also have notification URI, app success callback URL, app fail Callback Url if applicable
    And the payment request response should be signed
    Then I should receive a "<http_status>" error response with "<err_description>" error description and "<error_message>" errorcode within post payment response for "<mobileNo>" and "<pin>"
    Examples:
      | totalAmount | currency | mobileNo    | pin    | notificationURL | description           | orderId  | effectiveDuration | appSuccessCallback | appFailCallback | additionalData                                                                | http_status | err_description            | error_message             |
    #SIT
      | 1.200       | HKD      | 85282822828 | 142434 | /return3        | message from merchant | B1242183 | 15                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | 400         | QR code expired when scans | Business Rules Incorrect! |
    #UAT1
#      | 1.300       | HKD      | 85251493020 | 142434 | /return3        | message from merchant | B1242183 | 15                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | 400         | QR code expired when scans | Business Rules Incorrect! |

  @trial @regression @skiponsandbox @posRefund
  Scenario Outline: SC-3-4 - Positive flow- A merchant is able to post a refund payment successfully with all the valid inputs
    Given I am an authorized user
    And I have payment details "<totalAmount>","<currency>","<notificationURL>","<appSuccessCallback>","<appFailCallback>","<effectiveDuration>"
    And I have shopping cart details
      | sku            | name            | quantity | price | currency | category1 | category2  | category3 |
      | pizzapepperoni | pepperoni pizza | 1        | 60    | HKD      | Pizza     | Meat Pizza | Pepperoni |
    And I have merchant data "<description>","<orderId>","<additionalData>"
    When I make a request for the payment with POS role
    Then I should receive a successful payment response
    And the response body should contain valid payment request id, business logos, created timestamp, web link, app link, totalAmount, currencyCode, statusDescription, statusCode, effectiveDuration
    And the response body should also have notification URI, app success callback URL, app fail Callback Url if applicable
    And the payment request response should be signed
    When I make transaction successful with "<mobileNo>", "<pin>" and "<environment>"
    And I am an authorized user
    And I have a valid payment id
    When I make a request for the check status with POS role
    Then I should receive a transaction of for successful payment
    Given I am logging in as a user with refund role
    And I make refund request with refund amount "<refundAmount>", refund currency "<currency>", reason Code "<reasonCode>" and reason message "<reasonMessage>"
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token
    Given I am an authorized user
    When I query for a list of transactions
    Then I should receive a successful transaction list response after payment refund
    Examples:
      | totalAmount | refundAmount | reasonMessage         | reasonCode | currency | mobileNo    | pin    | environment | notificationURL | description           | orderId  | effectiveDuration | appSuccessCallback | appFailCallback | additionalData                                                                |
      #SIT
      | 1.500       | 1.000        | message from merchant | 00         | HKD      | 85282822828 | 142434 |             | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD |
      | 1.600       | 1.600        | message from merchant | 00         | HKD      | 85282822828 | 142434 |             | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD |
      #UAT1
  #    | 1.200       | 1.000        | message from merchant | 00         | HKD      | 85251493020 | 142434 |             | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD |
  #    | 1.200       | 1.200        | message from merchant | 00         | HKD      | 85251493020 | 142434 |             | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD |


  @trial @regression @skiponsandbox @skiponversionten
  Scenario Outline: SC-5 Negative flow- A merchant should not be able to make refund more than the pay amount
    Given I am an authorized user
    And I have payment details "<totalAmount>","<currency>","<notificationURL>","<appSuccessCallback>","<appFailCallback>","<effectiveDuration>"
    And I have shopping cart details
      | sku            | name            | quantity | price | currency | category1 | category2  | category3 |
      | pizzapepperoni | pepperoni pizza | 1        | 60    | HKD      | Pizza     | Meat Pizza | Pepperoni |
    And I have merchant data "<description>","<orderId>","<additionalData>"
    When I make a request for the payment with POS role
    Then I should receive a successful payment response
    And the response body should contain valid payment request id, business logos, created timestamp, web link, app link, totalAmount, currencyCode, statusDescription, statusCode, effectiveDuration
    And the response body should also have notification URI, app success callback URL, app fail Callback Url if applicable
    And the payment request response should be signed
    When I make transaction successful with "<mobileNo>", "<pin>" and "<environment>"
    And I am an authorized user
    And I have a valid payment id
    When I make a request for the check status with POS role
    Then I should receive a transaction of for successful payment
    Given I am logging in as a user with refund role
    And I make refund request with refund amount "<refundAmount>", refund currency "<currency>", reason Code "<reasonCode>" and reason message "<reasonMessage>"
    Then I should receive a "<httpStatus>" error response with "<error_description>" error description and "<error_code>" errorcode within payment refund response
    Examples:
      | totalAmount | refundAmount | reasonMessage         | reasonCode | currency | mobileNo    | pin    | environment | notificationURL | description           | orderId  | effectiveDuration | appSuccessCallback | appFailCallback | additionalData                                                                | httpStatus | error_description                             | error_code |
      #SIT
      | 1.300       | 1.500        | message from merchant | 00         | HKD      | 85282822828 | 142434 |             | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | 400        | Refund amount entered > net refundable amount | EB010      |
      #UAT1
 #     | 1.400       | 1.500        | message from merchant | 00         | HKD      | 85251493020 | 142434 |             | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | 400        | Refund amount entered > net refundable amount | EB010      |


  @trial @regression @skiponsandbox @skiponversionten
  Scenario Outline: SC6 - Negative flow- A merchant should not be able to make without X-HSBC-Device-Id
    Given I am an authorized user
    And I have payment details "<totalAmount>","<currency>","<notificationURL>","<appSuccessCallback>","<appFailCallback>","<effectiveDuration>"
    And I have shopping cart details
      | sku            | name            | quantity | price | currency | category1 | category2  | category3 |
      | pizzapepperoni | pepperoni pizza | 1        | 60    | HKD      | Pizza     | Meat Pizza | Pepperoni |
    And I have merchant data "<description>","<orderId>","<additionalData>"
    When I make a request for the payment with POS role
    Then I should receive a successful payment response
    And the response body should contain valid payment request id, business logos, created timestamp, web link, app link, totalAmount, currencyCode, statusDescription, statusCode, effectiveDuration
    And the response body should also have notification URI, app success callback URL, app fail Callback Url if applicable
    And the payment request response should be signed
    When I make transaction successful with "<mobileNo>", "<pin>" and "<environment>"
    And I am an authorized user
    And I have a valid payment id
    When I make a request for the check status with POS role
    Then I should receive a transaction of for successful payment
    Given I am logging in as a user with refund role
    And I make refund request with refund amount "<refundAmount>", refund currency "<currency>", reason Code "<reasonCode>" and reason message "<reasonMessage>" with missing header "<missingHeader>"
    Then I should receive a "<httpStatus>" error response with "<error_description>" error description and "<error_code>" errorcode within payment refund response
    And error message should be "<error_message>" within refund response
    Examples:
      | missingHeader    | totalAmount | refundAmount | reasonMessage         | reasonCode | currency | mobileNo    | pin    | environment | notificationURL | description           | orderId  | effectiveDuration | appSuccessCallback | appFailCallback | additionalData                                                                | httpStatus | error_description                                            | error_code | error_message                     |
    #SIT
      | X-HSBC-Device-Id | 1.200       | 1.200        | message from merchant | 00         | HKD      | 85282822828 | 142434 |             | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | 400        | API gateway validation failed - X-HSBC-Device-Id is mandator | EA002      | Service Request Validation Failed |
    #UAT1
 #     | X-HSBC-Device-Id | 1.400       | 1.400        | message from merchant | 00         | HKD      | 85251493020 | 142434 |             | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | 400        | API gateway validation failed - X-HSBC-Device-Id is mandator | EA002      | Service Request Validation Failed |


  @trial @regression @skiponsandbox @skiponversionten
  Scenario Outline: SC-7 - Negative flow- A merchant should be able to make refund without X-HSBC-Merchant-Id
    Given I am an authorized user
    And I have payment details "<totalAmount>","<currency>","<notificationURL>","<appSuccessCallback>","<appFailCallback>","<effectiveDuration>"
    And I have shopping cart details
      | sku            | name            | quantity | price | currency | category1 | category2  | category3 |
      | pizzapepperoni | pepperoni pizza | 1        | 60    | HKD      | Pizza     | Meat Pizza | Pepperoni |
    And I have merchant data "<description>","<orderId>","<additionalData>"
    When I make a request for the payment with POS role
    Then I should receive a successful payment response
    And the response body should contain valid payment request id, business logos, created timestamp, web link, app link, totalAmount, currencyCode, statusDescription, statusCode, effectiveDuration
    And the response body should also have notification URI, app success callback URL, app fail Callback Url if applicable
    And the payment request response should be signed
    When I make transaction successful with "<mobileNo>", "<pin>" and "<environment>"
    And I am an authorized user
    And I have a valid payment id
    When I make a request for the check status with POS role
    Then I should receive a transaction of for successful payment
    Given I am logging in as a user with refund role
    And I make refund request with refund amount "<refundAmount>", refund currency "<currency>", reason Code "<reasonCode>" and reason message "<reasonMessage>" with missing header "<missingHeader>"
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token
    Given I am an authorized user
    When I query for a list of transactions
    Then I should receive a successful transaction list response after payment refund
    Examples:
      | missingHeader      | totalAmount | refundAmount | reasonMessage         | reasonCode | currency | mobileNo    | pin    | environment | notificationURL | description           | orderId  | effectiveDuration | appSuccessCallback | appFailCallback | additionalData                                                                |
    #SIT
      | X-HSBC-Merchant-Id | 1.100       | 1.100        | message from merchant | 00         | HKD      | 85282822828 | 142434 |             | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD |
    #UAT1
#      | X-HSBC-Merchant-Id | 1.400       | 1.400        | message from merchant | 00         | HKD      | 85251493020 | 142434 |             | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD |


  @trial @regression @skiponsandbox @skiponversionten
  Scenario Outline: SC-8-10 - Positive flow- A merchant should see errors message with invalid X-HSBC-Device-Id for payment refund
    Given I am an authorized user
    And I have payment details "<totalAmount>","<currency>","<notificationURL>","<appSuccessCallback>","<appFailCallback>","<effectiveDuration>"
    And I have shopping cart details
      | sku            | name            | quantity | price | currency | category1 | category2  | category3 |
      | pizzapepperoni | pepperoni pizza | 1        | 60    | HKD      | Pizza     | Meat Pizza | Pepperoni |
    And I have merchant data "<description>","<orderId>","<additionalData>"
    When I make a request for the payment with POS role
    Then I should receive a successful payment response
    And the response body should contain valid payment request id, business logos, created timestamp, web link, app link, totalAmount, currencyCode, statusDescription, statusCode, effectiveDuration
    And the response body should also have notification URI, app success callback URL, app fail Callback Url if applicable
    And the payment request response should be signed
    When I make transaction successful with "<mobileNo>", "<pin>" and "<environment>"
    And I am an authorized user
    And I have a valid payment id
    When I make a request for the check status with POS role
    Then I should receive a transaction of for successful payment
    Given I am logging in as a user with refund role
    And I make refund request with refund amount "<refundAmount>", refund currency "<currency>", reason Code "<reasonCode>" and reason message "<reasonMessage>" and deviceId "<deviceId>"
    Then I should receive a "<httpStatus>" error response with "<error_description>" error description and "<error_code>" errorcode within payment refund response
    And error message should be "<error_message>" within refund response
    Examples:
      | totalAmount | refundAmount | reasonMessage         | reasonCode | currency | mobileNo    | pin    | deviceId     | environment | notificationURL | description           | orderId  | effectiveDuration | appSuccessCallback | appFailCallback | additionalData                                                                | httpStatus | error_description                                                                     | error_code | error_message                     |
    #SIT
      | 1.500       | 1.000        | message from merchant | 00         | HKD      | 85282822828 | 142434 | space        |             | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | 400        | API gateway validation failed - X-HSBC-Device-Id is mandatory                         | EA002      | Service Request Validation Failed |
      | 1.400       | 1.100        | message from merchant | 00         | HKD      | 85282822828 | 142434 | doubleQuotes |             | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | 400        | API gateway validation failed - X-HSBC-Device-Id is mandator                          | EA002      | Service Request Validation Failed |
      | 1.300       | 1.200        | message from merchant | 00         | HKD      | 85282822828 | 142434 | tooLong      |             | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | 400        | API gateway validation failed - X-HSBC-Device-Id length must be less than equal to 50 | EA002      | Service Request Validation Failed |
    #UAT1
 #     | 1.400       | 1.000        | message from merchant | 00         | HKD      | 85251493020 | 142434 | space        |             | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | 400        | API gateway validation failed - X-HSBC-Device-Id is mandatory                         | EA002      | Service Request Validation Failed |
 #     | 1.400       | 1.000        | message from merchant | 00         | HKD      | 85251493020 | 142434 | doubleQuotes |             | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | 400        | API gateway validation failed - X-HSBC-Device-Id is mandator                          | EA002      | Service Request Validation Failed |
 #     | 1.400       | 1.000        | message from merchant | 00         | HKD      | 85251493020 | 142434 | tooLong      |             | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | 400        | API gateway validation failed - X-HSBC-Device-Id length must be less than equal to 50 | EA002      | Service Request Validation Failed |


  @trial @regression @skiponsandbox @skiponversionten
  Scenario Outline: SC-11-13 - Positive flow- A merchant should see errors message with invalid X-HSBC-merchant-Id for payment refund
    Given I am an authorized user
    And I have payment details "<totalAmount>","<currency>","<notificationURL>","<appSuccessCallback>","<appFailCallback>","<effectiveDuration>"
    And I have shopping cart details
      | sku            | name            | quantity | price | currency | category1 | category2  | category3 |
      | pizzapepperoni | pepperoni pizza | 1        | 60    | HKD      | Pizza     | Meat Pizza | Pepperoni |
    And I have merchant data "<description>","<orderId>","<additionalData>"
    When I make a request for the payment with POS role
    Then I should receive a successful payment response
    And the response body should contain valid payment request id, business logos, created timestamp, web link, app link, totalAmount, currencyCode, statusDescription, statusCode, effectiveDuration
    And the response body should also have notification URI, app success callback URL, app fail Callback Url if applicable
    And the payment request response should be signed
    When I make transaction successful with "<mobileNo>", "<pin>" and "<environment>"
    And I am an authorized user
    And I have a valid payment id
    When I make a request for the check status with POS role
    Then I should receive a transaction of for successful payment
    Given I am logging in as a user with refund role
    And I make refund request with refund amount "<refundAmount>", refund currency "<currency>", reason Code "<reasonCode>" and reason message "<reasonMessage>" and subUnitId "<subUnitId>"
    Then I should receive a "<httpStatus>" error response with "<error_description>" error description and "<error_code>" errorcode within payment refund response
    And error message should be "<error_message>" within refund response
    Examples:
      | totalAmount | refundAmount | reasonMessage         | reasonCode | currency | mobileNo    | pin    | subUnitId                             | environment | notificationURL | description           | orderId  | effectiveDuration | appSuccessCallback | appFailCallback | additionalData                                                                | httpStatus | error_description                                                                                                                                                                  | error_code | error_message                     |
    #SIT
      | 1.400       | 1.200        | message from merchant | 00         | HKD      | 85282822828 | 142434 | 3fa85f64-5717-4562-b3fc-2c963f66afa71 |             | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | 401        | Invalid value provided in header X-HSBC-Merchant-Id                                                                                                                                | EA001      | Unauthorized Operation!           |
      | 1.300       | 1.100        | message from merchant | 00         | HKD      | 85282822828 | 142434 | randomSubUnitId                       |             | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | 400        | Failed to convert value of type 'java.lang.String' to required type 'java.util.UUID'; nested exception is java.lang.IllegalArgumentException: Invalid UUID string: randomSubUnitId | EA002      | Service Request Validation Failed |
      | 1.200       | 1.000        | message from merchant | 00         | HKD      | 85282822828 | 142434 | 3fa85f64-5717-4562-b3fc-2c963f66afa71 |             | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | 401        | Invalid value provided in header X-HSBC-Merchant-Id                                                                                                                                | EA001      | Unauthorized Operation!           |
    #UAT1
  #    | 1.400       | 1.000        | message from merchant | 00         | HKD      | 85251493020 | 142434 | 3fa85f64-5717-4562-b3fc-2c963f66afa71 |             | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | 401        | Invalid value provided in header X-HSBC-Merchant-Id                                                                                                                                | EA001      | Unauthorized Operation!           |
  #    | 1.400       | 1.000        | message from merchant | 00         | HKD      | 85251493020 | 142434 | randomSubUnitId                       |             | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | 400        | Failed to convert value of type 'java.lang.String' to required type 'java.util.UUID'; nested exception is java.lang.IllegalArgumentException: Invalid UUID string: randomSubUnitId | EA002      | Service Request Validation Failed |
  #    | 1.400       | 1.000        | message from merchant | 00         | HKD      | 85251493020 | 142434 | 3fa85f64-5717-4562-b3fc-2c963f66afa71 |             | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | 401        | Invalid value provided in header X-HSBC-Merchant-Id                                                                                                                                | EA001      | Unauthorized Operation!           |
