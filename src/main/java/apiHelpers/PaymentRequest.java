package apiHelpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.response.Response;
import cucumber.api.DataTable;
import managers.UtilManager;
import org.junit.Assert;
import java.net.URL;
import java.util.*;


public class PaymentRequest extends UtilManager{
    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(PaymentRequest.class);

    private String authToken, requestDateTime="",  currency, notificationURI=null, traceId="", appSuccessCallback=null, appFailCallback=null, effectiveDuration, totalAmount;
    private Double totalAmountInDouble;
    private HashMap merchantData= new HashMap();
    private List<HashMap> shoppingCart=new ArrayList<HashMap>();
    private HashMap<String, String> paymentRequestHeader;
    private HashMap paymentRequestBody = new HashMap();
    private Response paymentRequestResponse= null;

    /**
     *
     * Getters
     */
    public HashMap<String, String> getPaymentRequestHeader() {
        return paymentRequestHeader;
    }

    public HashMap getPaymentRequestBody() {
        return paymentRequestBody;
    }

    public String getAppSuccessCallback() {
        return appSuccessCallback;
    }

    public String getAppFailCallback() {
        return appFailCallback;
    }

    public String getTraceId() {
        return traceId;
    }

    public List<HashMap> getShoppingCart() {
        return shoppingCart;
    }

    public HashMap getMerchantData() {
        return merchantData;
    }

    public String getCurrency() {
        return currency;
    }

