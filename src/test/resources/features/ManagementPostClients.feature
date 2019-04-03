Feature: Management Post Clients API - DRAG-1416

  Background: Retrieving access Token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

  # For the parametres where values are missing within the table, while creating request, the parameter will not be included at all as a a part of the payload
  @regression
  Scenario Outline: Positive flow- A CSO user is able to create an application
    Given I am an authorized CSO user
    And I have a "<clientId>" from an existing AAD application
    And I have a "<peakId>", "<subUnitId>" and "<organisationId>" from an existing PM4B merchant identity
    When I make a POST request to the application endpoint
    Then I should receive a successful applications response
    And the response body should contain a valid applicationId, clientId, peakId, subUnitId and organisationId
    And the response body should also have empty notificationPath and empty notificationHost
    And the POST client request response should be signed

    Examples:
      |clientId                            |peakId                              |subUnitId                           |organisationId                      |
      |3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|

  # For the parametres where value is "no_value" within the table, while creating request the parameter (key) will be included but will have no value
#  @regression
#  Scenario Outline: Positive flow- A merchant is able to create a payment request where the non mandatory fields within shopping cart have no corresponding values in the payload
#    Given I am an authorized user
#    And I have payment details "<totalamount>","<currency>","<notificationURL>","<appSuccessCallback>","<appFailCallback>","<effectiveDuration>"
#    And I have shopping cart details
#      |sku            |name            |quantity|price |currency |category1 |category2 |category3 |
#      |no_value       |pepperoni pizza |1       |60    |HKD       |Pizza    |Meat Pizza|Pepperoni|
#      |pizzapepperoni |no_value        |1       |60    |HKD       |Pizza    |Meat Pizza|Pepperoni|
#      |pizzapepperoni |pepperoni pizza |1       |60    |no_value  |Pizza    |Meat Pizza|Pepperoni|
#      |pizzapepperoni |pepperoni pizza |1       |60    |HKD  |no_value    |Meat Pizza|Pepperoni|
#      |pizzapepperoni |pepperoni pizza |1       |60    |HKD  |Pizza    |no_value|Pepperoni|
#      |pizzapepperoni |pepperoni pizza |1       |60    |HKD  |Pizza    |Meat Pizza|no_value|
#
#    And I have merchant data "<description>","<orderId>","<additionalData>"
#    When I make a request for the payment
#    Then I should receive a successful payment response
#    And the response body should contain valid payment request id, created timestamp, web link, app link, totalAmount, currencyCode, statusDescription, statusCode, effectiveDuration
#    And the response body should also have notification URI, app success callback URL, app fail Callback Url if applicable
#    And the payment request response should be signed
#
#    Examples:
#      |totalamount|currency |notificationURL            |description          |orderId |effectiveDuration |appSuccessCallback|appFailCallback|additionalData|
#      |100.00     |HKD      |/return                    |message from merchant|B1242183|60                |/confirmation|/unsuccessful|pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD|


  @regression
  Scenario: Negative flow- Invalid auth token (without Bearer in the header)
    Given I am an authorized user
    And I dont send Bearer with the auth token
    And I have valid client details
    When I make a POST request to the application endpoint
    Then I should receive a "401" error response with "Error validating JWT" error description and "EA001" errorcode within the POST application response
    And error message should be "API Gateway Authentication Failed" within the POST application response
  #And the payment request response should be signed

