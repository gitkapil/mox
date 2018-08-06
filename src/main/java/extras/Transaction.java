package extras;

public class Transaction {
    private Double amount= null;
    private String currency= null;
    private String description= null;
    private String channel= null;
    private String invoiceId= null;
    private String merchantId= null;
    private Integer effectiveDuration= null;
    private String returnURL=null;

    public String getReturnURL() {
        return returnURL;
    }

    public void setReturnURL(String returnURL) {
        this.returnURL = returnURL;
    }

    public Double getAmount() {
        return amount;
    }


    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getEffectiveDuration() {
        return effectiveDuration;
    }

    public void setEffectiveDuration(Integer effectiveDuration) {
        this.effectiveDuration = effectiveDuration;
    }


}
