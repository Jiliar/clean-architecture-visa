package co.com.visa.transfer.domain.model;

public class Transfer {

    private Input input;
    private Output output;

    public Transfer() {
    }

    public Transfer(Input input, Output output) {
        this.input = input;
        this.output = output;
    }

    public Input getInput() {
        return input;
    }

    public void setInput(Input input) {
        this.input = input;
    }

    public Output getOutput() {
        return output;
    }

    public void setOutput(Output output) {
        this.output = output;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "input=" + input +
                ", output=" + output +
                '}';
    }
}
