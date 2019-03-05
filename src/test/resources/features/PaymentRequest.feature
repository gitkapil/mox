Feature: Payment Request API- DRAG-301

Background: Retrieving access Token
Given I am an user
When I make a request to the Dragon ID Manager
Then I receive an access_token

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
  Then I should receive a successful payment response
  And the response body should contain valid payment request id, created timestamp, web link, app link, totalAmount, currencyCode, statusDescription, statusCode, effectiveDuration
  And the response body should also have notification URI, app success callback URL, app fail Callback Url if applicable
  And the payment request response should be signed

Examples:
|totalamount|currency |notificationURL              |description          |orderId |effectiveDuration |appSuccessCallback |appFailCallback|additionalData|
|100.00     |HKD      |/return3                     |message from merchant|B1242183|60                |/confirmation1     |/unsuccessful9|pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD|
#description within Merchant Data missing
|300.12     |HKD      |/return4                     |                     |XYZ456  |30                |/confirmation2     |/unsuccessful|pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD|
#orderId within Merchant Data missing
|0.01       |HKD      |/return44                    |message from merchant|        |60                |/confirmation3     |/unsuccessful5|pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD|
#effectiveDuration missing
|1          |HKD      |/return12                    |message from merchant|XYZ123  |30                |/confirmation4     |/unsuccessful4|pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD|
#notificationURI missing
|500.00     |HKD      |                             |message from merchant|B1242183|60                |/confirmation5     |/unsuccessful1|pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD|
#appSuccessCallback missing
|800.00     |HKD      |/return09                    |message from merchant|B1242183|60                ||/unsuccessful6    |pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD|
#appFailCallback missing
|900.00     |HKD      |/return11                    |message from merchant|B1242183|60                |/confirmation7     ||pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD|
#additionalData  within Merchant Data missing
|550.00     |HKD      |/return2                     |message from merchant|B1242183|60                |/confirmation8     |/unsuccessful0||


  @regression    
Scenario Outline: Positive flow- A merchant is able to create a payment request with all the valid inputs without shopping cart
  Given I am an authorized user
  And I have payment details "<totalamount>","<currency>","<notificationURL>","<appSuccessCallback>","<appFailCallback>","<effectiveDuration>"
  And I have merchant data "<description>","<orderId>","<additionalData>"
  When I make a request for the payment
  Then I should receive a successful payment response
  And the response body should contain valid payment request id, created timestamp, web link, app link, totalAmount, currencyCode, statusDescription, statusCode, effectiveDuration
  And the response body should also have notification URI, app success callback URL, app fail Callback Url if applicable
  And the payment request response should be signed

Examples:
|totalamount|currency|notificationURL|description          |orderId |effectiveDuration|appSuccessCallback|appFailCallback|additionalData|
|100.00     |HKD     |/return45      |message from merchant|B1242183|60               |/confirmation5    |/unsuccessful1 |pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD|
#description within Merchant Data missing
|300.12     |HKD     |/return12      |null                 |XYZ456  |30               |/confirmation1    |/unsuccessful  |pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD|
#orderId within Merchant Data missing
|0.01       |HKD     |/return98      |message from merchant|        |60               |/confirmation7    |/unsuccessful9 |pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD|
#effectiveDuration missing
|1          |HKD     |/return42      |message from merchant|XYZ123  |20               |/confirmation8    |/unsuccessful3 |pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD|
#notificationURI missing
|500.00     |HKD     |               |message from merchant|B1242183|60               |/confirmation0    |/unsuccessful2 |pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD|
#appSuccessCallback missing
|800.00     |HKD     |/return29      |message from merchant|B1242183|60               |                  |/unsuccessful6 |pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD|
#appFailCallback missing
|900.00     |HKD     |/return13      |message from merchant|B1242183|60               |/confirmation3    |               |pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD|
#additionalData  within Merchant Data missing
|550.00     |HKD     |/return26      |message from merchant|B1242183|60               |/confirmations    |/unsuccessfuls |                                                                             |


  @regression    
