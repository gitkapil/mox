Feature: GET Credentials - DRAG-2177

  As a user
  I want to create up to credentials for merchant and validate correct response is returned

  Background: Retrieving access Token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

  #defect - timestamp of GET credentials != POST onboarding DRAG-2432
  #@trial @regression
  Scenario: SC-1 Positive flow - Fetch created credential details - single credential after POST onboarding
    Given I am logging in as a user with correct privileges
    When I onboard new merchant by POST onboarding API
    Given I am an authorized DRAGON user
    When I hit get credentials endpoint with applicationId from onboarding response
    Then I should receive successful get credential response
    And validate GET credentials response with onboarding response


  #defect - timestamp of GET credentials != POST credentials DRAG-2433 ;sandbox
  #@trial @regression
  Scenario Outline: SC-2-3 Positive flow - Fetch created credential details - after POST onboarding and POST credentials
    Given I am an authorized to create credentials as DRAGON user
    When I hit the post credentials endpoint with credential name "<credentialName>"
    Given I am an authorized DRAGON user
    When I hit get credentials endpoint without any filter
    Then I should receive successful get credential response
    And validate GET credentials response
    Examples:
      | credentialName |
      | validName      |
      | UUID           |


  #@trial @regression
  Scenario Outline: SC-4 Positive flow - Fetch created credential details - multiple credentials after POST onboarding, POST credentials, PUT credentials
    Given I am an authorized to create credentials as DRAGON user
    When I hit the post credentials endpoint five times with credential name "<credentialName>"
    Given I am an authorized to put credentials as DRAGON user
    When I hit the put credentials endpoint with new credential name "<credentialName>" and status "D"
    Given I am an authorized DRAGON user
    When I hit get credentials endpoint without any filter
    Then I should receive successful get credential response
    And validate GET credentials response with PUT credentials
    Examples:
      | credentialName |
      | validName      |


  #@trial @regression
  Scenario Outline: SC-5 Positive flow - Fetch credential details with non existing applicationId
    Given I am an authorized DRAGON user
    When I hit get credentials endpoint with applicationId "<applicationId>"
    Then I should receive successful get credential response
    And validate GET credentials response returns empty list
    Examples:
      | applicationId                        |
      | 9ab4462b-1f25-43ea-9740-0c069de8715a |


  #@trial @regression
  Scenario Outline: SC-6 Positive flow - Fetch created credential details with filter - status
    Given I am an authorized to create credentials as DRAGON user
    When I hit the post credentials endpoint five times with credential name "<credentialName>"
    Given I am an authorized to put credentials as DRAGON user
    When I hit the put credentials endpoint with new credential name "<credentialName>" and status "D"
    Given I am an authorized DRAGON user
    When I hit get credentials endpoint with filter status as "A"
    Then I should receive successful get credential response
    And validate GET credentials response returns with filter status
    When I hit get credentials endpoint with filter status as "D"
    And validate GET credentials response returns with filter status
    Examples:
      | credentialName |
      | validName      |


  #@trial @regression
  Scenario Outline: SC-7-8 Positive flow - Fetch created credential details with filter - status
    Given I am an authorized to create credentials as DRAGON user
    When I hit the post credentials endpoint five times with credential name "<credentialName>"
    Given I am an authorized DRAGON user
    When I hit get credentials endpoint with filter status as "<status>"
    Then I should receive successful get credential response
    And validate GET credentials response returns with filter status
    Examples:
      | credentialName | status |
      | validName      | E      |
      | validName      | a      |

