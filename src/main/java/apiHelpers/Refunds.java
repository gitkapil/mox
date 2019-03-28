package apiHelpers;

import com.jayway.restassured.response.Response;
import managers.UtilManager;
import utils.PropertyHelper;

import java.util.HashMap;


public class Refunds extends UtilManager {
    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Refunds.class);

    private String transactionId, url, authToken, traceId,requestDateTime, currencyCode, reason;
    private Double amount;
    private Response refundsResponse= null;
    private HashMap<String,String> refundsHeader= new HashMap<>();
    private HashMap refundsBody= new HashMap<>();

    /**
     *
     * Getters
     */
    public String getTransactionId() {
        return transactionId;
    }

    public String getUrl() {
        return url;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getTraceId() {
        return traceId;
    }

    public String getRequestDateTime() {
        return requestDateTime;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getReason() {
        return reason;
    }

    public Double getAmount() {
        return amount;
    }

    public HashMap<String, String> getRefundsHeader() {
        return refundsHeader;
    }

    public HashMap getRefundsBody() {
        return refundsBody;
    }

    public Response getRefundsResponse() {
        return refundsResponse;
    }





    /**
     *
     * Setters
     */
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public void setRequestDateTime(String requestDateTime) {
        this.requestDateTime = requestDateTime;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setRefundsResponse(Response refundsResponse) {
        this.refundsResponse = refundsResponse;
    }

    public void setRefundsHeader(HashMap<String, String> refundsHeader) {
        this.refundsHeader = refundsHeader;
    }

    public void setRefundsBody(HashMap refundsBody) {
        this.refundsBody = refundsBody;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setAuthTokenwithBearer() {

        this.authToken = "Bearer "+ authToken;
    }


    /**
     *
     * returns refundId from the response
     */
    public String refundIdInResponse(){
        return getRestHelper().getResponseBodyValue(refundsResponse, "refundId");

    }

    /**
     *
     * returns refundId from the response
     */
    public String amountInResponse(){
        return getRestHelper().getResponseBodyValue(refundsResponse, "amount");

    }

    /**
     *
     * returns refundId from the response
     */
    public String currencyCodeInResponse(){
        return getRestHelper().getResponseBodyValue(refundsResponse, "currencyCode");

    }

    /**
     *
     * returns refundId from the response
     */
    public String reasonCodeInResponse(){
        return getRestHelper().getResponseBodyValue(refundsResponse, "reasonCode");

    }

    /**
     *
     * returns refundId from the response
     */
    public String reasonInResponse(){
        return getRestHelper().getResponseBodyValue(refundsResponse, "reason");

    }

    /**
     *
     * returns refundId from the response
     */
    public String transactionIdInResponse(){
        return getRestHelper().getResponseBodyValue(refundsResponse, "transactionId");

    }

    /**
     * This methid hits POST refund endpoint with invalid header. "Key" values are missing from the header
     * @param urlPart1
     * @param urlPart2
     * @param key
     */
    public void retrieveRefundWithMissingHeaderKeys(String urlPart1, String urlPart2, String key){
        url= addTransactionIdInURL(urlPart1, urlPart2);

        HashMap<String, String> header= returnRefundsHeader();
        header.remove(key);

        refundsResponse= getRestHelper().postRequestWithHeaderAndBody(url,header, returnRefundsBody());

        logger.info("********** Refunds Response *********** ---> "+ refundsResponse.getBody().asString());

    }

    /**
     * This methid hits POST refund endpoint with invalid body. "Key" values are missing from the body
     * @param urlPart1
     * @param urlPart2
     * @param key
     */
    public void retrieveRefundWithMissingBodyKeys(String urlPart1, String urlPart2, String key){
        url= addTransactionIdInURL(urlPart1, urlPart2);

        HashMap<String, String> body= returnRefundsBody();
        body.remove(key);

        refundsResponse= getRestHelper().postRequestWithHeaderAndBody(url,returnRefundsHeader(), body);

        logger.info("********** Refunds Response *********** ---> "+ refundsResponse.getBody().asString());

    }

    /**
     * This methid creates a valid body for POST refund endpoint
     * @return
     */
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


    /**
     * This method creates valid header & body and hits the POST refund endpoint
     * @param urlPart1
     * @param urlPart2
     * @return
     */
    public Response retrieveRefunds(String urlPart1, String urlPart2){
        url= addTransactionIdInURL(urlPart1, urlPart2);

        refundsResponse= getRestHelper().postRequestWithHeaderAndBody(url,returnRefundsHeader(), returnRefundsBody());

        logger.info("********** Refunds Response *********** ---> "+ refundsResponse.getBody().asString());

        return refundsResponse;
    }


    /**
     * Following two methods appends transaction id in endpoint
     * @param urlPart1
     * @param urlPart2
     * @return
     */
    public String addTransactionIdInURL(String urlPart1, String urlPart2){
        return urlPart1+"/"+transactionId+urlPart2;
    }

    public String addTransactionIdInURL(String urlPart1, String urlPart2, String transactionId){
        return urlPart1+"/"+transactionId+urlPart2;
    }


    /**
     * This method creates a valid body for the POST refunds endpoint
     * @return
     */
    public HashMap<String,String> returnRefundsHeader(){

        refundsHeader= new HashMap<>();
        refundsHeader.put("Accept","application/json");
        refundsHeader.put("Content-Type","application/json");
        refundsHeader.put("Authorization", authToken);
        refundsHeader.put("Trace-Id",traceId);
        refundsHeader.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));
        refundsHeader.put("Request-Date-Time", requestDateTime);

        return refundsHeader;
    }
}
