Feature: Merchant Management AAD Password - DRAG-1481

  Background: Retrieving access Token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

  @trial
  @regression
  Scenario: Positive flow - create AAD Password
    Given I am logging in as a user with AAD Password role
    When I create a new AAD client
    And I get the client id from the response
    And I create an application with that client id
    And I get the application id from the response
    And I create a new AAD password
    Then the password response should be successful

  @trial
  @regression
  Scenario Outline: Negative flow - unable to create password due to bad application id
    Given I am logging in as a user with AAD Password role
    When I set the application id as "<applicationId>"
    Then I should have an error with status "<http_status>", error code as "<error_code>" and description "<error_description>"
  Examples:
    |applicationId                       |http_status|error_code|error_description|
    #bad application id format
    |aa                                  |400        |EA000     |test             |
    # invalid application id
    |00000002-1111-0000-c000-000000000000|400        |EA000     |test             |

  @trial
  @regression
  Scenario Outline: Negative flow - unable to create password due to bad data
    Given I am logging in as a user with AAD Password role
    When I get a list of applications
    And I get the first application id
    And I set create password data with activate at "<activateAt>", deactivate at "<deactivateAt>", and description "<description>"
    Then I should have an error with status "<http_status>", error code as "<error_code>" and description "<error_description>"
  Examples:
    |activateAt          |deactivateAt        |description|http_status|error_code|error_description|
    |null                |2019-02-02T00:00:00Z|test       |400        |EA000     |test             |
    |                    |2019-02-02T00:00:00Z|test       |400        |EA000     |test             |
    |Monday              |2019-02-02T00:00:00Z|test       |400        |EA000     |test             |
    |2019-01-01T00:00:00Z|null                |test       |400        |EA000     |test             |
    |2019-01-01T00:00:00Z|                    |test       |400        |EA000     |test             |
    |2019-01-01T00:00:00Z|Tuesday             |test       |400        |EA000     |test             |
    |2019-02-02T00:00:00Z|2019-01-01T00:00:00Z|test       |400        |EA000     |test             |
    |2019-01-01T00:00:00Z|2019-02-02T00:00:00Z|superlong  |400        |EA000     |test             |