#BUG raised DRAG-2406 for credentialName = Hide & Seek
  #@trial @regression
  Scenario Outline: SC-9-10 - Positive flow - Fetch created credential details with filter - credentialName
    Given I am an authorized to create credentials as DRAGON user
    When I hit the post credentials endpoint with credential name "<credentialName>"
    Given I am an authorized DRAGON user
    When I hit get credentials endpoint with filter credentialName as provided in POST credentials API request
    Then I should receive successful get credential response
    And validate GET credentials response
    Examples:
      | credentialName |
      | validName      |
      #scenario to test with special characters
     # | Hide & Seek    |
      | 测试             |
      | Nestlé         |


  #@trial @regression
  Scenario Outline: SC-11 Positive flow - Fetch created credential details with filter - credentialId
    Given I am an authorized to create credentials as DRAGON user
    When I hit the post credentials endpoint with credential name "<credentialName>"
    Given I am an authorized DRAGON user
    When I hit get credentials endpoint with filter credentialId retrieved in POST credentials API response
    Then I should receive successful get credential response
    And validate GET credentials response
    Examples:
      | credentialName |
      | validName      |

  #@trial @regression
  Scenario Outline: SC-12-14 Positive flow - Fetch created credential details with filter - limit
    Given I am an authorized to create credentials as DRAGON user
    When I hit the post credentials endpoint five times with credential name "<credentialName>"
    Given I am an authorized DRAGON user
    When I query for a list of credentials with filter limit as "<limit>"
    Then I should receive successful get credential response
    And I should receive "<actual>" number of credentials against limit "<limit>"
    And validate GET credentials response
    Examples:
      | credentialName | limit | actual |
      | validName      | 0     | 1      |
      | validName      | 31    | 30     |
      | validName      | -11   | 1      |

  #@trial @regression
  Scenario Outline: SC-15-17 Positive flow - Fetch created credential details with filter - page
    Given I am an authorized to create credentials as DRAGON user
    When I hit the post credentials endpoint with credential name "<credentialName>"
    Given I am an authorized DRAGON user
    When I query for a list of credentials with filter page as "<page>"
    Then I should receive successful get credential response
    And validate GET credentials response
    Examples:
      | credentialName | page |
      | validName      | 0    |
      | validName      | 8    |
      | validName      | -1   |


  #@trial @regression
  Scenario Outline: SC-18-19 Positive flow - Fetch created credential details with filter - sortDirection
    Given I am an authorized to create credentials as DRAGON user
    When I hit the post credentials endpoint with credential name "<credentialName>"
    Given I am an authorized DRAGON user
    When I query for a list of credentials with filter sortDirection as "<sortDirection>"
    Then I should receive successful get credential response
    And validate GET credentials response in "<sortDirection>"
    Examples:
      | credentialName | sortDirection |
      | validName      | DESC          |
      | validName      | ASC           |

  #@trial @regression
  Scenario Outline: SC-20-22 Positive flow - Fetch created credential details with filter - sortBy
    Given I am an authorized to create credentials as DRAGON user
    When I hit the post credentials endpoint with credential name "<credentialName>"
    Given I am an authorized DRAGON user
    When I query for a list of credentials with filter sortBy as "<sortBy>"
    Then I should receive successful get credential response
    And validate GET credentials response by filter "<sortBy>"
    Examples:
      | credentialName | sortBy            |
      | validName      | CREATION_DATE     |
      | validName      | LAST_UPDATED_DATE |
      | validName      | EXPIRY_DATE       |
      | validName      |                   |
      | validName      | space             |


  #@trial @regression
  Scenario Outline: SC-23-24 Positive flow - Fetch empty list of credential details with non existing filter query parameters - credentialName, credentialId
    Given I am an authorized to create credentials as DRAGON user
    When I hit the post credentials endpoint with credential name "<credentialName>"
    Given I am an authorized DRAGON user
    When I hit get credentials endpoint with filter "<filterName>" as "<value>" which doesn't exist
    Then I should receive successful get credential response
    And validate GET credentials response returns empty list
    Examples:
      | credentialName | filterName     | value                                |
      | validName      | credentialName | someRandomValue                      |
      | validName      | credentialId   | 6ff05d6c-408f-4fe2-b81b-c181d60a821a |


  #@trial @regression
  Scenario Outline: SC-25-28 Positive flow - Fetch total credential details with null and space filter query parameters - credentialName, credentialId
    Given I am an authorized to create credentials as DRAGON user
    When I hit the post credentials endpoint with credential name "<credentialName>"
    Given I am an authorized DRAGON user
    When I hit get credentials endpoint with filter "<filterName>" as "<value>" which doesn't exist
    Then I should receive successful get credential response
    And validate GET credentials response

    Examples:
      | credentialName | filterName     | value |
      | validName      | credentialName |       |
      | validName      | credentialName | space |
      | validName      | credentialId   |       |
      | validName      | credentialId   | space |


  #@trial @regression
  Scenario Outline: SC-29 Positive flow - Fetch created credential details with multiple filters - status, credentialId
    Given I am an authorized to create credentials as DRAGON user
    When I hit the post credentials endpoint with credential name "<credentialName>"
    Given I am an authorized DRAGON user
    When I query for a list of credentials with filter "<status>" and "<filter>"
    Then I should receive successful get credential response
    And validate GET credentials response by multiple filter "<status>", "<filter>"
    Examples:
      | credentialName | status | filter       |
      | validName      | A      | credentialId |


  #@trial @regression
  Scenario Outline: SC-30 Positive flow - Fetch created credential details with multiple filters - status, credentialName
    Given I am an authorized to create credentials as DRAGON user
    When I hit the post credentials endpoint with credential name "<credentialName>"
    Given I am an authorized DRAGON user
    When I query for a list of credentials with filter "<status>" and "<filter>"
    Then I should receive successful get credential response
    And validate GET credentials response by multiple filter "<status>", "<filter>"
    Examples:
      | credentialName | status | filter         |
      | validName      | A      | credentialName |


  #@trial @regression
  Scenario Outline: SC-29 Positive flow - Fetch empty list for multiple filters - status, credentialId/credentialName when no records are present
    Given I am an authorized to create credentials as DRAGON user
    When I hit the post credentials endpoint with credential name "<credentialName>"
    Given I am an authorized DRAGON user
    When I query for a list of credentials with filter "<status>" and "<filter>"
    Then I should receive successful get credential response
    And validate GET credentials response returns empty list
    Examples:
      | credentialName | status | filter         |
      | validName      | D      | credentialId   |
      | validName      | E      | credentialName |


  #@trial @regression
  Scenario Outline: SC-30 Positive flow - Fetch created credential details with multiple filters - status, limit
    Given I am an authorized to create credentials as DRAGON user
    When I hit the post credentials endpoint with credential name "<credentialName>"
    Given I am an authorized DRAGON user
    When I query for a list of credentials with filter status "<status>" and limit "<limit>"
    Then I should receive successful get credential response
    And validate GET credentials response by multiple filter status "<status>" and limit "<limit>"

    Examples:
      | credentialName | status | limit |
      | validName      | A      | 1     |
      | validName      | D      | 0     |
      | validName      | E      | 15    |


  #@trial @regression
  Scenario Outline: SC-30 Positive flow - Fetch created credential details with multiple filters - status, page
    Given I am an authorized to create credentials as DRAGON user
    When I hit the post credentials endpoint with credential name "<credentialName>"
    Given I am an authorized DRAGON user
    When I query for a list of credentials with filter status "<status>" and page "<page>"
    Then I should receive successful get credential response
    And validate GET credentials response by multiple filter status "<status>" and page "<page>"

    Examples:
      | credentialName | status | page |
      | validName      | A      | 1    |
      | validName      | D      | 0    |
      | validName      | E      | 15   |


  #@trial @regression
  Scenario Outline: SC-30 Positive flow - Fetch created credential details with multiple filters - status, sortDirection
    Given I am an authorized to create credentials as DRAGON user
    When I hit the post credentials endpoint with credential name "<credentialName>"
    Given I am an authorized DRAGON user
    When I query for a list of credentials with filter status "<status>" and sortDirection "<sortDirection>"
    Then I should receive successful get credential response
    And validate GET credentials response by multiple filter status "<status>" and sortDirection "<sortDirection>"

    Examples:
      | credentialName | status | sortDirection |
      | validName      | A      | ASC           |
      | validName      | A      | DESC          |
      | validName      | D      | DESC          |


  #@trial @regression
  Scenario Outline: SC-30 Positive flow - Fetch created credential details with multiple filters - status, sortBy
    Given I am an authorized to create credentials as DRAGON user
    When I hit the post credentials endpoint five times with credential name "<credentialName>"
    Given I am an authorized to put credentials as DRAGON user
    When I hit the put credentials endpoint with new credential name "<credentialName>" and status "D"
    Given I am an authorized DRAGON user
    When I query for a list of credentials with filter status "<status>" and sortBy "<sortBy>"
    Then I should receive successful get credential response
    And validate GET credentials response by multiple filter status "<status>" and sortBy "<sortBy>"

    Examples:
      | credentialName | status | sortBy            |
      | validName      | A      | EXPIRY_DATE       |
      | validName      | D      | LAST_UPDATED_DATE |
      | validName      | A      | CREATION_DATE     |


  #@trial @regression
  Scenario Outline: SC-30 Positive flow - Fetch created credential details with multiple filters - status, sortBy, sortDirection
    Given I am an authorized to create credentials as DRAGON user
    When I hit the post credentials endpoint five times with credential name "<credentialName>"
    Given I am an authorized to put credentials as DRAGON user
    When I hit the put credentials endpoint with new credential name "<credentialName>" and status "D"
    Given I am an authorized to create credentials as DRAGON user
    When I hit the post credentials endpoint with new credentialName "<credentialName>"
    Given I am an authorized to put credentials as DRAGON user
    When I hit the put credentials endpoint with new credential name "<credentialName>" and status "D"
    Given I am an authorized DRAGON user
    When I query for a list of credentials with filter status "<status>", sortBy "<sortBy>", sortDirection "<sortDirection>"
    Then I should receive successful get credential response
    And validate GET credentials response by multiple filter status "<status>", sortBy "<sortBy>", sortDirection "<sortDirection>"

    Examples:
      | credentialName | status | sortBy            | sortDirection |
      | validName      | A      | EXPIRY_DATE       | ASC           |
      | validName      | D      | LAST_UPDATED_DATE | DESC          |


    #BUG-2438
  #@trial @regression
  Scenario Outline: SC-30 Positive flow - Fetch created credential details with multiple filters - status, sortBy, sortDirection, limit, page
    Given I am an authorized to create credentials as DRAGON user
    When I hit the post credentials endpoint five times with credential name "<credentialName>"
    Given I am an authorized to put credentials as DRAGON user
    When I hit the put credentials endpoint with new credential name "<credentialName>" and status "D"
    Given I am an authorized to create credentials as DRAGON user
    When I hit the post credentials endpoint with new credentialName "<credentialName>"
    Given I am an authorized to put credentials as DRAGON user
    When I hit the put credentials endpoint with new credential name "<credentialName>" and status "D"
    Given I am an authorized DRAGON user
    When I query for a list of credentials with filter status "<status>", sortBy "<sortBy>", sortDirection "<sortDirection>", limit "<limit>", page "<page>"
    Then I should receive successful get credential response
    And validate GET credentials response by multiple filter status "<status>", sortBy "<sortBy>", sortDirection "<sortDirection>", limit "<limit>", page "<page>"

    Examples:
      | credentialName | status | sortBy            | sortDirection | limit | page |
      | validName      | A      | EXPIRY_DATE       | ASC           | 50    | 5    |
      | validName      | D      | LAST_UPDATED_DATE | DESC          | 1     | 2    |


  @trial @regression
  Scenario Outline: SC-30 Positive flow - Fetch created credential details with all filters - status, sortBy, sortDirection, limit, page, credentialId, credentialName
    Given I am an authorized to create credentials as DRAGON user
    When I hit the post credentials endpoint five times with credential name "<credentialName>"
    Given I am an authorized to put credentials as DRAGON user
    When I hit the put credentials endpoint with new credential name "<credentialName>" and status "D"
    Given I am an authorized to create credentials as DRAGON user
    When I hit the post credentials endpoint with new credentialName "<credentialName>"
    Given I am an authorized to put credentials as DRAGON user
    When I hit the put credentials endpoint with new credential name "<credentialName>" and status "D"
    Given I am an authorized DRAGON user
    When I query for a list of credentials with filter status "<status>", sortBy "<sortBy>", sortDirection "<sortDirection>", limit "<limit>", page "<page>", credentialId, credentialName
    Then I should receive successful get credential response
    And validate GET credentials response by multiple filter status "<status>", sortBy "<sortBy>", sortDirection "<sortDirection>", limit "<limit>", page "<page>", credentialId, credentialName

    Examples:
      | credentialName | status | sortBy            | sortDirection | limit | page |
      | validName      | A      | EXPIRY_DATE       | ASC           | 50    | 5    |
      | validName      | D      | LAST_UPDATED_DATE | DESC          | 1     | 0    |


  #@trial @regression
  Scenario Outline: SC-29-32 Negative flow - Invalid auth token
    Given I am an authorized to create credentials as DRAGON user
    When I hit the post credentials endpoint with credential name "<credentialName>"
    Given I am an unauthorized DRAGON user with invalid "<auth_token>" auth token
    When I hit get credentials endpoint without any filter
    Then I should receive a "<http_code>" error response with "<error_description>" error description and "<error_code>" errorcode within get credentials response
    And error message should be "<error_message>" within get credentials response

    Examples:
      | credentialName | error_description    | error_message                     | auth_token                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             | error_code | http_code |
 #Auth Token missing
      | validName      | Error validating JWT | API Gateway Authentication Failed |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        | EA001      | 401       |
 # Auth token not a JWT
      | validName      | Error validating JWT | API Gateway Authentication Failed | random_auth_token                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      | EA001      | 401       |
 # Expired auth token
      | validName      | Error validating JWT | API Gateway Authentication Failed | Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Imk2bEdrM0ZaenhSY1ViMkMzbkVRN3N5SEpsWSIsImtpZCI6Imk2bEdrM0ZaenhSY1ViMkMzbkVRN3N5SEpsWSJ9.eyJhdWQiOiI1MTczYTVhOS00MjEyLTQ4NzctODYyMS03YmMxNjRjZjE3OGIiLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yZmY5M2NjZS1lZmQxLTRlMTYtOTJiYS1hZmI1M2U5ZTA5ZmMvIiwiaWF0IjoxNTM3MTY4NzAzLCJuYmYiOjE1MzcxNjg3MDMsImV4cCI6MTUzNzE3MjYwMywiYWlvIjoiNDJCZ1lKaDlXbFZsNFh1SjZFMHZsMDMvY25TOU5nQT0iLCJhcHBpZCI6IjEwOTMyM2UzLWM1NWUtNDI0Yy1iNzEyLTJlODJmMTY5NWU5OCIsImFwcGlkYWNyIjoiMSIsImlkcCI6Imh0dHBzOi8vc3RzLndpbmRvd3MubmV0LzJmZjkzY2NlLWVmZDEtNGUxNi05MmJhLWFmYjUzZTllMDlmYy8iLCJvaWQiOiI4NjM3MjIwNi1jZWZhLTQ1ZWEtOTM0Ni0xNjNiYmFiYzc3MDYiLCJyb2xlcyI6WyJwYXltZW50UmVxdWVzdCIsInJlZnVuZCIsImRldmVsb3BlciJdLCJzdWIiOiI4NjM3MjIwNi1jZWZhLTQ1ZWEtOTM0Ni0xNjNiYmFiYzc3MDYiLCJ0aWQiOiIyZmY5M2NjZS1lZmQxLTRlMTYtOTJiYS1hZmI1M2U5ZTA5ZmMiLCJ1dGkiOiI3bUd1b0FRMUQwZTFGMks0Sy1ZWEFBIiwidmVyIjoiMS4wIn0.gCfn6QNUjzENuKvVN0bdkWgRwN-1ipovSq5Yb8IjCRfRhZNTFffLP1oIVw_8sPQxTFfV8CBOcQG385n-r_tIWBlMANHdKmpmwmOTz4J08EWzz_SY4zWdvMlF7quCYdrXVIXaKd-PLtO0UDTI7CsSAso7OAOsHBNrn3ITBoR0aMo_lM8X5dJM5fleSHFiJPMYDHpawZuy_BBXC0AUdcVT61NHkdO7sDV4Dc_C12CsShEqMi3Nj-uzr5wYHmPTxi3nk2px-_yiVaAHxxCmE0f7rWdM4BjVB89j_xtZrn1-VovgUUn_QDppY9yOgKx831xXBMX0Tz293V8g7BkagVI3lw | EA001      | 401       |
 # Auth token unverified
      | validName      | Error validating JWT | API Gateway Authentication Failed | Bearer nbCwW11w3XkB-xUaXwKRSLjMHGQ                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     | EA001      | 401       |


  #@trial @regression
  Scenario Outline: SC-33-37 Negative flow - Invalid mandatory field Api-Version provided in header
    Given I am an authorized DRAGON user
    When I hit get credentials endpoint for invalid header "<key>" with value "<invalidHeaderValues>"
    Then I should receive status code "<statusCode>" and message "<message>" in get credentials response
    Examples:
      | key         | invalidHeaderValues | statusCode | message            |
      | Api-Version | 0.20                | 404        | Resource not found |
      | Api-Version | abc                 | 404        | Resource not found |
      | Api-Version | 0.10                | 404        | Resource not found |
      | Api-Version | 0.11                | 404        | Resource not found |
      | Api-Version | @#$%^               | 404        | Resource not found |


  #@trial @regression
  Scenario Outline: SC-38-42 Negative flow - Invalid mandatory field provided in header
    Given I am an authorized DRAGON user
    When I hit get credentials endpoint for invalid header "<key>" with value "<invalidHeaderValues>"
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorcode within get credentials response
    And error message should be "<error_message>" within get credentials response
    Examples:
      | key          | invalidHeaderValues                  | http_status | error_code | error_description                                                 | error_message                     |
      | Accept       | Testing/Type                         | 406         | EA008      | Header Accept does not contain required value.  Access denied.    | Request Header Not Acceptable     |
      | Content-Type | application/json1                    | 415         | EA002      | Content type 'application/json1;charset=ISO-8859-1' not supported | Service Request Validation Failed |
      | Trace-Id     | 123456                               | 400         | EA002      | Failed to convert value of type                                   | Service Request Validation Failed |
      | Trace-Id     | abcde                                | 400         | EA002      | Failed to convert value of type                                   | Service Request Validation Failed |
      | Trace-Id     | 7454108z-yb37-454c-81da-0a12d8b0f867 | 400         | EA002      | Failed to convert value of type                                   | Service Request Validation Failed |


  #@trial @regression
  Scenario Outline: SC-43-47 Negative flow - Invalid applicationId
    Given I am an authorized DRAGON user
    When I make request to get credentials endpoint with invalid applicationId "<applicationId>"
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorcode within get credentials response
    And error message should be "<error_message>" within get credentials response
    Examples:
      | applicationId                         | http_status | error_code | error_description               | error_message                     |
      | space                                 | 400         | EA002      | applicationId not provided      | Service Request Validation Failed |
      | 1234                                  | 400         | EA002      | Failed to convert value of type | Service Request Validation Failed |
      | abcd                                  | 400         | EA002      | Failed to convert value of type | Service Request Validation Failed |
      | !~@^*                                 | 400         | EA002      | Failed to convert value of type | Service Request Validation Failed |
      | xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxxx | 400         | EA002      | Failed to convert value of type | Service Request Validation Failed |


  #@trial @regression
  Scenario Outline: SC-42- Negative flow - Null applicationId
    Given I am an authorized DRAGON user
    When I make request to get credentials endpoint with invalid applicationId "<applicationId>"
    Then I should receive status code "404" and message "Resource not found" in get credentials response
    Examples:
      | applicationId |
      | null          |


  #@trial @regression
  Scenario Outline: SC-43-48 - Negative flow - Invalid filter - status
    Given I am an authorized to create credentials as DRAGON user
    When I hit the post credentials endpoint with credential name "<credentialName>"
    Given I am an authorized DRAGON user
    When I hit get credentials endpoint with filter status as "<status>"
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorcode within get credentials response
    And error message should be "<error_message>" within get credentials response
    Examples:
      | credentialName | status | http_status | error_code | error_description | error_message                     |
      | validName      |        | 400         | EA002      | Status is invalid | Service Request Validation Failed |
      | validName      | active | 400         | EA002      | Status is invalid | Service Request Validation Failed |
      | validName      | A,D,E  | 400         | EA002      | Status is invalid | Service Request Validation Failed |
      | validName      | 1234   | 400         | EA002      | Status is invalid | Service Request Validation Failed |
      | validName      | abcd   | 400         | EA002      | Status is invalid | Service Request Validation Failed |
      | validName      | !~@^*  | 400         | EA002      | Status is invalid | Service Request Validation Failed |


  #@trial @regression
  Scenario Outline: SC-53-56 Negative flow - Invalid filter - credentialId
    Given I am an authorized to create credentials as DRAGON user
    When I hit the post credentials endpoint with credential name "<credentialName>"
    Given I am an authorized DRAGON user
    When I hit get credentials endpoint with filter credentialId as "<credentialId>"
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorcode within get credentials response
    And error message should be "<error_message>" within get credentials response
    Examples:
      | credentialName | credentialId | http_status | error_code | error_description               | error_message                     |
      | validName      | 123          | 400         | EA002      | Failed to convert value of type | Service Request Validation Failed |
      | validName      | !~^$@        | 400         | EA002      | Failed to convert value of type | Service Request Validation Failed |


  #@trial @regression
  Scenario Outline: SC-53-56 Negative flow - Invalid filter - sortDirection
    Given I am an authorized to create credentials as DRAGON user
    When I hit the post credentials endpoint with credential name "<credentialName>"
    Given I am an authorized DRAGON user
    When I hit get credentials endpoint with filter sortDirection as "<sortDirection>"
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorcode within get credentials response
    And error message should be "<error_message>" within get credentials response
    Examples:
      | credentialName | sortDirection | http_status | error_code | error_description        | error_message                     |
      | validName      | 123           | 400         | EA002      | sortDirection is invalid | Service Request Validation Failed |
      | validName      | !~^$@         | 400         | EA002      | sortDirection is invalid | Service Request Validation Failed |
      | validName      | ascending     | 400         | EA002      | sortDirection is invalid | Service Request Validation Failed |
      | validName      | space         | 400         | EA002      | sortDirection is invalid | Service Request Validation Failed |

  #@trial @regression
  Scenario Outline: SC-53-56 Negative flow - Invalid filter - sortBy
    Given I am an authorized to create credentials as DRAGON user
    When I hit the post credentials endpoint with credential name "<credentialName>"
    Given I am an authorized DRAGON user
    When I query for a list of credentials with filter sortBy as "<sortBy>"
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorcode within get credentials response
    And error message should be "<error_message>" within get credentials response
    Examples:
      | credentialName | sortBy  | http_status | error_code | error_description | error_message                     |
      | validName      | 123     | 400         | EA002      | sortBy is invalid | Service Request Validation Failed |
      | validName      | !~^$@   | 400         | EA002      | sortBy is invalid | Service Request Validation Failed |
      | validName      | expired | 400         | EA002      | sortBy is invalid | Service Request Validation Failed |
      | validName      | space   | 400         | EA002      | sortBy is invalid | Service Request Validation Failed |