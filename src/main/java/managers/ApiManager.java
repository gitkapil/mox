package managers;

import apiHelpers.*;

public class ApiManager {
    AccessTokenForMerchants accessToken;
    PaymentRequest paymentRequest;
    PaymentStatus paymentStatus;
    Refunds refunds;
    OpenIdConfigForPEAK openIdConfig;
    Transaction transaction;
    PostApplication postApplication;
    GetApplication getApplication;
    PutApplication putApplication;
    PostPublicKey postPublicKey;

    public ApiManager() {
        accessToken= new AccessTokenForMerchants();
        paymentRequest= new PaymentRequest();
        paymentStatus= new PaymentStatus();
        refunds= new Refunds();
        openIdConfig= new OpenIdConfigForPEAK();
        transaction = new Transaction();
        postApplication = new PostApplication();
        getApplication = new GetApplication();
        putApplication = new PutApplication();
        postPublicKey = new PostPublicKey();
    }

    public PostPublicKey getPostPublicKey() {
        return postPublicKey;
    }

    public void setPostPublicKey(PostPublicKey postPublicKey) {
        this.postPublicKey = postPublicKey;
    }

    public AccessTokenForMerchants getAccessToken() {
        return (accessToken == null) ? accessToken = new AccessTokenForMerchants() : accessToken;
    }

    public PaymentRequest getPaymentRequest() {
        return (paymentRequest == null) ? paymentRequest = new PaymentRequest() : paymentRequest;
    }

    public PaymentStatus getPaymentStatus() {
        return (paymentStatus == null) ? paymentStatus = new PaymentStatus() : paymentStatus;
    }

    public Refunds getRefunds() {
        return (refunds == null) ? refunds = new Refunds() : refunds;
    }

    public OpenIdConfigForPEAK getOpenIdConfig() {
        return (openIdConfig == null) ? openIdConfig = new OpenIdConfigForPEAK() : openIdConfig;
    }

    public Transaction getTransaction() {
        return (transaction == null) ? transaction = new Transaction(): transaction;
    }

    public PostApplication getPostApplication() {
        return (postApplication == null) ? postApplication = new PostApplication() : postApplication;
    }

    public GetApplication getGetApplication() {
        return (getApplication == null) ? getApplication = new GetApplication() : getApplication;
    }

    public PutApplication getPutApplication() {
        return (putApplication == null) ? putApplication = new PutApplication() : putApplication;
    }
}
