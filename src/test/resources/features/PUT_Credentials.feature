@PutCredentials
Feature: PUT_Credentials - PUT Credentials Merchant
  As a user
  I want to create up to credentials for merchant and validate correct response is returned

  Background: Retrieving access Token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

  @regression
  Scenario Outline: SC-1 Positive flow - Update credentials name for existing credential
    Given I am an authorized to put credentials as DRAGON user
    When I hit the put credentials endpoint with new credential name "<credentialName>"
    Then put credentials response should be successful
    Examples:
      | credentialName  |
      | validName       |
      | $^&$^#$%^^^^^^  |
      | t1s2t3i4n5g6    |
      | &testing        |
      | -testging       |
      | _testing        |
      | @testing        |
      | testing testing |

  @regression
  Scenario Outline: SC-9-10 Positive flow - Update credentials with space in quotes and just double quotes in name for existing credential
    Given I am an authorized to put credentials as DRAGON user
    When I hit the put credentials endpoint with new invalid credential name "<credentialName>"
    Then I should receive a "<response_code>" error response with "<error_description>" error description and "<error_code>" errorCode within put credentials response
    And error message should be "<error_message>" within put credentials response
    Examples:
      | credentialName | response_code | error_code | error_message                     | error_description                                      |
      | spaceInQuotes  | 400           | EA002      | Service Request Validation Failed | Atleast one field must be there to update Credentials. |
      | doubleQuotes   | 400           | EA002      | Service Request Validation Failed | Atleast one field must be there to update Credentials. |

  @regression
  Scenario Outline: SC-11 Positive flow - Deactivate the active credential
    Given I am an authorized to put credentials as DRAGON user
    When I hit the Put credentials endpoint with new credential name "<credentialName>" and status "<status>"
    Then put credentials response should be updated
    Examples:
      | credentialName | status |
      | validName      | D      |

  @regression
  Scenario Outline: SC-12 Positive flow - Should not be able to expire the deactivated credential
    Given I am an authorized to put credentials as DRAGON user
    When I hit the Put credentials endpoint with new credential name "<credentialName>" and status "<status>"
    Then I should receive a "<response_code>" error response with "<error_description>" error description and "<error_code>" errorCode within put credentials response
    And error message should be "<error_message>" within put credentials response
    Examples:
      | credentialName | status | response_code | error_code | error_message                     | error_description       |
      | validName      | E      | 400           | EA002      | Service Request Validation Failed | Status can only be 'D'. |

  @regression
  Scenario Outline: SC-13 Positive flow - Should not be able to activate the deactivated credentials
    Given I am an authorized to put credentials as DRAGON user
    When I hit the Put credentials endpoint with new credential name "<credentialName>" and status "<status>"
    Then put credentials response should be updated
    When I hit update API to reactivate the deactivated credentials "<activateCredential>" and credential name "<credentialName>"
    Then I should receive a "<response_code>" error response with "<error_description>" error description and "<error_code>" errorCode within put credentials response
    And error message should be "<error_message>" within put credentials response
    Examples:
      | credentialName | status | activateCredential | response_code | error_code | error_message                     | error_description       |
      | validName      | D      | A                  | 400           | EA002      | Service Request Validation Failed | Status can only be 'D'. |

  @regression
  Scenario Outline: SC-14 Positive flow - Should not be able to expired the deactivated credentials
    Given I am an authorized to put credentials as DRAGON user
    When I hit the Put credentials endpoint with new credential name "<credentialName>" and status "<status>"
    Then put credentials response should be updated
    When I hit update API to reactivate the deactivated credentials "<activateCredential>" and credential name "<credentialName>"
    Then I should receive a "<response_code>" error response with "<error_description>" error description and "<error_code>" errorCode within put credentials response
    And error message should be "<error_message>" within put credentials response
    Examples:
      | credentialName | status | activateCredential | response_code | error_code | error_message                     | error_description       |
      | validName      | D      | E                  | 400           | EA002      | Service Request Validation Failed | Status can only be 'D'. |

  @regression
  Scenario Outline: SC-15 Positive flow - Should not be able to update credentials name same as any existing active credential name
    Given I am an authorized to put credentials as DRAGON user
    When I hit the put credentials endpoint to update new credentials name as existing credential name "<credentialName>"
    Then I should receive a "<response_code>" error response with "<error_description>" error description and "<error_code>" errorCode within put credentials response
    And error message should be "<error_message>" within put credentials response
    Examples:
      | credentialName | response_code | error_code | error_message             | error_description                                                  |
      | validName      | 400           | EA002      | Business Rules Incorrect! | You are already using the same credential name for this credential |

  @regression
  Scenario Outline: SC-16 Positive flow -Create new credential with existing deactivated credential name
    Given I am an authorized to put credentials as DRAGON user
    When I hit the Put credentials endpoint with new credential name "<credentialName>" and status "<status>"
    Then put credentials response should be updated
    When I hit the post credential endpoint with existing deactivated credential name
    Examples:
      | credentialName | status |
      | validName      | D      |

  @regression
  Scenario Outline: SC-17 Positive flow -A merchant cannot update credentials without input body
    Given I am an authorized to put credentials as DRAGON user
    When I hit the put credentials endpoint with without input body
    Then I should receive a "<response_code>" error response with "<error_description>" error description and "<error_code>" errorCode within put credentials response
    And error message should be "<error_message>" within put credentials response
    Examples:
      | response_code | error_code | error_message                     | error_description                    |
      | 400           | EA002      | Service Request Validation Failed | Unable to read or parse message body |

  @regression
  Scenario Outline: SC-18 Positive flow -A merchant cannot update credentials with empty input body
    Given I am an authorized to put credentials as DRAGON user
    When I hit the put credentials endpoint with empty input body
    Then I should receive a "<response_code>" error response with "<error_description>" error description and "<error_code>" errorCode within put credentials response
    And error message should be "<error_message>" within put credentials response
    Examples:
      | response_code | error_code | error_message                     | error_description                                      |
      | 400           | EA002      | Service Request Validation Failed | Atleast one field must be there to update Credentials. |

  @regression
  Scenario Outline: SC-19-22  Negative flow- Merchant cannot access APIs with Invalid auth token
    And I send invalid auth token "<auth_token>" to put credentials
    When I hit the put credentials endpoint with new credential name "<credentialName>"
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorCode within put credentials response
    And error message should be "<error_message>" within put credentials response
    Examples:
      | credentialName | error_description    | http_status | error_message                     | error_code | auth_token                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
 #Auth Token missing
      | testing        | Error validating JWT | 401         | API Gateway Authentication Failed | EA001      |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 |
 # Auth token not a JWT
      | testing1       | Error validating JWT | 401         | API Gateway Authentication Failed | EA001      | random_auth_token                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               |
 # Expired auth token
      | testing2       | Error validating JWT | 401         | API Gateway Authentication Failed | EA001      | eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Imk2bEdrM0ZaenhSY1ViMkMzbkVRN3N5SEpsWSIsImtpZCI6Imk2bEdrM0ZaenhSY1ViMkMzbkVRN3N5SEpsWSJ9.eyJhdWQiOiI1MTczYTVhOS00MjEyLTQ4NzctODYyMS03YmMxNjRjZjE3OGIiLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yZmY5M2NjZS1lZmQxLTRlMTYtOTJiYS1hZmI1M2U5ZTA5ZmMvIiwiaWF0IjoxNTM3MTY4NzAzLCJuYmYiOjE1MzcxNjg3MDMsImV4cCI6MTUzNzE3MjYwMywiYWlvIjoiNDJCZ1lKaDlXbFZsNFh1SjZFMHZsMDMvY25TOU5nQT0iLCJhcHBpZCI6IjEwOTMyM2UzLWM1NWUtNDI0Yy1iNzEyLTJlODJmMTY5NWU5OCIsImFwcGlkYWNyIjoiMSIsImlkcCI6Imh0dHBzOi8vc3RzLndpbmRvd3MubmV0LzJmZjkzY2NlLWVmZDEtNGUxNi05MmJhLWFmYjUzZTllMDlmYy8iLCJvaWQiOiI4NjM3MjIwNi1jZWZhLTQ1ZWEtOTM0Ni0xNjNiYmFiYzc3MDYiLCJyb2xlcyI6WyJwYXltZW50UmVxdWVzdCIsInJlZnVuZCIsImRldmVsb3BlciJdLCJzdWIiOiI4NjM3MjIwNi1jZWZhLTQ1ZWEtOTM0Ni0xNjNiYmFiYzc3MDYiLCJ0aWQiOiIyZmY5M2NjZS1lZmQxLTRlMTYtOTJiYS1hZmI1M2U5ZTA5ZmMiLCJ1dGkiOiI3bUd1b0FRMUQwZTFGMks0Sy1ZWEFBIiwidmVyIjoiMS4wIn0.gCfn6QNUjzENuKvVN0bdkWgRwN-1ipovSq5Yb8IjCRfRhZNTFffLP1oIVw_8sPQxTFfV8CBOcQG385n-r_tIWBlMANHdKmpmwmOTz4J08EWzz_SY4zWdvMlF7quCYdrXVIXaKd-PLtO0UDTI7CsSAso7OAOsHBNrn3ITBoR0aMo_lM8X5dJM5fleSHFiJPMYDHpawZuy_BBXC0AUdcVT61NHkdO7sDV4Dc_C12CsShEqMi3Nj-uzr5wYHmPTxi3nk2px-_yiVaAHxxCmE0f7rWdM4BjVB89j_xtZrn1-VovgUUn_QDppY9yOgKx831xXBMX0Tz293V8g7BkagVI3lw |
 # Auth token unverified
      | testing3       | Error validating JWT | 401         | API Gateway Authentication Failed | EA001      | eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |

  @regression
  Scenario Outline: SC-23-27 Positive flow - Merchant should not be able to put credentials using prior to twelve version
    Given I am an authorized to put credentials as DRAGON user
    When I hit the put credentials endpoint with invalid API versions invalid header "<key>" and values "<invalidHeaderValues>"
    Then I should receive a "<httpStatus>" status code with "<statusCode>" message "<message>" with put credentials response
    Examples:
      | key         | invalidHeaderValues | httpStatus | statusCode | message            |
      | Api-Version | 0.20                | 404        | 404        | Resource not found |
      | Api-Version | abc                 | 404        | 404        | Resource not found |
      | Api-Version | 0.10                | 404        | 404        | Resource not found |
      | Api-Version | 0.11                | 404        | 404        | Resource not found |
      | Api-Version | @#$%^               | 404        | 404        | Resource not found |

  @regression @skiponversionten @skiponversioneleven
  Scenario Outline: SC-28-33 Negative flow - update credentials with invalid applicationId
    Given I am an authorized to put credentials as DRAGON user
    When I hit the put credentials endpoint with invalid credential name "<credentialName>" and valid application id "<applicationId>"
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorCode within put credentials response
    And error message should be "<error_message>" within put credentials response
    Examples:
      | credentialName | applicationId       | error_code | http_status | error_message                     | error_description                                                   |
