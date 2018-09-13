Feature: Payment Request API- DRAG-301

Background: Retrieving access Token
Given I am an user
When I make a request to the Dragon ID Manager
Then I recieve an access_token

# For the parametres where values are missing within the table, while creating request, the parameter will not be included at all as a a part of the payload
 @regression
Scenario Outline: Positive flow- A merchant is able to create a payment request with all the valid inputs
  Given I am an authorized user
  And I have payment details "<merchantid>", "<totalamount>","<currency>","<notificationURL>"
  And I have shopping cart details
  |sku            |name            |quantity|price |currency |category |
  |pizzapepperoni |pepperoni pizza |1       |60    |HKD       |123     |
  |               |margherita pizza|1       |60    |HKD       |123     |
  |pizzapepperoni |                |1       |60    |HKD       |123     |
  |pizzapepperoni |pepperoni pizza |        |60    |HKD       |123     |
  |pizzapepperoni |pepperoni pizza |1       |      |HKD       |123     |
  |pizzapepperoni |pepperoni pizza |1       |60    |          |123     |
  |pizzapepperoni |pepperoni pizza |1       |60    |HKD       |        |
  And I have merchant data "<description>", "<channel>","<orderId>","<effectiveDuration>"
  When I make a request for the payment
  Then I should recieve a successful payment response
  And the response body should contain valid payment request id, created timestamp, web link, app link

Examples:
|merchantid  |totalamount|currency |notificationURL            |description          |channel  |orderId |effectiveDuration |
|053598653254|100.00     |HKD      |https://pizzahut.com/return|message from merchant|mCommerce|B1242183|60                |
#description within Merchant Data missing
|053598653254|300.12     |HKD      |https://pizzahut.com/return|                     |ECommerce|XYZ456  |30                |
#channel within Merchant Data missing
|053598653254|500        |HKD      |https://pizzahut.com/return|message from merchant|         |B1242183|10                |
#orderId within Merchant Data missing
|053598653254|0.01       |HKD      |https://pizzahut.com/return|message from merchant|mCommerce|        |60                |
#effectiveDuration within Merchant Data missing
|053598653254|1          |HKD      |https://pizzahut.com/return|message from merchant|Native   |XYZ123  |                  |
#notificationURI missing
|053598653254|100.00     |HKD      |                           |message from merchant|mCommerce|B1242183|60                |

 @regression
Scenario Outline: Positive flow- A merchant is able to create a payment request with all the valid inputs without shopping cart
  Given I am an authorized user
  And I have payment details "<merchantid>", "<totalamount>","<currency>","<notificationURL>"
  And I have merchant data "<description>", "<channel>","<orderId>","<effectiveDuration>"
  When I make a request for the payment
  Then I should recieve a successful payment response
  And the response body should contain valid payment request id, created timestamp, web link, app link

Examples:
|merchantid  |totalamount|currency |notificationURL            |description          |channel  |orderId |effectiveDuration |
|053598653254|100.00     |HKD      |https://pizzahut.com/return|message from merchant|mCommerce|B1242183|10                |
#description within Merchant Data missing
|053598653254|300.12     |HKD      |https://pizzahut.com/return|                     |ECommerce|XYZ456  |30                |
#channel within Merchant Data missing
|053598653254|500        |HKD      |https://pizzahut.com/return|message from merchant|         |B1242183|10                |
#orderId within Merchant Data missing
|053598653254|0.01       |HKD      |https://pizzahut.com/return|message from merchant|mCommerce|        |60                |
#effectiveDuration within Merchant Data missing
|053598653254|1          |HKD      |https://pizzahut.com/return|message from merchant|Native   |XYZ123  |                  |
#notificationURI missing
|053598653254|100.00     |HKD      |                           |message from merchant|mCommerce|B1242183|60                |


 @regression
Scenario: Positive flow- A merchant is able to create a payment request with all the valid inputs without merchant data
  Given I am an authorized user
  And I have valid payment details
  When I make a request for the payment
  Then I should recieve a successful payment response
  And the response body should contain valid payment request id, created timestamp, web link, app link


# For the parametres where value is "no_value" within the table, while creating request the parameter (key) will be included but will have no value
 @regression
Scenario Outline: Positive flow- A merchant is able to create a payment request where the non mandatory fields within body have no corresponding values in the payload
  Given I am an authorized user
  And I have payment details "<merchantid>", "<totalamount>","<currency>","<notificationURL>"
  And I have merchant data "<description>", "<channel>","<orderId>","<effectiveDuration>"
  When I make a request for the payment
  Then I should recieve a successful payment response
  And the response body should contain valid payment request id, created timestamp, web link, app link

Examples:
|merchantid  |totalamount|currency |notificationURL            |description          |channel  |orderId |effectiveDuration |
|053598653254|100.00     |HKD      |https://pizzahut.com/return|no_value             |mCommerce|B1242183|60                |
|053598653254|300.00     |HKD      |https://pizzahut.com/return|message from merchant|no_value |B1242183|60                |
|053598653254|150.00     |HKD      |https://pizzahut.com/return|message from merchant|mCommerce|no_value|60                |
|053598653254|900000     |HKD      |no_value                   |message from merchant|mCommerce|B1242183|60                |


# For the parametres where value is "no_value" within the table, while creating request the parameter (key) will be included but will have no value
 @regression
