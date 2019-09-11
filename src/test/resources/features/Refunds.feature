Feature: Refund - DRAG- 179

  Background: Retrieving access Token
    Given I am an user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token


#@trial
#@regression
#Scenario: Negative flow - Invalid user role
#  Given I am logging in as a user with incorrect role
#  When I try to make a call to refund
#  Then I should receive a "401" error response with "Error validating JWT" error description and "401" errorcode within refund response

#@trial
  @regression
  Scenario Outline: Negative flow - Invalid transaction Id
    Given I am logging in as a user with refund role
    When I try to make a call to refund with transaction id as "<transactionId>"
    And I enter the refund data with refund amount "2", refund currency "HKD", reason Code "00" and reason message "test"
    Then I should receive a "<http_status>" error response with "<err_description>" error description and "<err_code>" errorcode within refund response
    Examples:
      | transactionId | http_status | err_description                 | err_code |
  #null transactionId
      |               | 404         | null                            | null     |
  #invalid UUID format
      | asd           | 400         | Failed to convert value of type | EA002    |

# DRAG-1812 removed payerId
  @regression
  Scenario Outline: Negative flow - Invalid body contents
    Given I am logging in as a user with refund role
    When I call for a list of transactions
    And I record the first transaction details
    And I try to make a call to refund with that transaction
    And I enter the refund data with refund amount "<amount>", refund currency "<currencyCode>", reason Code "<reasonCode>" and reason message "<reasonMessage>"
    Then I should receive a "<http_status>" error response with "<err_description>" error description and "<err_code>" errorcode within refund response
    Examples:
      | amount     | currencyCode | reasonCode | reasonMessage | http_status | err_description                                                                                                                               | err_code |
  #refund amount -'ve
      | -20        | HKD          | 00         | test          | 400         | Field error in object 'refundInputModel': field 'amount' must be greater than or equal to 0.01; rejected value [-20.0]                        | EA018    |
  # refund amount = 0
      | 0          | HKD          | 00         | test          | 400         | Field error in object 'refundInputModel': field 'amount' must be greater than or equal to 0.01; rejected value [0.0]                          | EA018    |
  #refund amount with 3 decimal places
      | 1.123      | HKD          | 00         | test          | 400         | Field error in object 'refundInputModel': field 'amount' numeric value out of bounds (<7 digits>.<2 digits> expected); rejected value [1.123] | EA018    |
  #refund amount = 1000000.01
      | 1000000.01 | HKD          | 00         | test          | 400         | Field error in object 'refundInputModel': field 'amount' must be less than or equal to 1000000; rejected value [1000000.01]                   | EA018    |
  #without refund amount
      | null       | HKD          | 00         | test          | 400         | Field error in object 'refundInputModel': field 'amount' may not be null; rejected value [null]                                              | EA018    |
  #refund currency code usd
      | 1          | USD          | 00         | test          | 400         | Invalid currency code                                                                                                                         | EA014    |
  #without refund currency code
      | 1          | null         | 00         | test          | 400         | Field error in object 'refundInputModel': field 'currencyCode' may not be null; rejected value [null]                                        | EA014    |
  #without reason code
      | 1          | HKD          | null       | test          | 400         | Field error in object 'refundInputModel': field 'reasonCode' may not be null; rejected value [null]                                          | EA034    |
  #reason code = 000
      | 1          | HKD          | 000        | test          | 400         | Field error in object 'refundInputModel': field 'reasonCode' may not be null; rejected value [null]                                          | EA034    |
  #reason code = 06
      | 1          | HKD          | 06         | test          | 400         | Field error in object 'refundInputModel': field 'reasonCode' may not be null; rejected value [null]                                          | EA034    |
  #reason message longer than 140 characters
      | 1          | HKD          | 00         | biglength     | 400         | Field error in object                                                                                                                         | EA002    |
  # reason message exists with reason code != 00
      | 1          | HKD          | 82         | test          | 400         | Field error in object 'refundInputModel': field 'reasonCode' may      not be null; rejected value [null]                                          | EA034    |
