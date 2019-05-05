Feature: Management Put Public Keys - DRAG-1558

  Background: Retrieving access Token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

#    @trial
    @regression
    Scenario Outline: Negative flow - Unable to access the API after logging in with incorrect role
      Given I am a PUT public keys Dragon user with incorrect role
      And I have an "<applicationId>" and "<keyId>" from an existing public key
      And I enter a "<description>" description with value "<value>", activate At "<activateAt>", deactivate at "<deactivateAt>" and entity status "<entityStatus>"
      Then the PUT public key response should have error status "<http_status>" with error code "<err_code>" and description "<err_description>"
      Examples:
      |applicationId|keyId|description|value|activateAt|deactivateAt|entityStatus|http_status|err_code|err_description|

#    @trial
    @regression
    Scenario Outline: Positive flow - Able to update a public key
      Given I am a PUT public keys DRAGON user with correct role
      And I have an "<applicationId>" and "<keyId>" from an existing public key
      And I enter a "<description>" description with value "<value>", activate At "<activateAt>", deactivate at "<deactivateAt>" and entity status "<entityStatus>"
      Then the PUT public key response should return success
    Examples:
      |applicationId|keyId|description|value|activateAt|deactivateAt|entityStatus|

#    @trial
    @regression
    Scenario Outline: Negative flow - invalid application id and key id
      Given I am a PUT public keys DRAGON user with correct role
      And I have an "<applicationId>" and "<keyId>" from an existing public key
      And I enter a "test" description with value "value", activate At "01-01-2019T00:00:00", deactivate at "02-02-2019T00:00:00" and entity status "A"
      Then the PUT public key response should have error status "<http_status>" with error code "<err_code>" and description "<err_description>"
      Examples:
        |applicationId|keyId|http_status|err_code|err_description|
      #invalidate application id format
      # null application id
      # application id not found
      #invalidate key id format
      #null key id
      #key id not found

#    @trial
    @regression
    Scenario Outline: Negative flow - Invalid body
      Given I am a PUT public keys DRAGON user with correct role
      And I have an "applicationId" and "keyId" from an existing public key
      And I enter a "<description>" description with value "<value>", activate At "<activateAt>", deactivate at "<deactivateAt>" and entity status "<entityStatus>"
      Then the PUT public key response should have error status "<http_status>" with error code "<err_code>" and description "<err_description>"
      Examples:
        |description|value|activateAt|deactivateAt|entityStatus|http_status|err_code|err_description|
      # empty description
      #description > length 256
      #value empty
      #activateAt empty
      #activateAt invalid date
      #deactiveAt empty
      #deactiveAt invalid date
      #deactiveAt before activateAt
      #enitytStatus = Z
      #entityStatus empty
