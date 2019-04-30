package apiHelpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import managers.UtilManager;
import org.apache.log4j.Logger;
import com.jayway.restassured.response.*;
import utils.EnvHelper;
import utils.PropertyHelper;

import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;

public class PostPublicKey extends UtilManager {
    private static Logger logger = Logger.getLogger(PostPublicKey.class);
    private Response response = null;
    private String authToken;
    private HashMap<String, String> requestHeader;

    public void setAuthTokenWithBearer(String authToken) {
        this.authToken = "Bearer "+ authToken;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    private HashMap<String,String> returnRequestHeader(String method, String url, String signingKeyId, String signingAlgorithm, String signingKey, HashSet headerElementsForSignature) {
        requestHeader = new HashMap<String, String>();
        requestHeader.put("Accept","application/json");
        requestHeader.put("Content-Type","application/json");
        requestHeader.put("Authorization", authToken);
        requestHeader.put("Trace-Id",getGeneral().generateUniqueUUID());
        requestHeader.put("Accept-Language", "en-US");
        requestHeader.put("Request-Date-Time", getDateHelper().getUTCNowDateTime());
        requestHeader.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));

        if (EnvHelper.getInstance().isLocalDevMode()) {
            EnvHelper.getInstance().addMissingHeaderForLocalDevMode(requestHeader);
        }

//        try {
//            requestHeader.put("Digest", getSignatureHelper().calculateContentDigestHeader(new ObjectMapper().writeValueAsBytes(requestBody)));
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            org.junit.Assert.assertTrue("Trouble creating Digest!", false);
//        }

//        try{
//            byte[] sigKey = Base64.getDecoder().decode(signingKey);
//            String signature = getSignatureHelper().calculateSignature(method, url, sigKey, signingAlgorithm, signingKeyId, headerElementsForSignature, requestHeader);
//            requestHeader.put("Signature", signature);
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            org.junit.Assert.assertTrue("Trouble creating Signature!", false);
//        }
        return requestHeader;
    }
}
