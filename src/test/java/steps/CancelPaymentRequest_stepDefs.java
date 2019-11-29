package steps;
import com.google.common.collect.Sets;
import com.jayway.restassured.response.Response;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.TestContext;
import managers.UtilManager;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import utils.Constants;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CancelPaymentRequest_stepDefs extends UtilManager {

    TestContext testContext;
    Response response;
    public static final String RESOURCE_ENDPOINT_PROPERTY_NAME = "cancel_payment_request";
    public CancelPaymentRequest_stepDefs(TestContext testContext) {
        this.testContext = testContext;
    }

    private static final Set<String> ROLE_SET = Sets.newHashSet("Application.ReadWrite.All");
    final static Logger logger = Logger.getLogger(CancelPaymentRequest_stepDefs.class);


    @Given("^I am an authorized user to cancel payment request$")
    public void i_am_an_authorized_DRAGON_user_with_role() {
        testContext.getApiManager().putCancelPaymentRequest().setAuthToken(testContext.getApiManager().getAccessToken().getAccessToken());
    }

    @Given("^I am an authorized user to cancel payment request with token \"([^\"]*)\"$")
    public void i_am_an_authorized_DRAGON_user_with_role_withToken(String token) {
        testContext.getApiManager().putCancelPaymentRequest().setAuthToken(token);
    }

    private static String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }
    @When("^I hit cancel payment request endpoint with payment request id")
    public void iMakeCancelPaymentRequestAPI() {
        String url = getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource") + "/" + testContext.getApiManager().getPaymentRequest().getPaymentRequestId() + "/" + "cancel";
        testContext.getApiManager().putCancelPaymentRequest().makeCancelPaymentRequest(url,
                testContext.getApiManager().getMerchantManagementSigningKeyId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getApiManager().getMerchantManagementSigningKey(),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-post").split(","))));

    }

    @When("^cancel payment request endpoint with payment request id \"([^\"]*)\"$")
    public void iMakeCancelPaymentRequestAPIWithInvalidPaymentId(String paymentRequestId) {
        String url = getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource") + "/" + paymentRequestId + "/" + "cancel";
        testContext.getApiManager().putCancelPaymentRequest().makeCancelPaymentRequest(url,
                testContext.getApiManager().getMerchantManagementSigningKeyId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getApiManager().getMerchantManagementSigningKey(),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-post").split(","))));

    }


    @When("^cancel payment request endpoint with payment request id with null header \"([^\"]*)\"$")
    public void iMakeCancelPaymentRequestAPIWithNullHeaderValue(String nullHeader) {
        String url = getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource") + "/" + testContext.getApiManager().getPaymentRequest().getPaymentRequestId() + "/" + "cancel";
        testContext.getApiManager().putCancelPaymentRequest().makeCancelPaymentRequestWithNullHeader(url,
                testContext.getApiManager().getMerchantManagementSigningKeyId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getApiManager().getMerchantManagementSigningKey(),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-post").split(","))),nullHeader);
    }

    @When("^hit cancel payment request endpoint with payment request id with invalid header \"([^\"]*)\" and values \"([^\"]*)\"$")
    public void iMakeCancelPaymentRequestAPIWithInvalidHeaderValue(String key, String invalidValues) {
        String url = getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource") + "/" + testContext.getApiManager().getPaymentRequest().getPaymentRequestId() + "/" + "cancel";
        testContext.getApiManager().putCancelPaymentRequest().makeCancelPaymentRequestWithInvalidHeader(url,
                testContext.getApiManager().getMerchantManagementSigningKeyId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getApiManager().getMerchantManagementSigningKey(),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-post").split(","))), key, invalidValues);
    }

    @And("^I should receive a \"([^\"]*)\" status code with \"([^\"]*)\" message \"([^\"]*)\" with cancel payment response$")
    public void errorMessageShouldBeWithInResponse(int httpsCode, int statusCode, String errorMessage) throws Throwable {
        Response response = testContext.getApiManager().putCancelPaymentRequest().getResponse();
        Assert.assertEquals(getRestHelper().getResponseStatusCode(response), httpsCode, "Expected Response Code: " + httpsCode + "Actual: " + response.getStatusCode());
        Assert.assertTrue(
                getRestHelper().getErrorMessage(response).contains(errorMessage),
                "Different error message being returned..Expected: " + errorMessage + " Actual: " +
                        getRestHelper().getErrorMessage(response));
    }

    @When("^put cancel payment request endpoint with payment request id after \"([^\"]*)\"$")
    public void iMakeCancelPaymentRequestAPIAfterEffectiveDuration(String effectiveDuration) throws InterruptedException {
        String url = getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource") + "/" + testContext.getApiManager().getPaymentRequest().getPaymentRequestId() + "/" + "cancel";
        if (effectiveDuration.equalsIgnoreCase("15")) {
            Thread.sleep(16000);
            testContext.getApiManager().putCancelPaymentRequest().makeCancelPaymentRequest(url,
                    testContext.getApiManager().getMerchantManagementSigningKeyId(),
                    getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                    testContext.getApiManager().getMerchantManagementSigningKey(),
                    new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-post").split(","))));
        }
    }

    @Then("^payment request should be cancelled$")
    public void successful_response() {
        Assert.assertEquals(
                HttpStatus.SC_OK
                , getRestHelper().getResponseStatusCode(testContext.getApiManager().putCancelPaymentRequest().getResponse()));
    }

    @Then("^validate the success response of cancel payment request$")
    public void validateCancelPaymentResponse() {
        response = testContext.getApiManager().putCancelPaymentRequest().getResponse();
        Assert.assertEquals(response.path(Constants.PAYMENT_REQUEST_ID), testContext.getApiManager().getPaymentRequest().getPaymentRequestId(), "payment request is not same");
        Assert.assertEquals(response.path(Constants.STATUS_CODE), "PR004", "response code should be PR004");
        Assert.assertEquals(response.path(Constants.STATUS_DESCRIPTION), "Request for Payment Rejected");
    }

    @Then("^I should receive a \"([^\"]*)\" error response with \"([^\"]*)\" error description and \"([^\"]*)\" error Code with cancel payment response$")
    public void errorsDescriptionAndStatusCodeForCancelPayment(int statusCode, String errorDesc, String errorCode) {
        response = testContext.getApiManager().putCancelPaymentRequest().getResponse();
        Assert.assertEquals(getRestHelper().getResponseStatusCode(testContext.getApiManager().putCancelPaymentRequest().getResponse()), statusCode, "Different response code being returned");
        if (errorDesc.equalsIgnoreCase("Digest")) {
            Assert.assertTrue(
                    getRestHelper().getErrorDescription(testContext.getApiManager().putCancelPaymentRequest().getResponse())
                            .contains("Digest"),
                    "Different error description being returned..Expected: " + errorDesc + "Actual: " + getRestHelper().getErrorDescription(testContext.getApiManager().putCancelPaymentRequest().getResponse()));
        }   else{
            Assert.assertTrue(
                    getRestHelper().getErrorDescription(testContext.getApiManager().putCancelPaymentRequest().getResponse())
                            .replace("\"", "")
                            .contains(errorDesc),
                    "Different error description being returned..Expected: " + errorDesc + "Actual: " + getRestHelper().getErrorDescription(testContext.getApiManager().putCancelPaymentRequest().getResponse()));
        }
        Assert.assertEquals(getRestHelper().getErrorCode(testContext.getApiManager().putCancelPaymentRequest().getResponse()), errorCode, "Different error code being returned");
        }

    @Then("^error message should be \"([^\"]*)\" within cancel payment response$")
    public void errorMessageForCancelPaymentResponse(String errorMessage) {
        response = testContext.getApiManager().putCancelPaymentRequest().getResponse();
        Assert.assertTrue(
                getRestHelper().getErrorMessage(testContext.getApiManager().putCancelPaymentRequest().getResponse()).contains(errorMessage),
                "Different error message being returned..Expected: " + errorMessage + " Actual: " +
                        getRestHelper().getErrorMessage(testContext.getApiManager().putCancelPaymentRequest().getResponse()));

    }
}