Scenario: Positive flow- A merchant is able to create a payment request with all the valid inputs without merchant data
  Given I am an authorized user
  And I have valid payment details
  When I make a request for the payment
  Then I should receive a successful payment response
  And the response body should contain valid payment request id, created timestamp, web link, app link, totalAmount, currencyCode, statusDescription, statusCode, effectiveDuration
  And the response body should also have notification URI, app success callback URL, app fail Callback Url if applicable
  And the payment request response should be signed


# For the parametres where value is "no_value" within the table, while creating request the parameter (key) will be included but will have no value
  @regression    
Scenario Outline: Positive flow- A merchant is able to create a payment request where the non mandatory fields within body have no corresponding values in the payload
  Given I am an authorized user
  And I have payment details "<totalamount>","<currency>","<notificationURL>","<appSuccessCallback>","<appFailCallback>","<effectiveDuration>"
  And I have merchant data "<description>","<orderId>","<additionalData>"
  When I make a request for the payment
  Then I should receive a successful payment response
  And the response body should contain valid payment request id, created timestamp, web link, app link, totalAmount, currencyCode, statusDescription, statusCode, effectiveDuration
  And the response body should also have notification URI, app success callback URL, app fail Callback Url if applicable
  And the payment request response should be signed

Examples:
|totalamount|currency |notificationURL   |description          |orderId |effectiveDuration |appSuccessCallback|appFailCallback|additionalData|
|100.00     |HKD      |/return           |no_value             |B1242183|60                |/confirmation|/unsuccessful|pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD|
|150.00     |HKD      |/return           |message from merchant|no_value|60                |/confirmation|/unsuccessful|pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD|
|900        |HKD      |no_value          |message from merchant|B1242183|60                |/confirmation|/unsuccessful|pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD|
|133        |HKD      |/return           |message from merchant|B1242183|60                |no_value|/unsuccessful|pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD|
|876        |HKD      |/return           |message from merchant|B1242183|60                |/confirmation|no_value|pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD|
|77         |HKD      |/return           |message from merchant|B1242183|60                |/confirmation|/unsuccessful|no_value|


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
  Then I should receive a successful payment response
  And the response body should contain valid payment request id, created timestamp, web link, app link, totalAmount, currencyCode, statusDescription, statusCode, effectiveDuration
  And the response body should also have notification URI, app success callback URL, app fail Callback Url if applicable
  And the payment request response should be signed

Examples:
|totalamount|currency |notificationURL            |description          |orderId |effectiveDuration |appSuccessCallback|appFailCallback|additionalData|
|100.00     |HKD      |/return                    |message from merchant|B1242183|60                |/confirmation|/unsuccessful|pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD|


  @regression
Scenario: Negative flow- Invalid auth token (without Bearer in the header)
  Given I am an authorized user
  And I dont send Bearer with the auth token
  And I have valid payment details
  When I make a request for the payment
  Then I should receive a "401" error response with "Error validating JWT" error description and "EA001" errorcode within payment response
  And error message should be "API Gateway Authentication Failed" within payment response
  #And the payment request response should be signed

#DRAG-1157 - Please update the correct error_message for the signature in the examples.
  @regression  
Scenario Outline: Negative flow- Mandatory fields not sent in the header
  Given I am an authorized user
  And I have valid payment details
  When I make a request for the payment with "<key>" missing in the header
  Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorcode within payment response
  And error message should be "<error_message>" within payment response
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
  And I have valid payment details
  When I make a request for the payment with "<key>" missing in the header
  And error message should be "Resource not found" within payment response
  #And the payment request response should be signed

 Examples:
 | key             |
 |Api-Version      |



  @regression  
Scenario Outline: Negative flow- Invalid auth token
  Given I am a merchant with invalid "<auth_token>"
  And I have valid payment details
  When I make a request for the payment
  Then I should receive a "401" error response with "<error_description>" error description and "<error_code>" errorcode within payment response
  And error message should be "<error_message>" within payment response
  #And the payment request response should be signed
