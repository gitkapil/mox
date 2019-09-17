@getPassword
Feature: Merchant Management get Password - DRAG-1481
         As a user
         I want to get the password details and validate the details returned in response are correct!!

  Background: Retrieving access Token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

  #@trial
  @regression
  Scenario: Positive flow - get the password details with valid input header
    Given I am logging in as a user with authorize Dragon user
    When I get the password request with newly created applicationId
    Then I should see the response from get password request

  #@trial
  @regression
  Scenario Outline: Negative flow - unable to get password with invalid application id
    Given I am logging in as a user with authorize Dragon user
    When I set the application id as "<applicationId>"
    When I make the GET password request to the endpoint
    Then I GET request should have an error with status "<http_status>", error code as "<error_code>" and description "<error_description>"
    And error message should be "<error_message>" within the GET password response
    Examples:
      | applicationId                        | http_status | error_code | error_message                     | error_description                                                                                                                                                     |
      | aa                                   | 400         | EA002      | Service Request Validation Failed | Failed to convert value of type 'java.lang.String' to required type 'java.util.UUID'; nested exception is java.lang.IllegalArgumentException: Invalid UUID string: aa |
      | 00000002-1111-0000-c000-000000000000 | 400         | EA002      | Resource Not Found!               | application not found                                                                                                                                                 |


  #@trial
  @regression
  Scenario Outline: Negative flow - Unable to create password due to invalid header values
    Given I am logging in as a user with authorize Dragon user
    And  I get the password with null header "<nullHeaderValues>"
    Then I GET request should have an error with status "<http_status>", error code as "<error_code>" and description "<error_description>"
    And error message should be "<error_message>" within the GET password response
    Examples:

      | error_description                                              | error_message                     | nullHeaderValues | error_code | http_status |
      | Error validating JWT                                           | API Gateway Authentication Failed | Authorization    | EA001      | 401         |
      | Header Trace-Id was not found in the request. Access denied.   | API Gateway Validation Failed     | Trace-Id         | EA002      | 400         |
      | Header Accept does not contain required value.  Access denied. | Request Header Not Acceptable     | Accept           | EA008      | 406         |
      | Content type                                                   | Service Request Validation Failed | Content-Type     | EA002      | 415         |

  #@trial
  @regression
  Scenario Outline: Positive flow - POST password for new application should return list with last update items
    Given I am logging in as a user with AAD Password role
    And I have created password data with application id, activate at "<activateAt>", and deactivate at "<deactivateAt>"
    Then I should see the response from post password request
    And validate the response from post password request
    Given I am logging in as a user with authorize Dragon user
    When I get the password request with the same applicationId
    Then I should see the list of response from get password request
    Examples:
      | activateAt           | deactivateAt         |
      | 2019-01-01T00:00:00Z | 2032-02-02T00:00:00Z |