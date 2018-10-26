package apiHelpers;

import com.jayway.restassured.response.Response;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OpenIdConfigForPEAK{
    TestContext testContext;

    public OpenIdConfigForPEAK(TestContext testContext) {
        this.testContext = testContext;
    }

    final static Logger logger = Logger.getLogger(OpenIdConfigForPEAK.class);
    Response openIdConfigResponse, jwksUriResponse; String jwksURI=null; List<HashMap<String, String>> keysValue= new ArrayList<>();

    public String getJwksURI() {
        return jwksURI;
    }

    public Response getJwksUriResponse() {
        return jwksUriResponse;
    }

    public void setJwksUriResponse(Response jwksUriResponse) {
        this.jwksUriResponse = jwksUriResponse;
    }

    public void setJwksURI(String jwksURI) {
        this.jwksURI = jwksURI;
    }

    public void retrieveOpenIdConfigResponse(String url){
        openIdConfigResponse= testContext.getUtilManager().getRestHelper().getResponse(url);
        logger.info("Response: "+ openIdConfigResponse.getBody().asString());

    }

    public void retrieveJwksUriResponse(){
        jwksUriResponse= testContext.getUtilManager().getRestHelper().getResponse(jwksURI);
        logger.info("Response of JWKS URI: "+ jwksUriResponse.getBody().asString());

    }

    public Response getOpenIdConfigResponse() {
        return openIdConfigResponse;
    }

    public String jwksUriInResponse(){
        return testContext.getUtilManager().getRestHelper().getResponseBodyValue(openIdConfigResponse, "jwks_uri");
    }

    public String issuerInResponse(){
        return testContext.getUtilManager().getRestHelper().getResponseBodyValue(openIdConfigResponse, "issuer");
    }

    public String responseTypesSupportedInResponse(){
        try{
            return testContext.getUtilManager().getRestHelper().getJsonArray(openIdConfigResponse, "response_types_supported").get(0).toString();
        }
        catch (NullPointerException e){}

        return null;
    }

    public String algValueInResponse(){

        try{
            return testContext.getUtilManager().getRestHelper().getJsonArray(openIdConfigResponse, "id_token_signing_alg_values_supported").get(0).toString();
        }
        catch (NullPointerException e){}

        return null;
    }

    public String scopesSupportedInResponse(){
        try{
            return testContext.getUtilManager().getRestHelper().getJsonArray(openIdConfigResponse, "scopes_supported").get(0).toString();
        }
        catch (NullPointerException e){}

        return null;
    }

    public String subjectTypesSupportedInResponse(){
        try{
            return testContext.getUtilManager().getRestHelper().getJsonArray(openIdConfigResponse, "subject_types_supported").get(0).toString();
        }
        catch (NullPointerException e){}

        return null;
    }

    public String claimsSupportedInResponse(){
        try{
            return testContext.getUtilManager().getRestHelper().getJsonArray(openIdConfigResponse, "claims_supported").get(0).toString();
        }
        catch (NullPointerException e){}

        return null;
    }

    public void retrieveKeysValues(){
        keysValue= testContext.getUtilManager().getRestHelper().getJsonArray(jwksUriResponse, "keys");
    }

    public String algInJWKSResponse(){

        try{
            return keysValue.get(0).get("alg");
        }
        catch (NullPointerException e){}

        return null;
    }

    public String ktyInJWKSResponse(){

        try{
            return keysValue.get(0).get("kty");
        }
        catch (NullPointerException e){}

        return null;
    }

    public String useInJWKSResponse(){

        try{
            return keysValue.get(0).get("use");
        }
        catch (NullPointerException e){}

        return null;
    }





}
