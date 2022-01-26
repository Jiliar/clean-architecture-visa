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
public class OutputDynamoDBItem {

    private String id;
    private double tax_collected;
    private double CAD;

}
