Feature: Merchant Management API - GET /keys/signing

  Background: Retrieving access Token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

  #@trial
#  @regression
  Scenario: positive flow - get signing keys
    Given I am a user with permissions to use signing key
    When I create a new application id for get signing key
#    And I create a public key for this application
    And I make a request to get signing keys
    Then I should receive a successful signing key response

#  @trial
#  @regression
  Scenario Outline: negative flow - get signing keys without public key
    Given I am a user with permissions to use signing key
    When I create a new application id for get signing key
    And I make a request to get signing keys
    Then I should receive a signing key response error "<http_status>" status with code "<error_code>" and description "<error_description>"
    Examples:
      | http_status | error_code | error_description                                                        |
      | 400         | EA028      | Active Public Key not found, please update expired key or create new key |

#  @trial
#  @regression
  Scenario Outline: Negative flow - Invalid application Id
    Given I am a user with permissions to use signing key
    When I make a request with "<applicationId>" as application id
    Then I should receive a signing key response error "<http_status>" status with code "<error_code>" and description "<error_description>"
    Examples:
      | applicationId                        | http_status | error_code | error_description               |
    # Invalid application Id format
      | aaa                                  | 400         | EA002      | Failed to convert value of type |
    # Application Id not found
      | 00000002-0000-3333-c000-000000000000 | 400         | EA025      | Application Id not found        |