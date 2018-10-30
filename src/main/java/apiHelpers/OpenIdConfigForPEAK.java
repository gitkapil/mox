package apiHelpers;

import com.jayway.restassured.response.Response;
import managers.UtilManager;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OpenIdConfigForPEAK extends UtilManager{

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
        openIdConfigResponse= getRestHelper().getResponse(url);
        logger.info("Response: "+ openIdConfigResponse.getBody().asString());

    }

    public void retrieveJwksUriResponse(){
        jwksUriResponse= getRestHelper().getResponse(jwksURI);
        logger.info("Response of JWKS URI: "+ jwksUriResponse.getBody().asString());

    }

    public Response getOpenIdConfigResponse() {
        return openIdConfigResponse;
    }

    public String jwksUriInResponse(){
        return getRestHelper().getResponseBodyValue(openIdConfigResponse, "jwks_uri");
    }

    public String issuerInResponse(){
        return getRestHelper().getResponseBodyValue(openIdConfigResponse, "issuer");
    }

    public String responseTypesSupportedInResponse(){
        try{
            return getRestHelper().getJsonArray(openIdConfigResponse, "response_types_supported").get(0).toString();
        }
        catch (NullPointerException e){}

        return null;
    }

    public String algValueInResponse(){

        try{
            return getRestHelper().getJsonArray(openIdConfigResponse, "id_token_signing_alg_values_supported").get(0).toString();
        }
        catch (NullPointerException e){}

        return null;
    }

    public String scopesSupportedInResponse(){
        try{
            return getRestHelper().getJsonArray(openIdConfigResponse, "scopes_supported").get(0).toString();
        }
        catch (NullPointerException e){}

        return null;
    }

    public String subjectTypesSupportedInResponse(){
        try{
            return getRestHelper().getJsonArray(openIdConfigResponse, "subject_types_supported").get(0).toString();
        }
        catch (NullPointerException e){}

        return null;
    }

    public String claimsSupportedInResponse(){
        try{
            return getRestHelper().getJsonArray(openIdConfigResponse, "claims_supported").get(0).toString();
        }
        catch (NullPointerException e){}

        return null;
    }

    public void retrieveKeysValues(){
        keysValue= getRestHelper().getJsonArray(jwksUriResponse, "keys");
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
