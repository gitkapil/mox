Feature: Message Signing - DRAG- 580

Background: Retrieving access Token
Given I am an user
When I make a request to the Dragon ID Manager
Then I recieve an access_token


Scenario: Negative flow- Invalid signing key id used to create signature and passed in POST payment request header
  Given I am an authorized user
  And I have valid payment details
  When I make a request for the payment with invalid signing key id
  Then I should recieve a "TODO" error response with "TODO" error description and "TODO" errorcode within payment response
  And error message should be "TODO" within payment response


Scenario: Negative flow- Invalid signing key used to create signature and passed in POST payment request header
  Given I am an authorized user
  And I have valid payment details
  When I make a request for the payment with invalid signing key
  Then I should recieve a "TODO" error response with "TODO" error description and "TODO" errorcode within payment response
  And error message should be "TODO" within payment response



Scenario: Negative flow- Different signing algo (HmacSHA512) used to create signature and passed in POST payment request header
  Given I am an authorized user
  And I have valid payment details
  When I make a request for the payment with a different signing algo
  Then I should recieve a "TODO" error response with "TODO" error description and "TODO" errorcode within payment response
  And error message should be "TODO" within payment response



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



Scenario Outline: Negative flow- New POST Payment request sent with tampered header values
  Given I am an authorized user
  And I have valid payment details
  And I create a signature for the payment request
  When I use the same signature to trigger another payment request but with different value in "<header>"
  Then I should recieve a "TODO" error response with "TODO" error description and "TODO" errorcode within payment response
  And error message should be "TODO" within payment response

Examples:
|header|
|trace-id|
|request-date-time|
|Authorization|



Scenario: Negative flow- Body Values tampered in the request
  Given I am an authorized user
  And I have valid payment details
  And I create a signature for the payment request
  When I use the generated signature with tampered body
  Then I should recieve a "TODO" error response with "TODO" error description and "TODO" errorcode within payment response
  And error message should be "TODO" within payment response



Scenario: Negative flow- Invalid signing key id used to create signature and passed in GET payment request header
  Given I am an authorized user
  And I have valid payment details
  And I make a request for the payment
  And I should recieve a successful payment response
  And I have a valid payment id
  When I make a request for the payment status with invalid signing key id
  Then I should recieve a "TODO" error response with "TODO" error description and "TODO" errorcode within check status response
  And error message should be "TODO" within check status response



Scenario: Negative flow- Invalid signing key used to create signature and passed in GET payment request header
  Given I am an authorized user
  And I have valid payment details
  And I make a request for the payment
  And I should recieve a successful payment response
  And I have a valid payment id
  When I make a request for the payment status with invalid signing key
  Then I should recieve a "TODO" error response with "TODO" error description and "TODO" errorcode within check status response
  And error message should be "TODO" within check status response



Scenario: Negative flow- Different signing algo (HmacSHA512) used to create signature and passed in GET payment request header
  Given I am an authorized user
  And I have valid payment details
  And I make a request for the payment
  And I should recieve a successful payment response
  And I have a valid payment id
  When I make a request for the payment status with a different signing algo
  Then I should recieve a "TODO" error response with "TODO" error description and "TODO" errorcode within check status response
  And error message should be "TODO" within check status response



Scenario Outline: Negative flow- Incomplete Header Set used to create signature and passed in GET payment request
  Given I am an authorized user
  And I have valid payment details
  And I make a request for the payment
  And I should recieve a successful payment response
  And I have a valid payment id
  When I make a request for the payment status with header "<header>" value missing from the signature
  Then I should recieve a "TODO" error response with "TODO" error description and "TODO" errorcode within check status response
  And error message should be "TODO" within check status response

Examples:
|header|
|authorization|
|trace-id|
|api-version|
|request-date-time|


 
Scenario Outline: Negative flow- New POST Payment request sent with tampered header values
  Given I am an authorized user
  And I have valid payment details
  And I make a request for the payment
  And I should recieve a successful payment response
  And I have a valid payment id
  And I create a signature for the payment status
  When I use the same signature to trigger another payment status request but with different value in "<header>"
  Then I should recieve a "TODO" error response with "TODO" error description and "TODO" errorcode within check status response
  And error message should be "TODO" within check status response

Examples:
|header|
|trace-id|
|request-date-time|
|Authorization|


Scenario: Positive flow- The outbound messages from DRAGON are also signed- POST Payment Request
  Given I am an authorized user
  And I have valid payment details
  When I make a request for the payment
  Then I should recieve a successful payment response
  And the payment request response should be signed

@trial
Scenario: Positive flow- The outbound messages from DRAGON are also signed- GET Payment Request
  Given I am an authorized user
  And I have valid payment details
  And I make a request for the payment
  And I should recieve a successful payment response
  And I have a valid payment id
  When I make a request for the check status
  Then I should recieve a successful check status response
  And the payment status response should be signed


