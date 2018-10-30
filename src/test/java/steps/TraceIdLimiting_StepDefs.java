package steps;

import apiHelpers.TestContext;
import cucumber.api.java.en.When;
import managers.UtilManager;
import org.apache.log4j.Logger;
import java.util.Arrays;
import java.util.HashSet;


public class TraceIdLimiting_StepDefs extends UtilManager {

    TestContext testContext;

    public TraceIdLimiting_StepDefs(TestContext testContext) {
        this.testContext = testContext;
    }
    final static Logger logger = Logger.getLogger(TraceIdLimiting_StepDefs.class);

    @When("^I make a create payment request again with the same traceid$")
    public void i_make_a_request_again_with_the_same_traceid() {
      testContext.getApiManager().getPaymentRequest().setRequestDateTime(getDateHelper().getUTCNowDateTime());

      logger.info("********** Triggering Payment Request Again With the same Trace Id***********");
      testContext.getApiManager().getPaymentRequest().retrievePaymentRequest(getRestHelper().getBaseURI()+getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
              testContext.getApiManager().getAccessToken().getClientId(),
              getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
              getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
              new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-post").split(","))));

    }

    @When("^I make a check status request again with the same traceid$")
    public void i_make_a_check_status_request_again_with_the_same_traceid() {
        testContext.getApiManager().getPaymentStatus().setRequestDateTime(getDateHelper().getUTCNowDateTime());

        logger.info("********** Triggering Payment Status Again With the same Trace Id***********");
        testContext.getApiManager().getPaymentStatus().retrievePaymentStatus(getRestHelper().getBaseURI()+getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                testContext.getApiManager().getAccessToken().getClientId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-get").split(","))));

    }

    @When("^I make a create payment request again with the same traceid after (\\d+) mins$")
    public void i_make_a_create_payment_request_again_with_the_same_traceid_after_mins(int mins){
        logger.info("Waiting for "+mins +" mins!");
        getGeneral().waitFor(mins*60);
        testContext.getApiManager().getPaymentRequest().setRequestDateTime(getDateHelper().getUTCNowDateTime());

        logger.info("********** Triggering Payment Request Again With the same Trace Id After "+mins+" mins ***********");
        testContext.getApiManager().getPaymentRequest().retrievePaymentRequest(getRestHelper().getBaseURI()+getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                testContext.getApiManager().getAccessToken().getClientId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-post").split(","))));

    }

    @When("^I make a check status request again with the same traceid after (\\d+) mins$")
    public void i_make_a_check_status_request_again_with_the_same_traceid_after_mins(int mins) {
        logger.info("Waiting for "+mins +" mins!");
        getGeneral().waitFor(mins*60);
        testContext.getApiManager().getPaymentStatus().setRequestDateTime(getDateHelper().getUTCNowDateTime());

        logger.info("********** Triggering Payment Status Again With the same Trace Id After "+mins+" mins ***********");
        testContext.getApiManager().getPaymentStatus().retrievePaymentStatus(getRestHelper().getBaseURI()+getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                testContext.getApiManager().getAccessToken().getClientId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-get").split(","))));

    }

    @When("^I make a create payment request again with a different traceid$")
    public void i_make_a_request_again_with_a_different_traceid() {
        testContext.getApiManager().getPaymentRequest().setRequestDateTime(getDateHelper().getUTCNowDateTime());
        testContext.getApiManager().getPaymentRequest().setTraceId(getGeneral().generateUniqueUUID());

        logger.info("********** Triggering Payment Request Again With a different Trace Id***********");
        testContext.getApiManager().getPaymentRequest().retrievePaymentRequest(getRestHelper().getBaseURI()+getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                testContext.getApiManager().getAccessToken().getClientId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-post").split(","))));

    }

    @When("^I make a check status request again with a different traceid$")
    public void i_make_a_check_status_request_again_with_a_different_traceid() {
        testContext.getApiManager().getPaymentStatus().setRequestDateTime(getDateHelper().getUTCNowDateTime());
        testContext.getApiManager().getPaymentStatus().setTraceId(getGeneral().generateUniqueUUID());

        logger.info("********** Triggering Payment Status Again With a different Trace Id***********");
        testContext.getApiManager().getPaymentStatus().retrievePaymentStatus(getRestHelper().getBaseURI()+getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                testContext.getApiManager().getAccessToken().getClientId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-get").split(","))));

    }

    @When("^I make a create payment request again with a different traceid but request date time more than (\\d+) mins older than the current system time$")
    public void i_make_a_create_payment_request_again_with_a_different_traceid_but_request_date_time_more_than_mins_older_than_the_current_system_time(int mins){
        testContext.getApiManager().getPaymentRequest().setRequestDateTime(getDateHelper().subtractMinutesFromUTCNowDateTime(mins+2));
        testContext.getApiManager().getPaymentRequest().setTraceId(getGeneral().generateUniqueUUID());

        logger.info("********** Triggering Payment Request Again With a different Trace Id but request date timestamp more than "+mins+" mins older than current timestamp***********");
        testContext.getApiManager().getPaymentRequest().retrievePaymentRequest(getRestHelper().getBaseURI()+getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                testContext.getApiManager().getAccessToken().getClientId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-post").split(","))));

    }

    @When("^I make a check status request again with a different traceid but request date time more than (\\d+) mins older than the current system time$")
    public void i_make_a_check_status_request_again_with_a_different_traceid_but_request_date_time_more_than_mins_older_than_the_current_system_time(int mins){
        testContext.getApiManager().getPaymentStatus().setRequestDateTime(getDateHelper().subtractMinutesFromUTCNowDateTime(mins+2));
        testContext.getApiManager().getPaymentStatus().setTraceId(getGeneral().generateUniqueUUID());

        logger.info("********** Triggering Payment Status Again With a different Trace Id but request date timestamp more than "+mins+" mins older than current timestamp***********");
        testContext.getApiManager().getPaymentStatus().retrievePaymentStatus(getRestHelper().getBaseURI()+getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                testContext.getApiManager().getAccessToken().getClientId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-get").split(","))));

    }


    @When("^I make a create payment request again with the same traceid but request date time more than (\\d+) mins older than the current system time$")
    public void i_make_a_create_payment_request_again_with_the_same_traceid_but_request_date_time_more_than_mins_older_than_the_current_system_time(int mins){
        testContext.getApiManager().getPaymentRequest().setRequestDateTime(getDateHelper().subtractMinutesFromUTCNowDateTime(mins+2));

        logger.info("********** Triggering Payment Request Again With the same Trace Id but request date timestamp more than "+mins+" mins older than current timestamp***********");
        testContext.getApiManager().getPaymentRequest().retrievePaymentRequest(getRestHelper().getBaseURI()+getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                testContext.getApiManager().getAccessToken().getClientId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-post").split(","))));

    }

    @When("^I make a check status request again with the same traceid but request date time more than (\\d+) mins older than the current system time$")
    public void i_make_a_check_status_request_again_with_the_same_traceid_but_request_date_time_more_than_mins_older_than_the_current_system_time(int mins){
        testContext.getApiManager().getPaymentStatus().setRequestDateTime(getDateHelper().subtractMinutesFromUTCNowDateTime(mins+2));

        logger.info("********** Triggering Payment Status Again With the same Trace Id but request date timestamp more than "+mins+" mins older than current timestamp***********");
        testContext.getApiManager().getPaymentStatus().retrievePaymentStatus(getRestHelper().getBaseURI()+getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                testContext.getApiManager().getAccessToken().getClientId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-get").split(","))));

    }


}



