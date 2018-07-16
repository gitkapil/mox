Feature: Access Token

@functional @DRAG-81
Scenario: Positive flow- A valid merchant recieves a valid access token
  Given I am a valid merchant
  When I make a request to the Dragon ID Manager
  Then I recieve an access_token
  And it should be a valid JWT

@functional @DRAG-81
Scenario: Negative flow- An invalid merchant (invalid client id) should not recieve a valid access token
  Given I am a merchant with invalid merchant id
  When I make a request to the Dragon ID Manager
  Then I shouldnot recieve an access_token

@functional @DRAG-81
Scenario: Negative flow- An invalid merchant (invalid client secret) should not recieve a valid access token
  Given I am a merchant with invalid merchant secret
  When I make a request to the Dragon ID Manager
  Then I shouldnot recieve an access_token

@functional @DRAG-81
Scenario: Negative flow- Incorrect grant_type
  Given I am a valid merchant
  When I make a request to the Dragon ID Manager with an incorrect grant type
  Then I shouldnot recieve an access_token

