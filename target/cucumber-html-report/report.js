$(document).ready(function() {var formatter = new CucumberHTML.DOMFormatter($('.cucumber-report'));formatter.uri("PaymentRequest.feature");
formatter.feature({
  "line": 1,
  "name": "Payment Request - DRAG-187",
  "description": "",
  "id": "payment-request---drag-187",
  "keyword": "Feature"
});
formatter.scenarioOutline({
  "line": 10,
  "name": "Positive flow- A merchant is able to create a payment request with all the valid inputs",
  "description": "",
  "id": "payment-request---drag-187;positive-flow--a-merchant-is-able-to-create-a-payment-request-with-all-the-valid-inputs",
  "type": "scenario_outline",
  "keyword": "Scenario Outline",
  "tags": [
    {
      "line": 9,
      "name": "@functional"
    },
    {
      "line": 9,
      "name": "@trial"
    }
  ]
});
formatter.step({
  "line": 11,
  "name": "I am an authorized merchant",
  "keyword": "Given "
});
formatter.step({
  "line": 12,
  "name": "I have transaction details \"\u003ctraceid\u003e\", \"\u003camount\u003e\",\"\u003ccurrency\u003e\",\"\u003cdescription\u003e\",\"\u003cchannel\u003e\",\"\u003cinvoiceid\u003e\",\"\u003cmerchantid\u003e\",\"\u003ceffectiveduration\u003e\",\"\u003creturnURL\u003e\"",
  "keyword": "And "
});
formatter.step({
  "line": 13,
  "name": "I make a request for the payment",
  "keyword": "When "
});
formatter.step({
  "line": 14,
  "name": "I should recieve a payment response with valid trace id in the header",
  "keyword": "Then "
});
formatter.step({
  "line": 15,
  "name": "the response body should contain valid payment id, created timestamp, transaction details, links",
  "keyword": "And "
});
formatter.examples({
  "line": 17,
  "name": "",
  "description": "",
  "id": "payment-request---drag-187;positive-flow--a-merchant-is-able-to-create-a-payment-request-with-all-the-valid-inputs;",
  "rows": [
    {
      "cells": [
        "traceid",
        "amount",
        "currency",
        "description",
        "channel",
        "invoiceid",
        "merchantid",
        "effectiveduration",
        "returnURL"
      ],
      "line": 18,
      "id": "payment-request---drag-187;positive-flow--a-merchant-is-able-to-create-a-payment-request-with-all-the-valid-inputs;;1"
    },
    {
      "cells": [
        "2c350a50-7ae0-4191-acc7-4420b74ed52d",
        "20.00",
        "HKD",
        "Pizza order1",
        "Ecommerce",
        "48787589673",
        "Pizzahut1239893993",
        "55",
        "https://pizzahut.com/return"
      ],
      "line": 19,
      "id": "payment-request---drag-187;positive-flow--a-merchant-is-able-to-create-a-payment-request-with-all-the-valid-inputs;;2"
    },
    {
      "cells": [
        "2c350a50-7ae0-4191-acc7-4420b74ed53d",
        "10000",
        "HKD",
        "",
        "Mcommerce",
        "48787589674",
        "Pizzahut1239893993",
        "10",
        "https://pizzahut.com/return"
      ],
      "line": 20,
      "id": "payment-request---drag-187;positive-flow--a-merchant-is-able-to-create-a-payment-request-with-all-the-valid-inputs;;3"
    },
    {
      "cells": [
        "2c350a50-7ae0-4191-acc7-4420b74ed54d",
        "89.09",
        "HKD",
        "Pizza order2",
        "",
        "48787589675",
        "Pizzahut1239893993",
        "30",
        "https://pizzahut.com/return"
      ],
      "line": 21,
      "id": "payment-request---drag-187;positive-flow--a-merchant-is-able-to-create-a-payment-request-with-all-the-valid-inputs;;4"
    },
    {
      "cells": [
        "2c350a50-7ae0-4191-acc7-4420b74ed55d",
        "0.044",
        "HKD",
        "Pizza order3",
        "Native",
        "",
        "Pizzahut1239893993",
        "30",
        "https://pizzahut.com/return"
      ],
      "line": 22,
      "id": "payment-request---drag-187;positive-flow--a-merchant-is-able-to-create-a-payment-request-with-all-the-valid-inputs;;5"
    },
    {
      "cells": [
        "2c350a50-7ae0-4191-acc7-4420b74ed56d",
        "2.00",
        "HKD",
        "Pizza order4",
        "Ecommerce",
        "48787589677",
        "Pizzahut1239893993",
        "",
        "https://pizzahut.com/return"
      ],
      "line": 23,
      "id": "payment-request---drag-187;positive-flow--a-merchant-is-able-to-create-a-payment-request-with-all-the-valid-inputs;;6"
    },
    {
      "cells": [
        "2c350a50-7ae0-4191-acc7-4420b74ed57d",
        "3",
        "HKD",
        "Pizza order5",
        "Mcommerce",
        "48787589678",
        "Pizzahut1239893993",
        "30",
        "https://pizzahut.com/return"
      ],
      "line": 24,
      "id": "payment-request---drag-187;positive-flow--a-merchant-is-able-to-create-a-payment-request-with-all-the-valid-inputs;;7"
    },
    {
      "cells": [
        "2c350a50-7ae0-4191-acc7-4420b74ed58d",
        "600.0",
        "HKD",
        "Pizza order6",
        "Ecommerce",
        "48787589679",
        "Pizzahut1239893993",
        "30",
        "https://pizzahut.com/return"
      ],
      "line": 25,
      "id": "payment-request---drag-187;positive-flow--a-merchant-is-able-to-create-a-payment-request-with-all-the-valid-inputs;;8"
    },
    {
      "cells": [
        "2c350a50-7ae0-4191-acc7-4420b74ed59d",
        "12.123",
        "USD",
        "Pizza order7",
        "Ecommerce",
        "48787589611",
        "Pizzahut1239893993",
        "30",
        "https://pizzahut.com/return"
      ],
      "line": 26,
      "id": "payment-request---drag-187;positive-flow--a-merchant-is-able-to-create-a-payment-request-with-all-the-valid-inputs;;9"
    },
    {
      "cells": [
        "2c350a50-7ae0-4191-acc7-4420b74ed60d",
        "12.13",
        "USD",
        "Pizza order8",
        "Native",
        "ABCD",
        "Pizzahut1239893993",
        "30",
        "https://pizzahut.com/return"
      ],
      "line": 27,
      "id": "payment-request---drag-187;positive-flow--a-merchant-is-able-to-create-a-payment-request-with-all-the-valid-inputs;;10"
    }
  ],
  "keyword": "Examples"
});
formatter.before({
  "duration": 447696527,
  "status": "passed"
});
formatter.background({
  "line": 3,
  "name": "Retrieving access Token",
  "description": "",
  "type": "background",
  "keyword": "Background"
});
formatter.step({
  "line": 4,
  "name": "I am a merchant",
  "keyword": "Given "
});
formatter.step({
  "line": 5,
  "name": "I make a request to the Dragon ID Manager",
  "keyword": "When "
});
formatter.step({
  "line": 6,
  "name": "I recieve an access_token",
  "keyword": "Then "
});
formatter.match({
  "location": "AccessTokenForMerchants_StepDefs.i_am_a_merchant()"
});
formatter.result({
  "duration": 85497678,
  "status": "passed"
});
formatter.match({
  "location": "AccessTokenForMerchants_StepDefs.i_make_a_request_to_the_Dragon_ID_Manager()"
});
formatter.result({
  "duration": 2005018257,
  "status": "passed"
});
formatter.match({
  "location": "AccessTokenForMerchants_StepDefs.i_recieve_a_valid_access_token()"
});
formatter.result({
  "duration": 461594781,
  "status": "passed"
});
formatter.scenario({
  "line": 19,
  "name": "Positive flow- A merchant is able to create a payment request with all the valid inputs",
  "description": "",
  "id": "payment-request---drag-187;positive-flow--a-merchant-is-able-to-create-a-payment-request-with-all-the-valid-inputs;;2",
  "type": "scenario",
  "keyword": "Scenario Outline",
  "tags": [
    {
      "line": 9,
      "name": "@trial"
    },
    {
      "line": 9,
      "name": "@functional"
    }
  ]
});
formatter.step({
  "line": 11,
  "name": "I am an authorized merchant",
  "keyword": "Given "
});
formatter.step({
  "line": 12,
  "name": "I have transaction details \"2c350a50-7ae0-4191-acc7-4420b74ed52d\", \"20.00\",\"HKD\",\"Pizza order1\",\"Ecommerce\",\"48787589673\",\"Pizzahut1239893993\",\"55\",\"https://pizzahut.com/return\"",
  "matchedColumns": [
    0,
    1,
    2,
    3,
    4,
    5,
    6,
    7,
    8
  ],
  "keyword": "And "
});
formatter.step({
  "line": 13,
  "name": "I make a request for the payment",
  "keyword": "When "
});
formatter.step({
  "line": 14,
  "name": "I should recieve a payment response with valid trace id in the header",
  "keyword": "Then "
});
formatter.step({
  "line": 15,
  "name": "the response body should contain valid payment id, created timestamp, transaction details, links",
  "keyword": "And "
});
formatter.match({
  "location": "PaymentRequest_StepDefs.i_am_an_authorized_merchant()"
});
formatter.result({
  "duration": 39675988,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "2c350a50-7ae0-4191-acc7-4420b74ed52d",
      "offset": 28
    },
    {
      "val": "20.00",
      "offset": 68
    },
    {
      "val": "HKD",
      "offset": 76
    },
    {
      "val": "Pizza order1",
      "offset": 82
    },
    {
      "val": "Ecommerce",
      "offset": 97
    },
    {
      "val": "48787589673",
      "offset": 109
    },
    {
      "val": "Pizzahut1239893993",
      "offset": 123
    },
    {
      "val": "55",
      "offset": 144
    },
    {
      "val": "https://pizzahut.com/return",
      "offset": 149
    }
  ],
  "location": "PaymentRequest_StepDefs.i_have_valid_transaction_details(String,String,String,String,String,String,String,String,String)"
});
formatter.result({
  "duration": 3588753,
  "status": "passed"
});
formatter.match({
  "location": "PaymentRequest_StepDefs.i_make_a_request_for_the_payment()"
});
formatter.result({
  "duration": 1502792868,
  "status": "passed"
});
formatter.match({
  "location": "PaymentRequest_StepDefs.i_should_recieve_a_response_with_valid_trace_id_in_the_header()"
});
formatter.result({
  "duration": 3259103,
  "status": "passed"
});
formatter.match({
  "location": "PaymentRequest_StepDefs.the_response_body_should_contain_valid_payment_id_created_timestamp_transaction_details_links()"
});
formatter.result({
  "duration": 41570684,
  "error_message": "java.lang.AssertionError: Links within response is either incomplete or incorrect..Please check!! expected [true] but found [false]\n\tat org.testng.Assert.fail(Assert.java:94)\n\tat org.testng.Assert.failNotEquals(Assert.java:513)\n\tat org.testng.Assert.assertTrue(Assert.java:42)\n\tat steps.PaymentRequest_StepDefs.the_response_body_should_contain_valid_payment_id_created_timestamp_transaction_details_links(PaymentRequest_StepDefs.java:75)\n\tat ✽.And the response body should contain valid payment id, created timestamp, transaction details, links(PaymentRequest.feature:15)\n",
  "status": "failed"
});
formatter.before({
  "duration": 256825,
  "status": "passed"
});
formatter.background({
  "line": 3,
  "name": "Retrieving access Token",
  "description": "",
  "type": "background",
  "keyword": "Background"
});
formatter.step({
  "line": 4,
  "name": "I am a merchant",
  "keyword": "Given "
});
formatter.step({
  "line": 5,
  "name": "I make a request to the Dragon ID Manager",
  "keyword": "When "
});
formatter.step({
  "line": 6,
  "name": "I recieve an access_token",
  "keyword": "Then "
});
formatter.match({
  "location": "AccessTokenForMerchants_StepDefs.i_am_a_merchant()"
});
formatter.result({
  "duration": 43547,
  "status": "passed"
});
formatter.match({
  "location": "AccessTokenForMerchants_StepDefs.i_make_a_request_to_the_Dragon_ID_Manager()"
});
formatter.result({
  "duration": 595150886,
  "status": "passed"
});
formatter.match({
  "location": "AccessTokenForMerchants_StepDefs.i_recieve_a_valid_access_token()"
});
formatter.result({
  "duration": 17550924,
  "status": "passed"
});
formatter.scenario({
  "line": 20,
  "name": "Positive flow- A merchant is able to create a payment request with all the valid inputs",
  "description": "",
  "id": "payment-request---drag-187;positive-flow--a-merchant-is-able-to-create-a-payment-request-with-all-the-valid-inputs;;3",
  "type": "scenario",
  "keyword": "Scenario Outline",
  "tags": [
    {
      "line": 9,
      "name": "@trial"
    },
    {
      "line": 9,
      "name": "@functional"
    }
  ]
});
formatter.step({
  "line": 11,
  "name": "I am an authorized merchant",
  "keyword": "Given "
});
formatter.step({
  "line": 12,
  "name": "I have transaction details \"2c350a50-7ae0-4191-acc7-4420b74ed53d\", \"10000\",\"HKD\",\"\",\"Mcommerce\",\"48787589674\",\"Pizzahut1239893993\",\"10\",\"https://pizzahut.com/return\"",
  "matchedColumns": [
    0,
    1,
    2,
    3,
    4,
    5,
    6,
    7,
    8
  ],
  "keyword": "And "
});
formatter.step({
  "line": 13,
  "name": "I make a request for the payment",
  "keyword": "When "
});
formatter.step({
  "line": 14,
  "name": "I should recieve a payment response with valid trace id in the header",
  "keyword": "Then "
});
formatter.step({
  "line": 15,
  "name": "the response body should contain valid payment id, created timestamp, transaction details, links",
  "keyword": "And "
});
formatter.match({
  "location": "PaymentRequest_StepDefs.i_am_an_authorized_merchant()"
});
formatter.result({
  "duration": 30187168,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "2c350a50-7ae0-4191-acc7-4420b74ed53d",
      "offset": 28
    },
    {
      "val": "10000",
      "offset": 68
    },
    {
      "val": "HKD",
      "offset": 76
    },
    {
      "val": "",
      "offset": 82
    },
    {
      "val": "Mcommerce",
      "offset": 85
    },
    {
      "val": "48787589674",
      "offset": 97
    },
    {
      "val": "Pizzahut1239893993",
      "offset": 111
    },
    {
      "val": "10",
      "offset": 132
    },
    {
      "val": "https://pizzahut.com/return",
      "offset": 137
    }
  ],
  "location": "PaymentRequest_StepDefs.i_have_valid_transaction_details(String,String,String,String,String,String,String,String,String)"
});
formatter.result({
  "duration": 207220,
  "status": "passed"
});
formatter.match({
  "location": "PaymentRequest_StepDefs.i_make_a_request_for_the_payment()"
});
formatter.result({
  "duration": 838181329,
  "status": "passed"
});
formatter.match({
  "location": "PaymentRequest_StepDefs.i_should_recieve_a_response_with_valid_trace_id_in_the_header()"
});
formatter.result({
  "duration": 38928,
  "status": "passed"
});
formatter.match({
  "location": "PaymentRequest_StepDefs.the_response_body_should_contain_valid_payment_id_created_timestamp_transaction_details_links()"
});
formatter.result({
  "duration": 29223843,
  "error_message": "java.lang.AssertionError: Links within response is either incomplete or incorrect..Please check!! expected [true] but found [false]\n\tat org.testng.Assert.fail(Assert.java:94)\n\tat org.testng.Assert.failNotEquals(Assert.java:513)\n\tat org.testng.Assert.assertTrue(Assert.java:42)\n\tat steps.PaymentRequest_StepDefs.the_response_body_should_contain_valid_payment_id_created_timestamp_transaction_details_links(PaymentRequest_StepDefs.java:75)\n\tat ✽.And the response body should contain valid payment id, created timestamp, transaction details, links(PaymentRequest.feature:15)\n",
  "status": "failed"
});
formatter.before({
  "duration": 136640,
  "status": "passed"
});
formatter.background({
  "line": 3,
  "name": "Retrieving access Token",
  "description": "",
  "type": "background",
  "keyword": "Background"
});
formatter.step({
  "line": 4,
  "name": "I am a merchant",
  "keyword": "Given "
});
formatter.step({
  "line": 5,
  "name": "I make a request to the Dragon ID Manager",
  "keyword": "When "
});
formatter.step({
  "line": 6,
  "name": "I recieve an access_token",
  "keyword": "Then "
});
formatter.match({
  "location": "AccessTokenForMerchants_StepDefs.i_am_a_merchant()"
});
formatter.result({
  "duration": 27347,
  "status": "passed"
});
formatter.match({
  "location": "AccessTokenForMerchants_StepDefs.i_make_a_request_to_the_Dragon_ID_Manager()"
});
formatter.result({
  "duration": 707185862,
  "status": "passed"
});
formatter.match({
  "location": "AccessTokenForMerchants_StepDefs.i_recieve_a_valid_access_token()"
});
formatter.result({
  "duration": 14116693,
  "status": "passed"
});
formatter.scenario({
  "line": 21,
  "name": "Positive flow- A merchant is able to create a payment request with all the valid inputs",
  "description": "",
  "id": "payment-request---drag-187;positive-flow--a-merchant-is-able-to-create-a-payment-request-with-all-the-valid-inputs;;4",
  "type": "scenario",
  "keyword": "Scenario Outline",
  "tags": [
    {
      "line": 9,
      "name": "@trial"
    },
    {
      "line": 9,
      "name": "@functional"
    }
  ]
});
formatter.step({
  "line": 11,
  "name": "I am an authorized merchant",
  "keyword": "Given "
});
formatter.step({
  "line": 12,
  "name": "I have transaction details \"2c350a50-7ae0-4191-acc7-4420b74ed54d\", \"89.09\",\"HKD\",\"Pizza order2\",\"\",\"48787589675\",\"Pizzahut1239893993\",\"30\",\"https://pizzahut.com/return\"",
  "matchedColumns": [
    0,
    1,
    2,
    3,
    4,
    5,
    6,
    7,
    8
  ],
  "keyword": "And "
});
formatter.step({
  "line": 13,
  "name": "I make a request for the payment",
  "keyword": "When "
});
formatter.step({
  "line": 14,
  "name": "I should recieve a payment response with valid trace id in the header",
  "keyword": "Then "
});
formatter.step({
  "line": 15,
  "name": "the response body should contain valid payment id, created timestamp, transaction details, links",
  "keyword": "And "
});
formatter.match({
  "location": "PaymentRequest_StepDefs.i_am_an_authorized_merchant()"
});
formatter.result({
  "duration": 27248740,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "2c350a50-7ae0-4191-acc7-4420b74ed54d",
      "offset": 28
    },
    {
      "val": "89.09",
      "offset": 68
    },
    {
      "val": "HKD",
      "offset": 76
    },
    {
      "val": "Pizza order2",
      "offset": 82
    },
    {
      "val": "",
      "offset": 97
    },
    {
      "val": "48787589675",
      "offset": 100
    },
    {
      "val": "Pizzahut1239893993",
      "offset": 114
    },
    {
      "val": "30",
      "offset": 135
    },
    {
      "val": "https://pizzahut.com/return",
      "offset": 140
    }
  ],
  "location": "PaymentRequest_StepDefs.i_have_valid_transaction_details(String,String,String,String,String,String,String,String,String)"
});
formatter.result({
  "duration": 171904,
  "status": "passed"
});
formatter.match({
  "location": "PaymentRequest_StepDefs.i_make_a_request_for_the_payment()"
});
formatter.result({
  "duration": 836568624,
  "status": "passed"
});
formatter.match({
  "location": "PaymentRequest_StepDefs.i_should_recieve_a_response_with_valid_trace_id_in_the_header()"
});
formatter.result({
  "duration": 36617,
  "status": "passed"
});
formatter.match({
  "location": "PaymentRequest_StepDefs.the_response_body_should_contain_valid_payment_id_created_timestamp_transaction_details_links()"
});
formatter.result({
  "duration": 24880918,
  "error_message": "java.lang.AssertionError: Links within response is either incomplete or incorrect..Please check!! expected [true] but found [false]\n\tat org.testng.Assert.fail(Assert.java:94)\n\tat org.testng.Assert.failNotEquals(Assert.java:513)\n\tat org.testng.Assert.assertTrue(Assert.java:42)\n\tat steps.PaymentRequest_StepDefs.the_response_body_should_contain_valid_payment_id_created_timestamp_transaction_details_links(PaymentRequest_StepDefs.java:75)\n\tat ✽.And the response body should contain valid payment id, created timestamp, transaction details, links(PaymentRequest.feature:15)\n",
  "status": "failed"
});
formatter.before({
  "duration": 125030,
  "status": "passed"
});
formatter.background({
  "line": 3,
  "name": "Retrieving access Token",
  "description": "",
  "type": "background",
  "keyword": "Background"
});
formatter.step({
  "line": 4,
  "name": "I am a merchant",
  "keyword": "Given "
});
formatter.step({
  "line": 5,
  "name": "I make a request to the Dragon ID Manager",
  "keyword": "When "
});
formatter.step({
  "line": 6,
  "name": "I recieve an access_token",
  "keyword": "Then "
});
formatter.match({
  "location": "AccessTokenForMerchants_StepDefs.i_am_a_merchant()"
});
formatter.result({
  "duration": 31094,
  "status": "passed"
});
formatter.match({
  "location": "AccessTokenForMerchants_StepDefs.i_make_a_request_to_the_Dragon_ID_Manager()"
});
formatter.result({
  "duration": 588455996,
  "status": "passed"
});
formatter.match({
  "location": "AccessTokenForMerchants_StepDefs.i_recieve_a_valid_access_token()"
});
formatter.result({
  "duration": 12335898,
  "status": "passed"
});
formatter.scenario({
  "line": 22,
  "name": "Positive flow- A merchant is able to create a payment request with all the valid inputs",
  "description": "",
  "id": "payment-request---drag-187;positive-flow--a-merchant-is-able-to-create-a-payment-request-with-all-the-valid-inputs;;5",
  "type": "scenario",
  "keyword": "Scenario Outline",
  "tags": [
    {
      "line": 9,
      "name": "@trial"
    },
    {
      "line": 9,
      "name": "@functional"
    }
  ]
});
formatter.step({
  "line": 11,
  "name": "I am an authorized merchant",
  "keyword": "Given "
});
formatter.step({
  "line": 12,
  "name": "I have transaction details \"2c350a50-7ae0-4191-acc7-4420b74ed55d\", \"0.044\",\"HKD\",\"Pizza order3\",\"Native\",\"\",\"Pizzahut1239893993\",\"30\",\"https://pizzahut.com/return\"",
  "matchedColumns": [
    0,
    1,
    2,
    3,
    4,
    5,
    6,
    7,
    8
  ],
  "keyword": "And "
});
formatter.step({
  "line": 13,
  "name": "I make a request for the payment",
  "keyword": "When "
});
formatter.step({
  "line": 14,
  "name": "I should recieve a payment response with valid trace id in the header",
  "keyword": "Then "
});
formatter.step({
  "line": 15,
  "name": "the response body should contain valid payment id, created timestamp, transaction details, links",
  "keyword": "And "
});
formatter.match({
  "location": "PaymentRequest_StepDefs.i_am_an_authorized_merchant()"
});
formatter.result({
  "duration": 17250719,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "2c350a50-7ae0-4191-acc7-4420b74ed55d",
      "offset": 28
    },
    {
      "val": "0.044",
      "offset": 68
    },
    {
      "val": "HKD",
      "offset": 76
    },
    {
      "val": "Pizza order3",
      "offset": 82
    },
    {
      "val": "Native",
      "offset": 97
    },
    {
      "val": "",
      "offset": 106
    },
    {
      "val": "Pizzahut1239893993",
      "offset": 109
    },
    {
      "val": "30",
      "offset": 130
    },
    {
      "val": "https://pizzahut.com/return",
      "offset": 135
    }
  ],
  "location": "PaymentRequest_StepDefs.i_have_valid_transaction_details(String,String,String,String,String,String,String,String,String)"
});
formatter.result({
  "duration": 127709,
  "status": "passed"
});
formatter.match({
  "location": "PaymentRequest_StepDefs.i_make_a_request_for_the_payment()"
});
formatter.result({
  "duration": 900229313,
  "status": "passed"
});
formatter.match({
  "location": "PaymentRequest_StepDefs.i_should_recieve_a_response_with_valid_trace_id_in_the_header()"
});
formatter.result({
  "duration": 35840,
  "status": "passed"
});
formatter.match({
  "location": "PaymentRequest_StepDefs.the_response_body_should_contain_valid_payment_id_created_timestamp_transaction_details_links()"
});
formatter.result({
  "duration": 26363834,
  "error_message": "java.lang.AssertionError: Links within response is either incomplete or incorrect..Please check!! expected [true] but found [false]\n\tat org.testng.Assert.fail(Assert.java:94)\n\tat org.testng.Assert.failNotEquals(Assert.java:513)\n\tat org.testng.Assert.assertTrue(Assert.java:42)\n\tat steps.PaymentRequest_StepDefs.the_response_body_should_contain_valid_payment_id_created_timestamp_transaction_details_links(PaymentRequest_StepDefs.java:75)\n\tat ✽.And the response body should contain valid payment id, created timestamp, transaction details, links(PaymentRequest.feature:15)\n",
  "status": "failed"
});
formatter.before({
  "duration": 120454,
  "status": "passed"
});
formatter.background({
  "line": 3,
  "name": "Retrieving access Token",
  "description": "",
  "type": "background",
  "keyword": "Background"
});
formatter.step({
  "line": 4,
  "name": "I am a merchant",
  "keyword": "Given "
});
formatter.step({
  "line": 5,
  "name": "I make a request to the Dragon ID Manager",
  "keyword": "When "
});
formatter.step({
  "line": 6,
  "name": "I recieve an access_token",
  "keyword": "Then "
});
formatter.match({
  "location": "AccessTokenForMerchants_StepDefs.i_am_a_merchant()"
});
formatter.result({
  "duration": 30587,
  "status": "passed"
});
formatter.match({
  "location": "AccessTokenForMerchants_StepDefs.i_make_a_request_to_the_Dragon_ID_Manager()"
});
formatter.result({
  "duration": 604858838,
  "status": "passed"
});
formatter.match({
  "location": "AccessTokenForMerchants_StepDefs.i_recieve_a_valid_access_token()"
});
formatter.result({
  "duration": 14800915,
  "status": "passed"
});
formatter.scenario({
  "line": 23,
  "name": "Positive flow- A merchant is able to create a payment request with all the valid inputs",
  "description": "",
  "id": "payment-request---drag-187;positive-flow--a-merchant-is-able-to-create-a-payment-request-with-all-the-valid-inputs;;6",
  "type": "scenario",
  "keyword": "Scenario Outline",
  "tags": [
    {
      "line": 9,
      "name": "@trial"
    },
    {
      "line": 9,
      "name": "@functional"
    }
  ]
});
formatter.step({
  "line": 11,
  "name": "I am an authorized merchant",
  "keyword": "Given "
});
formatter.step({
  "line": 12,
  "name": "I have transaction details \"2c350a50-7ae0-4191-acc7-4420b74ed56d\", \"2.00\",\"HKD\",\"Pizza order4\",\"Ecommerce\",\"48787589677\",\"Pizzahut1239893993\",\"\",\"https://pizzahut.com/return\"",
  "matchedColumns": [
    0,
    1,
    2,
    3,
    4,
    5,
    6,
    7,
    8
  ],
  "keyword": "And "
});
formatter.step({
  "line": 13,
  "name": "I make a request for the payment",
  "keyword": "When "
});
formatter.step({
  "line": 14,
  "name": "I should recieve a payment response with valid trace id in the header",
  "keyword": "Then "
});
formatter.step({
  "line": 15,
  "name": "the response body should contain valid payment id, created timestamp, transaction details, links",
  "keyword": "And "
});
formatter.match({
  "location": "PaymentRequest_StepDefs.i_am_an_authorized_merchant()"
});
formatter.result({
  "duration": 22256860,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "2c350a50-7ae0-4191-acc7-4420b74ed56d",
      "offset": 28
    },
    {
      "val": "2.00",
      "offset": 68
    },
    {
      "val": "HKD",
      "offset": 75
    },
    {
      "val": "Pizza order4",
      "offset": 81
    },
    {
      "val": "Ecommerce",
      "offset": 96
    },
    {
      "val": "48787589677",
      "offset": 108
    },
    {
      "val": "Pizzahut1239893993",
      "offset": 122
    },
    {
      "val": "",
      "offset": 143
    },
    {
      "val": "https://pizzahut.com/return",
      "offset": 146
    }
  ],
  "location": "PaymentRequest_StepDefs.i_have_valid_transaction_details(String,String,String,String,String,String,String,String,String)"
});
formatter.result({
  "duration": 197147,
  "status": "passed"
});
formatter.match({
  "location": "PaymentRequest_StepDefs.i_make_a_request_for_the_payment()"
});
formatter.result({
  "duration": 835802946,
  "status": "passed"
});
formatter.match({
  "location": "PaymentRequest_StepDefs.i_should_recieve_a_response_with_valid_trace_id_in_the_header()"
});
formatter.result({
  "duration": 35811,
  "status": "passed"
});
formatter.match({
  "location": "PaymentRequest_StepDefs.the_response_body_should_contain_valid_payment_id_created_timestamp_transaction_details_links()"
});
formatter.result({
  "duration": 21108348,
  "error_message": "java.lang.AssertionError: Links within response is either incomplete or incorrect..Please check!! expected [true] but found [false]\n\tat org.testng.Assert.fail(Assert.java:94)\n\tat org.testng.Assert.failNotEquals(Assert.java:513)\n\tat org.testng.Assert.assertTrue(Assert.java:42)\n\tat steps.PaymentRequest_StepDefs.the_response_body_should_contain_valid_payment_id_created_timestamp_transaction_details_links(PaymentRequest_StepDefs.java:75)\n\tat ✽.And the response body should contain valid payment id, created timestamp, transaction details, links(PaymentRequest.feature:15)\n",
  "status": "failed"
});
formatter.before({
  "duration": 148747,
  "status": "passed"
});
formatter.background({
  "line": 3,
  "name": "Retrieving access Token",
  "description": "",
  "type": "background",
  "keyword": "Background"
});
formatter.step({
  "line": 4,
  "name": "I am a merchant",
  "keyword": "Given "
});
formatter.step({
  "line": 5,
  "name": "I make a request to the Dragon ID Manager",
  "keyword": "When "
});
formatter.step({
  "line": 6,
  "name": "I recieve an access_token",
  "keyword": "Then "
});
formatter.match({
  "location": "AccessTokenForMerchants_StepDefs.i_am_a_merchant()"
});
formatter.result({
  "duration": 58401,
  "status": "passed"
});
formatter.match({
  "location": "AccessTokenForMerchants_StepDefs.i_make_a_request_to_the_Dragon_ID_Manager()"
});
formatter.result({
  "duration": 930480579,
  "status": "passed"
});
formatter.match({
  "location": "AccessTokenForMerchants_StepDefs.i_recieve_a_valid_access_token()"
});
formatter.result({
  "duration": 13816223,
  "status": "passed"
});
formatter.scenario({
  "line": 24,
  "name": "Positive flow- A merchant is able to create a payment request with all the valid inputs",
  "description": "",
  "id": "payment-request---drag-187;positive-flow--a-merchant-is-able-to-create-a-payment-request-with-all-the-valid-inputs;;7",
  "type": "scenario",
  "keyword": "Scenario Outline",
  "tags": [
    {
      "line": 9,
      "name": "@trial"
    },
    {
      "line": 9,
      "name": "@functional"
    }
  ]
});
formatter.step({
  "line": 11,
  "name": "I am an authorized merchant",
  "keyword": "Given "
});
formatter.step({
  "line": 12,
  "name": "I have transaction details \"2c350a50-7ae0-4191-acc7-4420b74ed57d\", \"3\",\"HKD\",\"Pizza order5\",\"Mcommerce\",\"48787589678\",\"Pizzahut1239893993\",\"30\",\"https://pizzahut.com/return\"",
  "matchedColumns": [
    0,
    1,
    2,
    3,
    4,
    5,
    6,
    7,
    8
  ],
  "keyword": "And "
});
formatter.step({
  "line": 13,
  "name": "I make a request for the payment",
  "keyword": "When "
});
formatter.step({
  "line": 14,
  "name": "I should recieve a payment response with valid trace id in the header",
  "keyword": "Then "
});
formatter.step({
  "line": 15,
  "name": "the response body should contain valid payment id, created timestamp, transaction details, links",
  "keyword": "And "
});
formatter.match({
  "location": "PaymentRequest_StepDefs.i_am_an_authorized_merchant()"
});
formatter.result({
  "duration": 24648392,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "2c350a50-7ae0-4191-acc7-4420b74ed57d",
      "offset": 28
    },
    {
      "val": "3",
      "offset": 68
    },
    {
      "val": "HKD",
      "offset": 72
    },
    {
      "val": "Pizza order5",
      "offset": 78
    },
    {
      "val": "Mcommerce",
      "offset": 93
    },
    {
      "val": "48787589678",
      "offset": 105
    },
    {
      "val": "Pizzahut1239893993",
      "offset": 119
    },
    {
      "val": "30",
      "offset": 140
    },
    {
      "val": "https://pizzahut.com/return",
      "offset": 145
    }
  ],
  "location": "PaymentRequest_StepDefs.i_have_valid_transaction_details(String,String,String,String,String,String,String,String,String)"
});
formatter.result({
  "duration": 156952,
  "status": "passed"
});
formatter.match({
  "location": "PaymentRequest_StepDefs.i_make_a_request_for_the_payment()"
});
formatter.result({
  "duration": 748785976,
  "status": "passed"
});
formatter.match({
  "location": "PaymentRequest_StepDefs.i_should_recieve_a_response_with_valid_trace_id_in_the_header()"
});
formatter.result({
  "duration": 34633,
  "status": "passed"
});
formatter.match({
  "location": "PaymentRequest_StepDefs.the_response_body_should_contain_valid_payment_id_created_timestamp_transaction_details_links()"
});
formatter.result({
  "duration": 24870810,
  "error_message": "java.lang.AssertionError: Links within response is either incomplete or incorrect..Please check!! expected [true] but found [false]\n\tat org.testng.Assert.fail(Assert.java:94)\n\tat org.testng.Assert.failNotEquals(Assert.java:513)\n\tat org.testng.Assert.assertTrue(Assert.java:42)\n\tat steps.PaymentRequest_StepDefs.the_response_body_should_contain_valid_payment_id_created_timestamp_transaction_details_links(PaymentRequest_StepDefs.java:75)\n\tat ✽.And the response body should contain valid payment id, created timestamp, transaction details, links(PaymentRequest.feature:15)\n",
  "status": "failed"
});
formatter.before({
  "duration": 298799,
  "status": "passed"
});
formatter.background({
  "line": 3,
  "name": "Retrieving access Token",
  "description": "",
  "type": "background",
  "keyword": "Background"
});
formatter.step({
  "line": 4,
  "name": "I am a merchant",
  "keyword": "Given "
});
formatter.step({
  "line": 5,
  "name": "I make a request to the Dragon ID Manager",
  "keyword": "When "
});
formatter.step({
  "line": 6,
  "name": "I recieve an access_token",
  "keyword": "Then "
});
formatter.match({
  "location": "AccessTokenForMerchants_StepDefs.i_am_a_merchant()"
});
formatter.result({
  "duration": 38513,
  "status": "passed"
});
formatter.match({
  "location": "AccessTokenForMerchants_StepDefs.i_make_a_request_to_the_Dragon_ID_Manager()"
});
formatter.result({
  "duration": 508149304,
  "status": "passed"
});
formatter.match({
  "location": "AccessTokenForMerchants_StepDefs.i_recieve_a_valid_access_token()"
});
formatter.result({
  "duration": 18064449,
  "status": "passed"
});
formatter.scenario({
  "line": 25,
  "name": "Positive flow- A merchant is able to create a payment request with all the valid inputs",
  "description": "",
  "id": "payment-request---drag-187;positive-flow--a-merchant-is-able-to-create-a-payment-request-with-all-the-valid-inputs;;8",
  "type": "scenario",
  "keyword": "Scenario Outline",
  "tags": [
    {
      "line": 9,
      "name": "@trial"
    },
    {
      "line": 9,
      "name": "@functional"
    }
  ]
});
formatter.step({
  "line": 11,
  "name": "I am an authorized merchant",
  "keyword": "Given "
});
formatter.step({
  "line": 12,
  "name": "I have transaction details \"2c350a50-7ae0-4191-acc7-4420b74ed58d\", \"600.0\",\"HKD\",\"Pizza order6\",\"Ecommerce\",\"48787589679\",\"Pizzahut1239893993\",\"30\",\"https://pizzahut.com/return\"",
  "matchedColumns": [
    0,
    1,
    2,
    3,
    4,
    5,
    6,
    7,
    8
  ],
  "keyword": "And "
});
formatter.step({
  "line": 13,
  "name": "I make a request for the payment",
  "keyword": "When "
});
formatter.step({
  "line": 14,
  "name": "I should recieve a payment response with valid trace id in the header",
  "keyword": "Then "
});
formatter.step({
  "line": 15,
  "name": "the response body should contain valid payment id, created timestamp, transaction details, links",
  "keyword": "And "
});
formatter.match({
  "location": "PaymentRequest_StepDefs.i_am_an_authorized_merchant()"
});
formatter.result({
  "duration": 24933706,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "2c350a50-7ae0-4191-acc7-4420b74ed58d",
      "offset": 28
    },
    {
      "val": "600.0",
      "offset": 68
    },
    {
      "val": "HKD",
      "offset": 76
    },
    {
      "val": "Pizza order6",
      "offset": 82
    },
    {
      "val": "Ecommerce",
      "offset": 97
    },
    {
      "val": "48787589679",
      "offset": 109
    },
    {
      "val": "Pizzahut1239893993",
      "offset": 123
    },
    {
      "val": "30",
      "offset": 144
    },
    {
      "val": "https://pizzahut.com/return",
      "offset": 149
    }
  ],
  "location": "PaymentRequest_StepDefs.i_have_valid_transaction_details(String,String,String,String,String,String,String,String,String)"
});
formatter.result({
  "duration": 165220,
  "status": "passed"
});
formatter.match({
  "location": "PaymentRequest_StepDefs.i_make_a_request_for_the_payment()"
});
formatter.result({
  "duration": 778023299,
  "status": "passed"
});
formatter.match({
  "location": "PaymentRequest_StepDefs.i_should_recieve_a_response_with_valid_trace_id_in_the_header()"
});
formatter.result({
  "duration": 33231,
  "status": "passed"
});
formatter.match({
  "location": "PaymentRequest_StepDefs.the_response_body_should_contain_valid_payment_id_created_timestamp_transaction_details_links()"
});
formatter.result({
  "duration": 21645694,
  "error_message": "java.lang.AssertionError: Links within response is either incomplete or incorrect..Please check!! expected [true] but found [false]\n\tat org.testng.Assert.fail(Assert.java:94)\n\tat org.testng.Assert.failNotEquals(Assert.java:513)\n\tat org.testng.Assert.assertTrue(Assert.java:42)\n\tat steps.PaymentRequest_StepDefs.the_response_body_should_contain_valid_payment_id_created_timestamp_transaction_details_links(PaymentRequest_StepDefs.java:75)\n\tat ✽.And the response body should contain valid payment id, created timestamp, transaction details, links(PaymentRequest.feature:15)\n",
  "status": "failed"
});
formatter.before({
  "duration": 143012,
  "status": "passed"
});
formatter.background({
  "line": 3,
  "name": "Retrieving access Token",
  "description": "",
  "type": "background",
  "keyword": "Background"
});
formatter.step({
  "line": 4,
  "name": "I am a merchant",
  "keyword": "Given "
});
formatter.step({
  "line": 5,
  "name": "I make a request to the Dragon ID Manager",
  "keyword": "When "
});
formatter.step({
  "line": 6,
  "name": "I recieve an access_token",
  "keyword": "Then "
});
formatter.match({
  "location": "AccessTokenForMerchants_StepDefs.i_am_a_merchant()"
});
formatter.result({
  "duration": 38970,
  "status": "passed"
});
formatter.match({
  "location": "AccessTokenForMerchants_StepDefs.i_make_a_request_to_the_Dragon_ID_Manager()"
});
formatter.result({
  "duration": 586464385,
  "status": "passed"
});
formatter.match({
  "location": "AccessTokenForMerchants_StepDefs.i_recieve_a_valid_access_token()"
});
formatter.result({
  "duration": 13149603,
  "status": "passed"
});
formatter.scenario({
  "line": 26,
  "name": "Positive flow- A merchant is able to create a payment request with all the valid inputs",
  "description": "",
  "id": "payment-request---drag-187;positive-flow--a-merchant-is-able-to-create-a-payment-request-with-all-the-valid-inputs;;9",
  "type": "scenario",
  "keyword": "Scenario Outline",
  "tags": [
    {
      "line": 9,
      "name": "@trial"
    },
    {
      "line": 9,
      "name": "@functional"
    }
  ]
});
formatter.step({
  "line": 11,
  "name": "I am an authorized merchant",
  "keyword": "Given "
});
formatter.step({
  "line": 12,
  "name": "I have transaction details \"2c350a50-7ae0-4191-acc7-4420b74ed59d\", \"12.123\",\"USD\",\"Pizza order7\",\"Ecommerce\",\"48787589611\",\"Pizzahut1239893993\",\"30\",\"https://pizzahut.com/return\"",
  "matchedColumns": [
    0,
    1,
    2,
    3,
    4,
    5,
    6,
    7,
    8
  ],
  "keyword": "And "
});
formatter.step({
  "line": 13,
  "name": "I make a request for the payment",
  "keyword": "When "
});
formatter.step({
  "line": 14,
  "name": "I should recieve a payment response with valid trace id in the header",
  "keyword": "Then "
});
formatter.step({
  "line": 15,
  "name": "the response body should contain valid payment id, created timestamp, transaction details, links",
  "keyword": "And "
});
formatter.match({
  "location": "PaymentRequest_StepDefs.i_am_an_authorized_merchant()"
});
formatter.result({
  "duration": 18318024,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "2c350a50-7ae0-4191-acc7-4420b74ed59d",
      "offset": 28
    },
    {
      "val": "12.123",
      "offset": 68
    },
    {
      "val": "USD",
      "offset": 77
    },
    {
      "val": "Pizza order7",
      "offset": 83
    },
    {
      "val": "Ecommerce",
      "offset": 98
    },
    {
      "val": "48787589611",
      "offset": 110
    },
    {
      "val": "Pizzahut1239893993",
      "offset": 124
    },
    {
      "val": "30",
      "offset": 145
    },
    {
      "val": "https://pizzahut.com/return",
      "offset": 150
    }
  ],
  "location": "PaymentRequest_StepDefs.i_have_valid_transaction_details(String,String,String,String,String,String,String,String,String)"
});
formatter.result({
  "duration": 170863,
  "status": "passed"
});
formatter.match({
  "location": "PaymentRequest_StepDefs.i_make_a_request_for_the_payment()"
});
formatter.result({
  "duration": 844813819,
  "status": "passed"
});
formatter.match({
  "location": "PaymentRequest_StepDefs.i_should_recieve_a_response_with_valid_trace_id_in_the_header()"
});
formatter.result({
  "duration": 41484,
  "status": "passed"
});
formatter.match({
  "location": "PaymentRequest_StepDefs.the_response_body_should_contain_valid_payment_id_created_timestamp_transaction_details_links()"
});
formatter.result({
  "duration": 23185443,
  "error_message": "java.lang.AssertionError: Links within response is either incomplete or incorrect..Please check!! expected [true] but found [false]\n\tat org.testng.Assert.fail(Assert.java:94)\n\tat org.testng.Assert.failNotEquals(Assert.java:513)\n\tat org.testng.Assert.assertTrue(Assert.java:42)\n\tat steps.PaymentRequest_StepDefs.the_response_body_should_contain_valid_payment_id_created_timestamp_transaction_details_links(PaymentRequest_StepDefs.java:75)\n\tat ✽.And the response body should contain valid payment id, created timestamp, transaction details, links(PaymentRequest.feature:15)\n",
  "status": "failed"
});
formatter.before({
  "duration": 171474,
  "status": "passed"
});
formatter.background({
  "line": 3,
  "name": "Retrieving access Token",
  "description": "",
  "type": "background",
  "keyword": "Background"
});
formatter.step({
  "line": 4,
  "name": "I am a merchant",
  "keyword": "Given "
});
formatter.step({
  "line": 5,
  "name": "I make a request to the Dragon ID Manager",
  "keyword": "When "
});
formatter.step({
  "line": 6,
  "name": "I recieve an access_token",
  "keyword": "Then "
});
formatter.match({
  "location": "AccessTokenForMerchants_StepDefs.i_am_a_merchant()"
});
formatter.result({
  "duration": 38855,
  "status": "passed"
});
formatter.match({
  "location": "AccessTokenForMerchants_StepDefs.i_make_a_request_to_the_Dragon_ID_Manager()"
});
formatter.result({
  "duration": 517010754,
  "status": "passed"
});
formatter.match({
  "location": "AccessTokenForMerchants_StepDefs.i_recieve_a_valid_access_token()"
});
formatter.result({
  "duration": 14227853,
  "status": "passed"
});
formatter.scenario({
  "line": 27,
  "name": "Positive flow- A merchant is able to create a payment request with all the valid inputs",
  "description": "",
  "id": "payment-request---drag-187;positive-flow--a-merchant-is-able-to-create-a-payment-request-with-all-the-valid-inputs;;10",
  "type": "scenario",
  "keyword": "Scenario Outline",
  "tags": [
    {
      "line": 9,
      "name": "@trial"
    },
    {
      "line": 9,
      "name": "@functional"
    }
  ]
});
formatter.step({
  "line": 11,
  "name": "I am an authorized merchant",
  "keyword": "Given "
});
formatter.step({
  "line": 12,
  "name": "I have transaction details \"2c350a50-7ae0-4191-acc7-4420b74ed60d\", \"12.13\",\"USD\",\"Pizza order8\",\"Native\",\"ABCD\",\"Pizzahut1239893993\",\"30\",\"https://pizzahut.com/return\"",
  "matchedColumns": [
    0,
    1,
    2,
    3,
    4,
    5,
    6,
    7,
    8
  ],
  "keyword": "And "
});
formatter.step({
  "line": 13,
  "name": "I make a request for the payment",
  "keyword": "When "
});
formatter.step({
  "line": 14,
  "name": "I should recieve a payment response with valid trace id in the header",
  "keyword": "Then "
});
formatter.step({
  "line": 15,
  "name": "the response body should contain valid payment id, created timestamp, transaction details, links",
  "keyword": "And "
});
formatter.match({
  "location": "PaymentRequest_StepDefs.i_am_an_authorized_merchant()"
});
formatter.result({
  "duration": 16165377,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "2c350a50-7ae0-4191-acc7-4420b74ed60d",
      "offset": 28
    },
    {
      "val": "12.13",
      "offset": 68
    },
    {
      "val": "USD",
      "offset": 76
    },
    {
      "val": "Pizza order8",
      "offset": 82
    },
    {
      "val": "Native",
      "offset": 97
    },
    {
      "val": "ABCD",
      "offset": 106
    },
    {
      "val": "Pizzahut1239893993",
      "offset": 113
    },
    {
      "val": "30",
      "offset": 134
    },
    {
      "val": "https://pizzahut.com/return",
      "offset": 139
    }
  ],
  "location": "PaymentRequest_StepDefs.i_have_valid_transaction_details(String,String,String,String,String,String,String,String,String)"
});
formatter.result({
  "duration": 130803,
  "status": "passed"
});
formatter.match({
  "location": "PaymentRequest_StepDefs.i_make_a_request_for_the_payment()"
});
formatter.result({
  "duration": 858727044,
  "status": "passed"
});
formatter.match({
  "location": "PaymentRequest_StepDefs.i_should_recieve_a_response_with_valid_trace_id_in_the_header()"
});
formatter.result({
  "duration": 35931,
  "status": "passed"
});
formatter.match({
  "location": "PaymentRequest_StepDefs.the_response_body_should_contain_valid_payment_id_created_timestamp_transaction_details_links()"
});
formatter.result({
  "duration": 20635634,
  "error_message": "java.lang.AssertionError: Links within response is either incomplete or incorrect..Please check!! expected [true] but found [false]\n\tat org.testng.Assert.fail(Assert.java:94)\n\tat org.testng.Assert.failNotEquals(Assert.java:513)\n\tat org.testng.Assert.assertTrue(Assert.java:42)\n\tat steps.PaymentRequest_StepDefs.the_response_body_should_contain_valid_payment_id_created_timestamp_transaction_details_links(PaymentRequest_StepDefs.java:75)\n\tat ✽.And the response body should contain valid payment id, created timestamp, transaction details, links(PaymentRequest.feature:15)\n",
  "status": "failed"
});
});