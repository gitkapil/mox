Feature: Merchant Management POST Signing Keys - DRAG-1565

  Background: Retrieving access Token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

  @trial
  @regression
  Scenario Outline: Positive flow - Able to create a new signing keys
    Given I am an authorized Signing Key DRAGON user
    And I have a "<applicationId>" application id
    And I have an activate date "<activateAt>" and deactivate date "<deactivateAt>", with entity status "<entityStatus>" and a description "<description>"
    When I make a request to create a new signing key
    Then the create signing key response should be successful
  Examples:
    |applicationId|activateAt|deactivateAt|entityStatus|description|

  @trial
  @regression
  Scenario Outline: Negative flow - Unauthorized user
    Given I am not an authorized Signing key DRAGON user
    And I have a "<applicationId>" application id
    And I have an activate date "<activateAt>" and deactivate date "<deactivateAt>", with entity status "<entityStatus>" and a description "<description>"
    When I make a request to create a new signing key
    Then the create signing key response should give a "<http_status>" http status with error code "<error_code>" and description "<error_description>"
  Examples:
    |applicationId|activateAt|deactivateAt|entityStatus|description|http_status|error_code|error_description|

  @trial
  @regression
  Scenario Outline: Negative flow - Invalid application id
    Given I am an authorized Signing Key DRAGON user
    And I have a "<applicationId>" application id
    And I have an activate date "<activateAt>" and deactivate date "<deactivateAt>", with entity status "<entityStatus>" and a description "<description>"
    When I make a request to create a new signing key
    Then the create signing key response should give a "<http_status>" http status with error code "<error_code>" and description "<error_description>"
  Examples:
    |applicationId|activateAt|deactivateAt|entityStatus|description|http_status|error_code|error_description|
    #empty application id
    #invalid application id format
    #application id not found

  @trial
  @regression
  Scenario Outline: Negative flow - Invalid dates
    Given I am an authorized Signing Key DRAGON user
    And I have a "<applicationId>" application id
    And I have an activate date "<activateAt>" and deactivate date "<deactivateAt>", with entity status "<entityStatus>" and a description "<description>"
    When I make a request to create a new signing key
    Then the create signing key response should give a "<http_status>" http status with error code "<error_code>" and description "<error_description>"
    Examples:
      |applicationId|activateAt|deactivateAt|entityStatus|description|http_status|error_code|error_description|
    #empty activateAt
    #invalid activateAt format
    #empty deactivateAt
    #invalid deactivateAt format
    #activateAt order than deactivateAt date
    #activateAt same as deactivateAt date

  @trial
  @regression
  Scenario Outline: Negative flow - Entity status
    Given I am an authorized Signing Key DRAGON user
    And I have a "<applicationId>" application id
    And I have an activate date "<activateAt>" and deactivate date "<deactivateAt>", with entity status "<entityStatus>" and a description "<description>"
    When I make a request to create a new signing key
    Then the create signing key response should give a "<http_status>" http status with error code "<error_code>" and description "<error_description>"
    Examples:
      |applicationId|activateAt|deactivateAt|entityStatus|description|http_status|error_code|error_description|
    #empty entity status
    #entity status = Z
    #entity status 2 characters
    #entity status = lowercase a
    #entity status = lowercase d

  @trial
  @regression
  Scenario Outline: Negative flow - Description
    Given I am an authorized Signing Key DRAGON user
    And I have a "<applicationId>" application id
    And I have an activate date "<activateAt>" and deactivate date "<deactivateAt>", with entity status "<entityStatus>" and a description "<description>"
    When I make a request to create a new signing key
    Then the create signing key response should give a "<http_status>" http status with error code "<error_code>" and description "<error_description>"
    Examples:
      |applicationId|activateAt|deactivateAt|entityStatus|description|http_status|error_code|error_description|
    #empty description
    #description > 256 characters