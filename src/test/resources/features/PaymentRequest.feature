Feature: Payment Request - DRAG-187

Background: Retrieving access Token
Given I am a merchant
When I make a request to the Dragon ID Manager
Then I recieve an access_token


@functional @payment
Scenario Outline: Positive flow- A merchant is able to create a payment request with all the valid inputs
  Given I am an authorized merchant
  And I have transaction details "<traceid>", "<amount>","<currency>","<description>","<channel>","<invoiceid>","<merchantid>","<effectiveduration>","<returnURL>"
  When I make a request for the payment
  Then I should recieve a payment response with valid trace id in the header
  And the response body should contain valid payment id, created timestamp, transaction details, links

  Examples:
 |traceid                              |amount|currency|description |channel  |invoiceid  |merchantid        |effectiveduration|returnURL                  |
 |2c350a50-7ae0-4191-acc7-4420b74ed52d |20.00 |HKD     |Pizza order1|Ecommerce|48787589673|Pizzahut1239893993|55               |https://pizzahut.com/return|
 |2c350a50-7ae0-4191-acc7-4420b74ed53d |10000 |HKD     |            |Mcommerce|48787589674|Pizzahut1239893993|10               |https://pizzahut.com/return|
 |2c350a50-7ae0-4191-acc7-4420b74ed54d |89.09 |HKD     |Pizza order2|         |48787589675|Pizzahut1239893993|30               |https://pizzahut.com/return|
 |2c350a50-7ae0-4191-acc7-4420b74ed55d |0.044 |HKD     |Pizza order3|Native   |           |Pizzahut1239893993|30               |https://pizzahut.com/return|
 |2c350a50-7ae0-4191-acc7-4420b74ed56d |2.00  |HKD     |Pizza order4|Ecommerce|48787589677|Pizzahut1239893993|                 |https://pizzahut.com/return|
 |2c350a50-7ae0-4191-acc7-4420b74ed57d |3     |HKD     |Pizza order5|Mcommerce|48787589678|Pizzahut1239893993|30               |https://pizzahut.com/return|
 |2c350a50-7ae0-4191-acc7-4420b74ed58d |600.0 |HKD     |Pizza order6|Ecommerce|48787589679|Pizzahut1239893993|30               |https://pizzahut.com/return|
 |2c350a50-7ae0-4191-acc7-4420b74ed59d |12.123 |USD    |Pizza order7|Ecommerce|48787589611|Pizzahut1239893993|30               |https://pizzahut.com/return|
 |2c350a50-7ae0-4191-acc7-4420b74ed60d |12.13 |USD     |Pizza order8|Native   |ABCD       |Pizzahut1239893993|30               |https://pizzahut.com/return|


@functional @payment
Scenario Outline: Negative flow- Invalid auth token
  Given I am a merchant with invalid auth token
  And I have transaction details "<traceid>", "<amount>","<currency>","<description>","<channel>","<invoiceid>","<merchantid>","<effectiveduration>","<returnURL>"
  When I make a request for the payment
  Then I should recieve a 401 error response with "Service Request Authentication Failed" error description and "BNA001" errorcode within payment response

 Examples:
 |traceid                              |amount|currency|description|channel  |invoiceid  |merchantid        |effectiveduration|returnURL|
 |2c350a50-7ae0-4191-acc7-4420b74ed52d |20.00 |HKD     |Pizza order|Ecommerce|48787589673|Pizzahut1239893993|30               |https://pizzahut.com/return|


@functional @payment
Scenario Outline: Negative flow- Missing auth token
  Given I am a merchant with missing auth token
  And I have transaction details "<traceid>", "<amount>","<currency>","<description>","<channel>","<invoiceid>","<merchantid>","<effectiveduration>","<returnURL>"
  When I make a request for the payment
  Then I should recieve a 401 error response with "The request cannot be fulfilled due to bad syntax." error description and "BG2005" errorcode within payment response

  Examples:
 |traceid                              |amount|currency|description|channel  |invoiceid  |merchantid        |effectiveduration|returnURL|
 |2c350a50-7ae0-4191-acc7-4420b74ed52d |20.00 |HKD     |Pizza order|Ecommerce|48787589673|Pizzahut1239893993|30               |https://pizzahut.com/return|


