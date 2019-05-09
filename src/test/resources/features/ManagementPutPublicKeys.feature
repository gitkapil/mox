Feature: Management Put Public Keys - DRAG-1558

  Background: Retrieving access Token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

    @trial
    @regression
    Scenario Outline: Positive flow - Able to update public key attributes
      Given I am a POST create keys authorized DRAGON user with the correct privileges
      When I have a valid base64 encoded RSA public key, activate at "<activateAt>", deactivate at "<deactivateAt>", "<entityStatus>" as entity status and description is "<description>"
      And I create a new public key based on using an existing application key
      And I retrieve the applicationId and the keyId from the response
      And I am a PUT public keys DRAGON user with correct role
      And I enter a "<updateDescription>" description, activate At "<updateActivateAt>", deactivate at "<updateDeactivateAt>" and entity status "<updateEntityStatus>"
      Then the PUT public key response should return success
    Examples:
      |description|activateAt          |deactivateAt     |entityStatus|updateActivateAt    |updateDeactivateAt  |updateDescription |updateEntityStatus|
      |test       |2020-01-01T00:00:00Z|2020-02-02T00:00Z|A           |2019-01-01T00:00:00Z|2029-01-01T00:00:00Z|updated test      |A                 |
      |test 2     |2020-01-01T00:00:00Z|2020-02-02T00:00Z|A           |2019-01-01T00:00:00Z|2029-01-01T00:00:00Z|updated test 2    |D                 |
      |test 3     |2020-01-01T00:00:00Z|2020-02-02T00:00Z|A           |2019-01-01T00:00:00Z|2029-01-01T00:00:00Z|updated test 3    |D                 |

    @trial
    @regression
    Scenario Outline: Negative flow - invalid application id and key id
      Given I am a PUT public keys DRAGON user with correct role
      And I have an "<applicationId>" and "<keyId>" from an existing public key
      And I enter a "test" description, activate At "2019-01-01T00:00:00Z", deactivate at "2029-01-01T00:00:00Z" and entity status "A"
      Then the PUT public key response should have error status "<http_status>" with error code "<err_code>" and description "<err_description>"
      Examples:
        |applicationId                       |keyId                               |http_status|err_code|err_description                      |
        # application id is empty
        |                                    |af3177e4-6304-4c66-946c-de6e382b336c|404|  |  |
        # application id not found
        |c9621185-b86d-48a9-97f0-eeddef7c3dc2|af3177e4-6304-4c66-946c-de6e382b336c|400|EA025|Application Id not found|
        # application id illegal format
        |xxxxxxxx|af3177e4-6304-4c66-946c-de6e382b336c|400|EA002|Invalid UUID string|
        # key id is empty
        |c9621185-b86d-48a9-97f0-eeddef7c3dc1|                                    |404|  |  |
        # key id not found
        |c9621185-b86d-48a9-97f0-eeddef7c3dc1|00000000-6304-4c66-946c-de6e382b336c|400|EA027|Key Id not found|
        # key id illegal format
        |c9621185-b86d-48a9-97f0-eeddef7c3dc1|xxxxxxxx|400|EA002|Invalid UUID string|

    @trial
    @regression
    Scenario Outline: Negative flow - Invalid body
      Given I am a PUT public keys DRAGON user with correct role
      And I have an "c9621185-b86d-48a9-97f0-eeddef7c3dc1" and "af3177e4-6304-4c66-946c-de6e382b336c" from an existing public key
      And I enter a "<description>" description, activate At "<activateAt>", deactivate at "<deactivateAt>" and entity status "<entityStatus>"
      Then the PUT public key response should have error status "<http_status>" with error code "<err_code>" and description "<err_description>"
      Examples:
        |description|activateAt          |deactivateAt        |entityStatus|http_status|err_code|err_description                     |
        # empty description
        |null       |2019-01-01T00:00:00Z|2019-02-02T00:00:00Z|A           |400        |EA002   |field 'description' may not be null     |
        #description > length 256
        |bigbigvalue|2019-01-01T00:00:00Z|2019-02-02T00:00:00Z|A           |400        |EA002   |field 'description' size must be between 0 and 256     |
        #activateAt empty
        |test       |null                |2019-02-02T00:00:00Z|A           |400        |EA002   |field 'activateAt' may not be null     |
        #activateAt invalid date
        |test       |Monday              |2019-02-02T00:00:00Z|A           |400        |EA002   |Unable to read or parse message body: json parse error at     |
        #activateAt weird date format
        |test       |01-01-2019T00:00:00Z|2019-02-02T00:00:00Z|A           |400        |EA002   |Unable to read or parse message body: json parse error at     |
        #deactivateAt empty
        |test       |2019-01-01T00:00:00Z|null                |A           |400        |EA002   |field 'deactivateAt' may not be null     |
        #deactivateAt invalid date
        |test       |2019-01-01T00:00:00Z|Tuesday             |A           |400        |EA002   |Unable to read or parse message body: json parse error at     |
        #deactivateAt weird date format
        |test       |2019-01-01T00:00:00Z|02-02-2019T00:00:00Z|A           |400        |EA002   |Unable to read or parse message body: json parse error at     |
        #deactivateAt before activateAt
        |test       |2019-02-02T00:00:00Z|2019-01-01T00:00:00Z|A           |400        |EA024   |(activateAt) is equal or after (deactivateAt)    |
        |test       |2019-02-02T00:00:00Z|2019-02-02T00:00:00Z|A           |400        |EA024   |(activateAt) is equal or after (deactivateAt)    |
        #entityStatus == null
        |test       |2019-01-01T00:00:00Z|2019-02-02T00:00:00Z|null        |400        |EA002   |field 'entityStatus' may not be null     |
        #enitytStatus = Un-recognised values -> null
        |test       |2019-01-01T00:00:00Z|2019-02-02T00:00:00Z|z           |400        |EA002   |field 'entityStatus' may not be null     |
        |test       |2019-01-01T00:00:00Z|2019-02-02T00:00:00Z|a           |400        |EA002   |field 'entityStatus' may not be null     |
        |test       |2019-01-01T00:00:00Z|2019-02-02T00:00:00Z|d           |400        |EA002   |field 'entityStatus' may not be null     |
        |test       |2019-01-01T00:00:00Z|2019-02-02T00:00:00Z|abc         |400        |EA002   |field 'entityStatus' may not be null     |
