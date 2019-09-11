Feature: Open Id Config & JWKS URI - DRAG- 369 & DRAG- 370

#@regression
Scenario: Positive flow- OpenId Config URI is UP and returning valid values
  When I hit the openid config URI
  Then I receive a successful response
  And the response body should have valid values

#@regression
Scenario: Positive flow-  JWKS.json URI is UP and returning valid values
  When I hit the openid config URI
  And I receive a successful response
  And I obtain the JWKS URI
  When I hit the JWKS URI
  Then I should receive a successful response from JWKS URI Request
  And the response body should have valid keys values

