Feature: Retrieve Access Token - DRAG-310

@functional 
Scenario: Positive flow- A valid merchant recieves a valid access token
  Given I am an user
  When I make a request to the Dragon ID Manager
  Then I recieve an access_token
  And it should be a valid JWT
  And response should also have expiresOn, token type

@functional 
Scenario Outline: Negative flow- An invalid merchant (invalid client id) should not recieve a valid access token
  Given I am an user
  And I have "<invalid_value>" as client id
  When I make a request to the Dragon ID Manager
  Then I should recieve a "401" error response with "<error_description>" error description and "EA001" errorcode within token response
  And error message should be "Service Request Authentication Failed" within token response

Examples:
|invalid_value    |
|random_client_id |
|                 |

@functional 
Scenario Outline: Negative flow- An invalid merchant (invalid client secret) should not recieve a valid access token
  Given I am an user
  And I have "<invalid_value>" as client secret
  When I make a request to the Dragon ID Manager
  Then I should recieve a "401" error response with "<error_description>" error description and "EA001" errorcode within token response
  And error message should be "Service Request Authentication Failed" within token response

 Examples:
 |invalid_value       |
 |random_client_secret|
 |                    |

@functional 
Scenario Outline: Negative flow- Mandatory Fields missing from the body
  Given I am an user
  And I dont provide "<parameter>"
  When I make a request to the Dragon ID Manager
  Then I should recieve a "400" error response with "<error_description>" error description and "EA002" errorcode within token response
  And error message should be "Service Request Authentication Failed" within token response

  Examples:
  |parameter    |
  |clientid     |
  |clientsecret |
  |clientid&clientsecret |

@functional 
Scenario: Negative flow- Body sent in an invalid format
  Given I am an user
  When I make a request to the Dragon ID Manager with body in JSON format
  Then I should recieve a "400" error response with "<error_description>" error description and "EA002" errorcode within token response
  And error message should be "Service Request Authentication Failed" within token response


@functional 
Scenario Outline: Negative flow- Invalid header values sent
  Given I am an user
  And I have invalid_value for the header "<parameter>"
  When I make a request to the Dragon ID Manager
  Then I should recieve a "400" error response with "<error_description>" error description and "EA002" errorcode within token response
  And error message should be "Service Request Authentication Failed" within token response

 Examples:
 |parameter    |
 |Accept       |
 |Content-type |

