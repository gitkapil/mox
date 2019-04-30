Feature: Management POST public keys API - DRAG-1461

  @trial
  Scenario: Positive Flow - Able to create a public key successfully
    Given I am a POST create keys authorized DRAGON user with the correct privileges
    When I create a new public key based on using an existing application key
    Then I should receive a successful create public key response

  @trial
  Scenario Outline: Negative Flow - Unable to create a public key because of wrong credentials
    Given I am a POST create key authorized DRAGON user with incorrect privileges
    When I create a new public key based on using an existing application key
    Then the public keys response should receive a "<http_status>" error with "<error_description>" description and "<error_code>" error code
    Examples:
      |applicationKey||http_status|error_description|error_code|

  @trial
  Scenario Outline: Positive Flow - Returns an error if application Id is incorrect
    Given I am a POST create keys authorized DRAGON user with the correct privileges
    When I create a new public key based on using "<applicationKey>" application key
    And I give it in a valid JSON in the body
    Then the public keys response should receive a "<http_status>" error with "<error_description>" description and "<error_code>" error code
  Examples:
    |applicationKey||http_status|error_description|error_code|
    # Empty Application Key
    # Invalid Application Key Format
    # Non-existing Application Key

  @trial
  Scenario Outline: Negative Flow - Create public key with invalid json values
    Given I am a POST create keys authorized DRAGON user with the correct privileges
    When I create a new public key based on using "<applicationKey>" application key
    And I have "<value>" value, activate at "<activateAt>", deactivate at "<deactivateAt>", "<entityStatus>" as entity status and description is "<description>"
    Then the public keys response should receive a "<http_status>" error with "<error_description>" description and "<error_code>" error code
  Examples:
    |applicationKey|value|activateAt|deactivateAt|entityStatus|description|http_status|error_description|error_code|
    #value: zero length
    #value: Non base64 encoded
    #value: without passing it
    #activateAt: without passing it
    #activateAt: non date-time
    #activateAt: weird date-time format
    #deactivateAt: without passing it
    #deactivateAt: non date-time
    #deactivateAt: weird date-time format
    #deactivateAt/activateAt: deactivate is before activate
    #entityStatus: without passing it
    #entityStatus: 2 character length
    #entityStatus: not "A" or "D"
    #entityStatus: "a"
    #entityStatus: "d"
    #description: without passing it
    #description: more than 256 length