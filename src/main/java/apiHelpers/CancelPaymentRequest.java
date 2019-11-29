package apiHelpers;
import com.jayway.restassured.response.Response;
import managers.UtilManager;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import utils.EnvHelper;
import utils.PropertyHelper;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;

public class CancelPaymentRequest extends UtilManager{
    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(CancelPaymentRequest.class);
    private String authToken, requestDateTime="", traceId;
    private String statusCode;
    private Response cancelRequestResponse = null;
    private  String paymentRequestId;
    private String statusDescription;
    private String deviceId;
    private HashMap cancelPaymentRequestHeader;
    private String apiVersion;
    private String contentType;
    public HashMap<String, String> cancelPaymentRequest() {

        return cancelPaymentRequestHeader;
    }

    public String getDeviceId() { return deviceId; }


    public String getStatusCode() { return statusCode; }

    public void setStatusCode(String statusCode) { this.statusCode = statusCode; }
    public String getApiVersion() { return apiVersion; }

    public String getContentType() { return contentType; }

    public void setContentType(String contentType) { this.contentType = contentType;
    }

    public void setApiVersion(String apiVersion) { this.apiVersion = apiVersion; }

    public void setDeviceId(String deviceId) {
        if (deviceId.equalsIgnoreCase("doubleQuotes")) {
            this.deviceId = "";
        } else if (deviceId.equalsIgnoreCase("space")) {
            this.deviceId = " ";
        } else if (deviceId.equalsIgnoreCase("tooLong")) {
            this.deviceId = StringUtils.repeat("*", 51);
        }
    }
    public String getTraceId() {
        return traceId;
    }

    public String getPaymentRequestId() {
        return paymentRequestId;
    }

