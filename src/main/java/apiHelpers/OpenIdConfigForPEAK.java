package apiHelpers;

import com.jayway.restassured.response.Response;
import org.apache.log4j.Logger;
import utils.BaseStep;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OpenIdConfigForPEAK implements BaseStep {
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
        openIdConfigResponse= restHelper.getResponse(url);
        logger.info("Response: "+ openIdConfigResponse.getBody().asString());

    }

    public void retrieveJwksUriResponse(){
        jwksUriResponse= restHelper.getResponse(jwksURI);
        logger.info("Response of JWKS URI: "+ jwksUriResponse.getBody().asString());

    }

    public Response getOpenIdConfigResponse() {
        return openIdConfigResponse;
    }

    public String jwksUriInResponse(){
        return restHelper.getResponseBodyValue(openIdConfigResponse, "jwks_uri");
    }

    public String issuerInResponse(){
        return restHelper.getResponseBodyValue(openIdConfigResponse, "issuer");
    }

    public String responseTypesSupportedInResponse(){
        try{
            return restHelper.getJsonArray(openIdConfigResponse, "response_types_supported").get(0).toString();
        }
        catch (NullPointerException e){}

        return null;
    }

    public String algValueInResponse(){

        try{
            return restHelper.getJsonArray(openIdConfigResponse, "id_token_signing_alg_values_supported").get(0).toString();
        }
        catch (NullPointerException e){}

        return null;
    }

    public String scopesSupportedInResponse(){
        try{
            return restHelper.getJsonArray(openIdConfigResponse, "scopes_supported").get(0).toString();
        }
        catch (NullPointerException e){}

        return null;
    }

    public String subjectTypesSupportedInResponse(){
        try{
            return restHelper.getJsonArray(openIdConfigResponse, "subject_types_supported").get(0).toString();
        }
        catch (NullPointerException e){}

        return null;
    }

    public String claimsSupportedInResponse(){
        try{
            return restHelper.getJsonArray(openIdConfigResponse, "claims_supported").get(0).toString();
        }
        catch (NullPointerException e){}

        return null;
    }

    public void retrieveKeysValues(){
        keysValue= restHelper.getJsonArray(jwksUriResponse, "keys");
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





}
