package steps;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import managers.TestContext;
import managers.UtilManager;
import org.apache.log4j.Logger;
import org.junit.Assert;
import utils.ApiUtils;
import utils.CreateTransaction;
import utils.GetTokenTest;
import utils.PropertyHelper;

import static org.junit.Assert.assertEquals;

public class CreateTransactionStep extends UtilManager {

    ManagementCommon common;
    final static Logger logger = Logger.getLogger(CreateTransactionStep.class);
    private RequestSpecification postRequest;
    private Response postResponse;
    private String serviceURL, payerAuthentication, payMeBaseUrl;
    public static String transactionId, authentication;
    TestContext testContext;

    public CreateTransactionStep(TestContext testContext) {
        this.testContext = testContext;
        common = new ManagementCommon(testContext);
    }

    private ApiUtils apiUtils = new ApiUtils();

    @When("^I make transaction successful with \"([^\"]*)\", \"([^\"]*)\" and \"([^\"]*)\"$")
    public void i_makeTransactionSuccessful(String mobileNo, String pin, String environment) throws Throwable {
        String env = PropertyHelper.getInstance().getPropertyCascading("env");
        logger.info("****** Payment Request id *********** --->: " + testContext.getApiManager().getPaymentRequest().getPaymentRequestId());
        String dragonQRCode = testContext.getApiManager().getPaymentRequest().getPaymentRequestId();

        if (env.equalsIgnoreCase("SIT")) {
            payMeBaseUrl = "https://api-uat5.allyoupayclouds.com/hsbcpayme_api/";
        } else if (env.equalsIgnoreCase("PRE")) {
            payMeBaseUrl = "https://api-poc.allyoupayclouds.com/hsbcpayme_api/";
        } else if (env.equalsIgnoreCase("CI")) {
            payMeBaseUrl = "https://api-poc.allyoupayclouds.com/hsbcpayme_api/";
        } else if (env.equalsIgnoreCase("UAT1")) {
            payMeBaseUrl = "https://api-uat.allyoupayclouds.com/hsbcpayme_api/";
        }

        payerAuthentication = new GetTokenTest().getJWTToken(mobileNo, payMeBaseUrl);
        String operationToken = new GetTokenTest().operationToken(pin);
        String payerAvatar = "https://shopfront.paymebiz-preprod.hsbc.com.hk/onboarding/5b50c792a2bed20f028dff218d0d2d93d90bc9521bd10e5feb83a31405b9080b/businessLogo.png";
        String currencyCode = "HKD";
        String amount = testContext.getApiManager().getPaymentRequest().getTotalAmount();
        String message = testContext.getApiManager().getPaymentRequest().getOrderDescription();
        String reference = testContext.getApiManager().getPaymentRequest().getOrderId();

        //String payeeId = "fe0895de-a18b-4c8a-bb0c-982d16b7e87c";  //SIT
        String payeeId = "3f40f912-3a93-4890-8368-75b7eb4c1265";    //UAT1

        String payMeMemberId = CreateTransaction.payMeMemberId;
        String payerName = CreateTransaction.payMeUserName;

        testContext.getApiManager().getPaymentRequest().setPayMeMemberId(payMeMemberId);
        testContext.getApiManager().getPaymentRequest().setPayerName(payerName);

        String jsonBody = "{\"qrcodeId\":\"" + dragonQRCode + "\",\"amount\":{\"currencyCode\":\"" + currencyCode + "\", \"amount\":" + amount + "}, \"payerId\":\"" + payMeMemberId + "\",\"payerName\":\"" + payerName + "\",\"payerAvatar\":\""
                + payerAvatar + "\",\"payeeId\":\"" + payeeId + "\", \"message\":\"" + message + "\", \"reference\":\"" + reference + "\"}";

        logger.info(" ******** Input request body to make post payment ********* --->:  " + jsonBody);
        postRequest = apiUtils.getRequestForPOS_Role().headers("Authorization", payerAuthentication, "X-HSBC-Operation-Token", operationToken).body(jsonBody);

        if (env.equalsIgnoreCase("SIT")) {
            serviceURL = "https://api-sit.dpwaf.com/payments/transactions/payment";
        } else if (env.equalsIgnoreCase("PRE")) {
            serviceURL = "https://paymebiz-app-preprod.hsbc.com.hk/payments/transactions/payment";
        } else if (env.equalsIgnoreCase("CI")) {
            serviceURL = "https://paymebiz-app-preprod.hsbc.com.hk/payments/transactions/payment";
        } else if (env.equalsIgnoreCase("UAT1")) {
            serviceURL = "https://paymebiz-app-uat1.dpwaf.com/payments/transactions/payment";
        }
        postResponse = apiUtils.getPostResponse(postRequest, serviceURL);
        System.out.println("postResponse : " + postResponse.asString());
        transactionId = postResponse.path("id");
        testContext.getApiManager().getTransaction().setTransactionId(transactionId);
        logger.info("****** Post Payment Response ****** --->:   " + postResponse.prettyPrint());
    }

