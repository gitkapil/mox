Feature: Message Signing - DRAG- 580

Background: Retrieving access Token
Given I am an user
When I make a request to the Dragon ID Manager
Then I recieve an access_token

@regression
Scenario: Negative flow- Invalid signing key id used to create signature and passed in POST payment request header
  Given I am an authorized user
  And I have valid payment details
  When I make a request for the payment with invalid signing key id
  Then I should recieve a "TODO" error response with "TODO" error description and "TODO" errorcode within payment response
  And error message should be "TODO" within payment response

@regression
Scenario: Negative flow- Invalid signing key used to create signature and passed in POST payment request header
  Given I am an authorized user
  And I have valid payment details
  When I make a request for the payment with invalid signing key
  Then I should recieve a "TODO" error response with "TODO" error description and "TODO" errorcode within payment response
  And error message should be "TODO" within payment response


@regression
Scenario: Negative flow- Different signing algo (HmacSHA512) used to create signature and passed in POST payment request header
  Given I am an authorized user
  And I have valid payment details
  When I make a request for the payment with a different signing algo
  Then I should recieve a "TODO" error response with "TODO" error description and "TODO" errorcode within payment response
  And error message should be "TODO" within payment response


@regression
Scenario Outline: Negative flow- Incomplete Header Set used to create signature and passed in POST payment request
  Given I am an authorized user
  And I have valid payment details
  When I make a request for the payment with header "<header>" value missing from the signature
  Then I should recieve a "TODO" error response with "TODO" error description and "TODO" errorcode within payment response
  And error message should be "TODO" within payment response

Examples:
|header|
|authorization|
|trace-id|
|api-version|
|request-date-time|











