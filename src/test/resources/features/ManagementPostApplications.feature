
Feature: Management Post Applications API - DRAG-1416

  Background: Retrieving access Token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

    @regression @merchantManagement @postApplication
  Scenario Outline: Positive flow- A DRAGON user with Application.ReadWrite.All privilege is able to create an application
    Given I am a POST application authorized DRAGON user with the ApplicationKey.ReadWrite.All privilege
    And I have a "<clientId>" from an existing AAD application
    And I have a "<peakId>", "<subUnitId>", "<organisationId>" and "<description>" from an existing PM4B merchant identity
    When I make a POST request to the application endpoint
    Then I should receive a successful applications response
    And the response body should contain a valid applicationId, clientId, peakId, subUnitId and organisationId
    And the response body should also have empty notificationPath and empty notificationHost
    Examples:
      | clientId | peakId                               | subUnitId                            | organisationId                       | description |
      | random   | 00000002-0000-0000-c000-000000000000 | 00000002-0000-0000-c000-000000000000 | 00000002-0000-0000-c000-000000000000 | test        |

  # For the parametres where values are missing within the table, while creating request, the parameter will not be included at all as a a part of the payload
  #@trial
  #@regression @merchantManagement @merchantManagementPost
  Scenario Outline: Positive flow- A DRAGON user with Application.ReadWrite.All privilege is able to create an application
    Given I am a POST application authorized DRAGON user with the Application.ReadWrite.All privilege
    And I have a "<clientId>" from an existing AAD application
    And I have a "<peakId>", "<subUnitId>", "<organisationId>" and "<description>" from an existing PM4B merchant identity
    When I make a POST request to the application endpoint
    Then I should receive a successful applications response
    And the response body should contain a valid applicationId, clientId, peakId, subUnitId and organisationId
    And the response body should also have empty notificationPath and empty notificationHost
    Examples:
      | clientId | peakId                               | subUnitId                            | organisationId                       | description |
      | random   | 00000002-0000-0000-c000-000000000000 | 00000002-0000-0000-c000-000000000000 | 00000002-0000-0000-c000-000000000000 | test desc   |

#  @trial
  #@regression @merchantManagement @merchantManagementPost
  Scenario Outline: Negative flow- A DRAGON with Application.ReadWrite.All privilege user cannot enter a description > 256 characters
    Given I am a POST application authorized DRAGON user with the Application.ReadWrite.All privilege
    And I have a "<clientId>" from an existing AAD application
    And I have a "<peakId>", "<subUnitId>", "<organisationId>" and a super large description from an existing PM4B merchant identity
    When I make a POST request to the application endpoint
    Then I should receive a successful applications response
    And I make a POST request with the same client Id to the application endpoint
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorcode within the POST application response
    Examples:
      | clientId | peakId                               | subUnitId                            | organisationId                       | error_description                                                     | error_code | http_status |
      | random   | 00000002-0000-0000-c000-000000000000 | 00000002-0000-0000-c000-000000000000 | 00000002-0000-0000-c000-000000000000 | Merchant Application Mapping record with same client Id already exist | EA021      | 400         |

#  @trial
  #@regression @merchantManagement @merchantManagementPost
  Scenario Outline: Negative flow- A DRAGON with Application.ReadWrite.All privilege user is unable to create a duplicate application
    Given I am a POST application authorized DRAGON user with the Application.ReadWrite.All privilege
    And I have a "<clientId>" from an existing AAD application
    And I have a "<peakId>", "<subUnitId>", "<organisationId>" and "<description>" from an existing PM4B merchant identity
    When I make a POST request to the application endpoint
    Then I should receive a successful applications response
    And I make a POST request with the same client Id to the application endpoint
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorcode within the POST application response
    Examples:
      | clientId | peakId                               | subUnitId                            | organisationId                       | description | error_description                                                     | error_code | http_status |
      | random   | 00000002-0000-0000-c000-000000000000 | 00000002-0000-0000-c000-000000000000 | 00000002-0000-0000-c000-000000000000 | test d      | Merchant Application Mapping record with same client Id already exist | EA021      | 400         |

  #DRAG-1157 - Please update the correct error_message for the signature in the examples.
#  @trial
  #@regression @merchantManagement @merchantManagementPost
  Scenario Outline: Negative flow- Mandatory fields not sent in the header
    Given I am a POST application authorized DRAGON user with the Application.ReadWrite.All privilege
    And I have valid application details
    When I make a POST request to the application endpoint with "<key>" missing in the header
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorcode within the POST application response
    And error message should be "<error_message>" within the POST application response
    Examples:
      | error_description                                              | error_message                     | key           | error_code | http_status |
      | Error validating JWT                                           | API Gateway Authentication Failed | authorization | EA001      | 401         |
#      | Header Request-Date-Time was not found in the request. Access denied. | API Gateway Validation Failed | Request-Date-Time | EA002 | 400 |
      | Header Trace-Id was not found in the request. Access denied.   | API Gateway Validation Failed     | Trace-Id      | EA002      | 400         |
