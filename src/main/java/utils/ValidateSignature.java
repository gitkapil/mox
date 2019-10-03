package utils;

import java.io.IOException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.tomitribe.auth.signatures.MissingKeyIdException;
import org.tomitribe.auth.signatures.Signature;
import org.tomitribe.auth.signatures.Verifier;

public class ValidateSignature {
    private static final String SIGNATURE = "Signature %s";
    public static void main(String asd[]) {
        boolean verifies = false;
        final String signature_value = "keyId=\"93993aec-863c-42a1-86dd-6649051ed87d\",algorithm=\"hmac-sha256\",headers=\"digest x-client-id x-event-type (request-target)\",signature=\"k+m4wRFRQXW0rCD1u6D5wqiE5s0QILR4Fb1hImJphm8=\"";
        Signature signature = null;
        try {
            signature = Signature.fromString(String.format(SIGNATURE, signature_value));
        } catch (MissingKeyIdException e) {
            System.out.println("Error during format signature"+e.getMessage());
        }
        final Key key = new SecretKeySpec(Base64.decodeBase64("aVBPT1ljMllka1ZDRytKSE84ZDhSTzRka3hZWmw3VU12VmJtWFA1WUYyZz0="), "HmacSHA256");
        final Verifier verifier = new Verifier(key, signature);
        final String method = "post";
        final String uri = "/92d79ec7-eb01-4503-8d92-7a43d930f0dc";
        final Map<String, String> headers = new HashMap<String, String>();
        headers.put("digest", "SHA-256=xths5uqC9yKtdPL8YaWoUQJf/QzSm1kWc9f4/b8xO8I=");
        headers.put("x-client-id", "93993aec-863c-42a1-86dd-6649051ed87d");
        headers.put("x-event-type", "payment.failure");
        try {
            verifies = verifier.verify(method, uri, headers);
        } catch (NoSuchAlgorithmException | SignatureException | IOException e) {
            e.printStackTrace();
        }
        if(verifies) {
            System.out.println("Success");
        }else {
            System.out.println("Signature Validation Failed");
        }
    }

}
