Feature: Merchant Management AAD Password - DRAG-1481

  Background: Retrieving access Token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

#  @trial
#  @regression
  Scenario Outline: Positive flow - create AAD Password provided with valid request body and header values
    Given I am logging in as a user with AAD Password role
    And I have an "<applicationId>" from an existing application
    And I have created password data with activate at "<activateAt>", deactivate at "<deactivateAt>", pdfChannel "<pdfChannel>" and password channel "<passwordChannel>"
    Then I should see the response from post password request
    Examples:
      | applicationId                        | activateAt           | deactivateAt         | pdfChannel | passwordChannel  |
      | c9621185-b86d-48a9-97f0-eeddef7c3dc1 | 2019-01-01T00:00:00Z | 2019-02-02T00:00:00Z | pdfChannel | password Channel |


#  @trial
#  @regression
  Scenario Outline: Negative flow - unable to create password due to bad application id
    Given I am logging in as a user with AAD Password role
    When I set the application id as "<applicationId>"
    And I have created password data with activate at "<activateAt>", deactivate at "<deactivateAt>", pdfChannel "<pdfChannel>" and password channel "<passwordChannel>"
    Then I should have an error with status "<http_status>", error code as "<error_code>" and description "<error_description>"
    Examples:
      | applicationId                        | http_status | error_code | error_description | activateAt           | deactivateAt         | pdfChannel | passwordChannel  |
    #bad application id format
      | aa                                   | 400         | EA000      | test              | 2019-01-01T00:00:00Z | 2019-02-02T00:00:00Z | pdfChannel | password Channel |

    # invalid application id
      | 00000002-1111-0000-c000-000000000000 | 400         | EA000      | test              | 2019-01-01T00:00:00Z | 2019-02-02T00:00:00Z | pdfChannel | password Channel |

#  @trial
#  @regression
  Scenario Outline: Negative flow - Unable to create password with invalid body values
    Given I am logging in as a user with AAD Password role
    When I get a list of applications
    And I get the first application id
    And I have created password data with activate at "<activateAt>", deactivate at "<deactivateAt>", pdfChannel "<pdfChannel>" and password channel "<passwordChannel>"
    Then I should have an error with status "<http_status>", error code as "<error_code>" and description "<error_description>"
    Examples:
      | activateAt           | deactivateAt         | pdfChannel | http_status | error_code | error_description | passwordChannel  |
      |                      | 2019-02-02T00:00:00Z | test       | 400         | EA000      | test              | password Channel |
      | null                 | 2019-02-02T00:00:00Z | test       | 400         | EA000      | test              | password Channel |
      | Monday               | 2019-02-02T00:00:00Z | test       | 400         | EA000      | test              | password Channel |
      | 2019-01-01T00:00:00Z | null                 | test       | 400         | EA000      | test              | password Channel |
      | 2019-01-01T00:00:00Z |                      | test       | 400         | EA000      | test              | password Channel |
      | 2019-01-01T00:00:00Z | Tuesday              | test       | 400         | EA000      | test              | password Channel |
      | 2019-02-02T00:00:00Z | 2019-01-01T00:00:00Z | test       | 400         | EA000      | test              | password Channel |
      | 2019-01-01T00:00:00Z | 2019-02-02T00:00:00Z | superlong  | 400         | EA000      | test              | password Channel |


    #@trial
  @regression @HappyFlow
  Scenario Outline: Negative flow - Unable to create password due to invalid header values
    Given I am logging in as a user with AAD Password role
    When I get a list of applications
    And I get the first application id
    And  I create a new AAD password with null header "<nullHeaderValues>"
    Then I should have an error with status "<http_status>", error code as "<error_code>" and description "<error_description>"

    Examples:
      | http_status | error_code | error_description | nullHeaderValues |
      | 400         | EA000      | test              | applicationId    |
      | 400         | EA000      | test              | Api-Version      |
      | 400         | EA000      | test              | Accept           |
      | 400         | EA000      | test              | Content-Type     |
      | 400         | EA000      | test              | Authorization    |
      | 400         | EA000      | test              | Trace-Id         |