#      | Header Signature was not found in the request. Access denied. | API Gateway Validation Failed | Signature | EA002 | 400 |
      | Header Accept does not contain required value.  Access denied. | Request Header Not Acceptable     | Accept        | EA008      | 406         |
      | Content type 'text/plain;charset=ISO-8859-1' not supported     | Service Request Validation Failed | Content-Type  | EA002      | 415         |

#  @trial
  #@regression @merchantManagement @merchantManagementPost
  Scenario Outline: Negative flow- Mandatory fields not sent in the header
    Given I am a POST application authorized DRAGON user with the Application.ReadWrite.All privilege
    And I have valid application details
    When I make a POST request to the application endpoint with "<key>" missing in the header
    And error message should be "Resource not found" within the POST application response
    Examples:
      | key         |
      | Api-Version |

#  @trial
  #@regression @merchantManagement @merchantManagementPost
  Scenario Outline: Negative flow- Invalid auth token
    Given I am a DRAGON user with invalid "<auth_token>"
    And I have valid application details
    When I make a POST request to the application endpoint
    Then I should receive a "<http_code>" error response with "<error_description>" error description and "<error_code>" errorcode within the POST application response
    And error message should be "<error_message>" within the POST application response
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

#  @trial
  #@regression @merchantManagement @merchantManagementPost
  Scenario Outline: Negative flow- Mandatory fields from the body missing or invalid
    Given I am a POST application authorized DRAGON user with the Application.ReadWrite.All privilege
    And I have a "<clientId>" from an existing AAD application
    And I have a "<peakId>", "<subUnitId>", "<organisationId>" and "<description>" from an existing PM4B merchant identity
    When I make a POST request to the application endpoint
    Then I should receive a "400" error response with "<error_description>" error description and "<error_code>" errorcode within the POST application response
    And error message should be "<error_message>" within the POST application response
    Examples:
      | clientId                             | peakId                               | subUnitId                            | organisationId                       | description | error_description                                                                                                                 | error_message                     | error_code |
      | no_value                             | 00000002-0000-0000-c000-000000000000 | 00000002-0000-0000-c000-000000000000 | 00000002-0000-0000-c000-000000000000 |             | Field error in object 'createMerchantApplicationMappingInputModel': field 'clientId' may not be null; rejected value [null]       | Service Request Validation Failed | EA002      |
      | 00000002-0000-0000-c000-000000000000 | no_value                             | 00000002-0000-0000-c000-000000000000 | 00000002-0000-0000-c000-000000000000 |             | Field error in object 'createMerchantApplicationMappingInputModel': field 'peakId' may not be null; rejected value [null]         | Service Request Validation Failed | EA002      |
      | 00000002-0000-0000-c000-000000000000 | 00000002-0000-0000-c000-000000000000 | no_value                             | 00000002-0000-0000-c000-000000000000 |             | Field error in object 'createMerchantApplicationMappingInputModel': field 'subUnitId' may not be null; rejected value [null]      | Service Request Validation Failed | EA002      |
      | 00000002-0000-0000-c000-000000000000 | 00000002-0000-0000-c000-000000000000 | 00000002-0000-0000-c000-000000000000 | no_value                             |             | Field error in object 'createMerchantApplicationMappingInputModel': field 'organisationId' may not be null; rejected value [null] | Service Request Validation Failed | EA002      |
      | not a UUID                           | 00000002-0000-0000-c000-000000000000 | 00000002-0000-0000-c000-000000000000 | 00000002-0000-0000-c000-000000000000 |             | Unable to read or parse message body: json parse error at [line: 1, column: 61]                                                   | Service Request Validation Failed | EA002      |
      | 00000002-0000-0000-c000-000000000000 | not a UUID                           | 00000002-0000-0000-c000-000000000000 | 00000002-0000-0000-c000-000000000000 |             | Unable to read or parse message body: json parse error at [line: 1, column: 11]                                                   | Service Request Validation Failed | EA002      |
      | 00000002-0000-0000-c000-000000000000 | 00000002-0000-0000-c000-000000000000 | not a UUID                           | 00000002-0000-0000-c000-000000000000 |             | Unable to read or parse message body: json parse error at [line: 1, column: 168]                                                  | Service Request Validation Failed | EA002      |
      | 00000002-0000-0000-c000-000000000000 | 00000002-0000-0000-c000-000000000000 | 00000002-0000-0000-c000-000000000000 | not a UUID                           |             | Unable to read or parse message body: json parse error at [line: 1, column: 117]                                                  | Service Request Validation Failed | EA002      |

#  @regression
#  Scenario Outline: Negative flow- Request Date Time's invalid values set within the header
#    Given I am an authorized DRAGON user with the Application.ReadWrite.All privilege
#    And I have valid application details with invalid value "<value>" set for Request Date Time sent in the header
#    When I make a POST request to the application endpoint
#    Then I should receive a "400" error response with "<error_description>" error description and "EA002" errorcode within the POST application response
#    And error message should be "Service Request Validation Failed" within the POST application response
#    Examples:
#      |value|error_description|
#      |  | Request timestamp not a valid RFC3339 date-time |
#      | xyz | Request timestamp not a valid RFC3339 date-time |
#      | 2019-02-04T00:42:45.237Z | Request timestamp too old |
