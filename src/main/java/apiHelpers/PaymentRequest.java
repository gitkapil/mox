package apiHelpers;

import com.jayway.restassured.response.Response;
import cucumber.api.DataTable;
import org.junit.Assert;
import utils.BaseStep;
import java.net.URL;
import java.util.*;


public class PaymentRequest implements BaseStep {
    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(PaymentRequest.class);
    private String authToken, requestDateTime="",  currency, notificationURI=null, traceId="", appSuccessCallback=null, appFailCallback=null, effectiveDuration, totalAmount;
    private Double totalAmountInDouble;

    private HashMap merchantData= new HashMap();
    private List<HashMap> shoppingCart=new ArrayList<HashMap>();
    private HashMap<String, String> paymentRequestHeader= new HashMap<String, String>();

    public HashMap<String, String> getPaymentRequestHeader() {
        return paymentRequestHeader;
    }

    public HashMap getPaymentRequestBody() {
        return paymentRequestBody;
    }

    private HashMap paymentRequestBody = new HashMap();
    private Response paymentRequestResponse= null;

    public String getAppSuccessCallback() {
        return appSuccessCallback;
    }

    public void setAppSuccessCallback(String appSuccessCallback) {
        this.appSuccessCallback = appSuccessCallback;
    }

    public String getAppFailCallback() {
        return appFailCallback;
    }

    public void setAppFailCallback(String appFailCallback) {
        this.appFailCallback = appFailCallback;
    }

    public String getTraceId() {
        return traceId;
    }

    public List<HashMap> getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(List<HashMap> shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public HashMap getMerchantData() {
        return merchantData;
    }

    public void setMerchantData(HashMap merchantData) {
        this.merchantData = merchantData;
    }


    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getnotificationURI() {
        return notificationURI;
    }

    public void setNotificationURI(String notificationURI) {
        this.notificationURI = notificationURI;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public Double getTotalAmountInDouble() {
        return totalAmountInDouble;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Response getPaymentRequestResponse() {
        return paymentRequestResponse;
    }


    public String getRequestDateTime() {
        return requestDateTime;

    }

    public void setRequestDateTime(String requestDateTime) {
        this.requestDateTime = requestDateTime;
    }



    public String getAuthToken() {

        return authToken;
    }


    public void setAuthToken(String authToken) {

        this.authToken = authToken;
    }

    public void setAuthTokenwithBearer(String authToken) {

        this.authToken = "Bearer "+ authToken;
    }

    public HashMap<String,String> returnPaymentRequestHeader(String method, String url, String signingKeyId, String signingAlgorithm, String signingKey, HashSet headerElementsForSignature) {
        paymentRequestHeader.put("Accept","application/json");
        paymentRequestHeader.put("Content-Type","application/json");
        paymentRequestHeader.put("Authorization", authToken);
        paymentRequestHeader.put("Trace-Id",traceId);
        paymentRequestHeader.put("Accept-Language", "en-US");
        paymentRequestHeader.put("Request-Date-Time", getRequestDateTime());
        paymentRequestHeader.put("Api-Version", System.getProperty("version"));
        try{
            paymentRequestHeader.put("Signature", signatureHelper.calculateSignature(method, url,
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

    public String getEffectiveDuration() {
        return effectiveDuration;
    }

    public void setEffectiveDuration(String effectiveDuration) {
        this.effectiveDuration = effectiveDuration;
    }

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
            totalAmountInDouble= Double.parseDouble(totalAmount);
            paymentRequestBody.put("totalAmount", totalAmountInDouble);
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

    public void createMerchantData(String description, String orderId, String additionalData){
        merchantData= new HashMap();
        if (!description.equals(""))
        {
            if (!description.equals("no_value"))
                merchantData.put("orderDescription", description);
            else
                merchantData.put("orderDescription", "");
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

    public Response retrievePaymentRequestExistingHeaderBody(String url, HashMap header, HashMap body) {

        paymentRequestResponse= restHelper.postRequestWithHeaderAndBody(url, header, body);

        logger.info("********** Payment Request Response *********** ----> "+ paymentRequestResponse.getBody().asString());

        return paymentRequestResponse;
    }


    public Response retrievePaymentRequest(String url, String signingKeyId, String signingAlgorithm, String signingKey, HashSet headerElementsForSignature) {

        try{
            paymentRequestResponse= restHelper.postRequestWithHeaderAndBody(url,
                    returnPaymentRequestHeader("POST", new URL(url).getPath(), signingKeyId, signingAlgorithm, signingKey, headerElementsForSignature),
                    returnPaymentRequestBody());
            signatureHelper.verifySignature(paymentRequestResponse, "POST", url, Base64.getDecoder().decode(signingKey), signingAlgorithm);
            logger.info("********** Payment Request Response *********** ----> "+ paymentRequestResponse.getBody().asString());
        }
        catch (Exception e){
            Assert.assertTrue("Verification of signature failed!", false);

        }
        return paymentRequestResponse;
    }


    public Response retrievePaymentRequestWithMissingHeaderKeys(String url, String key, String signingKeyId, String signingAlgorithm, String signingKey, HashSet headerElementsForSignature)  {

        try {
            HashMap<String, String> header = returnPaymentRequestHeader("POST", new URL(url).getPath(), signingKeyId, signingAlgorithm, signingKey, headerElementsForSignature);
            header.remove(key);

            paymentRequestResponse = restHelper.postRequestWithHeaderAndBody(url, header, returnPaymentRequestBody());

            signatureHelper.verifySignature(paymentRequestResponse, "GET", url, Base64.getDecoder().decode(signingKey), signingAlgorithm);
            logger.info("Response: "+ paymentRequestResponse.getBody().asString());
        }
        catch (Exception e){
            Assert.assertTrue("Verification of signature failed!", false);
        }

        return paymentRequestResponse;
    }



    public String paymentRequestIdInResponse(){
        return restHelper.getResponseBodyValue(paymentRequestResponse, "paymentRequestId");

    }

    public Integer effectiveDurationInResponse(){
        return Integer.parseInt(restHelper.getResponseBodyValue(paymentRequestResponse, "effectiveDuration"));

    }

    public String webLinkInResponse(){
        return restHelper.getResponseBodyValue(paymentRequestResponse, "webLink");

    }

    public String appLinkInResponse(){
        return restHelper.getResponseBodyValue(paymentRequestResponse, "appLink");

    }

    public String statusCodeInResponse(){
        return restHelper.getResponseBodyValue(paymentRequestResponse, "statusCode");

    }

    public String statusDescriptionInResponse(){
        return restHelper.getResponseBodyValue(paymentRequestResponse, "statusDescription");

    }

    public String totalAmountInResponse(){
        return restHelper.getResponseBodyValue(paymentRequestResponse, "totalAmount");

    }

    public String currencyCodeInResponse(){
        return restHelper.getResponseBodyValue(paymentRequestResponse, "currencyCode");

    }


    public String createdTimestampInResponse(){
        return restHelper.getResponseBodyValue(paymentRequestResponse, "createdTime");

    }

    public String notificationURIInResponse(){
        return restHelper.getResponseBodyValue(paymentRequestResponse, "notificationUri");

    }

    public String appSuccessCallbackInResponse(){
        return restHelper.getResponseBodyValue(paymentRequestResponse, "appSuccessCallback");

    }

    public String appFailCallbackInResponse(){
        return restHelper.getResponseBodyValue(paymentRequestResponse, "appFailCallback");

    }






}
