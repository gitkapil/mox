package apiHelpers;

import com.jayway.restassured.response.Response;
import managers.UtilManager;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OpenIdConfigForPEAK extends UtilManager{
    final static Logger logger = Logger.getLogger(OpenIdConfigForPEAK.class);

    private Response openIdConfigResponse, jwksUriResponse;
    private String jwksURI=null;
    private List<HashMap<String, String>> keysValue= new ArrayList<>();

    /**
     *
     * Getters
     */
    public String getJwksURI() {
        return jwksURI;
    }

    public Response getJwksUriResponse() {
        return jwksUriResponse;
    }

    public Response getOpenIdConfigResponse() {
        return openIdConfigResponse;
    }

    /**
     *
     * Setters
     */
    public void setJwksUriResponse(Response jwksUriResponse) {
        this.jwksUriResponse = jwksUriResponse;
    }

    public void setJwksURI(String jwksURI) {
        this.jwksURI = jwksURI;
    }


    /**
     *
     * This method hits the openidconfig URL
     */
    public void retrieveOpenIdConfigResponse(String url){
        openIdConfigResponse= getRestHelper().getResponse(url);
        logger.info("Response: "+ openIdConfigResponse.getBody().asString());

    }

    /**
     *
     * This method hits the JwksURI URL
     */
    public void retrieveJwksUriResponse(){
        jwksUriResponse= getRestHelper().getResponse(jwksURI);
        logger.info("Response of JWKS URI: "+ jwksUriResponse.getBody().asString());

    }

    /**
     *
     * @returns jwks_uri from the response
     */
    public String jwksUriInResponse(){
        return getRestHelper().getResponseBodyValue(openIdConfigResponse, "jwks_uri");
    }

    /**
     *
     * @returns issuer from the response
     */
    public String issuerInResponse(){
        return getRestHelper().getResponseBodyValue(openIdConfigResponse, "issuer");
    }

    /**
     *
     * @returns response_types_supported from the response
     */
    public String responseTypesSupportedInResponse(){
        try{
            return getRestHelper().getJsonArray(openIdConfigResponse, "response_types_supported").get(0).toString();
        }
        catch (NullPointerException e){}

        return null;
    }

    /**
     *
     * @returns id_token_signing_alg_values_supported from the response
     */
    public String algValueInResponse(){

        try{
            return getRestHelper().getJsonArray(openIdConfigResponse, "id_token_signing_alg_values_supported").get(0).toString();
        }
        catch (NullPointerException e){}

        return null;
    }

    /**
     *
     * @returns scopes_supported from the response
     */
    public String scopesSupportedInResponse(){
        try{
            return getRestHelper().getJsonArray(openIdConfigResponse, "scopes_supported").get(0).toString();
        }
        catch (NullPointerException e){}

        return null;
    }

    /**
     *
     * @returns subject_types_supported from the response
     */
    public String subjectTypesSupportedInResponse(){
        try{
            return getRestHelper().getJsonArray(openIdConfigResponse, "subject_types_supported").get(0).toString();
        }
        catch (NullPointerException e){}

        return null;
    }

    /**
     *
     * @returns claims_supported from the response
     */
    public String claimsSupportedInResponse(){
        try{
            return getRestHelper().getJsonArray(openIdConfigResponse, "claims_supported").get(0).toString();
        }
        catch (NullPointerException e){}

        return null;
    }

    /**
     *
     * @returns keys from the response
     */
    public void retrieveKeysValues(){
        keysValue= getRestHelper().getJsonArray(jwksUriResponse, "keys");
    }

    /**
     *
     * @returns alg from the JWKS response
     */
    public String algInJWKSResponse(){

        try{
            return keysValue.get(0).get("alg");
        }
        catch (NullPointerException e){}

        return null;
    }

    /**
     *
     * @returns kty from the JWKS response
     */
    public String ktyInJWKSResponse(){

        try{
            return keysValue.get(0).get("kty");
        }
        catch (NullPointerException e){}

        return null;
    }


    /**
     *
     * @returns use from the response
     */
    public String useInJWKSResponse(){

        try{
            return keysValue.get(0).get("use");
        }
        catch (NullPointerException e){}

        return null;
    }

}