#      | validName      | abcde                                 | EA002      | 400         | Service Request Validation Failed | Failed to convert value of type 'java.lang.String' to required type 'java.util.UUID'; nested exception is java.lang.IllegalArgumentException: Invalid UUID string: |
#      | validName      | 1234                                  | EA002      | 400         | Service Request Validation Failed | Failed to convert value of type 'java.lang.String' to required type 'java.util.UUID'; nested exception is java.lang.IllegalArgumentException: Invalid UUID string: |
#      | validName      | !~@^*                                 | EA002      | 400         | Service Request Validation Failed | Failed to convert value of type 'java.lang.String' to required type 'java.util.UUID'; nested exception is java.lang.IllegalArgumentException: Invalid UUID string: |
#      | validName      | xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxxx | EA002      | 400         | Service Request Validation Failed | Failed to convert value of type 'java.lang.String' to required type 'java.util.UUID'; nested exception is java.lang.NumberFormatException: For input string:       |
#      | validName      | 859cce3f-f3da-4448-9e88-cf8450aea289  | EA002      | 404         | Resource Not Found!               | Application ID not found                                                                                                                                           |
      | validName      | spaceInDoubleQuotes | EA002      | 400         | Service Request Validation Failed | Please provide application id                                       |
      | validName      | doubleQuotes        | EA002      | 400         | Service Request Validation Failed | Failed to convert value of type 'java.lang.String' to required type |

  @regression
  Scenario Outline: SC-34-38 Negative flow - Should not be able to update credentials with invalid credentials Id
    Given I am an authorized to put credentials as DRAGON user
    When I hit the put credentials endpoint with invalid credential id "<credentialId>" and valid credential name "<credentialName>"
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorCode within put credentials response
    And error message should be "<error_message>" within put credentials response
    Examples:
      | credentialName | credentialId                         | http_status | error_code | error_description                                                 | error_message                     |
      | validName      | 859cce3f-f3da-4448-9e88-cf8450aea289 | 400         | EA002      | Credential/Application Id is invalid or credential is not Active. | Service Request Validation Failed |
      | validName      | 123                                  | 400         | EA002      | Failed to convert value of type                                   | Service Request Validation Failed |
      | validName      | !~^$@                                | 400         | EA002      | Failed to convert value of type                                   | Service Request Validation Failed |
      | validName      | doubleQuotes                         | 400         | EA002      | Failed to convert value of type                                   | Service Request Validation Failed |
      | validName      | spaceInDoubleQuotes                  | 400         | EA002      | Failed to convert value of type                                   | Service Request Validation Failed |

  @regression
  Scenario Outline: SC-39-42 Negative flow- put credentials with missing mandatory field provided in header
    Given I am an authorized to put credentials as DRAGON user
    When I hit the put credentials endpoint with missing header keys "<key>"
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorCode within put credentials response
    And error message should be "<error_message>" within put credentials response
    Examples:
      | key           | error_code | http_status | error_message                     | error_description                                              |
      | Authorization | EA001      | 401         | API Gateway Authentication Failed | Error validating JWT                                           |
      | Trace-Id      | EA002      | 400         | API Gateway Validation Failed     | Header Trace-Id was not found in the request. Access denied.   |
      | Accept        | EA008      | 406         | Request Header Not Acceptable     | Header Accept does not contain required value.  Access denied. |
      | Content-Type  | EA002      | 415         | Service Request Validation Failed | Content type                                                   |

  @regression
  Scenario Outline: SC-43-48 Negative flow- Invalid mandatory field provided in header
    Given I am an authorized to put credentials as DRAGON user
    When I hit the put credentials endpoint with invalid header for "<key>" and values "<invalidHeaderValues>"
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorCode within put credentials response
    And error message should be "<error_message>" within put credentials response
    Examples:
      | key          | invalidHeaderValues                  | http_status | error_code | error_message                     | error_description                                                                                                                                                  |
      | Accept       | Testing/Type                         | 406         | EA008      | Request Header Not Acceptable     | Header Accept does not contain required value.  Access denied.                                                                                                     |
      | Content-Type | application/json1                    | 415         | EA002      | Service Request Validation Failed | Content type 'application/json1;charset=ISO-8859-1' not supported                                                                                                  |
      | Trace-Id     | 123456                               | 400         | EA002      | Service Request Validation Failed | Failed to convert value of type 'java.lang.String' to required type 'java.util.UUID'; nested exception is java.lang.IllegalArgumentException: Invalid UUID string: |
      | Trace-Id     | abcde                                | 400         | EA002      | Service Request Validation Failed | Failed to convert value of type 'java.lang.String' to required type 'java.util.UUID'; nested exception is java.lang.IllegalArgumentException: Invalid UUID string: |
      | Trace-Id     | 7454108z-yb37-454c-81da-0a12d8b0f867 | 400         | EA002      | Service Request Validation Failed | Failed to convert value of type 'java.lang.String' to required type 'java.util.UUID'; nested exception is java.lang.NumberFormatException: For input string:       |

  @regression
  Scenario Outline: SC-50 - Positive flow - should not allow to update credential name of existing expired credentials
    Given I am an authorized to put credentials as DRAGON user
    When I hit the put credentials endpoint to expired credential "<credentialName>"
    When I hit the put credentials endpoint with existing expired credential name "<credentialName>"
    Then I should receive a "<response_code>" error response with "<error_description>" error description and "<error_code>" errorCode within put credentials response
    And error message should be "<error_message>" within put credentials response
    Examples:
      | credentialName | response_code | error_code | error_message                     | error_description                                                 |
      | validName      | 400           | EA002      | Service Request Validation Failed | Credential/Application Id is invalid or credential is not Active. |

  @regression
  Scenario Outline: SC-51 - Positive flow - should not allow to deactivate the expired credentials
    Given I am an authorized to put credentials as DRAGON user
    When I hit the put credentials endpoint to expired credential "<credentialName>"
    When I hit the Put credentials endpoint to deactivate the expired credentials with status "<status>"
    Then I should receive a "<response_code>" error response with "<error_description>" error description and "<error_code>" errorCode within put credentials response
    And error message should be "<error_message>" within put credentials response
    Examples:
      | credentialName | status | response_code | error_code | error_message                     | error_description       |
      | validName      | E      | 400           | EA002      | Service Request Validation Failed | Status can only be 'D'. |

  @regression
  Scenario Outline: SC-52 Positive flow - Should not allow user to activate the already activated credential
    Given I am an authorized to put credentials as DRAGON user
    When I hit the put credentials endpoint with activate the already activated credential "<credentialName>"
    Then I should receive a "<response_code>" error response with "<error_description>" error description and "<error_code>" errorCode within put credentials response
    And error message should be "<error_message>" within put credentials response
    Examples:
      | credentialName | response_code | error_code | error_message                     | error_description       |
      | validName      | 400           | EA002      | Service Request Validation Failed | Status can only be 'D'. |
