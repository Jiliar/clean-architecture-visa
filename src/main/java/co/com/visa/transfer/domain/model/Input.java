package co.com.visa.transfer.domain.model;

public class Input {

    private double amount;
    private String currency;
    private String origin_account;
    private String destination_account;
    private String description;

    public Input(){}

    public Input(double amount, String currency, String origin_account, String destination_account, String description) {
        this.amount = amount;
        this.currency = currency;
        this.origin_account = origin_account;
        this.destination_account = destination_account;
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getOrigin_account() {
        return origin_account;
    }

    public void setOrigin_account(String origin_account) {
        this.origin_account = origin_account;
    }

    public String getDestination_account() {
        return destination_account;
    }

    public void setDestination_account(String destination_account) {
        this.destination_account = destination_account;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Input{" +
                "amount=" + amount +
                ", currency='" + currency + '\'' +
                ", origin_account='" + origin_account + '\'' +
                ", destination_account='" + destination_account + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
