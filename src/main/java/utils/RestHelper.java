package utils;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestLogSpecification;
import com.jayway.restassured.specification.RequestSpecification;
import org.apache.log4j.Logger;
import org.junit.Assert;
import java.util.HashMap;
import java.util.List;
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
    public  Response getRequestWithHeaders(String url, HashMap<String, String> headers){

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


    public Response postRequestWithBody(String url, HashMap<String, String> body){

        Response response=null;
        try{
            response = given().body(body).when().post(url);

        }catch(Exception e){

            Assert.assertTrue(e.getMessage(), false);
        }

        return response;
    }

    public Response postRequestWithHeaderAndBody(String url, HashMap headers,HashMap body){

        Response response=null;
        try{
            response = given().log().all().headers(headers).body(body).when().post(url);

        }catch(Exception e){

            Assert.assertTrue(e.getMessage(), false);
        }

        return response;
    }


    public Response postRequestWithEncodedBody(String url, RequestSpecification requestSpecification){
        Response response=null;
        try{
            System.out.println("Request Log::: "+ requestSpecification.log().all().toString());
            response = requestSpecification.post(url);
        }catch(Exception e){

            Assert.assertTrue(e.getMessage(), false);
        }

        return response;
    }


    public  JsonPath getJsonPath (Response res) {
        String json = res.asString();
        return new JsonPath(json);
    }


    public String getResponseHeaderValue(Response res, String headerName){
        String value=null;

        try{
            value= res.getHeader(headerName);
        }
        catch(NullPointerException e){

        }
        catch (IllegalArgumentException e){

        }

        return value;
    }


    public String getResponseBodyValue(Response res, String key){
        String value=null;

        try{
            value= res.path(key).toString();
        }
        catch(NullPointerException e){

        }
        catch (IllegalArgumentException e){

        }

        return value;
    }

    public List getJsonArray(Response res, String key){

        List<HashMap<String, String>> list= res.jsonPath().getList(key);

        return list;
    }

    public int getResponseStatusCode(Response res){
        return res.statusCode();
    }

    public String getErrorMessage(Response res){
        return getResponseBodyValue(res, "message");

    }

    public String getErrorDescription(Response res){

        List<HashMap<String, String>> errorDetails= getJsonArray(res, "errors");

        String errorDesc=null;


        try{
            errorDesc= errorDetails.get(0).get("errorDescription");

        } catch (NullPointerException e){
            return null;
        }
        catch (IllegalArgumentException e){

        }

        return errorDesc;
    }


    public String getErrorCode(Response res){

        List<HashMap<String, String>> errorDetails= getJsonArray(res, "errors");

        String errorCode=null;


        try{
            errorCode= errorDetails.get(0).get("errorCode");

        } catch (NullPointerException e){
            return null;
        }
        catch (IllegalArgumentException e){

        }

        return errorCode;
    }



}
