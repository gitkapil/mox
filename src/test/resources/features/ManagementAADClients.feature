Feature: Merchant Management AAD Create - DRAG-1252

  Background: Retrieving access Token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

#    @trial
    @regression
    Scenario: Positive flow - Create a new client
      Given I am logging in as a user with AAD role
      When I create a new client with a unique name and grant url
      Then the create application response should be successful

#    @trial
    @regression
    Scenario Outline: Negative flow - Unable to create client due to incorrect body data
      Given I am logging in as a user with AAD role
      When I create a new client with name as "<name>" and grant url as "<url>"
      Then the create response will return http status "<http_status>" with error code "<error_code>" and description "<error_description>"
    Examples:
      |name                             |url             |http_status|error_code|error_description                 |
      |null                             |http://localhost|400        |EA002     |Field error in object             |
      |                                 |http://localhost|400        |EA002     |Field error in object             |
      |longname                         |http://localhost|400        |EA002     |Field error in object             |
      |app-us-apple-test-live-client-app|http://localhost|400        |EA002     |Field error in object             |
      |validname                        |                |400        |EA002     |Field error in object             |
      |validname                        |longurl         |400        |EA002     |Field error in object             |