    public void setPaymentRequestId(String paymentRequestId) { this.paymentRequestId = paymentRequestId; }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }
    public Response getResponse()
    {
        return cancelRequestResponse;
    }

    public String getRequestDateTime() { return requestDateTime; }

    public String getAuthToken() { return authToken; }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public void setRequestDateTime(String requestDateTime) {
        this.requestDateTime = requestDateTime;
    }

    public void setAuthTokenWithout(String authToken) { this.authToken = authToken; }

    public void setAuthToken(String authToken) {

        this.authToken = "Bearer "+ authToken;
    }

    public HashMap<String,String> returnCancelPaymentRequestHeader(String method, String url, String signingKeyId, String signingAlgorithm, String signingKey, HashSet headerElementsForSignature) {
        cancelPaymentRequestHeader= new HashMap<String, String>();
        cancelPaymentRequestHeader.put("Accept","application/json");
        cancelPaymentRequestHeader.put("Content-Type","application/json");
        cancelPaymentRequestHeader.put("Authorization", authToken);
        cancelPaymentRequestHeader.put("Trace-Id", getGeneral().generateUniqueUUID());
        cancelPaymentRequestHeader.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));
        cancelPaymentRequestHeader.put("Request-Date-Time", getDateHelper().getUTCNowDateTime());

        if (EnvHelper.getInstance().isLocalDevMode()) {
            EnvHelper.getInstance().addMissingHeaderForLocalDevMode(cancelPaymentRequestHeader);
        }
        try{
            byte[] sigKey = Base64.getDecoder().decode(signingKey);
            String signature = getSignatureHelper().calculateSignature(method, url, sigKey, signingAlgorithm, signingKeyId, headerElementsForSignature, cancelPaymentRequestHeader);
            cancelPaymentRequestHeader.put("Signature", signature);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Assert.assertTrue("Trouble creating Signature!", false);
        }
        return cancelPaymentRequestHeader;
    }

    public HashMap<String,String> returnCancelPaymentRequestHeaderWithNullHeader(String method, String url, String signingKeyId, String signingAlgorithm, String signingKey, HashSet headerElementsForSignature, String nullHeader) {
        cancelPaymentRequestHeader= new HashMap<String, String>();
        cancelPaymentRequestHeader.put("Accept","application/json");
        cancelPaymentRequestHeader.put("Content-Type","application/json");
        cancelPaymentRequestHeader.put("Authorization", authToken);
        cancelPaymentRequestHeader.put("Trace-Id", getGeneral().generateUniqueUUID());
        cancelPaymentRequestHeader.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));
        cancelPaymentRequestHeader.put("Request-Date-Time", getDateHelper().getUTCNowDateTime());

        if (EnvHelper.getInstance().isLocalDevMode()) {
            EnvHelper.getInstance().addMissingHeaderForLocalDevMode(cancelPaymentRequestHeader);
        }
        try{
            byte[] sigKey = Base64.getDecoder().decode(signingKey);
            String signature = getSignatureHelper().calculateSignature(method, url, sigKey, signingAlgorithm, signingKeyId, headerElementsForSignature, cancelPaymentRequestHeader);
            cancelPaymentRequestHeader.put("Signature", signature);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Assert.assertTrue("Trouble creating Signature!", false);
        }
        cancelPaymentRequestHeader.remove(nullHeader);
        return cancelPaymentRequestHeader;
    }

    public HashMap<String,String> returnCancelPaymentRequestHeaderWithInvalidHeader(String method, String url, String signingKeyId, String signingAlgorithm, String signingKey, HashSet headerElementsForSignature, String key, String invalidHeader) {
        cancelPaymentRequestHeader= new HashMap<String, String>();
        cancelPaymentRequestHeader.put("Accept","application/json");
        cancelPaymentRequestHeader.put("Content-Type","application/json");
        cancelPaymentRequestHeader.put("Authorization", authToken);
        cancelPaymentRequestHeader.put("Trace-Id", getGeneral().generateUniqueUUID());
        cancelPaymentRequestHeader.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));
        cancelPaymentRequestHeader.put("Request-Date-Time", getDateHelper().getUTCNowDateTime());

        if (EnvHelper.getInstance().isLocalDevMode()) {
            EnvHelper.getInstance().addMissingHeaderForLocalDevMode(cancelPaymentRequestHeader);
        }
        try{
            byte[] sigKey = Base64.getDecoder().decode(signingKey);
            String signature = getSignatureHelper().calculateSignature(method, url, sigKey, signingAlgorithm, signingKeyId, headerElementsForSignature, cancelPaymentRequestHeader);
            cancelPaymentRequestHeader.put("Signature", signature);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Assert.assertTrue("Trouble creating Signature!", false);
        }
        if(cancelPaymentRequestHeader.containsKey(key)){
            cancelPaymentRequestHeader.remove(key);
            cancelPaymentRequestHeader.put(key,invalidHeader);
        }
        return cancelPaymentRequestHeader;
    }

    public Response makeCancelPaymentRequest(String url, String signingKeyId, String signingAlgorithm, String signingKey, HashSet headerElementsForSignature)  {
        try {
            HashMap<String, String> header = returnCancelPaymentRequestHeader("PUT", new URL(url).getPath(), signingKeyId, signingAlgorithm, signingKey, headerElementsForSignature);
            cancelRequestResponse = getRestHelper().putRequestWithHeader(url, header);
            logger.info("Response: "+ cancelRequestResponse.getBody().prettyPrint());
        }
        catch (Exception e){
            Assert.assertTrue("Verification of signature failed!", false);
        }
        return cancelRequestResponse;
    }

    public Response makeCancelPaymentRequestWithNullHeader(String url, String signingKeyId, String signingAlgorithm, String signingKey, HashSet headerElementsForSignature, String nullHeader)  {
        try {
            HashMap<String, String> header = returnCancelPaymentRequestHeaderWithNullHeader("PUT", new URL(url).getPath(), signingKeyId, signingAlgorithm, signingKey, headerElementsForSignature, nullHeader);
            cancelRequestResponse = getRestHelper().putRequestWithHeader(url, header);
            logger.info("Response: "+ cancelRequestResponse.getBody().prettyPrint());
        }
        catch (Exception e){
            Assert.assertTrue("Verification of signature failed!", false);
        }
        return cancelRequestResponse;
    }

    public Response makeCancelPaymentRequestWithInvalidHeader(String url, String signingKeyId, String signingAlgorithm, String signingKey, HashSet headerElementsForSignature, String key, String invalidHeader)  {
        try {
            HashMap<String, String> header = returnCancelPaymentRequestHeaderWithInvalidHeader("PUT", new URL(url).getPath(), signingKeyId, signingAlgorithm, signingKey, headerElementsForSignature, key, invalidHeader);
            cancelRequestResponse = getRestHelper().putRequestWithHeader(url, header);
            logger.info("Response: "+ cancelRequestResponse.getBody().prettyPrint());
        }
        catch (Exception e){
            Assert.assertTrue("Verification of signature failed!", false);
        }
        return cancelRequestResponse;
    }
}
