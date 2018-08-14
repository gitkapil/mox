package apiHelpers;

import com.jayway.restassured.response.Response;
import utils.BaseStep;
import java.util.*;


public class PaymentRequest implements BaseStep {
    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(PaymentRequest.class);
    private String authToken, traceId, requestDateTime;
    private HashMap<String, String> paymentRequestHeader= new HashMap<String, String>();

    HashMap<String, HashMap> paymentRequestBody = new HashMap<String, HashMap>();

    HashMap transactionBody= new HashMap<>();
    

    private Response paymentRequestResponse= null;


    public Response getPaymentRequestResponse() {
        return paymentRequestResponse;
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

    public String getTraceId() {
        return traceId;
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
        paymentRequestHeader.put("TraceId",traceId);
        paymentRequestHeader.put("Ocp-Apim-Subscription-Key","fa08ac6eca5b4afb8354526811025b03");
       // paymentRequestHeader.put("RequestDateTime", getRequestDateTime());

        return paymentRequestHeader;
    }


    public HashMap<String,HashMap> returnPaymentRequestBody(){
        paymentRequestBody.put("transaction", transactionBody);
        return paymentRequestBody;
    }

    public HashMap createTransaction(String amount, String currency, String description, String channel, String invoiceId, String merchantId, String effectiveDuration, String returnURL){

        try{
            transactionBody.put("amount", Double.parseDouble(amount));
        }
        catch (Exception e){
            System.out.println("amount null");
        }

        if (currency.equals(""))
            transactionBody.put("currency", "HKD");
        else
            transactionBody.put("currency", currency);
        transactionBody.put("description", description);
        transactionBody.put("channel", channel);
        transactionBody.put("invoiceId", invoiceId);
        transactionBody.put("merchantId", merchantId);

        if(effectiveDuration.equals(""))
            transactionBody.put("effectiveDuration", 30);
        else
            transactionBody.put("effectiveDuration", Integer.parseInt(effectiveDuration));

        transactionBody.put("returnURL", returnURL);

        return transactionBody;

    }


    public Response retrievePaymentRequest(String url){

        paymentRequestResponse= restHelper.postRequestWithHeaderAndBody(url, returnPaymentRequestHeader(),returnPaymentRequestBody());
        System.out.println("Response::: "+ paymentRequestResponse.path("links"));

        return paymentRequestResponse;
    }

    public String traceIdInResponseHeader(){
        return restHelper.getResponseHeaderValue(paymentRequestResponse, "TraceId");

    }

    public String paymentIdInResponse(){
        return restHelper.getResponseBodyValue(paymentRequestResponse, "paymentId");

    }

    public String expiryDurationInResponse(){
        return restHelper.getResponseBodyValue(paymentRequestResponse, "expiryDuration");

    }

    public String createdTimestampInResponse(){
        String payId= null;
        payId= restHelper.getResponseBodyValue(paymentRequestResponse, "createdTime");

        return payId;
    }

    public boolean isLinksValid(){

        List<HashMap<String, String>> links= restHelper.getJsonArray(paymentRequestResponse, "links");

        Iterator<HashMap<String, String>> it= links.iterator();


        int i=1; int counter=0;
        //System.out.println("size: "+ links.size());

        while (i<=links.size()){
          //  System.out.println("Counter:" + counter);
          //  System.out.println("URI:  "+ links.get(i-1).get("uri"));
           // System.out.println("Channel:  "+ links.get(i-1).get("channel"));
            try {
                if (links.get(i-1).get("channel").equalsIgnoreCase("eCommerce") || links.get(i-1).get("channel").equalsIgnoreCase("mCommerce")
                    ||links.get(i-1).get("channel").equalsIgnoreCase("native"))
                    if (links.get(i-1).get("uri") != null)
                        counter++;

                i++;
            } catch (NullPointerException e){
                return false;
            }

        }


        if (counter==2)
            return true;
        else return false;

    }

    public boolean isExpiryDurationValid(){

        if (Integer.parseInt(restHelper.getResponseBodyValue(paymentRequestResponse, "expiryDuration"))==(Integer)transactionBody.get("effectiveDuration"))
            return true;

        return false;
    }


    public String isTransactionValid()
    {

        if (!restHelper.getResponseBodyValue(paymentRequestResponse, "transaction.merchantId").equalsIgnoreCase(transactionBody.get("merchantId").toString()))
            return "MerchantId mismatch";

        if (!restHelper.getResponseBodyValue(paymentRequestResponse, "transaction.invoiceId").equalsIgnoreCase(transactionBody.get("invoiceId").toString()))
            return "Invoice Id mismatch";

        if (!restHelper.getResponseBodyValue(paymentRequestResponse, "transaction.description").equalsIgnoreCase(transactionBody.get("description").toString()))
            return "Description mismatch";

        if (!restHelper.getResponseBodyValue(paymentRequestResponse, "transaction.channel").equalsIgnoreCase(transactionBody.get("channel").toString()))
            return "Channel mismatch";

        if (!restHelper.getResponseBodyValue(paymentRequestResponse, "transaction.amount").equalsIgnoreCase(transactionBody.get("amount").toString()))
            return "Amount mismatch";

        if (!restHelper.getResponseBodyValue(paymentRequestResponse, "transaction.currency").equalsIgnoreCase(transactionBody.get("currency").toString()))
            return "Currency mismatch";


        return null;
    }


}
