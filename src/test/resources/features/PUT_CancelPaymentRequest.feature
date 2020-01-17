@cancel @skiponversioneleven @skiponversionten
Feature: DRAG-2044 - Cancel the payment request
  As a user
  I want to cancel the payment request on aborted, expired and used QR codes

  Background: Retrieving access Token
    Given I am an user with POS role
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

  @regression @skiponversionten @skiponversioneleven
  Scenario Outline: SC1 -Positive flow- merchant should be able to cancel the payment request successfully
    Given I am an authorized user
    And I have payment details "<totalAmount>","<currency>","<notificationURL>","<appSuccessCallback>","<appFailCallback>","<effectiveDuration>"
    And I have shopping cart details
      | sku            | name            | quantity | price | currency | category1 | category2  | category3 |
      | pizzapepperoni | pepperoni pizza | 1        | 60    | HKD      | Pizza     | Meat Pizza | Pepperoni |
    And I have merchant data "<description>","<orderId>","<additionalData>"
    When I make a request for the payment with POS role
    Then I should receive a successful payment response
    Given I am an authorized user to cancel payment request
    When I hit cancel payment request endpoint with payment request id
    Then payment request should be cancelled
    And validate the success response of cancel payment request
    When I am an authorized user
    And I have a valid payment id
    When get payment status response should return status description "<statusDescription>"

    Examples:
      | totalAmount | currency | notificationURL | description           | orderId  | effectiveDuration | appSuccessCallback | appFailCallback | additionalData                                                                | statusDescription            |
      | 1.400       | HKD      | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | Request for Payment Rejected |

  @regression @skiponversionten @skiponversioneleven
  Scenario Outline: SC2- Negative flow- cancel the aborted QR code
    Given I am an authorized user
    And I have payment details "<totalAmount>","<currency>","<notificationURL>","<appSuccessCallback>","<appFailCallback>","<effectiveDuration>"
    And I have shopping cart details
      | sku            | name            | quantity | price | currency | category1 | category2  | category3 |
      | pizzapepperoni | pepperoni pizza | 1        | 60    | HKD      | Pizza     | Meat Pizza | Pepperoni |
    And I have merchant data "<description>","<orderId>","<additionalData>"
    When I make a request for the payment with POS role
    Then I should receive a successful payment response
    Given I am an authorized user to cancel payment request
    When I hit cancel payment request endpoint with payment request id
    Then payment request should be cancelled
    And validate the success response of cancel payment request
    When I hit cancel payment request endpoint with payment request id
    Then I should receive a "<httpStatus>" error response with "<error_description>" error description and "<error_code>" error Code with cancel payment response
    When I am an authorized user
    And I have a valid payment id
    When get payment status response should return status description "<statusDescription>"
    Examples:
      | totalAmount | currency | notificationURL | description           | orderId  | effectiveDuration | appSuccessCallback | appFailCallback | additionalData                                                                | httpStatus | error_description            | error_code | statusDescription            |
      | 1.000       | HKD      | /return3        | message from merchant | B1242183 | 15                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | 400        | QR code is in Aborted state. | EB020      | Request for Payment Rejected |


  @regression @skiponversionten @skiponversioneleven @skiponsandbox
  Scenario Outline: SC3- Negative flow- A merchant receives error - QR code is in Completed state
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
    Given I am an authorized user to cancel payment request
    When I hit cancel payment request endpoint with payment request id
    Then I should receive a "<httpStatus>" error response with "<error_description>" error description and "<error_code>" error Code with cancel payment response
    When I am an authorized user
    And I have a valid payment id
    When get payment status response should return status description "<statusDescription>"
    Examples:
      | totalAmount | currency | mobileNo    | pin    | environment | notificationURL | description           | orderId  | effectiveDuration | appSuccessCallback | appFailCallback | additionalData                                                                | httpStatus | error_description              | error_code | statusDescription |
      | 0.81        | HKD      | 85288552233 | 147258 |             | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | 400        | QR code is in Completed state. | EB021      | Payment Success   |
  #    | 0.81        | HKD      | 85282822828 | 142434 |             | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | 400        | QR code is in Completed state. | EB021      | Payment Success   |

  @skiponversionten @skiponversioneleven @skiponmerchant @regression
  Scenario Outline: SC3- Negative flow- A merchant receives error in SANDBOX - QR code is in Completed state
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
    Given I am an authorized user to cancel payment request
    When I hit cancel payment request endpoint with payment request id
    Then I should receive a "<httpStatus>" error response with "<error_description>" error description and "<error_code>" error Code with cancel payment response
    When I am an authorized user
    And I have a valid payment id
    When get payment status response should return status description "<statusDescription>"
    Examples:
      | totalAmount | currency | notificationURL | description           | orderId  | effectiveDuration | appSuccessCallback | appFailCallback | additionalData                                                                | httpStatus | error_description              | error_code | statusDescription |
      | 0.81        | HKD      | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | 400        | QR code is in Completed state. | EB021      | Payment Success   |


  @regression @skiponversionten @skiponversioneleven
  Scenario Outline: SC4-Negative flow- cancel the aborted QR code
    Given I am an authorized user
    And I have payment details "<totalAmount>","<currency>","<notificationURL>","<appSuccessCallback>","<appFailCallback>","<effectiveDuration>"
    And I have shopping cart details
      | sku            | name            | quantity | price | currency | category1 | category2  | category3 |
      | pizzapepperoni | pepperoni pizza | 1        | 60    | HKD      | Pizza     | Meat Pizza | Pepperoni |
    And I have merchant data "<description>","<orderId>","<additionalData>"
    When I make a request for the payment with POS role
    Then I should receive a successful payment response
    Given I am an authorized user to cancel payment request
    When  put cancel payment request endpoint with payment request id after "<effectiveDuration>"
    Then I should receive a "<httpStatus>" error response with "<error_description>" error description and "<error_code>" error Code with cancel payment response
    When I am an authorized user
    And I have a valid payment id
    When get payment status response should return status description "<statusDescription>"
    Examples:
      | totalAmount | currency | notificationURL | description           | orderId  | effectiveDuration | appSuccessCallback | appFailCallback | additionalData                                                                | httpStatus | error_description | error_code | statusDescription       |
      | 1.000       | HKD      | /return3        | message from merchant | B1242183 | 15                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | 400        | Expired pay code  | EB007      | Payment Request Expired |


  @regression @skiponmerchant
  Scenario Outline: SC 5-6- Positive flow- A merchant can initiate payment request and check status for magic numbers 70 cents , 45 cents, PayCode not found and Internal server Error
    Given I am an authorized user
    And I have payment details "<totalAmount>","<currency>","<notificationURL>","<appSuccessCallback>","<appFailCallback>","<effectiveDuration>"
    And I have shopping cart details
      | sku            | name            | quantity | price | currency | category1 | category2  | category3 |
      | pizzapepperoni | pepperoni pizza | 1        | 60    | HKD      | Pizza     | Meat Pizza | Pepperoni |
    And I have merchant data "<description>","<orderId>","<additionalData>"
    When I make a request for the payment with POS role
    Then I should receive a successful payment response
    When I am an authorized user
    And I have a valid payment id
    When get payment status response should return error description "<errorDescription>", errorCode "<errorCode>" and response code "<httpStatus>"
    Given I am an authorized user to cancel payment request
    When I hit cancel payment request endpoint with payment request id
    Then I should receive a "<httpStatus>" error response with "<errorDescription>" error description and "<errorCode>" error Code with cancel payment response
    Examples:
      | totalAmount | currency | notificationURL | description           | orderId  | effectiveDuration | appSuccessCallback | appFailCallback | additionalData                                                                | errorDescription                       | errorCode | httpStatus |
      | 0.70        | HKD      | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | PayCode not found                      | EB008     | 400        |
      | 0.45        | HKD      | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | Internal Server Error, contact support | EB099     | 400        |


  @regression @skiponmerchant @skiponversionten @skiponversioneleven
  Scenario Outline: SC-7 Positive flow- A merchant can initiate payment request and check status for magic numbers 71 cents, Expired
    Given I am an authorized user
    And I have payment details "<totalAmount>","<currency>","<notificationURL>","<appSuccessCallback>","<appFailCallback>","<effectiveDuration>"
    And I have shopping cart details
      | sku            | name            | quantity | price | currency | category1 | category2  | category3 |
      | pizzapepperoni | pepperoni pizza | 1        | 60    | HKD      | Pizza     | Meat Pizza | Pepperoni |
    And I have merchant data "<description>","<orderId>","<additionalData>"
    When I make a request for the payment with POS role
    Then I should receive a successful payment response
    When I am an authorized user
    And I have a valid payment id
    When get payment status response should return status description "<statusDescription>"
    Given I am an authorized user to cancel payment request
    When I hit cancel payment request endpoint with payment request id
    Then I should receive a "<httpStatus>" error response with "<errorDescription>" error description and "<cancel_errorCode>" error Code with cancel payment response
    Examples:
      | totalAmount | currency | notificationURL | description           | orderId  | effectiveDuration | appSuccessCallback | appFailCallback | additionalData                                                                | statusDescription       | errorDescription | cancel_errorCode | httpStatus |
      | 0.71        | HKD      | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | Payment Request Expired | Expired pay code | EB007            | 400        |


  @regression @skiponversionten @skiponversioneleven
  Scenario Outline: SC-8-12 Negative flow - Unable to cancel payment request due to invalid header values
    Given I am an authorized user
    And I have payment details "<totalAmount>","<currency>","<notificationURL>","<appSuccessCallback>","<appFailCallback>","<effectiveDuration>"
    And I have shopping cart details
      | sku            | name            | quantity | price | currency | category1 | category2  | category3 |
      | pizzapepperoni | pepperoni pizza | 1        | 60    | HKD      | Pizza     | Meat Pizza | Pepperoni |
    And I have merchant data "<description>","<orderId>","<additionalData>"
    When I make a request for the payment with POS role
    Then I should receive a successful payment response
    Given I am an authorized user to cancel payment request
    When cancel payment request endpoint with payment request id with null header "<nullHeaderValues>"
    Then I should receive a "<httpStatus>" error response with "<errorDescription>" error description and "<errorCode>" error Code with cancel payment response

    Examples:
      | nullHeaderValues | totalAmount | currency | notificationURL | description           | orderId  | effectiveDuration | appSuccessCallback | appFailCallback | additionalData                                                                | errorDescription                                               | errorCode | httpStatus |
      | Authorization    | 0.72        | HKD      | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | Error validating JWT                                           | EA001     | 401        |
      | Trace-Id         | 0.72        | HKD      | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | Header Trace-Id was not found in the request. Access denied.   | EA002     | 400        |
      | Accept           | 0.72        | HKD      | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | Header Accept does not contain required value.  Access denied. | EA008     | 406        |
      | Content-Type     | 0.72        | HKD      | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | Content type                                                   | EA002     | 415        |
      | Signature        | 0.71        | HKD      | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | Header Signature was not found in the request. Access denied.  | EA002     | 400        |


  @regression @skiponversionten @skiponversioneleven
  Scenario Outline: SC-13-16 -Negative flow- Invalid auth token
    Given I am an authorized user
    And I have payment details "<totalAmount>","<currency>","<notificationURL>","<appSuccessCallback>","<appFailCallback>","<effectiveDuration>"
    And I have shopping cart details
      | sku            | name            | quantity | price | currency | category1 | category2  | category3 |
      | pizzapepperoni | pepperoni pizza | 1        | 60    | HKD      | Pizza     | Meat Pizza | Pepperoni |
    And I have merchant data "<description>","<orderId>","<additionalData>"
    When I make a request for the payment with POS role
    Then I should receive a successful payment response
    Given I am an authorized user to cancel payment request
    Given I am an authorized user to cancel payment request with token "<auth_token>"
    When I hit cancel payment request endpoint with payment request id
    Then I should receive a "<http_Status>" error response with "<errorDescription>" error description and "<errorCode>" error Code with cancel payment response

    Examples:
      | totalAmount | currency | notificationURL | description           | orderId  | effectiveDuration | appSuccessCallback | appFailCallback | additionalData                                                                | http_Status | errorCode | errorDescription     | auth_token                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
      | 0.72        | HKD      | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | 401         | EA001     | Error validating JWT |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        |
      | 0.72        | HKD      | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | 401         | EA001     | Error validating JWT | random_auth_token                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
      | 0.72        | HKD      | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | 401         | EA001     | Error validating JWT | Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Imk2bEdrM0ZaenhSY1ViMkMzbkVRN3N5SEpsWSIsImtpZCI6Imk2bEdrM0ZaenhSY1ViMkMzbkVRN3N5SEpsWSJ9.eyJhdWQiOiI1MTczYTVhOS00MjEyLTQ4NzctODYyMS03YmMxNjRjZjE3OGIiLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yZmY5M2NjZS1lZmQxLTRlMTYtOTJiYS1hZmI1M2U5ZTA5ZmMvIiwiaWF0IjoxNTM3MTY4NzAzLCJuYmYiOjE1MzcxNjg3MDMsImV4cCI6MTUzNzE3MjYwMywiYWlvIjoiNDJCZ1lKaDlXbFZsNFh1SjZFMHZsMDMvY25TOU5nQT0iLCJhcHBpZCI6IjEwOTMyM2UzLWM1NWUtNDI0Yy1iNzEyLTJlODJmMTY5NWU5OCIsImFwcGlkYWNyIjoiMSIsImlkcCI6Imh0dHBzOi8vc3RzLndpbmRvd3MubmV0LzJmZjkzY2NlLWVmZDEtNGUxNi05MmJhLWFmYjUzZTllMDlmYy8iLCJvaWQiOiI4NjM3MjIwNi1jZWZhLTQ1ZWEtOTM0Ni0xNjNiYmFiYzc3MDYiLCJyb2xlcyI6WyJwYXltZW50UmVxdWVzdCIsInJlZnVuZCIsImRldmVsb3BlciJdLCJzdWIiOiI4NjM3MjIwNi1jZWZhLTQ1ZWEtOTM0Ni0xNjNiYmFiYzc3MDYiLCJ0aWQiOiIyZmY5M2NjZS1lZmQxLTRlMTYtOTJiYS1hZmI1M2U5ZTA5ZmMiLCJ1dGkiOiI3bUd1b0FRMUQwZTFGMks0Sy1ZWEFBIiwidmVyIjoiMS4wIn0.gCfn6QNUjzENuKvVN0bdkWgRwN-1ipovSq5Yb8IjCRfRhZNTFffLP1oIVw_8sPQxTFfV8CBOcQG385n-r_tIWBlMANHdKmpmwmOTz4J08EWzz_SY4zWdvMlF7quCYdrXVIXaKd-PLtO0UDTI7CsSAso7OAOsHBNrn3ITBoR0aMo_lM8X5dJM5fleSHFiJPMYDHpawZuy_BBXC0AUdcVT61NHkdO7sDV4Dc_C12CsShEqMi3Nj-uzr5wYHmPTxi3nk2px-_yiVaAHxxCmE0f7rWdM4BjVB89j_xtZrn1-VovgUUn_QDppY9yOgKx831xXBMX0Tz293V8g7BkagVI3lw |
      | 0.72        | HKD      | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | 401         | EA001     | Error validating JWT | Bearer nbCwW11w3XkB-xUaXwKRSLjMHGQ                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |


  @regression @skiponversionten @skiponversioneleven
  Scenario Outline: SC1 -Positive flow- merchant should not be able to cancel the payment request with invalid payment request id
    Given I am an authorized user
    And I have payment details "<totalAmount>","<currency>","<notificationURL>","<appSuccessCallback>","<appFailCallback>","<effectiveDuration>"
    And I have shopping cart details
      | sku            | name            | quantity | price | currency | category1 | category2  | category3 |
      | pizzapepperoni | pepperoni pizza | 1        | 60    | HKD      | Pizza     | Meat Pizza | Pepperoni |
    And I have merchant data "<description>","<orderId>","<additionalData>"
    When I make a request for the payment with POS role
    Then I should receive a successful payment response
    Given I am an authorized user to cancel payment request
    When cancel payment request endpoint with payment request id "<paymentRequestId>"
    Then I should receive a "<httpStatus>" error response with "<errorDescription>" error description and "<cancel_errorCode>" error Code with cancel payment response
    Examples:
      | paymentRequestId                    | totalAmount | currency | notificationURL | description           | orderId  | effectiveDuration | appSuccessCallback | appFailCallback | additionalData                                                                | errorDescription              | cancel_errorCode | httpStatus |
      | 7d7659c8-e188-4328-9d90-7deefd2a1fc | 0.72        | HKD      | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | PayCode not found             | EB008            | 400        |
      | 7d7659c8                            | 0.72        | HKD      | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | Payment Request Id is invalid | EA002            | 400        |
      | abc                                 | 0.72        | HKD      | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | Payment Request Id is invalid | EA002            | 400        |
      | $^$&&                               | 0.72        | HKD      | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | Unable to verify signature    | EA001            | 401        |


  @trial @regression @skiponversionten @skiponversioneleven
  Scenario Outline: Negative flow- Invalid mandatory field provided in header
    Given I am an authorized user
    And I have payment details "<totalAmount>","<currency>","<notificationURL>","<appSuccessCallback>","<appFailCallback>","<effectiveDuration>"
    And I have shopping cart details
      | sku            | name            | quantity | price | currency | category1 | category2  | category3 |
      | pizzapepperoni | pepperoni pizza | 1        | 60    | HKD      | Pizza     | Meat Pizza | Pepperoni |
    And I have merchant data "<description>","<orderId>","<additionalData>"
    When I make a request for the payment with POS role
    Then I should receive a successful payment response
    Given I am an authorized user to cancel payment request
    When hit cancel payment request endpoint with payment request id with invalid header "<key>" and values "<invalidHeaderValues>"
    Then I should receive a "<httpStatus>" error response with "<errorDescription>" error description and "<errorCode>" error Code with cancel payment response

    Examples:
      | key               | invalidHeaderValues                  | totalAmount | currency | notificationURL | description           | orderId  | effectiveDuration | appSuccessCallback | appFailCallback | additionalData                                                                | httpStatus | errorCode | errorDescription                                                  |
      | Accept            | Testing/Type                         | 1.400       | HKD      | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | 406        | EA008     | Header Accept does not contain required value.  Access denied.    |
      | Content-Type      | application/json1                    | 1.400       | HKD      | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | 415        | EA002     | Content type 'application/json1;charset=ISO-8859-1' not supported |
      | Trace-Id          | 123456                               | 1.400       | HKD      | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | 400        | EA002     | Trace-Id is invalid                                               |
      | Trace-Id          | abcde                                | 1.400       | HKD      | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | 400        | EA002     | Trace-Id is invalid                                               |
      | Trace-Id          | 7454108z-yb37-454c-81da-0a12d8b0f867 | 1.400       | HKD      | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | 400        | EA002     | Trace-Id is invalid                                               |
      | Trace-Id          | 790b6abc-48dc-4f6c-8dfe-e3befc771fbc | 1.400       | HKD      | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | 401        | EA001     | Unable to verify signature                                        |
      | Request-Date-Time | 1234                                 | 1.400       | HKD      | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | 400        | EA002     | Request timestamp not a valid RFC3339 date-time                   |
      | Request-Date-Time | 2021-11-12T08:05:55.936Z             | 1.400       | HKD      | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | 400        | EA002     | Request timestamp is future date-time                             |
      | Request-Date-Time | 2018-11-12T08:05:55.936Z             | 1.400       | HKD      | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | 400        | EA002     | Request timestamp too old                                         |

  @trial @regression @skiponversionten @skiponversioneleven
  Scenario Outline: Negative flow- Invalid mandatory field provided in header
    Given I am an authorized user
    And I have payment details "<totalAmount>","<currency>","<notificationURL>","<appSuccessCallback>","<appFailCallback>","<effectiveDuration>"
    And I have shopping cart details
      | sku            | name            | quantity | price | currency | category1 | category2  | category3 |
      | pizzapepperoni | pepperoni pizza | 1        | 60    | HKD      | Pizza     | Meat Pizza | Pepperoni |
    And I have merchant data "<description>","<orderId>","<additionalData>"
    When I make a request for the payment with POS role
    Then I should receive a successful payment response
    Given I am an authorized user to cancel payment request
    When hit cancel payment request endpoint with payment request id with invalid header "<key>" and values "<invalidHeaderValues>"
    Then I should receive a "<httpStatus>" status code with "<statusCode>" message "<message>" with cancel payment response

    Examples:
      | key         | invalidHeaderValues | totalAmount | currency | notificationURL | description           | orderId  | effectiveDuration | appSuccessCallback | appFailCallback | additionalData                                                                | httpStatus | statusCode | message            |
      | Api-Version | 0.20                | 1.400       | HKD      | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | 404        | 404        | Resource not found |
      | Api-Version | abc                 | 1.400       | HKD      | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | 404        | 404        | Resource not found |
      | Api-Version | 0.10                | 1.400       | HKD      | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | 404        | 404        | Resource not found |
      | Api-Version | 0.11                | 1.400       | HKD      | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | 404        | 404        | Resource not found |
      | Api-Version | @#$%^               | 1.400       | HKD      | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | 404        | 404        | Resource not found |
