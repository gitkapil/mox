Feature: Trace-Id Limiting - DRAG- 627

Background: Retrieving access Token
Given I am an user
When I make a request to the Dragon ID Manager
Then I receive an access_token

@regression
Scenario: Negative flow- Same traceid sent within 5 minutes for payment Request
  Given I am an authorized user
  And I have valid payment details
  And I make a request for the payment
  And I should receive a successful payment response
  When I make a create payment request again with the same traceid
  Then I should receive a "400" error response with "Rate limit is exceeded" error description and "EA002" errorcode within payment response
  And error message should be "Service Request Validation Failed" within payment response
  #And the payment request response should be signed

@regression
Scenario: Negative flow- Same traceid sent within 5 minutes for payment Request Status
  Given I am an authorized user
  And I have valid payment details
  And I make a request for the payment
  And I should receive a successful payment response
  And I have a valid payment id
  And I make a request for the check status
  And I should receive a successful check status response
  When I make a check status request again with the same traceid
  Then I should receive a "400" error response with "Rate limit is exceeded" error description and "EA002" errorcode within check status response
  And error message should be "Service Request Validation Failed" within check status response
  And the payment status response should be signed

@functional
Scenario: Positive flow- Same traceid sent after 5 minutes for payment Request
  Given I am an authorized user
  And I have valid payment details
  And I make a request for the payment
  And I should receive a successful payment response
  When I make a create payment request again with the same traceid after 5 mins
  Then I should receive a successful payment response
  And the payment request response should be signed

@functional
Scenario: Positive flow- Same traceid sent after 5 minutes for payment Request Status
  Given I am an authorized user
  And I have valid payment details
  And I make a request for the payment
  And I should receive a successful payment response
  And I have a valid payment id
  And I make a request for the check status
  And I should receive a successful check status response
  When I make a check status request again with the same traceid after 5 mins
  Then I should receive a successful check status response
  And the payment status response should be signed

@regression
Scenario: Positive flow- Different traceid sent within 5 minutes for payment Request
  Given I am an authorized user
  And I have valid payment details
  And I make a request for the payment
  And I should receive a successful payment response
  When I make a create payment request again with a different traceid
  Then I should receive a successful payment response
  And the payment request response should be signed

@regression
Scenario: Positive flow- Different traceid sent within 5 minutes for payment Request Status
  Given I am an authorized user
  And I have valid payment details
  And I make a request for the payment
  And I should receive a successful payment response
  And I have a valid payment id
  And I make a request for the check status
  And I should receive a successful check status response
  When I make a check status request again with a different traceid
  Then I should receive a successful check status response
  And the payment status response should be signed

@regression
Scenario: Negative flow- Different traceid sent within 5 minutes for payment Request but the request date time stamp is more than 5 mins older than the current time stamp
  Given I am an authorized user
  And I have valid payment details
  And I make a request for the payment
  And I should receive a successful payment response
  When I make a create payment request again with a different traceid but request date time more than 5 mins older than the current system time
  Then I should receive a "400" error response with "Request timestamp too old" error description and "EA002" errorcode within payment response
  And error message should be "Service Request Validation Failed" within payment response
  #And the payment request response should be signed


@regression
Scenario: Negative flow- Different traceid sent within 5 minutes for payment Status but the request date time stamp is more than 5 mins older than the current time stamp
  Given I am an authorized user
  And I have valid payment details
  And I make a request for the payment
  And I should receive a successful payment response
  And I have a valid payment id
  And I make a request for the check status
  And I should receive a successful check status response
  When I make a check status request again with a different traceid but request date time more than 5 mins older than the current system time
  Then I should receive a "400" error response with "Request timestamp too old" error description and "EA002" errorcode within check status response
  And error message should be "Service Request Validation Failed" within check status response
  And the payment status response should be signed

@regression
Scenario: Negative flow- Same traceid sent within 5 minutes for payment Request but the request date time stamp is more than 5 mins older than the current time stamp
  Given I am an authorized user
  And I have valid payment details
  And I make a request for the payment
  And I should receive a successful payment response
  When I make a create payment request again with the same traceid but request date time more than 5 mins older than the current system time
  Then I should receive a "400" error response with "Request timestamp too old" error description and "EA002" errorcode within payment response
  And error message should be "Service Request Validation Failed" within payment response
  #And the payment request response should be signed

@regression 
Scenario: Negative flow- Same traceid sent within 5 minutes for payment Status but the request date time stamp is more than 5 mins older than the current time stamp
  Given I am an authorized user
  And I have valid payment details
  And I make a request for the payment
  And I should receive a successful payment response
  And I have a valid payment id
  And I make a request for the check status
  And I should receive a successful check status response
  When I make a check status request again with the same traceid but request date time more than 5 mins older than the current system time
  Then I should receive a "400" error response with "Request timestamp too old" error description and "EA002" errorcode within check status response
  And error message should be "Service Request Validation Failed" within check status response
  And the payment status response should be signed