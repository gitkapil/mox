Feature: Payment Request API- DRAG-301

Background: Retrieving access Token
Given I am an user
When I make a request to the Dragon ID Manager
Then I recieve an access_token

# For the parametres where values are missing within the table, while creating request, the parameter will not be included at all as a a part of the payload
  @regression    
Scenario Outline: Positive flow- A merchant is able to create a payment request with all the valid inputs
  Given I am an authorized user
  And I have payment details "<totalamount>","<currency>","<notificationURL>","<appSuccessCallback>","<appFailCallback>","<effectiveDuration>"
  And I have shopping cart details
  |sku            |name            |quantity|price |currency |category1 |category2 |category3|
  |pizzapepperoni |pepperoni pizza |1       |60    |HKD       |Pizza    |Meat Pizza|Pepperoni|
  |               |margherita pizza|1       |60    |HKD       |Pizza    |Meat Pizza|Pepperoni|
  |pizzapepperoni |                |1       |60    |HKD       |Pizza    |Meat Pizza|Pepperoni|
  |pizzapepperoni |pepperoni pizza |        |60    |HKD       |Pizza    |Meat Pizza|Pepperoni|
  |pizzapepperoni |pepperoni pizza |1       |      |HKD       |Pizza    |Meat Pizza|Pepperoni|
  |pizzapepperoni |pepperoni pizza |1       |60    |          |Pizza    |Meat Pizza|Pepperoni|
  |pizzapepperoni |pepperoni pizza |1       |60    |HKD       |        |Meat Pizza|Pepperoni|
  |pizzapepperoni |pepperoni pizza |1       |60    |HKD       |Pizza    ||Pepperoni|
  |pizzapepperoni |pepperoni pizza |1       |60    |HKD       |Pizza    |Meat Pizza||
  And I have merchant data "<description>","<orderId>","<additionalData>"
  When I make a request for the payment
  Then I should recieve a successful payment response
  And the response body should contain valid payment request id, created timestamp, web link, app link, totalAmount, currencyCode, statusDescription, statusCode, effectiveDuration
  And the response body should also have notification URI, app success callback URL, app fail Callback Url if applicable

Examples:
|totalamount|currency |notificationURL              |description          |orderId |effectiveDuration |appSuccessCallback               |appFailCallback|additionalData|
|100.00     |HKD      |https://pizzahut.com/return3 |message from merchant|B1242183|60                |https://pizzahut.com/confirmation1|https://pizzahut.com/unsuccessful9|pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD|
#description within Merchant Data missing
|300.12     |HKD      |https://pizzahut.com/return4 |                     |XYZ456  |30                |https://pizzahut.com/confirmation2|https://pizzahut.com/unsuccessful|pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD|
#orderId within Merchant Data missing
|0.01       |HKD      |https://pizzahut.com/return44|message from merchant|        |60                |https://pizzahut.com/confirmation3|https://pizzahut.com/unsuccessful5|pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD|
#effectiveDuration missing
|1          |HKD      |https://pizzahut.com/return12|message from merchant|XYZ123  |                  |https://pizzahut.com/confirmation4|https://pizzahut.com/unsuccessful4|pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD|
#notificationURI missing
|500.00     |HKD      |                             |message from merchant|B1242183|60                |https://pizzahut.com/confirmation5|https://pizzahut.com/unsuccessful1|pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD|
#appSuccessCallback missing
|800.00     |HKD      |https://pizzahut.com/return09|message from merchant|B1242183|60                ||https://pizzahut.com/unsuccessful6|pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD|
#appFailCallback missing
|900.00     |HKD      |https://pizzahut.com/return11|message from merchant|B1242183|60                |https://pizzahut.com/confirmation7||pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD|
#additionalData  within Merchant Data missing
|550.00     |HKD      |https://pizzahut.com/return2 |message from merchant|B1242183|60                |https://pizzahut.com/confirmation8|https://pizzahut.com/unsuccessful0||


  @regression    
