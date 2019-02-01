Feature: Check Status - DRAG- 178

Background: Retrieving access Token
Given I am an user
When I make a request to the Dragon ID Manager
Then I recieve an access_token

  @regression
Scenario: Positive flow- A merchant is able to create a check status request with all the valid inputs
  Given I am an authorized user
  And I have valid payment details
  And I make a request for the payment
  And I should recieve a successful payment response
  And I have a valid payment id
  When I make a request for the check status
  Then I should recieve a successful check status response
  And the response body should contain valid payment request id, created timestamp, totalAmount, currencyCode, statusDescription, statusCode, effectiveDuration within check status response
  And the response body should also have app success callback URL, app fail Callback Url if applicable within check status response
  And the payment status response should be signed


  @regression   
Scenario: Negative flow- Invalid auth token (without Bearer in the header)
  Given I am an authorized user
  And I have valid payment details
  And I make a request for the payment
  And I should recieve a successful payment response
  And I have a valid payment id
  And I dont send Bearer with the auth token in the check status request
  When I make a request for the check status
  Then I should recieve a "401" error response with "JWT not present." error description and "EA001" errorcode within check status response
  And error message should be "Service Request Authentication Failed" within check status response
  And the payment status response should be signed


  @regression   
Scenario Outline: Negative flow- Mandatory fields not sent in the header
  Given I am an authorized user
  And I have valid payment details
  And I make a request for the payment
  And I should recieve a successful payment response
  And I have a valid payment id
  When I make a request for the payment status with "<key>" missing in the header
  Then I should recieve a "<response_code>" error response with "<error_description>" error description and "<error_code>" errorcode within check status response
  And error message should be "<error_message>" within check status response
  And the payment status response should be signed

 Examples:
 |error_description                                                    |error_message         | key             |response_code |error_code  |
 |Header Authorization was not found in the request. Access denied.    | HeaderNotFound       |Authorization    |401           |401         |
 |Header Request-Date-Time was not found in the request. Access denied.| HeaderNotFound       |Request-Date-Time|400           |400         |
 |Header Trace-Id was not found in the request. Access denied.         | HeaderNotFound       |Trace-Id         |400           |400         |
 |Header Accept does not contain required value.  Access denied.       | Request Header Not Acceptable|Accept   |406           |EA008       |


Scenario Outline: Negative flow- Mandatory fields not sent in the header
  Given I am an authorized user
  And I have valid payment details
  And I make a request for the payment
  And I should recieve a successful payment response
  And I have a valid payment id
  When I make a request for the payment status with "<key>" missing in the header
  Then error message should be "Resource not found" within check status response
  And the payment status response should be signed

 Examples:
 | key       |
 |Api-Version|

  @regression   
Scenario Outline: Negative flow- Invalid auth token
  Given I am an authorized user
  And I have valid payment details
  When I make a request for the payment
  And I should recieve a successful payment response
  And I have a valid payment id
  And I send invalid auth token "<auth_token>" in the check status request
  When I make a request for the check status
  Then I should recieve a "401" error response with "<error_description>" error description and "<error_code>" errorcode within check status response
  And error message should be "<error_message>" within check status response
  And the payment status response should be signed
