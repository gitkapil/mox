Feature: Merchant Management API - GET applications

  Background: Retrieving access Token
    Given I am an authorized CSO user
    When I make a request to the Dragon ID Manager
    Then I receive an access_token
    And I then clean and create 30 test applications

  @regression @merchantManagement @merchantManagementUpdateOne
  Scenario Outline: Positive flow - Update an application
    Given I am an authorized CSO user
    And created an application with "<clientId>", "<userId>", "<subUnitId>" and "<organisationId>"
    And I kept the application ID
    And I want to update my application details "<notificationPath>", "<notificationHost>", "<clientId>", "<userId>", "<subUnitId>", "<organisationId>"
    When I update an application with the kept application ID
    Then I should receive a successful response
    And applicationId in response body should match "<applicationId>"
    And notificationPath in response body should match "<notificationPath>"
    And notificationHost in response body should match "<notificationHost>"
    And clientId in response body should match "<clientId>"
    And userId in response body should match "<userId>"
    And subUnitId in response body should match "<subUnitId>"
    And organisationId in response body should match "<organisationId>"

    Examples:
    |notificationPath|notificationHost|clientId|userId|subUnitId|organisationId|
    |/payme/notifcations|myshop.com.hk|3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|

  @regression @merchantManagement @merchantManagementUpdateOne
  Scenario Outline: Positive flow - Update an application, notificationPath only
    Given I am an authorized CSO user
    And created an application with "<clientId>", "<userId>", "<subUnitId>" and "<organisationId>"
    And I keep the application ID
    And I want to update my application notificationPath "<newNotificationPath>"
    When I update an application with the kept application ID
    Then I should receive a successful response
    And applicationId in response body should match application ID I kept
    And notificationPath in response body should match "<newNotificationPath>"
    And notificationHost in response body should match ""
    And clientId in response body should match "<clientId>"
    And userId in response body should match "<userId>"
    And subUnitId in response body should match "<subUnitId>"
    And organisationId in response body should match "<organisationId>"

    Examples:
      |newNotificationPath|clientId|userId|subUnitId|organisationId|
      |/payme/notifcations/new|3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|

  @regression @merchantManagement @merchantManagementUpdateOne
  Scenario Outline: Positive flow - Update an application, notificationHost only
    Given I am an authorized CSO user
    And created an application with "<clientId>", "<userId>", "<subUnitId>" and "<organisationId>"
    And I keep the application ID
    And I want to update my application newNotificationHost "<newNotificationHost>"
    When I update an application with the kept application ID
    Then I should receive a successful response
    And applicationId in response body should match application ID I kept
    And notificationPath in response body should match ""
    And notificationHost in response body should match "<newNotificationHost>"
    And clientId in response body should match "<clientId>"
    And userId in response body should match "<userId>"
    And subUnitId in response body should match "<subUnitId>"
    And organisationId in response body should match "<organisationId>"

    Examples:
      |newNotificationHost|clientId|userId|subUnitId|organisationId|
      |newShop.com.hk|3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|

  @regression @merchantManagement @merchantManagementUpdateOne
  Scenario Outline: Positive flow - Update an application, clientId only
    Given I am an authorized CSO user
    And created an application with "<clientId>", "<userId>", "<subUnitId>" and "<organisationId>"
    And I keep the application ID
    And I want to update my application clientId "<newClientId>"
    When I update an application with the kept application ID
    Then I should receive a successful response
    And applicationId in response body should match application ID I kept
    And notificationPath in response body should match ""
    And notificationHost in response body should match ""
    And clientId in response body should match "<newClientId>"
    And userId in response body should match "<userId>"
    And subUnitId in response body should match "<subUnitId>"
    And organisationId in response body should match "<organisationId>"

    Examples:
      |newClientId|clientId|userId|subUnitId|organisationId|
      |3fa85f64-5717-4562-b3fc-ffffffffffff|3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|

  @regression @merchantManagement @merchantManagementUpdateOne
  Scenario Outline: Positive flow - Update an application, userId only
    Given I am an authorized CSO user
    And created an application with "<clientId>", "<userId>", "<subUnitId>" and "<organisationId>"
    And I keep the application ID
    And I want to update my application userId "<newUserId>"
    When I update an application with the kept application ID
    Then I should receive a successful response
    And applicationId in response body should match application ID I kept
    And notificationPath in response body should match ""
    And notificationHost in response body should match ""
    And clientId in response body should match "<clientId>"
    And userId in response body should match "<newUserId>"
    And subUnitId in response body should match "<subUnitId>"
    And organisationId in response body should match "<organisationId>"

    Examples:
      |newUserId|clientId|userId|subUnitId|organisationId|
      |3fa85f64-5717-4562-b3fc-ffffffffffff|3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|

  @regression @merchantManagement @merchantManagementUpdateOne
  Scenario Outline: Positive flow - Update an application, subUnitId only
    Given I am an authorized CSO user
    And created an application with "<clientId>", "<userId>", "<subUnitId>" and "<organisationId>"
    And I keep the application ID
    And I want to update my application subUnitId "<newSubUnitId>"
    When I update an application with the kept application ID
    Then I should receive a successful response
    And applicationId in response body should match application ID I kept
    And notificationPath in response body should match ""
    And notificationHost in response body should match ""
    And clientId in response body should match "<clientId>"
    And userId in response body should match "<userId>"
    And subUnitId in response body should match "<newSubUnitId>"
    And organisationId in response body should match "<organisationId>"

    Examples:
      |newSubUnitId|clientId|userId|subUnitId|organisationId|
      |3fa85f64-5717-4562-b3fc-ffffffffffff|3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|

  @regression @merchantManagement @merchantManagementUpdateOne
  Scenario Outline: Positive flow - Update an application, organisationId only
    Given I am an authorized CSO user
    And created an application with "<clientId>", "<userId>", "<subUnitId>" and "<organisationId>"
    And I keep the application ID
    And I want to update my application organisationId "<newOrganisationId>"
    When I update an application with the kept application ID
    Then I should receive a successful response
    And applicationId in response body should match application ID I kept
    And notificationPath in response body should match ""
    And notificationHost in response body should match ""
    And clientId in response body should match "<clientId>"
    And userId in response body should match "<userId>"
    And subUnitId in response body should match "<subUnitId>"
    And organisationId in response body should match "<newOrganisationId>"

    Examples:
      |newOrganisationId|clientId|userId|subUnitId|organisationId|
      |3fa85f64-5717-4562-b3fc-ffffffffffff|3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|

  @regression @merchantManagement @merchantManagementUpdateOne
  Scenario Outline: Negative flow - Update an application, notificationPath length > 1024
    Given I am an authorized CSO user
    And created an application with "<clientId>", "<userId>", "<subUnitId>" and "<organisationId>"
    And I keep the application ID
    And I want to update my application notificationPath "<newNotificationPath>"
    When I update an application with the kept application ID
    Then I should receive a "400" error response with "<errorDescription>" error description and "<errorCode>" error code

    Examples:
    |newNotificationPath|errorCode|errorDescription|
    |superlongpaththatyoucantimaginesuperlongpaththatyoucantimaginesuperlongpaththatyoucantimaginesuperlongpaththatyoucantimaginesuperlongpaththatyoucantimaginesuperlongpaththatyoucantimaginesuperlongpaththatyoucantimaginesuperlongpaththatyoucantimaginesuperlongpaththatyoucantimaginesuperlongpaththatyoucantimaginesuperlongpaththatyoucantimaginesuperlongpaththatyoucantimaginesuperlongpaththatyoucantimaginesuperlongpaththatyoucantimaginesuperlongpaththatyoucantimaginesuperlongpaththatyoucantimaginesuperlongpaththatyoucantimaginesuperlongpaththatyoucantimaginesuperlongpaththatyoucantimaginesuperlongpaththatyoucantimaginesuperlongpaththatyoucantimaginesuperlongpaththatyoucantimaginesuperlongpaththatyoucantimaginesuperlongpaththatyoucantimaginesuperlongpaththatyoucantimaginesuperlongpaththatyoucantimaginesuperlongpaththatyoucantimaginesuperlongpaththatyoucantimaginesuperlongpaththatyoucantimaginesuperlongpaththatyoucantimaginesuperlongpaththatyoucantimaginesuperlongpaththatyoucantimaginesuperlongpaththatyoucantimaginesuperlongpaththatyoucantimagine/|EA002|Server request validation failed|

  @regression @merchantManagement @merchantManagementUpdateOne
  Scenario Outline: Negative flow - Update an application, notificationHost length > 255
    Given I am an authorized CSO user
    And created an application with "<clientId>", "<userId>", "<subUnitId>" and "<organisationId>"
    And I keep the application ID
    And I want to update my application notificationHost "<newNotificationHost>"
    When I update an application with the kept application ID
    Then I should receive a "400" error response with "<errorDescription>" error description and "<errorCode>" error code

    Examples:
    |newNotificationHost|errorCode|errorDescription|
    |nonreasonablehostnamenonreasonablehostnamenonreasonablehostnamenonreasonablehostnamenonreasonablehostnamenonreasonablehostnamenonreasonablehostnamenonreasonablehostnamenonreasonablehostnamenonreasonablehostnamenonreasonablehostnamenonreasonablehostnamenonreasonablehostname|EA002|Server request validation failed|

  @regression @merchantManagement @merchantManagementUpdateOne
  Scenario Outline: Positive flow - Update an application, clientId incorrect format
    Given I am an authorized CSO user
    And created an application with "<clientId>", "<userId>", "<subUnitId>" and "<organisationId>"
    And I keep the application ID
    And I want to update my application clientId "<newClientId>"
    When I update an application with the kept application ID
    Then I should receive a "400" error response with "<errorDescription>" error description and "<errorCode>" error code

    Examples:
      |newClientId|clientId|userId|subUnitId|organisationId|
      |3fa85f64-5717-4562-b3fc-xxxxyyyyzzzz|3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|

  @regression @merchantManagement @merchantManagementUpdateOne
  Scenario Outline: Positive flow - Update an application, userId incorrect format
    Given I am an authorized CSO user
    And created an application with "<clientId>", "<userId>", "<subUnitId>" and "<organisationId>"
    And I keep the application ID
    And I want to update my application userId "<newUserId>"
    When I update an application with the kept application ID
    Then I should receive a "400" error response with "<errorDescription>" error description and "<errorCode>" error code

    Examples:
      |newUserId|clientId|userId|subUnitId|organisationId|
      |3fa85f64-5717-4562-b3fc-xxxxyyyyzzzz|3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|

  @regression @merchantManagement @merchantManagementUpdateOne
  Scenario Outline: Positive flow - Update an application, subUnitId incorrect format
    Given I am an authorized CSO user
    And created an application with "<clientId>", "<userId>", "<subUnitId>" and "<organisationId>"
    And I keep the application ID
    And I want to update my application subUnitId "<newSubUnitId>"
    When I update an application with the kept application ID
    Then I should receive a "400" error response with "<errorDescription>" error description and "<errorCode>" error code

    Examples:
      |newSubUnitId|clientId|userId|subUnitId|organisationId|
      |3fa85f64-5717-4562-b3fc-xxxxyyyyzzzz|3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|

  @regression @merchantManagement @merchantManagementUpdateOne
  Scenario Outline: Positive flow - Update an application, organisationId incorrect format
    Given I am an authorized CSO user
    And created an application with "<clientId>", "<userId>", "<subUnitId>" and "<organisationId>"
    And I keep the application ID
    And I want to update my application organisationId "<newOrganisationId>"
    When I update an application with the kept application ID
    Then I should receive a "400" error response with "<errorDescription>" error description and "<errorCode>" error code

    Examples:
      |newOrganisationId|clientId|userId|subUnitId|organisationId|
      |3fa85f64-5717-4562-b3fc-xxxxyyyyzzzz|3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|3fa85f64-5717-4562-b3fc-2c963f66afa6|
