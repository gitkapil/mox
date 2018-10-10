package steps;


import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import org.apache.log4j.Logger;
import utils.BaseStep;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;


public class MessageSigning_StepDefs implements BaseStep{
    final static Logger logger = Logger.getLogger(MessageSigning_StepDefs.class);

    @When("^I make a request for the payment with invalid signing key id$")
    public void i_make_a_request_for_the_payment_with_invalid_signing_key_id()  {
        logger.info("********** Creating Payment Request with invalid signing key id ***********");
        paymentRequest.retrievePaymentRequest(restHelper.getBaseURI()+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                "random_signing_key_id",
                fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                new HashSet(Arrays.asList(fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "header-list").split(","))));
    }

    @When("^I make a request for the payment with invalid signing key$")
    public void i_make_a_request_for_the_payment_with_invalid_signing_key()  {
        logger.info("********** Creating Payment Request with invalid signing key***********");
        paymentRequest.retrievePaymentRequest(restHelper.getBaseURI()+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "signing_key_id"),
                fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                "cGFzc3BocmFzZQ11",
                new HashSet(Arrays.asList(fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "header-list").split(","))));
    }

    @When("^I make a request for the payment with a different signing algo$")
    public void i_make_a_request_for_the_payment_with_a_different_signing_algo() {

        logger.info("********** Creating Payment Request with different signing algo***********");
        paymentRequest.retrievePaymentRequest(restHelper.getBaseURI()+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "signing_key_id"),
                "HmacSHA512",
                fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                new HashSet(Arrays.asList(fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "header-list").split(","))));
    }

    @When("^I make a request for the payment with header \"([^\"]*)\" value missing from the signature$")
    public void i_make_a_request_for_the_payment_with_header_value_missing_from_the_signature(String missingFromHeader) {
        HashSet invalidHeaderElements= new HashSet(Arrays.asList(fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "header-list").split(",")));
        invalidHeaderElements.remove(missingFromHeader);

        paymentRequest.retrievePaymentRequest(restHelper.getBaseURI()+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "signing_key_id"),
                fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"), invalidHeaderElements);

    }

    @When("^I use the same signature to trigger another payment request but with different value in \"([^\"]*)\"$")
    public void i_use_the_same_signature_to_trigger_another_payment_request_but_with_different_value_in(String headerElement)  {
        if (headerElement.equalsIgnoreCase("trace-id")){
            paymentRequest.getPaymentRequestHeader().put("Trace-Id", general.generateUniqueUUID());
        }

        if (headerElement.equalsIgnoreCase("request-date-time")){
           // general.waitFor(3000);
            paymentRequest.getPaymentRequestHeader().put("Request-Date-Time", dateHelper.getUTCNowDateTime());
        }

        if (headerElement.equalsIgnoreCase("Authorization")){
            accessToken.retrieveAccessToken(accessToken.getEndpoint() +fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "retrieve_access_token_resource"));

            paymentRequest.getPaymentRequestHeader().put("Authorization", "Bearer "+accessToken.getAccessToken());
        }

        paymentRequest.retrievePaymentRequestExistingHeaderBody(restHelper.getBaseURI()+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                paymentRequest.getPaymentRequestHeader(), paymentRequest.getPaymentRequestBody());
    }


    @Given("^I create a signature for the payment request$")
    public void i_create_a_signature_payment_request() {
        try {
            paymentRequest.returnPaymentRequestHeader("POST", new URL(restHelper.getBaseURI()+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource")).getPath(),
                    fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "signing_key_id"),
                    fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                    fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                    new HashSet(Arrays.asList(fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "header-list").split(","))));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        paymentRequest.returnPaymentRequestBody();
    }

    @When("^I use the generated signature with tampered body$")
    public void i_use_the_generated_signature_with_tampered_body() {
        paymentRequest.getPaymentRequestBody().put("totalAmount", 888888);

        paymentRequest.retrievePaymentRequestExistingHeaderBody(restHelper.getBaseURI()+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                paymentRequest.getPaymentRequestHeader(), paymentRequest.getPaymentRequestBody());
    }

    @When("^I make a request for the payment status with invalid signing key id$")
    public void i_make_a_request_for_the_payment_status_with_invalid_signing_key_id()  {
        logger.info("********** Creating Payment Status Request with invalid signing key id ***********");
        paymentStatus.retrievePaymentStatus(restHelper.getBaseURI()+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                "random_signing_key_id",
                fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                new HashSet(Arrays.asList(fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "header-list").split(","))));
    }

    @When("^I make a request for the payment status with invalid signing key$")
    public void i_make_a_request_for_the_payment_status_with_invalid_signing_key()  {
        logger.info("********** Creating Payment Status Request with invalid signing key***********");
        paymentStatus.retrievePaymentStatus(restHelper.getBaseURI()+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "signing_key_id"),
                fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                "cGFzc3BocmFzZQ11",
                new HashSet(Arrays.asList(fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "header-list").split(","))));
    }

    @When("^I make a request for the payment status with a different signing algo$")
    public void i_make_a_request_status_for_the_payment_with_a_different_signing_algo() {

        logger.info("********** Creating Payment Status Request with different signing algo***********");
        paymentStatus.retrievePaymentStatus(restHelper.getBaseURI()+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "signing_key_id"),
                "HmacSHA512",
                fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                new HashSet(Arrays.asList(fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "header-list").split(","))));
    }


    @When("^I make a request for the payment status with header \"([^\"]*)\" value missing from the signature$")
    public void i_make_a_request_for_the_payment_status_with_header_value_missing_from_the_signature(String missingFromHeader) {
        HashSet invalidHeaderElements= new HashSet(Arrays.asList(fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "header-list").split(",")));
        invalidHeaderElements.remove(missingFromHeader);

        paymentStatus.retrievePaymentStatus(restHelper.getBaseURI()+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "signing_key_id"),
                fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"), invalidHeaderElements);

    }

    @When("^I use the same signature to trigger another payment status request but with different value in \"([^\"]*)\"$")
    public void i_use_the_same_status_signature_to_trigger_another_payment_request_but_with_different_value_in(String headerElement)  {
        if (headerElement.equalsIgnoreCase("trace-id")){
            paymentStatus.getPaymentStatusHeader().put("Trace-Id", general.generateUniqueUUID());
        }

        if (headerElement.equalsIgnoreCase("request-date-time")){
            paymentStatus.getPaymentStatusHeader().put("Request-Date-Time", dateHelper.getUTCNowDateTime());
        }

        if (headerElement.equalsIgnoreCase("Authorization")){
            accessToken.retrieveAccessToken(accessToken.getEndpoint() +fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "retrieve_access_token_resource"));

            paymentStatus.getPaymentStatusHeader().put("Authorization", "Bearer "+accessToken.getAccessToken());
        }

        paymentStatus.retrievePaymentStatusExistingHeader(restHelper.getBaseURI()+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                paymentStatus.getPaymentStatusHeader());
    }


    @Given("^I create a signature for the payment status")
    public void i_create_a_signature_payment_status() {
        try {
            paymentStatus.returnPaymentStatusHeader("GET", new URL(restHelper.getBaseURI()+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource")).getPath(),
                    fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "signing_key_id"),
                    fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                    fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                    new HashSet(Arrays.asList(fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "header-list").split(","))));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

}