Scenario Outline: Positive flow- A merchant is able to create a payment request with all the valid inputs without shopping cart
  Given I am an authorized user
  And I have payment details "<totalamount>","<currency>","<notificationURL>","<appSuccessCallback>","<appFailCallback>","<effectiveDuration>"
  And I have merchant data "<description>","<orderId>","<additionalData>"
  When I make a request for the payment
  Then I should recieve a successful payment response
  And the response body should contain valid payment request id, created timestamp, web link, app link, totalAmount, currencyCode, statusDescription, statusCode, effectiveDuration
  And the response body should also have notification URI, app success callback URL, app fail Callback Url if applicable

Examples:
|totalamount|currency |notificationURL              |description          |orderId |effectiveDuration |appSuccessCallback               |appFailCallback|additionalData|
|100.00     |HKD      |https://pizzahut.com/return45|message from merchant|B1242183|60                |https://pizzahut.com/confirmation5|https://pizzahut.com/unsuccessful1|pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD|
#description within Merchant Data missing
|300.12     |HKD      |https://pizzahut.com/return12|                     |XYZ456  |30                |https://pizzahut.com/confirmation1|https://pizzahut.com/unsuccessful|pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD|
#orderId within Merchant Data missing
|0.01       |HKD      |https://pizzahut.com/return98|message from merchant|        |60                |https://pizzahut.com/confirmation7|https://pizzahut.com/unsuccessful9|pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD|
#effectiveDuration missing
|1          |HKD      |https://pizzahut.com/return42|message from merchant|XYZ123  |                  |https://pizzahut.com/confirmation8|https://pizzahut.com/unsuccessful3|pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD|
#notificationURI missing
|500.00     |HKD      |                             |message from merchant|B1242183|60                |https://pizzahut.com/confirmation0|https://pizzahut.com/unsuccessful2|pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD|
#appSuccessCallback missing
|800.00     |HKD      |https://pizzahut.com/return29|message from merchant|B1242183|60                ||https://pizzahut.com/unsuccessful6|pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD|
#appFailCallback missing
|900.00     |HKD      |https://pizzahut.com/return13|message from merchant|B1242183|60                |https://pizzahut.com/confirmation3||pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD|
#additionalData  within Merchant Data missing
|550.00     |HKD      |https://pizzahut.com/return26|message from merchant|B1242183|60                |https://pizzahut.com/confirmations|https://pizzahut.com/unsuccessfuls||


  @regression    
Scenario: Positive flow- A merchant is able to create a payment request with all the valid inputs without merchant data
  Given I am an authorized user
  And I have valid payment details
  When I make a request for the payment
  Then I should recieve a successful payment response
  And the response body should contain valid payment request id, created timestamp, web link, app link, totalAmount, currencyCode, statusDescription, statusCode, effectiveDuration
  And the response body should also have notification URI, app success callback URL, app fail Callback Url if applicable


# For the parametres where value is "no_value" within the table, while creating request the parameter (key) will be included but will have no value
  @regression    
Scenario Outline: Positive flow- A merchant is able to create a payment request where the non mandatory fields within body have no corresponding values in the payload
  Given I am an authorized user
  And I have payment details "<totalamount>","<currency>","<notificationURL>","<appSuccessCallback>","<appFailCallback>","<effectiveDuration>"
  And I have merchant data "<description>","<orderId>","<additionalData>"
  When I make a request for the payment
  Then I should recieve a successful payment response
  And the response body should contain valid payment request id, created timestamp, web link, app link, totalAmount, currencyCode, statusDescription, statusCode, effectiveDuration
  And the response body should also have notification URI, app success callback URL, app fail Callback Url if applicable

Examples:
|totalamount|currency |notificationURL            |description          |orderId |effectiveDuration |appSuccessCallback|appFailCallback|additionalData|
|100.00     |HKD      |https://pizzahut.com/return|no_value             |B1242183|60                |https://pizzahut.com/confirmation|https://pizzahut.com/unsuccessful|pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD|
|150.00     |HKD      |https://pizzahut.com/return|message from merchant|no_value|60                |https://pizzahut.com/confirmation|https://pizzahut.com/unsuccessful|pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD|
|900000     |HKD      |no_value                   |message from merchant|B1242183|60                |https://pizzahut.com/confirmation|https://pizzahut.com/unsuccessful|pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD|
|900000     |HKD      |https://pizzahut.com/return|message from merchant|B1242183|60                |no_value|https://pizzahut.com/unsuccessful|pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD|
|900000     |HKD      |https://pizzahut.com/return|message from merchant|B1242183|60                |https://pizzahut.com/confirmation|no_value|pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD|
|900000     |HKD      |https://pizzahut.com/return|message from merchant|B1242183|60                |https://pizzahut.com/confirmation|https://pizzahut.com/unsuccessful|no_value|


