Feature: Update Employees details
  As a user
  I want to update a new employee with all input details

  @updateEmployeeDetails @test
  Scenario Outline: Update the information of the employee by employee Id
    Given I am an user with having access to APIs
    When I make a request to create the detail of new employee with name "<name>", "<salary>", "<age>"
    Then I should receive a "<http_status>" response code create employee
    When I make a request to update the detail of employee existing employee with name "<name>", "<salary>", "<age>"
    Then I should receive a "<http_status>" response code update employee
    And response should have correct values

    Examples:
      | http_status | name | salary | age |
      | 200         | John | 5000   | 30  |

