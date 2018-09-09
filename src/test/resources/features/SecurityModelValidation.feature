Feature: Security Model Validation - DRAG- 362

Scenario: Validate that a developer hitting sandbox APIM gets 'developer' role and 'sandbox server app id' as aud within claimset in token (sandbox client app has permission only to access sandbox server app)
  Given I am a developer
  When I make a request to the sandbox Dragon ID Manager
  Then I recieve an access_token
  And it should be a valid JWT
  And the role should be "developer"
  And the aud should be sandbox server app id


Scenario: Validate that a merchant hitting merchant APIM gets 'merchant' role and 'merchant server app id' as aud within claimset in token (merchant client app has permission only to access merchant server app)
  Given I am a merchant
  When I make a request to the merchant Dragon ID Manager
  Then I recieve an access_token
  And it should be a valid JWT
  And the role should be "merchant"
  And the aud should be merchant server app id


Scenario: Validate that a merchant hitting sandbox APIM gets no role and 'sandbox server app id' as aud within claimset in token (merchant client app doesnot have permission to access sandbox server app)
  Given I am a merchant
  When I make a request to the sandbox Dragon ID Manager
  Then I recieve an access_token
  And it should be a valid JWT
  And there should be no role
  And the aud should be sandbox server app id


Scenario: Validate that a developer hitting merchant APIM gets no role and 'merchant server app id' as aud within claimset in token (sandbox client app doesnot have permission to access merchant server app)
  Given I am a developer
  When I make a request to the merchant Dragon ID Manager
  Then I recieve an access_token
  And it should be a valid JWT
  And there should be no role
  And the aud should be merchant server app id


Scenario: Validate that a developer with valid token and paymentRequest as role is able to access sandbox payment request and check status APIs (sandbox client app has permission to access sandbox server app)
  Given I am a developer
  And I make a request to the sandbox Dragon ID Manager
  And I recieve an access_token
  And it should be a valid JWT
  And the role should be "developer"
  And the role should have paymentrequest
  When I hit sandbox APIs
  And I have valid payment details
  And I make a request for the payment
  And I should recieve a successful payment response
  And I have a valid payment id
  And I make a request for the check status
  Then I should recieve a successful check status response


Scenario: Validate that a merchant with valid token and paymentRequest as role is able to access merchant payment request and check status APIs(merchant client app has permission to access merchant server app)
  Given I am a merchant
  And I make a request to the merchant Dragon ID Manager
  And I recieve an access_token
  And it should be a valid JWT
  And the role should be "merchant"
  And the role should have paymentrequest
  When I hit merchant APIs
  And I have valid payment details
  And I make a request for the payment
  And I should recieve a successful payment response
  And I have a valid payment id
  And I make a request for the check status
  Then I should recieve a successful check status response


Scenario: Validate that a developer with valid token and no paymentRequest as role is not able to access sandbox payment request and check status APIs (sandbox client app has permission to access sandbox server app)
  Given I am a developer
  And I make a request to the sandbox Dragon ID Manager
  And I recieve an access_token
  And it should be a valid JWT
  And the role should be "developer"
  And the role should not have paymentrequest
  When I hit sandbox APIs
  And I have valid payment details
  And I make a request for the payment
  And I should recieve a "400" error response with "<error_description>" error description and "<error_code>" errorcode within payment response
  And error message should be "<error_message>" within payment response
  And I have a payment id "b15e090a-5e97-4b44-a67e-542eb2aa0f4d"
  And I make a request for the check status
  Then I should recieve a "401" error response with "<error_description>" error description and "<error_code>" errorcode within check status response
  And error message should be "<error_message>" within check status response



Scenario: Validate that a merchant with valid token and no paymentRequest as role is not able to access merchant payment request and check status APIs (merchant client app has permission to access merchant server app)
  Given I am a merchant
  And I make a request to the merchant Dragon ID Manager
  And I recieve an access_token
  And it should be a valid JWT
  And the role should be "merchant"
  And the role should not have paymentrequest
  When I hit merchant APIs
  And I have valid payment details
  And I make a request for the payment
  And I should recieve a "400" error response with "<error_description>" error description and "<error_code>" errorcode within payment response
  And error message should be "<error_message>" within payment response
  And I have a payment id "b15e090a-5e97-4b44-a67e-542eb2aa0f4d"
  And I make a request for the check status
  Then I should recieve a "401" error response with "<error_description>" error description and "<error_code>" errorcode within check status response
  And error message should be "<error_message>" within check status response



