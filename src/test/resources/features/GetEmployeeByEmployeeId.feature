Feature: Accessing Employees details by employee Id
  As a user
  I want to access the information of the employees by using employee Ids

@getEmployeeByEmployeeId @test
  Scenario Outline: Retrieve the information of the employee by employee Id
    Given I am an user with having access to APIs
    When I make a request to get the employee details with employee Id "<employeeId>"
    Then I should receive a "<http_status>" response


    Examples:
      | http_status | employeeId |
      | 200         | 1          |