#DRAG-1130 -Updated the error description and error message for all the auth token validation scenarios
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


## No error from peak currently
## @regression @skiponsitmerchant
Scenario Outline: Negative flow- Peak error response parsed by DRAGON
   Given I am an authorized user
   And I have payment details with "<invalid_value>" set for the "<parameter>"
   When I make a request for the payment
   Then I should receive a "400" error response with "<error_description>" error description and "<error_code>" errorcode within payment response
   And error message should be "<error_message>" within payment response
   #And the payment request response should be signed

   Examples:
  |error_description     |error_message    |error_code| parameter      | invalid_value |
  | Field error in object 'paymentRequestInputModel': field 'totalAmount' must be greater than or equal to 0.01; rejected value [0.0] |                 |EA017     | totalamount    | 0             |
  | Field error in object 'paymentRequestInputModel': field 'totalAmount' must be greater than or equal to 0.01; rejected value [-10.0] |                 |EA017     | totalamount    | -10           |

  @regression
  Scenario Outline: Negative flow- Mandatory fields from the body missing or invalid (DRAG-1126, DRAG-1125, DRAG-1124, DRAG-1131, DRAG-1081)
    Given I am an authorized user
    And I have payment details "<totalamount>","<currency>","<notificationURL>","<appSuccessCallback>","<appFailCallback>","<effectiveDuration>"
    When I make a request for the payment
    Then I should receive a "400" error response with "<error_description>" error description and "<error_code>" errorcode within payment response
    And error message should be "<error_message>" within payment response
  #And the payment request response should be signed
    Examples:
      |totalamount|currency|notificationURL|error_description                                                                                                                                         |error_message                    |error_code|appSuccessCallback|appFailCallback|effectiveDuration|
      |150.00     |        |/return        |Field error in object 'paymentRequestInputModel': field 'currencyCode' may not be null; rejected value [null]                                             |Service Request Validation Failed|EA014     |/confirmation     |/unsuccessful  |6                |
      |150.00     |-       |/return        |Invalid currency code                                                                                                                                     |Service Request Validation Failed|EA014     |/confirmation     |/unsuccessful  |6                |
      |150.00     |RS      |/return        |Invalid currency code                                                                                                                                     |Service Request Validation Failed|EA014     |/confirmation     |/unsuccessful  |6                |
      |150.00     |USD     |/return        |Invalid currency code                                                                                                                                     |Service Request Validation Failed|EA014     |/confirmation     |/unsuccessful  |6                |
      |           |HKD     |/return        |Field error in object 'paymentRequestInputModel': field 'totalAmount' may not be null; rejected value [null]                                              |Service Request Validation Failed|EA017     |/confirmation     |/unsuccessful  |6                |
      |%%         |HKD     |/return        |Unable to read or parse message body: json parse error at [line: 1, column: 16]                                                                           |Service Request Validation Failed|EA002     |/confirmation     |/unsuccessful  |6                |
      |0.00       |HKD     |/return        |Field error in object 'paymentRequestInputModel': field 'totalAmount' must be greater than or equal to 0.01; rejected value [0.0]                         |Service Request Validation Failed|EA017     |/confirmation     |/unsuccessful  |6                |
      |-0.01      |HKD     |/return        |Field error in object 'paymentRequestInputModel': field 'totalAmount' must be greater than or equal to 0.01; rejected value [-0.01]                       |Service Request Validation Failed|EA017     |/confirmation     |/unsuccessful  |6                |
      |1000001    |HKD     |/return        |Field error in object 'paymentRequestInputModel': field 'totalAmount' must be less than or equal to 1000000.0; rejected value [1000001.0]                 |Service Request Validation Failed|EA017     |/confirmation     |/unsuccessful  |6                |
      |0.011      |HKD     |/return        |Field error in object 'paymentRequestInputModel': field 'totalAmount' numeric value out of bounds (<7 digits>.<2 digits> expected); rejected value [0.011]|Service Request Validation Failed|EA017     |/confirmation     |/unsuccessful  |6                |
      # DRAG-1258: Effective Duration Business Boundaries
      |150.00     |USD     |/return        |Invalid currency code                                                                                                                                     |Service Request Validation Failed|EA017     |/confirmation     |/unsuccessful  |14               |
      |150.00     |USD     |/return        |Invalid currency code                                                                                                                                     |Service Request Validation Failed|EA017     |/confirmation     |/unsuccessful  |3601             |



