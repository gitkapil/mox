Feature: Retrieve Access Token - DRAG-310

@functional @regression
Scenario: Positive flow- A valid merchant recieves a valid access token
  Given I am an user
  When I make a request to the Dragon ID Manager
  Then I recieve an access_token
  And it should be a valid JWT
  And response should also have expiresOn, token type

@functional @regression
Scenario Outline: Negative flow- An invalid merchant (invalid client id) should not recieve a valid access token
  Given I am an user
  And I have "<invalid_value>" as client id
  When I make a request to the Dragon ID Manager
  Then I should get a "<error_code>"

Examples:
|invalid_value|error_code |
|random_client_id |401|
|                 |401|

@functional @regression
Scenario Outline: Negative flow- An invalid merchant (invalid client secret) should not recieve a valid access token
  Given I am an user
  And I have "<invalid_value>" as client secret
  When I make a request to the Dragon ID Manager
  Then I should get a "<error_code>"

 Examples:
 |invalid_value| error_code |
 |random_client_secret | 401|
 |                     | 401|

@functional @regression
Scenario Outline: Negative flow- Mandatory Fields missing from the body
  Given I am an user
  And I dont provide "<parameter>"
  When I make a request to the Dragon ID Manager
  Then I should get a "<error_code>"

  Examples:
  |parameter    |error_code |
  |clientid     |401 |
  |clientsecret |401 |
  |clientid&clientsecret |400|

@functional @regression
Scenario: Negative flow- Body sent in an invalid format
  Given I am an user
  When I make a request to the Dragon ID Manager with body in JSON format
  Then I should get a "401"


@functional @regression
Scenario Outline: Negative flow- Invalid header values sent
  Given I am an user
  And I have invalid_value for the header "<parameter>"
  When I make a request to the Dragon ID Manager
  Then I should get a "<error_code>"

 Examples:
 |parameter    | error_code |
 |Accept       | 400|
 |Content-type | 400|

#Manual Test case: Offboarded client