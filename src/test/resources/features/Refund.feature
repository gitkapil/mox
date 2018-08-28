Feature: Refund - DRAG- 228

Background: Retrieving access Token
Given I am a merchant
When I make a request to the Dragon ID Manager
Then I recieve an access_token

 @refund
Scenario Outline: Positive flow- A merchant is able to create a refund request with all the valid inputs
  Given I am an authorized merchant
  And I have a "<paymentid>", "<refundamount>", "<currency>", "<reason>"
  When I make a request for the refund
  Then I should recieve a refund response with valid trace id in the header
  And the response body should contain valid refundid, paymentid, amount, currency, status, createdTime

  Examples:
 |paymentid                  | refundamount | currency |reason               |
 |FKX06908VVOPQEGXUDIX6      | 22.99        | HKD      |requested by customer|
 |FKX06908VVOPQEGXUDIX6      | 22.99        | HKD      |requested by customer|
 |FKX06908VVOPQEGXUDIX6      | 0.8          | HKD      |requested by customer|
 |FKX06908VVOPQEGXUDIX6      | 100          | HKD      |requested by customer|
 |FKX06908VVOPQEGXUDIX6      | 100          | USD      |123|
 |FKX06908VVOPQEGXUDIX6      | 100          | HKD      ||

 @refund
Scenario Outline: Negative flow- Invalid auth token
  Given I am a merchant with invalid auth token
  And I have a "<paymentid>", "<refundamount>", "<currency>", "<reason>"
  When I make a request for the refund
  Then I should recieve a 401 error response with "Service Request Authentication Failed" error description and "BNA001" errorcode within refund response

 Examples:
 |paymentid|refundamount | currency |reason               |
 |333      |22.99        | HKD      |requested by customer|

 @refund
Scenario Outline: Negative flow- Missing auth token
  Given I am a merchant with missing auth token
  And I have a "<paymentid>", "<refundamount>", "<currency>", "<reason>"
  When I make a request for the refund
  Then I should recieve a 400 error response with "The request cannot be fulfilled due to bad syntax." error description and "BG2005" errorcode within refund response

  Examples:
 |paymentid|refundamount | currency |reason               |
 |333      |22.99        | HKD      |requested by customer|

 @refund
Scenario Outline: Negative flow- Mandatory fields missing from header of the request
  Given I am an authorized merchant
  And I have a "<paymentid>", "<refundamount>", "<currency>", "<reason>"
  When I make a request for the refund
  Then I should recieve a 400 error response with "The request cannot be fulfilled due to bad syntax." error description and "BG2005" errorcode within refund response

  Examples:
 |paymentid|refundamount | currency |reason               |
 |         |22.99        | HKD      |requested by customer|
 |FKX06908VVOPQEGXUDIX6      || HKD      |requested by customer|
 |FKX06908VVOPQEGXUDIX6      | 22.99        ||requested by customer|


 @refund
 Scenario Outline: Negative flow- Invalid input parameters sent by the merchant
   Given I am an authorized merchant
   And I have a "<paymentid>", "<refundamount>", "<currency>", "<reason>"
   When I make a request for the refund
   Then I should recieve a 400 error response with "Service Request Validation Failed" error description and "BNA002" errorcode within refund response


   Examples:
  |paymentid|refundamount | currency |reason               |
  |FKX06908VVOPQEGXUDIX6      | 22.99        |HHY|requested by customer|
  |FKX06908VVOPQEGXUDIX6      | kk        |HKD|requested by customer|
  |FKX06908VVOPQEGXUDIX6      | 1000        |HHY|requested by customer|


 @refund
 Scenario Outline: Negative flow- Invalid traceid sent by the merchant
   Given I am an authorized merchant
   And I have a "<paymentid>", "<refundamount>", "<currency>", "<reason>"
   And I send invalid "<traceid>"
   When I make a request for the refund
   Then I should recieve a 400 error response with "Service Request Validation Failed" error description and "BNA002" errorcode within refund response

   Examples:
   |traceid|
   |random-trace-id|
   |Getmoret-han3-6cha-ract-ersfortraceid|


 @refund
 Scenario: Negative flow- Mandatory traceid missing from the header
   Given I am an authorized merchant
   And I have a "<paymentid>", "<refundamount>", "<currency>", "<reason>"
   And I do not send traceid in the header
   When I make a request for the refund
   Then I should recieve a 400 error response with "Service Request Validation Failed" error description and "BNA002" errorcode within refund response

  # Peak errors like (same transaction sent twice,random merchant id, peak server down, timeout between peak and dragon
