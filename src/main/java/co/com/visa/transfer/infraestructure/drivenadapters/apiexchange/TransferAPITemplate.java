package co.com.visa.transfer.infraestructure.drivenadapters.apiexchange;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransferAPITemplate implements Serializable {

    private String id;
    private String success;
    private String timestamp;
    private String base;
    private String date;
    private Map<String,Double> rates;

}
