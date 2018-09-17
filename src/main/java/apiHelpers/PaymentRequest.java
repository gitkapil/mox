package apiHelpers;

import com.jayway.restassured.response.Response;
import cucumber.api.DataTable;
import utils.BaseStep;
import java.util.*;


public class PaymentRequest implements BaseStep {
    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(PaymentRequest.class);
    private String authToken, requestDateTime="", merchantId, currency, notificationURI, traceId="", appSuccessCallback, appFailCallback;
    private Double totalAmount;
    private Integer effectiveDuration=600;

    private HashMap merchantData= new HashMap();
    private List<HashMap> shoppingCart=new ArrayList<HashMap>();

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

    private HashMap<String, String> paymentRequestHeader= new HashMap<String, String>();

    private HashMap paymentRequestBody = new HashMap();

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

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
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

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    private Response paymentRequestResponse= null;


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


    public HashMap<String,String> returnPaymentRequestHeader(){
        paymentRequestHeader.put("Accept","application/json");
        paymentRequestHeader.put("Content-Type","application/json");
        paymentRequestHeader.put("Authorization", authToken);
        paymentRequestHeader.put("Trace-Id",traceId);
        paymentRequestHeader.put("Accept-Language", "en-US");
        paymentRequestHeader.put("Request-Date-Time", getRequestDateTime());
        paymentRequestHeader.put("Api-Version", System.getProperty("version"));

        return paymentRequestHeader;
    }


    public Integer getEffectiveDuration() {
        return effectiveDuration;
    }

    public void setEffectiveDuration(Integer effectiveDuration) {
        this.effectiveDuration = effectiveDuration;
    }

    public HashMap<String,HashMap> returnPaymentRequestBody(){
        paymentRequestBody= new HashMap();

        if (!getMerchantId().equals(""))
        {
            if (!getMerchantId().equals("no_value"))
                paymentRequestBody.put("merchantId", getMerchantId());
            else
                paymentRequestBody.put("merchantId", "");
        }

        if (!getCurrency().equals(""))
        {
            if (!getCurrency().equals("no_value"))
                paymentRequestBody.put("currencyCode", getCurrency());
            else
                paymentRequestBody.put("currencyCode", "");
        }


         paymentRequestBody.put("totalAmount", getTotalAmount());



        if (!getnotificationURI().equals(""))
        {
            if (!getnotificationURI().equals("no_value"))
                paymentRequestBody.put("notificationURI", getnotificationURI());
            else
                paymentRequestBody.put("notificationURI", "");
        }

        if (!getAppSuccessCallback().equals(""))
        {
            if (!getAppSuccessCallback().equals("no_value"))
                paymentRequestBody.put("appSuccessCallback", getAppSuccessCallback());
            else
                paymentRequestBody.put("appSuccessCallback", "");
        }

        if (!getAppFailCallback().equals(""))
        {
            if (!getAppFailCallback().equals("no_value"))
                paymentRequestBody.put("appFailCallback", getAppFailCallback());
            else
                paymentRequestBody.put("appFailCallback", "");
        }

         try{
            if (!merchantData.isEmpty())
                 paymentRequestBody.put("merchantData", getMerchantData());
         }
         catch (NullPointerException e){ }

         return paymentRequestBody;
    }

    public void createMerchantData(String description, String orderId, String effectiveDuration, String additionalData){
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

        if (!effectiveDuration.equals(""))
        {

                this.effectiveDuration= Integer.parseInt(effectiveDuration);
                merchantData.put("effectiveDuration", this.effectiveDuration);
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



    public Response retrievePaymentRequest(String url){

        paymentRequestResponse= restHelper.postRequestWithHeaderAndBody(url, returnPaymentRequestHeader(),returnPaymentRequestBody());

        logger.info("********** Payment Request Response *********** ----> "+ paymentRequestResponse.getBody().asString());

        return paymentRequestResponse;
    }


    public Response retrievePaymentRequestWithMissingHeaderKeys(String url, String key){

        HashMap<String, String> header= returnPaymentRequestHeader();
        header.remove(key);

        paymentRequestResponse= restHelper.postRequestWithHeaderAndBody(url, header,returnPaymentRequestBody());

        logger.info("Response: "+ paymentRequestResponse.getBody().asString());

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


    public String createdTimestampInResponse(){
        return restHelper.getResponseBodyValue(paymentRequestResponse, "createdTime");

    }





}
