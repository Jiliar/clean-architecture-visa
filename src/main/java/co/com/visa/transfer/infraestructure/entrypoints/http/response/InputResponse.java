package co.com.visa.transfer.infraestructure.entrypoints.http.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InputResponse {

    private double amount;
    private String currency;
    private String origin_account;
    private String destination_account;
    private String description;

}
