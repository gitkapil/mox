package steps;

import apiHelpers.AccessTokenForMerchants;
import apiHelpers.PaymentRequest;
import apiHelpers.PaymentStatus;
import apiHelpers.TestContext;
import cucumber.api.java.en.When;
import org.apache.log4j.Logger;
import utils.BaseStep;

import java.util.Arrays;
import java.util.HashSet;


public class TraceIdLimiting_StepDefs{

    TestContext testContext;
    PaymentRequest paymentRequest;
    PaymentStatus paymentStatus;
    AccessTokenForMerchants accessToken;

    public TraceIdLimiting_StepDefs(TestContext testContext) {
        this.testContext = testContext;
        this.paymentRequest = new PaymentRequest(testContext);
        this.paymentStatus= new PaymentStatus(testContext);
        this.accessToken= new AccessTokenForMerchants(testContext);
    }
    final static Logger logger = Logger.getLogger(TraceIdLimiting_StepDefs.class);

    @When("^I make a create payment request again with the same traceid$")
    public void i_make_a_request_again_with_the_same_traceid() {
      paymentRequest.setRequestDateTime(testContext.getDateHelper().getUTCNowDateTime());

      logger.info("********** Triggering Payment Request Again With the same Trace Id***********");
      paymentRequest.retrievePaymentRequest(testContext.getRestHelper().getBaseURI()+testContext.getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
              accessToken.getClientId(),
              testContext.getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
              testContext.getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
              new HashSet(Arrays.asList(testContext.getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-post").split(","))));

    }

    @When("^I make a check status request again with the same traceid$")
    public void i_make_a_check_status_request_again_with_the_same_traceid() {
        paymentStatus.setRequestDateTime(testContext.getDateHelper().getUTCNowDateTime());

        logger.info("********** Triggering Payment Status Again With the same Trace Id***********");
        paymentStatus.retrievePaymentStatus(testContext.getRestHelper().getBaseURI()+testContext.getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                accessToken.getClientId(),
                testContext.getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                new HashSet(Arrays.asList(testContext.getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-get").split(","))));

    }

    @When("^I make a create payment request again with the same traceid after (\\d+) mins$")
    public void i_make_a_create_payment_request_again_with_the_same_traceid_after_mins(int mins){
        logger.info("Waiting for "+mins +" mins!");
        testContext.getGeneral().waitFor(mins*60);
        paymentRequest.setRequestDateTime(testContext.getDateHelper().getUTCNowDateTime());

        logger.info("********** Triggering Payment Request Again With the same Trace Id After "+mins+" mins ***********");
        paymentRequest.retrievePaymentRequest(testContext.getRestHelper().getBaseURI()+testContext.getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                accessToken.getClientId(),
                testContext.getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                new HashSet(Arrays.asList(testContext.getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-post").split(","))));

    }

    @When("^I make a check status request again with the same traceid after (\\d+) mins$")
    public void i_make_a_check_status_request_again_with_the_same_traceid_after_mins(int mins) {
        logger.info("Waiting for "+mins +" mins!");
        testContext.getGeneral().waitFor(mins*60);
        paymentStatus.setRequestDateTime(testContext.getDateHelper().getUTCNowDateTime());

        logger.info("********** Triggering Payment Status Again With the same Trace Id After "+mins+" mins ***********");
        paymentStatus.retrievePaymentStatus(testContext.getRestHelper().getBaseURI()+testContext.getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                accessToken.getClientId(),
                testContext.getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                new HashSet(Arrays.asList(testContext.getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-get").split(","))));

    }

    @When("^I make a create payment request again with a different traceid$")
    public void i_make_a_request_again_with_a_different_traceid() {
        paymentRequest.setRequestDateTime(testContext.getDateHelper().getUTCNowDateTime());
        paymentRequest.setTraceId(testContext.getGeneral().generateUniqueUUID());

        logger.info("********** Triggering Payment Request Again With a different Trace Id***********");
        paymentRequest.retrievePaymentRequest(testContext.getRestHelper().getBaseURI()+testContext.getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                accessToken.getClientId(),
                testContext.getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                new HashSet(Arrays.asList(testContext.getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-post").split(","))));

    }

    @When("^I make a check status request again with a different traceid$")
    public void i_make_a_check_status_request_again_with_a_different_traceid() {
        paymentStatus.setRequestDateTime(testContext.getDateHelper().getUTCNowDateTime());
        paymentStatus.setTraceId(testContext.getGeneral().generateUniqueUUID());

        logger.info("********** Triggering Payment Status Again With a different Trace Id***********");
        paymentStatus.retrievePaymentStatus(testContext.getRestHelper().getBaseURI()+testContext.getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                accessToken.getClientId(),
                testContext.getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                new HashSet(Arrays.asList(testContext.getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-get").split(","))));

    }

    @When("^I make a create payment request again with a different traceid but request date time more than (\\d+) mins older than the current system time$")
    public void i_make_a_create_payment_request_again_with_a_different_traceid_but_request_date_time_more_than_mins_older_than_the_current_system_time(int mins){
        paymentRequest.setRequestDateTime(testContext.getDateHelper().subtractMinutesFromUTCNowDateTime(mins+2));
        paymentRequest.setTraceId(testContext.getGeneral().generateUniqueUUID());

        logger.info("********** Triggering Payment Request Again With a different Trace Id but request date timestamp more than "+mins+" mins older than current timestamp***********");
        paymentRequest.retrievePaymentRequest(testContext.getRestHelper().getBaseURI()+testContext.getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                accessToken.getClientId(),
                testContext.getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                new HashSet(Arrays.asList(testContext.getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-post").split(","))));

    }

    @When("^I make a check status request again with a different traceid but request date time more than (\\d+) mins older than the current system time$")
    public void i_make_a_check_status_request_again_with_a_different_traceid_but_request_date_time_more_than_mins_older_than_the_current_system_time(int mins){
        paymentStatus.setRequestDateTime(testContext.getDateHelper().subtractMinutesFromUTCNowDateTime(mins+2));
        paymentStatus.setTraceId(testContext.getGeneral().generateUniqueUUID());

        logger.info("********** Triggering Payment Status Again With a different Trace Id but request date timestamp more than "+mins+" mins older than current timestamp***********");
        paymentStatus.retrievePaymentStatus(testContext.getRestHelper().getBaseURI()+testContext.getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                accessToken.getClientId(),
                testContext.getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                new HashSet(Arrays.asList(testContext.getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-get").split(","))));

    }


    @When("^I make a create payment request again with the same traceid but request date time more than (\\d+) mins older than the current system time$")
    public void i_make_a_create_payment_request_again_with_the_same_traceid_but_request_date_time_more_than_mins_older_than_the_current_system_time(int mins){
        paymentRequest.setRequestDateTime(testContext.getDateHelper().subtractMinutesFromUTCNowDateTime(mins+2));

        logger.info("********** Triggering Payment Request Again With the same Trace Id but request date timestamp more than "+mins+" mins older than current timestamp***********");
        paymentRequest.retrievePaymentRequest(testContext.getRestHelper().getBaseURI()+testContext.getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                accessToken.getClientId(),
                testContext.getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                new HashSet(Arrays.asList(testContext.getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-post").split(","))));

    }

    @When("^I make a check status request again with the same traceid but request date time more than (\\d+) mins older than the current system time$")
    public void i_make_a_check_status_request_again_with_the_same_traceid_but_request_date_time_more_than_mins_older_than_the_current_system_time(int mins){
        paymentStatus.setRequestDateTime(testContext.getDateHelper().subtractMinutesFromUTCNowDateTime(mins+2));

        logger.info("********** Triggering Payment Status Again With the same Trace Id but request date timestamp more than "+mins+" mins older than current timestamp***********");
        paymentStatus.retrievePaymentStatus(testContext.getRestHelper().getBaseURI()+testContext.getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                accessToken.getClientId(),
                testContext.getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                new HashSet(Arrays.asList(testContext.getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-get").split(","))));

    }


}



