
Feature: Management Put Applications API - DRAG-1446
         As a user
         I want update the application information and validate returned response is correct

  Background: Retrieving access Token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

  # For the parametres where values are missing within the table, while creating request, the parameter will not be included at all as a a part of the payload
  
  # @trial
  @regression @merchantManagement @merchantManagementPut
  Scenario Outline: Positive flow- A DRAGON user with Application.ReadWrite.All is able to update an existing application
    Given I am a PUT application authorized DRAGON user with Application.ReadWrite.All
    And I have updated the "<description>" and platformId values
    When I make a PUT request to the application endpoint
    Then I should receive a successful PUT application response
    And validate the put application response
    Examples:
      | description |
      | testingDescription |

    #trial
  @regression @merchantManagement @merchantManagementPut
  Scenario Outline: Positive flow- Put application and verify the application info using get application
    Given I am a PUT application authorized DRAGON user with Application.ReadWrite.All
    And I have updated the "<description>" and platformId values
    When I make a PUT request to the application endpoint
    Then I should receive a successful PUT application response
    And validate the put application response
    Given I am a GET application authorized DRAGON user with the Application.ReadWrite.All privilege
    When I get a list of applications using filters to filter "<filterName>"
    Then I should receive a successful response
    And the response should have a list of <numberOfResponses> applications
    And validate the item list from the response
    Examples:
      | filterName | numberOfResponses | description |
      | clientId   | 1                 | description |


