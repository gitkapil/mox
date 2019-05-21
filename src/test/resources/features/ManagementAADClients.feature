Feature: Merchant Management AAD Create - DRAG-1252

  Background: Retrieving access Token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

    @trial
    @regression
    Scenario: Positive flow - Create a new client
      Given I am logging in as a user with AAD role
      When I create a new client with a unique name and grant url
      Then the create application response should be successful

    @trial
    @regression
    Scenario Outline: Negative flow - Unable to create client due to incorrect body data
      Given I am logging in as a user with AAD role
      When I create a new client with name as "<name>" and grant url as "<url>"
      Then the create response will return http status "<http_status>" with error code "<error_code>" and description "<error_description>"
    Examples:
      |name                             |url             |http_status|error_code|error_description|
      |null                             |http://localhost|400        |EA000     |test             |
      |                                 |http://localhost|400        |EA000     |test             |
      |longname                         |http://localhost|400        |EA000     |test             |
      |app-us-apple-test-live-client-app|http://localhost|400        |EA000     |test             |
      |validname                        |                |400        |EA000     |test             |
      |validname                        |longurl         |400        |EA000     |test             |

    @trial
    @regression
    Scenario: Negative flow - I create a new application and tries to create the same application again
      Given I am logging in as a user with AAD role
      When I create a new client with a unique name and grant url
      And the create application response should be successful
      And I retrieve the application name
      And I create a new client with the same name and grant url
      Then the create response will return http status "400" with error code "EA000" and description "test"