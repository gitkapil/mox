package managers;

import apiHelpers.*;

public class ApiManager {
    AccessTokenForMerchants accessToken;
    PaymentRequest paymentRequest;
    PaymentStatus paymentStatus;
    Refunds refunds;
    OpenIdConfigForPEAK openIdConfig;
    Transaction transaction;
    GetApplication getApplication;
    PutApplication putApplication;
    GetSigningKey getSigningKey;
    PostSigningKeys postSigningKeys;
    PutSigningKeys putSigningKeys;
    GetPlatform getPlatform;
    PostPlatform postPlatform;
    PutPlatform putPlatform;
    PostPassword postPasswordCreateClientPassword;
    GetPassword getPassword;
    OneClickMerchantOnboarding oneClickMerchantOnboarding;
    String merchantManagementSigningKeyId;
    String merchantManagementSigningKey;

    public GetPlatform getGetPlatform() {
        return getPlatform;
    }

    public void setGetPlatform(GetPlatform getPlatform) {
        this.getPlatform = getPlatform;
    }

    public PostPlatform getPostPlatform() {
        return postPlatform;
    }

    public void setPostPlatform(PostPlatform postPlatform) {
        this.postPlatform = postPlatform;
    }

    public PutPlatform getPutPlatform() {
        return putPlatform;
    }

    public void setPutPlatform(PutPlatform putPlatform) {
        this.putPlatform = putPlatform;
    }


    public ApiManager() {
        accessToken= new AccessTokenForMerchants();
        paymentRequest= new PaymentRequest();
        paymentStatus= new PaymentStatus();
        refunds= new Refunds();
        openIdConfig= new OpenIdConfigForPEAK();
        transaction = new Transaction();
        getApplication = new GetApplication();
        putApplication = new PutApplication();
        postSigningKeys = new PostSigningKeys();
        getSigningKey = new GetSigningKey();
        putSigningKeys = new PutSigningKeys();
        postPasswordCreateClientPassword = new PostPassword();
        getPassword = new GetPassword();
        getPlatform = new GetPlatform();
        postPlatform = new PostPlatform();
        putPlatform = new PutPlatform();
        oneClickMerchantOnboarding = new OneClickMerchantOnboarding();

    }

    public PostPassword getPostPasswordCreateClientPassword() {
        return postPasswordCreateClientPassword;
    }

    public PutSigningKeys getPutSigningKeys() {
        return putSigningKeys;
    }

    public GetSigningKey getGetSigningKey() {
        return getSigningKey;
    }

    public GetPassword getGetPassword(){
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
    public PostPlatform postPlatform() {
        return (postPlatform == null) ? postPlatform = new PostPlatform() : postPlatform;
    }

}

