Feature: Accessing Employees details
  As a user
  I want to access the information of all the employees

  @getEmployees @test
  Scenario Outline: Retrieve the information of the employees
    Given I am an user with having access to APIs
    When I make a request to get the details of all employees
    Then I should receive a "<http_status>" response for employees

    Examples:
      | http_status |
      | 200        |



