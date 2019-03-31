Feature: Message Signing - DRAG- 580

Background: Retrieving access Token
Given I am an user
When I make a request to the Dragon ID Manager
Then I receive an access_token

@regression 
Scenario: Negative flow- Invalid signing key id used to create signature and passed in POST payment request header
  Given I am an authorized user
  And I have valid payment details
  When I make a request for the payment with invalid signing key id
  Then I should receive a "401" error response with "Unable to verify signature" error description and "EA001" errorcode within payment response
  And error message should be "Unauthorized Operation!" within payment response

@regression 
Scenario: Negative flow- Invalid signing key used to create signature and passed in POST payment request header
  Given I am an authorized user
  And I have valid payment details
  When I make a request for the payment with invalid signing key
  Then I should receive a "401" error response with "Unable to verify signature" error description and "EA001" errorcode within payment response
  And error message should be "Unauthorized Operation!" within payment response

@regression
Scenario: Negative flow- Different signing algo (HmacSHA512) used to create signature and passed in POST payment request header
  Given I am an authorized user
  And I have valid payment details
  When I make a request for the payment with a different signing algo
  Then I should receive a "401" error response with "Unable to verify signature" error description and "EA001" errorcode within payment response
  And error message should be "Unauthorized Operation!" within payment response



Scenario Outline: Negative flow- Incomplete Header Set used to create signature and passed in POST payment request
  Given I am an authorized user
  And I have valid payment details
  When I make a request for the payment with header "<header>" value missing from the signature
  Then I should receive a "401" error response with "Unable to verify signature" error description and "EA001" errorcode within payment response
  And error message should be "Unauthorized Operation!" within payment response

Examples:
|header|
|authorization|
|trace-id|
|api-version|
|request-date-time|


@regression 
Scenario Outline: Negative flow- New POST Payment request sent with tampered header values
  Given I am an authorized user
  And I have valid payment details
  And I create a signature for the payment request
  When I use the same signature to trigger another payment request but with different value in "<header>"
  Then I should receive a "401" error response with "Unable to verify signature" error description and "EA001" errorcode within payment response
  And error message should be "Unauthorized Operation!" within payment response

Examples:
|header|
|trace-id|
|request-date-time|
|Authorization|


@regression 
Scenario: Negative flow-New POST Payment request sent with tampered body

  Given I am an authorized user
  And I have valid payment details
  And I create a signature for the payment request
  When I use the generated signature with tampered body
  Then I should receive a "401" error response with "Unable to verify signature" error description and "EA001" errorcode within payment response
  And error message should be "Unauthorized Operation!" within payment response


@regression 
Scenario: Negative flow- Invalid signing key id used to create signature and passed in GET payment request header
  Given I am an authorized user
  And I have valid payment details
  And I make a request for the payment
  And I should receive a successful payment response
  And I have a valid payment id
  When I make a request for the payment status with invalid signing key id
  Then I should receive a "401" error response with "Unable to verify signature" error description and "EA001" errorcode within check status response
  And error message should be "Unauthorized Operation!" within check status response


@regression 
Scenario: Negative flow- Invalid signing key used to create signature and passed in GET payment request header
  Given I am an authorized user
  And I have valid payment details
  And I make a request for the payment
  And I should receive a successful payment response
  And I have a valid payment id
  When I make a request for the payment status with invalid signing key
  Then I should receive a "401" error response with "Unable to verify signature" error description and "EA001" errorcode within check status response
  And error message should be "Unauthorized Operation!" within check status response


@regression
Scenario: Negative flow- Different signing algo (HmacSHA512) used to create signature and passed in GET payment request header
  Given I am an authorized user
  And I have valid payment details
  And I make a request for the payment
  And I should receive a successful payment response
  And I have a valid payment id
  When I make a request for the payment status with a different signing algo
  Then I should receive a "401" error response with "Unable to verify signature" error description and "EA001" errorcode within check status response
  And error message should be "Unauthorized Operation!" within check status response



Scenario Outline: Negative flow- Incomplete Header Set used to create signature and passed in GET payment request
  Given I am an authorized user
  And I have valid payment details
  And I make a request for the payment
  And I should receive a successful payment response
  And I have a valid payment id
  When I make a request for the payment status with header "<header>" value missing from the signature
  Then I should receive a "401" error response with "Unable to verify signature" error description and "EA001" errorcode within check status response
  And error message should be "Unauthorized Operation!" within check status response

Examples:
|header|
|authorization|
|trace-id|
|api-version|
|request-date-time|


 @regression 
Scenario Outline: Negative flow- New GET Payment request sent with tampered header values
  Given I am an authorized user
  And I have valid payment details
  And I make a request for the payment
  And I should receive a successful payment response
  And I have a valid payment id
  And I create a signature for the payment status
  When I use the same signature to trigger another payment status request but with different value in "<header>"
  Then I should receive a "401" error response with "Unable to verify signature" error description and "EA001" errorcode within check status response
  And error message should be "Unauthorized Operation!" within check status response

Examples:
|header|
|trace-id|
|request-date-time|
|Authorization|


@regression 
Scenario: Positive flow- POST Payment Request digest is a mandatory header field
  Given I am an authorized user
  And I have valid payment details
  When I make a request for the payment without digest in the header
  Then I should receive a "401" error response with "Unable to verify signature: Missing required signature header" error description and "EA001" errorcode within payment response
  And error message should be "Unauthorized Operation!" within payment response


@regression
Scenario: Negative flow- Dragon server should throw an error if digest is used to create a signature but it is not send in the header
  Given I am an authorized user
  And I have valid payment details
  When I make a request for the payment with digest in the signature header list but not sent in the headers
  Then I should receive a "401" error response with "Unable to verify signature: Missing required signature header" error description and "EA001" errorcode within payment response
  And error message should be "Unauthorized Operation!" within payment response


