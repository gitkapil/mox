package apiHelpers;

import com.jayway.restassured.response.Response;
import managers.UtilManager;
import org.apache.log4j.Logger;

public class GetEmployeeDetails extends UtilManager {
    final static Logger logger = Logger.getLogger(GetEmployeeDetails.class);
    private Response response = null;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public void makeRequest(String url) {

        response = getRestHelper().getRequest(url);
        logger.info("********** get request *********** \n" + response.prettyPrint());
    }

}

