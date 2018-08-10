Feature: Payment Request - DRAG-187

Background: Retrieving access Token
Given I am a merchant
When I make a request to the Dragon ID Manager
Then I recieve an access_token


@functional @payment @trial
Scenario Outline: Positive flow- A merchant is able to create a payment request with all the valid inputs
  Given I am an authorized merchant
  And I have transaction details "<amount>","<currency>","<description>","<channel>","<invoiceid>","<merchantid>","<effectiveduration>","<returnURL>"
  When I make a request for the payment
  Then I should recieve a payment response with valid trace id in the header
  And the response body should contain valid payment id, created timestamp, transaction details, links, expiry Duration

  Examples:
 |amount|currency|description |channel  |invoiceid  |merchantid        |effectiveduration|returnURL                  |
 |10000 |HKD     |            |Mcommerce|48787589674|Pizzahut1239893993|10               |https://pizzahut.com/return|


@functional @payment
Scenario Outline: Negative flow- Invalid auth token (without Bearer in the header)
  Given I am an authorized merchant
  And I dont send Bearer with the auth token
  And I have transaction details "<amount>","<currency>","<description>","<channel>","<invoiceid>","<merchantid>","<effectiveduration>","<returnURL>"
  When I make a request for the payment
  Then I should recieve a 401 error response with "Service Request Authentication Failed" error description and "BNA001" errorcode within payment response

 Examples:
 |amount|currency|description|channel  |invoiceid  |merchantid        |effectiveduration|returnURL|
 |20.00 |HKD     |Pizza order|Ecommerce|48787589673|Pizzahut1239893993|30               |https://pizzahut.com/return|

@functional @payment
Scenario Outline: Negative flow- Invalid auth token
  Given I am a merchant with invalid auth token
  And I have transaction details "<amount>","<currency>","<description>","<channel>","<invoiceid>","<merchantid>","<effectiveduration>","<returnURL>"
  When I make a request for the payment
  Then I should recieve a 401 error response with "Service Request Authentication Failed" error description and "BNA001" errorcode within payment response

 Examples:
 |amount|currency|description|channel  |invoiceid  |merchantid        |effectiveduration|returnURL|
 |20.00 |HKD     |Pizza order|Ecommerce|48787589673|Pizzahut1239893993|30               |https://pizzahut.com/return|

@functional @payment
Scenario Outline: Negative flow- Missing auth token
  Given I am a merchant with missing auth token
  And I have transaction details "<amount>","<currency>","<description>","<channel>","<invoiceid>","<merchantid>","<effectiveduration>","<returnURL>"
  When I make a request for the payment
  Then I should recieve a 401 error response with "The request cannot be fulfilled due to bad syntax." error description and "BG2005" errorcode within payment response

  Examples:
 |amount|currency|description|channel  |invoiceid  |merchantid        |effectiveduration|returnURL|
 |20.00 |HKD     |Pizza order|Ecommerce|48787589673|Pizzahut1239893993|30               |https://pizzahut.com/return|


@functional @payment
Scenario Outline: Negative flow- Mandatory fields missing from header and body of the request
  Given I am an authorized merchant
  And I have transaction details "<amount>","<currency>","<description>","<channel>","<invoiceid>","<merchantid>","<effectiveduration>","<returnURL>"
  When I make a request for the payment
  Then I should recieve a 400 error response with "The request cannot be fulfilled due to bad syntax." error description and "BG2005" errorcode within payment response

  Examples:
 |amount|currency|description|channel       |invoiceid  |merchantid        |effectiveduration|returnURL|
 ||HKD     |Pizza order|Ecommerce|48787589673|Pizzahut1239893993|30               |https://pizzahut.com/return|
 |20.00 ||Pizza order|Ecommerce|48787589673|Pizzahut1239893993|30               |https://pizzahut.com/return|
 |20.00 |HKD     |Pizza order|Ecommerce|48787589673||30               |https://pizzahut.com/return|
 |20.00 |HKD     |Pizza order|Ecommerce|48787589673|Pizzahut1239893993|30               |https://pizzahut.com/return|
 |20.00 |HKD     |Pizza order|Ecommerce|48787589673|Pizzahut1239893993|30               ||


