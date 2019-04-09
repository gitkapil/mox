package steps;

import managers.TestContext;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.UtilManager;
import org.apache.log4j.Logger;
import org.junit.Assert;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;


public class MessageSigning_StepDefs extends UtilManager{
    TestContext testContext;

    public MessageSigning_StepDefs(TestContext testContext) {
        this.testContext = testContext;
    }

    final static Logger logger = Logger.getLogger(MessageSigning_StepDefs.class);

    @When("^I make a request for the payment with invalid signing key id$")
    public void i_make_a_request_for_the_payment_with_invalid_signing_key_id()  {
        logger.info("********** Creating Payment Request with invalid signing key id ***********");
        testContext.getApiManager().getPaymentRequest().retrievePaymentRequest(getRestHelper().getBaseURI()+getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                "random_signing_key_id",
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-post").split(","))));
    }

    @When("^I make a request for the payment with invalid signing key$")
    public void i_make_a_request_for_the_payment_with_invalid_signing_key()  {
        logger.info("********** Creating Payment Request with invalid signing key***********");
        testContext.getApiManager().getPaymentRequest().retrievePaymentRequest(getRestHelper().getBaseURI()+getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                testContext.getApiManager().getAccessToken().getClientId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                "cGFzc3BocmFzZQ11",
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-post").split(","))));
    }

    @When("^I make a request for the payment with a different signing algo$")
    public void i_make_a_request_for_the_payment_with_a_different_signing_algo() {

        logger.info("********** Creating Payment Request with different signing algo***********");
        testContext.getApiManager().getPaymentRequest().retrievePaymentRequest(getRestHelper().getBaseURI()+getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                testContext.getApiManager().getAccessToken().getClientId(),
                "HmacSHA512",
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-post").split(","))));
    }

    @When("^I make a request for the payment with header \"([^\"]*)\" value missing from the signature$")
    public void i_make_a_request_for_the_payment_with_header_value_missing_from_the_signature(String missingFromHeader) {
        HashSet invalidHeaderElements= new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-post").split(",")));
        invalidHeaderElements.remove(missingFromHeader);

        testContext.getApiManager().getPaymentRequest().retrievePaymentRequest(getRestHelper().getBaseURI()+getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                testContext.getApiManager().getAccessToken().getClientId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"), invalidHeaderElements);

    }

    @When("^I use the same signature to trigger another payment request but with different value in \"([^\"]*)\"$")
    public void i_use_the_same_signature_to_trigger_another_payment_request_but_with_different_value_in(String headerElement)  {
        if (headerElement.equalsIgnoreCase("trace-id")){
            testContext.getApiManager().getPaymentRequest().getPaymentRequestHeader().put("Trace-Id", getGeneral().generateUniqueUUID());
        }

        if (headerElement.equalsIgnoreCase("request-date-time")){

            testContext.getApiManager().getPaymentRequest().getPaymentRequestHeader().put("Request-Date-Time", getDateHelper().getUTCNowDateTime());
        }

        if (headerElement.equalsIgnoreCase("Authorization")){
            testContext.getApiManager().getAccessToken().retrieveAccessToken(testContext.getApiManager().getAccessToken().getEndpoint() +getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "retrieve_access_token_resource"));

            testContext.getApiManager().getPaymentRequest().getPaymentRequestHeader().put("Authorization", "Bearer "+testContext.getApiManager().getAccessToken().getAccessToken());
        }

        testContext.getApiManager().getPaymentRequest().retrievePaymentRequestExistingHeaderBody(getRestHelper().getBaseURI()+getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                testContext.getApiManager().getPaymentRequest().getPaymentRequestHeader(), testContext.getApiManager().getPaymentRequest().getPaymentRequestBody());
    }


    @Given("^I create a signature for the payment request$")
    public void i_create_a_signature_payment_request() {
        try {
            testContext.getApiManager().getPaymentRequest().returnPaymentRequestHeader("POST", new URL(getRestHelper().getBaseURI()+getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource")).getPath(),
                    testContext.getApiManager().getAccessToken().getClientId(),
                    getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                    getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                    new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-post").split(","))));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        testContext.getApiManager().getPaymentRequest().returnPaymentRequestBody();
    }

    @When("^I use the generated signature with tampered body$")
    public void i_use_the_generated_signature_with_tampered_body() {
        testContext.getApiManager().getPaymentRequest().getPaymentRequestBody().put("totalAmount", 888888);

        testContext.getApiManager().getPaymentRequest().retrievePaymentRequestExistingHeaderBody(getRestHelper().getBaseURI()+getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                testContext.getApiManager().getPaymentRequest().getPaymentRequestHeader(), testContext.getApiManager().getPaymentRequest().getPaymentRequestBody());
    }

    @When("^I make a request for the payment status with invalid signing key id$")
    public void i_make_a_request_for_the_payment_status_with_invalid_signing_key_id()  {
        logger.info("********** Creating Payment Status Request with invalid signing key id ***********");
        testContext.getApiManager().getPaymentStatus().retrievePaymentStatus(getRestHelper().getBaseURI()+getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                "random_signing_key_id",
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-get").split(","))));
    }

    @When("^I make a request for the payment status with invalid signing key$")
    public void i_make_a_request_for_the_payment_status_with_invalid_signing_key()  {
        logger.info("********** Creating Payment Status Request with invalid signing key***********");
        testContext.getApiManager().getPaymentStatus().retrievePaymentStatus(getRestHelper().getBaseURI()+getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                testContext.getApiManager().getAccessToken().getClientId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                "cGFzc3BocmFzZQ11",
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-get").split(","))));
    }

    @When("^I make a request for the payment status with a different signing algo$")
    public void i_make_a_request_status_for_the_payment_with_a_different_signing_algo() {

        logger.info("********** Creating Payment Status Request with different signing algo***********");
        testContext.getApiManager().getPaymentStatus().retrievePaymentStatus(getRestHelper().getBaseURI()+getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                testContext.getApiManager().getAccessToken().getClientId(),
                "HmacSHA512",
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-get").split(","))));
    }


    @When("^I make a request for the payment status with header \"([^\"]*)\" value missing from the signature$")
    public void i_make_a_request_for_the_payment_status_with_header_value_missing_from_the_signature(String missingFromHeader) {
        HashSet invalidHeaderElements= new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-get").split(",")));
        invalidHeaderElements.remove(missingFromHeader);

        testContext.getApiManager().getPaymentStatus().retrievePaymentStatus(getRestHelper().getBaseURI()+getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                testContext.getApiManager().getAccessToken().getClientId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"), invalidHeaderElements);

    }

    @When("^I use the same signature to trigger another payment status request but with different value in \"([^\"]*)\"$")
    public void i_use_the_same_status_signature_to_trigger_another_payment_request_but_with_different_value_in(String headerElement)  {
        if (headerElement.equalsIgnoreCase("trace-id")){
            testContext.getApiManager().getPaymentStatus().getPaymentStatusHeader().put("Trace-Id", getGeneral().generateUniqueUUID());
        }

        if (headerElement.equalsIgnoreCase("request-date-time")){
            testContext.getApiManager().getPaymentStatus().getPaymentStatusHeader().put("Request-Date-Time", getDateHelper().getUTCNowDateTime());
        }

        if (headerElement.equalsIgnoreCase("Authorization")){
            testContext.getApiManager().getAccessToken().retrieveAccessToken(testContext.getApiManager().getAccessToken().getEndpoint() +getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "retrieve_access_token_resource"));

            testContext.getApiManager().getPaymentStatus().getPaymentStatusHeader().put("Authorization", "Bearer "+testContext.getApiManager().getAccessToken().getAccessToken());
        }

        testContext.getApiManager().getPaymentStatus().retrievePaymentStatusExistingHeader(getRestHelper().getBaseURI()+getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                testContext.getApiManager().getPaymentStatus().getPaymentStatusHeader());
    }


    @Given("^I create a signature for the payment status")
    public void i_create_a_signature_payment_status() {
        try {
            testContext.getApiManager().getPaymentStatus().returnPaymentStatusHeader("GET", new URL(getRestHelper().getBaseURI()+getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource")).getPath(),
                    testContext.getApiManager().getAccessToken().getClientId(),
                    getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                    getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                    new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-get").split(","))));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    @Then("^the payment request response should be signed$")
    public void the_payment_request_response_should_be_signed() {
        try {
            logger.info("*** Payment Request Response Headers***");
            getRestHelper().logResponseHeaders(testContext.getApiManager().getPaymentRequest().getPaymentRequestResponse());

            getSignatureHelper().verifySignature(testContext.getApiManager().getPaymentRequest().getPaymentRequestResponse(), "POST",
                    getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
//                    Base64.getDecoder().decode(testContext.getApiManager().getAccessToken().getClientId()),
                    Base64.getDecoder().decode(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key")),
                    getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"));

        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage().equalsIgnoreCase("Signature failed validation"))
                Assert.assertTrue("Payment Request Response Signature Verification Failed", false);

            if (e.getMessage().equalsIgnoreCase("No Signature Found"))
                Assert.assertTrue("No Signature Found in the Payment Request Response", false);
        }
    }

    @Then("^the POST application request response should be signed$")
    public void the_post_application_request_response_should_be_signed() {
        try {
            logger.info("*** POST Application Response Headers***");
            getRestHelper().logResponseHeaders(testContext.getApiManager().getPostApplication().getPostApplicationRequestResponse());

            getSignatureHelper().verifySignature(testContext.getApiManager().getPostApplication().getPostApplicationRequestResponse(), "POST",
                    getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_application_resource"),
//                    Base64.getDecoder().decode(testContext.getApiManager().getAccessToken().getClientId()),
                    Base64.getDecoder().decode(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key")),
                    getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"));

        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage().equalsIgnoreCase("Signature failed validation"))
                Assert.assertTrue("Post Application Response Signature Verification Failed", false);

            if (e.getMessage().equalsIgnoreCase("No Signature Found"))
                Assert.assertTrue("No Signature Found in the Post Application Response", false);
        }
    }

    @Then("^the payment status response should be signed$")
    public void the_payment_status_should_be_signed() {
        try {
            logger.info("*** Payment Status Response Headers***");
            getRestHelper().logResponseHeaders(testContext.getApiManager().getPaymentStatus().getPaymentStatusResponse());

            getSignatureHelper().verifySignature(testContext.getApiManager().getPaymentStatus().getPaymentStatusResponse(),"GET",
                    getRestHelper().getBaseURI()+getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                    Base64.getDecoder().decode(testContext.getApiManager().getAccessToken().getClientId()),
                    // Base64.getDecoder().decode(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key")),
                    getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"));

        } catch (Exception e) {
            if (e.getMessage().equalsIgnoreCase("Signature failed validation"))
                Assert.assertTrue("Payment status Response Signature Verification Failed", false);

            if (e.getMessage().equalsIgnoreCase("No Signature Found"))
                Assert.assertTrue("No Signature Found in the Payment Status Response", false);
        }
    }


    @When("^I make a request for the payment without digest in the header$")
    public void i_make_a_request_for_the_payment_without_digest_in_the_header()  {
        logger.info("********** Creating Payment Request ***********");
        testContext.getApiManager().getPaymentRequest().retrievePaymentRequestWithoutDigest(getRestHelper().getBaseURI()+getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                testContext.getApiManager().getAccessToken().getClientId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-post").split(","))));
    }

    @When("^I make a request for the payment with digest in the signature header list but not sent in the headers$")
    public void i_make_a_request_for_the_payment_with_digest_in_the_signature_header_list_but_not_sent_in_the_headers(){
        try {
            testContext.getApiManager().getPaymentRequest().retrievePaymentRequestWithMissingHeaderKeys(getRestHelper().getBaseURI()+getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),"Digest",
                    testContext.getApiManager().getAccessToken().getClientId(),
                    getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                    getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                    new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-post").split(","))));
        } catch (Exception e) {
            e.printStackTrace();
        }

        testContext.getApiManager().getPaymentRequest().returnPaymentRequestBody();
    }

}



