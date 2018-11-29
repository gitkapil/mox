package utils;

import com.jayway.restassured.response.Header;
import com.jayway.restassured.response.Response;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.tomitribe.auth.signatures.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.URL;
import java.security.Key;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

public class SignatureHelper {
    private static final String REQUEST_TARGET = "(request-target)";

    public String calculateSignature(String method, String url, byte[] keyData, String algorithm, String keyId, Set<String> includeHeaders, Map<String, String> presentHeaders) throws IOException {
        final Algorithm algo = Algorithm.get(algorithm);
        final Key key = new SecretKeySpec(keyData, algorithm);
        final Signer signer =
                new Signer(key, new Signature(keyId, algo, null, obtainSignatureHeaders(presentHeaders.keySet(), includeHeaders)));
        Signature signed = signer.sign(method, url, presentHeaders);
        return signed.toString().replace("Signature ", "");
    }

    private List<String> obtainSignatureHeaders(Set<String> presentHeaders, Set<String> includeHeaders) {
        List<String> sigHeaders = presentHeaders.stream().map(String::toLowerCase).filter(includeHeaders::contains).collect(Collectors.toList());
        if (includeHeaders.contains(REQUEST_TARGET)) {
            sigHeaders.add(REQUEST_TARGET);
        }
        return sigHeaders;
    }

    public void verifySignature(Response response, String method, String url, byte[] keyData, String algorithm) throws Exception {
        String signatureHeaderVal = response.getHeader("Signature");

        if (response.getStatusCode() < 200 || response.getStatusCode() >= 300) {
            if (StringUtils.isNotEmpty(signatureHeaderVal)) {
                throw new Exception("Signature header found where it wasn't expected");
            } else {
                return;  // Nothing to verify.
            }
        }

        if (StringUtils.isEmpty(signatureHeaderVal) && response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
            throw new Exception("No Signature Found");
        }

        Signature parsedSig = Signature.fromString(String.format("Signature %s", signatureHeaderVal));

        Verifier verifier = new Verifier(new SecretKeySpec(keyData, algorithm), parsedSig);

        if (!verifier.verify(method, new URL(url).getPath(), response.getHeaders().asList().stream().collect(toMap(Header::getName, Header::getValue)))) {
            throw new Exception("Signature failed validation");
        }
    }

    public String calculateContentDigestHeader(byte[] content){
        //final byte[] digest = MessageDigest.getInstance("SHA-256").digest(content);
        final String digestHeader = "SHA-256=" + new String(content);
        return digestHeader;
    }
}
