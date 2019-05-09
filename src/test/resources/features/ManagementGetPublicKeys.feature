Feature: Management GET public keys API - DRAG-1487

  Background: Retrieving access Token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

#  @trial
  @regression
  Scenario: Positive Flow - Able to get a list of public keys successfully
    Given I am a GET create keys authorized DRAGON user with the correct privileges
    When I am able to get a list of public keys using an existing application key
    Then I should receive a successful get public key response

    #This should be a feature on its own
#  @trial
#  @regression
#  Scenario Outline: Negative Flow - Unable to get a list of public keys because using wrong privileges
#    Given I am a GET create keys authorized DRAGON user with the incorrect privileges
#    When I am able to get a list of public keys using an existing application key
#    Then the get public keys response should have a "<http_status>" error with "<error_description>" description and "<error_code>" error code
#    Examples:
#      |http_status|error_description|error_code|
#      |400        |asdf             |asdf      |

#  @trial
  @regression
  Scenario Outline: Negative Flow - Unable to get a list of public keys using bad application ID
    Given I am a GET create keys authorized DRAGON user with the correct privileges
    When I get a list of public keys using "<application_key>" as application key
    Then the get public keys response should have a "<http_status>" error with "<error_description>" description and "<error_code>" error code
    Examples:
      |application_key|http_status|error_description|error_code|
      # Empty Application Key
      |               |404        |                 |       |
      # Invalid Application Key Format
      |a              |400        |Failed to convert value of type |EA002      |
      # Non-existing Application Key
      |5d6bfbe6-504b-480b-a3e2-b71f07093f30              |400        |Application Id not found |EA025      |
