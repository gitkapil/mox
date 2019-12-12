Feature: POST_Credentials - POST Credentials Merchant - DRAG-2176
  As a user
  I want to create up to credentials for merchant and validate correct response is returned

  Background: Retrieving access Token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

  @trial
    @regression
  Scenario Outline: SC-1 Positive flow - Create a new credentials, new signing key and password
    Given I am an authorized to create credentials as DRAGON user
    When I hit the post credentials endpoint with credential name "<credentialName>"
    Then the create credentials response should be successful
    Examples:
      | credentialName |
      | validName      |

  @regression
  Scenario Outline: SC-2 Positive flow - Merchant can have maximum five active credentials, new signing keys and passwords
    Given I am an authorized to create credentials as DRAGON user
    When I hit the post credentials endpoint six times with same credential name "<credentialName>"
    Then I should receive a "<response_code>" error response with "<error_description>" error description and "<error_code>" errorCode within create credentials response
    And error message should be "<error_message>" within create credentials response
    Examples:
      | credentialName | response_code | error_description                   | error_code | error_message       |
      | validName      | 404           | Active Application Id are 5 already | EA002      | Resource Not Found! |


  @regression @credentials
  Scenario Outline: SC-3 Positive flow -A merchant cannot have same name for two credentials
    Given I am an authorized to create credentials as DRAGON user
    When I hit the post credentials endpoint second times with same credential name "<credentialName>"
    Then I should receive a "<response_code>" error response with "<error_description>" error description and "<error_code>" errorCode within create credentials response
    And error message should be "<error_message>" within create credentials response
    Examples:
      | credentialName | response_code | error_description                                                 | error_code | error_message       |
      | validName      | 404           | There should not be two ACTIVE keys with the same Credential Name | EA002      | Resource Not Found! |


