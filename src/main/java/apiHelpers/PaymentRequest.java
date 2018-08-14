package apiHelpers;

import com.jayway.restassured.response.Response;
import extras.Transaction;
import utils.BaseStep;
import java.util.*;


public class PaymentRequest implements BaseStep {
    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(PaymentRequest.class);
    private String authToken, traceId, requestDateTime;
    private Transaction transactionDetails= new Transaction();
    private HashMap<String, String> paymentRequestHeader= new HashMap<String, String>();
    private HashMap<String,Transaction> paymentRequestBody= new HashMap<String,Transaction>();

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

    public HashMap<String,Transaction> returnPaymentRequestBody(){
        paymentRequestBody.put("transaction", transactionDetails);
        return paymentRequestBody;
    }

    public Transaction createTransaction(String amount, String currency, String description, String channel, String invoiceId, String merchantId, String effectiveDuration, String returnURL){

        transactionDetails.setAmount(Double.parseDouble(amount));
        if (currency.equals(""))
            transactionDetails.setCurrency("HKD");
        else
            transactionDetails.setCurrency(currency);
        transactionDetails.setDescription(description);
        transactionDetails.setChannel(channel);
        transactionDetails.setInvoiceId(invoiceId);
        transactionDetails.setMerchantId(merchantId);
        transactionDetails.setReturnURL(returnURL);

        //If merchant does not pass the effective duration in the paylod, it has to be defaulted to 30 secs
        if(effectiveDuration.equals(""))
            transactionDetails.setEffectiveDuration(new Double(30));
        else
            transactionDetails.setEffectiveDuration(Double.parseDouble(effectiveDuration));

        return transactionDetails;
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

        if (Double.parseDouble(restHelper.getResponseBodyValue(paymentRequestResponse, "expiryDuration"))==transactionDetails.getEffectiveDuration())
            return true;

        return false;
    }


    public String isTransactionValid()
    {

        if (!restHelper.getResponseBodyValue(paymentRequestResponse, "transaction.merchantId").equalsIgnoreCase(transactionDetails.getMerchantId()))
            return "MerchantId mismatch";

        if (!restHelper.getResponseBodyValue(paymentRequestResponse, "transaction.invoiceId").equalsIgnoreCase(transactionDetails.getInvoiceId()))
            return "Invoice Id mismatch";

        if (!restHelper.getResponseBodyValue(paymentRequestResponse, "transaction.description").equalsIgnoreCase(transactionDetails.getDescription()))
            return "Description mismatch";

        if (!restHelper.getResponseBodyValue(paymentRequestResponse, "transaction.channel").equalsIgnoreCase(transactionDetails.getChannel()))
            return "Channel mismatch";

        if (!restHelper.getResponseBodyValue(paymentRequestResponse, "transaction.amount").equalsIgnoreCase(transactionDetails.getAmount().toString()))
            return "Amount mismatch";

        if (!restHelper.getResponseBodyValue(paymentRequestResponse, "transaction.currency").equalsIgnoreCase(transactionDetails.getCurrency()))
            return "Currency mismatch";

       /* if (!restHelper.getResponseBodyValue(paymentRequestResponse, "transaction.effectiveDuration").equalsIgnoreCase(transactionDetails.getEffectiveDuration()))
            return "Effective Duration mismatch";
           */

        return null;
    }


}
