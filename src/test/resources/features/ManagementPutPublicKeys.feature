Feature: Management Put Public Keys - DRAG-1558

  Background: Retrieving access Token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

#    @trial
#    @regression
#    Scenario Outline: Negative flow - Unable to access the API after logging in with incorrect role
#      Given I am a PUT public keys Dragon user with incorrect role
#      And I have an "<applicationId>" and "<keyId>" from an existing public key
#      And I enter a "<description>" description with value "<value>", activate At "<activateAt>", deactivate at "<deactivateAt>" and entity status "<entityStatus>"
#      Then the PUT public key response should have error status "<http_status>" with error code "<err_code>" and description "<err_description>"
#      Examples:
#      |applicationId|keyId|description|value|activateAt|deactivateAt|entityStatus|http_status|err_code|err_description|

#    @trial
    @regression
    Scenario Outline: Positive flow - Able to update a public key
      Given I am a PUT public keys DRAGON user with correct role
      And I have an "<applicationId>" and "<keyId>" from an existing public key
      And I enter a "<description>" description with value "<value>", activate At "<activateAt>", deactivate at "<deactivateAt>" and entity status "<entityStatus>"
      Then the PUT public key response should return success
    Examples:
      |applicationId                       |keyId                               |description|value    |activateAt          |deactivateAt     |entityStatus|
      |c9621185-b86d-48a9-97f0-eeddef7c3dc1|af3177e4-6304-4c66-946c-de6e382b336c|test       |bigvalue |2020-01-01T00:00:00Z|2020-02-02T00:00Z|A           |

#    @trial
    @regression
    Scenario Outline: Negative flow - invalid application id and key id
      Given I am a PUT public keys DRAGON user with correct role
      And I have an "<applicationId>" and "<keyId>" from an existing public key
      And I enter a "test" description with value "value", activate At "01-01-2019T00:00:00", deactivate at "02-02-2019T00:00:00" and entity status "A"
      Then the PUT public key response should have error status "<http_status>" with error code "<err_code>" and description "<err_description>"
      Examples:
        |applicationId                       |keyId                               |http_status|err_code|err_description                      |
        #invalidate application id format
        |a                                   |af3177e4-6304-4c66-946c-de6e382b336c|400        |EA002    |Failed to convert value of type     |
        # null application id
        |null                                |af3177e4-6304-4c66-946c-de6e382b336c|400        |EA002    |Failed to convert value of type     |
        # application id not found
        |c9621185-b86d-48a9-97f0-eeddef7c3dc2|af3177e4-6304-4c66-946c-de6e382b336c|400        |EA002    |Unable to read or parse message body|
        #invalidate key id format
        |c9621185-b86d-48a9-97f0-eeddef7c3dc1|b                                   |400        |EA002    |Failed to convert value of type     |
        #null key id
        |c9621185-b86d-48a9-97f0-eeddef7c3dc1|null                                |400        |EA002    |Failed to convert value of type     |
        #key id not found
        |c9621185-b86d-48a9-97f0-eeddef7c3dc1|af3177e4-6304-4c66-946c-de6e382b337c|400        |EA002    |Unable to read or parse message body|

#    @trial
    @regression
    Scenario Outline: Negative flow - Invalid body
      Given I am a PUT public keys DRAGON user with correct role
      And I have an "applicationId" and "keyId" from an existing public key
      And I enter a "<description>" description with value "<value>", activate At "<activateAt>", deactivate at "<deactivateAt>" and entity status "<entityStatus>"
      Then the PUT public key response should have error status "<http_status>" with error code "<err_code>" and description "<err_description>"
      Examples:
        |description|value   |activateAt          |deactivateAt        |entityStatus|http_status|err_code|err_description                     |
        # empty description
        |null       |bigvalue|2019-01-01T00:00:00Z|2019-02-02T00:00:00Z|A           |400        |EA002   |Failed to convert value of type     |
        #description > length 256
        |bigbigvalue|bigvalue|2019-01-01T00:00:00Z|2019-02-02T00:00:00Z|A           |400        |EA002   |Failed to convert value of type     |
        # empty description
        |           |bigvalue|2019-01-01T00:00:00Z|2019-02-02T00:00:00Z|A           |400        |EA002   |Failed to convert value of type     |
        #value empty
        |test       |null    |2019-01-01T00:00:00Z|2019-02-02T00:00:00Z|A           |400        |EA002   |Failed to convert value of type     |
        #value is too small in length
        |test       |testing1|2019-01-01T00:00:00Z|2019-02-02T00:00:00Z|A           |400        |EA002   |Failed to convert value of type     |
        #activateAt empty
        |test       |bigvalue|null                |2019-02-02T00:00:00Z|A           |400        |EA002   |Failed to convert value of type     |
        #activateAt invalid date
        |test       |bigvalue|Monday              |2019-02-02T00:00:00Z|A           |400        |EA002   |Failed to convert value of type     |
        #activateAt weird date format
        |test       |bigvalue|01-01-2019T00:00:00Z|2019-02-02T00:00:00Z|A           |400        |EA002   |Failed to convert value of type     |
        #deactiveAt empty
        |test       |bigValue|2019-01-01T00:00:00Z|null                |A           |400        |EA002   |Failed to convert value of type     |
        #deactiveAt invalid date
        |test       |bigValue|2019-01-01T00:00:00Z|Tuesday             |A           |400        |EA002   |Failed to convert value of type     |
        #deactiveAt weird date format
        |test       |bigValue|2019-01-01T00:00:00Z|02-02-2019T00:00:00Z|A           |400        |EA002   |Failed to convert value of type     |
        #deactiveAt before activateAt
        |test       |bigValue|2019-02-02T00:00:00Z|2019-01-01T00:00:00Z|A           |400        |EA002   |Failed to convert value of type     |
        |test       |bigValue|2019-02-02T00:00:00Z|2019-02-02T00:00:00Z|A           |400        |EA002   |Failed to convert value of type     |
        #entityStatus == null
        |test       |bigValue|2019-01-01T00:00:00Z|2019-02-02T00:00:00Z|null        |400        |EA002   |Failed to convert value of type     |
        #enitytStatus = Z
        |test       |bigValue|2019-01-01T00:00:00Z|2019-02-02T00:00:00Z|z           |400        |EA002   |Failed to convert value of type     |
        |test       |bigValue|2019-01-01T00:00:00Z|2019-02-02T00:00:00Z|a           |400        |EA002   |Failed to convert value of type     |
        |test       |bigValue|2019-01-01T00:00:00Z|2019-02-02T00:00:00Z|d           |400        |EA002   |Failed to convert value of type     |
        |test       |bigValue|2019-01-01T00:00:00Z|2019-02-02T00:00:00Z|abc         |400        |EA002   |Failed to convert value of type     |
