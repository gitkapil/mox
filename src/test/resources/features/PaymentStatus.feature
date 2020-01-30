@checkStatus
Feature: Check Status - DRAG- 178, DRAG-1127, DRAG-1130, DRAG-1133, DRAG-2152

  Background: Retrieving access Token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

  @regression
  Scenario: Positive flow- A merchant is able to create a check status request with all the valid inputs
    Given I am an authorized user
    And I have valid payment details
    And I make a request for the payment
    And I should receive a successful payment response
    And I have a valid payment id
    When I make a request for the check status
    Then I should receive a successful check status response
    And the response body should contain valid payment request id, created timestamp, totalAmount, currencyCode, statusDescription, statusCode, effectiveDuration within check status response
    And the response body should also have app success callback URL, app fail Callback Url if applicable within check status response
    And the payment status response should be signed

  @regression @skiponsandbox
  Scenario Outline: Positive flow- A merchant is able to create a payment request with all the valid inputs
    Given I am an authorized user
    And I have payment details "<totalamount>","<currency>","<notificationURL>","<appSuccessCallback>","<appFailCallback>","<effectiveDuration>"
    And I have shopping cart details
      | sku            | name            | quantity | price | currency | category1 | category2  | category3 |
      | pizzapepperoni | pepperoni pizza | 1        | 60    | HKD      | Pizza     | Meat Pizza | Pepperoni |
    And I have merchant data "<description>","<orderId>","<additionalData>"
    When I make a request for the payment
    Then I should receive a successful payment response
    And the response body should contain valid payment request id, business logos, created timestamp, web link, app link, totalAmount, currencyCode, statusDescription, statusCode, effectiveDuration
    And the response body should also have notification URI, app success callback URL, app fail Callback Url if applicable
    And the payment request response should be signed
    When I make transaction successful with "<mobileNo>", "<pin>" and "<environment>"
    Given I am an authorized user
    And I have a valid payment id
    When I make a request for the check status for amount "<totalamount>"
    Then the response body should contain a list of transactions
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token
    Given I am an authorized user
    When I query for a list of transactions
    Then verify transaction list contains transactionId as retrieved in check status response
    And validate the check status response body
    Examples:

      | totalamount | currency | mobileNo    | pin    | environment | notificationURL | description           | orderId  | effectiveDuration | appSuccessCallback | appFailCallback | additionalData                                                                |
      #SIT
     # | 1.400       | HKD      | 85282822828 | 142434 |             | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD |
      | 1.400       | HKD      | 85288552233 | 147258 |             | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD |
      #POC
     # | 1.400       | HKD      | 85240002083 | 123456 |             | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD |
     # | 1.400       | HKD      | 85276419932 | 135790 |             | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD |


  @regression @skipOnMerchant @failedPayment
  Scenario Outline: Positive flow- A merchant is able to create a check status request and validate transactions detail with transactions API
    Given I am an authorized user
    When I have payment details "<totalamount>","<currency>","<notificationURL>","<appSuccessCallback>","<appFailCallback>","<effectiveDuration>"
    And I have shopping cart details
      | sku            | name            | quantity | price | currency | category1 | category2  | category3 |
      | pizzapepperoni | pepperoni pizza | 1        | 60    | HKD      | Pizza     | Meat Pizza | Pepperoni |
    And I have merchant data "<description>","<orderId>","<additionalData>"
    And I make a request for the payment
    And I should receive a successful payment response
    And I have a valid payment id
    When I make a request for the check status
    Then I should receive a successful check status response
    And validate the check status response body
    And the response body should also have app success callback URL, app fail Callback Url if applicable within check status response
    And the payment status response should be signed
    Given I am an authorized user
    When I query for a list of transactions
    Then verify transaction list contains transactionId as retrieved in check status response
    Examples:
      | totalamount | currency | notificationURL                                           | description           | orderId  | effectiveDuration | appSuccessCallback | appFailCallback | additionalData                                                                |
      | 500.81      | HKD      | /return3                                                  | message from merchant | B1242183 | 40                | /confirmation      | /unsuccessful   | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD |
      | 100.66      | HKD      | https://webhook.site/2f6552af-3f1a-4191-810e-69f7d548fb74 | message from merchant | B1242183 |                   |                    |                 | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD |

  @regression
  Scenario: Negative flow- Invalid auth token (without Bearer in the header)
    Given I am an authorized user
    And I have valid payment details
    And I make a request for the payment
    And I should receive a successful payment response
    And I have a valid payment id
    And I dont send Bearer with the auth token in the check status request
    When I make a request for the check status
    Then I should receive a "401" error response with "Error validating JWT" error description and "EA001" errorcode within check status response
    And error message should be "API Gateway Authentication Failed" within check status response
    And the payment status response should be signed

  @regression
  Scenario Outline: Negative flow- Mandatory fields not sent in the header
    Given I am an authorized user
    And I have valid payment details
    And I make a request for the payment
    And I should receive a successful payment response
    And I have a valid payment id
    When I make a request for the payment status with "<key>" missing in the header
    Then I should receive a "<response_code>" error response with "<error_description>" error description and "<error_code>" errorcode within check status response
    And error message should be "<error_message>" within check status response
    And the payment status response should be signed

    Examples:
      | error_description                                                     | error_message                     | key               | response_code | error_code |
      | Error validating JWT                                                  | API Gateway Authentication Failed | Authorization     | 401           | EA001      |
      | Header Request-Date-Time was not found in the request. Access denied. | API Gateway Validation Failed     | Request-Date-Time | 400           | EA002      |
      | Header Trace-Id was not found in the request. Access denied.          | API Gateway Validation Failed     | Trace-Id          | 400           | EA002      |
      | Header Accept does not contain required value.  Access denied.        | Request Header Not Acceptable     | Accept            | 406           | EA008      |

  @regression
  Scenario Outline: Negative flow- Mandatory fields not sent in the header
    Given I am an authorized user
    And I have valid payment details
    And I make a request for the payment
    And I should receive a successful payment response
    And I have a valid payment id
    When I make a request for the payment status with "<key>" missing in the header
    Then error message should be "Resource not found" within check status response
    And the payment status response should be signed

    Examples:
      | key         |
      | Api-Version |

  @regression
  Scenario Outline: Negative flow- Invalid auth token
    Given I am an authorized user
    And I have valid payment details
    When I make a request for the payment
    And I should receive a successful payment response
    And I have a valid payment id
    And I send invalid auth token "<auth_token>" in the check status request
    When I make a request for the check status
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorcode within check status response
    And error message should be "<error_message>" within check status response
    And the payment status response should be signed

    Examples:
      | error_description    | http_status | error_message                     | auth_token                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      | error_code |
 #Auth Token missing
      | Error validating JWT | 401         | API Gateway Authentication Failed |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 | EA001      |
 # Auth token not a JWT
      | Error validating JWT | 401         | API Gateway Authentication Failed | random_auth_token                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               | EA001      |
 # Expired auth token
      | Error validating JWT | 401         | API Gateway Authentication Failed | eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Imk2bEdrM0ZaenhSY1ViMkMzbkVRN3N5SEpsWSIsImtpZCI6Imk2bEdrM0ZaenhSY1ViMkMzbkVRN3N5SEpsWSJ9.eyJhdWQiOiI1MTczYTVhOS00MjEyLTQ4NzctODYyMS03YmMxNjRjZjE3OGIiLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yZmY5M2NjZS1lZmQxLTRlMTYtOTJiYS1hZmI1M2U5ZTA5ZmMvIiwiaWF0IjoxNTM3MTY4NzAzLCJuYmYiOjE1MzcxNjg3MDMsImV4cCI6MTUzNzE3MjYwMywiYWlvIjoiNDJCZ1lKaDlXbFZsNFh1SjZFMHZsMDMvY25TOU5nQT0iLCJhcHBpZCI6IjEwOTMyM2UzLWM1NWUtNDI0Yy1iNzEyLTJlODJmMTY5NWU5OCIsImFwcGlkYWNyIjoiMSIsImlkcCI6Imh0dHBzOi8vc3RzLndpbmRvd3MubmV0LzJmZjkzY2NlLWVmZDEtNGUxNi05MmJhLWFmYjUzZTllMDlmYy8iLCJvaWQiOiI4NjM3MjIwNi1jZWZhLTQ1ZWEtOTM0Ni0xNjNiYmFiYzc3MDYiLCJyb2xlcyI6WyJwYXltZW50UmVxdWVzdCIsInJlZnVuZCIsImRldmVsb3BlciJdLCJzdWIiOiI4NjM3MjIwNi1jZWZhLTQ1ZWEtOTM0Ni0xNjNiYmFiYzc3MDYiLCJ0aWQiOiIyZmY5M2NjZS1lZmQxLTRlMTYtOTJiYS1hZmI1M2U5ZTA5ZmMiLCJ1dGkiOiI3bUd1b0FRMUQwZTFGMks0Sy1ZWEFBIiwidmVyIjoiMS4wIn0.gCfn6QNUjzENuKvVN0bdkWgRwN-1ipovSq5Yb8IjCRfRhZNTFffLP1oIVw_8sPQxTFfV8CBOcQG385n-r_tIWBlMANHdKmpmwmOTz4J08EWzz_SY4zWdvMlF7quCYdrXVIXaKd-PLtO0UDTI7CsSAso7OAOsHBNrn3ITBoR0aMo_lM8X5dJM5fleSHFiJPMYDHpawZuy_BBXC0AUdcVT61NHkdO7sDV4Dc_C12CsShEqMi3Nj-uzr5wYHmPTxi3nk2px-_yiVaAHxxCmE0f7rWdM4BjVB89j_xtZrn1-VovgUUn_QDppY9yOgKx831xXBMX0Tz293V8g7BkagVI3lw | EA001      |
 # Auth token unverified
      | Error validating JWT | 401         | API Gateway Authentication Failed | eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     | EA001      |

  @regression
  Scenario Outline: Negative flow- Invalid PaymentIds sent in the request
    Given I am an authorized user
    And I have valid payment details
    And I make a request for the payment
    And I should receive a successful payment response
    And I have a payment id "<payment_id>"
    When I make a request for the check status
    Then I should receive a "<response_code>" error response with "<error_description>" error description and "<error_code>" errorcode within check status response
    And error message should be "<error_message>" within check status response
    And the payment status response should be signed
    Examples:
      | error_description             | error_message                     | payment_id                           | error_code | response_code |
      | PayCode not found             |                                   | 591ec407-401d-40a6-9db0-b48a35fad8a3 | EB008      | 400           |
      | Payment Request Id is invalid | Service Request Validation Failed | random_payment_id                    | EA002      | 400           |

  @regression
  Scenario Outline: Negative flow- Mandatory field payment_id provided null in path parameter
    Given I am an authorized user
    And I have valid payment details
    And I make a request for the payment
    And I should receive a successful payment response
    And I have a payment id "<payment_id>"
    When I make a request for the check status
    Then I should receive "404" status code in check status response
    And error message should be "Resource not found" within the check status response
    Examples:
      | payment_id |
      |            |

  @trial @regression
  Scenario Outline: Negative flow- Request Date Time's invalid values set within the header
    Given I am an authorized user
    And I have valid payment details
    When I make a request for the payment
    And I should receive a successful payment response
    And I have a valid payment id
    When I make a request for the check status with invalid value for request date time "<value>"
    Then I should receive a "<response_code>" error response with "<error_description>" error description and "<error_code>" errorcode within check status response
    And error message should be "<error_message>" within check status response
    And the payment status response should be signed

    Examples:
      | value                     | error_description                               | error_code | error_message                     | response_code |
      |                           | Request timestamp not a valid RFC3339 date-time | EA002      | Service Request Validation Failed | 400           |
      | xyz                       | Request timestamp not a valid RFC3339 date-time | EA002      | Service Request Validation Failed | 400           |
      | 2019-02-04T00:42:45.237Z  | Request timestamp too old                       | EA002      | Service Request Validation Failed | 400           |
      | 2019-21-10T00:42:45.237Z  | Request timestamp not a valid RFC3339 date-time | EA002      | Service Request Validation Failed | 400           |
      | 2019-10-35T06:54:52.237Z  | Request timestamp not a valid RFC3339 date-time | EA002      | Service Request Validation Failed | 400           |
      #acceptable Request Date Time format
      | 2019-10-21                | Request timestamp too old                       | EA002      | Service Request Validation Failed | 400           |
      | 2019/10/21 T07:04:52.237Z | Request timestamp too old                       | EA002      | Service Request Validation Failed | 400           |
      | 21 Oct 2019 06:49:52      | Request timestamp too old                       | EA002      | Service Request Validation Failed | 400           |

  @trial @regression @skiponversioneleven @skiponversionten
  Scenario Outline: Negative flow- Request Date Time's invalid values set within the header
    Given I am an authorized user
    And I have valid payment details
    When I make a request for the payment
    And I should receive a successful payment response
    And I have a valid payment id
    When I make a request for the check status with invalid value for request date time "<value>"
    Then I should receive a "<response_code>" error response with "<error_description>" error description and "<error_code>" errorcode within check status response
    And error message should be "<error_message>" within check status response
    And the payment status response should be signed

    Examples:
      | value                    | error_description                     | error_code | error_message                     | response_code |
      | 2021-10-21T00:42:45.237Z | Request timestamp is future date-time | EA002      | Service Request Validation Failed | 400           |
      | 21 Jan 2021              | Request timestamp is future date-time | EA002      | Service Request Validation Failed | 400           |
      | 2021 October 20          | Request timestamp is future date-time | EA002      | Service Request Validation Failed | 400           |
