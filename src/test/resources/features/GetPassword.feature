Feature: Merchant Management get Password - DRAG-1481

  Background: Retrieving access Token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

#  @trial
#  @regression
  Scenario Outline: Positive flow - get the password details with valid input header
    Given I am logging in as a user with authorize Dragon user
    And I have an "<applicationId>" from an existing application
    When I get the password request
    Then I should see the response from get password request
    Examples:
      | applicationId                        | activateAt           | deactivateAt         | pdfChannel | passwordChannel  |
      | c9621185-b86d-48a9-97f0-eeddef7c3dc1 | 2019-01-01T00:00:00Z | 2019-02-02T00:00:00Z | pdfChannel | password Channel |


#  @trial
#  @regression
  Scenario Outline: Negative flow - unable to get password with invalid application id
    Given I am logging in as a user with AAD Password role
    When I set the application id as "<applicationId>"
    When I get the password request
    Then I should have an error with status "<http_status>", error code as "<error_code>" and description "<error_description>"
    Examples:
      | applicationId                        | http_status | error_code | error_description | activateAt           | deactivateAt         | pdfChannel | passwordChannel  |
    #bad application id format
      | aa                                   | 400         | EA000      | test              | 2019-01-01T00:00:00Z | 2019-02-02T00:00:00Z | pdfChannel | password Channel |

    # invalid application id
      | 00000002-1111-0000-c000-000000000000 | 400         | EA000      | test              | 2019-01-01T00:00:00Z | 2019-02-02T00:00:00Z | pdfChannel | password Channel |



  #@trial
  @regression @HappyFlow
  Scenario Outline: Negative flow - Unable to create password due to invalid header values
    Given I am logging in as a user with AAD Password role
    When I get a list of applications
    And I get the first application id
    And  I get the password with null header "<nullHeaderValues>"
    Then I should have an error with status "<http_status>", error code as "<error_code>" and description "<error_description>"

    Examples:
      | http_status | error_code | error_description | nullHeaderValues |
      | 400         | EA000      | test              | applicationId    |
      | 400         | EA000      | test              | Api-Version      |
      | 400         | EA000      | test              | Accept           |
      | 400         | EA000      | test              | Content-Type     |
      | 400         | EA000      | test              | Authorization    |
      | 400         | EA000      | test              | Trace-Id         |


