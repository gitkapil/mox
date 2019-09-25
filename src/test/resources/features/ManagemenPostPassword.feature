@healthCheck
Feature: Merchant Management AAD Password - DRAG-1481
         As a user
         I want to create new password metadata and validate the details are correct

  Background: Retrieving access Token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

#  @trial
  @regression
  Scenario Outline: Positive flow - create AAD Password provided with valid request body and header values
    Given I am logging in as a user with AAD Password role
    And I have created password data with application id, activate at "<activateAt>", and deactivate at "<deactivateAt>"
    Then I should see the response from post password request
    And validate the response from post password request
    Examples:
      | activateAt           | deactivateAt         |
      | 2019-01-01T00:00:00Z | 2032-02-02T00:00:00Z |

#  @trial
  @regression
  Scenario Outline: Negative flow - unable to create password due to bad application id
    Given I am logging in as a user with AAD Password role
    And I have created password data with "<applicationId>" and activate at "<activateAt>", deactivate at "<deactivateAt>" and "<entityStatus>"
    Then I should have an error with status "<http_status>", error code as "<error_code>" and description "<error_description>"
    And error message should be "<error_message>" within the POST password response
    Examples:
      | applicationId                        | http_status | error_code | entityStatus | activateAt           | deactivateAt         | error_message                     | error_description                                                                                                                                                     |
      | aa                                   | 400         | EA002      | A            | 2019-01-01T00:00:00Z | 2032-02-02T00:00:00Z | Service Request Validation Failed | Failed to convert value of type 'java.lang.String' to required type 'java.util.UUID'; nested exception is java.lang.IllegalArgumentException: Invalid UUID string: aa |
      | 00000002-1111-0000-c000-000000000000 | 404         | EA002      | A            | 2019-01-01T00:00:00Z | 2032-02-02T00:00:00Z | Resource Not Found!               | application not found                                                                                                                                                 |

#  @trial
  @regression
  Scenario Outline: Negative flow - Unable to create password with invalid body values
    Given I am logging in as a user with AAD Password role
    And I have created password data with application id, activate at "<activateAt>", and deactivate at "<deactivateAt>"
    Then I should have an error with status "<http_status>", error code as "<error_code>" and description "<error_description>"
    And error message should be "<error_message>" within the POST password response
    Examples:
      | activateAt           | deactivateAt         | http_status | error_code | error_description                                                                                            | error_message                     |
      |                      | 2019-02-02T00:00:00Z | 400         | EA002      | Field error in object 'addAADPasswordInputModel': field 'activateAt' must not be null                        | Service Request Validation Failed |
      | null                 | 2019-02-02T00:00:00Z | 400         | EA002      | Field error in object 'addAADPasswordInputModel': field 'activateAt' must not be null                        | Service Request Validation Failed |
      | Monday               | 2019-02-02T00:00:00Z | 400         | EA002      | Unable to read or parse message body: json parse error at                                                    | Service Request Validation Failed |
      | 2019-01-01T00:00:00Z | null                 | 400         | EA002      | Field error in object 'addAADPasswordInputModel': field 'deactivateAt' must not be nul                       | Service Request Validation Failed |
      | 2019-01-01T00:00:00Z |                      | 400         | EA002      | Field error in object 'addAADPasswordInputModel': field 'deactivateAt' must not be nul                       | Service Request Validation Failed |
      | 2019-01-01T00:00:00Z | Tuesday              | 400         | EA002      | Unable to read or parse message body: json parse error at                                                    | Service Request Validation Failed |


    #@trial
  @regression
  Scenario Outline: Negative flow - Unable to create password due to invalid header values
    Given I am logging in as a user with AAD Password role
    And  I create a new AAD password with applicationId, activateAt, deactivate and null header "<nullHeaderValues>"
    Then I should have an error with status "<http_status>", error code as "<error_code>" and description "<error_description>"
    And error message should be "<error_message>" within the POST password response
    Examples:
      | error_description                                              | error_message                     | nullHeaderValues  | error_code | http_status |
      | Error validating JWT                                           | API Gateway Authentication Failed | Authorization     | EA001      | 401         |
      | Header Trace-Id was not found in the request. Access denied.   | API Gateway Validation Failed     | Trace-Id          | EA002      | 400         |
      | Header Accept does not contain required value.  Access denied. | Request Header Not Acceptable     | Accept            | EA008      | 406         |
      | Content type                                                   | Service Request Validation Failed | Content-Type      | EA002      | 415         |




