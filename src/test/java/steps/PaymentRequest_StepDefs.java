package steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.log4j.Logger;
import org.testng.Assert;
import utils.BaseStep;

import java.util.Properties;

public class PaymentRequest_StepDefs implements BaseStep {

    final static Logger logger = Logger.getLogger(PaymentRequest_StepDefs.class);

   // Properties generalProperties= loadGeneralProperties();



    @Given("^I am an authorized merchant$")
    public void i_am_an_authorized_merchant()  {

      paymentRequest.setAuthToken(accessToken.getAccessToken().path("access_token").toString());
      checkStatus.setAuthToken(accessToken.getAccessToken().path("access_token").toString());
      refund.setAuthToken(accessToken.getAccessToken().path("access_token").toString());

    }

    @Given("^I am a merchant with invalid auth token$")
    public void i_am_an_authorized_merchant_invalid_token()  {
        paymentRequest.setAuthToken("random_authtoken");
        checkStatus.setPaymentId("random_authtoken");
        refund.setPaymentId("random_authtoken");

    }

    @Given("^I am a merchant with missing auth token$")
    public void i_am_an_authorized_merchant_missing_token()  {
        paymentRequest.setAuthToken("");
        checkStatus.setAuthToken("");
        refund.setAuthToken("");
    }



    @Given("^I have transaction details \"([^\"]*)\", \"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\"$")
    public void i_have_valid_transaction_details(String traceId, String amount, String currency, String description, String channel, String invoiceId, String merchantId, String effectiveDuration, String returnURL)  {
       paymentRequest.createTransaction(traceId,amount,currency,description,channel,invoiceId,merchantId,effectiveDuration,returnURL);
    }


    @When("^I make a request for the payment$")
    public void i_make_a_request_for_the_payment()  {
        paymentRequest.retrievePaymentRequest(restHelper.getBaseURI()+System.getProperty("version")+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_path"));
        
    }

    @Then("^I should recieve a payment response with valid trace id in the header$")
    public void i_should_recieve_a_response_with_valid_trace_id_in_the_header()  {

        Assert.assertEquals(paymentRequest.getResponseStatusCode(), 201,"Request was not successful!");

       // Assert.assertNotNull(paymentRequest.traceIdInResponseHeader(), "Trace Id is not present in the response header!!");

       // Assert.assertEquals(paymentRequest.traceIdInResponseHeader(), paymentRequest.getTraceId(),"Trace Id present in the response is not matching with the Trace Id passed in the request!!");
        
    }

    @Then("^the response body should contain valid payment id, created timestamp, transaction details, links$")
    public void the_response_body_should_contain_valid_payment_id_created_timestamp_transaction_details_links() throws Throwable {
        Assert.assertNotNull(paymentRequest.paymentIdInResponse(), "Payment Id is not present in the response!!");

        Assert.assertNotNull(paymentRequest.createdTimestampInResponse(), "Created Timestamp is not present in the response!!");

        Assert.assertTrue(paymentRequest.isLinksValid(),"Links within response is either incomplete or incorrect..Please check!!");

        //Assert.assertTrue(paymentRequest.isTransactionValid(),"Transaction Details within response is either incomplete or incorrect..Please check!!");

        Assert.assertNull(paymentRequest.isTransactionValid(), paymentRequest.isTransactionValid()+ "..Please check!");

    }


    @Then("^I should recieve a (\\d+) error response with \"([^\"]*)\" error description and \"([^\"]*)\" errorcode within payment response$")
    public void i_should_recieve_a_error_response_with_error_description_and_errorcode(int responseCode, String errorDesc, String errorCode) {
        Assert.assertEquals(paymentRequest.getResponseStatusCode(), responseCode,"Different response code being returned");

        Assert.assertEquals(paymentRequest.getErrorCode(), errorCode,"Different error code being returned");

        Assert.assertEquals(paymentRequest.getErrorDescription(), errorDesc,"Different error description being returned");

    }

}
