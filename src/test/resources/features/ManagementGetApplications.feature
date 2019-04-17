Feature: Merchant Management API - GET /applications

  Background: Retrieving access Token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

#  @trial
  @regression @merchantManagement @merchantManagementGet
  Scenario Outline: Positive flow - Get a list of applications
    Given I am a GET application authorized DRAGON user with the Application.ReadWrite.All privilege
    When I get a list of applications without any filters
    Then I should receive a successful response
    And the response should have a list of <numberOfResponses> applications
    And the response should have more than or equal to <numberOfResponses> in total

    Examples:
    |numberOfResponses|
    |20               |

#  @trial
  @regression @merchantManagement @merchantManagementGet
  Scenario Outline: Positive flow - Get a list of application using filters
    Given I am a GET application authorized DRAGON user with the Application.ReadWrite.All privilege
    When I get a list of applications using filters to filter "<filterName>" with "<filterValue>"
    Then I should receive a successful response
    And the response should have a list of <numberOfResponses> applications

    Examples:
    |filterName | filterValue                          | numberOfResponses |
    |clientId   | 00000001-0000-0000-0000-000000000000 | 1                 |
    |clientId   | 00000001-0000-0000-0000-000000009999 | 0                 |
    |peakId     | 00000002-0000-0000-c000-000000000000 | 2                 |

#  @trial
  Scenario: Negative flow - Get a list of application using multi filters
    Given I am a GET application authorized DRAGON user with the Application.ReadWrite.All privilege
    When I get a list of application using multiple filters
    Then I should get an error message with status 400 error code "EA002" and error description "Failed to convert value of type"

#  @trial
  Scenario: Negative flow - Get a list of application using multi filters
    Given I am a GET application authorized DRAGON user with the Application.ReadWrite.All privilege
    When I get a list of application using multiple filters with correct uuids
    Then I should get an error message with status 400 error code "EA002" and error description "Only one of peakId or clientId query can be used at the same time"

#  @trial
  @regression @merchantManagement @merchantManagementGet
    Scenario Outline: Positive flow - Get a list of application using paging and limits
    Given I am a GET application authorized DRAGON user with the Application.ReadWrite.All privilege
    When I get a list of applications using filters to filter "<filterName>" with "<filterValue>" with <limit> limits
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
    |filterName|filterValue                                    |limit|numberOfResponses|totalNumberOfItems|currentPageNumber|nextPageNumber|nextNumberOfResponses|
    |peakId    |00000002-0000-0000-c000-000000000000           | 1   |1                |2                 |0                |1             |1                    |
    |peakId    |00000002-0000-0000-c000-000000000000           |30   |2                |1                 |0                |2             |2                    |


