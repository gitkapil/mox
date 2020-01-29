@skiponversionten
Feature: Management POST platform API - DRAG-2027
  As a platform user
  I want to create a new platform using POST platform API

  Background: Retrieving access Token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

  @trial @regression
  Scenario Outline: Positive flow- POST platform with valid input request body, header and parameter values
    Given I am a POST platform authorized DRAGON user with Platform.ReadWrite.All
    When I make request for POST platform API with "<platformName>" platformName and "<platformDescription>" platformDescription in request body
    Then I should receive a successful POST platform response
    And validate the post platform response
    Given I am a GET platform authorized DRAGON user with Platform.ReadWrite.All
    When I make a GET request to the platform endpoint with platformId of created platform
    Then I should receive a successful GET platform response
    And validate the response from POST Platform API is present in GET platform API
    Examples:
      | platformName | platformDescription |
      | validname    | INDIVIDUAL          |
    #platformName is free text; can be anything


  @regression
  Scenario Outline: Negative flow- Invalid auth token
    Given I am a POST dragon DRAGON user with Platform.ReadWrite.All with invalid "<auth_token>"
    When I make a POST request to the post platform endpoint
    Then I should receive a "<http_code>" error response with "<error_description>" error description and "<error_code>" error code within the POST platform response
    And error message should be "<error_message>" within the POST platform response
    Examples:
      | error_description    | error_message                     | auth_token                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             | error_code | http_code |
 #Auth Token missing
      | Error validating JWT | API Gateway Authentication Failed |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        | EA001      | 401       |
 # Auth token not a JWT
   #   | Error validating JWT | Unexpected API Gateway Exception  | random_auth_token                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      | EA000      | 500       |
 # Expired auth token
      | Error validating JWT | API Gateway Authentication Failed | Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Imk2bEdrM0ZaenhSY1ViMkMzbkVRN3N5SEpsWSIsImtpZCI6Imk2bEdrM0ZaenhSY1ViMkMzbkVRN3N5SEpsWSJ9.eyJhdWQiOiI1MTczYTVhOS00MjEyLTQ4NzctODYyMS03YmMxNjRjZjE3OGIiLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yZmY5M2NjZS1lZmQxLTRlMTYtOTJiYS1hZmI1M2U5ZTA5ZmMvIiwiaWF0IjoxNTM3MTY4NzAzLCJuYmYiOjE1MzcxNjg3MDMsImV4cCI6MTUzNzE3MjYwMywiYWlvIjoiNDJCZ1lKaDlXbFZsNFh1SjZFMHZsMDMvY25TOU5nQT0iLCJhcHBpZCI6IjEwOTMyM2UzLWM1NWUtNDI0Yy1iNzEyLTJlODJmMTY5NWU5OCIsImFwcGlkYWNyIjoiMSIsImlkcCI6Imh0dHBzOi8vc3RzLndpbmRvd3MubmV0LzJmZjkzY2NlLWVmZDEtNGUxNi05MmJhLWFmYjUzZTllMDlmYy8iLCJvaWQiOiI4NjM3MjIwNi1jZWZhLTQ1ZWEtOTM0Ni0xNjNiYmFiYzc3MDYiLCJyb2xlcyI6WyJwYXltZW50UmVxdWVzdCIsInJlZnVuZCIsImRldmVsb3BlciJdLCJzdWIiOiI4NjM3MjIwNi1jZWZhLTQ1ZWEtOTM0Ni0xNjNiYmFiYzc3MDYiLCJ0aWQiOiIyZmY5M2NjZS1lZmQxLTRlMTYtOTJiYS1hZmI1M2U5ZTA5ZmMiLCJ1dGkiOiI3bUd1b0FRMUQwZTFGMks0Sy1ZWEFBIiwidmVyIjoiMS4wIn0.gCfn6QNUjzENuKvVN0bdkWgRwN-1ipovSq5Yb8IjCRfRhZNTFffLP1oIVw_8sPQxTFfV8CBOcQG385n-r_tIWBlMANHdKmpmwmOTz4J08EWzz_SY4zWdvMlF7quCYdrXVIXaKd-PLtO0UDTI7CsSAso7OAOsHBNrn3ITBoR0aMo_lM8X5dJM5fleSHFiJPMYDHpawZuy_BBXC0AUdcVT61NHkdO7sDV4Dc_C12CsShEqMi3Nj-uzr5wYHmPTxi3nk2px-_yiVaAHxxCmE0f7rWdM4BjVB89j_xtZrn1-VovgUUn_QDppY9yOgKx831xXBMX0Tz293V8g7BkagVI3lw | EA001      | 401       |
 # Auth token unverified
      | Error validating JWT | API Gateway Authentication Failed | Bearer nbCwW11w3XkB-xUaXwKRSLjMHGQ                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     | EA001      | 401       |


  @regression
  Scenario Outline: Negative flow- POST Platform with missing mandatory header values
    Given I am a POST platform authorized DRAGON user with Platform.ReadWrite.All
    When I make a POST request to the Platform endpoint with "<key>" missing in the header
    Then I should receive "<http_status>" error status with "<error_description>" error description and "<error_code>" errorcode in response
    And error message should be "<error_message>" in the response
    Examples:
      | error_description                                              | error_message                     | key           | error_code | http_status |
      | Error validating JWT                                           | API Gateway Authentication Failed | Authorization | EA001      | 401         |
      | Header Trace-Id was not found in the request. Access denied.   | API Gateway Validation Failed     | Trace-Id      | EA002      | 400         |
      | Header Accept does not contain required value.  Access denied. | Request Header Not Acceptable     | Accept        | EA008      | 406         |
      | Content type 'text/plain;charset=ISO-8859-1' not supported     | Service Request Validation Failed | Content-Type  | EA002      | 415         |


  @regression
  Scenario Outline: Negative flow- Mandatory fields Api-Version not sent in the header
    Given I am a POST platform authorized DRAGON user with Platform.ReadWrite.All
    When I make a POST request to the Platform endpoint with "<key>" missing in the header
    Then I should receive "404" status code in response
    And error message should be "Resource not found" within the POST platform response
    Examples:
      | key         |
      | Api-Version |


  @trial @regression
  Scenario Outline: Negative flow- Invalid mandatory field Api-Version provided in header
    Given I am a POST platform authorized DRAGON user with Platform.ReadWrite.All
    When I provide "<platformName>" platformName and "<platformDescription>" platformDescription in request body with invalid key "<invalidValue>" for "<key>" in header
    Then I should receive "<statusCode>" status code in response
    And error message should be "<error_message>" in the response

    Examples:
      | key         | invalidValue | statusCode | error_message      | platformName | platformDescription  |
      | Api-Version | 0.20         | 404        | Resource not found | validname    | Platform Description |
      | Api-Version | abc          | 404        | Resource not found | validname    | Platform Description |
      | Api-Version | @#$%^        | 404        | Resource not found | validname    | Platform Description |


  @trial @regression
  Scenario: Negative flow- Request body provided Null {}
    Given I am a POST platform authorized DRAGON user with Platform.ReadWrite.All
    When I make request to post platform endpoint with null request body
    Then I should receive "400" http code with "Service Request Validation Failed" error message
    And Validate errorCode and errorDescription within platform response


  @trial @regression
  Scenario: Negative flow- Request body not provided
    Given I am a POST platform authorized DRAGON user with Platform.ReadWrite.All
    When I make request to post platform endpoint without request body
    Then I should receive a "400" error response with "Unable to read or parse message body" error description and "EA002" errorcode within platform response
    And error message should be "Service Request Validation Failed" within platform response


  @trial @regression
  Scenario Outline: Negative flow- POST platform with one request body parameter not provided
    Given I am a POST platform authorized DRAGON user with Platform.ReadWrite.All
    When I make request for POST platform API with "<platformName>" platformName and "<platformDescription>" platformDescription in request body
    Then I should receive "<http_status>" error status with "<error_description>" error description and "<error_code>" errorcode in response
    And error message should be "<error_message>" in the response
    Examples:
      | platformName | platformDescription | http_status | error_description                                                                                             | error_code | error_message                     |
      | null         | validDescription    | 400         | Field error in object 'postPlatformsInputModel': field 'platformName' must not be null; rejected value [null] | EA002      | Service Request Validation Failed |
      | validname    | null                | 400         | Field error in object 'postPlatformsInputModel': field 'description' must not be null; rejected value [null]  | EA002      | Service Request Validation Failed |


  @trial @regression
  Scenario Outline: Negative flow- POST platform with one empty "" request body parameter
    Given I am a POST platform authorized DRAGON user with Platform.ReadWrite.All
    When I make request for POST platform API with "<platformName>" platformName and "<platformDescription>" platformDescription in request body
    Then I should receive "<http_status>" error status with "<error_description>" error description and "<error_code>" errorcode in response
    And error message should be "<error_message>" in the response
    Examples:
      | platformName | platformDescription | http_status | error_description                 | error_code | error_message                     |
      |              | validDescription    | 400         | Platform Name not provided        | EA009      | Service Request Validation Failed |
      | validname    |                     | 400         | Platform Description not provided | EA009      | Service Request Validation Failed |


  @trial @regression
  Scenario Outline: Negative flow - POST platform with invalid request body and verify the response
    Given I am a POST platform authorized DRAGON user with Platform.ReadWrite.All
    When I make request for POST platform API with "<platformName>" platformName and "<platformDescription>" platformDescription in request body
    Then I should receive "<http_status>" error status with "<error_description>" error description and "<error_code>" errorcode in response
    And error message should be "<error_message>" in the response
    Examples:
      | platformName | platformDescription | http_status | error_description                                                                                                    | error_code | error_message                     |
      | longname     | validDescription    | 400         | Field error in object 'postPlatformsInputModel': field 'platformName' size must be between 0 and 100; rejected value | EA002      | Service Request Validation Failed |
      | space        | validDescription    | 400         | Platform Name not provided                                                                                           | EA009      | Service Request Validation Failed |
      | validname    | longname            | 400         | Field error in object 'postPlatformsInputModel': field 'description' size must be between 0 and 256; rejected value  | EA002      | Service Request Validation Failed |


  @trial @regression
  Scenario Outline: Negative flow- POST platform with existing platformName but different description
    Given I am a POST platform authorized DRAGON user with Platform.ReadWrite.All
    When I make request for POST platform API with "<platformName>" platformName and "<platformDescription>" platformDescription in request body
    When I make request for POST platform API with "<platformName>" platformName and "<platformDescription>" platformDescription in request body
    Then I should receive "<http_status>" error status with "<error_description>" error description and "<error_code>" errorcode in response
    And error message should be "<error_message>" in the response
    Examples:
      | platformName | platformDescription | http_status | error_message                     | error_code | error_description            |
      | existingname | validDescription    | 400         | Service Request Validation Failed | EA009      | Platform name already exist. |