package apiHelpers;

import com.jayway.restassured.response.Response;
import managers.UtilManager;

import java.util.HashMap;


public class Refunds extends UtilManager {
    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Refunds.class);
    String transactionId, url, authToken, traceId,requestDateTime, currencyCode, reason;
    Double amount;


    Response refundsResponse= null;
    HashMap<String,String> refundsHeader= new HashMap<>();
    HashMap refundsBody= new HashMap<>();

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getRequestDateTime() {
        return requestDateTime;
    }

    public void setRequestDateTime(String requestDateTime) {
        this.requestDateTime = requestDateTime;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setRefundsResponse(Response refundsResponse) {
        this.refundsResponse = refundsResponse;
    }

    public HashMap<String, String> getRefundsHeader() {
        return refundsHeader;
    }

    public void setRefundsHeader(HashMap<String, String> refundsHeader) {
        this.refundsHeader = refundsHeader;
    }

    public HashMap getRefundsBody() {
        return refundsBody;
    }

    public void setRefundsBody(HashMap refundsBody) {
        this.refundsBody = refundsBody;
    }

    public Response getRefundsResponse() {
        return refundsResponse;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setAuthTokenwithBearer() {

        this.authToken = "Bearer "+ authToken;
    }

    public String refundIdInResponse(){
        return getRestHelper().getResponseBodyValue(refundsResponse, "refundId");

    }

    public String amountInResponse(){
        return getRestHelper().getResponseBodyValue(refundsResponse, "amount");

    }

    public String currencyCodeInResponse(){
        return getRestHelper().getResponseBodyValue(refundsResponse, "currencyCode");

    }

    public String reasonCodeInResponse(){
        return getRestHelper().getResponseBodyValue(refundsResponse, "reasonCode");

    }

    public String reasonInResponse(){
        return getRestHelper().getResponseBodyValue(refundsResponse, "reason");

    }

    public String transactionIdInResponse(){
        return getRestHelper().getResponseBodyValue(refundsResponse, "transactionId");

    }





    public void retrieveRefundWithMissingHeaderKeys(String urlPart1, String urlPart2, String key){
        url= addTransactionIdInURL(urlPart1, urlPart2);

        HashMap<String, String> header= returnRefundsHeader();
        header.remove(key);

        refundsResponse= getRestHelper().postRequestWithHeaderAndBody(url,header, returnRefundsBody());

        logger.info("********** Refunds Response *********** ---> "+ refundsResponse.getBody().asString());

    }

    public void retrieveRefundWithMissingBodyKeys(String urlPart1, String urlPart2, String key){
        url= addTransactionIdInURL(urlPart1, urlPart2);

        HashMap<String, String> body= returnRefundsBody();
        body.remove(key);

        refundsResponse= getRestHelper().postRequestWithHeaderAndBody(url,returnRefundsHeader(), body);

        logger.info("********** Refunds Response *********** ---> "+ refundsResponse.getBody().asString());

    }

    public HashMap returnRefundsBody(){
        refundsBody=new HashMap();

        refundsBody.put("amount", amount);

        if (!currencyCode.equals(""))
        {
            if (!currencyCode.equals("no_value"))
                refundsBody.put("currencyCode", currencyCode);
            else
            {
                refundsBody.put("currencyCode", "");
            }
        }


        if (!reason.equals(""))
        {
            if (!reason.equals("no_value"))
                refundsBody.put("reason", reason);
            else
            {
                refundsBody.put("reason", "");
                reason="";
            }
        }
        else
            reason=null;

        return refundsBody;
    }



    public Response retrieveRefunds(String urlPart1, String urlPart2){
        url= addTransactionIdInURL(urlPart1, urlPart2);

        refundsResponse= getRestHelper().postRequestWithHeaderAndBody(url,returnRefundsHeader(), returnRefundsBody());

        logger.info("********** Refunds Response *********** ---> "+ refundsResponse.getBody().asString());

        return refundsResponse;
    }

    public String addTransactionIdInURL(String urlPart1, String urlPart2){
        return urlPart1+"/"+transactionId+urlPart2;
    }

    public String addTransactionIdInURL(String urlPart1, String urlPart2, String transactionId){
        return urlPart1+"/"+transactionId+urlPart2;
    }


    public HashMap<String,String> returnRefundsHeader(){

        refundsHeader= new HashMap<>();
        refundsHeader.put("Accept","application/json");
        refundsHeader.put("Content-Type","application/json");
        refundsHeader.put("Authorization", authToken);
        refundsHeader.put("Trace-Id",traceId);
        refundsHeader.put("Api-Version", System.getProperty("version"));
        refundsHeader.put("Request-Date-Time", requestDateTime);

        return refundsHeader;
    }
}
