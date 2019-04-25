Feature: Transactions List

  Background: Retrieving access token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

  #Checking Input Parameters
#  @trial
  @regression
  Scenario Outline: Negative flow - Send invalid fromTime and toTime
    Given I am an authorized user
    When I query for a list of transactions between "<fromTime>" and "<toTime>"
    Then I should receive a error response with "<error_description>" error description and "<error_code>" errorcode within transaction response
    Examples:
      | fromTime                  | toTime                    | error_description | error_code |
      | 2019-01-02T10:00:00-05:00 | 2019-01-01T10:00:00-05:00 | (from) is after (to) | EA020 |
      | 2019-01-02T10:00:00-05:00 | 2019-01-02T09:00:00-05:00 | (from) is after (to) | EA020 |
      | abc                       | 2019-01-02T09:00:00-05:00 | Invalid RFC3339 | EA019 |
      | 2019                      | 2019-01-02T09:00:00-05:00 | Invalid RFC3339 | EA019 |
      | 2019-JAN-01               | 2019-01-02T09:00:00-05:00 | Invalid RFC3339 | EA019 |
      | 2019-02-01T00:00:00Z      | abc                       | Invalid RFC3339 | EA019 |
      | 2019-02-01T00:00:00Z      | 2019                      | Invalid RFC3339 | EA019 |
      | 2019-02-01T00:00:00Z      | 2019-JAN-01               | Invalid RFC3339 | EA019 |

#  @trial
  @regression
  Scenario Outline: Negative flow - Send invalid limit
    Given I am an authorized user
    When I query for a list of transactions between "<fromTime>" and "<toTime>" with "<limit>"
    Then I should receive a error response with "<error_description>" error description and "<error_code>" errorcode within transaction response
    Examples:
      | fromTime                  | toTime                    | limit | error_description | error_code |
      | 2019-01-01T10:00:00-05:00 | 2050-01-10T10:00:00-05:00 | XXX   | NumberFormatException | EA002      |

#  @trial
  @regression
    Scenario Outline: Negative flow - Limit not in acceptable range
      Given I am an authorized user
      When I query for a list of transactions with "<limit>"
      Then I should receive "<actual>" number of transactions
    Examples:
      | limit | actual |
      |  0    |    10  |
      |  -1     |    10   |
      |   31     |    30    |

#  @trial
  @regression
  Scenario Outline: Negative flow - Limit not specified returning default limit
    Given I am an authorized user
    When I query for a list of transactions between "<fromTime>" and "<toTime>"
    Then I should receive "<actual>" number of transactions
    Examples:
      |fromTime                 | toTime           | actual |
      |2000-01-01T00:00:00Z | 2100-02-01T00:00:00Z | 20     |


#  @trial
  @regression
  Scenario Outline: Negative flow - Send invalid startingAfter
    Given I am an authorized user
    When I query for a list of transactions starting after "<startingAfter>"
    Then I should receive a error response with "<error_description>" error description and "<error_code>" errorcode within transaction response

  Examples:
    | startingAfter                        | error_description | error_code |
    | XXXXXXXXXXXXXXXX |  Invalid UUID | EA002           |

#    #Positive Flow
#    @trial
    @regression @trial
    Scenario Outline: Positive flow - Get a list of transactions for a merchant
      Given I am an authorized user
      When I query for a list of transactions between "<fromTime>" and "<toTime>"
      Then I should receive a successful transaction response
      And transaction object is a subset of the original list
      And transactionSource is not numeric and converted properly
      And transactionType is not numeric and converted properly
      And status is convert properly

    Examples:
      |fromTime                 | toTime                    |
      |2019-01-01T00:00:00Z | 2019-02-01T00:00:00Z |
      |2002-10-02T15:00:00Z     | 2002-10-03T15:00:00Z      |
      |2000-01-01T00:00:00Z     | 2021-01-01T00:00:00Z      |

#    @trial
    @regression
    Scenario Outline: Positive flow - Get a list of transactions for a merchant with limit
      Given I am an authorized user
      When I query for a list of transactions between "<fromTime>" and "<toTime>" with "<limit>"
      Then I should receive a successful transaction response
      And I should have a maximum <limit> number of transactions returned

    Examples:
      |fromTime                 | toTime                    | limit |
      |2018-01-01T00:00:00Z | 2050-02-01T00:00:00-04:00 | 10    |
      |2018-01-01T00:00:00+08:00 | 2050-02-01T00:00:00Z | 20    |

#    @trial
    @regression
    Scenario Outline: Positive flow - Get a list of transactions for a merchant with limit and use startingAfter
      Given I am an authorized user
      When I query for a list of transactions between "<fromTime>" and "<toTime>" with "<limit>"
      Then I should receive a successful transaction response
      And I should have at least <minimum> number of transactions returned
      When I take the first transaction id and make a call with startingAfter between "<fromTime>" and "<toTime>" with limit of <limit> transactions
      Then I should receive a successful transaction response
      And the returned transactions list should not have the transaction id used in the request

      Examples:
        |fromTime                 | toTime                    | limit | minimum |
        |2018-01-01T00:00:00.235+01:00 | 2050-02-01T00:00:00.120Z | 10    | 2       |
