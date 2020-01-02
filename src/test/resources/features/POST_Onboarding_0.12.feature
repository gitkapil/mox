@credentials
Feature: POST One Click Merchant Onboarding API - DRAG-1850, DRAG-2010

  Background: Retrieving access Token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

  @regression
  Scenario Outline: SC-1 - Positive flow - A DRAGON user with All privilege is onboarded successfully with One Click Onboarding API
    Given I am logging in as a user with correct privileges
    When I make request for a new client with name as "<applicationName>", peakId as "<peakId>", subUnitId as "<subUnitId>", organisationId as "<organisationId>", description as "<description>" and platformId as "<platformId>"
    Then I should receive a successful merchant onboarding response
    And verify the response body contains all mandatory details
    Examples:
      | applicationName | peakId                               | subUnitId                            | organisationId                       | platformId                           | description |
      | validname       | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 2ee3e4a5-ef45-4fe2-a37d-d5fcfc6adb33 | description |

  @trial @regression
  Scenario Outline: SC-2 Positive flow - Validate platformId and platformName from database
    Given I am logging in as a user with correct privileges
    When I make request for a new client with name as "<applicationName>", peakId as "<peakId>", subUnitId as "<subUnitId>", organisationId as "<organisationId>", description as "<description>" and platformId as "<platformId>"
    Then I should receive a successful merchant onboarding response
    And validate "<platformId>" and platformName from database
    Examples:
      | applicationName | peakId                               | subUnitId                            | organisationId                       | platformId                           | description |
      | validname       | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 2ee3e4a5-ef45-4fe2-a37d-d5fcfc6adb33 | description |


  @regression
  Scenario Outline: SC-3-Positive flow- Existing applicationName provided in request body returns existing details
    Given I am logging in as a user with correct privileges
    When I make request for a new client with name as "<applicationName>", peakId as "<peakId>", subUnitId as "<subUnitId>", organisationId as "<organisationId>", description as "<description>" and platformId as "<platformId>"
    Then I should receive a success "<http_status>" status response
    And verify the response body contains all mandatory details
    And store the response of first API hit
    When I make request for same client with same applicationName, peakId as "<peakId>", subUnitId as "<subUnitId>", organisationId as "<organisationId>", description as "<description>" and platformId as "<platformId>"
    Then I should receive a appropriate status response
    And verify the response body should be returned for same client
    Examples:
      | applicationName | peakId                               | subUnitId                            | organisationId                       | platformId                           | description | http_status |
      #existing application name and existing request body parameters
      | validname       | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 2ee3e4a5-ef45-4fe2-a37d-d5fcfc6adb33 | string      | 201         |


  @regression @skiponversionten @skiponversioneleven
  Scenario Outline: SC-4-Positive flow- Existing applicationName provided in request body should returns eror
    Given I am logging in as a user with correct privileges
    When I make request for a new client with name as "<applicationName>", peakId as "<peakId>", subUnitId as "<subUnitId>", organisationId as "<organisationId>", description as "<description>" and platformId as "<platformId>"
    Then I should receive a success "<http_status>" status response
    And verify the response body contains all mandatory details
    And store the response of first API hit
    When I make request for same client with same applicationName, peakId as "<peakId>", subUnitId as "<subUnitId>", organisationId as "<organisationId>", description as "<description>" and platformId as "<platformId>"
    Then I should receive a "<status_code>" error response with "<error_description>" error description and "<error_code>" errorcode in response
    And error message should be "<error_message>" within the response
    Examples:
      | applicationName | peakId                               | subUnitId                            | organisationId                       | platformId                           | description | http_status | status_code | error_description                                                                                    | error_code | error_message             |
      #existing application name and existing request body parameters
      | validname       | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 2ee3e4a5-ef45-4fe2-a37d-d5fcfc6adb33 | string      | 201         | 400         | Client Id already exists. Please use the POST credentials API if you want to create the credentials. | EA056      | Business Rules Incorrect! |


  @regression
  Scenario Outline: SC-5- Positive flow- Existing applicationName with different applicationDescription provided in request body returns existing details with updated applicationDescription only
    Given I am logging in as a user with correct privileges
    When I make request for a new client with name as "<applicationName>", peakId as "<peakId>", subUnitId as "<subUnitId>", organisationId as "<organisationId>", description as "<description>" and platformId as "<platformId>"
    Then I should receive a success "<http_status>" status response
    And verify the response body contains all mandatory details
    And store the response of first API hit
    When I make request for same client with same applicationName, peakId as "<peakId>", subUnitId as "<subUnitId>", organisationId as "<organisationId>" and platformId as "<platformId>" but different description as "<description>"
    Then I should receive a appropriate status response
    And validate only applicationDescription is updated in response
    Examples:
      | applicationName | peakId                               | subUnitId                            | organisationId                       | platformId                           | description | http_status |
      #existing application name and existing request body parameters
      | validname       | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 2ee3e4a5-ef45-4fe2-a37d-d5fcfc6adb33 | random      | 201         |


  @regression @skiponversionten @skiponversioneleven
  Scenario Outline: SC-6 - Positive flow- Existing applicationName with different applicationDescription provided in request body should returns errors
    Given I am logging in as a user with correct privileges
    When I make request for a new client with name as "<applicationName>", peakId as "<peakId>", subUnitId as "<subUnitId>", organisationId as "<organisationId>", description as "<description>" and platformId as "<platformId>"
    Then I should receive a success "<http_status>" status response
    And verify the response body contains all mandatory details
    And store the response of first API hit
    When I make request for same client with same applicationName, peakId as "<peakId>", subUnitId as "<subUnitId>", organisationId as "<organisationId>" and platformId as "<platformId>" but different description as "<description>"
    Then I should receive a "<status_code>" error response with "<error_description>" error description and "<error_code>" errorcode in response
    And error message should be "<error_message>" within the response
    Examples:
      | applicationName | peakId                               | subUnitId                            | organisationId                       | platformId                           | description | http_status | status_code | error_description                                                                                    | error_code | error_message             |
      #existing application name and existing request body parameters
      | validname       | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 2ee3e4a5-ef45-4fe2-a37d-d5fcfc6adb33 | random      | 201         | 400         | Client Id already exists. Please use the POST credentials API if you want to create the credentials. | EA056      | Business Rules Incorrect! |


  @regression  @skiponversionten @skiponversiontwelve @check
  Scenario Outline: SC-7-10- Negative flow- Existing applicationName with different peakId, subUnitId, organisationId provided in request body returns error in version eleven
    Given I am logging in as a user with correct privileges
    And I am a POST platform authorized DRAGON user with Platform.ReadWrite.All
    When I make request for POST platform API with "<platformName>" platformName and "<platformDescription>" platformDescription in request body
    And I make request for existing client with name as "<applicationName>", peakId as "<peakId>", subUnitId as "<subUnitId>", organisationId as "<organisationId>", description as "<description>" and platformId from POST Platform API
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorcode in response
    And error message should be "<error_message>" within the response

    Examples:
      | applicationName | peakId                               | subUnitId                            | organisationId                       | description | http_status | error_description         | error_code | error_message                     | platformName | platformDescription |
    #existing application name and different peakId
      | existingname    | 859cce3f-f3da-4448-9e88-cf8450aea211 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | string      | 400         | Application already exist | EA002      | Service Request Validation Failed | validname    | INDIVIDUAL          |
    #existing application name and different subUnitId
      | existingname    | 859cce3f-f3da-4448-9e88-cf8450aea289 | 2ee3e4a5-ef45-4fe2-a37d-d5fcfc6adb33 | 859cce3f-f3da-4448-9e88-cf8450aea289 | string      | 400         | Application already exist | EA002      | Service Request Validation Failed | validname    | INDIVIDUAL          |
    #existing application name and different organisationIdClient Id already exists. Please use the POST credentials API if you want to create the credentials.Application already exist
      | existingname    | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 2ee3e4a5-ef45-4fe2-a37d-d5fcfc6adb33 | string      | 400         | Application already exist | EA002      | Service Request Validation Failed | validname    | INDIVIDUAL          |
    #existing application name and different platformIdClient Id already exists. Please use the POST credentials API if you want to create the credentials.Application already exist
      | existingname    | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | string      | 400         | Application already exist | EA002      | Service Request Validation Failed | validname    | INDIVIDUAL          |

  @regression @skiponversionten @skiponversioneleven
  Scenario Outline: SC-11-14 Negative flow- Existing applicationName with different peakId, subUnitId, organisationId provided in request body returns error in version twelve
    Given I am logging in as a user with correct privileges
    And I am a POST platform authorized DRAGON user with Platform.ReadWrite.All
    When I make request for POST platform API with "<platformName>" platformName and "<platformDescription>" platformDescription in request body
    And I make request for existing client with name as "<applicationName>", peakId as "<peakId>", subUnitId as "<subUnitId>", organisationId as "<organisationId>", description as "<description>" and platformId from POST Platform API
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorcode in response
    And error message should be "<error_message>" within the response

    Examples:
      | applicationName | peakId                               | subUnitId                            | organisationId                       | description | http_status | error_description                                                                                    | error_code | error_message             | platformName | platformDescription |
    #existing application name and different peakId
      | existingname    | 859cce3f-f3da-4448-9e88-cf8450aea211 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | string      | 400         | Client Id already exists. Please use the POST credentials API if you want to create the credentials. | EA056      | Business Rules Incorrect! | validname    | INDIVIDUAL          |
    #existing application name and different subUnitIdEA056
      | existingname    | 859cce3f-f3da-4448-9e88-cf8450aea289 | 2ee3e4a5-ef45-4fe2-a37d-d5fcfc6adb33 | 859cce3f-f3da-4448-9e88-cf8450aea289 | string      | 400         | Client Id already exists. Please use the POST credentials API if you want to create the credentials. | EA056      | Business Rules Incorrect! | validname    | INDIVIDUAL          |
    #existing application name and different organisationIdClient Id already exists. Please use the POST credentials API if you want to create the credentials.EA056
      | existingname    | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 2ee3e4a5-ef45-4fe2-a37d-d5fcfc6adb33 | string      | 400         | Client Id already exists. Please use the POST credentials API if you want to create the credentials. | EA056      | Business Rules Incorrect! | validname    | INDIVIDUAL          |
    #existing application name and different platformIdClient Id already exists. Please use the POST credentials API if you want to create the credentials.EA056
      | existingname    | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | string      | 400         | Client Id already exists. Please use the POST credentials API if you want to create the credentials. | EA056      | Business Rules Incorrect! | validname    | INDIVIDUAL          |


  @regression
  Scenario Outline: SC-15 Negative flow- Mandatory field Api-Vers\ion not sent in the header
    Given I am logging in as a user with correct privileges
    When I make request to one click merchant onboard endpoint with "<key>" missing in the header
    Then I should receive a "404" status code in response
    And error message should be "Resource not found" within the response
    Examples:
      | key         |
      | Api-Version |


  @regression
  Scenario Outline: SC-16-19 Negative flow- Mandatory fields not sent in the header
    Given I am logging in as a user with correct privileges
    When I make request to one click merchant onboard endpoint with "<key>" missing in the header
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorcode in response
    And error message should be "<error_message>" within the response
    Examples:
      | error_description                                              | error_message                     | key           | error_code | http_status |
      | Error validating JWT                                           | API Gateway Authentication Failed | Authorization | EA001      | 401         |
      | Header Trace-Id was not found in the request. Access denied.   | API Gateway Validation Failed     | Trace-Id      | EA002      | 400         |
      | Header Accept does not contain required value.  Access denied. | Request Header Not Acceptable     | Accept        | EA008      | 406         |
      | Content type 'text/plain;charset=ISO-8859-1' not supported     | Service Request Validation Failed | Content-Type  | EA002      | 415         |


  @regression
  Scenario Outline: SC-20-23- Negative flow- Invalid mandatory fields provided in header
    Given I am logging in as a user with correct privileges
    When I provide application name as "<applicationName>", peakId as "<peakId>", subUnitId as "<subUnitId>", organisationId as "<organisationId>", description as "<description>" and platformId as "<platformId>" in request body with invalid key "<invalidValue>" for "<key>" in header
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorcode in response
    And error message should be "<error_message>" within the response
    Examples:
      | key          | invalidValue                         | http_status | error_message                     | error_code | error_description                                                 | applicationName | peakId                               | subUnitId                            | organisationId                       | platformId                           | description |
      | Content-Type | application/json1                    | 415         | Service Request Validation Failed | EA002      | Content type 'application/json1;charset=ISO-8859-1' not supported | validname       | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 2ee3e4a5-ef45-4fe2-a37d-d5fcfc6adb33 | string      |
      | Accept       | application/json1                    | 406         | Request Header Not Acceptable     | EA008      | Header Accept does not contain required value.  Access denied.    | validname       | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 2ee3e4a5-ef45-4fe2-a37d-d5fcfc6adb33 | string      |
      | Trace-Id     | 12#@!%^&*)                           | 400         | Service Request Validation Failed | EA002      | Failed to convert value of type                                   | validname       | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 2ee3e4a5-ef45-4fe2-a37d-d5fcfc6adb33 | string      |
    #Invalid UUID Trace-Id
      | Trace-Id     | 7454108z-yb37-454c-81da-0a12d8b0f867 | 400         | Service Request Validation Failed | EA002      | Failed to convert value of type                                   | validname       | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 2ee3e4a5-ef45-4fe2-a37d-d5fcfc6adb33 | string      |


  @regression
  Scenario Outline: SC-24-26 - Negative flow- Invalid mandatory field Api-Version provided in header
    Given I am logging in as a user with correct privileges
    When I provide application name as "<applicationName>", peakId as "<peakId>", subUnitId as "<subUnitId>", organisationId as "<organisationId>", description as "<description>" and platformId as "<platformId>" in request body with invalid key "<invalidValue>" for "<key>" in header
    Then I should receive a "<statusCode>" status code in response
    And error message should be "<message>" within the response
    Examples:
      | key         | invalidValue | statusCode | message            | applicationName | peakId                               | subUnitId                            | organisationId                       | platformId                           | description |
      | Api-Version | 0.20         | 404        | Resource not found | validname       | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 2ee3e4a5-ef45-4fe2-a37d-d5fcfc6adb33 | string      |
      | Api-Version | abc          | 404        | Resource not found | validname       | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 2ee3e4a5-ef45-4fe2-a37d-d5fcfc6adb33 | string      |
      | Api-Version | @#$%^        | 404        | Resource not found | validname       | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 2ee3e4a5-ef45-4fe2-a37d-d5fcfc6adb33 | string      |


  @trial @regression
  Scenario Outline: SC-27-30 - Negative flow- Invalid auth token
    Given I am a DRAGON user with invalid "<auth_token>" auth token
    When I make request to one click merchant onboard endpoint
    Then I should receive a "<http_code>" error response with "<error_description>" error description and "<error_code>" errorcode within one click onboard response
    And error message should be "<error_message>" within one click onboard response
    Examples:
      | error_description    | error_message                     | auth_token                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             | error_code | http_code |
 #Auth Token missing
      | Error validating JWT | API Gateway Authentication Failed |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        | EA001      | 401       |
 # Auth token not a JWT
      | Error validating JWT | API Gateway Authentication Failed | random_auth_token                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      | EA001      | 401       |
 # Expired auth token
      | Error validating JWT | API Gateway Authentication Failed | Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Imk2bEdrM0ZaenhSY1ViMkMzbkVRN3N5SEpsWSIsImtpZCI6Imk2bEdrM0ZaenhSY1ViMkMzbkVRN3N5SEpsWSJ9.eyJhdWQiOiI1MTczYTVhOS00MjEyLTQ4NzctODYyMS03YmMxNjRjZjE3OGIiLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yZmY5M2NjZS1lZmQxLTRlMTYtOTJiYS1hZmI1M2U5ZTA5ZmMvIiwiaWF0IjoxNTM3MTY4NzAzLCJuYmYiOjE1MzcxNjg3MDMsImV4cCI6MTUzNzE3MjYwMywiYWlvIjoiNDJCZ1lKaDlXbFZsNFh1SjZFMHZsMDMvY25TOU5nQT0iLCJhcHBpZCI6IjEwOTMyM2UzLWM1NWUtNDI0Yy1iNzEyLTJlODJmMTY5NWU5OCIsImFwcGlkYWNyIjoiMSIsImlkcCI6Imh0dHBzOi8vc3RzLndpbmRvd3MubmV0LzJmZjkzY2NlLWVmZDEtNGUxNi05MmJhLWFmYjUzZTllMDlmYy8iLCJvaWQiOiI4NjM3MjIwNi1jZWZhLTQ1ZWEtOTM0Ni0xNjNiYmFiYzc3MDYiLCJyb2xlcyI6WyJwYXltZW50UmVxdWVzdCIsInJlZnVuZCIsImRldmVsb3BlciJdLCJzdWIiOiI4NjM3MjIwNi1jZWZhLTQ1ZWEtOTM0Ni0xNjNiYmFiYzc3MDYiLCJ0aWQiOiIyZmY5M2NjZS1lZmQxLTRlMTYtOTJiYS1hZmI1M2U5ZTA5ZmMiLCJ1dGkiOiI3bUd1b0FRMUQwZTFGMks0Sy1ZWEFBIiwidmVyIjoiMS4wIn0.gCfn6QNUjzENuKvVN0bdkWgRwN-1ipovSq5Yb8IjCRfRhZNTFffLP1oIVw_8sPQxTFfV8CBOcQG385n-r_tIWBlMANHdKmpmwmOTz4J08EWzz_SY4zWdvMlF7quCYdrXVIXaKd-PLtO0UDTI7CsSAso7OAOsHBNrn3ITBoR0aMo_lM8X5dJM5fleSHFiJPMYDHpawZuy_BBXC0AUdcVT61NHkdO7sDV4Dc_C12CsShEqMi3Nj-uzr5wYHmPTxi3nk2px-_yiVaAHxxCmE0f7rWdM4BjVB89j_xtZrn1-VovgUUn_QDppY9yOgKx831xXBMX0Tz293V8g7BkagVI3lw | EA001      | 401       |
 # Auth token unverified
      | Error validating JWT | API Gateway Authentication Failed | Bearer nbCwW11w3XkB-xUaXwKRSLjMHGQ                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     | EA001      | 401       |


  @regression
  Scenario: SC-31- Negative flow- Request body provided Null {}
    Given I am logging in as a user with correct privileges
    When I make request to one click merchant onboard endpoint with null request body
    Then I should receive a "400" http code with "Service Request Validation Failed" error message
    And Validate errorCode and errorDescription within one click onboard response


  @regression
  Scenario: SC-32- Negative flow- Request body not provided
    Given I am logging in as a user with correct privileges
    When I make request to one click merchant onboard endpoint without request body
    Then I should receive a "400" error response with "Unable to read or parse message body" error description and "EA002" errorcode within one click onboard response
    And error message should be "Service Request Validation Failed" within one click onboard response


  @regression
  Scenario Outline: SC-33-38- Negative flow- Mandatory fields provided null in request body
    Given I am logging in as a user with correct privileges
    When I make request for a new client with name as "<applicationName>", peakId as "<peakId>", subUnitId as "<subUnitId>", organisationId as "<organisationId>", description as "<description>" and platformId as "<platformId>"
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorcode in response
    And error message should be "<error_message>" within the response

    Examples:
      | applicationName | peakId                               | subUnitId                            | organisationId                       | platformId                           | description | http_status | error_description                                                                                            | error_code | error_message                     |
      |                 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 2ee3e4a5-ef45-4fe2-a37d-d5fcfc6adb33 | string      | 400         | applicationNameErrorDescription                                                                              | EA002      | Service Request Validation Failed |
      | validname       |                                      | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 2ee3e4a5-ef45-4fe2-a37d-d5fcfc6adb33 | string      | 400         | Field error in object 'onboardingInputModel': field 'peakId' must not be null; rejected value [null]         | EA002      | Service Request Validation Failed |
      | validname       | 859cce3f-f3da-4448-9e88-cf8450aea289 |                                      | 859cce3f-f3da-4448-9e88-cf8450aea289 | 2ee3e4a5-ef45-4fe2-a37d-d5fcfc6adb33 | string      | 400         | Field error in object 'onboardingInputModel': field 'subUnitId' must not be null; rejected value [null]      | EA002      | Service Request Validation Failed |
      | validname       | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 |                                      | 2ee3e4a5-ef45-4fe2-a37d-d5fcfc6adb33 | string      | 400         | Field error in object 'onboardingInputModel': field 'organisationId' must not be null; rejected value [null] | EA002      | Service Request Validation Failed |
      | validname       | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 |                                      | string      | 400         | Field error in object 'onboardingInputModel': field 'platformId' must not be null; rejected value [null]     | EA002      | Service Request Validation Failed |
      | validname       | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 2ee3e4a5-ef45-4fe2-a37d-d5fcfc6adb33 |             | 400         | Application description not provided                                                                         | EA002      | Service Request Validation Failed |


  @regression
  Scenario Outline: SC-39 - Negative flow- Mandatory field applicationName not provided in request body
    Given I am logging in as a user with correct privileges
    When I make request for a new client with peakId as "<peakId>", subUnitId as "<subUnitId>", organisationId as "<organisationId>", description as "<description>" and platformId as "<platformId>"
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorcode in response
    And error message should be "<error_message>" within the response
    Examples:
      | peakId                               | subUnitId                            | organisationId                       | platformId                           | description | http_status | error_description                                                                                             | error_code | error_message                     |
      | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 2ee3e4a5-ef45-4fe2-a37d-d5fcfc6adb33 | string      | 400         | Field error in object 'onboardingInputModel': field 'applicationName' must not be null; rejected value [null] | EA002      | Service Request Validation Failed |


  @regression
  Scenario Outline: SC-40 - Negative flow- Mandatory field peakId not provided in request body
    Given I am logging in as a user with correct privileges
    When I make request for a new client with name as "<applicationName>", subUnitId as "<subUnitId>", organisationId as "<organisationId>", description as "<description>" and platformId as "<platformId>"
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorcode in response
    And error message should be "<error_message>" within the response
    Examples:
      | applicationName | subUnitId                            | organisationId                       | platformId                           | description | http_status | error_description                                                                                    | error_code | error_message                     |
      | validname       | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 2ee3e4a5-ef45-4fe2-a37d-d5fcfc6adb33 | string      | 400         | Field error in object 'onboardingInputModel': field 'peakId' must not be null; rejected value [null] | EA002      | Service Request Validation Failed |


  @regression
  Scenario Outline: SC-41 - Negative flow- Mandatory field subUnitId not provided in request body
    Given I am logging in as a user with correct privileges
    When I make request for a new client with name as "<applicationName>", peakId as "<peakId>", organisationId as "<organisationId>", description as "<description>" and platformId as "<platformId>"
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorcode in response
    And error message should be "<error_message>" within the response
    Examples:
      | applicationName | peakId                               | organisationId                       | platformId                           | description | http_status | error_description                                                                                       | error_code | error_message                     |
      | validname       | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 2ee3e4a5-ef45-4fe2-a37d-d5fcfc6adb33 | string      | 400         | Field error in object 'onboardingInputModel': field 'subUnitId' must not be null; rejected value [null] | EA002      | Service Request Validation Failed |


  @regression
  Scenario Outline: SC-42 - Negative flow- Mandatory field organisationId not provided in request body
    Given I am logging in as a user with correct privileges
    When I make request for a new client with name as "<applicationName>", peakId as "<peakId>", subUnitId as "<subUnitId>", description as "<description>" and platformId as "<platformId>"
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorcode in response
    And error message should be "<error_message>" within the response
    Examples:
      | applicationName | peakId                               | subUnitId                            | platformId                           | description | http_status | error_description                                                                                            | error_code | error_message                     |
      | validname       | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 2ee3e4a5-ef45-4fe2-a37d-d5fcfc6adb33 | string      | 400         | Field error in object 'onboardingInputModel': field 'organisationId' must not be null; rejected value [null] | EA002      | Service Request Validation Failed |


  @regression
  Scenario Outline: SC-43 - Negative flow- Mandatory field platformId not provided in request body
    Given I am logging in as a user with correct privileges
    When I make request for a new client with name as "<applicationName>", peakId as "<peakId>", subUnitId as "<subUnitId>", organisationId as "<organisationId>" and description as "<description>"
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorcode in response
    And error message should be "<error_message>" within the response
    Examples:
      | applicationName | peakId                               | subUnitId                            | organisationId                       | description | http_status | error_description                                                                                        | error_code | error_message                     |
      | validname       | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | string      | 400         | Field error in object 'onboardingInputModel': field 'platformId' must not be null; rejected value [null] | EA002      | Service Request Validation Failed |


  @regression
  Scenario Outline: SC-44 - Negative flow- Mandatory field applicationDescription not provided in request body
    Given I am logging in as a user with correct privileges
    When I make request for a new client with name as "<applicationName>", peakId as "<peakId>", subUnitId as "<subUnitId>", organisationId as "<organisationId>" and platformId as "<platformId>"
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorcode in response
    And error message should be "<error_message>" within the response
    Examples:
      | applicationName | peakId                               | subUnitId                            | organisationId                       | platformId                           | http_status | error_description                                                                                                    | error_code | error_message                     |
      | validname       | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 2ee3e4a5-ef45-4fe2-a37d-d5fcfc6adb33 | 400         | Field error in object 'onboardingInputModel': field 'applicationDescription' must not be null; rejected value [null] | EA002      | Service Request Validation Failed |


  @regression
  Scenario Outline: SC-45-60 Negative flow- Invalid Mandatory fields provided in request body
    Given I am logging in as a user with correct privileges
    When I make request for a new client with name as "<applicationName>", peakId as "<peakId>", subUnitId as "<subUnitId>", organisationId as "<organisationId>", description as "<description>" and platformId as "<platformId>"
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorcode in response
    And error message should be "<error_message>" within the response
    Examples:
      | applicationName     | peakId                               | subUnitId                            | organisationId                        | platformId                           | description | http_status | error_description                                      | error_code | error_message                     |
      | ^%$@#*              | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289  | 2ee3e4a5-ef45-4fe2-a37d-d5fcfc6adb33 | string      | 400         | applicationNameErrorDescription                        | EA002      | Service Request Validation Failed |
      | test123             | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289  | 2ee3e4a5-ef45-4fe2-a37d-d5fcfc6adb33 | string      | 400         | applicationNameErrorDescription                        | EA002      | Service Request Validation Failed |
      | merchant-client-app | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289  | 2ee3e4a5-ef45-4fe2-a37d-d5fcfc6adb33 | string      | 400         | applicationNameErrorDescription                        | EA002      | Service Request Validation Failed |
      | validname           | 859cce3                              | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289  | 2ee3e4a5-ef45-4fe2-a37d-d5fcfc6adb33 | string      | 400         | Unable to read or parse message body: json parse error | EA002      | Service Request Validation Failed |
      | validname           | $#@!~&                               | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289  | 2ee3e4a5-ef45-4fe2-a37d-d5fcfc6adb33 | string      | 400         | Unable to read or parse message body: json parse error | EA002      | Service Request Validation Failed |
      | validname           | xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289  | 2ee3e4a5-ef45-4fe2-a37d-d5fcfc6adb33 | string      | 400         | Unable to read or parse message body: json parse error | EA002      | Service Request Validation Failed |
      | validname           | 859cce3f-f3da-4448-9e88-cf8450aea289 | $#@!~&                               | 859cce3f-f3da-4448-9e88-cf8450aea289  | 2ee3e4a5-ef45-4fe2-a37d-d5fcfc6adb33 | string      | 400         | Unable to read or parse message body: json parse error | EA002      | Service Request Validation Failed |
      | validname           | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f9                            | 859cce3f-f3da-4448-9e88-cf8450aea289  | 2ee3e4a5-ef45-4fe2-a37d-d5fcfc6adb33 | string      | 400         | Unable to read or parse message body: json parse error | EA002      | Service Request Validation Failed |
      | validname           | 859cce3f-f3da-4448-9e88-cf8450aea289 | xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx | 859cce3f-f3da-4448-9e88-cf8450aea289  | 2ee3e4a5-ef45-4fe2-a37d-d5fcfc6adb33 | string      | 400         | Unable to read or parse message body: json parse error | EA002      | Service Request Validation Failed |
      | validname           | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | $#@!~&                                | 2ee3e4a5-ef45-4fe2-a37d-d5fcfc6adb33 | string      | 400         | Unable to read or parse message body: json parse error | EA002      | Service Request Validation Failed |
      | validname           | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | vhgkd859cce3f9                        | 2ee3e4a5-ef45-4fe2-a37d-d5fcfc6adb33 | string      | 400         | Unable to read or parse message body: json parse error | EA002      | Service Request Validation Failed |
      | validname           | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx  | 2ee3e4a5-ef45-4fe2-a37d-d5fcfc6adb33 | string      | 400         | Unable to read or parse message body: json parse error | EA002      | Service Request Validation Failed |
      | validname           | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450a ea289 | 13ac$@#!%                            | string      | 400         | Unable to read or parse message body: json parse error | EA002      | Service Request Validation Failed |
      | validname           | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289  | vhgkd859cce3f9                       | string      | 400         | Unable to read or parse message body: json parse error | EA002      | Service Request Validation Failed |
      | validname           | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289  | xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx | string      | 400         | Unable to read or parse message body: json parse error | EA002      | Service Request Validation Failed |
      | validname           | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289  | 2d5cbfe7-86c5-40ed-a58f-bade080dd7e6 | string      | 400         | Platform Id is invalid.                                | EA002      | Service Request Validation Failed |
