@postSigningKey
Feature: Merchant Management POST Signing Keys - DRAG-1565

  Background: Retrieving access Token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

#  @trial
  @regression
  Scenario Outline: Positive flow - Create a new application, new public key, new signing key
    Given I am an authorized Signing Key DRAGON user
    And I have a "<applicationID>" application id
    And I have an activate date "<activateAt>" and deactivate date "<deactivateAt>", with entity status "<entityStatus>"
    When I make a request to create a new signing key with "<applicationID>"
    Then the create signing key response should be successful
    Examples:
      | activateAt           | deactivateAt         | entityStatus | applicationID                        |
      | 2019-01-01T00:00:00Z | 2020-10-01T00:00:00Z | A            | c9621185-b86d-48a9-97f0-eeddef7c3dc1 |

#  @trial
  @regression
  Scenario Outline: Negative flow - Invalid application id
    Given I am an authorized Signing Key DRAGON user
    And I have a "<applicationId>" application id
    And I have an activate date "<activateAt>" and deactivate date "<deactivateAt>", with entity status "<entityStatus>"
    When I make a request to create a new signing key with "<applicationId>"
    Then the create signing key response should give a "<http_status>" http status with error code "<error_code>" and description "<error_description>"
    Examples:
      | applicationId                        | activateAt           | deactivateAt         | entityStatus | http_status | error_code | error_description               |
    #invalid application id format
      | ab                                   | 2019-01-01T00:00:00Z | 2019-02-02T00:00:00Z | A            | 400         | EA002      | Failed to convert value of type |
    #application id not found
      | 00000002-0000-4444-c000-000000000000 | 2019-01-01T00:00:00Z | 2019-02-02T00:00:00Z | A            | 400         | EA025      | Application Id not found        |

#  @trial
  @regression
  Scenario Outline: Negative flow - Invalid dates
    Given I am an authorized Signing Key DRAGON user
    And I have a "<applicationID>" application id
    And I have an activate date "<activateAt>" and deactivate date "<deactivateAt>", with entity status "<entityStatus>"
    When I make a request to create a new signing key with "<applicationID>"
    Then the create signing key response should give a "<http_status>" http status with error code "<error_code>" and description "<error_description>"
    Examples:
      | activateAt           | deactivateAt          | entityStatus | http_status | error_code | error_description                             | applicationID                        |
    #empty activateAt
      |                      | 2019-02-02-T00:00:00Z | A            | 400         | EA002      | Unable to read or parse message body          | c9621185-b86d-48a9-97f0-eeddef7c3dc1 |
    #invalid activateAt format
      | Monday               | 2019-02-02-T00:00:00Z | A            | 400         | EA002      | Unable to read or parse message body          | c9621185-b86d-48a9-97f0-eeddef7c3dc1 |
    #empty deactivateAt
      | 2019-01-01T00:00:00Z |                       | A            | 400         | EA002      | Field error in object                         | c9621185-b86d-48a9-97f0-eeddef7c3dc1 |
    #invalid deactivateAt format
      | 2019-01-01T00:00:00Z | Tuesday               | A            | 400         | EA002      | Unable to read or parse message body          | c9621185-b86d-48a9-97f0-eeddef7c3dc1 |
    #activateAt order than deactivateAt date
      | 2019-02-02T00:00:00Z | 2019-01-01T00:00:00Z  | A            | 400         | EA024      | (activateAt) is equal or after (deactivateAt) | c9621185-b86d-48a9-97f0-eeddef7c3dc1 |
    #activateAt same as deactivateAt date
      | 2019-02-02T00:00:00Z | 2019-02-02T00:00:00Z  | A            | 400         | EA024      | (activateAt) is equal or after (deactivateAt) | c9621185-b86d-48a9-97f0-eeddef7c3dc1 |

#  @trial
  @regression
  Scenario Outline: Negative flow - Entity status
    Given I am an authorized Signing Key DRAGON user
    And I have a "<applicationID>" application id
    And I have an activate date "<activateAt>" and deactivate date "<deactivateAt>", with entity status "<entityStatus>"
    When I make a request to create a new signing key with "<applicationID>"
    Then the create signing key response should give a "<http_status>" http status with error code "<error_code>" and description "<error_description>"
    Examples:
      | activateAt           | deactivateAt         | entityStatus | http_status | error_code | error_description     | applicationID                        |
    #empty entity status
      | 2019-01-01T00:00:00Z | 2019-02-02T00:00:00Z |              | 400         | EA002      | Field error in object | c9621185-b86d-48a9-97f0-eeddef7c3dc1 |
    #entity status = Z
      | 2019-01-01T00:00:00Z | 2019-02-02T00:00:00Z | Z            | 400         | EA002      | Field error in object | c9621185-b86d-48a9-97f0-eeddef7c3dc1 |
    #entity status 2 characters
      | 2019-01-01T00:00:00Z | 2019-02-02T00:00:00Z | AB           | 400         | EA002      | Field error in object | c9621185-b86d-48a9-97f0-eeddef7c3dc1 |
    #entity status = lowercase a
      | 2019-01-01T00:00:00Z | 2019-02-02T00:00:00Z | a            | 400         | EA002      | Field error in object | c9621185-b86d-48a9-97f0-eeddef7c3dc1 |
    #entity status = lowercase d
      | 2019-01-01T00:00:00Z | 2019-02-02T00:00:00Z | d            | 400         | EA002      | Field error in object | c9621185-b86d-48a9-97f0-eeddef7c3dc1 |


  @regression @merchantManagement @merchantManagementPost @postKey
  Scenario Outline: Negative flow- Mandatory fields not sent in the header
    Given I am an authorized Signing Key DRAGON user
    And I have a "<applicationID>" application id
    And I have an activate date "<activateAt>" and deactivate date "<deactivateAt>", with entity status "<entityStatus>"
    When I make a POST request to the post signing key endpoint with "<key>" missing in the header
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" error code within the POST signing key response
    And error message should be "<error_message>" within the POST signing key response
    Examples:
      | applicationID                        | error_description                                                     | error_message                     | key               | error_code | http_status | activateAt           | deactivateAt         | entityStatus |
      | c9621185-b86d-48a9-97f0-eeddef7c3dc1 | Error validating JWT                                                  | API Gateway Authentication Failed | Authorization     | EA001      | 401         | 2019-01-01T00:00:00Z | 2020-10-01T00:00:00Z | A            |
      | c9621185-b86d-48a9-97f0-eeddef7c3dc1 | Header Request-Date-Time was not found in the request. Access denied. | API Gateway Validation Failed     | Request-Date-Time | EA002      | 400         | 2019-01-01T00:00:00Z | 2020-10-01T00:00:00Z | A            |
      | c9621185-b86d-48a9-97f0-eeddef7c3dc1 | Header Trace-Id was not found in the request. Access denied.          | API Gateway Validation Failed     | Trace-Id          | EA002      | 400         | 2019-01-01T00:00:00Z | 2020-10-01T00:00:00Z | A            |
      | c9621185-b86d-48a9-97f0-eeddef7c3dc1 | Header Accept does not contain required value.  Access denied.        | Request Header Not Acceptable     | Accept            | EA008      | 406         | 2019-01-01T00:00:00Z | 2020-10-01T00:00:00Z | A            |
      | c9621185-b86d-48a9-97f0-eeddef7c3dc1 | Request timestamp not a valid RFC3339 date-time                       | Service Request Validation Failed | Content-Type      | EA002      | 400         | 2019-01-01T00:00:00Z | 2020-10-01T00:00:00Z | A            |
