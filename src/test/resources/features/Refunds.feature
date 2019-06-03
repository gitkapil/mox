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
  And I enter the refund data with payerId "C731000251", refund amount "2", refund currency "HKD", reason Code "00" and reason message "test"
  Then I should receive a "<http_status>" error response with "<err_description>" error description and "<err_code>" errorcode within refund response
Examples:
  |transactionId                       |http_status|err_description                 |err_code|
  #null transactionId
  |                                    |404        |null                            |null    |
  #invalid UUID format
  |asd                                 |400        |Failed to convert value of type |EA002   |

#@trial
@regression
Scenario Outline: Negative flow - Invalid body contents
  Given I am logging in as a user with refund role
  When I try to make a call to refund with transaction id as "<transactionId>"
  And I enter the refund data with payerId "<payerId>", refund amount "<amount>", refund currency "<currencyCode>", reason Code "<reasonCode>" and reason message "<reasonMessage>"
  Then I should receive a "<http_status>" error response with "<err_description>" error description and "<err_code>" errorcode within refund response
Examples:
  |transactionId                       |payerId               |amount    |currencyCode|reasonCode|reasonMessage|http_status|err_description                  |err_code|
  # empty payer Id
  |a2d3a25a-3dea-4346-bc49-600e4b83f33e|                      |100       |HKD         |00        |test         |400        |Field error in object            |EA002   |
  # 21 length payer id
  |a2d3a25a-3dea-4346-bc49-600e4b83f33e|smallLength           |100       |HKD         |00        |test         |400        |Field error in object            |EA002   |
  # 23 length payer id
  |a2d3a25a-3dea-4346-bc49-600e4b83f33e|bigLength             |100       |HKD         |00        |test         |400        |Field error in object            |EA002   |
  # not sending payer id
  |a2d3a25a-3dea-4346-bc49-600e4b83f33e|null                  |100       |HKD         |00        |test         |400        |Field error in object            |EA002   |
  #refund amount -'ve
  |a2d3a25a-3dea-4346-bc49-600e4b83f33e|2222222222222222222222|-20       |HKD         |00        |test         |400        |Field error in object            |EA002   |
  # refund amount = 0
  |a2d3a25a-3dea-4346-bc49-600e4b83f33e|2222222222222222222222|0         |HKD         |00        |test         |400        |Field error in object            |EA002   |
  #refund amount with 3 decimal places
  |a2d3a25a-3dea-4346-bc49-600e4b83f33e|2222222222222222222222|20.123    |HKD         |00        |test         |400        |Field error in object            |EA002   |
  #refund amount = 1000000.01
  |a2d3a25a-3dea-4346-bc49-600e4b83f33e|2222222222222222222222|1000000.01|HKD         |00        |test         |400        |Field error in object            |EA002   |
  #without refund amount
  |a2d3a25a-3dea-4346-bc49-600e4b83f33e|2222222222222222222222|null      |HKD         |00        |test         |400        |Field error in object            |EA002   |
  #refund currency code hkd
  |a2d3a25a-3dea-4346-bc49-600e4b83f33e|2222222222222222222222|20        |hkd         |00        |test         |400        |Field error in object            |EA002   |
  #refund currency code usd
  |a2d3a25a-3dea-4346-bc49-600e4b83f33e|2222222222222222222222|20        |USD         |00        |test         |400        |Field error in object            |EA002   |
  #without refund currency code
  |a2d3a25a-3dea-4346-bc49-600e4b83f33e|2222222222222222222222|20        |HKD         |00        |test         |400        |Field error in object            |EA002   |
  #without reason code
  |a2d3a25a-3dea-4346-bc49-600e4b83f33e|2222222222222222222222|20        |HKD         |null      |test         |400        |Field error in object            |EA002   |
  #reason code = 000
  |a2d3a25a-3dea-4346-bc49-600e4b83f33e|2222222222222222222222|20        |HKD         |000       |test         |400        |Field error in object            |EA002   |
  #reason code = 06
  |a2d3a25a-3dea-4346-bc49-600e4b83f33e|2222222222222222222222|20        |HKD         |06        |test         |400        |Field error in object            |EA002   |
  #without reason message
  |a2d3a25a-3dea-4346-bc49-600e4b83f33e|2222222222222222222222|20        |HKD         |00        |null         |400        |Field error in object            |EA002   |
  #reason message longer than 140 characters
  |a2d3a25a-3dea-4346-bc49-600e4b83f33e|2222222222222222222222|20        |HKD         |00        |biglength    |400        |Field error in object            |EA002   |
  # reason message exists with reason code != 00
  |a2d3a25a-3dea-4346-bc49-600e4b83f33e|2222222222222222222222|20        |HKD         |82        |test         |400        |Field error in object            |EA002   |
