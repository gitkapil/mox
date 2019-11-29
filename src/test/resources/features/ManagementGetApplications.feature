@skiponversionten @skiponversiontwelve
Feature: Merchant Management API - GET /applications
         As a user
         I want to get the application details and validate the response is correct

  Background: Retrieving access Token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

  #@trial
  @regression @getApplication
  Scenario Outline: Positive flow - Get a list of applications
    Given I am a GET application authorized DRAGON user with the Application.ReadWrite.All privilege
    When I get a list of applications without any filters
    Then I should receive a successful response
    And the response should have a list of <numberOfResponses> applications
    And the response should have more than or equal to <numberOfResponses> in total
    Examples:
      | numberOfResponses |
      | 20                |
    
  #@trial
  @regression
  Scenario Outline: Positive flow - Get a list of application using filters 1
    Given I am a GET application authorized DRAGON user with the Application.ReadWrite.All privilege
    When I get a list of applications using filters to filter "<filterName>"
    Then I should receive a successful response
    And the response should have a list of <numberOfResponses> applications
    Examples:
      | filterName   | numberOfResponses |
      | clientId     | 1                 |
      | peakId       | 20                |
      | subUnitId    | 20                |
      | PlatformId   | 20                |
      | platformName | 20                |

  #@trial
  @regression
  Scenario Outline: Positive flow - Get  the application using filters platformId
    Given I am a GET application authorized DRAGON user with the Application.ReadWrite.All privilege
    When I create new application
    When I get the application details of newly created application using filter "<filterName>"
    Then I should receive a successful response
    And the response should have a list of <numberOfResponses> applications
    And validate the item list from the response
    Examples:
      | filterName | numberOfResponses |
      | clientId   | 1                 |


  #@trial
  @regression
  Scenario: Negative flow - Get a list of application with two filter using invalid peakId and clientId
    Given I am a GET application authorized DRAGON user with the Application.ReadWrite.All privilege
    When I get a list of application using multiple filters
    Then I should get an error message with status 400 error code "EA002" and error description "Failed to convert value of type"


  #@trial
  @regression
  Scenario: Negative flow - Get a list of application using multi filters 1
    Given I am a GET application authorized DRAGON user with the Application.ReadWrite.All privilege
    When I get a list of application using multiple filters with correct uuids
    Then I should receive a successful response

  #@trial
  @regression
  Scenario Outline: Positive flow - Get a list of application using paging and limits
    Given I am a GET application authorized DRAGON user with the Application.ReadWrite.All privilege
    When I get a list of applications using filters to filter "<filterName>" with <limit> limits
    Then I should receive a successful response
    And the response should have a list of <numberOfResponses> applications
    And the response should have more than or equal to <numberOfResponses> in total
    And the response should have a <totalNumberOfItems> number of total items
    And the response should be on page <currentPageNumber>
    When I move to page <nextPageNumber>
    Then I should receive a successful response
    And the response should have a list of <nextNumberOfResponses> applications
    And the response should have more than or equal to <nextNumberOfResponses> in total
    And the response should have a <totalNumberOfItems> number of total items
    And the response should be on page <nextPageNumber>

    Examples:
      | filterName | limit | numberOfResponses | totalNumberOfItems | currentPageNumber | nextPageNumber | nextNumberOfResponses |
      | clientId   | 1     | 1                 | 1                  | 0                 | 0              | 1                     |
      | clientId   | 30    | 1                 | 1                  | 0                 | 0              | 1                     |

  #@trial
  @regression
  Scenario Outline: Negative flow - Get a list of application using null header values
    Given I am a GET application authorized DRAGON user with the Application.ReadWrite.All privilege
    When I get a list of applications using filters to filter "<filterName>" and "<nullHeaderValue>" values
    Then I should receive a "<http_status>" error response with "<error_description>" error description and "<error_code>" errorCode within the get application response
    And error message should be "<error_message>" within the get application response
    Examples:
      | filterName | nullHeaderValue   | error_message                     | error_code | http_status | error_description                               |
   #   | clientId   | Trace-Id          | API Gateway Validation Failed     | EA002      | 400         | Header Trace-Id was not found in the request    |
   #   | clientId   | Content-Type      | Service Request Validation Failed | EA002      | 415         | Content type                                    |
      | clientId   | Accept            | Request Header Not Acceptable     | EA008      | 406         | Header Accept does not contain required value   |


  #@trial
  @regression
  Scenario Outline: Negative flow- Api version Field is missing from the header
    Given I am a GET application authorized DRAGON user with the Application.ReadWrite.All privilege
    And I make a get request to the application endpoint with "<missingApiVersion>" missing in the header
    And error message should be "<errorMessage>" within the get application response

    Examples:
      | missingApiVersion | errorMessage       |
      | Api-Version       | Resource not found |

