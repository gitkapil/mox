Feature: Check Status - DRAG- 178

Background: Retrieving access Token
Given I am an user
When I make a request to the Dragon ID Manager
Then I recieve an access_token

 @regression @skiponcimerchant
Scenario: Positive flow- A merchant is able to create a check status request with all the valid inputs
  Given I am an authorized user
  And I have valid payment details
  And I make a request for the payment
  And I should recieve a successful payment response
  And I have a valid payment id
  When I make a request for the check status
  Then I should recieve a successful check status response
  And the response body should contain valid status description and status code


 @regression @skiponcimerchant
Scenario: Negative flow- Invalid auth token (without Bearer in the header)
  Given I am an authorized user
  And I have valid payment details
  And I make a request for the payment
  And I should recieve a successful payment response
  And I have a valid payment id
  And I dont send Bearer with the auth token in the check status request
  When I make a request for the check status
  Then I should recieve a "401" error response with "JWT not present." error description and "401" errorcode within check status response
  And error message should be "TokenNotPresent" within check status response


 @regression @skiponcimerchant
Scenario Outline: Negative flow- Mandatory fields not sent in the header
  Given I am an authorized user
  And I have valid payment details
  And I make a request for the payment
  And I should recieve a successful payment response
  And I have a valid payment id
  When I make a request for the payment status with "<key>" missing in the header
  Then I should recieve a "<error_code>" error response with "<error_description>" error description and "<error_code>" errorcode within check status response
  And error message should be "<error_message>" within check status response

 Examples:
 |error_description                                                  |error_message         | key           |error_code |
 |Header Authorization was not found in the request. Access denied.  | HeaderNotFound       |Authorization  |401        |
 |Header Accept does not contain required value. Access denied.      | HeaderValueNotAllowed|Accept         |400        |
 |Header TraceId was not found in the request. Access denied.        | HeaderNotFound       |TraceId        |400        |


 @regression @skiponcimerchant
Scenario Outline: Negative flow- Invalid auth token
  Given I am an authorized user
  And I have valid payment details
  When I make a request for the payment
  And I should recieve a successful payment response
  And I have a valid payment id
  And I send invalid auth token "<auth_token>" in the check status request
  When I make a request for the check status
  Then I should recieve a "401" error response with "<error_description>" error description and "401" errorcode within check status response
  And error message should be "<error_message>" within check status response

 Examples:
 |error_description           |error_message          |auth_token|
 #Auth Token missing
 |JWT not present.            |TokenNotPresent        ||
 # Auth token not a JWT
 |JWT is not well formed      |Invalid JWT            |random_auth_token|
 # Auth Token has an invalid claim (roles)
 #|Claim value mismatch: roles=Basic.|TokenClaimValueMismatch|need_to_generate_it_with_invalid_roles|
 # Expired auth token
 |Lifetime validation failed. The token is expired|TokenExpired|eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IjdfWnVmMXR2a3dMeFlhSFMzcTZsVWpVWUlHdyIsImtpZCI6IjdfWnVmMXR2a3dMeFlhSFMzcTZsVWpVWUlHdyJ9.eyJhdWQiOiI1MDg4MzAwOC0yZDM4LTRjN2QtYjU2Yi0wY2NjOGJiZGY4MDIiLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC80NGMyOGMxNy1jNmI5LTRlOTAtOWQxMS1hZjc1OGMwMjgyYWUvIiwiaWF0IjoxNTM0MTMyMTAzLCJuYmYiOjE1MzQxMzIxMDMsImV4cCI6MTUzNDEzNjAwMywiYWlvIjoiNDJCZ1lKQmlPQ2t6Ylk3RlNnbEdlNnRWVVpGN0FRPT0iLCJhcHBpZCI6IjBmZmNhZDA2LTlkMmYtNDkxNS05MmMxLWNlMjU3ZTViYzBlYyIsImFwcGlkYWNyIjoiMSIsImlkcCI6Imh0dHBzOi8vc3RzLndpbmRvd3MubmV0LzQ0YzI4YzE3LWM2YjktNGU5MC05ZDExLWFmNzU4YzAyODJhZS8iLCJvaWQiOiI0MTczOTdjMi1mYTBlLTRmNGMtYWFmMy0yMmI4YWI1ODFlNzQiLCJyb2xlcyI6WyJCYXNpYyIsIlJlZnVuZCJdLCJzdWIiOiI0MTczOTdjMi1mYTBlLTRmNGMtYWFmMy0yMmI4YWI1ODFlNzQiLCJ0aWQiOiI0NGMyOGMxNy1jNmI5LTRlOTAtOWQxMS1hZjc1OGMwMjgyYWUiLCJ1dGkiOiJuMVRWdkVEUVUwZTNHVHJpcUFOU0FBIiwidmVyIjoiMS4wIn0.agJHt3dQwKZSzxymjWvmyyv8jcjgosZf6TjK4dzLp61wp0zcXidphqkp3Vu6iDXul5vakIavSnrXC50ZXwc3A_sBTJyQG2pSIkTSSF_Fb8zD7tEFuUpyk6Cul4jGqjhWJbt1brRnknhMCRqfhiyGEe9j0j9CaqVGyZa1zD4PxBOxUeL0H3PSZ5GJO6P_ieFuLaWy4DtXNOmJ6ym9WMWxVued5xRAVfRMySTPSiF9o14o3pjNpXoqYXTaC2mqkKiUFmtkOHRc_TGpjmR42DT5gMdfNdon2YjkRjFqg89huzzQD-pXH27EMT4JoVdTj60rToQPqc9VDdJyq7iKs_tLog|
 # Auth token unverified
 |Signature validation failed |TokenInvalidSignature  |eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c|


 @regression @skiponcimerchant
Scenario Outline: Negative flow- Invalid PaymentIds sent in the request
  Given I am an authorized user
  And I have valid payment details
  And I make a request for the payment
  And I should recieve a successful payment response
  And I have a payment id "<payment_id>"
  When I make a request for the check status
  Then I should recieve a "<response_code>" error response with "<error_description>" error description and "<error_code>" errorcode within check status response
  And error message should be "<error_message>" within check status response

 Examples:
 |error_description             |error_message                                     | payment_id                          |error_code |response_code|
 #|null                         | Resource not found                               |                                     |null       |404|
 |QR code not found when scans  | Resource Not Found!              | 591ec407-401d-40a6-9db0-b48a35fad8a3|CF2003     |404|
 |Payment Request Id is invalid | Service Request Validation Failed| random_payment_id                   |EA002      |400|


 @regression @skiponcimerchant
Scenario Outline: Positive flow- A merchant is able to create a check status request with all the valid inputs
  Given I am an authorized user
  And I have a payment id "<payment_id>"
  When I make a request for the check status
  Then I should recieve a successful check status response
  And the response body should contain correct "<status_description>" and "<status_code>"

  Examples:
  |payment_id                           | status_description          | status_code|
  |25f90d96-4052-4c47-8ec1-f818c0e7a212 |Payment Request Expired      |PR007       |
  |b15e090a-5e97-4b44-a67e-542eb2aa0f4d |Request for Payment Initiated|PR001       |
  |9dbcf291-d71e-4c9f-938c-1fdf4035b5f5 |Payment Success              |PR005       |
