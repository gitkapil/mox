Feature: Check Status - DRAG- 213

Background: Retrieving access Token
Given I am a merchant
When I make a request to the Dragon ID Manager
Then I recieve an access_token

 @checkstatus
Scenario Outline: Positive flow- A merchant is able to create a check status request with all the valid inputs
  Given I am an authorized merchant
  And I have a "<paymentid>"
  When I make a request for the check status
  Then I should recieve a check status response with valid trace id in the header
  And the response body should contain valid paymentid, status

  Examples:
 |paymentid|
 |333      |


 @checkstatus
Scenario Outline: Negative flow- Invalid auth token
  Given I am a merchant with invalid auth token
  And I have a "<paymentid>"
  When I make a request for the check status
  Then I should recieve a 401 error response with "Service Request Authentication Failed" error description and "BNA001" errorcode within check status response

 Examples:
 |paymentid|
 |333      |


 @checkstatus
Scenario Outline: Negative flow- Missing auth token
  Given I am a merchant with missing auth token
  And I have a "<paymentid>"
  When I make a request for the check status
  Then I should recieve a 400 error response with "The request cannot be fulfilled due to bad syntax." error description and "BG2005" errorcode within check status response

  Examples:
 |paymentid|
 |333      |
 |xyz      |
 |1.1      |


 @checkstatus
Scenario Outline: Negative flow- Mandatory fields missing from header of the request
  Given I am an authorized merchant
  And I have a "<paymentid>"
  When I make a request for the check status
  Then I should recieve a 400 error response with "The request cannot be fulfilled due to bad syntax." error description and "BG2005" errorcode within check status response

  Examples:
 |paymentid|
 |         |


  @checkstatus
 Scenario Outline: Negative flow- Invalid input parameters sent by the merchant
   Given I am an authorized merchant
   And I have a "<paymentid>"
   When I make a request for the check status
   Then I should recieve a 400 error response with "Service Request Validation Failed" error description and "BNA002" errorcode within check status response


   Examples:
  |paymentid|
  |123 |


 @checkstatus
 Scenario Outline: Negative flow- Invalid traceid sent by the merchant
   Given I am an authorized merchant
   And I have a "<paymentid>"
   And I send invalid "<traceid>"
   When I make a request for the check status
   Then I should recieve a 400 error response with "Service Request Validation Failed" error description and "BNA002" errorcode within check status response

   Examples:
   |traceid|
   |random-trace-id|
   |Getmoret-han3-6cha-ract-ersfortraceid|


 @checkstatus
 Scenario: Negative flow- Mandatory traceid missing from the header
   Given I am an authorized merchant
   And I have a "<paymentid>"
   And I do not send traceid in the header
   When I make a request for the check status
   Then I should recieve a 400 error response with "Service Request Validation Failed" error description and "BNA002" errorcode within check status response


  # Peak errors like (same transaction sent twice,random merchant id, peak server down, timeout between peak and dragon
