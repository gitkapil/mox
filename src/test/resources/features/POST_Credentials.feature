Feature: POST_Credentials - POST Credentials Merchant - DRAG-2176
  As a user
  I want to create up to credentials for merchant and validate correct response is returned

  Background: Retrieving access Token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

  #@trial
  @regression @credentials
  Scenario Outline: Positive flow - Create a new credentials, new signing key and password
    Given I am an authorized to create credentials as DRAGON user
    When I hit the post credentials endpoint with credential name "<credentialName>"
    Then the create credentials response should be successful
#    Then the create signing key response should be successful

    Examples:
      | credentialName |
      | Testing_Kapil  |

