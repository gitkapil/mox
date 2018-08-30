package apiHelpers;

import com.jayway.restassured.response.Response;
import cucumber.api.DataTable;
import utils.BaseStep;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PaymentStatus implements BaseStep {
    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(PaymentStatus.class);

    String paymentRequestId, traceId, authToken;
    Response paymentStatusResponse= null;
    HashMap<String,String> paymentStatusHeader= new HashMap<>();

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setPaymentRequestId(String paymentRequestId) {

        this.paymentRequestId = paymentRequestId;
    }

    public Response getPaymentStatusResponse() {
        return paymentStatusResponse;
    }

    public void setAuthTokenwithBearer() {

        this.authToken = "Bearer "+ authToken;
    }

    public String statusDescriptionInResponse(){
        return restHelper.getResponseBodyValue(paymentStatusResponse, "statusDescription");

    }

    public String statusCodeInResponse(){
        return restHelper.getResponseBodyValue(paymentStatusResponse, "statusCode");

    }

    public void retrievePaymentStatusWithMissingHeaderKeys(String url, String key){
        url= appendPaymentIdInURL(url);

        HashMap<String, String> header= returnPaymentStatusHeader();
        header.remove(key);

        paymentStatusResponse= restHelper.getRequestWithHeaders(url, header);

        logger.info("********** Payment Request Status Response *********** ---> "+ paymentStatusResponse.getBody().asString());

    }



    public Response retrievePaymentStatus(String url){
        url= appendPaymentIdInURL(url);

        paymentStatusResponse= restHelper.getRequestWithHeaders(url, returnPaymentStatusHeader() );

        logger.info("********** Payment Request Status Response *********** ---> "+ paymentStatusResponse.getBody().asString());

        return paymentStatusResponse;
    }

    public String appendPaymentIdInURL(String url){
        return url+"/"+paymentRequestId;
    }

    public String appendPaymentIdInURL(String url, String paymentId){
        return url+"/"+paymentId;
    }


    public HashMap<String,String> returnPaymentStatusHeader(){
        paymentStatusHeader.put("Accept","application/json");
        paymentStatusHeader.put("Content-Type","application/json");
        paymentStatusHeader.put("Authorization", authToken);
        paymentStatusHeader.put("TraceId",traceId);

        return paymentStatusHeader;
    }
}