@functional @payment
Scenario Outline: Negative flow- Mandatory fields missing from header and body of the request
  Given I am an authorized merchant
  And I have transaction details "<traceid>", "<amount>","<currency>","<description>","<channel>","<invoiceid>","<merchantid>","<effectiveduration>","<returnURL>"
  When I make a request for the payment
  Then I should recieve a 400 error response with "The request cannot be fulfilled due to bad syntax." error description and "BG2005" errorcode within payment response

  Examples:
 |traceid                              |amount|currency|description|channel       |invoiceid  |merchantid        |effectiveduration|returnURL|
 ||20.00 |HKD     |Pizza order|Ecommerce|48787589673|Pizzahut1239893993|30               |https://pizzahut.com/return|
 |2c350a50-7ae0-4191-acc7-4420b74ed52d ||HKD     |Pizza order|Ecommerce|48787589673|Pizzahut1239893993|30               |https://pizzahut.com/return|
 |2c350a50-7ae0-4191-acc7-4420b74ed52d |20.00 ||Pizza order|Ecommerce|48787589673|Pizzahut1239893993|30               |https://pizzahut.com/return|
 |2c350a50-7ae0-4191-acc7-4420b74ed52d |20.00 |HKD     |Pizza order|Ecommerce|48787589673||30               |https://pizzahut.com/return|
 |2c350a50-7ae0-4191-acc7-4420b74ed52d |20.00 |HKD     |Pizza order|Ecommerce|48787589673|Pizzahut1239893993|30               |https://pizzahut.com/return|
 |2c350a50-7ae0-4191-acc7-4420b74ed52d |20.00 |HKD     |Pizza order|Ecommerce|48787589673|Pizzahut1239893993|30               ||


@functional @payment
 Scenario Outline: Negative flow- Invalid input parameters sent by the merchant
   Given I am an authorized merchant
   And I have transaction details "<traceid>", "<amount>","<currency>","<description>","<channel>","<invoiceid>","<merchantid>","<effectiveduration>","<returnURL>"
   When I make a request for the payment
   Then I should recieve a 400 error response with "Service Request Validation Failed" error description and "BNA002" errorcode within payment response


   Examples:
  |traceid                              |amount|currency|description|channel       |invoiceid  |merchantid        |effectiveduration|returnURL|
  |2c350a50-7ae0-4191-acc7-4420b74ed52d |xxx   |HKD     |Pizza order|Ecommerce|48787589673|Pizzahut1239893993|30               |https://pizzahut.com/return|
  |2c350a50-7ae0-4191-acc7-4420b74ed52d |10   |HKD     |Trying_to_get_more_than_150_characters_for_description_uuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu|Ecommerce|48787589673|Pizzahut1239893993|30               |https://pizzahut.com/return|
  |2c350a50-7ae0-4191-acc7-4420b74ed52d |20.00 |HKD     |Pizza order|Ecommerce|48787589673|Pizzahut1239893993|hh               |https://pizzahut.com/return|
  |Get_more_than_36_characters_for_traceid |20.00 |HKD     |Pizza order|Ecommerce|48787589673|Pizzahut1239893993|30               |https://pizzahut.com/return|
  |2c350a50-7ae0-4191-acc7-4420b74ed52d |66   |HKY     |Pizza order|Ecommerce|48787589673|Pizzahut1239893993|30               |https://pizzahut.com/return|
  |2c350a50-7ae0-4191-acc7-4420b74ed52d |20.00 |HKD     |Pizza order|commerce|48787589673|Pizzahut1239893993|30               |https://pizzahut.com/return|
  |2c350a50-7ae0-4191-acc7-4420b74ed52d |20.00 |HKD     |Pizza order|Ecommerce|48787589673|Pizzahut1239893993|30               |not_an_url|
  
  
  # Peak errors like (same transaction sent twice,random merchant id, peak server down, timeout between peak and dragon