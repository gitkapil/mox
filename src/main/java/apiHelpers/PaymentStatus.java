package apiHelpers;


import com.jayway.restassured.response.Response;
import org.junit.Assert;
import utils.BaseStep;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;


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

    public HashMap<String, String> getPaymentStatusHeader() {
        return paymentStatusHeader;
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


    public void retrievePaymentStatusWithMissingHeaderKeys(String url, String key, String signingKeyId, String signingAlgorithm, String signingKey, HashSet headerElementsForSignature) {
        try{
            url= appendPaymentIdInURL(url);

            HashMap<String, String> header= returnPaymentStatusHeader("GET", url, signingKeyId, signingAlgorithm, signingKey, headerElementsForSignature);
            header.remove(key);

            paymentStatusResponse= restHelper.getRequestWithHeaders(url, header);

            // If the verification message is returned by APIMs it won't be signed so verification will fail.
           // signatureHelper.verifySignature(paymentStatusResponse, "GET", url, Base64.getDecoder().decode(signingKey), signingAlgorithm);

            logger.info("********** Payment Request Status Response *********** ---> "+ paymentStatusResponse.getBody().asString());
        }
        catch (Exception e){
            Assert.assertTrue("Verification of signature failed!", false);
        }

    }



    public Response retrievePaymentStatus(String url, String signingKeyId, String signingAlgorithm, String signingKey, HashSet headerElementsForSignature) {
        try{
            url= appendPaymentIdInURL(url);

        paymentStatusResponse= restHelper.getRequestWithHeaders(url, returnPaymentStatusHeader("GET", url, signingKeyId, signingAlgorithm, signingKey,headerElementsForSignature));

       // signatureHelper.verifySignature(paymentStatusResponse, "GET", url, Base64.getDecoder().decode(signingKey), signingAlgorithm);

        logger.info("********** Payment Request Status Response *********** ---> "+ paymentStatusResponse.getBody().asString());
        }
        catch (Exception e){
            Assert.assertTrue("Verification of signature failed!", false);

        }


        return paymentStatusResponse;
    }


    public Response retrievePaymentStatusExistingHeader(String url, HashMap header) {

        url= appendPaymentIdInURL(url);

        paymentStatusResponse= restHelper.getRequestWithHeaders(url, header);


        logger.info("********** Payment Request Status Response *********** ---> "+ paymentStatusResponse.getBody().asString());

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

    public HashMap<String,String> returnPaymentStatusHeader(String method, String url,String signingKeyId, String signingAlgorithm, String signingKey, HashSet headerElementsforSignature) {
        paymentStatusHeader.put("Accept","application/json");
        paymentStatusHeader.put("Authorization", authToken);
        paymentStatusHeader.put("Trace-Id",traceId);
        paymentStatusHeader.put("Api-Version", System.getProperty("version"));
        paymentStatusHeader.put("Request-Date-Time", getRequestDateTime());
        try{
            paymentStatusHeader.put("Signature", signatureHelper.calculateSignature(method, url, Base64.getDecoder().decode(signingKey), signingAlgorithm, signingKeyId,
                    headerElementsforSignature, paymentStatusHeader)
            );
        }
        catch (IOException e){
            e.printStackTrace();
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


    public String totalAmountInResponse(){
        return restHelper.getResponseBodyValue(paymentStatusResponse, "totalAmount");

    }

    public String currencyCodeInResponse(){
        return restHelper.getResponseBodyValue(paymentStatusResponse, "currencyCode");

    }


    public String createdTimestampInResponse(){
        return restHelper.getResponseBodyValue(paymentStatusResponse, "createdTime");

    }


    public String appSuccessCallbackInResponse(){
        return restHelper.getResponseBodyValue(paymentStatusResponse, "appSuccessCallback");

    }

    public String appFailCallbackInResponse(){
        return restHelper.getResponseBodyValue(paymentStatusResponse, "appFailCallback");

    }

}
