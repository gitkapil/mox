Feature: Access Token - DRAG-81


Scenario: Positive flow- A valid merchant recieves a valid access token
  Given I am a merchant
  When I make a request to the Dragon ID Manager
  Then I recieve an access_token
  And it should be a valid JWT


Scenario: Negative flow- An invalid merchant (invalid client id) should not recieve a valid access token
  Given I am a merchant
  And I have an invalid client id
  When I make a request to the Dragon ID Manager
  Then I shouldnot recieve an access_token


Scenario: Negative flow- An invalid merchant (invalid client secret) should not recieve a valid access token
  Given I am a merchant
  And I have an invalid client secret
  When I make a request to the Dragon ID Manager
  Then I shouldnot recieve an access_token


Scenario: Negative flow- Incorrect grant_type
  Given I am a merchant
  And I pass an invalid grant type
  When I make a request to the Dragon ID Manager
  Then I shouldnot recieve an access_token


Scenario: Negative flow- Incorrect application_id
  Given I am a merchant
  And I pass an invalid application id
  When I make a request to the Dragon ID Manager
  Then I shouldnot recieve an access_token

