package steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.testng.Assert;
import utils.BaseStep;


public class Refund_StepDefs implements BaseStep {

    @Given("^I have a \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\"$")
    public void i_have_a(String traceId, String paymentId, String amount, String currency, String reason) {
        refund.setTraceId(traceId);
        refund.setPaymentId(paymentId);
        refund.setRefundAmount(amount);
        refund.setCurrency(currency);
        refund.setReason(reason);

    }

    @When("^I make a request for the refund$")
    public void i_make_a_request_for_the_refund() {
        refund.retrieveRefundRequest(restHelper.getBaseURI()+System.getProperty("version")+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "refund_path"));
    }

    @Then("^I should recieve a refund response with valid trace id in the header$")
    public void i_should_recieve_a_refund_response_with_valid_trace_id_in_the_header()  {
        Assert.assertEquals(refund.getResponseStatusCode(), 201,"Request was not successful!");

        Assert.assertNotNull(refund.traceIdInResponseHeader(), "Trace Id is not present in the response header!!");

        Assert.assertEquals(refund.traceIdInResponseHeader(), checkStatus.getTraceId(),"Trace Id present in the response is not matching with the Trace Id passed in the request!!");

    }

    @Then("^the response body should contain valid refundid, paymentid, amount, currency, status, createdTime$")
    public void the_response_body_should_contain_valid_refundid_paymentid_amount_currency_status_createdTime() {
        Assert.assertNotNull(refund.refundIdInResponse(), "Refund Id is either null or not present in the response!!");

        Assert.assertNotNull(refund.statusInResponse(), "Status is either null or not present in the response!!");

        Assert.assertNotNull(refund.createdTimestampInResponse(), "Created timestamp is either null or not present in the response!!");

        Assert.assertEquals(refund.paymentIdInResponse(), refund.getPaymentId(), "Payment id differs in the response!");

        Assert.assertEquals(refund.amountInResponse(), refund.getRefundAmount(), "Refund amount differs in the response!");

        Assert.assertEquals(refund.currencyInResponse(), refund.getCurrency(), "Currency differs in the response!");

        Assert.assertEquals(refund.reasonInResponse(), refund.getReason(), "Reason differs in the response!");

        Assert.assertEquals(refund.isItAPartialRefund(), refund.partialRefundInResponse(), "Value of partial refund is incorrect!");

    }

    @Then("^I should recieve a (\\d+) error response with \"([^\"]*)\" error description and \"([^\"]*)\" errorcode within refund response$")
    public void i_should_recieve_a_error_response_with_error_description_and_errorcode_within_refund_response(int responseCode, String errorDescription, String errorCode) {
        Assert.assertEquals(refund.getResponseStatusCode(), responseCode,"Different response code being returned");

        Assert.assertEquals(refund.getErrorCode(), errorCode,"Different error code being returned");

        Assert.assertEquals(refund.getErrorDescription(), errorDescription,"Different error description being returned");


    }

}
