@skiponversioneleven @skiponversionten
Feature: GET Transaction details based on Transaction ID API DRAG-2080

  Background: Retrieving access token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token


  @trial @regression @skiponsandbox
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
    Given I am an authorized user
    When I query for a list of transactions
    Then verify transaction list contains transactionId as retrieved in check status response
    Given I am an authorized user
    When I query for transaction details with transactionId retrieved in check status response
    Then I should receive a successful transactionID response
    And validate GET transaction by ID response

    Examples:
      | totalamount | currency | mobileNo    | pin    | environment | notificationURL | description           | orderId | effectiveDuration | appSuccessCallback | appFailCallback | additionalData                                                                |
    #SIT
      | 1.400       | HKD      | 85282822828 | 142434 |             | /return3        | message from merchant | random  | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD |
    #UAT1
    #  | 2.500       | HKD      | 85251493020 | 142434 |             | /return3        | message from merchant | random  | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD |

  #partial refund
  @trial @regression @skiponsandbox
  Scenario Outline: Positive flow- A merchant is able to create a payment request and do partial refund
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
    Given I am an authorized user
    When I query for a list of transactions
    Then verify transaction list contains transactionId as retrieved in check status response
    Given I am logging in as a user with refund role
    And I record the first transaction details
    Then I try to make a call to refund with that transaction
    And I enter the refund data with refund amount "<refundamount>", refund currency "<currency>", reason Code "<reasonCode>" and reason message "<reasonMessage>"
    And validate refund response
    Given I am an authorized user
    When I query for transaction details with transactionId retrieved in check status response
    Then I should receive a successful transactionID response
    And validate GET transaction by ID response
    And validate GET transaction list populates in ascending order

    Examples:
      | totalamount | currency | mobileNo    | pin    | environment | notificationURL | description           | orderId | effectiveDuration | appSuccessCallback | appFailCallback | additionalData                                                                | refundamount | reasonCode | reasonMessage  |
      #SIT
      | 1.400       | HKD      | 85282822828 | 142434 |             | /return3        | message from merchant | random  | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | 0.2          | 01         | Incorrect size |

  #POS
  @trial @regression @skiponsandbox
  Scenario Outline: Positive flow- A merchant with POS role is able to post a payment successfully with all the valid inputs
    Given I am an user with POS role
    When I make a request to the Dragon ID Manager
    Then I receive an access_token
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
    Given I am an authorized user
    When I query for transaction details with transactionId retrieved in check status response
    Then I should receive a successful transactionID response
    And validate GET transaction by ID response

    Examples:

      | totalAmount | currency | mobileNo    | pin    | environment | notificationURL | description           | orderId  | effectiveDuration | appSuccessCallback | appFailCallback | additionalData                                                                |
      | 1.400       | HKD      | 85282822828 | 142434 |             | /return3        | message from merchant | B1242183 | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD |


  #Sandbox Transactions Journey
  @trial @regression @skiponmerchant
  Scenario Outline: Positive flow- A merchant successfully receives payment with 1.81 magic number in sandbox and validate transaction details with GET Transaction ID API
    Given I am an authorized user
    And I have payment details "<totalAmount>","<currency>","<notificationURL>","<appSuccessCallback>","<appFailCallback>","<effectiveDuration>"
    When I make a request for the payment
    Then I should receive a successful payment response
    And the response body should contain valid payment request id, business logos, created timestamp, web link, app link, totalAmount, currencyCode, statusDescription, statusCode, effectiveDuration
    And the response body should also have notification URI, app success callback URL, app fail Callback Url if applicable
    And the payment request response should be signed
    And I am an authorized user
    And I have a valid payment id
    When I make a request for the check status for amount "<totalAmount>"
    Then I should receive a successful check status response for amount "<totalAmount>"
    And validate payment status response for amount "<totalAmount>"
    And the response body should contain a list of transactions
    Given I am an authorized user
    When I query for a list of transactions
    Then verify transaction list contains transactionId as retrieved in check status response
    Given I am an authorized user
    When I query for transaction details with transactionId retrieved in check status response
    Then I should receive a successful transaction response
    Given I am an authorized user
    When I query for transaction details with transactionId retrieved in check status response
    Then I should receive a successful transactionID response
    And validate GET transaction by ID response

    Examples:
      | totalAmount | currency | notificationURL                                           | appSuccessCallback | appFailCallback | effectiveDuration |
      #Successful Payment
      | 1.81        | HKD      | https://webhook.site/cb082ee4-bdb8-4ca3-82ba-7d771365e57f | /confirmation1     | /unsuccessful9  | 15                |


  #Valid transaction ID of other merchant
  @trial @regression @skiponsandbox
  Scenario Outline: Negative flow- Merchant should not be able to fetch transaction details of other merchant
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
    Given I am an authorized user
    When I query for transaction details with transactionId retrieved in check status response

    Examples:
      | totalamount | currency | mobileNo    | pin    | environment | notificationURL | description           | orderId | effectiveDuration | appSuccessCallback | appFailCallback | additionalData                                                                | transactionId                        |
      #SIT
      | 1.400       | HKD      | 85282822828 | 142434 |             | /return3        | message from merchant | random  | 60                | /confirmation1     | /unsuccessful9  | pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD | 4dd2f63b-b190-4ad2-b07e-68f66eb572ff |


  @trial @regression
  Scenario Outline: Negative flow - GET Transaction details for invalid transactionId
    Given I am an authorized user
    When I query for a list of transactions with "<transactionId>" transactionId
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorcode in transactionID API response
    And error message should be "<error_message>" within transactionID API response
    Examples:
      | transactionId                        | http_status | error_message                     | error_code | error_description         |
    #valid transactionId of other merchant
      | 8222aee3-c6a2-4553-9a0d-eaadc51eb0ed | 400         |                                   | EB009      | Transaction not found     |
    #invalid transactionId
      | abcd123                              | 400         | Service Request Validation Failed | EA002      | Transaction Id is invalid |


  @trial @regression
  Scenario Outline: Negative flow- Mandatory field Api-Version not sent in the header
    Given I am an authorized user
    When I make request to transactionID endpoint with "<key>" missing in the header
    Then I should receive a "404" status code in transactionID API response
    And error message should be "Resource not found" within transactionID API response
    Examples:
      | key         |
      | Api-Version |


  @trial @regression
  Scenario Outline: Negative flow- Mandatory fields not sent in the header
    Given I am an authorized user
    When I make request to transactionID endpoint with "<key>" missing in the header
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorcode in transactionID API response
    And error message should be "<error_message>" within transactionID API response
    Examples:
      | error_description                                              | error_message                     | key           | error_code | http_status |
      | Error validating JWT                                           | API Gateway Authentication Failed | Authorization | EA001      | 401         |
      | Header Trace-Id was not found in the request. Access denied.   | API Gateway Validation Failed     | Trace-Id      | EA002      | 400         |
      | Header Accept does not contain required value.  Access denied. | Request Header Not Acceptable     | Accept        | EA008      | 406         |
      | Content type '' not supported                                  | Service Request Validation Failed | Content-Type  | EA002      | 415         |

  @trial @regression @invalidt
  Scenario Outline: Negative flow- Invalid mandatory fields provided in header
    Given I am an authorized user
    When I make request to transactionID endpoint with invalid key "<invalidValue>" for "<key>" in header
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorcode in transactionID API response
    And error message should be "<error_message>" within transactionID API response
    Examples:
      | key          | invalidValue                         | http_status | error_message                     | error_code | error_description                                                 |
      | Content-Type | application/json1                    | 415         | Service Request Validation Failed | EA002      | Content type 'application/json1;charset=ISO-8859-1' not supported |
      | Accept       | application/json1                    | 406         | Request Header Not Acceptable     | EA008      | Header Accept does not contain required value.  Access denied.    |
      | Trace-Id     | 12#@!%^&*)                           | 400         | Service Request Validation Failed | EA002      | Trace-Id is invalid                                               |
    #Invalid UUID Trace-Id
      | Trace-Id     | 7454108z-yb37-454c-81da-0a12d8b0f867 | 400         | Service Request Validation Failed | EA002      | Trace-Id is invalid                                               |


  @trial @regression
  Scenario Outline: Negative flow- Invalid mandatory field Api-Version provided in header
    Given I am an authorized user
    When I make request to transactionID endpoint with invalid key "<invalidValue>" for "<key>" in header
    Then I should receive a "<statusCode>" status code in transactionID API response
    And error message should be "<message>" within transactionID API response
    Examples:
      | key         | invalidValue | statusCode | message            |
      | Api-Version | 0.20         | 404        | Resource not found |
      | Api-Version | abc          | 404        | Resource not found |
      | Api-Version | @#$%^        | 404        | Resource not found |


  @trial @regression
  Scenario Outline: Negative flow- Invalid auth token
    Given I am a user with invalid "<auth_token>" auth token without Bearer
    When I query for a list of transactions with "<transactionId>" transactionId
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorcode in transactionID API response
    And error message should be "<error_message>" within transactionID API response
    Examples:
      | transactionId                        | error_description    | error_message                     | auth_token                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             | error_code | http_status |
 #Auth Token missing
      | 27053dcf-c6d9-4d12-baf4-3a4116904e6b | Error validating JWT | API Gateway Authentication Failed |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        | EA001      | 401         |
 # Auth token not a JWT; sent without Bearer
      | 27053dcf-c6d9-4d12-baf4-3a4116904e6b | Error validating JWT | API Gateway Authentication Failed | random_auth_token                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      | EA001      | 401         |
 # Expired auth token
      | 27053dcf-c6d9-4d12-baf4-3a4116904e6b | Error validating JWT | API Gateway Authentication Failed | Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Imk2bEdrM0ZaenhSY1ViMkMzbkVRN3N5SEpsWSIsImtpZCI6Imk2bEdrM0ZaenhSY1ViMkMzbkVRN3N5SEpsWSJ9.eyJhdWQiOiI1MTczYTVhOS00MjEyLTQ4NzctODYyMS03YmMxNjRjZjE3OGIiLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yZmY5M2NjZS1lZmQxLTRlMTYtOTJiYS1hZmI1M2U5ZTA5ZmMvIiwiaWF0IjoxNTM3MTY4NzAzLCJuYmYiOjE1MzcxNjg3MDMsImV4cCI6MTUzNzE3MjYwMywiYWlvIjoiNDJCZ1lKaDlXbFZsNFh1SjZFMHZsMDMvY25TOU5nQT0iLCJhcHBpZCI6IjEwOTMyM2UzLWM1NWUtNDI0Yy1iNzEyLTJlODJmMTY5NWU5OCIsImFwcGlkYWNyIjoiMSIsImlkcCI6Imh0dHBzOi8vc3RzLndpbmRvd3MubmV0LzJmZjkzY2NlLWVmZDEtNGUxNi05MmJhLWFmYjUzZTllMDlmYy8iLCJvaWQiOiI4NjM3MjIwNi1jZWZhLTQ1ZWEtOTM0Ni0xNjNiYmFiYzc3MDYiLCJyb2xlcyI6WyJwYXltZW50UmVxdWVzdCIsInJlZnVuZCIsImRldmVsb3BlciJdLCJzdWIiOiI4NjM3MjIwNi1jZWZhLTQ1ZWEtOTM0Ni0xNjNiYmFiYzc3MDYiLCJ0aWQiOiIyZmY5M2NjZS1lZmQxLTRlMTYtOTJiYS1hZmI1M2U5ZTA5ZmMiLCJ1dGkiOiI3bUd1b0FRMUQwZTFGMks0Sy1ZWEFBIiwidmVyIjoiMS4wIn0.gCfn6QNUjzENuKvVN0bdkWgRwN-1ipovSq5Yb8IjCRfRhZNTFffLP1oIVw_8sPQxTFfV8CBOcQG385n-r_tIWBlMANHdKmpmwmOTz4J08EWzz_SY4zWdvMlF7quCYdrXVIXaKd-PLtO0UDTI7CsSAso7OAOsHBNrn3ITBoR0aMo_lM8X5dJM5fleSHFiJPMYDHpawZuy_BBXC0AUdcVT61NHkdO7sDV4Dc_C12CsShEqMi3Nj-uzr5wYHmPTxi3nk2px-_yiVaAHxxCmE0f7rWdM4BjVB89j_xtZrn1-VovgUUn_QDppY9yOgKx831xXBMX0Tz293V8g7BkagVI3lw | EA001      | 401         |
 # Auth token unverified
      | 27053dcf-c6d9-4d12-baf4-3a4116904e6b | Error validating JWT | API Gateway Authentication Failed | Bearer nbCwW11w3XkB-xUaXwKRSLjMHGQ                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     | EA001      | 401         |

  @trial @regression @skiponversioneleven @skiponversionten
  Scenario Outline: Negative flow- Request Date Time's invalid values set within the header
    Given I am an authorized user
    When I make a request for GET Transactions by ID API with invalid value "<value>" for request date time
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorcode in transactionID API response
    And error message should be "<error_message>" within transactionID API response

    Examples:
      | value                     | error_description                               | error_code | error_message                     | http_status |
      |                           | Request timestamp not a valid RFC3339 date-time | EA002      | Service Request Validation Failed | 400         |
      | xyz                       | Request timestamp not a valid RFC3339 date-time | EA002      | Service Request Validation Failed | 400         |
      | 2019-02-04T00:42:45.237Z  | Request timestamp too old                       | EA002      | Service Request Validation Failed | 400         |
      | 2019-21-10T00:42:45.237Z  | Request timestamp not a valid RFC3339 date-time | EA002      | Service Request Validation Failed | 400         |
      | 2019-10-35T06:54:52.237Z  | Request timestamp not a valid RFC3339 date-time | EA002      | Service Request Validation Failed | 400         |
      #acceptable Request Date Time format
      | 2019-10-21                | Request timestamp too old                       | EA002      | Service Request Validation Failed | 400         |
      | 2019/10/21 T07:04:52.237Z | Request timestamp too old                       | EA002      | Service Request Validation Failed | 400         |
      | 21 Oct 2019 06:49:52      | Request timestamp too old                       | EA002      | Service Request Validation Failed | 400         |

  @trial @regression @skiponversioneleven @skiponversionten
  Scenario Outline: Negative flow- Request Date Time's invalid values set within the header
    Given I am an authorized user
    When I make a request for GET Transactions by ID API with invalid value "<value>" for request date time
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorcode in transactionID API response
    And error message should be "<error_message>" within transactionID API response

    Examples:
      | value                    | error_description                     | error_code | error_message                     | http_status |
      | 2020-10-21T00:42:45.237Z | Request timestamp is future date-time | EA002      | Service Request Validation Failed | 400         |
      | 21 Jan 2020              | Request timestamp is future date-time | EA002      | Service Request Validation Failed | 400         |
      | 2020 October 20          | Request timestamp is future date-time | EA002      | Service Request Validation Failed | 400         |
