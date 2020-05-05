Feature: Delete Employees details by employee Id
  As a user
  I want to delete the employees by using employee Ids

  @DeleteEmployeeRecord @test
  Scenario Outline: Delete the employees by employee Id
    Given I am an user with having access to APIs
    When I make a request to create the detail of new employee with name "<name>", "<salary>", "<age>"
    Then I should receive a "<http_status>" response code create employee
    When I make a request to delete the employee details with employee Id "<employeeId>"
    Then I should receive a "<http_status>" response code and correct for delete employee

    Examples:
      | http_status | name | salary | age | employeeId |
      | 200         | John | 5000   | 30  | 1          |