Scenario Outline: Positive flow- A merchant is able to create a payment request where the non mandatory fields within shopping cart have no corresponding values in the payload
  Given I am an authorized user
  And I have payment details "<merchantid>", "<totalamount>","<currency>","<notificationURL>"
  And I have shopping cart details
    |sku            |name            |quantity|price |currency |category |
    |no_value       |pepperoni pizza |1       |60    |HKD       |123     |
    |pizzapepperoni |no_value        |1       |60    |HKD       |123     |
    |pizzapepperoni |pepperoni pizza |1       |60    |no_value  |123     |

  And I have merchant data "<description>", "<channel>","<orderId>","<effectiveDuration>"
  When I make a request for the payment
  Then I should recieve a successful payment response
  And the response body should contain valid payment request id, created timestamp, web link, app link

Examples:
|merchantid  |totalamount|currency |notificationURL            |description          |channel  |orderId |effectiveDuration |
|053598653254|100.00     |HKD      |https://pizzahut.com/return|message from merchant|mCommerce|B1242183|60                |


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
 |error_description                                                  |error_message  | key           |error_code |
 |Header Authorization was not found in the request. Access denied.  | HeaderNotFound|Authorization  |401        |
 |Header RequestDateTime was not found in the request. Access denied.| HeaderNotFound|RequestDateTime|400        |
 |Header TraceId was not found in the request. Access denied.        | HeaderNotFound|TraceId        |400        |

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
 |Lifetime validation failed. The token is expired|TokenExpired|eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IjdfWnVmMXR2a3dMeFlhSFMzcTZsVWpVWUlHdyIsImtpZCI6IjdfWnVmMXR2a3dMeFlhSFMzcTZsVWpVWUlHdyJ9.eyJhdWQiOiI1MDg4MzAwOC0yZDM4LTRjN2QtYjU2Yi0wY2NjOGJiZGY4MDIiLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC80NGMyOGMxNy1jNmI5LTRlOTAtOWQxMS1hZjc1OGMwMjgyYWUvIiwiaWF0IjoxNTM0MTMyMTAzLCJuYmYiOjE1MzQxMzIxMDMsImV4cCI6MTUzNDEzNjAwMywiYWlvIjoiNDJCZ1lKQmlPQ2t6Ylk3RlNnbEdlNnRWVVpGN0FRPT0iLCJhcHBpZCI6IjBmZmNhZDA2LTlkMmYtNDkxNS05MmMxLWNlMjU3ZTViYzBlYyIsImFwcGlkYWNyIjoiMSIsImlkcCI6Imh0dHBzOi8vc3RzLndpbmRvd3MubmV0LzQ0YzI4YzE3LWM2YjktNGU5MC05ZDExLWFmNzU4YzAyODJhZS8iLCJvaWQiOiI0MTczOTdjMi1mYTBlLTRmNGMtYWFmMy0yMmI4YWI1ODFlNzQiLCJyb2xlcyI6WyJCYXNpYyIsIlJlZnVuZCJdLCJzdWIiOiI0MTczOTdjMi1mYTBlLTRmNGMtYWFmMy0yMmI4YWI1ODFlNzQiLCJ0aWQiOiI0NGMyOGMxNy1jNmI5LTRlOTAtOWQxMS1hZjc1OGMwMjgyYWUiLCJ1dGkiOiJuMVRWdkVEUVUwZTNHVHJpcUFOU0FBIiwidmVyIjoiMS4wIn0.agJHt3dQwKZSzxymjWvmyyv8jcjgosZf6TjK4dzLp61wp0zcXidphqkp3Vu6iDXul5vakIavSnrXC50ZXwc3A_sBTJyQG2pSIkTSSF_Fb8zD7tEFuUpyk6Cul4jGqjhWJbt1brRnknhMCRqfhiyGEe9j0j9CaqVGyZa1zD4PxBOxUeL0H3PSZ5GJO6P_ieFuLaWy4DtXNOmJ6ym9WMWxVued5xRAVfRMySTPSiF9o14o3pjNpXoqYXTaC2mqkKiUFmtkOHRc_TGpjmR42DT5gMdfNdon2YjkRjFqg89huzzQD-pXH27EMT4JoVdTj60rToQPqc9VDdJyq7iKs_tLog|
 # Auth token unverified
 |Signature validation failed |TokenInvalidSignature  |eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c|


 @regression
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
  And I have payment details "<merchantid>", "<totalamount>","<currency>","<notificationURL>"
  When I make a request for the payment
  Then I should recieve a "400" error response with "<error_description>" error description and "<error_code>" errorcode within payment response
  And error message should be "<error_message>" within payment response


Examples:
|merchantid  |totalamount|currency |notificationURL            |error_description                |error_message|error_code|
|            |100.00     |HKD      |https://pizzahut.com/return|Service Request Validation Failed|Something went wrong. Sorry, we are unable to perform this action right now. Please try again.|BNA002|
|053598653254|150.00     |         |https://pizzahut.com/return|Service Request Validation Failed|Something went wrong. Sorry, we are unable to perform this action right now. Please try again.|BNA002|
#|no_value    |100.00     |HKD      |https://pizzahut.com/return|Service Request Validation Failed|Something went wrong. Sorry, we are unable to perform this action right now. Please try again.|BNA002|
#|053598653254|150.00     |no_value |https://pizzahut.com/return|Service Request Validation Failed|Something went wrong. Sorry, we are unable to perform this action right now. Please try again.|BNA002|


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

   # Peak errors - same transaction sent twice, random merchant id
   # Manual test cases - peak timeout & peak server down (switch off peak mock), Restrict Caller IPs Policy, large amount
   #                   - different content-type in the header, characters for amount & effective duration
   #                   - integer parameter's value missing from the body
