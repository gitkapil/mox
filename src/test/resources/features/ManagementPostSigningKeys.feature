Feature: Merchant Management POST Signing Keys - DRAG-1565
         As a user
         I want to create signing key information and validate correct response is returned

  Background: Retrieving access Token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

  #@trial
  @regression @merchantManagement @merchantManagementPost
  Scenario Outline: Positive flow - Create a new application, new signing key
    Given I am an authorized Signing Key DRAGON user
    And I have an activate date "<activateAt>" and deactivate date "<deactivateAt>", with entity status "<entityStatus>"
    When I make a request to create a new signing key with application id
    Then the create signing key response should be successful

    Examples:
      | activateAt           | deactivateAt         | entityStatus |
      | 2019-01-01T00:00:00Z | 2020-10-01T00:00:00Z | A            |


 #@trial
  @regression @merchantManagement @merchantManagementPost
  Scenario Outline: Negative flow - Invalid application id
    Given I am an authorized Signing Key DRAGON user
    And I have an activate date "<activateAt>" and deactivate date "<deactivateAt>", with entity status "<entityStatus>"
    When I make a request to create a new signing key with "<applicationId>"
    Then the create signing key response should give a "<http_status>" http status with error code "<error_code>" and description "<error_description>"
    And error message should be "<error_message>" within the POST signing key response
    Examples:
      | applicationId                        | activateAt           | deactivateAt         | entityStatus | http_status | error_code | error_message                     | error_description                                                                                                                                                     |
    #invalid application id format
      | ab                                   | 2019-01-01T00:00:00Z | 2019-02-02T00:00:00Z | A            | 400         | EA002      | Service Request Validation Failed | Failed to convert value of type 'java.lang.String' to required type 'java.util.UUID'; nested exception is java.lang.IllegalArgumentException: Invalid UUID string: ab |
    #application id not found
      | 00000002-0000-4444-c000-000000000000 | 2019-01-01T00:00:00Z | 2019-02-02T00:00:00Z | A            | 400         | EA025      | Service Request Validation Failed | Application Id not found                                                                                                                                              |

 #@trial
  @regression @merchantManagement @merchantManagementPost
  Scenario Outline: Negative flow - Invalid dates
    Given I am an authorized Signing Key DRAGON user
    And I have an activate date "<activateAt>" and deactivate date "<deactivateAt>", with entity status "<entityStatus>"
    When I make a request to create a new signing key with application id
    Then the create signing key response should give a "<http_status>" http status with error code "<error_code>" and description "<error_description>"
    Examples:
      | activateAt           | deactivateAt          | entityStatus | http_status | error_code | error_description                             |
    #empty activateAt
      |                      | 2019-02-02-T00:00:00Z | A            | 400         | EA002      | Unable to read or parse message body          |
    #invalid activateAt format
      | Monday               | 2019-02-02-T00:00:00Z | A            | 400         | EA002      | Unable to read or parse message body          |
    #empty deactivateAt
      | 2019-01-01T00:00:00Z |                       | A            | 400         | EA002      | Field error in object                         |
    #invalid deactivateAt format
      | 2019-01-01T00:00:00Z | Tuesday               | A            | 400         | EA002      | Unable to read or parse message body          |
    #activateAt order than deactivateAt date
      | 2019-02-02T00:00:00Z | 2019-01-01T00:00:00Z  | A            | 400         | EA024      | (activateAt) is equal or after (deactivateAt) |
    #activateAt same as deactivateAt date
      | 2019-02-02T00:00:00Z | 2019-02-02T00:00:00Z  | A            | 400         | EA024      | (activateAt) is equal or after (deactivateAt) |

 #@trial
  @regression @merchantManagement @merchantManagementPost
  Scenario Outline: Negative flow - Entity status
    Given I am an authorized Signing Key DRAGON user
    And I have an activate date "<activateAt>" and deactivate date "<deactivateAt>", with entity status "<entityStatus>"
    When I make a request to create a new signing key with application id
    Then the create signing key response should give a "<http_status>" http status with error code "<error_code>" and description "<error_description>"
    Examples:
      | activateAt           | deactivateAt         | entityStatus | http_status | error_code | error_description     |
    #empty entity status
      | 2019-01-01T00:00:00Z | 2019-02-02T00:00:00Z |              | 400         | EA002      | Field error in object |
    #entity status = Z
      | 2019-01-01T00:00:00Z | 2019-02-02T00:00:00Z | Z            | 400         | EA002      | Field error in object |
    #entity status 2 characters
      | 2019-01-01T00:00:00Z | 2019-02-02T00:00:00Z | AB           | 400         | EA002      | Field error in object |
    #entity status = lowercase a
      | 2019-01-01T00:00:00Z | 2019-02-02T00:00:00Z | a            | 400         | EA002      | Field error in object |
    #entity status = lowercase d
      | 2019-01-01T00:00:00Z | 2019-02-02T00:00:00Z | d            | 400         | EA002      | Field error in object |


   #@trial
  @regression @merchantManagement @merchantManagementPost
  Scenario Outline: Negative flow- Mandatory fields not sent in the header
    Given I am an authorized Signing Key DRAGON user
    And I create a new application id for signing key
    And I have an activate date "<activateAt>" and deactivate date "<deactivateAt>", with entity status "<entityStatus>"
    When I make a POST request to the post signing key endpoint with "<key>" missing in the header
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" error code within the POST signing key response
    And error message should be "<error_message>" within the POST signing key response
    Examples:
      | error_description                                              | error_message                     | key               | error_code | http_status | activateAt           | deactivateAt         | entityStatus |
      | Error validating JWT                                           | API Gateway Authentication Failed | Authorization     | EA001      | 401         | 2019-01-01T00:00:00Z | 2020-10-01T00:00:00Z | A            |
      | Header Trace-Id was not found in the request. Access denied.   | API Gateway Validation Failed     | Trace-Id          | EA002      | 400         | 2019-01-01T00:00:00Z | 2020-10-01T00:00:00Z | A            |
      | Header Accept does not contain required value.  Access denied. | Request Header Not Acceptable     | Accept            | EA008      | 406         | 2019-01-01T00:00:00Z | 2020-10-01T00:00:00Z | A            |
      | Content type 'text/plain;charset=ISO-8859-1' not supported     | Service Request Validation Failed | Content-Type      | EA002      | 415         | 2019-01-01T00:00:00Z | 2020-10-01T00:00:00Z | A            |
