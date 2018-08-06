Feature: Refund - DRAG- 228

Background: Retrieving access Token
Given I am a merchant
When I make a request to the Dragon ID Manager
Then I recieve an access_token


Scenario Outline: Positive flow- A merchant is able to create a refund request with all the valid inputs
  Given I am an authorized merchant
  And I have a "<traceid>", "<paymentid>", "<amount>", "<currency>"
  When I make a request for the refund
  Then I should recieve a refund response with valid trace id in the header
  And the response body should contain valid refundid, paymentid, amount, currency, status, createdTime

  Examples:
 |traceid                              |paymentid| amount | currency |
 |2c350a50-7ae0-4191-acc7-4420b74ed52d |333      | 22.99  | HKD      |
 |2c350a50-7ae0-4191-acc7-4420b74ed52d |xyz      | 22.99  | HKD      |
 |2c350a50-7ae0-4191-acc7-4420b74ed52d |333      | 0.8    | HKD      |
 |2c350a50-7ae0-4191-acc7-4420b74ed52d |333      | 1000   | HKD      |
 |2c350a50-7ae0-4191-acc7-4420b74ed52d |333      | 1000   | USD      |


Scenario Outline: Negative flow- Invalid auth token
  Given I am a merchant with invalid auth token
  And I have a "<traceid>", "<paymentid>", "<amount>", "<currency>"
  When I make a request for the refund
  Then I should recieve a 401 error response with "Service Request Authentication Failed" error description and "BNA001" errorcode within refund response

 Examples:
 |traceid                              |paymentid|
 |2c350a50-7ae0-4191-acc7-4420b74ed52d |333      |


Scenario Outline: Negative flow- Missing auth token
  Given I am a merchant with missing auth token
  And I have a "<traceid>", "<paymentid>", "<amount>", "<currency>"
  When I make a request for the refund
  Then I should recieve a 400 error response with "The request cannot be fulfilled due to bad syntax." error description and "BG2005" errorcode within refund response

  Examples:
 |traceid                              |paymentid|
 |2c350a50-7ae0-4191-acc7-4420b74ed52d |333      |
 |2c350a50-7ae0-4191-acc7-4420b74ed52d |xyz      |
 |2c350a50-7ae0-4191-acc7-4420b74ed52d |1.1      |

Scenario Outline: Negative flow- Mandatory fields missing from header of the request
  Given I am an authorized merchant
  And I have a "<traceid>", "<paymentid>", "<amount>", "<currency>"
  When I make a request for the refund
  Then I should recieve a 400 error response with "The request cannot be fulfilled due to bad syntax." error description and "BG2005" errorcode within refund response

  Examples:
 |traceid                              |paymentid|
 |2c350a50-7ae0-4191-acc7-4420b74ed52d |         |
 | |333      |


 Scenario Outline: Negative flow- Invalid input parameters sent by the merchant
   Given I am an authorized merchant
   And I have a "<traceid>", "<paymentid>", "<amount>", "<currency>"
   When I make a request for the refund
   Then I should recieve a 400 error response with "Service Request Validation Failed" error description and "BNA002" errorcode within refund response


   Examples:
  |traceid                              |paymentid|
  |2Get_more_than_36_characters_for_traceid  |123 |


  # Peak errors like (same transaction sent twice,random merchant id, peak server down, timeout between peak and dragon
