@POSGetMerchantByPlatformId
Feature: [POS]Internal Api to get merchant list under the platform id


  Scenario Outline: Business triggers the get merchant list api to get merchant list under given platform id
    Given user provides valid data "<Authorization>","<Apiversion>","<Trace-id>" in header to get merchants list under given platform id
    And user provides "<PlatformId>" in path to get subunit ids mapped to the same
    When user triggers the get merchants by platform id
    Then verify the response status code "<statusCode>" for get merchants by platform id
    And verify the response contains all the expected merchants details under the given "<PlatformId>"

    Examples:
      | Authorization | statusCode | Apiversion | Trace-id                             | PlatformId                           |
      |               | 200        | 0.12       | 3c80a054-aa0c-4d3d-868e-76f0b8b48f40 | 39416ca9-96a0-4c16-b4a1-a7b885832f3f |

#negative scenario
  Scenario Outline: Error is thrown when get merchant by platform id api is triggered with missing headers
    Given user provides valid data "<Authorization>","<Apiversion>","<Trace-id>" in header to get merchants list under given platform id
    And user remove the header "<Header>"
    And user provides "<PlatformId>" in path to get subunit ids mapped to the same
    When user triggers the get merchants by platform id
    Then verify the response status code "<statusCode>" for get merchants by platform id
    And check error code and error description matches "<ErrorCode>","<ErrorDescription>"


    Examples:
      | Authorization | statusCode | Apiversion | Trace-id                             | PlatformId                           | Header        | ErrorCode | ErrorDescription                                                           |
      |               | 400        | 0.12       | 3c80a054-aa0c-4d3d-868e-76f0b8b48f40 | 39416ca9-96a0-4c16-b4a1-a7b885832f3f | Api-Version   | EA002     | Missing request header 'Api-Version' for method parameter of type String   |
      |               | 400        | 0.12       | 3c80a054-aa0c-4d3d-868e-76f0b8b48f40 | 39416ca9-96a0-4c16-b4a1-a7b885832f3f | Trace-Id      | EA002     | Missing request header 'Trace-Id' for method parameter of type UUID        |
      |               | 400        | 0.12       | 3c80a054-aa0c-4d3d-868e-76f0b8b48f40 | 39416ca9-96a0-4c16-b4a1-a7b885832f3f | Authorization | EA002     | Missing request header 'Authorization' for method parameter of type String |

#negative scenario
  Scenario Outline: Error is thrown when get merchant by platform id api is triggered with Trace-id as null
    Given user provides valid data "<Authorization>","<Apiversion>","<Trace-id>" in header to get merchants list under given platform id
    And user provides "<PlatformId>" in path to get subunit ids mapped to the same
    When user triggers the get merchants by platform id
    Then verify the response status code "<statusCode>" for get merchants by platform id
    And check error code and error description matches "<ErrorCode>","<ErrorDescription>"

    Examples:
      | Authorization | statusCode | Apiversion | Trace-id | PlatformId                           | ErrorCode | ErrorDescription    |
      |               | 400        | 0.12       | null     | 39416ca9-96a0-4c16-b4a1-a7b885832f3f | EA002     | Invalid UUID string |