Scenario Outline: Negative flow- TraceId's value missing from the header
   Given I am an authorized user
   And I have valid payment details with no TraceId value sent in the header
   When I make a request for the payment
   Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorcode within payment response
   And error message should be "<error_message>" within payment response
   And the payment request response should be signed

   Examples:
  | error_description                  | error_message                                         | error_code |http_status|
  |Trace-Id can not be empty.          | Service Request Validation Failed                     |EA002       |400        |


@regression
Scenario Outline: Negative flow- Request Date Time's invalid values set within the header
   Given I am an authorized user
   And I have valid payment details with invalid value "<value>" set for Request Date Time sent in the header
   When I make a request for the payment
   Then I should receive a "400" error response with "<error_description>" error description and "EA002" errorcode within payment response
  And error message should be "Service Request Validation Failed" within payment response
   #And the payment request response should be signed

   Examples:
  |value|error_description|
  |  | Request timestamp not a valid RFC3339 date-time |
  | xyz | Request timestamp not a valid RFC3339 date-time |
  | 2019-02-04T00:42:45.237Z | Request timestamp too old |


  @regression   
Scenario Outline: Negative flow- verify Error message if the additionalData is of more than 1024 characters
  Given I am an authorized user
  And I have payment details "<totalamount>","<currency>","<notificationURL>","<appSuccessCallback>","<appFailCallback>","<effectiveDuration>"
  And I have merchant data "<description>","<orderId>","<additionalData>"
  And the additionalData is of more than 1024 characters
  When I make a request for the payment
  Then I should receive a "400" error response with "Field error in object 'paymentRequestInputModel': field 'merchantData.additionalData' size must be between 0 and 1024; rejected value [Morethan1024charactersMorethan1024charactersMorethan1024charactersMorethan1024charactersMorethan1024charactersMorethan1024charactersMorethan1024charactersMorethan1024charactersMorethan1024charactersMorethan1024charactersMorethan1024charactersMorethan1024charactersMorethan1024charactersMorethan1024charactersMorethan1024charactersMorethan1024charactersMorethan1024charactersMorethan1024charactersMorethan1024charactersMorethan1024charactersMorethan1024charactersMorethan1024charactersMorethan1024charactersMorethan1024charactersMorethan1024charactersMorethan1024charactersMorethan1024charactersMorethan1024charactersMorethan1024charactersMorethan1024charactersMorethan1024charactersMorethan1024charactersMorethan1024charactersMorethan1024charactersMorethan1024charactersMorethan1024charactersMorethan1024charactersMorethan1024charactersMorethan1024charactersMorethan1024charactersMorethan1024charactersMorethan1024charactersMorethan1024charactersMorethan1024charactersMorethan1024charactersMorethan1024charactersMorethan1024characters]" error description and "EA002" errorcode within payment response
  And error message should be "Service Request Validation Failed" within payment response
  #And the payment request response should be signed

Examples:
|totalamount|currency |notificationURL            |description          |orderId |effectiveDuration |appSuccessCallback|appFailCallback|additionalData|
|100.00     |HKD      |/return                    |message from merchant|B1242183|60                |/confirmation|/unsuccessful|pizzapepperoni1234, pepperoni pizza, quantity: 1, price: 60.00, currency: HKD|

   # Peak errors - same transaction sent twice, random merchant id
   # Manual test cases - peak timeout & peak server down (switch off peak mock), Restrict Caller IPs Policy, large amount
   #                   - different content-type in the header, characters for amount & effective duration
   #                   - integer parameter's value missing from the body
