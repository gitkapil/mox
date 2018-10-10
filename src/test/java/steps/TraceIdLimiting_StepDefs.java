package steps;

import cucumber.api.java.en.When;
import org.apache.log4j.Logger;
import utils.BaseStep;


public class TraceIdLimiting_StepDefs implements BaseStep{
    final static Logger logger = Logger.getLogger(TraceIdLimiting_StepDefs.class);

    @When("^I make a create payment request again with the same traceid$")
    public void i_make_a_request_again_with_the_same_traceid() {
      paymentRequest.setRequestDateTime(dateHelper.getUTCNowDateTime());

      logger.info("********** Triggering Payment Request Again With the same Trace Id***********");
      paymentRequest.retrievePaymentRequest(restHelper.getBaseURI()+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"));

    }

    @When("^I make a check status request again with the same traceid$")
    public void i_make_a_check_status_request_again_with_the_same_traceid() {
        paymentStatus.setRequestDateTime(dateHelper.getUTCNowDateTime());

        logger.info("********** Triggering Payment Status Again With the same Trace Id***********");
        paymentStatus.retrievePaymentStatus(restHelper.getBaseURI()+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"));

    }

    @When("^I make a create payment request again with the same traceid after (\\d+) mins$")
    public void i_make_a_create_payment_request_again_with_the_same_traceid_after_mins(int mins){
        logger.info("Waiting for "+mins +" mins!");
        general.waitFor(mins*60);
        paymentRequest.setRequestDateTime(dateHelper.getUTCNowDateTime());

        logger.info("********** Triggering Payment Request Again With the same Trace Id After "+mins+" mins ***********");
        paymentRequest.retrievePaymentRequest(restHelper.getBaseURI()+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"));

    }

    @When("^I make a check status request again with the same traceid after (\\d+) mins$")
    public void i_make_a_check_status_request_again_with_the_same_traceid_after_mins(int mins) {
        logger.info("Waiting for "+mins +" mins!");
        general.waitFor(mins*60);
        paymentStatus.setRequestDateTime(dateHelper.getUTCNowDateTime());

        logger.info("********** Triggering Payment Status Again With the same Trace Id After "+mins+" mins ***********");
        paymentStatus.retrievePaymentStatus(restHelper.getBaseURI()+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"));

    }

    @When("^I make a create payment request again with a different traceid$")
    public void i_make_a_request_again_with_a_different_traceid() {
        paymentRequest.setRequestDateTime(dateHelper.getUTCNowDateTime());
        paymentRequest.setTraceId(general.generateUniqueUUID());

        logger.info("********** Triggering Payment Request Again With a different Trace Id***********");
        paymentRequest.retrievePaymentRequest(restHelper.getBaseURI()+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"));

    }

    @When("^I make a check status request again with a different traceid$")
    public void i_make_a_check_status_request_again_with_a_different_traceid() {
        paymentStatus.setRequestDateTime(dateHelper.getUTCNowDateTime());
        paymentStatus.setTraceId(general.generateUniqueUUID());

        logger.info("********** Triggering Payment Status Again With a different Trace Id***********");
        paymentStatus.retrievePaymentStatus(restHelper.getBaseURI()+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"));

    }

    @When("^I make a create payment request again with a different traceid but request date time more than (\\d+) mins older than the current system time$")
    public void i_make_a_create_payment_request_again_with_a_different_traceid_but_request_date_time_more_than_mins_older_than_the_current_system_time(int mins){
        paymentRequest.setRequestDateTime(dateHelper.subtractMinutesFromUTCNowDateTime(mins+2));
        paymentRequest.setTraceId(general.generateUniqueUUID());

        logger.info("********** Triggering Payment Request Again With a different Trace Id but request date timestamp more than "+mins+" mins older than current timestamp***********");
        paymentRequest.retrievePaymentRequest(restHelper.getBaseURI()+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"));

    }

    @When("^I make a check status request again with a different traceid but request date time more than (\\d+) mins older than the current system time$")
    public void i_make_a_check_status_request_again_with_a_different_traceid_but_request_date_time_more_than_mins_older_than_the_current_system_time(int mins){
        paymentStatus.setRequestDateTime(dateHelper.subtractMinutesFromUTCNowDateTime(mins+2));
        paymentStatus.setTraceId(general.generateUniqueUUID());

        logger.info("********** Triggering Payment Status Again With a different Trace Id but request date timestamp more than "+mins+" mins older than current timestamp***********");
        paymentStatus.retrievePaymentStatus(restHelper.getBaseURI()+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"));

    }


    @When("^I make a create payment request again with the same traceid but request date time more than (\\d+) mins older than the current system time$")
    public void i_make_a_create_payment_request_again_with_the_same_traceid_but_request_date_time_more_than_mins_older_than_the_current_system_time(int mins){
        paymentRequest.setRequestDateTime(dateHelper.subtractMinutesFromUTCNowDateTime(mins+2));

        logger.info("********** Triggering Payment Request Again With the same Trace Id but request date timestamp more than "+mins+" mins older than current timestamp***********");
        paymentRequest.retrievePaymentRequest(restHelper.getBaseURI()+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"));

    }

    @When("^I make a check status request again with the same traceid but request date time more than (\\d+) mins older than the current system time$")
    public void i_make_a_check_status_request_again_with_the_same_traceid_but_request_date_time_more_than_mins_older_than_the_current_system_time(int mins){
        paymentStatus.setRequestDateTime(dateHelper.subtractMinutesFromUTCNowDateTime(mins+2));

        logger.info("********** Triggering Payment Status Again With the same Trace Id but request date timestamp more than "+mins+" mins older than current timestamp***********");
        paymentStatus.retrievePaymentStatus(restHelper.getBaseURI()+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"));

    }


}



