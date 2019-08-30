@getKey
Feature: Merchant Management API - GET /keys/signing

  Background: Retrieving access Token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

#  @trial
  @regression
  Scenario Outline: positive flow - get signing keys
    Given I am a user with permissions to use signing key
    And I make a request to get signing keys with "<applicationID>" and with missing header "<headerValue>" values
    Then I should receive a successful signing key response
    Examples:
      | applicationID                        | headerValue |
      | c9621185-b86d-48a9-97f0-eeddef7c3dc1 |             |
   #| 96703cf3-82bb-429e-bcff-93a078b39307|             |

#  @trial
  @regression
  Scenario Outline: Negative flow - Invalid application Id
    Given I am a user with permissions to use signing key
    And I make a request to get signing keys with "<applicationID>" and with missing header "<missingHeader>" values
    Then I should receive a signing key response error "<http_status>" status with code "<error_code>" and description "<error_description>"
    Examples:
      | applicationID                        | http_status | error_code | error_description               | missingHeader |
    # Invalid application Id format
      | aaa                                  | 400         | EA002      | Failed to convert value of type |               |
    # Application Id not found
      | 00000002-0000-3333-c000-000000000000 | 400         | EA025      | Application Id not found        |               |

     # @trial
  @singingKeyError @regression
  Scenario Outline: Negative flow - get signing key with null or invalid header values
    Given I am a user with permissions to use signing key
    And I make a request to get signing keys with "<applicationID>" and with missing header "<missingHeader>" values
    Then I should receive a signing key response error "<http_status>" status with code "<error_code>" and description "<error_description>"
    And error message should be "<error_message>" within the get signing key response

    Examples:
      | applicationID                        | missingHeader     | error_message                     | error_code | http_status | error_description                                                     |
      | c9621185-b86d-48a9-97f0-eeddef7c3dc1 | Request-Date-Time | API Gateway Validation Failed     | EA002      | 400         | Header Request-Date-Time was not found in the request. Access denied. |
      | c9621185-b86d-48a9-97f0-eeddef7c3dc1 | Content-Type      | Service Request Validation Failed | EA002      | 415         | Content type                                                          |
      | c9621185-b86d-48a9-97f0-eeddef7c3dc1 | Accept            | Request Header Not Acceptable     | EA008      | 406         | Header Accept does not contain required value.  Access denied.        |
      | c9621185-b86d-48a9-97f0-eeddef7c3dc1 | Trace-Id          | API Gateway Validation Failed     | EA002      | 400         | Header Trace-Id was not found in the request. Access denied.          |


  @regression @negativeFlow
  Scenario Outline: Negative flow- Api version Field is missing from the header
    Given I am a GET signing key authorized DRAGON user with the signingKey.ReadWrite.All privilege
    And I make a get request to the signingKey endpoint with "<applicationId>" and "<key>" missing in the header
    And error message should be "<errorMessage>" within the get signing key response

    Examples:
      | key           | errorMessage       | applicationId                        |
      | Api-Version   | Resource not found | c9621185-b86d-48a9-97f0-eeddef7c3dc1 |
      | Authorization | Resource not found | c9621185-b86d-48a9-97f0-eeddef7c3dc1 |