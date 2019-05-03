Feature: Refund - DRAG- 179

Background: Retrieving access Token
  Given I am an user
  When I make a request to the Dragon ID Manager
  Then I receive an access_token


@trial
@regression
Scenario: Negative flow - Invalid user role
  Given I am logging in as a user with incorrect role
  When I try to make a call to refund
  Then I should receive a "401" error response with "Error validating JWT" error description and "401" errorcode within refund response

@trial
@regression
Scenario Outline: Negative flow - Invalid transaction Id
  Given I am logging in as a user with refund role
  When I try to make a call to refund with transaction id as "<transactionId>"
  Then I should receive a "<http_status>" error response with "<err_description>" error description and "<err_code>" errorcode within refund response
Examples:
  |transactionId|http_status|err_description|err_code|
  #null transactionId
  #invalid UUID format
  #transaction Id not found

@trial
@regression
Scenario Outline: Negative flow - Invalid body contents
  Given I am logging in as a user with refund role
  When I try to make a call to refund with transaction id as "<transactionId>"
  And I enter the refund data with payerId "<payerId>", refund amount "<amount>", refund currency "<currencyCode>", fee amount "<feeAmount>", fee currency "<feeCurrencyCode>", reason Code "<reasonCode>" and reason message "<reasonMessage>"
  Then I should receive a "<http_status>" error response with "<err_description>" error description and "<err_code>" errorcode within refund response
Examples:
  |transactionId|payerId|amount|currencyCode|feeAmount|feeCurrencyCode|reasonCode|reasonMessage|http_status|err_description|err_code|
  # empty payer Id
  # 21 length payer id
  # 23 length payer id
  # not sending payer id
  #refund amount -'ve
  # refund amount = 0
  #refund amount with 3 decimal places
  #refund amount = 1000000.01
  #without refund amount
  #refund currency code hkd
  #refund currency code usd
  #without refund currency code
  #without reason code
  #reason code = 000
  #reason code = 06
  #without reason message
  #reason message longer than 140 characters
  # reason message exists with reason code != 00



@trial
@regression
Scenario: Negative flow- Invalid auth token (without Bearer in the header)
  Given I am an authorized user
  And I have a valid transaction for refund
  And I dont send Bearer with the auth token in the refund request
  When I make a request for the refund
  Then I should receive a "401" error response with "Error validating JWT" error description and "401" errorcode within refund response
  And error message should be "TokenNotPresent" within refund response

@trial
@regression
Scenario Outline: Negative flow- Mandatory fields not sent in the header
  Given I am an authorized user
  And I have a valid transaction for refund
  When I make a request for the refund with "<key>" missing in the header
  Then I should receive a "<response_code>" error response with "<error_description>" error description and "<error_code>" errorcode within refund response
  And error message should be "<error_message>" within refund response

 Examples:
 |error_description                                                    |error_message         | key             |error_code |response_code |
 |Error validating JWT    | API Gateway Authentication Failed       |Authorization    |EA001      |401           |
 |Header Accept does not contain required value. Access denied.        | HeaderValueNotAllowed|Accept           |400        |400           |
 |Header Request-Date-Time was not found in the request. Access denied.| HeaderNotFound       |Request-Date-Time|400        |400           |
 |Header Trace-Id was not found in the request. Access denied.         | HeaderNotFound       |Trace-Id         |EA002      |400           |


@trial
@regression
Scenario Outline: Negative flow- Mandatory fields not sent in the body
  Given I am an authorized user
  And I have a valid transaction for refund
  When I make a request for the refund with "<key>" missing in the body
  Then I should receive a "400" error response with "<error_description>" error description and "EA002" errorcode within refund response
  And error message should be "Service Request Validation Failed" within refund response

 Examples:
 |error_description   | key         |
 |amount missing      | amount      |
 |currencyCode missing| currencyCode|


@trial
@regression
Scenario Outline: Negative flow- Request Date Time's invalid values set within the header
   Given I am an authorized user
   And I have a valid transaction for refund
   And I send invalid value "<value>" for the request date time in the refund request
   When I make a request for the refund
   Then I should receive a "400" error response with "<error_description>" error description and "EA002" errorcode within refund response
   And error message should be "Service Request Validation Failed" within refund response

   Examples:
  |value|error_description|
  |  | Request timestamp not a valid RFC3339 date-time |
  | xyz | Request timestamp not a valid RFC3339 date-time |
  | 2019-02-04T00:42:45.237Z | Request timestamp too old |