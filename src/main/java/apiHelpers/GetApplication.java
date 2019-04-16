package apiHelpers;

import org.apache.log4j.Logger;

public class GetApplication {
    private final static Logger logger = Logger.getLogger(GetApplication.class);

    public void getListOfApplications(String url) {
        try {
            HashMap<String, String> header = generateHeader("GET", url, signingKeyId, signingAlgorithm, signingKey, headerElements)
        }
    }
}
