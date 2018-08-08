package steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.testng.Assert;
import utils.BaseStep;


public class CheckStatus_StepDefs implements BaseStep {

    @Given("^I have a \"([^\"]*)\"$")
    public void i_have_a_and(String paymentid)  {
        checkStatus.setTraceId(general.generateUniqueUUID());
        checkStatus.setPaymentId(paymentid);
    }

    @When("^I make a request for the check status$")
    public void i_make_a_request_for_the_check_status() {
        checkStatus.retrieveCheckStatusRequest(restHelper.getBaseURI()+System.getProperty("version")+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "check_status_path"));
    }

    @Then("^I should recieve a check status response with valid trace id in the header$")
    public void i_should_recieve_a_response_with_valid_trace_id_in_the_header()  {
        Assert.assertEquals(restHelper.getResponseStatusCode(checkStatus.getCheckStatusResponse()), 200,"Request was not successful!");

        Assert.assertNotNull(checkStatus.traceIdInResponseHeader(), "Trace Id is not present in the response header!!");

        Assert.assertEquals(checkStatus.traceIdInResponseHeader(), checkStatus.getTraceId(),"Trace Id present in the response is not matching with the Trace Id passed in the request!!");

    }

    @Then("^the response body should contain valid paymentid, status$")
    public void the_response_body_should_contain_valid_paymentid_status()  {
        Assert.assertNotNull(checkStatus.paymentIdInResponse(), "Payment Id is not present or is null in the response!!");

        Assert.assertNotNull(checkStatus.statusInResponse(), "Status is not present or is null in the response!!");

        Assert.assertEquals(checkStatus.paymentIdInResponse(), checkStatus.getPaymentId(),"Payment Id present in the response is not matching with the Payment Id passed in the request!!");

    }

    @Then("^I should recieve a (\\d+) error response with \"([^\"]*)\" error description and \"([^\"]*)\" errorcode within check status response$")
    public void i_should_recieve_a_error_response_with_error_description_and_errorcode_within_check_status_response(int responseCode, String errorDesc, String errorCode) {

        Assert.assertEquals(restHelper.getResponseStatusCode(checkStatus.getCheckStatusResponse()), responseCode,"Different response code being returned");

        Assert.assertEquals(restHelper.getErrorCode(checkStatus.getCheckStatusResponse()), errorCode,"Different error code being returned");

        Assert.assertEquals(restHelper.getErrorDescription(checkStatus.getCheckStatusResponse()), errorDesc,"Different error description being returned");


    }

}
