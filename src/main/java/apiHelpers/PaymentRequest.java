package apiHelpers;

import com.jayway.restassured.response.Response;
import extras.Transaction;
import utils.BaseStep;
import java.util.*;


public class PaymentRequest implements BaseStep {
    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(PaymentRequest.class);
    private String authToken, traceId, requestDateTime;
    private Transaction transactionDetails= new Transaction();


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

    HashMap<String, String> paymentRequestHeader= new HashMap<String, String>();
    HashMap<String,Transaction> paymentRequestBody= new HashMap<String,Transaction>();

    Response paymentRequestResponse= null;


    public String getAuthToken() {

        return authToken;
    }


    public void setAuthToken(String authToken) {

        this.authToken = authToken;
    }



    public HashMap<String,String> returnPaymentRequestHeader(){
        paymentRequestHeader.put("Accept","application/json");
        paymentRequestHeader.put("Content-Type","application/json");
        paymentRequestHeader.put("Authorization","Bearer "+ authToken);
        paymentRequestHeader.put("TraceId",traceId);
        paymentRequestHeader.put("Ocp-Apim-Subscription-Key","fa08ac6eca5b4afb8354526811025b03");
        paymentRequestHeader.put("RequestDateTime", getRequestDateTime());

        return paymentRequestHeader;
    }

    public HashMap<String,Transaction> returnPaymentRequestBody(){
        paymentRequestBody.put("transaction", transactionDetails);
        return paymentRequestBody;
    }

    public Transaction createTransaction(String amount, String currency, String description, String channel, String invoiceId, String merchantId, String effectiveDuration, String returnURL){

        transactionDetails.setAmount(amount);
        transactionDetails.setCurrency(currency);
        transactionDetails.setDescription(description);
        transactionDetails.setChannel(channel);
        transactionDetails.setInvoiceId(invoiceId);
        transactionDetails.setMerchantId(merchantId);
        transactionDetails.setReturnURL(returnURL);

        //If merchant does not pass the effective duration in the paylod, it has to be defaulted to 30 secs
        if(effectiveDuration.equals(""))
            transactionDetails.setEffectiveDuration("30");
        else
            transactionDetails.setEffectiveDuration(effectiveDuration);

        return transactionDetails;
    }

    public Response retrievePaymentRequest(String url){

        System.out.println("URL:  "+ url);
        paymentRequestResponse= restHelper.postRequestWithHeaderAndBody(url, returnPaymentRequestHeader(),returnPaymentRequestBody());
        System.out.println(paymentRequestResponse.toString());

        return paymentRequestResponse;
    }

    public String traceIdInResponseHeader(){
        return restHelper.getResponseHeaderValue(paymentRequestResponse, "TraceId");

    }

    public String paymentIdInResponse(){
        return restHelper.getResponseBodyValue(paymentRequestResponse, "paymentId");

    }

    public String createdTimestampInResponse(){
        String payId= null;
        payId= restHelper.getResponseBodyValue(paymentRequestResponse, "createdTime");

        return payId;
    }

    public boolean isLinksValid(){

        List<HashMap<String, String>> links= restHelper.getJsonArray(paymentRequestResponse, "links");

        Iterator<HashMap<String, String>> it= links.iterator();


        int i=0; int counter=0;
        while (it.hasNext()){
            //System.out.println("URI:  "+ links.get(i).get("URI"));
            // System.out.println("Channel:  "+ links.get(i).get("Channel"));
            try {
                if (links.get(i).get("Channel").equalsIgnoreCase("ecommerce") || links.get(i).get("Channel").equalsIgnoreCase("mcommerce")
                        ||links.get(i).get("Channel").equalsIgnoreCase("native"))
                    if (links.get(i).get("URI") != null)
                        counter++;


                it.next();
                i++;
            } catch (NullPointerException e){
                return false;
            }

        }


        if (counter==3)
            return true;
        else return false;

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

        if (!restHelper.getResponseBodyValue(paymentRequestResponse, "transaction.effectiveDuration").equalsIgnoreCase(transactionDetails.getEffectiveDuration().toString()))
            return "Effective Duration mismatch";


        return null;
    }


}
