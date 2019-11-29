package utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.tomitribe.auth.signatures.Signature;
import org.tomitribe.auth.signatures.Verifier;

import com.google.common.io.CharStreams;

public class ValidateSignature {
    private static String DIGEST_VALUE = "SHA-256=SwrJhHizjCpiqpl7zfvTtPMiQTbePIdNwMFFoY9L9Oo=";

    public static void main(String asd[]) {
        ValidateSignature instance = new ValidateSignature();
        instance.veryfySignature();
        instance.veryfyDigest();
    }

    public void veryfySignature() {
        final String authorization = "Signature keyId=\"ac65ee37-727c-4f77-b538-f9595e775b62\",algorithm=\"hmac-sha256\",headers=\"digest x-client-id x-event-type (request-target)\",signature=\"47CpsUsLe1Gb2EIH/rcY8rbOchCk+dt1DMIeGrFx+28=\"";
        final Signature signature = Signature.fromString(authorization);
        final Key key = new SecretKeySpec(Base64.decodeBase64("alZpMzVSUVhzanJLelhBWUtxUDZTWG1yMG1WL0x1NFZMZmJCWjQwbUtSND0="), "HmacSHA256");
        final Verifier verifier = new Verifier(key, signature);

        {
            //before webhook
            final String method = "post";
            final String uri = "/149cbdf3-ab67-4013-bf7b-ed96463ee0bc";
            final Map<String, String> headers = new HashMap<String, String>();
            headers.put("digest", DIGEST_VALUE);

            //from webhook site
            headers.put("x-client-id", "0bce48c3-d2a3-458d-a553-964211c920c9");
            headers.put("x-event-type", "payment.failure");
            boolean verifies = false;
            try {
                verifies = verifier.verify(method, uri, headers);
            } catch (NoSuchAlgorithmException | SignatureException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (verifies) {
                System.out.println("Signature Validation Success");
            } else {
                System.out.println("Signature Validation Failed");
            }
        }
    }

    public void veryfyDigest() {
        try {
            String content = "{\"reasonCode\":\"EW2036\",\"reasonDescription\":\"PayMe user Annual Pay limit is reached.\",\"paymentRequestId\":\"e8109f3f-9942-4a11-a657-cd9ef93eb806\",\"paymentRequestType\":\"Dynamic\",\"totalAmount\":\"99.77\",\"currencyCode\":\"HKD\",\"appSuccessCallback\":\"https://www.example.com/success\",\"appFailCallback\":\"https://www.example.com/faliure\",\"createdTime\":\"2019-11-08T09:22:44.296Z\",\"effectiveDuration\":600,\"statusDescription\":\"Request for Payment Initiated\",\"statusCode\":\"PR001\"}";
            //convertJsonToBytes("c:/digest_payload/data.json");
            byte[] content_byte = content.getBytes(StandardCharsets.UTF_8);
            if (content != null) {
                String calcDigest = calculateContentDiges(content_byte);
                if (!calcDigest.equals(DIGEST_VALUE)) {
                    System.out.println("Digest Validation Failed");
                } else {
                    System.out.println("Digest Validation Success");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String convertJsonToBytes(String file) throws Exception {
        InputStream is = null;
        Reader reader = null;
        String str = null;
        try {
            is = getClass().getResourceAsStream(file);
            reader = new InputStreamReader(is);
            str = CharStreams.toString(reader);
            return str;
        } catch (Exception e) {
            throw e;
        } finally {
            if (is != null) {
                is.close();
            }
            if (reader != null) {
                reader.close();
            }
        }
    }

    public String calculateContentDiges(byte[] content) throws Exception {
        String returnString = "";
        try {
            final byte[] digest = DigestUtils.sha256(content);
            //We need to check how this will work against chinese encoding when we start allowing chinese
            String val = new String(Base64.encodeBase64(digest), "UTF-8");
            returnString = "SHA-256=" + val;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return returnString;
    }
}
