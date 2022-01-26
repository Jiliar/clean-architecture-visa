package co.com.visa.transfer.infraestructure.drivenadapters.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OutputDynamoDBItemTraslator implements DynamoDBTypeConverter<String, OutputDynamoDBItem> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convert(OutputDynamoDBItem object) {
        try{
            return mapper.writeValueAsString(object);
        }catch(JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public OutputDynamoDBItem unconvert(String object) {
        try{
            return mapper.readValue(object, OutputDynamoDBItem.class);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
