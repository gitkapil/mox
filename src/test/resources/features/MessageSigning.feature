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


@regression
Scenario Outline: Negative flow- Header Values tampered in the request
  Given I am an authorized user
  And I have valid payment details
  And I make a request for the payment
  And I should recieve a successful payment response
  When I use the same signature to trigger another payment request but with different value in "<header>"
  Then I should recieve a "TODO" error response with "TODO" error description and "TODO" errorcode within payment response
  And error message should be "TODO" within payment response

Examples:
|header|
|trace-id|
|request-date-time|
|authorization|


# Don't think we need this as we cant send the same header with a different body (Rate limit policy will apply). If we change header the last test case would suffice
Scenario: Negative flow- Body Values tampered in the request
  Given I am an authorized user
  And I have valid payment details
  And I make a request for the payment
  And I should recieve a successful payment response
  When I use the same signature to trigger another payment request but with a changed body
  Then I should recieve a "TODO" error response with "TODO" error description and "TODO" errorcode within payment response
  And error message should be "TODO" within payment response














