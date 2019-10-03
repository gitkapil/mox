Feature: Retrieve Access Token - DRAG-310

  @regression
  Scenario: Positive flow- A valid merchant receives a valid access token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token
    And it should be a valid JWT
    And response should also have expiresOn, token type

# DRAG-1165
  @regression
  Scenario Outline: Negative flow- An invalid merchant (invalid client id) should not receive a valid access token
    Given I am an user
    And I have "<invalid_value>" as client id
    When I make a request to the Dragon ID Manager
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorcode within token response
    And error message should be "Service Request Validation Failed" within token response

    Examples:
      | invalid_value                        | error_description                                                        | http_status | error_code |
      | random_client_id                     | Request could not be understood. Please modify the request and try again | 400         | EA002      |
      |                                      | Request could not be understood. Please modify the request and try again | 400         | EA002      |
      | 06eae1e2-4886-47e1-8be7-062182f698e8 | Request could not be understood. Please modify the request and try again | 400         | EA002      |

# DRAG-1166 Accurate Error description need to be added
  @regression
  Scenario Outline: Negative flow- An invalid merchant (invalid client secret) should not receive a valid access token
    Given I am an user
    And I have "<invalid_value>" as client secret
    When I make a request to the Dragon ID Manager
    Then I should receive a "401" error response with "<error_description>" error description and "EA001" errorcode within token response
    And error message should be "Service Request Authentication Failed" within token response

    Examples:
      | invalid_value        | error_description                                                                  |
      | random_client_secret | Authorisation failure. Please check the client_id and client_secret and try again. |
      |                      | Authorisation failure. Please check the client_id and client_secret and try again. |


  @regression
  Scenario Outline: Negative flow- Mandatory Fields missing from the body
    Given I am an user
    And I dont provide "<parameter>"
    When I make a request to the Dragon ID Manager
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorcode within token response
    And error message should be "<error_message>" within token response

    Examples:
      | parameter             | http_status | error_code | error_message                         | error_description                                           |
      | clientid              | 401         | EA001      | Service Request Authentication Failed | Please check the client_id and client_secret and try again. |
      | clientsecret          | 401         | EA001      | Service Request Authentication Failed | Please check the client_id and client_secret and try again. |
      | clientid&clientsecret | 401         | EA001      | Service Request Authentication Failed | Please check the client_id and client_secret and try again. |

#DRAG-1138- fix --- Error description for the Content-Type need to be updated when it is defined.
  @regression
  Scenario Outline: Negative flow- Mandatory Fields missing from the header
    Given I am an user
    And I dont provide "<parameter>"
    When I make a request to the Dragon ID Manager
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorcode within token response
    And error message should be "<error_message>" within token response

    Examples:
      | parameter    | http_status | error_code | error_message                 | error_description                                                   |
      | Accept       | 406         | EA008      | Request Header Not Acceptable | Header Accept does not contain required value. Access denied.       |
      | Content-Type | 415         | EA018      | Content-Type Unsupported      | Header Content-Type does not contain required value. Access denied. |

  @regression
  Scenario Outline: Negative flow- Mandatory Fields missing from the header
    Given I am an user
    And I dont provide "<parameter>"
    When I make a request to the Dragon ID Manager
    And error message should be "Resource not found" within token response

    Examples:
      | parameter   |
      | Api-Version |


  Scenario: Negative flow- Body sent in an invalid format
    Given I am an user
    When I make a request to the Dragon ID Manager with body in JSON format
    Then I should receive a "401" error response with "Authorisation failure. Please check the client_id and client_secret and try again." error description and "EA001" errorcode within token response
    And error message should be "Service Request Authentication Failed" within token response
# DRAG-1138 - fix

  @regression
  Scenario Outline: Negative flow- Invalid Accept type header values sent
    Given I am an user
    And I have "<invalid_value>" value for the header "Accept"
    When I make a request to the Dragon ID Manager
    Then I should receive a "406" error response with "<error_description>" error description and "EA008" errorcode within token response
    And error message should be "Request Header Not Acceptable" within token response

    Examples:

      | invalid_value      | error_description                                             |
      | random_Accept-type | Header Accept does not contain required value. Access denied. |
# DRAG-1138 - fix

  @regression
  Scenario Outline: Negative flow- Invalid Content-type header values sent
    Given I am an user
    And I have "<invalid_value>" value for the header "Content-Type"
    When I make a request to the Dragon ID Manager
    Then I should receive a "415" error response with "<error_description>" error description and "EA018" errorcode within token response
    And error message should be "Content-Type Unsupported" within token response

    Examples:
      | invalid_value    | error_description                                                   |
      | application/json | Header Content-Type does not contain required value. Access denied. |


  @regression
  Scenario Outline: Negative flow- Invalid header values sent
    Given I am an user
    And I have invalid_value for the header "<parameter>"
    When I make a request to the Dragon ID Manager
    And error message should be "Resource not found" within token response

    Examples:
      | parameter   |
      | Api-Version |
