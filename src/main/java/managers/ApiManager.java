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
    GetSigningKey getSigningKey;
    PostSigningKeys postSigningKeys;
    PutPublicKeys putPublicKeys;
    PostPublicKey postPublicKey;
    GetPublicKey getPublicKey;
    PutSigningKeys putSigningKeys;
    CreateClient createClient;
    PostPassword_CreateClientPassword postPasswordCreateClientPassword;

    GetPassword getPassword;

    OneClickMerchantOnboarding oneClickMerchantOnboarding;


    String merchantManagementSigningKeyId;
    String merchantManagementSigningKey;

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
        postSigningKeys = new PostSigningKeys();
        putPublicKeys = new PutPublicKeys();
        postPublicKey = new PostPublicKey();
        getPublicKey = new GetPublicKey();
        getSigningKey = new GetSigningKey();
        putSigningKeys = new PutSigningKeys();
        createClient = new CreateClient();
        postPasswordCreateClientPassword = new PostPassword_CreateClientPassword();

        getPassword = new GetPassword();

        oneClickMerchantOnboarding = new OneClickMerchantOnboarding();

    }

    public CreateClient getCreateClient() { return createClient; }

    public PostPassword_CreateClientPassword getPostPasswordCreateClientPassword() {
        return postPasswordCreateClientPassword;
    }

    public PutSigningKeys getPutSigningKeys() {
        return putSigningKeys;
    }

    public GetSigningKey getGetSigningKey() {
        return getSigningKey;
    }

    public GetPassword getPassword(){
        return getPassword;
    }

    public void setGetPassword(GetPassword getPassword) {
        this.getPassword = getPassword;
    }
    public PostSigningKeys getPostSigningKeys() {
        return postSigningKeys;
    }

    public void setPostSigningKeys(PostSigningKeys postSigningKeys) {
        this.postSigningKeys = postSigningKeys;
    }
    public PutPublicKeys getPutPublicKeys() {
        return putPublicKeys;
    }

    public void setPutPublicKeys(PutPublicKeys putPublicKeys) {
        this.putPublicKeys = putPublicKeys;
    }

    public GetPublicKey getGetPublicKey() {
        return getPublicKey;
    }

    public void setGetPublicKey(GetPublicKey getPublicKey) {
        this.getPublicKey = getPublicKey;
    }

    public PostPublicKey getPostPublicKey() {
        return postPublicKey;
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

    public PutApplication getPutApplication()
    {
        return (putApplication == null) ? putApplication = new PutApplication() : putApplication;
    }

    public void setMerchantManagementSigningKeyId(String merchantManagementSigningKeyId) {
        this.merchantManagementSigningKeyId = merchantManagementSigningKeyId;
    }

    public String getMerchantManagementSigningKeyId() {
        return merchantManagementSigningKeyId;
    }

    public void setMerchantManagementSigningKey(String merchantManagementSigningKey) {
        this.merchantManagementSigningKey = merchantManagementSigningKey;
    }

    public String getMerchantManagementSigningKey() {
        return merchantManagementSigningKey;
    }

    public OneClickMerchantOnboarding getOneClickMerchantOnboarding() {
        return (oneClickMerchantOnboarding == null) ? oneClickMerchantOnboarding = new OneClickMerchantOnboarding() : oneClickMerchantOnboarding;
    }
}
