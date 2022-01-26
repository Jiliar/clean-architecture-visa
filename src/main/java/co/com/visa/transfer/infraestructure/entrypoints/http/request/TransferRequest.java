package co.com.visa.transfer.infraestructure.entrypoints.http.request;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Validated
@ToString
public class TransferRequest {

    @NotNull
    @JsonProperty("amount")
    private double amount;

    @NotNull
    @JsonProperty("currency")
    private String currency;

    @NotNull
    @JsonProperty("origin_account")
    private String origin_account;

    @NotNull
    @JsonProperty("destination_account")
    private String destination_account;

    @NotNull
    @JsonProperty("description")
    private String description;

}
