package apiHelpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.response.Response;
import managers.UtilManager;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import utils.PropertyHelper;

import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;


public class Refunds extends UtilManager {
    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Refunds.class);

    private String transactionId, url, authToken, traceId,requestDateTime;
    private String amount;
    private String currencyCode;
    private String reasonCode;
    private String payerId;
    private String reasonMessage;
    private Response refundsResponse= null;
    private HashMap<String,String> refundsHeader= new HashMap<>();
    private HashMap refundsBody= new HashMap<>();

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }


    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getReasonMessage() {
        return reasonMessage;
    }

    public void setReasonMessage(String reasonMessage) {
        this.reasonMessage = reasonMessage;
    }

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

    public void setAuthTokenWithBearer(String value) {
        this.authToken = "Bearer " + value;
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
    public void retrieveRefundWithMissingHeaderKeys(String urlPart1, String urlPart2, String signingKeyId, String key, String signingAlgorithm, String signingKey, HashSet headerElementsForSignature){
        url= addTransactionIdInURL(urlPart1, urlPart2);

        HashMap<String, String> header = returnRefundsHeader(signingKeyId, signingKey, signingAlgorithm, headerElementsForSignature);
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
    public void retrieveRefundWithMissingBodyKeys(String urlPart1, String urlPart2, String signingKeyId,  String key, String signingAlgorithm, String signingKey, HashSet headerElementsForSignature){
        url= addTransactionIdInURL(urlPart1, urlPart2);

        HashMap<String, String> body= returnRefundsBody();
        body.remove(key);

        refundsResponse= getRestHelper().postRequestWithHeaderAndBody(url,returnRefundsHeader(signingKeyId, signingKey, signingAlgorithm, headerElementsForSignature), body);

        logger.info("********** Refunds Response *********** ---> "+ refundsResponse.getBody().asString());

    }

    /**
     * This methid creates a valid body for POST refund endpoint
     * @return
     */
    public HashMap returnRefundsBody(){
        refundsBody=new HashMap();

        if (payerId != null && !payerId.equalsIgnoreCase("null")) {
            if (payerId.equalsIgnoreCase("smalllength")) {
                refundsBody.put("payerId", StringUtils.repeat("*", 21));
            } else if (payerId.equalsIgnoreCase("biglength")) {
                refundsBody.put("payerId", StringUtils.repeat("*", 23));
            } else {
                refundsBody.put("payerId", payerId);
            }
        }
        if (amount!= null && !amount.equalsIgnoreCase("null")) {
            refundsBody.put("amount", Double.parseDouble(amount));
        }
        if (currencyCode != null && !currencyCode.equalsIgnoreCase("null")) {
            refundsBody.put("currencyCode", currencyCode);
        }
        if (reasonCode != null && !reasonCode.equalsIgnoreCase("null")) {
            refundsBody.put("reasonCode", reasonCode);
        }
        if (reasonMessage != null && !reasonMessage.equalsIgnoreCase("null")) {
            if (reasonMessage.equalsIgnoreCase("biglength")) {
                refundsBody.put("reasonMessage", StringUtils.repeat("*", 150));
            } else {
                refundsBody.put("reasonMessage", reasonMessage);
            }
        }

        return refundsBody;
    }


    /**
     * This method creates valid header & body and hits the POST refund endpoint
     * @param urlPart1
     * @param urlPart2
     * @return
     */
    public Response retrieveRefunds(String urlPart1, String urlPart2, String signingKeyId, String signingAlgorithm, String signingKey, HashSet headerElementsForSignature){
        url= addTransactionIdInURL(urlPart1, urlPart2);

        refundsResponse= getRestHelper().postRequestWithHeaderAndBody(url,returnRefundsHeader(signingKeyId, signingKey, signingAlgorithm, headerElementsForSignature), returnRefundsBody());

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
        if (transactionId.equalsIgnoreCase("null")) {
            return urlPart1+"/"+urlPart2;
        }
        return urlPart1+"/"+transactionId+urlPart2;
    }

    public String addTransactionIdInURL(String urlPart1, String urlPart2, String transactionId){
        return urlPart1+"/"+transactionId+urlPart2;
    }


    /**
     * This method creates a valid body for the POST refunds endpoint
     * @return
     */
    public HashMap<String,String> returnRefundsHeader(String signingKeyId, String signingKey, String signingAlgorithm, HashSet headerElementsForSignature){

        refundsHeader= new HashMap<>();
        refundsHeader.put("Accept","application/json");
        refundsHeader.put("Content-Type","application/json");
        refundsHeader.put("Authorization", authToken);
        refundsHeader.put("Trace-Id",getGeneral().generateUniqueUUID());
        refundsHeader.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));
        refundsHeader.put("Request-Date-Time", getDateHelper().getUTCNowDateTime());
        try {
            refundsHeader.put("Digest", getSignatureHelper().calculateContentDigestHeader(new ObjectMapper().writeValueAsBytes(returnRefundsBody())));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            Assert.assertTrue("Trouble creating Digest!", false);
        }

        try{
            byte[] sigKey = Base64.getDecoder().decode(signingKey);
            String splitstr[] = url.split("/payments");
            String signature = getSignatureHelper().calculateSignature("POST", splitstr[1], sigKey,
                    signingAlgorithm, signingKeyId, headerElementsForSignature, refundsHeader);
            refundsHeader.put("Signature", signature);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Assert.assertTrue("Trouble creating Signature!", false);
        }

        return refundsHeader;
    }
}
