package apiHelpers;

import managers.UtilManager;
import com.jayway.restassured.response.Response;
import org.apache.log4j.Logger;
import org.junit.Assert;
import utils.DateHelper;
import utils.EnvHelper;
import utils.General;
import utils.PropertyHelper;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import static com.jayway.restassured.RestAssured.given;

public class GetSigningKey extends UtilManager {
    private String authToken;
    private String applicationId;
    private Response response = null;
    private String keyId;
    private String keyName;
    private String entityStatus;

    private HashMap<String, String> requestHeader;
    private static Logger logger = Logger.getLogger(GetPublicKey.class);
    General general = new General();
    DateHelper dateHelper;
    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public void setAuthTokenWithBearer(String authToken) {

        this.authToken = "Bearer "+ authToken;
    }
    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getEntityStatus() {
        return entityStatus;
    }

    public void setEntityStatus(String entityStatus) {
        this.entityStatus = entityStatus;
    }
    public String getAuthToken() { return authToken; }
    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public void makeCallWithApplicationID(String url, String applicationID) {
        returnRequestHeader();
        response =
                getRestHelper().getRequestWithHeaders(url + applicationID + "/keys/signing", requestHeader);
        logger.info("********** GET Public Key Response *********** ----> "+ response.getBody().prettyPrint());
    }

    private HashMap<String, String> getListHeaderWithMissingValues(String method, String url
            , String authToken, String nullHeader) {
        HashMap<String,String> header = new HashMap<>();
        header.put("ACCEPT", "application/json");
        header.put("Authorization", authToken);
        header.put("Trace-Id", general.generateUniqueUUID());
        header.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));
        header.put("Request-Date-Time", dateHelper.getUTCNowDateTime());
        header.put("Content-Type", "application/json");
        header.remove(nullHeader);
        return header;
    }


    public void makeCallWithMissingHeader(String url, String applicationID, String headerValue) {
        returnRequestHeaderWithApiVersionAndAuthorization(headerValue);
        response =
                getRestHelper().getRequestWithHeaders(url + applicationID + "/keys/signing", returnRequestHeaderWithApiVersionAndAuthorization(headerValue));
        logger.info("********** GET Public Key Response *********** ----> "+ response.getBody().prettyPrint());
    }

    public void makeCall(String url, String headerKey, String headerValue, String applicationId) {

        response = getRestHelper().getRequestWithHeadersWithEncode(url + applicationId + "/keys/signing", returnRequestHeaderInvalidInputs(headerKey, headerValue));
        logger.info("********** GET Public Key Response *********** ----> "+ response.getBody().prettyPrint());
    }

    private HashMap<String,String> returnRequestHeader() {
        requestHeader = new HashMap<String, String>();
        requestHeader.put("Accept","application/json");
        requestHeader.put("Content-Type","application/json");
        requestHeader.put("Authorization", authToken);
        requestHeader.put("Trace-Id",getGeneral().generateUniqueUUID());
        requestHeader.put("Accept-Language", "en-US");
        requestHeader.put("Request-Date-Time", getDateHelper().getUTCNowDateTime());
        requestHeader.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));
        return requestHeader;
    }


    private HashMap<String,String> returnRequestHeaderWithMissingHeader(String nullHeaderValue) {
        requestHeader = new HashMap<String, String>();
        requestHeader.put("Accept","application/json");
        requestHeader.put("Content-Type","application/json");
        requestHeader.put("Authorization", authToken);
        requestHeader.put("Trace-Id",getGeneral().generateUniqueUUID());
        requestHeader.put("Accept-Language", "en-US");
        requestHeader.put("Request-Date-Time", getDateHelper().getUTCNowDateTime());
        requestHeader.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));
        requestHeader.remove(nullHeaderValue);
        return requestHeader;
    }

    private HashMap<String,String> returnRequestHeaderWithApiVersionAndAuthorization(String missingHeader) {
        requestHeader = new HashMap<String, String>();
        requestHeader.put("Accept","application/json");
        requestHeader.put("Content-Type","application/json");
        requestHeader.put("Authorization", authToken);
        requestHeader.put("Accept-Language", "en-US");
        requestHeader.put("Trace-Id",getGeneral().generateUniqueUUID());
        requestHeader.put("Request-Date-Time", getDateHelper().getUTCNowDateTime());
        requestHeader.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));

        if (EnvHelper.getInstance().isLocalDevMode()) {
            EnvHelper.getInstance().addMissingHeaderForLocalDevMode(requestHeader);
        }
        requestHeader.remove(missingHeader);
        return requestHeader;
    }

    private HashMap<String,String> returnRequestHeaderInvalidInputs(String headerKey, String headerValue) {
        requestHeader = new HashMap<String, String>();
        requestHeader.put("Accept","application/json");
        requestHeader.put("Content-Type","application/json");
        requestHeader.put("Authorization", authToken);
        requestHeader.put("Trace-Id",getGeneral().generateUniqueUUID());
        requestHeader.put("Accept-Language", "en-US");
        requestHeader.put("Request-Date-Time", getDateHelper().getUTCNowDateTime());
        requestHeader.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));
        requestHeader.replace(headerKey,headerValue );
        return requestHeader;
    }
}

