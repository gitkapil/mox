package utils;

import com.google.common.collect.Lists;
import org.tomitribe.auth.signatures.Algorithm;
import org.tomitribe.auth.signatures.Signer;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.Key;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SignatureHelper {
    private static final String REQUEST_TARGET = "(request-target)";

    public String calculateSignature(String method, String url, byte[] keyData, String algorithm, String keyId, Set<String> includeHeaders, Map<String, String> presentHeaders) throws IOException {
        final Algorithm algo = Algorithm.get(algorithm);
        final Key key = new SecretKeySpec(keyData, algorithm);
        final Signer signer =
                new Signer(key, new org.tomitribe.auth.signatures.Signature(keyId, algo, null,
                        obtainSignatureHeaders(presentHeaders.keySet(), includeHeaders)));
        org.tomitribe.auth.signatures.Signature signed = signer.sign(method, url, presentHeaders);
        return signed.toString().replace("Signature ", "");
    }

    private List<String> obtainSignatureHeaders(Set<String> presentHeaders, Set<String> includeHeaders) {
        List<String> sigHeaders = presentHeaders.stream().map(String::toLowerCase).filter(includeHeaders::contains).collect(Collectors.toList());
        if (includeHeaders.contains(REQUEST_TARGET)) {
            sigHeaders.add(REQUEST_TARGET);
        }
        return sigHeaders;
    }
}
