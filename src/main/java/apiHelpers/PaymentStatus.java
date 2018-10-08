package apiHelpers;

import com.google.common.collect.Sets;
import com.jayway.restassured.response.Response;
import org.junit.Assert;
import utils.BaseStep;

import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;


public class PaymentStatus implements BaseStep {
    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(PaymentStatus.class);

    String paymentRequestId, traceId, authToken, requestDateTime;
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

    public String transactionIdInResponse(){
        return restHelper.getResponseBodyValue(paymentStatusResponse, "transactionId");

    }

    public void retrievePaymentStatusWithMissingHeaderKeys(String url, String key, String signingKeyId, String signingAlgorithm, String signingKey) {
        try{
            url= appendPaymentIdInURL(url);

            HashMap<String, String> header= returnPaymentStatusHeader("GET", url, signingKeyId, signingAlgorithm, signingKey);
            header.remove(key);

            paymentStatusResponse= restHelper.getRequestWithHeaders(url, header);

            // If the verification message is returned by APIMs it won't be signed so verification will fail.
            signatureHelper.verifySignature(paymentStatusResponse, "GET", url, Base64.getDecoder().decode(signingKey), signingAlgorithm);

            logger.info("********** Payment Request Status Response *********** ---> "+ paymentStatusResponse.getBody().asString());
        }
        catch (Exception e){
            Assert.assertTrue("Verification of signature failed!", false);
        }

    }



    public Response retrievePaymentStatus(String url, String signingKeyId, String signingAlgorithm, String signingKey) {
        try{
            url= appendPaymentIdInURL(url);

        paymentStatusResponse= restHelper.getRequestWithHeaders(url, returnPaymentStatusHeader("GET", url, signingKeyId, signingAlgorithm, signingKey));

        signatureHelper.verifySignature(paymentStatusResponse, "GET", url, Base64.getDecoder().decode(signingKey), signingAlgorithm);

        logger.info("********** Payment Request Status Response *********** ---> "+ paymentStatusResponse.getBody().asString());
        }
        catch (Exception e){
            Assert.assertTrue("Verification of signature failed!", false);

        }


        return paymentStatusResponse;
    }

    public String appendPaymentIdInURL(String url){
        return url+"/"+paymentRequestId;
    }

    public String appendPaymentIdInURL(String url, String paymentId){
        return url+"/"+paymentId;
    }

    public String getRequestDateTime() {
        return requestDateTime;
    }

    public void setRequestDateTime(String requestDateTime) {
        this.requestDateTime = requestDateTime;
    }

    public HashMap<String,String> returnPaymentStatusHeader(String method, String url,String signingKeyId, String signingAlgorithm, String signingKey) {
        paymentStatusHeader.put("Accept","application/json");
        paymentStatusHeader.put("Authorization", authToken);
        paymentStatusHeader.put("Trace-Id",traceId);
        paymentStatusHeader.put("Api-Version", System.getProperty("version"));
        paymentStatusHeader.put("Request-Date-Time", getRequestDateTime());
        try{
            paymentStatusHeader.put("Signature", signatureHelper.calculateSignature(method, new URL(url).getPath(), Base64.getDecoder().decode(signingKey), signingAlgorithm, signingKeyId, Sets.newHashSet("authorization", "trace-id", "request-date-time", "api-version"), paymentStatusHeader));
        }
        catch (IOException e){
            Assert.assertTrue("Trouble creating signature!", false);

        }
        return paymentStatusHeader;
    }

    public String paymentRequestIdInResponse(){
        return restHelper.getResponseBodyValue(paymentStatusResponse, "paymentRequestId");

    }

    public Integer effectiveDurationInResponse(){
        return Integer.parseInt(restHelper.getResponseBodyValue(paymentStatusResponse, "effectiveDuration"));

    }

    public String webLinkInResponse(){
        return restHelper.getResponseBodyValue(paymentStatusResponse, "webLink");

    }

    public String appLinkInResponse(){
        return restHelper.getResponseBodyValue(paymentStatusResponse, "appLink");

    }

    public String totalAmountInResponse(){
        return restHelper.getResponseBodyValue(paymentStatusResponse, "totalAmount");

    }

    public String currencyCodeInResponse(){
        return restHelper.getResponseBodyValue(paymentStatusResponse, "currencyCode");

    }


    public String createdTimestampInResponse(){
        return restHelper.getResponseBodyValue(paymentStatusResponse, "createdTime");

    }

    public String notificationURIInResponse(){
        return restHelper.getResponseBodyValue(paymentStatusResponse, "notificationUri");

    }

    public String appSuccessCallbackInResponse(){
        return restHelper.getResponseBodyValue(paymentStatusResponse, "appSuccessCallback");

    }

    public String appFailCallbackInResponse(){
        return restHelper.getResponseBodyValue(paymentStatusResponse, "appFailCallback");

    }

}
