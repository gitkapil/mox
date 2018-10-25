# DRAGON TEST HARNESS
=======================

Closed Dragon is a set of open APIs which are basically payment services and would be used by the third party merchants and their technical team to integrate within their apps or websites.

Every env has two APIMs. One is sandbox APIM which talks to PEAK emulator and another is merchant APIM which interacts with real PEAK services.

(Although for CI env merchant APIM doesnot talk to PEAK but the emulator). Playpen only has one APIM

## How to Run
---------------

verify -P<profile> -Denv=<Env Name> -Dversion=<version number> -Dusertype=<merchant/ developer>

<i>Example: </i>

To run functional tests on playpen/dev env

command: verify -Pregression -Denv=playpen -Dversion=0.5 -Dusertype=developer

##Report
----------
Genertaed Extent Test Report can be found here:
digital-dragon-testing/target/cucumber-reports/report.html

##PreRequisites
-----------------

- Java 8
- Every env should have a config file under test/ resources
- All the values explained below should be present within the config file
- All the test cases need to be tagged with relevant profiles/ tags
- There are four cucumber tags created (3 of them are associated with respective profiles). One tag @skiponcimerchant is used to skip test cases which are not suppose to run when env is CI and usertype is merchant


## Env Values
--------------
playpen
sit
ci
pre

## Profiles (All the tests are marked as regression at the moment)
------------
functional --> to run functional tests
regression --> To run regression tests


## UserType Values
-------------------
developer --> to run the sandbox APIM in an env
merchant --> to run live/ merchant APIM in an env

Note: By default the user type is 'developer'


Explanation of Properties (config) file & its Values
-----------------------------------------------------

sandbox-server-application-id --> This is the app id of the sandbox server application in Azure AD
merchant-server-application-id --> This is the app id of the merchant server application in Azure AD

developer-client-id --> This is the app id of the sandbox client application in Azure AD. Minimum roles required: developer, paymentRequest. This client app should have access to only sandbox server app.
developer-client-secret --> This is the client secret for the above client id
merchant-client-id --> This is the app id of the merchant client application in Azure AD. Minimum roles required: merchant, paymentRequest. This client app should have access to only merchant server app.
merchant-client-secret --> This is the client secret for the above client id

client-id-access-both-servers --> This is the app id of a client application in Azure AD having access to both sandbox & merchant server app. Minimum roles required: developer/ merchant, paymentRequest.
client-secret-access-both-servers --> This is the client secret for the above client id

developer-client-id-no-paymentrequest-role --> This is the app id of the sandbox client application in Azure AD without 'paymentRequest' role. Roles required: developer. This client app should have access to only sandbox server app.
developer-client-secret-no-paymentrequest-role --> This is the client secret for the above client id
merchant-client-id-no-paymentrequest-role --> This is the app id of the merchant client application in Azure AD without 'paymentRequest' role. Roles required: merchant. This client app should have access to only merchant server app.
merchant-client-secret-no-paymentrequest-role --> This is the client secret for the above client id

jwks_uri_idp= https://login.microsoftonline.com/<Azure AD Tenant>/discovery/v2.0/keys

merchant-api-management-url --> URL of merchant API managment
sandbox-api-management-url --> URL of sandbox API management

Base_Path_APIs= /payments/
Base_Path_Token=/oauth2/

create_payment_request_resource= paymentrequests
check_payment_status_resource= paymentRequestStatus
retrieve_access_token_resource= token

openid-configuration-for-PEAK --> This is Dragon's internal token issuer. Peak uses this open id config to retrive the JWKS
                                  https://paymedev-api-<env>.open-paymentsapi.com/internal-token/.well-known/openid-configuration



