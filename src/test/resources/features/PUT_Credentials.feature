Feature: PUT_Credentials - PUT Credentials Merchant
  As a user
  I want to create up to credentials for merchant and validate correct response is returned

  Background: Retrieving access Token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

  @regression @credentials
  Scenario Outline: SC-1 Positive flow - Update credentials name for existing credential
    Given I am an authorized to put credentials as DRAGON user
    When I hit the put credentials endpoint with new credential name "<credentialName>"
    Then put credentials response should be successful
    Examples:
      | credentialName |
      | validName        |