    @When("^I make transaction successful with \"([^\"]*)\", \"([^\"]*)\"$")
    public void i_makeTransactionSuccessfulWithExpiredCode(String mobileNo, String pin) throws Throwable {
        String env = PropertyHelper.getInstance().getPropertyCascading("env");
        logger.info("****** Payment Request id *********** --->: " + testContext.getApiManager().getPaymentRequest().getPaymentRequestId());
        String dragonQRCode = testContext.getApiManager().getPaymentRequest().getPaymentRequestId();

        if (env.equalsIgnoreCase("SIT")) {
            payMeBaseUrl = "https://api-uat5.allyoupayclouds.com/hsbcpayme_api/";
        } else if (env.equalsIgnoreCase("PRE")) {
            payMeBaseUrl = "https://api-poc.allyoupayclouds.com/hsbcpayme_api/";
        } else if (env.equalsIgnoreCase("CI")) {
            payMeBaseUrl = "https://api-poc.allyoupayclouds.com/hsbcpayme_api/";
        } else if (env.equalsIgnoreCase("UAT1")) {
            payMeBaseUrl = "https://api-uat.allyoupayclouds.com/hsbcpayme_api/";
        }

        payerAuthentication = new GetTokenTest().getJWTToken(mobileNo, payMeBaseUrl);
        String operationToken = new GetTokenTest().operationToken(pin);
        String payerAvatar = "https://shopfront.paymebiz-preprod.hsbc.com.hk/onboarding/5b50c792a2bed20f028dff218d0d2d93d90bc9521bd10e5feb83a31405b9080b/businessLogo.png";
        String currencyCode = "HKD";
        String amount = testContext.getApiManager().getPaymentRequest().getTotalAmount();
        String message = testContext.getApiManager().getPaymentRequest().getOrderDescription();
        String reference = testContext.getApiManager().getPaymentRequest().getOrderId();
        //String payeeId = "fe0895de-a18b-4c8a-bb0c-982d16b7e87c";  //SIT
        String payeeId = "3f40f912-3a93-4890-8368-75b7eb4c1265";    //UAT1
        String payMeMemberId = CreateTransaction.payMeMemberId;
        String payerName = CreateTransaction.payMeUserName;

        testContext.getApiManager().getPaymentRequest().setPayMeMemberId(payMeMemberId);
        testContext.getApiManager().getPaymentRequest().setPayerName(payerName);

        String jsonBody = "{\"qrcodeId\":\"" + dragonQRCode + "\",\"amount\":{\"currencyCode\":\"" + currencyCode + "\", \"amount\":" + amount + "}, \"payerId\":\"" + payMeMemberId + "\",\"payerName\":\"" + payerName + "\",\"payerAvatar\":\""
                + payerAvatar + "\",\"payeeId\":\"" + payeeId + "\", \"message\":\"" + message + "\", \"reference\":\"" + reference + "\"}";

        logger.info(" ******** Input request body to make post payment ********* --->:  " + jsonBody);
        postRequest = apiUtils.getRequestForPOS_Role().headers("Authorization", payerAuthentication, "X-HSBC-Operation-Token", operationToken).body(jsonBody);

        if (env.equalsIgnoreCase("SIT")) {
            serviceURL = "https://api-sit.dpwaf.com/payments/transactions/payment";
        } else if (env.equalsIgnoreCase("PRE")) {
            serviceURL = "https://paymebiz-app-preprod.hsbc.com.hk/payments/transactions/payment";
        } else if (env.equalsIgnoreCase("CI")) {
            serviceURL = "https://paymebiz-app-preprod.hsbc.com.hk/payments/transactions/payment";
        } else if (env.equalsIgnoreCase("UAT1")) {
            serviceURL = "https://paymebiz-app-uat1.dpwaf.com/payments/transactions/payment";
        }
        postResponse = apiUtils.getPostResponse(postRequest, serviceURL);
        transactionId = postResponse.path("id");

        //setting transactionId
        testContext.getApiManager().getPaymentStatus().setTransactionId(transactionId);
        testContext.getApiManager().getTransaction().setTransactionId(transactionId);

        System.out.println("testContext.getApiManager().getTransaction().getTransactionId() : " + testContext.getApiManager().getTransaction().getTransactionId());
        System.out.println("testContext.getApiManager().getPaymentStatus().setTransactionId(transactionId); : " + testContext.getApiManager().getPaymentStatus().getTransactionId());

        logger.info("****** Post Payment Response ****** --->:   " + postResponse.prettyPrint());
    }


