Feature: API BDD- DEMO


Scenario: Demo 1
  Given I hit "https://adb2ccidigitalpeak.b2clogin.com/adb2ccidigitalpeak.onmicrosoft.com/oauth2/v2.0/authorize?p=B2C_1A_SignUp_v1&client_id=cbf48534-459a-4652-9536-3a5955a56176&nonce=defaultNonce&scope=openid%20offline_access&response_type=code&prompt=login&response_mode=query&DC=HKG"

Scenario: Demo 2
  Given a customer account exists
  When the customer initiates the payment
  And he has enough funds
  Then the amount should be debited