#     | validname       | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | Shopline  |  1a@b#4!&    | 400         |                                                                                                                      | EA002      | Service Request Validation Failed |

#applicationDescription is free text; can be anything


  @regression
  Scenario Outline: SC-61 - Negative flow- Invalid long applicationName provided in request body
    Given I am logging in as a user with correct privileges
    When I make request for a new client with name as "<applicationName>", peakId as "<peakId>", subUnitId as "<subUnitId>", organisationId as "<organisationId>", description as "<description>" and platformId as "<platformId>"
    Then I should receive a "400" http code with "Service Request Validation Failed" error message
    And Validate errorCodes and errorDescriptions in response
    Examples:
      | applicationName | peakId                               | subUnitId                            | organisationId                       | platformId                           | description |
      | longname        | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 2ee3e4a5-ef45-4fe2-a37d-d5fcfc6adb33 | string      |

  @regression
  Scenario Outline: SC-62 - Negative flow- Invalid long applicationDescription provided in request body
    Given I am logging in as a user with correct privileges
    When I make request for a new client with name as "<applicationName>", peakId as "<peakId>", subUnitId as "<subUnitId>", organisationId as "<organisationId>", description as "<description>" and platformId as "<platformId>"
    Then I should receive a "400" http code with "Service Request Validation Failed" error message
    And Validate errorCode "<error_code>" and errorDescription "<error_description>" in response for long applicationDescription
    Examples:
      | applicationName | peakId                               | subUnitId                            | organisationId                       | platformId                           | description     | error_description                                                                                                           | error_code |
      | validname       | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 2ee3e4a5-ef45-4fe2-a37d-d5fcfc6adb33 | longdescription | Field error in object 'onboardingInputModel': field 'applicationDescription' size must be between 0 and 256; rejected value | EA002      |


  @regression @skiponversionten @skiponversioneleven
  Scenario Outline: SC-63 - Positive flow - A DRAGON user with All privilege is onboarded successfully with POST On-Boarding API
    Given I am logging in as a user with correct privileges
    When I make request for a new client with name as "<applicationName>", peakId as "<peakId>", subUnitId as "<subUnitId>", organisationId as "<organisationId>", description as "<description>" and platformId as "<platformId>"
    Then I should receive a successful merchant onboarding response
    And validate response have the valid values
    Examples:
      | applicationName | peakId                               | subUnitId                            | organisationId                       | platformId                           | description |
      | validName       | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 859cce3f-f3da-4448-9e88-cf8450aea289 | 2ee3e4a5-ef45-4fe2-a37d-d5fcfc6adb33 | description |


   #Onboard merchant on two platform with the same application name
  # On-board merchant on deactivated platform valid
  # double Hyphon in applicationName before userType
  # SubUnit ID should exist in Database of PM4B
  # Space and space in Double quotes of every input body field