    @Then("^I should receive a \"([^\"]*)\" error response with \"([^\"]*)\" error description and \"([^\"]*)\" errorcode within post payment response for \"([^\"]*)\" and \"([^\"]*)\"$")
    public void i_should_receive_a_error_response_with_error_description_and_errorcode_within_refund_response(int responseCode, String errorDesc, String errorMessage, String mobileNo, String pin) throws InterruptedException {
        Thread.sleep(16000);
        String env = PropertyHelper.getInstance().getPropertyCascading("env");
        logger.info("****** Payment Request id *********** --->: " + testContext.getApiManager().getPaymentRequest().getPaymentRequestId());
        String dragonQRCode = testContext.getApiManager().getPaymentRequest().getPaymentRequestId();

        if (env.equalsIgnoreCase("SIT")) {
            payMeBaseUrl = "https://api-uat5.allyoupayclouds.com/hsbcpayme_api/";
        } else if (env.equalsIgnoreCase("PRE")) {
            payMeBaseUrl = "https://api-poc.allyoupayclouds.com/hsbcpayme_api/";
        } else if (env.equalsIgnoreCase("CI")) {
            payMeBaseUrl = "https://api-poc.allyoupayclouds.com/hsbcpayme_api/";
        } else if (env.equalsIgnoreCase("UAT1")) {
            payMeBaseUrl = "https://api-uat.allyoupayclouds.com/hsbcpayme_api/";
        }

        payerAuthentication = new GetTokenTest().getJWTToken(mobileNo, payMeBaseUrl);
        String operationToken = new GetTokenTest().operationToken(pin);
        String payerAvatar = "https://shopfront.paymebiz-preprod.hsbc.com.hk/onboarding/5b50c792a2bed20f028dff218d0d2d93d90bc9521bd10e5feb83a31405b9080b/businessLogo.png";
        String currencyCode = "HKD";
        String amount = testContext.getApiManager().getPaymentRequest().getTotalAmount();
        String message = testContext.getApiManager().getPaymentRequest().getOrderDescription();
        String reference = testContext.getApiManager().getPaymentRequest().getOrderId();
        //String payeeId = "fe0895de-a18b-4c8a-bb0c-982d16b7e87c";  //SIT
        String payeeId = "3f40f912-3a93-4890-8368-75b7eb4c1265";    //UAT1
        String payMeMemberId = CreateTransaction.payMeMemberId;
        String payerName = CreateTransaction.payMeUserName;

        testContext.getApiManager().getPaymentRequest().setPayMeMemberId(payMeMemberId);
        testContext.getApiManager().getPaymentRequest().setPayerName(payerName);

        String jsonBody = "{\"qrcodeId\":\"" + dragonQRCode + "\",\"amount\":{\"currencyCode\":\"" + currencyCode + "\", \"amount\":" + amount + "}, \"payerId\":\"" + payMeMemberId + "\",\"payerName\":\"" + payerName + "\",\"payerAvatar\":\""
                + payerAvatar + "\",\"payeeId\":\"" + payeeId + "\", \"message\":\"" + message + "\", \"reference\":\"" + reference + "\"}";

        logger.info(" ******** Input request body to make post payment ********* --->:  " + jsonBody);
        postRequest = apiUtils.getRequestForPOS_Role().headers("Authorization", payerAuthentication, "X-HSBC-Operation-Token", operationToken).body(jsonBody);

        if (env.equalsIgnoreCase("SIT")) {
            serviceURL = "https://api-sit.dpwaf.com/payments/transactions/payment";
        } else if (env.equalsIgnoreCase("PRE")) {
            serviceURL = "https://paymebiz-app-preprod.hsbc.com.hk/payments/transactions/payment";
        } else if (env.equalsIgnoreCase("CI")) {
            serviceURL = "https://paymebiz-app-preprod.hsbc.com.hk/payments/transactions/payment";
        } else if (env.equalsIgnoreCase("UAT1")) {
            serviceURL = "https://paymebiz-app-uat1.dpwaf.com/payments/transactions/payment";
        }
        postResponse = apiUtils.getPostResponse(postRequest, serviceURL);
        Assert.assertEquals("Response code didn't match", responseCode, postResponse.getStatusCode());
        assertEquals("Error description didn't match", errorDesc, postResponse.path("errors[0].errorDescription"));
        assertEquals("error message didn't match!", errorMessage, postResponse.path("message"));


    }
}

