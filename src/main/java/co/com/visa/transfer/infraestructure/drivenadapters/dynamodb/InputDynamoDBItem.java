package co.com.visa.transfer.infraestructure.drivenadapters.dynamodb;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class InputDynamoDBItem {

    private double amount;
    private String currency;
    private String origin_account;
    private String destination_account;
    private String description;

}
