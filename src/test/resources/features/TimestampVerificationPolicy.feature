Feature: Timestamp Verification Policy - DRAG- 577

Background: Retrieving access Token
Given I am an user
When I make a request to the Dragon ID Manager
Then I recieve an access_token

@regression
Scenario: Positive flow- (System time) < (Request-Date-Time + Tolerance) within payment request
  Given I am an authorized user
  And I have valid payment details
  And request date timestamp in the payment request header is less than 5 mins than the current timestamp
  And I make a request for the payment
  And I should recieve a successful payment response

@regression 
Scenario: Positive flow- (System time) = (Request-Date-Time + Tolerance) within payment request
  Given I am an authorized user
  And I have valid payment details
  And request date timestamp in the payment request header is exactly 5 mins behind than the current timestamp
  And I make a request for the payment
  And I should recieve a successful payment response


@regression  
Scenario: Negative flow- (System time) > (Request-Date-Time + Tolerance) within payment request
  Given I am an authorized user
  And I have valid payment details
  And request date timestamp in the payment request header is more than 5 mins than the current timestamp
  And I make a request for the payment
  Then I should recieve a "400" error response with "Request timestamp too old" error description and "EA002" errorcode within payment response
  And error message should be "Service Request Validation Failed" within payment response


@regression  
Scenario: Positive flow- (System time) < (Request-Date-Time + Tolerance) within payment status request
  Given I am an authorized user
  And I have valid payment details
  And I make a request for the payment
  And I should recieve a successful payment response
  And I have a valid payment id
  And request date timestamp in the payment status header is less than 5 mins than the current timestamp
  When I make a request for the check status
  Then I should recieve a successful check status response



Scenario: Positive flow- (System time) = (Request-Date-Time + Tolerance) within payment status request
  Given I am an authorized user
  And I have valid payment details
  And I make a request for the payment
  And I should recieve a successful payment response
  And I have a valid payment id
  And request date timestamp in the payment status header is exactly 5 mins behind than the current timestamp
  When I make a request for the check status
  Then I should recieve a successful check status response


@regression  
Scenario: Negative flow- (System time) > (Request-Date-Time + Tolerance) within payment status request
  Given I am an authorized user
  And I have valid payment details
  And I make a request for the payment
  And I should recieve a successful payment response
  And I have a valid payment id
  And request date timestamp in the payment status header is more than 5 mins than the current timestamp
  When I make a request for the check status
  Then I should recieve a "400" error response with "Request timestamp too old" error description and "EA002" errorcode within check status response
  And error message should be "Service Request Validation Failed" within check status response


@regression  @skiponcimerchant @skiponsitmerchant
Scenario: Positive flow- (System time) < (Request-Date-Time + Tolerance) within refund request
  Given I am an authorized user
  And I have a valid transaction for refund
  And request date timestamp in the refund header is less than 5 mins than the current timestamp
  When I make a request for the refund
  Then I should recieve a successful refund response


Scenario: Positive flow- (System time) = (Request-Date-Time + Tolerance) within refund request
  Given I am an authorized user
  And I have a valid transaction for refund
  And request date timestamp in the refund header is exactly 5 mins behind than the current timestamp
  When I make a request for the refund
  Then I should recieve a successful refund response


@regression  @skiponcimerchant @skiponsitmerchant
Scenario: Negative flow- (System time) > (Request-Date-Time + Tolerance) within refund request
  Given I am an authorized user
  And I have a valid transaction for refund
  And request date timestamp in the refund header is more than 5 mins than the current timestamp
  When I make a request for the refund
  Then I should recieve a "400" error response with "Request timestamp too old" error description and "EA002" errorcode within refund response
  And error message should be "Service Request Validation Failed" within refund response