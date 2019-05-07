Feature: Management POST public keys API - DRAG-1461

  Background: Retrieving access Token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

#  @trial
  @regression
  Scenario Outline: Positive Flow - Able to create a public key successfully
    Given I am a POST create keys authorized DRAGON user with the correct privileges
    When I have a value of 2048 characters, activate at "<activateAt>", deactivate at "<deactivateAt>", "<entityStatus>" as entity status and description is "<description>"
    And I create a new public key based on using an existing application key
    Then I should receive a successful create public key response
  Examples:
    |activateAt          |deactivateAt        |entityStatus|description       |
    |2019-01-01T00:00:00Z|2020-01-01T00:00:00Z|A           |this is a test key|

    #Refactor this feature to be on its own
#  @trial
#  @regression
#  Scenario Outline: Negative Flow - Unable to create a public key because of wrong credentials
#    Given I am a POST create key authorized DRAGON user with incorrect privileges
#    When I create a new public key based on using an existing application key
#    Then the public keys response should receive a "<http_status>" error with "<error_description>" description and "<error_code>" error code
#    Examples:
#      |http_status|error_description|error_code|

#  @trial
  @regression
  Scenario Outline: Negative Flow - Returns an error if application Id is incorrect
    Given I am a POST create keys authorized DRAGON user with the correct privileges
    When I create a new public key based on using "<applicationKey>" application key
    And I give it in a valid JSON in the body
    Then the public keys response should receive a "<http_status>" error with "<error_description>" description and "<error_code>" error code
  Examples:
    |applicationKey|http_status|error_description|error_code|
    # Empty Application Key
    |              |404        |                 |          |
    # Invalid Application Key Format
    |a              |400        |Failed to convert value of type |EA002      |
      # Non-existing Application Key
    |5d6bfbe6-504b-480b-a3e2-b71f07093f30              |400        |Application Id not found |EA025      |

#  @trial
  @regression
  Scenario Outline: Negative Flow - Create public key with invalid json values
    Given I am a POST create keys authorized DRAGON user with the correct privileges
    When I create a new public key based on using "<applicationKey>" application key
    And I have "<value>" value, activate at "<activateAt>", deactivate at "<deactivateAt>", "<entityStatus>" as entity status and description is "<description>"
    Then the public keys response should receive a "<http_status>" error with "<error_description>" description and "<error_code>" error code
  Examples:
    |applicationKey                      |value|activateAt          |deactivateAt        |entityStatus|description|http_status|error_description|error_code|
    #value: zero length
    |c9621185-b86d-48a9-97f0-eeddef7c3dc1|     |2019-01-01T00:00:00Z|2019-02-02T00:00:00Z|A           |d          |400        |Value must be a public key of length|EA023     |
    #value: Non base64 encoded
    |c9621185-b86d-48a9-97f0-eeddef7c3dc1| fd  |2019-01-01T00:00:00Z|2019-02-02T00:00:00Z|A           |d          |400        |Value must be a public key of length|EA023     |
    #value: without passing it
    |c9621185-b86d-48a9-97f0-eeddef7c3dc1|null |2019-01-01T00:00:00Z|2019-02-02T00:00:00Z|A           |d          |400        |Field error in object               |EA002     |
    #activateAt: without passing it
    |c9621185-b86d-48a9-97f0-eeddef7c3dc1|value|null                |2019-02-02T00:00:00Z|A           |d          |400        |Field error in object               |EA002     |
    #activateAt: non date-time
    |c9621185-b86d-48a9-97f0-eeddef7c3dc1|value|Monday              |2019-02-02T00:00:00Z|A           |d          |400        |Unable to read or parse message body|EA002     |
    #activateAt: weird date-time format
    |c9621185-b86d-48a9-97f0-eeddef7c3dc1|value|01-01-2019T00:00:00Z|2019-02-02T00:00:00Z|A           |d          |400        |Unable to read or parse message body|EA002     |
    #deactivateAt: without passing it
    |c9621185-b86d-48a9-97f0-eeddef7c3dc1|value|2019-01-01T00:00:00Z|null                |A           |d          |400        |Field error in object               |EA002     |
    #deactivateAt: non date-time
    |c9621185-b86d-48a9-97f0-eeddef7c3dc1|value|2019-01-01T00:00:00Z|Tuesday             |A           |d          |400        |Unable to read or parse message body|EA002     |
    #deactivateAt: weird date-time format
    |c9621185-b86d-48a9-97f0-eeddef7c3dc1|value|2019-01-01T00:00:00Z|02-02-2019T00:00:00Z|A           |d          |400        |Unable to read or parse message body|EA002     |
    #deactivateAt/activateAt: deactivate is before activate
    |c9621185-b86d-48a9-97f0-eeddef7c3dc1|value|2019-02-02T00:00:00Z|2019-01-01T00:00:00Z|A           |d          |400        |(activateAt) is equal or after (deactivateAt)|EA024     |
    |c9621185-b86d-48a9-97f0-eeddef7c3dc1|value|2019-02-02T00:00:00Z|2019-02-02T00:00:00Z|A           |d          |400        |(activateAt) is equal or after (deactivateAt)|EA024     |
    #entityStatus: without passing it
    |c9621185-b86d-48a9-97f0-eeddef7c3dc1|value|2019-01-01T00:00:00Z|2019-02-02T00:00:00Z|null        |d          |400        |Field error in object               |EA002     |
    #entityStatus: 2 character length
    |c9621185-b86d-48a9-97f0-eeddef7c3dc1|value|2019-01-01T00:00:00Z|2019-02-02T00:00:00Z|ad          |d          |400        |Field error in object               |EA002     |
    #entityStatus: not "A" or "D"
    |c9621185-b86d-48a9-97f0-eeddef7c3dc1|value|2019-01-01T00:00:00Z|2019-02-02T00:00:00Z|G           |d          |400        |Field error in object               |EA002     |
    #entityStatus: "a"
    |c9621185-b86d-48a9-97f0-eeddef7c3dc1|value|2019-01-01T00:00:00Z|2019-02-02T00:00:00Z|a           |d          |400        |Field error in object               |EA002     |
    #entityStatus: "d"
    |c9621185-b86d-48a9-97f0-eeddef7c3dc1|value|2019-01-01T00:00:00Z|2019-02-02T00:00:00Z|d           |d          |400        |Field error in object               |EA002     |
    #description: without passing it
    |c9621185-b86d-48a9-97f0-eeddef7c3dc1|value|2019-01-01T00:00:00Z|2019-02-02T00:00:00Z|A           |null       |400        |Field error in object               |EA002     |
    #description: more than 256 length
    |c9621185-b86d-48a9-97f0-eeddef7c3dc1|value|2019-01-01T00:00:00Z|2019-02-02T00:00:00Z|A           |bigvalue   |400        |Field error in object               |EA002     |