Feature: PUT_Credentials - PUT Credentials Merchant
  As a user
  I want to create up to credentials for merchant and validate correct response is returned

  Background: Retrieving access Token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

  @regression @sc1
  Scenario Outline: SC-1 Positive flow - Update credentials name for existing credential
    Given I am an authorized to put credentials as DRAGON user
    When I hit the put credentials endpoint with new credential name "<credentialName>"
    Then put credentials response should be successful
    Examples:
      | credentialName |
      | validName      |
      | spaceInQuotes  |
      | doubleQuotes   |
      | $^&$^#$%^^^^^^ |
      | t1s2t3i4n5g6   |

  @regression
  Scenario Outline: SC-1 Positive flow - Deactivate the active credential
    Given I am an authorized to put credentials as DRAGON user
    When I hit the put credentials endpoint with new credential name "<credentialName>" and status "<status>"
    Then put credentials response should be updated
    Examples:
      | credentialName | status |
      | validName      | D      |

  @regression
  Scenario Outline: SC-1 Positive flow - Should not be able to expire the deactivated credential
    Given I am an authorized to put credentials as DRAGON user
    When I hit the put credentials endpoint with new credential name "<credentialName>" and status "<status>"
    Then I should receive a "<response_code>" error response with "<error_description>" error description and "<error_code>" errorCode within put credentials response
    And error message should be "<error_message>" within put credentials response
    Examples:
      | credentialName | status | response_code | error_code | error_message                     | error_description       |
      | validName      | E      | 400           | EA013      | Service Request Validation Failed | Status can only be 'D'. |


  @regression
  Scenario Outline: SC-1 Positive flow - Should not be able to activate the deactivated credentials
    Given I am an authorized to put credentials as DRAGON user
    When I hit the put credentials endpoint with new credential name "<credentialName>" and status "<status>"
    Then put credentials response should be updated
    When I hit update API to reactivate the deactivated credentials "<activateCredential>" and credential name "<credentialName>"
    Then I should receive a "<response_code>" error response with "<error_description>" error description and "<error_code>" errorCode within put credentials response
    And error message should be "<error_message>" within put credentials response
    Examples:
      | credentialName | status | activateCredential | response_code | error_code | error_message                     | error_description       |
      | validName      | D      | A                  | 400           | EA013      | Service Request Validation Failed | Status can only be 'D'. |


  @regression   @putCredentials
  Scenario Outline: SC-1 Positive flow - Should not be able to update credentials name same as any existing active credential name
    Given I am an authorized to put credentials as DRAGON user
    When I hit the put credentials endpoint to update new credentials name as existing credential name "<credentialName>"
    Then I should receive a "<response_code>" error response with "<error_description>" error description and "<error_code>" errorCode within put credentials response
    And error message should be "<error_message>" within put credentials response
    Examples:
      | credentialName | response_code | error_code | error_message             | error_description                                        |
      | validName      | 400           | EA002      | Business Rules Incorrect! | The Name is already in use for another ACTIVE KEY (NAME) |