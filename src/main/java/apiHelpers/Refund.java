package apiHelpers;

import com.jayway.restassured.response.Response;
import utils.BaseStep;

import java.util.HashMap;
import java.util.List;


public class Refund implements BaseStep {
    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Refund.class);
    private String authToken, traceId, paymentId, amount, currency;


    HashMap<String, String> refundHeader= new HashMap<String, String>();
    HashMap<String, String> refundBody= new HashMap<String, String>();

    Response refundResponse= null;


    public String getTraceId() {
        return traceId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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



    public HashMap<String,String> returnRefundHeader(){
        refundHeader.put("Accept","application/json");
        refundHeader.put("Content-Type","application/json");
        refundHeader.put("Authorization",authToken);
        refundHeader.put("TraceId",traceId);

        return refundHeader;
    }

    public HashMap<String,String> returnRefundBody(){
        refundBody.put("paymentId", paymentId);
        refundBody.put("amount", amount);
        refundBody.put("currency", currency);

        return refundBody;
    }


    public Response retrieveRefundRequest(String url){

        refundResponse= restHelper.postRequestWithHeaderAndBody(url, returnRefundHeader(), returnRefundBody());

        return refundResponse;
    }

    public String traceIdInResponseHeader(){
        return restHelper.getResponseHeaderValue(refundResponse, "TraceId");

    }

    public String paymentIdInResponse(){
        return restHelper.getResponseBodyValue(refundResponse, "paymentId");

    }

    public String refundIdInResponse(){
        return restHelper.getResponseBodyValue(refundResponse, "refundId");

    }

    public String amountInResponse(){
        return restHelper.getResponseBodyValue(refundResponse, "amount");

    }

    public String currencyInResponse(){
        return restHelper.getResponseBodyValue(refundResponse, "currency");

    }

    public String createdTimestampInResponse(){
        return restHelper.getResponseBodyValue(refundResponse, "createdTime");

    }

    public String statusInResponse(){
        return restHelper.getResponseBodyValue(refundResponse, "status");

    }


    public int getResponseStatusCode(){
        return refundResponse.statusCode();
    }

    public String getErrorMessage(){
        return restHelper.getResponseBodyValue(refundResponse, "message");

    }

    public String getErrorDescription(){

        List<HashMap<String, String>> errorDetails= restHelper.getJsonArray(refundResponse, "errors");

        String errorDesc=null;


        try{
            errorDesc= errorDetails.get(0).get("errorDescription");

        } catch (NullPointerException e){
            return null;
        }

        return errorDesc;
    }


    public String getErrorCode(){

        List<HashMap<String, String>> errorDetails= restHelper.getJsonArray(refundResponse, "errors");

        String errorCode=null;


        try{
            errorCode= errorDetails.get(0).get("errorCode");

        } catch (NullPointerException e){
            return null;
        }

        return errorCode;
    }




}
