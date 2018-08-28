package utils;

import com.nimbusds.jose.*;
import com.nimbusds.jose.jwk.source.*;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.*;
import com.nimbusds.jwt.proc.*;
import org.junit.Assert;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;


public class JWTHelper {
    /**
     *
     * @param token JWT token to be validated
     * @param jwks_uri the tenant's JWKS Url
     * @return the claim set from the JWT once it gets validated else null
     */
    public JWTClaimsSet validateJWT(String token, String jwks_uri){
        JWTClaimsSet claimsSet = null;
        // Set up a JWT processor to parse the tokens and then check their signature
        // and validity time window (bounded by the "iat", "nbf" and "exp" claims)
        ConfigurableJWTProcessor jwtProcessor = new DefaultJWTProcessor();

        // The public RSA keys to validate the signatures will be sourced from the
        // OAuth 2.0 server's JWK set, published at a well-known URL. The RemoteJWKSet
        // object caches the retrieved keys to speed up subsequent look-ups and can
        // also gracefully handle key-rollover
        JWKSource keySource = null;
        try {
            keySource = new RemoteJWKSet(new URL(jwks_uri));
        } catch (MalformedURLException e) {
            Assert.assertTrue(e.getMessage(), false);
        }

        // The expected JWS algorithm of the access tokens (agreed out-of-band)
        JWSAlgorithm expectedJWSAlg = JWSAlgorithm.RS256;

         // Configure the JWT processor with a key selector to feed matching public
        // RSA keys sourced from the JWK set URL
        JWSKeySelector keySelector = new JWSVerificationKeySelector(expectedJWSAlg, keySource);
        jwtProcessor.setJWSKeySelector(keySelector);

        // Process the token
        SecurityContext ctx = null; // optional context parameter, not required here

        try {
            claimsSet = jwtProcessor.process(token, ctx);
        } catch (ParseException e) {
            Assert.assertTrue(e.getMessage(), false);
        } catch (BadJOSEException e) {
            Assert.assertTrue(e.getMessage(), false);
        } catch (JOSEException e) {
            Assert.assertTrue(e.getMessage(), false);
        } catch (Exception e){
            System.out.println(e.getMessage());
//            Assert.assertTrue(e.getMessage(), false);
        }

        // Print out the token claims set
        if (claimsSet!=null)
        {
            System.out.println("Claim Set as follows:::");
            System.out.println(claimsSet.toJSONObject());
            //System.out.println("ver : "+claimsSet.toJSONObject().getAsString("ver"));
        }

        return claimsSet;
    }


}
