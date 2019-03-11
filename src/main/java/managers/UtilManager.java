package managers;

import utils.*;

public class UtilManager {

    private RestHelper restHelper;
    private FileHelper fileHelper;
    private DateHelper dateHelper;
    private JWTHelper jwtHelper;
    private General general;
    private SignatureHelper signatureHelper;

    public RestHelper getRestHelper() {
        return (restHelper == null) ? restHelper = new RestHelper() : restHelper;
    }

    public FileHelper getFileHelper() {
        return (fileHelper == null) ? fileHelper = new FileHelper() : fileHelper;
    }

    public DateHelper getDateHelper() {
        return (dateHelper == null) ? dateHelper = new DateHelper() : dateHelper;
    }

    public JWTHelper getJwtHelper() {
        return (jwtHelper == null) ? jwtHelper = new JWTHelper() : jwtHelper;
    }

    public General getGeneral() {
        return (general == null) ? general = new General() : general;
    }

    public SignatureHelper getSignatureHelper() {
        return (signatureHelper == null) ? signatureHelper = new SignatureHelper() : signatureHelper;
    }
}
