package apiHelpers;


import com.jayway.restassured.response.Response;
import managers.UtilManager;
import org.junit.Assert;
import utils.EnvHelper;
import utils.PropertyHelper;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class PaymentStatus extends UtilManager {
    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(PaymentStatus.class);

    private String paymentRequestId, traceId, authToken, requestDateTime;
    private Response paymentStatusResponse= null;
    private HashMap<String,String> paymentStatusHeader= new HashMap<>();
    private List<Transaction> transactions = new ArrayList<>();

    /**
     *
     * Getters
     */
    public String getTraceId() {
        return traceId;
    }

    public HashMap<String, String> getPaymentStatusHeader() {
        return paymentStatusHeader;
    }

    public String getAuthToken() {
        return authToken;
    }

    public Response getPaymentStatusResponse() {
        return paymentStatusResponse;
    }

    public String getRequestDateTime() {
        return requestDateTime;
    }

    public List<Transaction> getTransactions() { return transactions; }

    /**
     *
     * Setters
     */
    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setPaymentRequestId(String paymentRequestId) {

        this.paymentRequestId = paymentRequestId;
    }

    public void setAuthTokenwithBearer() {

        this.authToken = "Bearer "+ authToken;
    }

    public void setRequestDateTime(String requestDateTime) {
        this.requestDateTime = requestDateTime;
    }

    public void setTransactions(List<Transaction> transactions) { this.transactions = transactions; }

    /***
     * This method hits GET payment request with invalid header values. "Key" value is not included within the header.
     * @param url
     * @param key
     * @param signingKeyId
     * @param signingAlgorithm
     * @param signingKey
     * @param headerElementsForSignature
     */
    public void retrievePaymentStatusWithMissingHeaderKeys(String url, String key, String signingKeyId, String signingAlgorithm, String signingKey, HashSet headerElementsForSignature) {
        try{
            url= appendPaymentIdInURL(url);

            HashMap<String, String> header= returnPaymentStatusHeader("GET", url, signingKeyId, signingAlgorithm, signingKey, headerElementsForSignature);
            header.remove(key);

            paymentStatusResponse= getRestHelper().getRequestWithHeaders(url, header);

            logger.info("********** Payment Request Status Response *********** ---> "+ paymentStatusResponse.getBody().asString());
        }
        catch (Exception e){
            Assert.assertTrue("Verification of signature failed!", false);
        }

    }


    /**
     * This method creates a valid header and hits the GET payment request endpoint
     * @param url
     * @param signingKeyId
     * @param signingAlgorithm
     * @param signingKey
     * @param headerElementsForSignature
     * @return
     */
    public Response retrievePaymentStatus(String url, String signingKeyId, String signingAlgorithm, String signingKey, HashSet headerElementsForSignature) {
        try{
            url= appendPaymentIdInURL(url);

            paymentStatusResponse= getRestHelper().getRequestWithHeaders(url, returnPaymentStatusHeader("GET", new URL(url).getPath(), signingKeyId, signingAlgorithm, signingKey,headerElementsForSignature));

            logger.info("********** Payment Request Status Response *********** ---> "+ paymentStatusResponse.getBody().asString());
        }
        catch (Exception e){
            Assert.assertTrue("Verification of signature failed!", false);

        }


        return paymentStatusResponse;
    }


    /**
     * This method hits the GET payment request endpoint with an existing header.
     * @param url
     * @param header
     * @return
     */
    public Response retrievePaymentStatusExistingHeader(String url, HashMap header) {

        url= appendPaymentIdInURL(url);

        paymentStatusResponse= getRestHelper().getRequestWithHeaders(url, header);


        logger.info("********** Payment Request Status Response *********** ---> "+ paymentStatusResponse.getBody().asString());

        return paymentStatusResponse;
    }


    /**
     * The following two methods append payment request id within the endpoint
     * @param url
     * @return
     */
    public String appendPaymentIdInURL(String url){
        return url+"/"+paymentRequestId;
    }

    public String appendPaymentIdInURL(String url, String paymentId){
        return url+"/"+paymentId;
    }


    /**
     * This method creates and returns a valid header for the GET payment request
     * @param method
     * @param url
     * @param signingKeyId
     * @param signingAlgorithm
     * @param signingKey
     * @param headerElementsforSignature
     * @return
     */
    public HashMap<String,String> returnPaymentStatusHeader(String method, String url,String signingKeyId, String signingAlgorithm, String signingKey, HashSet headerElementsforSignature) {
        paymentStatusHeader.put("Accept","application/json");
        paymentStatusHeader.put("Authorization", authToken);
        paymentStatusHeader.put("Trace-Id",traceId);
        paymentStatusHeader.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));
        paymentStatusHeader.put("Request-Date-Time", getRequestDateTime());

        if (EnvHelper.getInstance().isLocalDevMode()) {
            EnvHelper.getInstance().addMissingHeaderForLocalDevMode(paymentStatusHeader);
        }

        try{
            paymentStatusHeader.put("Signature", getSignatureHelper().calculateSignature(method, url, Base64.getDecoder().decode(signingKey), signingAlgorithm, signingKeyId,
                    headerElementsforSignature, paymentStatusHeader)
            );
        }
        catch (IOException e){
            e.printStackTrace();
            Assert.assertTrue("Trouble creating signature!", false);

        }
        return paymentStatusHeader;
    }

    /**
     *
     * @returns paymentRequestId from the response
     */
    public String paymentRequestIdInResponse(){
        return getRestHelper().getResponseBodyValue(paymentStatusResponse, "paymentRequestId");

    }

    /**
     *
     * @returns effectiveDuration from the response
     */
    public Integer effectiveDurationInResponse(){
        return Integer.parseInt(getRestHelper().getResponseBodyValue(paymentStatusResponse, "effectiveDuration"));

    }

    /**
     *
     * @returns totalAmount from the response
     */
    public String totalAmountInResponse(){
        return getRestHelper().getResponseBodyValue(paymentStatusResponse, "totalAmount");

    }

    /**
     *
     * @returns currencyCode from the response
     */
    public String currencyCodeInResponse(){
        return getRestHelper().getResponseBodyValue(paymentStatusResponse, "currencyCode");

    }

    /**
     *
     * @returns createdTime from the response
     */
    public String createdTimestampInResponse(){
        return getRestHelper().getResponseBodyValue(paymentStatusResponse, "createdTime");

    }


    /**
     *
     * @returns appSuccessCallback from the response
     */
    public String appSuccessCallbackInResponse(){
        return getRestHelper().getResponseBodyValue(paymentStatusResponse, "appSuccessCallback");

    }

    /**
     *
     * @returns appFailCallback from the response
     */
    public String appFailCallbackInResponse(){
        return getRestHelper().getResponseBodyValue(paymentStatusResponse, "appFailCallback");

    }

    /**
     *
     * @returns statusDescription from the response
     */
    public String statusDescriptionInResponse(){
        return getRestHelper().getResponseBodyValue(paymentStatusResponse, "statusDescription");

    }

    /**
     *
     * @returns statusCode from the response
     */
    public String statusCodeInResponse(){
        return getRestHelper().getResponseBodyValue(paymentStatusResponse, "statusCode");

    }
}
