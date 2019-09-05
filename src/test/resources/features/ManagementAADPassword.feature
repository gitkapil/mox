@passwordApi
Feature: Merchant Management AAD Password - DRAG-1481

  Background: Retrieving access Token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

#  @trial
  @regression
  Scenario Outline: Positive flow - create AAD Password provided with valid request body and header values
    Given I am logging in as a user with AAD Password role
    And I have created password data with "<applicationId>" and activate at "<activateAt>", deactivate at "<deactivateAt>"
    Then I should see the response from post password request
    And validate the response from post password request
    Examples:
      | applicationId                        | activateAt           | deactivateAt         |
      | 9b6e98a4-32ed-493d-88fe-795e60b06d68 | 2019-01-01T00:00:00Z | 2032-02-02T00:00:00Z |

#  @trial
  @regression
  Scenario Outline: Negative flow - unable to create password due to bad application id
    Given I am logging in as a user with AAD Password role
    And I have created password data with "<applicationId>" and activate at "<activateAt>", deactivate at "<deactivateAt>"
    Then I should have an error with status "<http_status>", error code as "<error_code>" and description "<error_description>"
    And error message should be "<error_message>" within the POST password response
    Examples:
      | applicationId                        | http_status | error_code | activateAt           | deactivateAt         | error_message                     | error_description                                                                                                                                                     |
      | aa                                   | 400         | EA002      | 2019-01-01T00:00:00Z | 2032-02-02T00:00:00Z | Service Request Validation Failed | Failed to convert value of type 'java.lang.String' to required type 'java.util.UUID'; nested exception is java.lang.IllegalArgumentException: Invalid UUID string: aa |
      | 00000002-1111-0000-c000-000000000000 | 404         | EA002      | 2019-01-01T00:00:00Z | 2032-02-02T00:00:00Z | Resource Not Found!               | application not found                                                                                                                                                 |

#  @trial
  @regression
  Scenario Outline: Negative flow - Unable to create password with invalid body values
    Given I am logging in as a user with AAD Password role
    And I have created password data with "<applicationId>" and activate at "<activateAt>", deactivate at "<deactivateAt>"
    Then I should have an error with status "<http_status>", error code as "<error_code>" and description "<error_description>"
    And error message should be "<error_message>" within the POST password response
    Examples:
      | activateAt           | deactivateAt         | http_status | error_code | error_description                                                                      | applicationId                        | error_message                     |
      |                      | 2019-02-02T00:00:00Z | 400         | EA002      | Field error in object 'addAADPasswordInputModel': field 'activateAt' must not be null  | 9b6e98a4-32ed-493d-88fe-795e60b06d68 | Service Request Validation Failed |
      | null                 | 2019-02-02T00:00:00Z | 400         | EA002      | Field error in object 'addAADPasswordInputModel': field 'activateAt' must not be null  | 9b6e98a4-32ed-493d-88fe-795e60b06d68 | Service Request Validation Failed |
      | Monday               | 2019-02-02T00:00:00Z | 400         | EA002      | Unable to read or parse message body: json parse error at                              | 9b6e98a4-32ed-493d-88fe-795e60b06d68 | Service Request Validation Failed |
      | 2019-01-01T00:00:00Z | null                 | 400         | EA002      | Field error in object 'addAADPasswordInputModel': field 'deactivateAt' must not be nul | 9b6e98a4-32ed-493d-88fe-795e60b06d68 | Service Request Validation Failed |
      | 2019-01-01T00:00:00Z |                      | 400         | EA002      | Field error in object 'addAADPasswordInputModel': field 'deactivateAt' must not be nul | 9b6e98a4-32ed-493d-88fe-795e60b06d68 | Service Request Validation Failed |
      | 2019-01-01T00:00:00Z | Tuesday              | 400         | EA002      | Unable to read or parse message body: json parse error at                              | 9b6e98a4-32ed-493d-88fe-795e60b06d68 | Service Request Validation Failed |
   #  | 2019-02-02T00:00:00Z | 2019-01-01T00:00:00Z | 400         | EA002      | test                                                                                   | 9b6e98a4-32ed-493d-88fe-795e60b06d68 | Service Request Validation Failed |
   #  | 2019-01-01T00:00:00Z | 2019-02-02T00:00:00Z | 400         | EA000      | test                                                                                   | 9b6e98a4-32ed-493d-88fe-795e60b06d68 | Service Request Validation Failed |


    #@trial
  @regression @HappyFlow @passwordApi
  Scenario Outline: Negative flow - Unable to create password due to invalid header values
    Given I am logging in as a user with AAD Password role
    And  I create a new AAD password with null header "<nullHeaderValues>"
    Then I should have an error with status "<http_status>", error code as "<error_code>" and description "<error_description>"
    And error message should be "<error_message>" within the POST password response
    Examples:
      | error_description                                                     | error_message                     | nullHeaderValues  | error_code | http_status |
      | Error validating JWT                                                  | API Gateway Authentication Failed | Authorization     | EA001      | 401         |
      | Request timestamp not a valid RFC3339 date-time | API Gateway Validation Failed     | Request-Date-Time | EA002      | 400         |
      | Header Trace-Id was not found in the request. Access denied.          | API Gateway Validation Failed     | Trace-Id          | EA002      | 400         |
      | Header Accept does not contain required value.  Access denied.        | Request Header Not Acceptable     | Accept            | EA008      | 406         |
      | Content type                                                          | Service Request Validation Failed | Content-Type      | EA002      | 415         |