Scenario: Validate that a developer with valid token and paymentRequest as role is not able to access merchant payment request and check status APIs (sandbox client app doesnot have permission to access merchant server app)
  Given I am a developer
  And I make a request to the sandbox Dragon ID Manager
  And I recieve an access_token
  And it should be a valid JWT
  And the role should be "developer"
  And the role should have paymentrequest
  When I hit merchant APIs
  And I have valid payment details
  And I make a request for the payment
  And I should recieve a "400" error response with "<error_description>" error description and "<error_code>" errorcode within payment response
  And error message should be "<error_message>" within payment response
  And I have a payment id "b15e090a-5e97-4b44-a67e-542eb2aa0f4d"
  And I make a request for the check status
  Then I should recieve a "401" error response with "<error_description>" error description and "<error_code>" errorcode within check status response
  And error message should be "<error_message>" within check status response


Scenario: Validate that a merchant with valid token and paymentRequest as role is not able to access sandbox payment request and check status APIs (merchant client app doesnot have permission to access sandbox server app)
  Given I am a merchant
  And I make a request to the merchant Dragon ID Manager
  And I recieve an access_token
  And it should be a valid JWT
  And the role should be "merchant"
  And the role should have paymentrequest
  When I hit sandbox APIs
  And I have valid payment details
  And I make a request for the payment
  And I should recieve a "400" error response with "<error_description>" error description and "<error_code>" errorcode within payment response
  And error message should be "<error_message>" within payment response
  And I have a payment id "b15e090a-5e97-4b44-a67e-542eb2aa0f4d"
  And I make a request for the check status
  Then I should recieve a "401" error response with "<error_description>" error description and "<error_code>" errorcode within check status response
  And error message should be "<error_message>" within check status response


Scenario Outline: Validate that a user with (developer) token and paymentRequest as role is not able to access merchant payment request and check status APIs (merchant client app has access to both the server apps)
  Given I am a user with "<client_id>" and "<client_secret>"
  And I make a request to the sandbox Dragon ID Manager
  And I recieve an access_token
  And it should be a valid JWT
  And the role should be "developer"
  And the role should have paymentrequest
  When I hit merchant APIs
  And I have valid payment details
  And I make a request for the payment
  And I should recieve a "400" error response with "<error_description>" error description and "<error_code>" errorcode within payment response
  And error message should be "<error_message>" within payment response
  And I have a payment id "b15e090a-5e97-4b44-a67e-542eb2aa0f4d"
  And I make a request for the check status
  Then I should recieve a "401" error response with "<error_description>" error description and "<error_code>" errorcode within check status response
  And error message should be "<error_message>" within check status response

Examples:
   |client_id|client_secret|
   |||


Scenario Outline: Validate that a user with (merchant) token and paymentRequest as role is not able to access sandbox payment request and check status APIs (sandbox client app has access to both the server apps)
  Given I am a user with "<client_id>" and "<client_secret>"
  And I make a request to the merchant Dragon ID Manager
  And I recieve an access_token
  And it should be a valid JWT
  And the role should be "merchant"
  And the role should have paymentrequest
  When I hit sandbox APIs
  And I have valid payment details
  And I make a request for the payment
  And I should recieve a "400" error response with "<error_description>" error description and "<error_code>" errorcode within payment response
  And error message should be "<error_message>" within payment response
  And I have a payment id "b15e090a-5e97-4b44-a67e-542eb2aa0f4d"
  And I make a request for the check status
  Then I should recieve a "401" error response with "<error_description>" error description and "<error_code>" errorcode within check status response
  And error message should be "<error_message>" within check status response

Examples:
   |client_id|client_secret|
   |||

Scenario Outline: Validate that a user with (merchant) token and paymentRequest as role is able to access merchant payment request and check status APIs (merchant client app has access to both the server apps)
  Given I am a user with "<client_id>" and "<client_secret>"
  And I make a request to the merchant Dragon ID Manager
  And I recieve an access_token
  And it should be a valid JWT
  And the role should be "developer"
  And the role should have paymentrequest
  When I hit merchant APIs
  And I have valid payment details
  And I make a request for the payment
  And I should recieve a successful payment response
  And I have a valid payment id
  And I make a request for the check status
  Then I should recieve a successful check status response

Examples:
   |client_id|client_secret|
   |||

@trial
Scenario Outline: Validate that a user with (developer) token and paymentRequest as role is able to access sandbox payment request and check status APIs (sandbox client app has access to both the server apps)
  Given I am a user with "<client_id>" and "<client_secret>"
  And I make a request to the sandbox Dragon ID Manager
  And I recieve an access_token
  And it should be a valid JWT
  And the role should be "merchant"
  And the role should have paymentrequest
  When I hit sandbox APIs
  And I have valid payment details
  And I make a request for the payment
  And I should recieve a successful payment response
  And I have a valid payment id
  And I make a request for the check status
  Then I should recieve a successful check status response

Examples:
   |client_id|client_secret|
   |||


