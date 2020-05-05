package utils;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import org.apache.log4j.Logger;
import org.junit.Assert;
import java.util.HashMap;

import static com.jayway.restassured.RestAssured.given;


public class RestHelper {
    final static Logger logger = Logger.getLogger(RestHelper.class);

    public void setBaseURI(String baseURI) {
        RestAssured.baseURI = baseURI;
    }
    public String getBaseURI() {
        return RestAssured.baseURI;
    }


    public Response getResponse(String url) {
        Response response = null;
        try {
            response = given().log().all().get(url);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), false);
        }
        return response;
    }

    public Response getRequest(String url) {
        Response response = null;
        try {
           response = given().log().all()
                    .when()
                    .get(url);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), false);
        }
        return response;
    }

    public Response getDelete(String url) {
        Response response = null;
        try {
            response = given().log().all()
                    .when()
                    .delete(url);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), false);
        }
        return response;
    }



    public Response postRequestWithBody(String url, HashMap<String, String> body) {
        Response response = null;
        try {
            response = given().body(body).when().post(url);
        } catch (Exception e) {

            Assert.assertTrue(e.getMessage(), false);
        }
        return response;
    }

    public Response putRequestWithBody(String url, HashMap<String, String> body) {
        Response response = null;
        try {
            response = given().body(body).when().put(url);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), false);
        }
        return response;
    }


    public int getResponseStatusCode(Response res) {
        return res.statusCode();
    }









}
