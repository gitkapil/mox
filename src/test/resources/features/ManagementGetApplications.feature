Feature: Merchant Management API - GET applications

  Background: Retrieving access Token
    Given I am an authorized CSO user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token
#    And I then clean and create 30 test applications     NOT Sure how to do this yet.

  @trial
  @regression @merchantManagement @merchantManagementGet
  Scenario Outline: Positive flow - Get a list of applications
    Given I am an authorized CSO user
    When I get a list of applications without any filters
    Then I should receive a successful response
    And the response should have a list of <numberOfResponses> applications
    And the response should have more than <numberOfResponses> in total

    Examples:
    |numberOfResponses|
    |20               |

#  @trial
  @regression @merchantManagement @merchantManagementGet
  Scenario Outline: Positive flow - Get a list of application using filters
    Given I am an authorized CSO user
    When I get a list of applications using filters to filter "<filterName>" with "<filterValue>"
    Then I should receive a successful response
    And the response should have a list of <numberOfResponses> applications

    Examples:
    |filterName | filterValue | numberOfResponses|
    |clientid   |             |                  |
    |clientid   |   unknown          |                  |
    |peakid     | |                              |
    |subunitid  | |                              |
    |organisationid||                            |
    |clientid,peakid|unknown,known|                           |
    |clientid,peakid|unknown,unknown|                           |
    |clientid,peakid,subunitid||                 |
    |clientid,peakid,subunitid,organisationid||  |

#  @trial
  @regression @merchantManagement @merchantManagementGet
    Scenario Outline: Positive flow - Get a list of application using paging and limits
    Given I am an authorized CSO user
    When I get a list of applications using filters to filter "<filterName>" with "<filterValue>" with <limit> limits
    Then I should receive a successful response
    And the response should have a list of <numberOfResponses> applications
    And the response should have more than <numberOfResponses> in total
    And the response should have a <totalNumberOfPages> number of total pages
    And the response should be on page <currentPageNumber>
    When I move to page <nextPageNumber>
    Then I should receive a successful response
    And the response should have a list of <nextNumberOfResponses> applications
    And the response should have more than <nextNumberOfResponses> in total
    And the response should have a <totalNumberOfPages> number of total pages
    And the response should be on page <nextPageNumber>

    Examples:
    |filterName|filterValue|limit|numberOfResponses|totalNumberOfPages|currentPageNumber|nextPageNumber|nextNumberOfResponses|
    |clientid  |           | 1    |1                 |                  |                 |2              |1                 |
    |clientId  |           | 1    |1                 |                  |                 |200              |                |
    |clientId  |           |30    |20                |                  |                 |2                |                |