    public String getnotificationURI() {
        return notificationURI;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public Double getTotalAmountInDouble() {
        return totalAmountInDouble;
    }

    public Response getPaymentRequestResponse() {
        return paymentRequestResponse;
    }

    public String getRequestDateTime() { return requestDateTime; }

    public String getAuthToken() { return authToken; }

    public String getEffectiveDuration() { return effectiveDuration; }

    /**
     *
     * Setters
     */
    public void setAppSuccessCallback(String appSuccessCallback) {
        this.appSuccessCallback = appSuccessCallback;
    }

    public void setAppFailCallback(String appFailCallback) {
        this.appFailCallback = appFailCallback;
    }

    public void setShoppingCart(List<HashMap> shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public void setMerchantData(HashMap merchantData) {
        this.merchantData = merchantData;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setNotificationURI(String notificationURI) {
        this.notificationURI = notificationURI;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setRequestDateTime(String requestDateTime) {
        this.requestDateTime = requestDateTime;
    }

    public void setAuthToken(String authToken) { this.authToken = authToken; }

    public void setEffectiveDuration(String effectiveDuration) { this.effectiveDuration = effectiveDuration; }


    /**
     * Prefixes authToken with "Bearer"
     * @param authToken
     */
    public void setAuthTokenwithBearer(String authToken) {

        this.authToken = "Bearer "+ authToken;
    }


    /**
     * This method creates valid header for the POST Payment Request
     * @param method
     * @param url
     * @param signingKeyId
     * @param signingAlgorithm
     * @param signingKey
     * @param headerElementsForSignature
     * @return
     */
    public HashMap<String,String> returnPaymentRequestHeader(String method, String url, String signingKeyId, String signingAlgorithm, String signingKey, HashSet headerElementsForSignature) {
        paymentRequestHeader= new HashMap<String, String>();
        paymentRequestHeader.put("Accept","application/json");
        paymentRequestHeader.put("Content-Type","application/json");
        paymentRequestHeader.put("Authorization", authToken);
        paymentRequestHeader.put("Trace-Id",traceId);
        paymentRequestHeader.put("Accept-Language", "en-US");
        paymentRequestHeader.put("Request-Date-Time", getRequestDateTime());
        paymentRequestHeader.put("Api-Version", System.getProperty("version"));

        try {
           paymentRequestHeader.put("Digest", getSignatureHelper().calculateContentDigestHeader(new ObjectMapper().writeValueAsBytes(paymentRequestBody)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            Assert.assertTrue("Trouble creating Digest!", false);
        }

        try{
            byte[] sigKey = Base64.getDecoder().decode(signingKey);
            String signature = getSignatureHelper().calculateSignature(method, url, sigKey, signingAlgorithm, signingKeyId, headerElementsForSignature, paymentRequestHeader);
            paymentRequestHeader.put("Signature", signature);
        }
        catch (Exception e)
        {
            e.printStackTrace();
           Assert.assertTrue("Trouble creating Signature!", false);
        }
        return paymentRequestHeader;
    }

    /**
     * This method creates valid body for the POST payment Request
     * @return
     */
    public HashMap<String,HashMap> returnPaymentRequestBody(){
        paymentRequestBody= new HashMap();


        if (!getCurrency().equals(""))
        {
            if (!getCurrency().equals("no_value"))
                paymentRequestBody.put("currencyCode", getCurrency());
            else
                paymentRequestBody.put("currencyCode", "");
        }


        if (!getTotalAmount().equals(""))
        {
            try {
                totalAmountInDouble= Double.parseDouble(totalAmount);
                paymentRequestBody.put("totalAmount", totalAmountInDouble);
            } catch (NumberFormatException e) {
                paymentRequestBody.put("totalAmount", getTotalAmount());
            }
        }



        if (!getnotificationURI().equals(""))
        {
            if (!getnotificationURI().equals("no_value"))
                paymentRequestBody.put("notificationUri", getnotificationURI());
            else
            {
                paymentRequestBody.put("notificationUri", "");
                notificationURI="";
            }
        }

        else{
            notificationURI=null;
        }

        if (!getAppSuccessCallback().equals(""))
        {
            if (!getAppSuccessCallback().equals("no_value"))
                paymentRequestBody.put("appSuccessCallback", getAppSuccessCallback());
            else
            {
                paymentRequestBody.put("appSuccessCallback", "");
                appSuccessCallback="";
            }
        }
        else{
            appSuccessCallback=null;
        }

        if (!getAppFailCallback().equals(""))
        {
            if (!getAppFailCallback().equals("no_value"))
                paymentRequestBody.put("appFailCallback", getAppFailCallback());
            else
            {
                paymentRequestBody.put("appFailCallback", "");
                appFailCallback="";
            }
        }
        else{
            appFailCallback=null;
        }

         try{
            if (!merchantData.isEmpty())
                 paymentRequestBody.put("merchantData", getMerchantData());
         }
         catch (NullPointerException e){ }

        if (!effectiveDuration.equals(""))
        {

            paymentRequestBody.put("effectiveDuration", Integer.parseInt(this.effectiveDuration));
        }

         return paymentRequestBody;
    }

    /**
     * This method compiles merchant data to be included within the body for POST payment Request
     * @param description
     * @param orderId
     * @param additionalData
     */
    public void createMerchantData(String description, String orderId, String additionalData){
        merchantData= new HashMap();
        if (!"".equals(description))
        {
            if (!description.equals("null"))
                merchantData.put("orderDescription", description);
        }

        if (!orderId.equals(""))
        {
            if (!orderId.equals("no_value"))
                merchantData.put("orderId", orderId);
            else
                merchantData.put("orderId", "");
        }

        if (!additionalData.equals(""))
        {
            if (!additionalData.equals("no_value"))
                merchantData.put("additionalData", additionalData);
            else
                merchantData.put("additionalData", "");
        }

        try{
            if (shoppingCart.size()>=1)
                merchantData.put("shoppingCart", shoppingCart);
        }
        catch (NullPointerException e){}
    }


    /**
     * This method compiles shopping cart data to be included within the body(within merchant) for POST payment Request
     * @param dt
     */
    public void createShoppingCart(DataTable dt){
        shoppingCart= new ArrayList<>();
        List<Map<String, String>> list = dt.asMaps(String.class, String.class);

        for(int i=0; i<list.size(); i++) {

            HashMap temp= new HashMap();
            if (!list.get(i).get("sku").equals(""))
            {
                if (!list.get(i).get("sku").equals("no_value"))
                    temp.put("sku", list.get(i).get("sku"));
                else
                    temp.put("sku", "");
            }
            if (!list.get(i).get("name").equals(""))
            {
                if (!list.get(i).get("name").equals("no_value"))
                    temp.put("name", list.get(i).get("name"));
                else
                    temp.put("name", "");
            }
            if (!list.get(i).get("currency").equals(""))
            {
                if (!list.get(i).get("currency").equals("no_value"))
                    temp.put("currencyCode", list.get(i).get("currency"));
                else
                    temp.put("currencyCode", "");
            }


            if (!list.get(i).get("quantity").equals(""))
            {
                    temp.put("quantity", Integer.parseInt(list.get(i).get("quantity")));

            }


            if (!list.get(i).get("price").equals(""))
            {
                    temp.put("price", Double.parseDouble(list.get(i).get("price")));

            }


            if (!list.get(i).get("category1").equals(""))
            {
                if (!list.get(i).get("category1").equals("no_value"))
                    temp.put("category1", list.get(i).get("category1"));
                else
                    temp.put("category1", "");
            }
            if (!list.get(i).get("category2").equals(""))
            {
                if (!list.get(i).get("category2").equals("no_value"))
                    temp.put("category2", list.get(i).get("category2"));
                else
                    temp.put("category2", "");
            }
            if (!list.get(i).get("category3").equals(""))
            {
                if (!list.get(i).get("category3").equals("no_value"))
                    temp.put("category3", list.get(i).get("category3"));
                else
                    temp.put("category3", "");
            }


            shoppingCart.add(temp);
        }

    }


    /**
     * This method hits POST payment request endpoint with an existing header and body
     * @param url
     * @param header
     * @param body
     * @return
     */
    public Response retrievePaymentRequestExistingHeaderBody(String url, HashMap header, HashMap body) {

        paymentRequestResponse= getRestHelper().postRequestWithHeaderAndBody(url, header, body);

        logger.info("********** Payment Request Response *********** ----> "+ paymentRequestResponse.getBody().asString());

        return paymentRequestResponse;
    }


    /**
     * This method hits POST payment request endpoint and creates header and body values
     * @param url
     * @param signingKeyId
     * @param signingAlgorithm
     * @param signingKey
     * @param headerElementsForSignature
     * @return
     */
    public Response retrievePaymentRequest(String url, String signingKeyId, String signingAlgorithm, String signingKey, HashSet headerElementsForSignature) {

        try{
            returnPaymentRequestBody();
            paymentRequestResponse= getRestHelper().postRequestWithHeaderAndBody(url,
                    returnPaymentRequestHeader("POST", new URL(url).getPath(), signingKeyId, signingAlgorithm, signingKey, headerElementsForSignature),
                    paymentRequestBody);

            //testContext.getUtilManager().getSignatureHelper().verifySignature(paymentRequestResponse, "POST", url, Base64.getDecoder().decode(signingKey), signingAlgorithm);
            logger.info("********** Payment Request Response *********** ----> "+ paymentRequestResponse.getBody().asString());
        }
        catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue("Verification of signature failed!", false);

        }
        return paymentRequestResponse;
    }


    /**
     * This method hits POST payment request endpoint with invalid header values. "key" values are missing from the header.
     * @param url
     * @param key
     * @param signingKeyId
     * @param signingAlgorithm
     * @param signingKey
     * @param headerElementsForSignature
     * @return
     */
    public Response retrievePaymentRequestWithMissingHeaderKeys(String url, String key, String signingKeyId, String signingAlgorithm, String signingKey, HashSet headerElementsForSignature)  {

        try {
            returnPaymentRequestBody();
            HashMap<String, String> header = returnPaymentRequestHeader("POST", new URL(url).getPath(), signingKeyId, signingAlgorithm, signingKey, headerElementsForSignature);
            header.remove(key);
            paymentRequestResponse = getRestHelper().postRequestWithHeaderAndBody(url, header, paymentRequestBody);

            //testContext.getUtilManager().getSignatureHelper().verifySignature(paymentRequestResponse, "GET", url, Base64.getDecoder().decode(signingKey), signingAlgorithm);
            logger.info("Response: "+ paymentRequestResponse.getBody().asString());
        }
        catch (Exception e){
            Assert.assertTrue("Verification of signature failed!", false);
        }

        return paymentRequestResponse;
    }


    /**
     *
     * @returns paymentRequestId from the response
     */
    public String paymentRequestIdInResponse(){
        return getRestHelper().getResponseBodyValue(paymentRequestResponse, "paymentRequestId");

    }

    /**
     *
     * @returns effectiveDuration from the response
     */
    public Integer effectiveDurationInResponse(){
        return Integer.parseInt(getRestHelper().getResponseBodyValue(paymentRequestResponse, "effectiveDuration"));

    }

    /**
     *
     * @returns webLink from the response
     */
    public String webLinkInResponse(){
        return getRestHelper().getResponseBodyValue(paymentRequestResponse, "webLink");

    }

    /**
     *
     * @returns appLink from the response
     */
    public String appLinkInResponse(){
        return getRestHelper().getResponseBodyValue(paymentRequestResponse, "appLink");

    }

    /**
     *
     * @returns statusCode from the response
     */
    public String statusCodeInResponse(){
        return getRestHelper().getResponseBodyValue(paymentRequestResponse, "statusCode");

    }

    /**
     *
     * @returns statusDescription from the response
     */
    public String statusDescriptionInResponse(){
        return getRestHelper().getResponseBodyValue(paymentRequestResponse, "statusDescription");

    }


    /**
     *
     * @returns totalAmount from the response
     */
    public String totalAmountInResponse(){
        return getRestHelper().getResponseBodyValue(paymentRequestResponse, "totalAmount");

    }

    /**
     *
     * @returns currencyCode from the response
     */
    public String currencyCodeInResponse(){
        return getRestHelper().getResponseBodyValue(paymentRequestResponse, "currencyCode");

    }


    /**
     *
     * @returns createdTime from the response
     */
    public String createdTimestampInResponse(){
        return getRestHelper().getResponseBodyValue(paymentRequestResponse, "createdTime");

    }

    /**
     *
     * @returns notificationUri from the response
     */
    public String notificationURIInResponse(){
        return getRestHelper().getResponseBodyValue(paymentRequestResponse, "notificationUri");

    }

    /**
     *
     * @returns appSuccessCallback from the response
     */
    public String appSuccessCallbackInResponse(){
        return getRestHelper().getResponseBodyValue(paymentRequestResponse, "appSuccessCallback");

    }

    /**
     *
     * @returns appFailCallback from the response
     */
    public String appFailCallbackInResponse(){
        return getRestHelper().getResponseBodyValue(paymentRequestResponse, "appFailCallback");

    }

    /**
     * This method hits POST payment request and the "digest" is not included for signature calculation
     * @param url
     * @param signingKeyId
     * @param signingAlgorithm
     * @param signingKey
     * @param headerElementsForSignature
     * @return
     */
    public Response retrievePaymentRequestWithoutDigest(String url, String signingKeyId, String signingAlgorithm, String signingKey, HashSet headerElementsForSignature)  {

        try {
            returnPaymentRequestBody();
            HashMap<String, String> header = returnPaymentRequestHeaderWithoutDigest("POST", new URL(url).getPath(), signingKeyId, signingAlgorithm, signingKey, headerElementsForSignature);
            paymentRequestResponse = getRestHelper().postRequestWithHeaderAndBody(url, header, paymentRequestBody);

            logger.info("Response: "+ paymentRequestResponse.getBody().asString());
        }
        catch (Exception e){
            Assert.assertTrue("Verification of signature failed!", false);
        }

        return paymentRequestResponse;
    }


    /**
     * This method creates a valid header but without digest
     * @param method
     * @param url
     * @param signingKeyId
     * @param signingAlgorithm
     * @param signingKey
     * @param headerElementsForSignature
     * @return
     */
    public HashMap<String,String> returnPaymentRequestHeaderWithoutDigest(String method, String url, String signingKeyId, String signingAlgorithm, String signingKey, HashSet headerElementsForSignature) {
        paymentRequestHeader= new HashMap<String, String>();
        paymentRequestHeader.put("Accept","application/json");
        paymentRequestHeader.put("Content-Type","application/json");
        paymentRequestHeader.put("Authorization", authToken);
        paymentRequestHeader.put("Trace-Id",traceId);
        paymentRequestHeader.put("Accept-Language", "en-US");
        paymentRequestHeader.put("Request-Date-Time", getRequestDateTime());
        paymentRequestHeader.put("Api-Version", System.getProperty("version"));

        try{
            paymentRequestHeader.put("Signature", getSignatureHelper().calculateSignature(method, url,
                    Base64.getDecoder().decode(signingKey), signingAlgorithm, signingKeyId,
                    headerElementsForSignature, paymentRequestHeader));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Assert.assertTrue("Trouble creating Signature!", false);
        }
        return paymentRequestHeader;
    }

}
