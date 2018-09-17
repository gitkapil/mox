Feature: Retrieve Access Token - DRAG-310

@regression 
Scenario: Positive flow- A valid merchant recieves a valid access token
  Given I am an user
  When I make a request to the Dragon ID Manager
  Then I recieve an access_token
  And it should be a valid JWT
  And response should also have expiresOn, token type

@regression 
Scenario Outline: Negative flow- An invalid merchant (invalid client id) should not recieve a valid access token
  Given I am an user
  And I have "<invalid_value>" as client id
  When I make a request to the Dragon ID Manager
  Then I should recieve a "400" error response with "<error_description>" error description and "EA002" errorcode within token response
  And error message should be "Service Request Validation Failed" within token response

Examples:
|invalid_value    |error_description|
|random_client_id |AADSTS70001|
|                 |AADSTS90014: The request body must contain the following parameter: 'client_id'.|

@regression 
Scenario Outline: Negative flow- An invalid merchant (invalid client secret) should not recieve a valid access token
  Given I am an user
  And I have "<invalid_value>" as client secret
  When I make a request to the Dragon ID Manager
  Then I should recieve a "401" error response with "<error_description>" error description and "EA001" errorcode within token response
  And error message should be "Service Request Authentication Failed" within token response

 Examples:
 |invalid_value       |error_description|
 |random_client_secret|AADSTS70002: Error validating credentials. AADSTS50012: Invalid client secret is provided.|
 |                    |AADSTS70002: 'client_assertion', 'client_secret' or 'request' is required for the 'client_credentials' grant type.|

@regression 
Scenario Outline: Negative flow- Mandatory Fields missing from the body
  Given I am an user
  And I dont provide "<parameter>"
  When I make a request to the Dragon ID Manager
  Then I should recieve a "<error_response>" error response with "<error_description>" error description and "<error_code>" errorcode within token response
  And error message should be "<error_message>" within token response

  Examples:
  |parameter             |error_response|error_code|error_message                         |error_description|
  |clientid              |400           |EA002     |Service Request Validation Failed     |AADSTS90014: The request body must contain the following parameter: 'client_id'.|
  |clientsecret          |401           |EA001     |Service Request Authentication Failed |AADSTS70002: 'client_assertion', 'client_secret' or 'request' is required for the 'client_credentials' grant type.|
  |clientid&clientsecret |400           |EA002     |Service Request Validation Failed     |Expression evaluation failed. Object reference not set to an instance of an object.|



Scenario Outline: Negative flow- Mandatory Fields missing from the body
  Given I am an user
  And I dont provide "<parameter>"
  When I make a request to the Dragon ID Manager
  And error message should be "Resource not found" within token response

  Examples:
  |parameter             |
  |Api-Version|
  
@regression 
Scenario: Negative flow- Body sent in an invalid format
  Given I am an user
  When I make a request to the Dragon ID Manager with body in JSON format
  Then I should recieve a "401" error response with "AADSTS70002: 'client_assertion', 'client_secret' or 'request' is required for the 'client_credentials' grant type." error description and "EA001" errorcode within token response
  And error message should be "Service Request Authentication Failed" within token response


@regression 
Scenario Outline: Negative flow- Invalid header values sent
  Given I am an user
  And I have invalid_value for the header "<parameter>"
  When I make a request to the Dragon ID Manager
  Then I should recieve a "400" error response with "<error_description>" error description and "EA002" errorcode within token response
  And error message should be "Service Request Validation Failed" within token response

 Examples:
 |parameter    |error_description|
 |Accept       |Header Accept does not contain required value. Access denied.|
 |Content-type |Header Content-Type does not contain required value. Access denied.|


