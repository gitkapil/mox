@pos
Feature: DRAG-2123 - Payment request with Integrated POS role
  As a user
  I want to Make payment request with Integrated POS role

  Background: Retrieving access Token
    Given I am an user with POS role
    When I make a request to the Dragon ID Manager
    Then I receive an access_token

  @trial @regression
  Scenario Outline: Positive flow- merchant makes a payment request with Integrated-POS role using valid subUnitId
    Given I am an authorized user
    And I have payment details "<totalAmount>","<currency>","<notificationURL>","<appSuccessCallback>","<appFailCallback>","<effectiveDuration>"
    When I make a request for the payment with POS role
    Then I should receive a successful payment response
    And the response body should contain valid payment request id, business logos, created timestamp, web link, app link, totalAmount, currencyCode, statusDescription, statusCode, effectiveDuration
    And the response body should also have notification URI, app success callback URL, app fail Callback Url if applicable
    And the payment request response should be signed
    Examples:
      | totalAmount | currency | notificationURL                                           | appSuccessCallback | appFailCallback | effectiveDuration |
      | 1.81        | HKD      | https://webhook.site/cb082ee4-bdb8-4ca3-82ba-7d771365e57f | /confirmation1     | /unsuccessful9  | 15                |

  @trial @regression
  Scenario Outline: Negative flow- merchant makes a payment request with Integrated-POS role using invalid subUnitId
    Given I am an authorized user
    And I have payment details "<totalAmount>","<currency>","<notificationURL>","<appSuccessCallback>","<appFailCallback>","<effectiveDuration>"
    When I make a request for the payment with POS role using "<subUnitId>"
    Then I should receive a "<httpStatus>" error response with "<error_description>" error description and "<error_code>" errorcode within payment response
    And error message should be "<error_message>" within payment response
    Examples:
      | totalAmount | currency | notificationURL                                           | appSuccessCallback | appFailCallback | effectiveDuration | httpStatus | subUnitId                             | error_description                                                                                                                                                                  | error_code | error_message                     |
      | 1.81        | HKD      | https://webhook.site/cb082ee4-bdb8-4ca3-82ba-7d771365e57f | /confirmation1     | /unsuccessful9  | 15                | 401        | 3fa85f64-5717-4562-b3fc-2c963f66afa71 | Invalid value provided in header X-HSBC-Merchant-Id                                                                                                                                | EA001      | Unauthorized Operation!           |
      | 1.81        | HKD      | https://webhook.site/cb082ee4-bdb8-4ca3-82ba-7d771365e57f | /confirmation1     | /unsuccessful9  | 15                | 400        | randomSubUnitId                       | Failed to convert value of type 'java.lang.String' to required type 'java.util.UUID'; nested exception is java.lang.IllegalArgumentException: Invalid UUID string: randomSubUnitId | EA002      | Service Request Validation Failed |
      | 1.81        | HKD      | https://webhook.site/cb082ee4-bdb8-4ca3-82ba-7d771365e57f | /confirmation1     | /unsuccessful9  | 15                | 401        |                                       | Invalid value provided in header X-HSBC-Merchant-Id                                                                                                                                | EA001      | Unauthorized Operation!           |

  @trial @regression
  Scenario Outline: Positive flow- merchant makes a payment request with Integrated-POS role using valid subUnitId but missing optional X-HSBC-Merchant-Id header
    Given I am an authorized user
    And I have payment details "<totalAmount>","<currency>","<notificationURL>","<appSuccessCallback>","<appFailCallback>","<effectiveDuration>"
    When I make a request for the payment with POS role with missing header "<missingHeader>"
    Then I should receive a successful payment response
    And the response body should contain valid payment request id, business logos, created timestamp, web link, app link, totalAmount, currencyCode, statusDescription, statusCode, effectiveDuration
    And the response body should also have notification URI, app success callback URL, app fail Callback Url if applicable
    And the payment request response should be signed
    Examples:
      | totalAmount | currency | notificationURL                                           | appSuccessCallback | appFailCallback | effectiveDuration | missingHeader      |
      | 1.81        | HKD      | https://webhook.site/cb082ee4-bdb8-4ca3-82ba-7d771365e57f | /confirmation1     | /unsuccessful9  | 15                | X-HSBC-Merchant-Id |


  @trial @regression
  Scenario Outline: Negative flow- merchant makes a payment request with Integrated-POS role valid subUnitId but missing mandatory X-HSBC-DeviceId header
    Given I am an authorized user
    And I have payment details "<totalAmount>","<currency>","<notificationURL>","<appSuccessCallback>","<appFailCallback>","<effectiveDuration>"
    When I make a request for the payment with POS role with missing header "<missingHeader>"
    Then I should receive a "<httpStatus>" error response with "<error_description>" error description and "<error_code>" errorcode within payment response
    And error message should be "<error_message>" within payment response
    Examples:
      | totalAmount | currency | notificationURL                                           | appSuccessCallback | appFailCallback | effectiveDuration | missingHeader    | httpStatus | error_description                                             | error_code | error_message                     |
      | 1.81        | HKD      | https://webhook.site/cb082ee4-bdb8-4ca3-82ba-7d771365e57f | /confirmation1     | /unsuccessful9  | 15                | X-HSBC-Device-Id | 400        | API gateway validation failed - X-HSBC-Device-Id is mandatory | EA002      | Service Request Validation Failed |

  @trial @regression
  Scenario Outline: Negative flow- merchant makes a payment request with Integrated-POS role valid subUnitId but invalid X-HSBC-DeviceId header
    Given I am an authorized user
    And I have payment details "<totalAmount>","<currency>","<notificationURL>","<appSuccessCallback>","<appFailCallback>","<effectiveDuration>"
    When I make a request for the payment with POS role with device id "<deviceId>"
    Then I should receive a "<httpStatus>" error response with "<error_description>" error description and "<error_code>" errorcode within payment response
    And error message should be "<error_message>" within payment response
    Examples:
      | totalAmount | currency | notificationURL                                           | appSuccessCallback | appFailCallback | effectiveDuration | deviceId     | httpStatus | error_description                                                                     | error_code | error_message                     |
      | 1.81        | HKD      | https://webhook.site/cb082ee4-bdb8-4ca3-82ba-7d771365e57f | /confirmation1     | /unsuccessful9  | 15                | space        | 400        | API gateway validation failed - X-HSBC-Device-Id is mandatory                         | EA002      | Service Request Validation Failed |
      | 1.81        | HKD      | https://webhook.site/cb082ee4-bdb8-4ca3-82ba-7d771365e57f | /confirmation1     | /unsuccessful9  | 15                | doubleQuotes | 400        | API gateway validation failed - X-HSBC-Device-Id is mandatory                         | EA002      | Service Request Validation Failed |
      | 1.81        | HKD      | https://webhook.site/cb082ee4-bdb8-4ca3-82ba-7d771365e57f | /confirmation1     | /unsuccessful9  | 15                | tooLong      | 400        | API gateway validation failed - X-HSBC-Device-Id length must be less than equal to 50 | EA002      | Service Request Validation Failed |
