@newGet
Feature: Merchant Management API - GET /keys/signing

  Background: Retrieving access Token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

#  @trial
  @regression
  Scenario: positive flow - get signing keys
    Given I am a user with permissions to use signing key
    And I make a request to get signing keys with application id
    Then I should receive a successful signing key response

#  @trial
  @regression
  Scenario Outline: Negative flow - Invalid application Id
    Given I am a user with permissions to use signing key
    And I make a request to get signing keys with application id "<applicationId>"
    Then I should receive a signing key response error "<http_status>" status with code "<error_code>" and description "<error_description>"
    And error message should be "<error_message>" within the get signing key response
    Examples:
      | applicationId                        | http_status | error_code | error_message                     | error_description                                                                                                                                                     |
      | ab                                   | 400         | EA002      | Service Request Validation Failed | Failed to convert value of type 'java.lang.String' to required type 'java.util.UUID'; nested exception is java.lang.IllegalArgumentException: Invalid UUID string: ab |
      | 00000002-0000-4444-c000-000000000000 | 400         | EA025      | Service Request Validation Failed | Application Id not found                                                                                                                                              |



     # @trial
  @regression
  Scenario Outline: Negative flow - get signing key with null or invalid header values
    Given I am a user with permissions to use signing key
    And I make a request to get signing keys with application id and missing header "<missingHeader>"
    Then I should receive a signing key response error "<http_status>" status with code "<error_code>" and description "<error_description>"
    And error message should be "<error_message>" within the get signing key response

    Examples:
      | missingHeader | error_message                     | error_code | http_status | error_description                                              |
      | Content-Type  | Service Request Validation Failed | EA002      | 415         | Content type                                                   |
      | Accept        | Request Header Not Acceptable     | EA008      | 406         | Header Accept does not contain required value.  Access denied. |
      | Trace-Id      | API Gateway Validation Failed     | EA002      | 400         | Header Trace-Id was not found in the request. Access denied.   |


  @regression
  Scenario Outline: Negative flow- Api version Field is missing from the header
    Given I am a GET signing key authorized DRAGON user with the signingKey.ReadWrite.All privilege
    And I make a request to get signing keys with application id and missing header "<key>"
    And error message should be "<errorMessage>" within the get signing key response

    Examples:
      | key           | errorMessage                      |
      | Api-Version   | Resource not found                |
      | Authorization | API Gateway Authentication Failed |