#   @trial
  @regression @merchantManagement @merchantManagementPut
  Scenario Outline: Negative flow- Mandatory fields not sent in the header
    Given I am a PUT application authorized DRAGON user with Application.ReadWrite.All
    And I have updated the "<description>" and platformId values
    When I make a PUT request to the application endpoint with "<key>" missing in the header
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorcode within the PUT application response
    And error message should be "<error_message>" within the PUT application response
    Examples:
      | description        | error_description                                              | error_message                     | key               | error_code | http_status |
      | testingDescription | Error validating JWT                                           | API Gateway Authentication Failed | Authorization     | EA001      | 401         |
      | testingDescription | Request timestamp not a valid RFC3339 date-time                | Service Request Validation Failed | Request-Date-Time | EA002      | 400         |
      | testingDescription | Header Trace-Id was not found in the request. Access denied.   | API Gateway Validation Failed     | Trace-Id          | EA002      | 400         |
      | testingDescription | Header Accept does not contain required value.  Access denied. | Request Header Not Acceptable     | Accept            | EA008      | 406         |
      | testingDescription | Content type 'text/plain;charset=ISO-8859-1' not supported     | Service Request Validation Failed | Content-Type      | EA002      | 415         |

  #@trial
  @regression @merchantManagement @merchantManagementPut
  Scenario Outline: Negative flow- Mandatory fields not sent in the header
    Given I am a PUT application authorized DRAGON user with Application.ReadWrite.All
    When I make a PUT request to the application endpoint with "<key>" missing in the header
    And error message should be "Resource not found" within the PUT application response
    Examples:
      | key         |
      | Api-Version |

   #@trial
  @regression @merchantManagement @merchantManagementPut
  Scenario Outline: Negative flow- Invalid auth token
    Given I am a PUT application DRAGON user with Application.ReadWrite.All with invalid "<auth_token>"
    When I make a PUT request to the application endpoint
    Then I should receive a "<http_code>" error response with "<error_description>" error description and "<error_code>" errorcode within the PUT application response
    And error message should be "<error_message>" within the PUT application response
    Examples:
      | error_description    | error_message                     | auth_token                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             | error_code | http_code |
 #Auth Token missing
      | Error validating JWT | API Gateway Authentication Failed |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        | EA001      | 401       |
 # Auth token not a JWT
      | Error validating JWT | Unexpected API Gateway Exception  | random_auth_token                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      | EA000      | 500       |
 # Expired auth token
      | Error validating JWT | API Gateway Authentication Failed | Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Imk2bEdrM0ZaenhSY1ViMkMzbkVRN3N5SEpsWSIsImtpZCI6Imk2bEdrM0ZaenhSY1ViMkMzbkVRN3N5SEpsWSJ9.eyJhdWQiOiI1MTczYTVhOS00MjEyLTQ4NzctODYyMS03YmMxNjRjZjE3OGIiLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yZmY5M2NjZS1lZmQxLTRlMTYtOTJiYS1hZmI1M2U5ZTA5ZmMvIiwiaWF0IjoxNTM3MTY4NzAzLCJuYmYiOjE1MzcxNjg3MDMsImV4cCI6MTUzNzE3MjYwMywiYWlvIjoiNDJCZ1lKaDlXbFZsNFh1SjZFMHZsMDMvY25TOU5nQT0iLCJhcHBpZCI6IjEwOTMyM2UzLWM1NWUtNDI0Yy1iNzEyLTJlODJmMTY5NWU5OCIsImFwcGlkYWNyIjoiMSIsImlkcCI6Imh0dHBzOi8vc3RzLndpbmRvd3MubmV0LzJmZjkzY2NlLWVmZDEtNGUxNi05MmJhLWFmYjUzZTllMDlmYy8iLCJvaWQiOiI4NjM3MjIwNi1jZWZhLTQ1ZWEtOTM0Ni0xNjNiYmFiYzc3MDYiLCJyb2xlcyI6WyJwYXltZW50UmVxdWVzdCIsInJlZnVuZCIsImRldmVsb3BlciJdLCJzdWIiOiI4NjM3MjIwNi1jZWZhLTQ1ZWEtOTM0Ni0xNjNiYmFiYzc3MDYiLCJ0aWQiOiIyZmY5M2NjZS1lZmQxLTRlMTYtOTJiYS1hZmI1M2U5ZTA5ZmMiLCJ1dGkiOiI3bUd1b0FRMUQwZTFGMks0Sy1ZWEFBIiwidmVyIjoiMS4wIn0.gCfn6QNUjzENuKvVN0bdkWgRwN-1ipovSq5Yb8IjCRfRhZNTFffLP1oIVw_8sPQxTFfV8CBOcQG385n-r_tIWBlMANHdKmpmwmOTz4J08EWzz_SY4zWdvMlF7quCYdrXVIXaKd-PLtO0UDTI7CsSAso7OAOsHBNrn3ITBoR0aMo_lM8X5dJM5fleSHFiJPMYDHpawZuy_BBXC0AUdcVT61NHkdO7sDV4Dc_C12CsShEqMi3Nj-uzr5wYHmPTxi3nk2px-_yiVaAHxxCmE0f7rWdM4BjVB89j_xtZrn1-VovgUUn_QDppY9yOgKx831xXBMX0Tz293V8g7BkagVI3lw | EA001      | 401       |
 # Auth token unverified
      | Error validating JWT | API Gateway Authentication Failed | Bearer nbCwW11w3XkB-xUaXwKRSLjMHGQ                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     | EA001      | 401       |


   #@trial
  @regression @merchantManagement @merchantManagementPut
  Scenario Outline: Negative flow- Mandatory fields from the body missing
    Given I am a PUT application authorized DRAGON user with Application.ReadWrite.All
    And I have updated the "<description>" and platformId values
    When I make a PUT request to the application endpoint with missing "<missBodyValue>" value
    Then I should receive a "400" error response with "<error_description>" error description and "<error_code>" errorcode within the PUT application response
    And error message should be "<error_message>" within the PUT application response
    Examples:
      | missBodyValue | description | error_description                                                                                                          | error_message                     | error_code |
      | description   | description | Service Request Validation Failed                                                                                          | Service Request Validation Failed | EA002      |
      | platformId    |             | Field error in object 'updateMerchantApplicationMappingInputModel': field 'peakId' must not be null; rejected value [null] | Service Request Validation Failed | EA002      |


    #@trial
  @regression @merchantManagement @merchantManagementPut
  Scenario Outline: Negative flow- Mandatory fields from the invalid body
    Given I am a PUT application authorized DRAGON user with Application.ReadWrite.All
    When I make a PUT request to the application endpoint with invalid platformId "<platformId>" value and description "<description>"
    Then I should receive a "400" error response with "<error_description>" error description and "<error_code>" errorcode within the PUT application response
    And error message should be "<error_message>" within the PUT application response
    Examples:
      | platformId                                 | description | error_description                                                               | error_message                     | error_code |
      | 2ee3e4a5-ef45-4fe2-a37d-d5fcfc6adb33345456 | description | Unable to read or parse message body: json parse error at [line: 1, column: 43] | Service Request Validation Failed | EA002      |
      | 2ee3e4a5-ef45-4fe2-a37d-d5f                | description | Unable to read or parse message body: json parse error at [line: 1, column: 43] | Service Request Validation Failed | EA002      |


     #@trial
  @regression @merchantManagement @merchantManagementPut
  Scenario Outline: Negative flow- Put application request with no body
    Given I am a PUT application authorized DRAGON user with Application.ReadWrite.All
    When I make a PUT request to the application endpoint with no body
    Then I should receive a "400" error response with "<error_description>" error description and "<error_code>" errorcode within the PUT application response
    And error message should be "<error_message>" within the PUT application response
    Examples:
      | error_description                    | error_message                     | error_code |
      | Unable to read or parse message body | Service Request Validation Failed | EA002      |


     #@trial
  @regression @merchantManagement @merchantManagementPut
  Scenario Outline: Negative flow - Put application with invalid Application id
    Given I am a PUT application authorized DRAGON user with Application.ReadWrite.All
    And I have updated the "<description>" and platformId values
    When I make a PUT request to the application endpoint with invalid "<applicationId>"
    Then I should receive a "<httpStatus>" error response with "<error_description>" error description and "<error_code>" errorcode within the PUT application response
    And error message should be "<errorMessage>" within the PUT application response
    Examples:

      | description | applicationId                        | httpStatus | error_code | error_description               | errorMessage                      |
      | testing     | 00000002-0000-4444-c000-000000000000 | 400        | EA025      | Application Id not found        | Service Request Validation Failed |
      | testing     | ab                                   | 400        | EA002      | Failed to convert value of type | Service Request Validation Failed |
