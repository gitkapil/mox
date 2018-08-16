Feature: Payment Request API

Background: Retrieving access Token
Given I am a merchant
When I make a request to the Dragon ID Manager
Then I recieve an access_token

@payment @DRAG-241 @DRAG-288
Scenario Outline: Positive flow- A merchant is able to create a payment request with all the valid inputs
  Given I am an authorized merchant
  And I have transaction details "<amount>","<currency>","<description>","<channel>","<invoiceid>","<merchantid>","<effectiveduration>","<returnURL>"
  When I make a request for the payment
  Then I should recieve a payment response with valid trace id in the header
  And the response body should contain valid payment id, created timestamp, transaction details, links, expiry Duration

  Examples:
 |amount|currency|description |channel  |invoiceid  |merchantid        |effectiveduration|returnURL                  |
 |10000 |HKD     |            |Mcommerce|48787589674|Pizzahut1239893993|10               |https://pizzahut.com/return|
 |89.09 |HKD     |Pizza order2|         |48787589675|Pizzahut1239893993|30               |https://pizzahut.com/return|
 |0.044 |HKD     |Pizza order3|Native   |           |Pizzahut1239893993|30               |https://pizzahut.com/return|
 |2.00  |HKD     |Pizza order4|Ecommerce|48787589677|Pizzahut1239893993|                 |https://pizzahut.com/return|
 |3     |HKD     |Pizza order5|Mcommerce|48787589678|Pizzahut1239893993|30               |https://pizzahut.com/return|
 |600.0 |HKD     |Pizza order6|Ecommerce|48787589679|Pizzahut1239893993|30               |https://pizzahut.com/return|
 |12.123 |USD    |Pizza order7|Ecommerce|48787589611|Pizzahut1239893993|30               |https://pizzahut.com/return|
 |12.13 |USD     |Pizza order8|Native   |ABCD       |Pizzahut1239893993|30               |https://pizzahut.com/return|
 |20.00 |HKD     |Pizza order1|Ecommerce|48787589673|Pizzahut1239893993|55               |https://pizzahut.com/return|


@payment @DRAG-241
Scenario: Negative flow- Invalid auth token (without Bearer in the header)
  Given I am an authorized merchant
  And I dont send Bearer with the auth token
  And I have a valid transaction
  When I make a request for the payment
  Then I should recieve a 401 error response with "JWT not present." error description and "401" errorcode within payment response
  And error message should be "TokenNotPresent" within payment response


@payment @DRAG-241
Scenario Outline: Negative flow- Authorization not sent in the header
  Given I am an authorized merchant
  And I have a valid transaction
  When I make a request for the payment with "<key>" missing in the header
  Then I should recieve a 401 error response with "<error_description>" error description and "401" errorcode within payment response
  And error message should be "<error_message>" within payment response

 Examples:
 |error_description                                                |error_message  | key         |
 |Header Authorization was not found in the request. Access denied.| HeaderNotFound|Authorization|


@payment @DRAG-241
Scenario Outline: Negative flow- Invalid auth token
  Given I am a merchant with invalid "<auth_token>"
  And I have a valid transaction
  When I make a request for the payment
  Then I should recieve a 401 error response with "<error_description>" error description and "401" errorcode within payment response
  And error message should be "<error_message>" within payment response

 Examples:
 |error_description           |error_message          |auth_token|
 #Auth Token missing
 |JWT not present.            |TokenNotPresent        ||
 # Auth token not a JWT
 |JWT is not well formed      |Invalid JWT            |random_auth_token|
 # Auth Token has an invalid claim (aud)
 |Claim value mismatch: aud        |TokenClaimValueMismatch|need_to_generate_it_with_invalid_appid|
 # Auth Token has an invalid claim (roles)
 |Claim value mismatch: roles=Basic.|TokenClaimValueMismatch|need_to_generate_it_with_invalid_roles|
 # Expired auth token
 |Lifetime validation failed. The token is expired|TokenExpired|eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IjdfWnVmMXR2a3dMeFlhSFMzcTZsVWpVWUlHdyIsImtpZCI6IjdfWnVmMXR2a3dMeFlhSFMzcTZsVWpVWUlHdyJ9.eyJhdWQiOiI1MDg4MzAwOC0yZDM4LTRjN2QtYjU2Yi0wY2NjOGJiZGY4MDIiLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC80NGMyOGMxNy1jNmI5LTRlOTAtOWQxMS1hZjc1OGMwMjgyYWUvIiwiaWF0IjoxNTM0MTMyMTAzLCJuYmYiOjE1MzQxMzIxMDMsImV4cCI6MTUzNDEzNjAwMywiYWlvIjoiNDJCZ1lKQmlPQ2t6Ylk3RlNnbEdlNnRWVVpGN0FRPT0iLCJhcHBpZCI6IjBmZmNhZDA2LTlkMmYtNDkxNS05MmMxLWNlMjU3ZTViYzBlYyIsImFwcGlkYWNyIjoiMSIsImlkcCI6Imh0dHBzOi8vc3RzLndpbmRvd3MubmV0LzQ0YzI4YzE3LWM2YjktNGU5MC05ZDExLWFmNzU4YzAyODJhZS8iLCJvaWQiOiI0MTczOTdjMi1mYTBlLTRmNGMtYWFmMy0yMmI4YWI1ODFlNzQiLCJyb2xlcyI6WyJCYXNpYyIsIlJlZnVuZCJdLCJzdWIiOiI0MTczOTdjMi1mYTBlLTRmNGMtYWFmMy0yMmI4YWI1ODFlNzQiLCJ0aWQiOiI0NGMyOGMxNy1jNmI5LTRlOTAtOWQxMS1hZjc1OGMwMjgyYWUiLCJ1dGkiOiJuMVRWdkVEUVUwZTNHVHJpcUFOU0FBIiwidmVyIjoiMS4wIn0.agJHt3dQwKZSzxymjWvmyyv8jcjgosZf6TjK4dzLp61wp0zcXidphqkp3Vu6iDXul5vakIavSnrXC50ZXwc3A_sBTJyQG2pSIkTSSF_Fb8zD7tEFuUpyk6Cul4jGqjhWJbt1brRnknhMCRqfhiyGEe9j0j9CaqVGyZa1zD4PxBOxUeL0H3PSZ5GJO6P_ieFuLaWy4DtXNOmJ6ym9WMWxVued5xRAVfRMySTPSiF9o14o3pjNpXoqYXTaC2mqkKiUFmtkOHRc_TGpjmR42DT5gMdfNdon2YjkRjFqg89huzzQD-pXH27EMT4JoVdTj60rToQPqc9VDdJyq7iKs_tLog|
 # Auth token unverified
 |Signature validation failed |TokenInvalidSignature  |eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c|


@payment @DRAG-287
Scenario Outline: Negative flow- Peak error response parsed by DRAGON
   Given I am an authorized merchant
   And I have transaction details with "<invalid_value>" set for the "<parameter>"
   When I make a request for the payment
   Then I should recieve a 400 error response with "<error_description>" error description and "<error_code>" errorcode within payment response
   And error message should be "<error_message>" within payment response

   Examples:
  |error_description             |error_message          |error_code| parameter | invalid_value |
  | Payment Amount error_Dynamic | Validation Fail!      |BG2002    | amount    | 0             |
  | Payment Amount error_Dynamic | Validation Fail!      |BG2002    | amount    | -10           |


  # Peak errors - same transaction sent twice, random merchant id
  # Manual test cases - peak timeout & peak server down (switch off peak mock), Restrict Caller IPs Policy, large amount
  #                   - different content-type in the header, characters for amount & effective duration