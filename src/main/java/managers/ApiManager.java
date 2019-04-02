package managers;

import apiHelpers.*;

public class ApiManager {
    AccessTokenForMerchants accessToken;
    PaymentRequest paymentRequest;
    PaymentStatus paymentStatus;
    Refunds refunds;
    OpenIdConfigForPEAK openIdConfig;
    Transaction transaction;
    PostClient postClient;

    public ApiManager() {
        accessToken= new AccessTokenForMerchants();
        paymentRequest= new PaymentRequest();
        paymentStatus= new PaymentStatus();
        refunds= new Refunds();
        openIdConfig= new OpenIdConfigForPEAK();
        transaction = new Transaction();
        postClient = new PostClient();
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

    public PostClient getPostClient() {
        return (postClient == null) ? postClient = new PostClient() : postClient;
    }
}
