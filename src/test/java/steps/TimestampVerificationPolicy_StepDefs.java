package steps;

import apiHelpers.*;
import cucumber.api.java.en.Given;
import org.apache.log4j.Logger;
import utils.BaseStep;



public class TimestampVerificationPolicy_StepDefs{

    TestContext testContext;
    PaymentRequest paymentRequest;
    PaymentStatus paymentStatus;
    Refunds refunds;

    public TimestampVerificationPolicy_StepDefs(TestContext testContext) {
        this.testContext = testContext;
        this.paymentRequest = new PaymentRequest(testContext);
        this.paymentStatus= new PaymentStatus(testContext);
        this.refunds= new Refunds(testContext);
    }
    final static Logger logger = Logger.getLogger(TimestampVerificationPolicy_StepDefs.class);


    @Given("^request date timestamp in the payment request header is less than (\\d+) mins than the current timestamp$")
    public void request_date_timestamp_in_the_payment_request_header_is_less_than_mins_than_the_current_timestamp(int mins) {

        paymentRequest.setRequestDateTime(testContext.getDateHelper().subtractMinutesFromUTCNowDateTime(mins-1));
    }

    @Given("^request date timestamp in the payment request header is exactly (\\d+) mins behind than the current timestamp$")
    public void request_date_timestamp_in_the_payment_request_header_is_exactly_mins_behind_than_the_current_timestamp(int mins) {

        paymentRequest.setRequestDateTime(testContext.getDateHelper().subtractMinutesFromUTCNowDateTime(mins));
    }

    @Given("^request date timestamp in the payment request header is more than (\\d+) mins than the current timestamp$")
    public void request_date_timestamp_in_the_payment_request_header_is_more_than_mins_than_the_current_timestamp(int mins) {

        paymentRequest.setRequestDateTime(testContext.getDateHelper().subtractMinutesFromUTCNowDateTime(mins+2));
    }

    @Given("^request date timestamp in the payment status header is less than (\\d+) mins than the current timestamp$")
    public void request_date_timestamp_in_the_payment_rstatus_header_is_less_than_mins_than_the_current_timestamp(int mins) {

        paymentStatus.setRequestDateTime(testContext.getDateHelper().subtractMinutesFromUTCNowDateTime(mins-1));
    }

    @Given("^request date timestamp in the payment status header is exactly (\\d+) mins behind than the current timestamp$")
    public void request_date_timestamp_in_the_payment_rstatus_header_is_exactly_mins_behind_than_the_current_timestamp(int mins) {

        paymentStatus.setRequestDateTime(testContext.getDateHelper().subtractMinutesFromUTCNowDateTime(mins));
    }

    @Given("^request date timestamp in the payment status header is more than (\\d+) mins than the current timestamp$")
    public void request_date_timestamp_in_the_payment_status_header_is_more_than_mins_than_the_current_timestamp(int mins) {

        paymentStatus.setRequestDateTime(testContext.getDateHelper().subtractMinutesFromUTCNowDateTime(mins+2));
    }

    @Given("^request date timestamp in the refund header is less than (\\d+) mins than the current timestamp$")
    public void request_date_timestamp_in_the_refund_header_is_less_than_mins_than_the_current_timestamp(int mins)  {

        refunds.setRequestDateTime(testContext.getDateHelper().subtractMinutesFromUTCNowDateTime(mins-1));
    }

    @Given("^request date timestamp in the refund header is exactly (\\d+) mins behind than the current timestamp$")
    public void request_date_timestamp_in_the_refund_header_is_exactly_mins_behind_than_the_current_timestamp(int mins) {

        refunds.setRequestDateTime(testContext.getDateHelper().subtractMinutesFromUTCNowDateTime(mins));
    }

    @Given("^request date timestamp in the refund header is more than (\\d+) mins than the current timestamp$")
    public void request_date_timestamp_in_the_refund_header_is_more_than_mins_than_the_current_timestamp(int mins)  {

        refunds.setRequestDateTime(testContext.getDateHelper().subtractMinutesFromUTCNowDateTime(mins+2));
    }


}



