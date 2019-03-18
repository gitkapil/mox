Feature: Transactions List

  Background: Retrieving access token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

    #Checking Input Parameters
    @regression
    Scenario Outline: Negative flow - Send invalid fromTime and toTime
      Given I am an authorized user
      When I query for a list of transactions between <fromTime> and <toTime>
      Then I should receive a <error_response> error response with <error_description> error description and <error_code> errorcode within transaction response

    Examples:
      | fromTime                  | toTime                    | error_response | error_description | error_code |
      #### ToTime is before fromTime
      | 2019-01-02T10:00:00-05:00 | 2019-01-01T10:00:00-05:00 |                |                   |            |
      | 2019-01-02T10:00:00-05:00 | 2019-01-02T09:00:00-05:00 |                |                   |            |
      #### fromTime
      | abc                       | 2019-01-02T09:00:00-05:00 |                |                   |            |
      |                           | 2019-01-02T09:00:00-05:00 |                |                   |            |
      | 2019                      | 2019-01-02T09:00:00-05:00 |                |                   |            |
      | 2019-01-32                | 2019-01-02T09:00:00-05:00 |                |                   |            |
      | 2019-JAN-01               | 2019-01-02T09:00:00-05:00 |                |                   |            |
      | 2019-32-01                | 2019-01-02T09:00:00-05:00 |                |                   |            |
      #Invalid RF3339 format: https://validator.w3.org/feed/docs/error/InvalidRFC3339Date.html
      | 2018-10-02t10:00:00-05:00 | 2019-01-02T09:00:00-05:00 |                |                   |            |
      | 2018-10-02T10:00:00z      | 2019-01-02T09:00:00-05:00 |                |                   |            |
      | 2018-10-02T10:00:00       | 2019-01-02T09:00:00-05:00 |                |                   |            |
      #### toTime
      | 2019-02-01T00:00:00Z      | abc                       |                |                   |            |
      | 2019-02-01T00:00:00Z      |                           |                |                   |            |
      | 2019-02-01T00:00:00Z      | 2019                      |                |                   |            |
      | 2019-02-01T00:00:00Z      | 2019-01-32                |                |                   |            |
      | 2019-02-01T00:00:00Z      | 2019-JAN-01               |                |                   |            |
      | 2019-02-01T00:00:00Z      | 2019-32-01                |                |                   |            |
      #Invalid RF3339 format: https://validator.w3.org/feed/docs/error/InvalidRFC3339Date.html
      | 2002-10-02T10:00:00-05:00 | 2002-10-02t10:00:00-05:00 |                |                   |            |
      | 2002-10-02T10:00:00-05:00 | 2002-10-02T10:00:00z      |                |                   |            |
      | 2002-10-02T10:00:00-05:00 | 2002-10-02T10:00:00       |                |                   |            |

    @regression
    Scenario Outline: Negative flow - Send invalid limit
      Given I am an authorized user
      When I query for a list of transactions between <fromTime> and <toTime> with limit of <limit> transactions
      Then I should receive a <error_response> error response with <error_description> error description and <error_code> errorcode within transaction response

    Examples:
      | fromTime                  | toTime                    | limit | error_response | error_description | error_code |
      | 2019-01-01T10:00:00-05:00 | 2019-01-10T10:00:00-05:00 | -1    |                |                   |            |
      | 2019-01-01T10:00:00-05:00 | 2019-01-10T10:00:00-05:00 | 9     |                |                   |            |
      | 2019-01-01T10:00:00-05:00 | 2019-01-10T10:00:00-05:00 | 31    |                |                   |            |
      | 2019-01-01T10:00:00-05:00 | 2019-01-10T10:00:00-05:00 | abc   |                |                   |            |

    @regression
    Scenario Outline: Negative flow - Send invalid startingAfter
      Given I am an authorized user
      When I query for a list of transactions starting after <startingAfter>
      Then I should receive a <error_response> error response with <error_description> error description and <error_code> errorcode within transaction response

    Examples:
      | startingAfter                        | error_response | error_description | error_code |
      |                                      |                |                   |            |
      | a                                    |                |                   |            |
      | 123e4567-e89b-12d3-a456-42665544000  |                |                   |            |

    #Positive Flow
    @regression
    Scenario Outline: Positive flow - Get a list of transactions for a merchant
      Given I am an authorized user
      When I query for a list of transactions between <fromTime> and <toTime>
      Then I should receive a successful transaction response

    Examples:
      |fromTime                 | toTime                    |
      |2019-01-01T00:00:00+8:00 | 2019-02-01T00:00:00+08:00 |
      |2002-10-02T15:00:00Z     | 2002-10-03T15:00:00Z      |

    @regression
    Scenario Outline: Positive flow - Get a list of transactions for a merchant with limit
      Given I am an authorized user
      When I query for a list of transactions between <fromTime> and <toTime> with limit of <limit> transactions
      Then I should receive a successful transaction response
      And I should have a maximum <limit> number of transactions returned

    Examples:
      |fromTime                 | toTime                    | limit |
      |2018-01-01T00:00:00+8:00 | 2050-02-01T00:00:00+08:00 | 10    |
      |2018-01-01T00:00:00+8:00 | 2050-02-01T00:00:00+08:00 | 20    |

    @regression
    Scenario Outline: Positive flow - Get a list of transactions for a merchant with limit and use startingAfter
      Given I am an authorized user
      When I query for a list of transactions between <fromTime> and <toTime> with <limit>
      Then I should receive a successful transaction response
      And I should have at least <minimum> number of transactions returned
      When I take the firsts transaction id and make a call with startingAfter between <fromTime> and <toTime> with limit of <limit> transactions
      Then I should receive a successful transaction response
      And the returned transactions list should not have the transaction id used in the request

      Examples:
        |fromTime                 | toTime                    | limit | minimum |
        |2018-01-01T00:00:00+8:00 | 2050-02-01T00:00:00+08:00 | 10    | 2       |