# For the parametres where value is "no_value" within the table, while creating request the parameter (key) will be included but will have no value
  @regression    
Scenario Outline: Positive flow- A merchant is able to create a payment request where the non mandatory fields within shopping cart have no corresponding values in the payload
  Given I am an authorized user
  And I have payment details "<totalamount>","<currency>","<notificationURL>","<appSuccessCallback>","<appFailCallback>","<effectiveDuration>"
  And I have shopping cart details
    |sku            |name            |quantity|price |currency |category1 |category2 |category3 |
    |no_value       |pepperoni pizza |1       |60    |HKD       |Pizza    |Meat Pizza|Pepperoni|
    |pizzapepperoni |no_value        |1       |60    |HKD       |Pizza    |Meat Pizza|Pepperoni|
    |pizzapepperoni |pepperoni pizza |1       |60    |no_value  |Pizza    |Meat Pizza|Pepperoni|
    |pizzapepperoni |pepperoni pizza |1       |60    |HKD  |no_value    |Meat Pizza|Pepperoni|
    |pizzapepperoni |pepperoni pizza |1       |60    |HKD  |Pizza    |no_value|Pepperoni|
    |pizzapepperoni |pepperoni pizza |1       |60    |HKD  |Pizza    |Meat Pizza|no_value|

  And I have merchant data "<description>","<orderId>","<additionalData>"
  When I make a request for the payment
  Then I should recieve a successful payment response
  And the response body should contain valid payment request id, created timestamp, web link, app link, totalAmount, currencyCode, statusDescription, statusCode, effectiveDuration
  And the response body should also have notification URI, app success callback URL, app fail Callback Url if applicable

Examples:
|totalamount|currency |notificationURL            |description          |orderId |effectiveDuration |appSuccessCallback|appFailCallback|additionalData|
|100.00     |HKD      |https://pizzahut.com/return|message from merchant|B1242183|60                |https://pizzahut.com/confirmation|https://pizzahut.com/unsuccessful|pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD|


  @regression   
Scenario: Negative flow- Invalid auth token (without Bearer in the header)
  Given I am an authorized user
  And I dont send Bearer with the auth token
  And I have valid payment details
  When I make a request for the payment
  Then I should recieve a "401" error response with "JWT not present." error description and "401" errorcode within payment response
  And error message should be "TokenNotPresent" within payment response


  @regression  
Scenario Outline: Negative flow- Mandatory fields not sent in the header
  Given I am an authorized user
  And I have valid payment details
  When I make a request for the payment with "<key>" missing in the header
  Then I should recieve a "<error_code>" error response with "<error_description>" error description and "<error_code>" errorcode within payment response
  And error message should be "<error_message>" within payment response

 Examples:
 |error_description                                                    |error_message  | key             |error_code |
 |Header Authorization was not found in the request. Access denied.    | HeaderNotFound|Authorization    |401        |
 |Header Request-Date-Time was not found in the request. Access denied.| HeaderNotFound|Request-Date-Time|400        |
 |Header Trace-Id was not found in the request. Access denied.         | HeaderNotFound|Trace-Id         |400        |


 @regression  
Scenario Outline: Negative flow- Mandatory fields not sent in the header
  Given I am an authorized user
  And I have valid payment details
  When I make a request for the payment with "<key>" missing in the header
  And error message should be "Resource not found" within payment response

 Examples:
 | key             |
 |Api-Version      |



  @regression  
