package apiHelpers;

import com.jayway.restassured.response.Response;
import managers.UtilManager;
import org.apache.log4j.Logger;


public class DeleteEmployee extends UtilManager {
    final static Logger logger = Logger.getLogger(DeleteEmployee.class);
    private Response response = null;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public void makeRequest(String url) {
        response = getRestHelper().getDelete(url);
        logger.info("********** get response *********** \n" + response.prettyPrint());
    }


}

