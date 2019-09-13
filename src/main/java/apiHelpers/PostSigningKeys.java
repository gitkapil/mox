package apiHelpers;
import com.jayway.restassured.response.Response;
import managers.UtilManager;
import org.junit.Assert;
import utils.EnvHelper;
import utils.PropertyHelper;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import static apiHelpers.PutPublicKeys.logger;

public class PostSigningKeys extends UtilManager {

    private String authToken;
    private String applicationId;
    private String activateAt;
    private String deactivateAt;
    private String entityStatus;
    private Response response = null;
    private HashMap<String, String> requestHeader = new HashMap();
    private HashMap requestBody = new HashMap();

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    private HashMap returnRequestBody() {
        requestBody.clear();
        requestBody.put("activateAt", activateAt);
        requestBody.put("deactivateAt", deactivateAt);
        requestBody.put("entityStatus", entityStatus);
        return requestBody;
    }


    private HashMap<String, String> returnRequestHeader() {
        requestHeader.clear();
        requestHeader.put("Accept", "application/json");
        requestHeader.put("Content-Type", "application/json");
        requestHeader.put("Authorization", authToken);
        requestHeader.put("Trace-Id", getGeneral().generateUniqueUUID());
        requestHeader.put("Request-Date-Time", getDateHelper().getUTCNowDateTime());
        requestHeader.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));

        if (EnvHelper.getInstance().isLocalDevMode()) {
            EnvHelper.getInstance().addMissingHeaderForLocalDevMode(requestHeader);
        }
       return requestHeader;
    }

    public void makeRequest(String url) {

        returnRequestHeader();
        response = getRestHelper().postRequestWithHeaderAndBody(url, requestHeader,returnRequestBody());
        System.out.println("response after make request: " + response.getBody().prettyPrint());
    }

    public void setAuthTokenWithBearer(String authToken) {
        this.authToken = "Bearer " + authToken;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getActivateAt() {
        return activateAt;
    }

    public void setActivateAt(String activateAt) {
        this.activateAt = activateAt;
    }

    public String getDeactivateAt() {
        return deactivateAt;
    }

    public void setDeactivateAt(String deactivateAt) {
        this.deactivateAt = deactivateAt;
    }

    public String getEntityStatus() {
        return entityStatus;
    }

    public void setEntityStatus(String entityStatus) {
        this.entityStatus = entityStatus;
    }

    public Response executePostRequestWithMissingHeaderKeys(String url, String keys) {

        try {
            HashMap<String, String> header = returnRequestHeaderWithMissingKeys("POST", new URL(url).getPath(), keys);
            response = getRestHelper().postRequestWithHeaderAndBody(url, header, returnRequestBody());
            logger.info("Response: " + response.getBody().prettyPrint());
        } catch (Exception e) {
            Assert.assertTrue("Verification of signature failed!", false);
        }
        return response;
    }



    public HashMap<String, String> returnRequestHeaderWithMissingKeys(String method, String url, String keys) throws IOException {
        requestHeader.clear();
        requestHeader.put("Accept", "application/json");
        requestHeader.put("Content-Type", "application/json");
        requestHeader.put("Authorization", authToken);
        requestHeader.put("Trace-Id", getGeneral().generateUniqueUUID());
        requestHeader.put("Request-Date-Time", getDateHelper().getUTCNowDateTime());
        requestHeader.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));
        if (EnvHelper.getInstance().isLocalDevMode()) {
            EnvHelper.getInstance().addMissingHeaderForLocalDevMode(requestHeader);
        }
        requestHeader.remove(keys);
        return requestHeader;
    }
}