Scenario Outline: Negative flow- Invalid auth token
  Given I am a merchant with invalid "<auth_token>"
  And I have valid payment details
  When I make a request for the payment
  Then I should recieve a "401" error response with "<error_description>" error description and "401" errorcode within payment response
  And error message should be "<error_message>" within payment response

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


  @regression   @skiponcimerchant @skiponsitmerchant
Scenario Outline: Negative flow- Peak error response parsed by DRAGON
   Given I am an authorized user
   And I have payment details with "<invalid_value>" set for the "<parameter>"
   When I make a request for the payment
   Then I should recieve a "400" error response with "<error_description>" error description and "<error_code>" errorcode within payment response
   And error message should be "<error_message>" within payment response

   Examples:
  |error_description             |error_message          |error_code| parameter      | invalid_value |
  | Payment Amount error_Dynamic | Validation Fail!      |BG2002    | totalamount    | 0             |
  | Payment Amount error_Dynamic | Validation Fail!      |BG2002    | totalamount    | -10           |

  @regression
Scenario Outline: Negative flow- Mandatory fields from the body missing
  Given I am an authorized user
  And I have payment details "<totalamount>","<currency>","<notificationURL>","<appSuccessCallback>","<appFailCallback>","<effectiveDuration>"
  When I make a request for the payment
  Then I should recieve a "400" error response with "<error_description>" error description and "<error_code>" errorcode within payment response
  And error message should be "<error_message>" within payment response


Examples:
|totalamount|currency |notificationURL            |error_description                |error_message|error_code|appSuccessCallback|appFailCallback|effectiveDuration|
|150.00     |         |https://pizzahut.com/return|Service Request Validation Failed|Something went wrong. Sorry, we are unable to perform this action right now. Please try again.|BNA002|https://pizzahut.com/confirmation|https://pizzahut.com/unsuccessful|6|
|           |HKD      |https://pizzahut.com/return|Service Request Validation Failed|Something went wrong. Sorry, we are unable to perform this action right now. Please try again.|BNA002|https://pizzahut.com/confirmation|https://pizzahut.com/unsuccessful|6|


Scenario Outline: Negative flow- TraceId's value missing from the header
   Given I am an authorized user
   And I have valid payment details with no TraceId sent in the header
   When I make a request for the payment
   Then I should recieve a "400" error response with "<error_description>" error description and "<error_code>" errorcode within payment response
   And error message should be "<error_message>" within payment response

   Examples:
  |error_description             |error_message          |error_code|
  | Payment Amount error_Dynamic | Validation Fail!      |BG2002    |


Scenario Outline: Negative flow- Request Date Time's value missing from the header
   Given I am an authorized user
   And I have valid payment details with no Request Date Time sent in the header
   When I make a request for the payment
   Then I should recieve a "400" error response with "<error_description>" error description and "<error_code>" errorcode within payment response
   And error message should be "<error_message>" within payment response

   Examples:
  |error_description             |error_message          |error_code|
  | Payment Amount error_Dynamic | Validation Fail!      |BG2002    |


  @regression   
Scenario Outline: Negative flow- verify Error message if the additionalData is of more than 1024 characters
  Given I am an authorized user
  And I have payment details "<totalamount>","<currency>","<notificationURL>","<appSuccessCallback>","<appFailCallback>","<effectiveDuration>"
  And I have merchant data "<description>","<orderId>","<additionalData>"
  And the additionalData is of more than 1024 characters
  When I make a request for the payment
  Then I should recieve a "400" error response with "Additional Data has too many characters. limit: 1024" error description and "EA002" errorcode within payment response
  And error message should be "Service Request Validation Failed" within payment response

Examples:
|totalamount|currency |notificationURL            |description          |orderId |effectiveDuration |appSuccessCallback|appFailCallback|additionalData|
|100.00     |HKD      |https://pizzahut.com/return|message from merchant|B1242183|60                |https://pizzahut.com/confirmation|https://pizzahut.com/unsuccessful|pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD|

   # Peak errors - same transaction sent twice, random merchant id
   # Manual test cases - peak timeout & peak server down (switch off peak mock), Restrict Caller IPs Policy, large amount
   #                   - different content-type in the header, characters for amount & effective duration
   #                   - integer parameter's value missing from the body
