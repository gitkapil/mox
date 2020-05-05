Feature: Create Employees details
  As a user
  I want to create a new employee with all input details

@CreateEmployeeRecord @test
  Scenario Outline: Create employee with input body.
    Given I am an user with having access to APIs
    When I make a request to create the detail of new employee with name "<name>", "<salary>", "<age>"
    Then I should receive a "<http_status>" response code create employee
    And response should have correct values
    Examples:
      | http_status | name | salary | age |
      | 200         | John | 5000   | 30  |

