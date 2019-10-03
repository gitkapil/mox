package apiHelpers;

import com.jayway.restassured.response.Response;
import managers.UtilManager;
import org.apache.log4j.Logger;
import org.junit.Assert;
import utils.*;

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
    private HashMap<String,String >requestHeader = new HashMap<>();
    public String getAuthToken() {
        return authToken;
    }
    public void setAuthToken(String authToken) {
        this.authToken = "Bearer "+ authToken;
    }
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

    public void getListOfApplications(String url, String authToken) {
        try {
            response = getRestHelper().getRequestWithHeaders(url,
                    returnRequestHeader("GET", new URL(url).getPath(),
                            authToken));

            logger.info("****************List of application response ******************** --> " + response.getBody().asString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Assert.assertTrue("Unable to get URL" , false);
        }
    }

    public void getListOfApplication(String url, String authToken, String nullHeader) {
        try {
            response = getRestHelper().getRequestWithHeaders(url,
                    getListHeaderWithMissingValues("GET", new URL(url).getPath(),
                            authToken, nullHeader));


            logger.info("****************List of application response ******************** --> " + response.getBody().prettyPrint());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Assert.assertTrue("Unable to get URL" , false);
        }
    }

    private HashMap<String, String> getListHeaderWithMissingValues(String method, String url, String authToken, String nullHeader) {
            requestHeader.put("Accept", "application/json");
            requestHeader.put("Content-Type", "application/json");
            requestHeader.put("Authorization", authToken);
            requestHeader.put("Trace-Id", getGeneral().generateUniqueUUID());
            requestHeader.put("Accept-Language", "en-US");
            requestHeader.put("Request-Date-Time", getDateHelper().getUTCNowDateTime());
            requestHeader.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));
            requestHeader.remove(nullHeader);
            return requestHeader;
    }

    private HashMap<String, String> returnRequestHeader(String method, String url, String authToken) {
        requestHeader.put("Accept", "application/json");
        requestHeader.put("Content-Type", "application/json");
        requestHeader.put("Authorization", authToken);
        requestHeader.put("Trace-Id", getGeneral().generateUniqueUUID());
        requestHeader.put("Accept-Language", "en-US");
        requestHeader.put("Request-Date-Time", getDateHelper().getUTCNowDateTime());
        requestHeader.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));
        return requestHeader;

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
