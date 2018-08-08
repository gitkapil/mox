package apiHelpers;

import com.jayway.restassured.response.Response;
import utils.BaseStep;
import java.util.HashMap;


public class CheckStatus implements BaseStep {
    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(CheckStatus.class);
    private String authToken, traceId, paymentId;


    HashMap<String, String> checkStatusRequestHeader= new HashMap<String, String>();

    Response checkStatusResponse= null;

    public Response getCheckStatusResponse() {
        return checkStatusResponse;
    }


    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getAuthToken() {

        return authToken;
    }


    public void setAuthToken(String authToken) {

        this.authToken = authToken;
    }



    public HashMap<String,String> returnCheckStatusHeader(){
        checkStatusRequestHeader.put("Accept","application/json");
        checkStatusRequestHeader.put("Content-Type","application/json");
        checkStatusRequestHeader.put("Authorization",authToken);
        checkStatusRequestHeader.put("TraceId",traceId);

        return checkStatusRequestHeader;
    }


    public Response retrieveCheckStatusRequest(String url){
        url=url+"/"+paymentId;

        checkStatusResponse= restHelper.getRequestWithHeaders(url, returnCheckStatusHeader());

        return checkStatusResponse;
    }

    public String traceIdInResponseHeader(){
        return restHelper.getResponseHeaderValue(checkStatusResponse, "TraceId");

    }

    public String paymentIdInResponse(){
        return restHelper.getResponseBodyValue(checkStatusResponse, "paymentId");

    }

    public String statusInResponse(){
        return restHelper.getResponseBodyValue(checkStatusResponse, "status");

    }

}
