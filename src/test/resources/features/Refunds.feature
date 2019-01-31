Feature: Refund - DRAG- 179

Background: Retrieving access Token
Given I am an user
When I make a request to the Dragon ID Manager
Then I recieve an access_token

    @skiponcimerchant @skiponsitmerchant
Scenario Outline: Positive flow- A merchant is able to perform refund with all the valid inputs
  Given I am an authorized user
  And I have a "<refundamount>", "<currency>", "<reason>"
  When I make a request for the refund
  Then I should recieve a successful refund response
  And the response body should contain valid refund id, amount, currencyCode, reasonCode, transaction Id
  And the response body should also have reason if applicable

  Examples:
 | refundamount | currency |reason               |
 | 22.99        | HKD      |requested by customer|
 | 0.8          | HKD      |requested by customer|
 | 100          | USD      |123|
 | 500          | HKD      ||
 | 10           | HKD      |no_value|

    @skiponcimerchant @skiponsitmerchant  
Scenario: Negative flow- Invalid auth token (without Bearer in the header)
  Given I am an authorized user
  And I have a valid transaction for refund
  And I dont send Bearer with the auth token in the refund request
  When I make a request for the refund
  Then I should recieve a "401" error response with "JWT not present." error description and "401" errorcode within refund response
  And error message should be "TokenNotPresent" within refund response


    @skiponcimerchant @skiponsitmerchant  
Scenario Outline: Negative flow- Mandatory fields not sent in the header
  Given I am an authorized user
  And I have a valid transaction for refund
  When I make a request for the refund with "<key>" missing in the header
  Then I should recieve a "<error_code>" error response with "<error_description>" error description and "<error_code>" errorcode within refund response
  And error message should be "<error_message>" within refund response

 Examples:
 |error_description                                                    |error_message         | key             |error_code |
 |Header Authorization was not found in the request. Access denied.    | HeaderNotFound       |Authorization    |EA001      |
 |Header Accept does not contain required value. Access denied.        | HeaderValueNotAllowed|Accept           |400        |
 |Header Request-Date-Time was not found in the request. Access denied.| HeaderNotFound       |Request-Date-Time|400        |
 |Header Trace-Id was not found in the request. Access denied.         | HeaderNotFound       |Trace-Id         |EA002      |


    @skiponcimerchant @skiponsitmerchant  
Scenario Outline: Negative flow- Mandatory fields not sent in the body
  Given I am an authorized user
  And I have a valid transaction for refund
  When I make a request for the refund with "<key>" missing in the body
  Then I should recieve a "400" error response with "<error_description>" error description and "EA002" errorcode within refund response
  And error message should be "Service Request Validation Failed" within refund response

 Examples:
 |error_description   | key         |
 |amount missing      | amount      |
 |currencyCode missing| currencyCode|


    @skiponcimerchant @skiponsitmerchant 
Scenario Outline: Negative flow- Mandatory fields not sent in the body
  Given I am an authorized user
  And I have a "<refundamount>", "<currency>", "<reason>"
  When I make a request for the refund
  Then I should recieve a "400" error response with "<error_description>" error description and "EA002" errorcode within refund response
  And error message should be "Service Request Validation Failed" within refund response

 Examples:
 | refundamount | currency |reason               |error_description   |
 | 22.99        | no_value |requested by customer|currencyCode missing|

     @skiponcimerchant @skiponsitmerchant  
 Scenario Outline: Negative flow- Mandatory fields not sent in the header
   Given I am an authorized user
   And I have a valid transaction for refund
   When I make a request for the refund with "<key>" missing in the header
   Then error message should be "Resource not found" within refund response

  Examples:
  | key       |
  |Api-Version|


    @skiponcimerchant @skiponsitmerchant  
