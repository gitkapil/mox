package apiHelpers;

import com.jayway.restassured.response.Response;
import managers.UtilManager;
import org.apache.log4j.Logger;
import org.junit.Assert;
import utils.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;

public class GetApplication extends UtilManager {
    private final static Logger logger = Logger.getLogger(GetApplication.class);
    String authToken;
    DateHelper dateHelper;
    SignatureHelper signatureHelper;
    General general = new General();
    private Response response= null;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public GetApplication() {
        dateHelper = new DateHelper();
        signatureHelper = new SignatureHelper();
    }

    public void getListOfApplications(String url, String signingKeyId, String signingAlgorithm, String signingKey, HashSet headerElementsForSignature, String authToken) {
        try {
            response = getRestHelper().getRequestWithHeaders(url,
                    getListHeader("GET", new URL(url).getPath(), signingKeyId,
                            signingAlgorithm, signingKey, headerElementsForSignature,
                            authToken));

            logger.info("****************List of application response ******************** --> " + response.getBody().asString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Assert.assertTrue("Unable to get URL" , false);
        }
    }

    private HashMap<String, String> getListHeader(String method, String url,String signingKeyId,
                                                  String signingAlgorithm, String signingKey,
                                                  HashSet headerElementsforSignature, String authToken) {
        HashMap<String,String> header = new HashMap<>();
        header.put("ACCEPT", "application/json");
        header.put("Authorization", authToken);
        header.put("Trace-Id", general.generateUniqueUUID());
        header.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));
        header.put("Request-Date-Time", dateHelper.getUTCNowDateTime());
        header.put("Content-Type", "application/json");

        if (EnvHelper.getInstance().isLocalDevMode()) {
            EnvHelper.getInstance().addMissingHeaderForLocalDevMode(header);
        }

        try {
            header.put("Signature",
                    getSignatureHelper().calculateSignature(method, url, Base64.getDecoder().decode(signingKey),
                            signingAlgorithm, signingKeyId,
                    headerElementsforSignature, header));
        } catch (IOException e) {
            e.printStackTrace();
            Assert.assertTrue("Trouble creating signature!", false);
        }
        return header;
    }

    private HashMap<String, String> generateHeader(String method, String url, String signingKeyId, String signingAlgorithm, String signingKey, HashSet headerElementsForSignature) {
        HashMap<String, String> requestHeader = new HashMap<String, String>();

        requestHeader.put("Accept", "application/json");
        requestHeader.put("Content-Type", "application/json");
        requestHeader.put("Authorization", authToken);
        requestHeader.put("Trace-Id",general.generateUniqueUUID());
        requestHeader.put("Accept-Language", "en-US");
        requestHeader.put("Request-Date-Time", dateHelper.getUTCNowDateTime());
        requestHeader.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));

        if (EnvHelper.getInstance().isLocalDevMode()) {
            EnvHelper.getInstance().addMissingHeaderForLocalDevMode(requestHeader);
        }

        try{
            byte[] sigKey = Base64.getDecoder().decode(signingKey);
            String signature = signatureHelper.calculateSignature(method, url, sigKey, signingAlgorithm, signingKeyId, headerElementsForSignature, requestHeader);
            requestHeader.put("Signature", signature);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Assert.assertTrue("Trouble creating Signature!", false);
        }
        return requestHeader;
    }
}
