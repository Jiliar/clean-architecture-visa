package co.com.visa.transfer.domain.model;

public class Output {

    private String id;
    private double tax_collected;
    private double cad;

    public Output() {}

    public Output(String id, double tax_collected, double cad) {
        this.id = id;
        this.tax_collected = tax_collected;
        this.cad = cad;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getTax_collected() {
        return tax_collected;
    }

    public void setTax_collected(double tax_collected) {
        this.tax_collected = tax_collected;
    }

    public double getCad() {
        return cad;
    }

    public void setCad(double cad) {
        this.cad = cad;
    }

    @Override
    public String toString() {
        return "Output{" +
                "id='" + id + '\'' +
                ", tax_collected=" + tax_collected +
                ", cad=" + cad +
                '}';
    }
}
