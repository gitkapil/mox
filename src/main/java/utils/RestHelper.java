package utils;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import org.apache.log4j.Logger;
import org.junit.Assert;
import java.util.HashMap;
import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;



public class RestHelper {
    final static Logger logger = Logger.getLogger(RestHelper.class);

    public  void setBaseURI(String baseURI){
        RestAssured.baseURI = baseURI;
    }

    public  void setBasePath(String basePath){
        RestAssured.basePath = basePath;
    }

    public  String getBaseURI(){
        return RestAssured.baseURI;
    }

    public  String getBasePath(){
        return RestAssured.basePath;
    }


    public  void setcontentType(ContentType type){
        given().contentType(type);

    }

    // get Response from an endpoint
    public  Response getResponse(String url){


        logger.info("Endpoint hit-->   "+ url);
        return get(url);
    }

    // get Response from an endpoint with Headers
    public  Response getResponseWithHeaders(String url, HashMap<String, String> headers){

        Response res=null;
        logger.info("Endpoint hit-->   "+ url);

        try{
            res= given()
                    .headers(headers)
                    .when()
                    .get(url);
        }
        catch(Exception e){

            Assert.assertTrue(e.getMessage(), false);
        }


        return res;

    }


    public Response postResponseWithBody(String url, HashMap<String, String> body){

        Response response=null;
        try{
            response = given().body(body).when().post(url);
        }catch(Exception e){

            Assert.assertTrue(e.getMessage(), false);
        }

        return response;
    }


    public Response postResponseWithEncodedBody(RequestSpecification res, String url){
        Response response=null;
        try{
            response = res.post(url);
        }catch(Exception e){

            Assert.assertTrue(e.getMessage(), false);
        }

        return response;
    }


    public  JsonPath getJsonPath (Response res) {
        String json = res.asString();
        return new JsonPath(json);
    }






}
