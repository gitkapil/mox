
Feature: Merchant Management API - GET /keys/signing

  Background: Retrieving access Token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

#  @trial
  @regression @testingSingingKey
  Scenario Outline: positive flow - get signing keys
    Given I am a user with permissions to use signing key
    And I make a request to get signing keys with "<applicationID>"
    Then I should receive a successful signing key response
Examples:
    |applicationID                        |
    |c9621185-b86d-48a9-97f0-eeddef7c3dc1 |
  # | 96703cf3-82bb-429e-bcff-93a078b39307|

 # @trial
  @regression
  Scenario Outline: negative flow - get signing keys without trace id
    Given I am a user with permissions to use signing key
    And I make a request to get signing keys with "<applicationID>" with no track id
    Then I should receive a signing key response error "<http_status>" status with code "<error_code>" and description "<error_description>"
    Examples:
      |http_status|error_code|error_description                                             |  applicationID                       |
      |400        |EA002     |Header Trace-Id was not found in the request. Access denied.  | c9621185-b86d-48a9-97f0-eeddef7c3dc1 |

#  @trial
  @regression
  Scenario Outline: Negative flow - Invalid application Id
    Given I am a user with permissions to use signing key
    And I make a request to get signing keys with "<applicationID>"
    Then I should receive a signing key response error "<http_status>" status with code "<error_code>" and description "<error_description>"
  Examples:
    |applicationID                       |http_status|error_code|error_description|
    # Invalid application Id format
    |aaa                                 |400        |EA002     |Failed to convert value of type             |
    # Application Id not found
    |00000002-0000-3333-c000-000000000000|400        |EA025     |Application Id not found             |