Feature: Management Put Signing Keys - DRAG-1573

  Background: Retrieving access Token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

#  @trial
#  @regression
  Scenario Outline: Positive flow - Able to update signing key attribute
    Given I am a PUT signing key authorized user
    When I create a new application id for PUT signing key
    And I create a new public key for it
    And I create a new signing key
    And I retrieve the applicationId and keyId from the signing key response
    And I update the signing key with description "<description>", activate at "<activateAt>", deactivate at "<deactivateAt>" and entity status "<entityStatus>"
    Then the PUT signing key response should return success
  Examples:
    |description|activateAt          |deactivateAt        |entityStatus|
    |test       |2019-01-01T00:00:00Z|2019-02-02T00:00:00Z|A           |

#  @trial
#  @regression
  Scenario Outline: Negative flow - invalid application id
    Given I am a PUT signing key authorized user
    And I have an "<applicationId>" and "<keyId>" from an existing signing key
    And I update the signing key with description "test", activate at "2019-01-01T00:00:00Z", deactivate at "2019-02-02T00:00:00Z" and entity status "D"
    Then the PUT signing key response should have error status "<http_status>" with error code "<err_code>" and description "<err_description>"
  Examples:
    |applicationId                       |keyId                               |http_status|err_code|err_description         |
    #invalid application Id
    |00000002-0000-4444-c000-000000000000|00000002-0000-0000-c000-000000000000|400        |EA025   |Application Id not found|
    #bad application id format
    |00000002-0000-4444-c000             |00000002-0000-0000-c000-000000000000|400        |EA002   |Failed to convert value of type|

#    @trial
#    @regression
    Scenario Outline: Negative flow - invalid key id
      Given I am a PUT signing key authorized user
      When I create a new application id for PUT signing key
      And I create a new public key for it
      And I create a new signing key
      And I retrieve the applicationId and keyId from the signing key response
      And I update the key id to "<keyId>"
      And I update the signing key with description "test", activate at "2019-01-01T00:00:00Z", deactivate at "2019-02-02T00:00:00Z" and entity status "D"
      Then the PUT signing key response should have error status "<http_status>" with error code "<err_code>" and description "<err_description>"
    Examples:
      |keyId|http_status|err_code|err_description         |
      #invalid key id
      |00000002-0000-0000-c000-000000000000|400        |EA027   |Key Id not found|
      #bad key id format
      |00000002-0000-0000-c000-0000        |400        |EA027   |Key Id not found|

#  @trial
#  @regression
  Scenario Outline: Negative flow - Invalid body
    Given I am a PUT signing key authorized user
    When I create a new application id for PUT signing key
    And I create a new public key for it
    And I create a new signing key
    And I retrieve the applicationId and keyId from the signing key response
    And I update the signing key with description "<description>", activate at "<activateAt>", deactivate at "<deactivateAt>" and entity status "<entityStatus>"
    Then the PUT signing key response should have error status "<http_status>" with error code "<err_code>" and description "<err_description>"
  Examples:
    |description    |activateAt|deactivateAt|entityStatus|http_status|err_code|err_description|
    |test           |          |2019-02-02T00:00:00Z|A|400 |EA002   |Field error in object           |
    |test           |Monday          |2019-02-02T00:00:00Z|A|400 |EA002   |Unable to read or parse message body           |
    |test           |2019-01-01T00:00:00Z| |A|400 |EA002   |Field error in object           |
    |test           |2019-01-01T00:00:00Z|Monday|A|400 |EA002   |Unable to read or parse message body           |
    |test           |2019-02-02T00:00:00Z|2019-01-01T00:00:00Z|A|400 |EA024   |(activateAt) is equal or after (deactivateAt)           |
    |test           |2019-01-01T00:00:00Z|2019-02-02T00:00:00Z|a|400 |EA002   |Field error in object           |
    |test           |2019-01-01T00:00:00Z|2019-02-02T00:00:00Z|d|400 |EA002   |Field error in object           |
    |test           |2019-01-01T00:00:00Z|2019-02-02T00:00:00Z|Z|400 |EA002   |Field error in object           |
    |test           |2019-01-01T00:00:00Z|2019-02-02T00:00:00Z|AB|400 |EA002   |Field error in object           |
    |test           |2019-01-01T00:00:00Z|2019-02-02T00:00:00Z||400 |EA002   |Field error in object           |