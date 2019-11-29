@healthCheck @skiponversionten @skiponversiontwelve
Feature: Management GET platform API - DRAG-2029
  As a platform user
  i want retrieve existing platform information using GET platform

  Background: Retrieving access Token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

  # @trial
  @regression
  Scenario: Positive flow- A DRAGON user with platform.ReadWrite.All is able to update an existing application
    Given I am a GET platform authorized DRAGON user with Platform.ReadWrite.All
    When I make a GET request to the platform endpoint
    Then I should receive a successful GET platform response

  #trial
  @regression
  Scenario Outline: Positive flow - Get a list of platform using platform name, platform id
    Given I am a GET platform authorized DRAGON user with Platform.ReadWrite.All
    When I get a list of platform "<numberOfResponses>" using filter "<filterName>"
    Then I should receive a successful GET platform response

    Examples:
      | filterName   | numberOfResponses |
      | platformId   | 1                 |
      | platformName | 1                 |


  #trial
  @regression
  Scenario Outline: Positive flow - Get a list of platform using platform status
    Given I am a GET platform authorized DRAGON user with Platform.ReadWrite.All
    When I make a GET request to the platform endpoint
    When I get a list of platform using filter "<filterName>" and filter value "<filterValue>"
    Then I should receive a successful GET platform response
    And validate the all items list of platform should have active status
    Examples:
      | filterName | filterValue |
      | status     | A           |

  #trial
  @regression
  Scenario Outline: Negative flow- POST Platform with missing mandatory header values
    Given I am a GET platform authorized DRAGON user with Platform.ReadWrite.All
    When I make a GET request to the Platform endpoint with "<key>" missing in the header
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" error code within the GET platform response
    And error message should be "<error_message>" within the GET platform response
    Examples:
      | error_description                                              | error_message                     | key           | error_code | http_status |
      | Error validating JWT                                           | API Gateway Authentication Failed | Authorization | EA001      | 401         |
      | Header Trace-Id was not found in the request. Access denied.   | API Gateway Validation Failed     | Trace-Id      | EA002      | 400         |
      | Header Accept does not contain required value.  Access denied. | Request Header Not Acceptable     | Accept        | EA008      | 406         |
      | Content type '' not supported                                  | Service Request Validation Failed | Content-Type  | EA002      | 415         |


  #@trial
  @regression
  Scenario Outline: Negative flow- Mandatory fields Api-Version not sent in the header
    Given I am a GET platform authorized DRAGON user with Platform.ReadWrite.All
    When I make a GET request to the Platform endpoint with "<key>" missing in the header
    And error message should be "Resource not found" within the GET platform response
    Examples:
      | key         |
      | Api-Version |

  @trial
  @regression
  Scenario Outline: Negative flow- Invalid auth token
    Given I am a GET dragon DRAGON user with Platform.ReadWrite.All with invalid "<auth_token>"
    When I make a GET request to the platform endpoint
    Then I should receive a "<http_code>" error response with "<error_description>" error description and "<error_code>" error code within the GET platform response
    And error message should be "<error_message>" within the GET platform response
    Examples:
      | error_description    | error_message                     | auth_token                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             | error_code | http_code |
 #Auth Token missing
      | Error validating JWT | API Gateway Authentication Failed |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        | EA001      | 401       |
 # Auth token not a JWT
      | Error validating JWT | API Gateway Authentication Failed | random_ auth_token                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     | EA001      | 401       |
 # Expired auth token
      | Error validating JWT | API Gateway Authentication Failed | Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Imk2bEdrM0ZaenhSY1ViMkMzbkVRN3N5SEpsWSIsImtpZCI6Imk2bEdrM0ZaenhSY1ViMkMzbkVRN3N5SEpsWSJ9.eyJhdWQiOiI1MTczYTVhOS00MjEyLTQ4NzctODYyMS03YmMxNjRjZjE3OGIiLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yZmY5M2NjZS1lZmQxLTRlMTYtOTJiYS1hZmI1M2U5ZTA5ZmMvIiwiaWF0IjoxNTM3MTY4NzAzLCJuYmYiOjE1MzcxNjg3MDMsImV4cCI6MTUzNzE3MjYwMywiYWlvIjoiNDJCZ1lKaDlXbFZsNFh1SjZFMHZsMDMvY25TOU5nQT0iLCJhcHBpZCI6IjEwOTMyM2UzLWM1NWUtNDI0Yy1iNzEyLTJlODJmMTY5NWU5OCIsImFwcGlkYWNyIjoiMSIsImlkcCI6Imh0dHBzOi8vc3RzLndpbmRvd3MubmV0LzJmZjkzY2NlLWVmZDEtNGUxNi05MmJhLWFmYjUzZTllMDlmYy8iLCJvaWQiOiI4NjM3MjIwNi1jZWZhLTQ1ZWEtOTM0Ni0xNjNiYmFiYzc3MDYiLCJyb2xlcyI6WyJwYXltZW50UmVxdWVzdCIsInJlZnVuZCIsImRldmVsb3BlciJdLCJzdWIiOiI4NjM3MjIwNi1jZWZhLTQ1ZWEtOTM0Ni0xNjNiYmFiYzc3MDYiLCJ0aWQiOiIyZmY5M2NjZS1lZmQxLTRlMTYtOTJiYS1hZmI1M2U5ZTA5ZmMiLCJ1dGkiOiI3bUd1b0FRMUQwZTFGMks0Sy1ZWEFBIiwidmVyIjoiMS4wIn0.gCfn6QNUjzENuKvVN0bdkWgRwN-1ipovSq5Yb8IjCRfRhZNTFffLP1oIVw_8sPQxTFfV8CBOcQG385n-r_tIWBlMANHdKmpmwmOTz4J08EWzz_SY4zWdvMlF7quCYdrXVIXaKd-PLtO0UDTI7CsSAso7OAOsHBNrn3ITBoR0aMo_lM8X5dJM5fleSHFiJPMYDHpawZuy_BBXC0AUdcVT61NHkdO7sDV4Dc_C12CsShEqMi3Nj-uzr5wYHmPTxi3nk2px-_yiVaAHxxCmE0f7rWdM4BjVB89j_xtZrn1-VovgUUn_QDppY9yOgKx831xXBMX0Tz293V8g7BkagVI3lw | EA001      | 401       |
 # Auth token unverified
      | Error validating JWT | API Gateway Authentication Failed | Bearer nbCwW11w3XkB-xUaXwKRSLjMHGQ                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     | EA001      | 401       |