@functional @payment
 Scenario Outline: Negative flow- Invalid input parameters sent by the merchant
   Given I am an authorized merchant
   And I have transaction details "<amount>","<currency>","<description>","<channel>","<invoiceid>","<merchantid>","<effectiveduration>","<returnURL>"
   When I make a request for the payment
   Then I should recieve a 400 error response with "Service Request Validation Failed" error description and "BNA002" errorcode within payment response


   Examples:
  |amount|currency|description|channel       |invoiceid  |merchantid        |effectiveduration|returnURL|
  |xxx   |HKD     |Pizza order|Ecommerce|48787589673|Pizzahut1239893993|30               |https://pizzahut.com/return|
  |10   |HKD     |Trying_to_get_more_than_150_characters_for_description_uuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu|Ecommerce|48787589673|Pizzahut1239893993|30               |https://pizzahut.com/return|
  |20.00 |HKD     |Pizza order|Ecommerce|48787589673|Pizzahut1239893993|hh               |https://pizzahut.com/return|
  |66   |HKY     |Pizza order|Ecommerce|48787589673|Pizzahut1239893993|30               |https://pizzahut.com/return|
  |20.00 |HKD     |Pizza order|commerce|48787589673|Pizzahut1239893993|30               |https://pizzahut.com/return|
  |20.00 |HKD     |Pizza order|Ecommerce|48787589673|Pizzahut1239893993|30               |not_an_url|
  |-10   |HKD     |Pizza order|Ecommerce|48787589673|Pizzahut1239893993|30               |https://pizzahut.com/return|



 Scenario Outline: Negative flow- Invalid date format sent by the merchant
   Given I am an authorized merchant
   And I have transaction details "<amount>","<currency>","<description>","<channel>","<invoiceid>","<merchantid>","<effectiveduration>","<returnURL>"
   And I send request date timestamp in an invalid "<format>"
   When I make a request for the payment
   Then I should recieve a 400 error response with "Service Request Validation Failed" error description and "BNA002" errorcode within payment response

   Examples:
   |format|
   |yyyy-MM-dd HH:mm:ss.SSS'Z'|
   |yyyy-MM-dd'T'HH:mm:ss.SSS|
   |yyyy-mm-dd HH:mm|



 Scenario: Negative flow- Mandatory request date timestamp missing from the header
   Given I am an authorized merchant
   And I have transaction details "<amount>","<currency>","<description>","<channel>","<invoiceid>","<merchantid>","<effectiveduration>","<returnURL>"
   And I do not send request date timestamp in the header
   When I make a request for the payment
   Then I should recieve a 400 error response with "Service Request Validation Failed" error description and "BNA002" errorcode within payment response



 Scenario: Negative flow- Request date timestamp is greater than 5 mins from the current sys date time
   Given I am an authorized merchant
   And I have transaction details "<amount>","<currency>","<description>","<channel>","<invoiceid>","<merchantid>","<effectiveduration>","<returnURL>"
   And request date timestamp in the header is more than 5 mins behind than the current timestamp
   When I make a request for the payment
   Then I should recieve a 400 error response with "Service Request Validation Failed" error description and "BNA002" errorcode within payment response



 Scenario Outline: Same traceid sent within 5 minutes
   Given I am an authorized merchant
   And I have transaction details "<amount>","<currency>","<description>","<channel>","<invoiceid>","<merchantid>","<effectiveduration>","<returnURL>"
   When I make two payment requests with the same trace id within 5 minutes
   Then I should recieve one valid payment response
   And one invalid payment response with 400 status code
   And "Service Request Validation Failed" error description and "BNA002" errorcode within payment response

 Examples:
 |amount|currency|description |channel  |invoiceid  |merchantid        |effectiveduration|returnURL                  |
 |20.00 |HKD     |Pizza order1|Ecommerce|48787589673|Pizzahut1239893993|55               |https://pizzahut.com/return|



 Scenario Outline: Same traceid sent with a gap of 5 minutes
   Given I am an authorized merchant
   And I have transaction details "<amount>","<currency>","<description>","<channel>","<invoiceid>","<merchantid>","<effectiveduration>","<returnURL>"
   When I make two payment requests with the same trace id with a gap of 5 minutes
   Then I should recieve two valid payment responses

 Examples:
 |amount|currency|description |channel  |invoiceid  |merchantid        |effectiveduration|returnURL                  |
 |20.00 |HKD     |Pizza order1|Ecommerce|48787589673|Pizzahut1239893993|55               |https://pizzahut.com/return|



 Scenario Outline: Different traceids sent within 5 minutes
   Given I am an authorized merchant
   And I have transaction details "<amount>","<currency>","<description>","<channel>","<invoiceid>","<merchantid>","<effectiveduration>","<returnURL>"
   When I make two payment requests with the different trace ids within 5 minutes
   Then I should recieve two valid payment responses

 Examples:
 |amount|currency|description |channel  |invoiceid  |merchantid        |effectiveduration|returnURL                  |
 |20.00 |HKD     |Pizza order1|Ecommerce|48787589673|Pizzahut1239893993|55               |https://pizzahut.com/return|


@functional @payment
 Scenario Outline: Negative flow- Invalid traceid sent by the merchant
   Given I am an authorized merchant
   And I have transaction details "<amount>","<currency>","<description>","<channel>","<invoiceid>","<merchantid>","<effectiveduration>","<returnURL>"
   And I send invalid "<traceid>"
   When I make a request for the payment
   Then I should recieve a 400 error response with "Service Request Validation Failed" error description and "BNA002" errorcode within payment response

   Examples:
   |traceid|
   |random-trace-id|
   |Getmoret-han3-6cha-ract-ersfortraceid|


@functional @payment
 Scenario: Negative flow- Mandatory traceid missing from the header
   Given I am an authorized merchant
   And I have transaction details "<amount>","<currency>","<description>","<channel>","<invoiceid>","<merchantid>","<effectiveduration>","<returnURL>"
   And I do not send traceid in the header
   When I make a request for the payment
   Then I should recieve a 400 error response with "Service Request Validation Failed" error description and "BNA002" errorcode within payment response

  # Peak errors - same transaction sent twice, random merchant id
  # Manual test cases - expired JWT, DRAGON is down, peak timeout & peak server down (switch off peak mock), Restrict Caller IPs Policy, large amount