# DRAG-1127 Updated the error description and the error_message based on DRAG-1130
 Examples:
 |error_description           |error_message          |auth_token|error_code|
 #Auth Token missing
 |Error validating JWT        |API Gateway Authentication Failed||EA001|
 # Auth token not a JWT
 |Error validating JWT        |API Gateway Authentication Failed |random_auth_token|EA001|
 # Expired auth token
 |Error validating JWT        |API Gateway Authentication Failed |eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Imk2bEdrM0ZaenhSY1ViMkMzbkVRN3N5SEpsWSIsImtpZCI6Imk2bEdrM0ZaenhSY1ViMkMzbkVRN3N5SEpsWSJ9.eyJhdWQiOiI1MTczYTVhOS00MjEyLTQ4NzctODYyMS03YmMxNjRjZjE3OGIiLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yZmY5M2NjZS1lZmQxLTRlMTYtOTJiYS1hZmI1M2U5ZTA5ZmMvIiwiaWF0IjoxNTM3MTY4NzAzLCJuYmYiOjE1MzcxNjg3MDMsImV4cCI6MTUzNzE3MjYwMywiYWlvIjoiNDJCZ1lKaDlXbFZsNFh1SjZFMHZsMDMvY25TOU5nQT0iLCJhcHBpZCI6IjEwOTMyM2UzLWM1NWUtNDI0Yy1iNzEyLTJlODJmMTY5NWU5OCIsImFwcGlkYWNyIjoiMSIsImlkcCI6Imh0dHBzOi8vc3RzLndpbmRvd3MubmV0LzJmZjkzY2NlLWVmZDEtNGUxNi05MmJhLWFmYjUzZTllMDlmYy8iLCJvaWQiOiI4NjM3MjIwNi1jZWZhLTQ1ZWEtOTM0Ni0xNjNiYmFiYzc3MDYiLCJyb2xlcyI6WyJwYXltZW50UmVxdWVzdCIsInJlZnVuZCIsImRldmVsb3BlciJdLCJzdWIiOiI4NjM3MjIwNi1jZWZhLTQ1ZWEtOTM0Ni0xNjNiYmFiYzc3MDYiLCJ0aWQiOiIyZmY5M2NjZS1lZmQxLTRlMTYtOTJiYS1hZmI1M2U5ZTA5ZmMiLCJ1dGkiOiI3bUd1b0FRMUQwZTFGMks0Sy1ZWEFBIiwidmVyIjoiMS4wIn0.gCfn6QNUjzENuKvVN0bdkWgRwN-1ipovSq5Yb8IjCRfRhZNTFffLP1oIVw_8sPQxTFfV8CBOcQG385n-r_tIWBlMANHdKmpmwmOTz4J08EWzz_SY4zWdvMlF7quCYdrXVIXaKd-PLtO0UDTI7CsSAso7OAOsHBNrn3ITBoR0aMo_lM8X5dJM5fleSHFiJPMYDHpawZuy_BBXC0AUdcVT61NHkdO7sDV4Dc_C12CsShEqMi3Nj-uzr5wYHmPTxi3nk2px-_yiVaAHxxCmE0f7rWdM4BjVB89j_xtZrn1-VovgUUn_QDppY9yOgKx831xXBMX0Tz293V8g7BkagVI3lw|EA001|
 # Auth token unverified
 |Error validating JWT        |API Gateway Authentication Failed  |eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c|401|


  @regression @skiponsitmerchant
Scenario Outline: Negative flow- Invalid PaymentIds sent in the request
  Given I am an authorized user
  And I have valid payment details
  And I make a request for the payment
  And I should recieve a successful payment response
  And I have a payment id "<payment_id>"
  When I make a request for the check status
  Then I should recieve a "<response_code>" error response with "<error_description>" error description and "<error_code>" errorcode within check status response
  And error message should be "<error_message>" within check status response
  And the payment status response should be signed

 Examples:
 |error_description             |error_message                     | payment_id                          |error_code |response_code|
 #|null                         | Resource not found               |                                     |null       |404|
 |QR code not found when scans  | Resource Not Found!              | 591ec407-401d-40a6-9db0-b48a35fad8a3|CF2003     |404|
 |Payment Request Id is invalid | Service Request Validation Failed| random_payment_id                   |EA002      |400|


 @regression  @skiponsitmerchant
Scenario Outline: Emulator Scenarios
  Given I am an authorized user
  And I have a payment id "<payment_id>"
  When I make a request for the check status
  Then I should recieve a successful check status response
  And the response body should contain correct "<status_description>" and "<status_code>"
  And the payment status response should be signed

  Examples:
  |payment_id                           | status_description          | status_code|
  |25f90d96-4052-4c47-8ec1-f818c0e7a212 |Payment Request Expired      |PR007       |
  |b15e090a-5e97-4b44-a67e-542eb2aa0f4d |Request for Payment Initiated|PR001       |
  |9dbcf291-d71e-4c9f-938c-1fdf4035b5f5 |Payment Success              |PR005       |
  |b15e090a-5e97-4b44-a67e-542eb2aa0f4d |Request for Payment Initiated|PR001       |
  |33f8b91c-82ed-4bb5-a771-40daa14a5d3e |Payment Success              |PR005       |
  |839040ff-128f-47ec-b69a-d44ae05aae80 |Payment Success              |PR005       |


@regression
Scenario Outline: Negative flow- Request Date Time's invalid values set within the header
   Given I am an authorized user
   And I have valid payment details
   When I make a request for the payment
   And I should recieve a successful payment response
   And I have a valid payment id
   When I make a request for the check status with invalid value for request date time "<value>"
   Then I should recieve a "400" error response with "Service Request Validation Failed" error description and "BNA002" errorcode within check status response
   And error message should be "Something went wrong. Sorry, we are unable to perform this action right now. Please try again." within check status response
   And the payment status response should be signed

   Examples:
  |value|
  ||
  |xyz|

#manual Test case: E2E after completing the payment through Payme/ Peak