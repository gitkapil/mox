package steps;

import cucumber.api.java.en.Given;
import managers.TestContext;
import managers.UtilManager;
import org.apache.log4j.Logger;


public class TimestampVerificationPolicy_StepDefs extends UtilManager {

    TestContext testContext;

    public TimestampVerificationPolicy_StepDefs(TestContext testContext) {
        this.testContext = testContext;
    }
    final static Logger logger = Logger.getLogger(TimestampVerificationPolicy_StepDefs.class);


    @Given("^request date timestamp in the payment request header is less than (\\d+) mins than the current timestamp$")
    public void request_date_timestamp_in_the_payment_request_header_is_less_than_mins_than_the_current_timestamp(int mins) {

        testContext.getApiManager().getPaymentRequest().setRequestDateTime(getDateHelper().subtractMinutesFromUTCNowDateTime(mins-1));
    }

    @Given("^request date timestamp in the payment request header is exactly (\\d+) mins behind than the current timestamp$")
    public void request_date_timestamp_in_the_payment_request_header_is_exactly_mins_behind_than_the_current_timestamp(int mins) {

        testContext.getApiManager().getPaymentRequest().setRequestDateTime(getDateHelper().subtractMinutesFromUTCNowDateTime(mins));
    }

    @Given("^request date timestamp in the payment request header is more than (\\d+) mins than the current timestamp$")
    public void request_date_timestamp_in_the_payment_request_header_is_more_than_mins_than_the_current_timestamp(int mins) {

        testContext.getApiManager().getPaymentRequest().setRequestDateTime(getDateHelper().subtractMinutesFromUTCNowDateTime(mins+2));
    }

    @Given("^request date timestamp in the payment status header is less than (\\d+) mins than the current timestamp$")
    public void request_date_timestamp_in_the_payment_rstatus_header_is_less_than_mins_than_the_current_timestamp(int mins) {

        testContext.getApiManager().getPaymentStatus().setRequestDateTime(getDateHelper().subtractMinutesFromUTCNowDateTime(mins-1));
    }

    @Given("^request date timestamp in the payment status header is exactly (\\d+) mins behind than the current timestamp$")
    public void request_date_timestamp_in_the_payment_rstatus_header_is_exactly_mins_behind_than_the_current_timestamp(int mins) {

        testContext.getApiManager().getPaymentStatus().setRequestDateTime(getDateHelper().subtractMinutesFromUTCNowDateTime(mins));
    }

    @Given("^request date timestamp in the payment status header is more than (\\d+) mins than the current timestamp$")
    public void request_date_timestamp_in_the_payment_status_header_is_more_than_mins_than_the_current_timestamp(int mins) {

        testContext.getApiManager().getPaymentStatus().setRequestDateTime(getDateHelper().subtractMinutesFromUTCNowDateTime(mins+2));
    }

    @Given("^request date timestamp in the refund header is less than (\\d+) mins than the current timestamp$")
    public void request_date_timestamp_in_the_refund_header_is_less_than_mins_than_the_current_timestamp(int mins)  {

        testContext.getApiManager().getRefunds().setRequestDateTime(getDateHelper().subtractMinutesFromUTCNowDateTime(mins-1));
    }

    @Given("^request date timestamp in the refund header is exactly (\\d+) mins behind than the current timestamp$")
    public void request_date_timestamp_in_the_refund_header_is_exactly_mins_behind_than_the_current_timestamp(int mins) {

        testContext.getApiManager().getRefunds().setRequestDateTime(getDateHelper().subtractMinutesFromUTCNowDateTime(mins));
    }

    @Given("^request date timestamp in the refund header is more than (\\d+) mins than the current timestamp$")
    public void request_date_timestamp_in_the_refund_header_is_more_than_mins_than_the_current_timestamp(int mins)  {

        testContext.getApiManager().getRefunds().setRequestDateTime(getDateHelper().subtractMinutesFromUTCNowDateTime(mins+2));
    }


}