Scenario Outline: Negative flow- Invalid auth token
  Given I am an authorized user
  And I have a valid transaction for refund
  And I send invalid auth token "<auth_token>" in the refund request
  When I make a request for the refund
  Then I should recieve a "401" error response with "<error_description>" error description and "401" errorcode within refund response
  And error message should be "<error_message>" within refund response

 Examples:
 |error_description           |error_message          |auth_token|
 #Auth Token missing
 |JWT not present.            |TokenNotPresent        ||
 # Auth token not a JWT
 |JWT is not well formed      |Invalid JWT            |random_auth_token|
 # Expired auth token
 |Lifetime validation failed. The token is expired|TokenExpired|eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Imk2bEdrM0ZaenhSY1ViMkMzbkVRN3N5SEpsWSIsImtpZCI6Imk2bEdrM0ZaenhSY1ViMkMzbkVRN3N5SEpsWSJ9.eyJhdWQiOiI1MTczYTVhOS00MjEyLTQ4NzctODYyMS03YmMxNjRjZjE3OGIiLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yZmY5M2NjZS1lZmQxLTRlMTYtOTJiYS1hZmI1M2U5ZTA5ZmMvIiwiaWF0IjoxNTM3MTY4NzAzLCJuYmYiOjE1MzcxNjg3MDMsImV4cCI6MTUzNzE3MjYwMywiYWlvIjoiNDJCZ1lKaDlXbFZsNFh1SjZFMHZsMDMvY25TOU5nQT0iLCJhcHBpZCI6IjEwOTMyM2UzLWM1NWUtNDI0Yy1iNzEyLTJlODJmMTY5NWU5OCIsImFwcGlkYWNyIjoiMSIsImlkcCI6Imh0dHBzOi8vc3RzLndpbmRvd3MubmV0LzJmZjkzY2NlLWVmZDEtNGUxNi05MmJhLWFmYjUzZTllMDlmYy8iLCJvaWQiOiI4NjM3MjIwNi1jZWZhLTQ1ZWEtOTM0Ni0xNjNiYmFiYzc3MDYiLCJyb2xlcyI6WyJwYXltZW50UmVxdWVzdCIsInJlZnVuZCIsImRldmVsb3BlciJdLCJzdWIiOiI4NjM3MjIwNi1jZWZhLTQ1ZWEtOTM0Ni0xNjNiYmFiYzc3MDYiLCJ0aWQiOiIyZmY5M2NjZS1lZmQxLTRlMTYtOTJiYS1hZmI1M2U5ZTA5ZmMiLCJ1dGkiOiI3bUd1b0FRMUQwZTFGMks0Sy1ZWEFBIiwidmVyIjoiMS4wIn0.gCfn6QNUjzENuKvVN0bdkWgRwN-1ipovSq5Yb8IjCRfRhZNTFffLP1oIVw_8sPQxTFfV8CBOcQG385n-r_tIWBlMANHdKmpmwmOTz4J08EWzz_SY4zWdvMlF7quCYdrXVIXaKd-PLtO0UDTI7CsSAso7OAOsHBNrn3ITBoR0aMo_lM8X5dJM5fleSHFiJPMYDHpawZuy_BBXC0AUdcVT61NHkdO7sDV4Dc_C12CsShEqMi3Nj-uzr5wYHmPTxi3nk2px-_yiVaAHxxCmE0f7rWdM4BjVB89j_xtZrn1-VovgUUn_QDppY9yOgKx831xXBMX0Tz293V8g7BkagVI3lw|
 # Auth token unverified
 |Signature validation failed |TokenInvalidSignature  |eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c|


#wallet balance= 2000 HKD and total amount within the transaction = 1000HKD set within the emulator
    @skiponcimerchant @skiponsitmerchant 
Scenario Outline: Negative flow- Invalid refund amount sent in the request (error responses set within emulator)
  Given I am an authorized user
  And I have a "<refundamount>", "HKD", "customer initiated"
  When I make a request for the refund
  Then I should recieve a "400" error response with "<error_description>" error description and "<error_code>" errorcode within refund response
  And error message should be "<error_message>" within refund response

 Examples:
 |error_description                    |error_message               |error_code  |refundamount |
# |Refund amount > net refundable amount| Business Rules Incorrect!  |BG2009      | 1500        |
# |amount > wallet balance              | Business Rules Incorrect!  |BG2026      | 2200        |
 |Amount must be positive              | Business Rules Incorrect!  |BG2002      | -100        |
 |Amount must be positive              | Business Rules Incorrect!  |BG2002      | 0           |
 |amount > wallet balance              | Business Rules Incorrect!  |BG2026      | 1500        |
 |Refund amount > net refundable amount| Business Rules Incorrect!  |BG2009      | 2200        |


   @skiponcimerchant @skiponsitmerchant 
Scenario Outline: Negative flow- Request Date Time's invalid values set within the header
   Given I am an authorized user
   And I have a valid transaction for refund
   And I send invalid value "<value>" for the request date time in the refund request
   When I make a request for the refund
   Then I should recieve a "400" error response with "Service Request Validation Failed" error description and "BNA002" errorcode within refund response
   And error message should be "Something went wrong. Sorry, we are unable to perform this action right now. Please try again." within refund response

   Examples:
  |value|
  ||
  |xyz|