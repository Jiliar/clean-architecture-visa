package co.com.visa.transfer.infraestructure.entrypoints.http.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransferResponse {

    private InputResponse input;
    private OutputResponse output;
}
