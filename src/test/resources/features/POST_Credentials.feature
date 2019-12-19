@postKeys
Feature: POST_Credentials - POST Credentials Merchant - DRAG-2176
  As a user
  I want to up to credentials for merchant and validate correct response is returned

  Background: Retrieving access Token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

  @regression
  Scenario Outline: SC-1-5 Positive flow - Create a new credentials, new signing key and password
    Given I am an authorized to create credentials as DRAGON user
    When I hit the post credentials endpoint with credential name "<credentialName>"
    Then the create credentials response should be successful
    Examples:
      | credentialName |
      | validName      |
      | spaceInQuotes  |
      | doubleQuotes   |
      | $^&$^#$%^^^^^^ |
      | t1s2t3i4n5g6   |


  @regression
  Scenario Outline: SC-6 Positive flow - Merchant can have maximum five active credentials, new signing keys and passwords
    Given I am an authorized to create credentials as DRAGON user
    When I hit the post credentials endpoint six times with same credential name "<credentialName>"
    Then I should receive a "<response_code>" error response with "<error_description>" error description and "<error_code>" errorCode within create credentials response
    And error message should be "<error_message>" within create credentials response
    Examples:
      | credentialName | response_code | error_description                                           | error_code | error_message             |
      | validName      | 400           | There can be only 5 active credentials at any point in time | EA002      | Business Rules Incorrect! |


  @regression
  Scenario Outline: SC-7 Positive flow -A merchant cannot have same name for two credentials
    Given I am an authorized to create credentials as DRAGON user
    When I hit the post credentials endpoint second times with same credential name "<credentialName>"
    Then I should receive a "<response_code>" error response with "<error_description>" error description and "<error_code>" errorCode within create credentials response
    And error message should be "<error_message>" within create credentials response
    Examples:
      | credentialName | response_code | error_description                                        | error_code | error_message             |
      | validName      | 400           | The Name is already in use for another ACTIVE KEY (NAME) | EA002      | Business Rules Incorrect! |

  @regression
  Scenario Outline: SC-8 Positive flow -A merchant cannot create credentials without credentials name
    Given I am an authorized to create credentials as DRAGON user
    When I hit the post credentials endpoint without credential name "<credentialName>"
    Then I should receive a "<response_code>" error response with "<error_description>" error description and "<error_code>" errorCode within create credentials response
    And error message should be "<error_message>" within create credentials response
    Examples:
      | credentialName | response_code | error_code | error_message                     | error_description                                                                                            |
      | validName      | 400           | EA050      | Service Request Validation Failed | Field error in object 'onboardingInputModel': field 'credentialName' must not be null; rejected value [null] |

   #bug
  @regression
  Scenario Outline: SC-9 Positive flow -A merchant cannot create credentials without request body
    Given I am an authorized to create credentials as DRAGON user
    When I hit the post credentials endpoint without request body
    Then I should receive a "<response_code>" error response with "<error_description>" error description and "<error_code>" errorCode within create credentials response
    And error message should be "<error_message>" within create credentials response
    Examples:
      | response_code | error_code | error_message                     | error_description                    |
      | 400           | EA002      | Service Request Validation Failed | Unable to read or parse message body |

    #bug
  @regression @token
  Scenario Outline: SC-10-14  Negative flow- Merchant cannot access APIs with Invalid auth token
    And I send invalid auth token "<auth_token>" to create credentials
    When I hit the post credentials endpoint with credential name "<credentialName>"
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorCode within create credentials response
    And error message should be "<error_message>" within create credentials response


    Examples:
      | credentialName | error_description    | http_status | error_message                     | auth_token                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      | error_code |
 #Auth Token missing
      | testing        | Error validating JWT | 401         | API Gateway Authentication Failed |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 | EA001      |
 # Auth token not a JWT
      | testing1       | Error validating JWT | 401         | API Gateway Authentication Failed | random_auth_token                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               | EA001      |
 # Expired auth token
      | testing2       | Error validating JWT | 401         | API Gateway Authentication Failed | eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Imk2bEdrM0ZaenhSY1ViMkMzbkVRN3N5SEpsWSIsImtpZCI6Imk2bEdrM0ZaenhSY1ViMkMzbkVRN3N5SEpsWSJ9.eyJhdWQiOiI1MTczYTVhOS00MjEyLTQ4NzctODYyMS03YmMxNjRjZjE3OGIiLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yZmY5M2NjZS1lZmQxLTRlMTYtOTJiYS1hZmI1M2U5ZTA5ZmMvIiwiaWF0IjoxNTM3MTY4NzAzLCJuYmYiOjE1MzcxNjg3MDMsImV4cCI6MTUzNzE3MjYwMywiYWlvIjoiNDJCZ1lKaDlXbFZsNFh1SjZFMHZsMDMvY25TOU5nQT0iLCJhcHBpZCI6IjEwOTMyM2UzLWM1NWUtNDI0Yy1iNzEyLTJlODJmMTY5NWU5OCIsImFwcGlkYWNyIjoiMSIsImlkcCI6Imh0dHBzOi8vc3RzLndpbmRvd3MubmV0LzJmZjkzY2NlLWVmZDEtNGUxNi05MmJhLWFmYjUzZTllMDlmYy8iLCJvaWQiOiI4NjM3MjIwNi1jZWZhLTQ1ZWEtOTM0Ni0xNjNiYmFiYzc3MDYiLCJyb2xlcyI6WyJwYXltZW50UmVxdWVzdCIsInJlZnVuZCIsImRldmVsb3BlciJdLCJzdWIiOiI4NjM3MjIwNi1jZWZhLTQ1ZWEtOTM0Ni0xNjNiYmFiYzc3MDYiLCJ0aWQiOiIyZmY5M2NjZS1lZmQxLTRlMTYtOTJiYS1hZmI1M2U5ZTA5ZmMiLCJ1dGkiOiI3bUd1b0FRMUQwZTFGMks0Sy1ZWEFBIiwidmVyIjoiMS4wIn0.gCfn6QNUjzENuKvVN0bdkWgRwN-1ipovSq5Yb8IjCRfRhZNTFffLP1oIVw_8sPQxTFfV8CBOcQG385n-r_tIWBlMANHdKmpmwmOTz4J08EWzz_SY4zWdvMlF7quCYdrXVIXaKd-PLtO0UDTI7CsSAso7OAOsHBNrn3ITBoR0aMo_lM8X5dJM5fleSHFiJPMYDHpawZuy_BBXC0AUdcVT61NHkdO7sDV4Dc_C12CsShEqMi3Nj-uzr5wYHmPTxi3nk2px-_yiVaAHxxCmE0f7rWdM4BjVB89j_xtZrn1-VovgUUn_QDppY9yOgKx831xXBMX0Tz293V8g7BkagVI3lw | EA001      |
 # Auth token unverified
      | testing3       | Error validating JWT | 401         | API Gateway Authentication Failed | eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     | EA001      |

  @regression @apiv
  Scenario Outline: SC-15-19 Positive flow - Merchant should not be able to create credentials using prior to twelve version
    Given I am an authorized to create credentials as DRAGON user
    When I hit the post credentials endpoint with invalid API versions invalid header "<key>" and values "<invalidHeaderValues>"
    Then I should receive a "<httpStatus>" status code with "<statusCode>" message "<message>" with create credentials response

    Examples:
      | key         | invalidHeaderValues | httpStatus | statusCode | message            |
      | Api-Version | 0.20                | 404        | 404        | Resource not found |
      | Api-Version | abc                 | 404        | 404        | Resource not found |
      | Api-Version | 0.10                | 404        | 404        | Resource not found |
      | Api-Version | 0.11                | 404        | 404        | Resource not found |
      | Api-Version | @#$%^               | 404        | 404        | Resource not found |


    #bug
  @regression @post
  Scenario Outline: SC-20-24 Negative flow- Invalid mandatory field provided in header
    Given I am an authorized to create credentials as DRAGON user
    When I hit the post credentials endpoint with invalid API versions invalid header "<key>" and values "<invalidHeaderValues>"
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorCode within create credentials response
    And error message should be "<error_message>" within create credentials response

    Examples:
      | key          | invalidHeaderValues                  | http_status | error_code | error_description                                                                                                                                                  | error_message                     |
      | Accept       | Testing/Type                         | 406         | EA008      | Header Accept does not contain required value.  Access denied.                                                                                                     | Service Request Validation Failed |
      | Content-Type | application/json1                    | 415         | EA002      | Content type 'application/json1;charset=ISO-8859-1' not supported                                                                                                  | Service Request Validation Failed |
      | Trace-Id     | 123456                               | 400         | EA002      | Failed to convert value of type 'java.lang.String' to required type 'java.util.UUID'; nested exception is java.lang.IllegalArgumentException: Invalid UUID string: | Service Request Validation Failed |
      | Trace-Id     | abcde                                | 400         | EA002      | Failed to convert value of type 'java.lang.String' to required type 'java.util.UUID'; nested exception is java.lang.IllegalArgumentException: Invalid UUID string: | Service Request Validation Failed |
      | Trace-Id     | 7454108z-yb37-454c-81da-0a12d8b0f867 | 400         | EA002      | Failed to convert value of type 'java.lang.String' to required type 'java.util.UUID'; nested exception is java.lang.NumberFormatException: For input string:       | Service Request Validation Failed |

    #bug
  @regression @tooLong
  Scenario Outline: SC-25 Positive flow - should not be able to create credential with more than 256 long characters, new signing key and password
    Given I am an authorized to create credentials as DRAGON user
    When I hit the post credentials endpoint with credential name "<credentialName>"
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorCode within create credentials response
    And error message should be "<error_message>" within create credentials response

    Examples:
      | credentialName | http_status | error_code | error_message                     | error_description                                                                                                                    |
      | tooLong        | 400         | EA002      | Service Request Validation Failed | Field error in object 'createApplicationCredentialInputModel': field 'credentialName' size must be between 0 and 256; rejected value |


    #bug
  @regression
  Scenario Outline: SC-26-29 Negative flow- missing mandatory field provided in header
    Given I am an authorized to create credentials as DRAGON user
    When I hit the post credentials endpoint with missing header keys "<key>"
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorCode within create credentials response
    And error message should be "<error_message>" within create credentials response
    Examples:
      | key           | error_description                                              | error_code | http_status | error_message                     |
      | Authorization | Error validating JWT                                           | EA001      | 401         | API Gateway Authentication Failed |
      | Trace-Id      | Header Trace-Id was not found in the request. Access denied.   | EA002      | 400         | API Gateway Authentication Failed |
      | Accept        | Header Accept does not contain required value.  Access denied. | EA008      | 406         | API Gateway Authentication Failed |
      | Content-Type  | Content type                                                   | EA002      | 415         | API Gateway Authentication Failed |


  @regression
  Scenario Outline: SC-30-35 Negative flow - Create a new credentials with invalid applicationId
    Given I am an authorized to create credentials as DRAGON user
    When I hit the post credentials endpoint with invalid applicationId "<applicationId>" and valid credential name "<credentialName>"
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorCode within create credentials response
    And error message should be "<error_message>" within create credentials response
    Examples:
      | credentialName | applicationId                         | error_code | http_status | error_message                     | error_description                                                                                                                                                  |
      | validName      | abcde                                 | EA002      | 400         | Service Request Validation Failed | Failed to convert value of type 'java.lang.String' to required type 'java.util.UUID'; nested exception is java.lang.IllegalArgumentException: Invalid UUID string: |
#      | validName      | 8e093bb5-da9b-4b27-b991-6171e9f7c5    | EA025      | 400         | Service Request Validation Failed | Application Id not found                                                                                                                                           |
      | validName      | 1234                                  | EA002      | 400         | Service Request Validation Failed | Failed to convert value of type 'java.lang.String' to required type 'java.util.UUID'; nested exception is java.lang.IllegalArgumentException: Invalid UUID string: |
      | validName      | !~@^*                                 | EA002      | 400         | Service Request Validation Failed | Failed to convert value of type 'java.lang.String' to required type 'java.util.UUID'; nested exception is java.lang.IllegalArgumentException: Invalid UUID string: |
      | validName      | xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxxx | EA002      | 400         | Service Request Validation Failed | Failed to convert value of type 'java.lang.String' to required type 'java.util.UUID'; nested exception is java.lang.IllegalArgumentException: Invalid UUID string: |



    