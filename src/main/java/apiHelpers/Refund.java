package apiHelpers;

import com.jayway.restassured.response.Response;
import utils.BaseStep;
import java.util.HashMap;

public class Refund implements BaseStep {
    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Refund.class);
    private String authToken, traceId, paymentId, refundAmount, currency, reason;
    private boolean partialRefund= true;

    //TODO: Hardcoding the value matching the stub, once we have a persistence layer within mock layer of DRAGON then we can query the transaction id and the correspondingoriginal amount
    private static Double originalAmount=100.0;

    HashMap<String, String> refundHeader= new HashMap<String, String>();
    HashMap<String, String> refundBody= new HashMap<String, String>();

    public Response getRefundResponse() {
        return refundResponse;
    }

    Response refundResponse= null;

    public static Double getOriginalAmount() {
        return originalAmount;
    }

    public static void setOriginalAmount(Double originalAmount) {
        Refund.originalAmount = originalAmount;
    }

    public boolean isPartialRefund() {
        return partialRefund;
    }

    public void setPartialRefund(boolean partialRefund) {
        this.partialRefund = partialRefund;
    }

    public String getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTraceId() {
        return traceId;
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
        refundBody.put("amount", refundAmount);
        refundBody.put("currency", currency);
        refundBody.put("reason", reason);

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

    public String reasonInResponse(){
        return restHelper.getResponseBodyValue(refundResponse, "reason");

    }

    public String createdTimestampInResponse(){
        return restHelper.getResponseBodyValue(refundResponse, "createdTime");

    }

    public String statusInResponse(){
        return restHelper.getResponseBodyValue(refundResponse, "status");

    }

    public String partialRefundInResponse(){
        return restHelper.getResponseBodyValue(refundResponse, "partialRefund");

    }


   public  String isItAPartialRefund(){
        if (originalAmount== Double.parseDouble(refundAmount))
            return "T";

        return "F";
   }

}
