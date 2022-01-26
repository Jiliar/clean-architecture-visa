package co.com.visa.transfer.infraestructure.drivenadapters.dynamodb;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
@DynamoDBTable(tableName = "transfers")
public class TransferDynamoDBTable {

    @DynamoDBHashKey(attributeName = "id")
    private String id;

    @DynamoDBAttribute(attributeName = "input")
    @DynamoDBTypeConverted(converter = InputDynamoDBItemTraslator.class)
    private InputDynamoDBItem input;

    @DynamoDBAttribute(attributeName = "output")
    @DynamoDBTypeConverted(converter = OutputDynamoDBItemTraslator.class)
    private OutputDynamoDBItem output;

    @DynamoDBRangeKey(attributeName = "date")
    @DynamoDBIndexHashKey(attributeName = "date", globalSecondaryIndexName = "date-index")
    private String date;

    @DynamoDBAttribute(attributeName = "usuario")
    @DynamoDBIndexHashKey(attributeName = "usuario", globalSecondaryIndexName = "usuario-index")
    private String usuario;

}
