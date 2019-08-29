@postKey
Feature: Merchant Management POST Signing Keys - DRAG-1565

  Background: Retrieving access Token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

#  @trial
  @regression
  Scenario Outline: Positive flow - Create a new application, new public key, new signing key
    Given I am an authorized Signing Key DRAGON user
    And I make a request to create a new signing key with "<applicationID>"
    And I have an activate date "<activateDate>" and deactivate date "<deactivateDate>", with entity status "<entityStatus>" and a description "<description>"
    When I make a request to create a new signing key with "<applicationID>"
    Then the create signing key response should be successful
    Examples:
      | activateDate         | deactivateDate       | entityStatus | description | applicationID                        |
      | 2019-01-01T00:00:00Z | 2020-10-01T00:00:00Z | A            | test        | c9621185-b86d-48a9-97f0-eeddef7c3dc1 |

#  @trial
#  @regression @postKey
#  Scenario Outline: Negative flow - Create a new application and signing key without public key
#    Given I am an authorized Signing Key DRAGON user
#    And I make a request to get signing keys with "<applicationID>"
#    And I have an activate date "<activateAt>" and deactivate date "<deactivateAt>", with entity status "<entityStatus>" and a description "<description>"
#    When I make a request to create a new signing key with "<applicationID>"
#    Then the create signing key response should give a "<http_status>" http status with error code "<error_code>" and description "<error_description>"
#  Examples:
#    |activateAt          |deactivateAt        |entityStatus|description|http_status|error_code|error_description                                                       | applicationID|
#    |2019-01-01T00:00:00Z|2019-02-02T00:00:00Z|A           |test       |400        |EA028     |Active Public Key not found, please update expired key or create new key| c9621185-b86d-48a9-97f0-eeddef7c3dc1|


#  @trial
  @regression
  Scenario Outline: Negative flow - Invalid application id
    Given I am an authorized Signing Key DRAGON user
    And I have a "<applicationId>" application id
    And I have an activate date "<activateAt>" and deactivate date "<deactivateAt>", with entity status "<entityStatus>" and a description "<description>"
    When I make a request to create a new signing key with "<applicationId>"
    Then the create signing key response should give a "<http_status>" http status with error code "<error_code>" and description "<error_description>"
    Examples:
      | applicationId                        | activateAt           | deactivateAt         | entityStatus | description | http_status | error_code | error_description               |
    #invalid application id format
      | ab                                   | 2019-01-01T00:00:00Z | 2019-02-02T00:00:00Z | A            | test        | 400         | EA002      | Failed to convert value of type |
    #application id not found
      | 00000002-0000-4444-c000-000000000000 | 2019-01-01T00:00:00Z | 2019-02-02T00:00:00Z | A            | test        | 400         | EA025      | Application Id not found        |

#  @trial
  @regression
  Scenario Outline: Negative flow - Invalid dates
    Given I am an authorized Signing Key DRAGON user
    And I make a request to create a new signing key with "<applicationID>"
    And I have an activate date "<activateAt>" and deactivate date "<deactivateAt>", with entity status "<entityStatus>" and a description "<description>"
    When I make a request to create a new signing key with "<applicationID>"
    Then the create signing key response should give a "<http_status>" http status with error code "<error_code>" and description "<error_description>"
    Examples:
      | activateAt           | deactivateAt          | entityStatus | description | http_status | error_code | error_description                             | applicationID                        |
    #empty activateAt
      |                      | 2019-02-02-T00:00:00Z | A            | test        | 400         | EA002      | Unable to read or parse message body          | c9621185-b86d-48a9-97f0-eeddef7c3dc1 |
    #invalid activateAt format
      | Monday               | 2019-02-02-T00:00:00Z | A            | test        | 400         | EA002      | Unable to read or parse message body          | c9621185-b86d-48a9-97f0-eeddef7c3dc1 |
    #empty deactivateAt
      | 2019-01-01T00:00:00Z |                       | A            | test        | 400         | EA002      | Field error in object                         | c9621185-b86d-48a9-97f0-eeddef7c3dc1 |
    #invalid deactivateAt format
      | 2019-01-01T00:00:00Z | Tuesday               | A            | test        | 400         | EA002      | Unable to read or parse message body          | c9621185-b86d-48a9-97f0-eeddef7c3dc1 |
    #activateAt order than deactivateAt date
      | 2019-02-02T00:00:00Z | 2019-01-01T00:00:00Z  | A            | test        | 400         | EA024      | (activateAt) is equal or after (deactivateAt) | c9621185-b86d-48a9-97f0-eeddef7c3dc1 |
    #activateAt same as deactivateAt date
      | 2019-02-02T00:00:00Z | 2019-02-02T00:00:00Z  | A            | test        | 400         | EA024      | (activateAt) is equal or after (deactivateAt) | c9621185-b86d-48a9-97f0-eeddef7c3dc1 |

#  @trial
  @regression
  Scenario Outline: Negative flow - Entity status
    Given I am an authorized Signing Key DRAGON user
    And I make a request to create a new signing key with "<applicationID>"
    And I have an activate date "<activateAt>" and deactivate date "<deactivateAt>", with entity status "<entityStatus>" and a description "<description>"
    When I make a request to create a new signing key with "<applicationID>"
    Then the create signing key response should give a "<http_status>" http status with error code "<error_code>" and description "<error_description>"
    Examples:
      | activateAt           | deactivateAt         | entityStatus | description | http_status | error_code | error_description     | applicationID                        |
    #empty entity status
      | 2019-01-01T00:00:00Z | 2019-02-02T00:00:00Z |              | test        | 400         | EA002      | Field error in object | c9621185-b86d-48a9-97f0-eeddef7c3dc1 |
    #entity status = Z
      | 2019-01-01T00:00:00Z | 2019-02-02T00:00:00Z | Z            | test        | 400         | EA002      | Field error in object | c9621185-b86d-48a9-97f0-eeddef7c3dc1 |
    #entity status 2 characters
      | 2019-01-01T00:00:00Z | 2019-02-02T00:00:00Z | AB           | test        | 400         | EA002      | Field error in object | c9621185-b86d-48a9-97f0-eeddef7c3dc1 |
    #entity status = lowercase a
      | 2019-01-01T00:00:00Z | 2019-02-02T00:00:00Z | a            | test        | 400         | EA002      | Field error in object | c9621185-b86d-48a9-97f0-eeddef7c3dc1 |
    #entity status = lowercase d
      | 2019-01-01T00:00:00Z | 2019-02-02T00:00:00Z | d            | test        | 400         | EA002      | Field error in object | c9621185-b86d-48a9-97f0-eeddef7c3dc1 |

#  @trial
  @regression
  Scenario Outline: Negative flow - Description
    Given I am an authorized Signing Key DRAGON user
    And I make a request to create a new signing key with "<applicationID>"
    And I have an activate date "<activateAt>" and deactivate date "<deactivateAt>", with entity status "<entityStatus>" and a description "<description>"
    When I make a request to create a new signing key with "<applicationID>"
    Then the create signing key response should give a "<http_status>" http status with error code "<error_code>" and description "<error_description>"
    Examples:
      | activateAt           | deactivateAt         | entityStatus | description    | http_status | error_code | error_description     | applicationID                        |
    #description > 256 characters
      | 2019-01-01T00:00:00Z | 2019-02-02T00:00:00Z | A            | bigDescription | 400         | EA002      | Field error in object | c9621185-b86d-48a9-97f0-eeddef7c3dc1 |
