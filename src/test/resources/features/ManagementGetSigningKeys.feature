Feature: Merchant Management API - GET /keys/signing

  Background: Retrieving access Token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

  @trial
  @regression
  Scenario Outline: positive flow - get signing keys
    Given I am a user with permissions to use signing key
    When I make a request with "<applicationId>" as application id
    Then I should receive a successful signing key response
  Examples:
    |applicationId|
    |             |

  @trial
  @regression
  Scenario Outline: Negative flow - Invalid application Id
    Given I am a user with permissions to use signing key
    When I make a request with "<applicationId>" as application id
    Then I should receive a signing key response error "<http_status>" status with code "<error_code>" and description "<error_description>"
  Examples:
    |applicationId|http_status|error_code|error_description|
    # Empty application Id
    # Invalid application Id format
    # Application Id not found