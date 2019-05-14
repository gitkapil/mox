Feature: Management Put Signing Keys - DRAG-1573

  Background: Retrieving access Token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

  @trial
  @regression
  Scenario Outline: Positive flow - Able to update signing key attribute
    Given I am a PUT signing key authorized user
    And I create a new signing key based on using an existing application key
    And I retrieve the applicationId and keyId from the signing key response
    And I update the signing key with description "<description>", activate at "<activateAt>", deactivate at "<deactivateAt>" and entity status "<entityStatus>"
    Then the PUT signing key response should return success
  Examples:
    |description|activateAt|deactivateAt|entityStatus|

  @trial
  @regression
  Scenario Outline: Negative flow - invalid application id and key id
    Given I am a PUT signing key authorized user
    And I have an "<applicationId>" and "<keyId>" from an existing signing key
    And I update the signing key with description "test", activate at "2019-01-01T00:00:00Z", deactivate at "2019-02-02T00:00:00Z" and entity status "D"
    Then the PUT signing key response should have error status "<http_status>" with error code "<err_code>" and description "<err_description>"
  Examples:
    |applicationId|keyId|http_status|err_code|err_description|

  @trial
  @regression
  Scenario Outline: Negative flow - Invalid body
    Given I am a PUT signing key authorized user
    And I create a new signing key based on using an existing application key
    And I retrieve the applicationId and keyId from the signing key response
    And I update the signing key with description "<description>", activate at "<activateAt>", deactivate at "<deactivateAt>" and entity status "<entityStatus>"
    Then the PUT signing key response should have error status "<http_status>" with error code "<err_code>" and description "<err_description>"
  Examples:
    |description|activateAt|deactivateAt|entityStatus|http_status|err_code|err_description|