#DRAG-1157 - Please update the correct error_message for the signature in the examples.
  @regression
  Scenario Outline: Negative flow- Mandatory fields not sent in the header
    Given I am an authorized user
    And I have valid client details
    When I make a POST request to the application endpoint with "<key>" missing in the header
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorcode within the POST application response
    And error message should be "<error_message>" within the POST application response
  #And the payment request response should be signed

    Examples:
      |error_description                                                     |error_message                     | key             |error_code |http_status|
      |Error validating JWT                                                  | API Gateway Authentication Failed|Authorization    |EA001      |401        |
      | Header Request-Date-Time was not found in the request. Access denied. | API Gateway Validation Failed | Request-Date-Time | EA002 | 400 |
      | Header Trace-Id was not found in the request. Access denied. | API Gateway Validation Failed | Trace-Id | EA002 | 400 |
      | Header Signature was not found in the request. Access denied. | API Gateway Validation Failed | Signature | EA002 | 400 |
      |Header Accept does not contain required value.  Access denied.        | Request Header Not Acceptable    |Accept           |EA008      |406        |

  @regression
  Scenario Outline: Negative flow- Mandatory fields not sent in the header
    Given I am an authorized user
    And I have valid application details
    When I make a POST request to the application endpoint with "<key>" missing in the header
    And error message should be "Resource not found" within the POST application response
  #And the payment request response should be signed

    Examples:
      | key             |
      |Api-Version      |



  @regression
  Scenario Outline: Negative flow- Invalid auth token
    Given I am a merchant with invalid "<auth_token>"
    And I have valid application details
    When I make a POST request to the application endpoint
    Then I should receive a "401" error response with "<error_description>" error description and "<error_code>" errorcode within the POST application response
    And error message should be "<error_message>" within the POST application response
    Examples:
      |error_description           |error_message          |auth_token|error_code|
 #Auth Token missing
      |Error validating JWT        |API Gateway Authentication Failed  ||EA001|
 # Auth token not a JWT
      |Error validating JWT        |API Gateway Authentication Failed  |random_auth_token|EA001|
 # Expired auth token
      |Error validating JWT        |API Gateway Authentication Failed   |eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Imk2bEdrM0ZaenhSY1ViMkMzbkVRN3N5SEpsWSIsImtpZCI6Imk2bEdrM0ZaenhSY1ViMkMzbkVRN3N5SEpsWSJ9.eyJhdWQiOiI1MTczYTVhOS00MjEyLTQ4NzctODYyMS03YmMxNjRjZjE3OGIiLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yZmY5M2NjZS1lZmQxLTRlMTYtOTJiYS1hZmI1M2U5ZTA5ZmMvIiwiaWF0IjoxNTM3MTY4NzAzLCJuYmYiOjE1MzcxNjg3MDMsImV4cCI6MTUzNzE3MjYwMywiYWlvIjoiNDJCZ1lKaDlXbFZsNFh1SjZFMHZsMDMvY25TOU5nQT0iLCJhcHBpZCI6IjEwOTMyM2UzLWM1NWUtNDI0Yy1iNzEyLTJlODJmMTY5NWU5OCIsImFwcGlkYWNyIjoiMSIsImlkcCI6Imh0dHBzOi8vc3RzLndpbmRvd3MubmV0LzJmZjkzY2NlLWVmZDEtNGUxNi05MmJhLWFmYjUzZTllMDlmYy8iLCJvaWQiOiI4NjM3MjIwNi1jZWZhLTQ1ZWEtOTM0Ni0xNjNiYmFiYzc3MDYiLCJyb2xlcyI6WyJwYXltZW50UmVxdWVzdCIsInJlZnVuZCIsImRldmVsb3BlciJdLCJzdWIiOiI4NjM3MjIwNi1jZWZhLTQ1ZWEtOTM0Ni0xNjNiYmFiYzc3MDYiLCJ0aWQiOiIyZmY5M2NjZS1lZmQxLTRlMTYtOTJiYS1hZmI1M2U5ZTA5ZmMiLCJ1dGkiOiI3bUd1b0FRMUQwZTFGMks0Sy1ZWEFBIiwidmVyIjoiMS4wIn0.gCfn6QNUjzENuKvVN0bdkWgRwN-1ipovSq5Yb8IjCRfRhZNTFffLP1oIVw_8sPQxTFfV8CBOcQG385n-r_tIWBlMANHdKmpmwmOTz4J08EWzz_SY4zWdvMlF7quCYdrXVIXaKd-PLtO0UDTI7CsSAso7OAOsHBNrn3ITBoR0aMo_lM8X5dJM5fleSHFiJPMYDHpawZuy_BBXC0AUdcVT61NHkdO7sDV4Dc_C12CsShEqMi3Nj-uzr5wYHmPTxi3nk2px-_yiVaAHxxCmE0f7rWdM4BjVB89j_xtZrn1-VovgUUn_QDppY9yOgKx831xXBMX0Tz293V8g7BkagVI3lw|EA001|
 # Auth token unverified
      |Error validating JWT        |API Gateway Authentication Failed |nbCwW11w3XkB-xUaXwKRSLjMHGQ|EA001|


  # TODO
  @regression
  Scenario Outline: Negative flow- Mandatory fields from the body missing or invalid
    Given I am an authorized CSO user
    And I have a "<clientId>" from an existing AAD application
    And I have a "<peakId>", "<subUnitId>" and "<organisationId>" from an existing PM4B merchant identity
    When I make a POST request to the application endpoint
    Then I should receive a "400" error response with "<error_description>" error description and "<error_code>" errorcode within the POST application response
    And error message should be "<error_message>" within the POST application response
    Examples:
      |clientId                            |peakId                              |subUnitId                           |organisationId                      |error_description                                                                                                                                         |error_message                    |error_code|
      |no_value                            |3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|Field error in object 'paymentRequestInputModel': field 'currencyCode' may not be null; rejected value [null]                                             |Service Request Validation Failed|EA014     |
      |3fa85f64-5717-4562-b3fc-2c963f66afa6|no_value                            |3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|Field error in object 'paymentRequestInputModel': field 'currencyCode' may not be null; rejected value [null]                                             |Service Request Validation Failed|EA014     |
      |3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|no_value                            |3fa85f64-5717-4562-b3fc-2c963f66afa6|Field error in object 'paymentRequestInputModel': field 'currencyCode' may not be null; rejected value [null]                                             |Service Request Validation Failed|EA014     |
      |3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|no_value                            |Field error in object 'paymentRequestInputModel': field 'currencyCode' may not be null; rejected value [null]                                             |Service Request Validation Failed|EA014     |
      |not a UUID                          |3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|Field error in object 'paymentRequestInputModel': field 'currencyCode' may not be null; rejected value [null]                                             |Service Request Validation Failed|EA014     |
      |3fa85f64-5717-4562-b3fc-2c963f66afa6|not a UUID                          |3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|Field error in object 'paymentRequestInputModel': field 'currencyCode' may not be null; rejected value [null]                                             |Service Request Validation Failed|EA014     |
      |3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|not a UUID                          |3fa85f64-5717-4562-b3fc-2c963f66afa6|Field error in object 'paymentRequestInputModel': field 'currencyCode' may not be null; rejected value [null]                                             |Service Request Validation Failed|EA014     |
      |3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|not a UUID                          |Field error in object 'paymentRequestInputModel': field 'currencyCode' may not be null; rejected value [null]                                             |Service Request Validation Failed|EA014     |



  Scenario Outline: Negative flow- TraceId's value missing from the header
    Given I am an authorized user
    And I have valid application details with no TraceId value sent in the header
    When I make a POST request to the application endpoint
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorcode within the POST application response
    And error message should be "<error_message>" within the POST application response

    Examples:
      | error_description                  | error_message                                         | error_code |http_status|
      |Trace-Id can not be empty.          | Service Request Validation Failed                     |EA002       |400        |


  @regression
  Scenario Outline: Negative flow- Request Date Time's invalid values set within the header
    Given I am an authorized user
    And I have valid application details with invalid value "<value>" set for Request Date Time sent in the header
    When I make a POST request to the application endpoint
    Then I should receive a "400" error response with "<error_description>" error description and "EA002" errorcode within the POST application response
    And error message should be "Service Request Validation Failed" within the POST application response

    Examples:
      |value|error_description|
      |  | Request timestamp not a valid RFC3339 date-time |
      | xyz | Request timestamp not a valid RFC3339 date-time |
      | 2019-02-04T00:42:45.237Z | Request timestamp too old |
