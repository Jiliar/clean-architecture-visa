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
@DynamoDBTable(tableName = "balance_accounts")
public class BalanceAccountDynamoDBTable {

    @DynamoDBHashKey(attributeName = "account")
    @DynamoDBIndexHashKey(attributeName = "account", globalSecondaryIndexName = "account-index")
    private String account;

    @DynamoDBAttribute(attributeName = "founds")
    private double founds;

    @DynamoDBAttribute(attributeName = "transaction")
    private String transaction;

    @DynamoDBAttribute(attributeName = "currency")
    private String currency;

    @DynamoDBRangeKey(attributeName = "date")
    @DynamoDBIndexHashKey(attributeName = "date", globalSecondaryIndexName = "date-index")
    private String update_date;

}
