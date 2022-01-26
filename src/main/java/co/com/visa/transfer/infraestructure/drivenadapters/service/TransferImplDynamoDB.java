package co.com.visa.transfer.infraestructure.drivenadapters.service;
import co.com.visa.transfer.domain.model.Input;
import co.com.visa.transfer.domain.model.Output;
import co.com.visa.transfer.domain.model.Transfer;
import co.com.visa.transfer.domain.model.gateways.TransferRepository;
import co.com.visa.transfer.helpers.EnvVariables;
import co.com.visa.transfer.infraestructure.drivenadapters.dynamodb.BalanceAccountDynamoDBTable;
import co.com.visa.transfer.infraestructure.drivenadapters.dynamodb.TransferDynamoDBTable;
import co.com.visa.transfer.infraestructure.drivenadapters.dynamodb.InputDynamoDBItem;
import co.com.visa.transfer.infraestructure.drivenadapters.dynamodb.OutputDynamoDBItem;
import co.com.visa.transfer.infraestructure.entrypoints.http.response.InputResponse;
import co.com.visa.transfer.infraestructure.entrypoints.http.response.OutputResponse;
import co.com.visa.transfer.infraestructure.entrypoints.http.response.TransferResponse;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import org.springframework.context.annotation.Configuration;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Configuration
public class TransferImplDynamoDB implements TransferRepository {

    EnvVariables env = new EnvVariables();
    private final static Logger LOGGER = Logger.getLogger(EnvVariables.GEN_PACKAGE);



    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(env.DATE_FORMAT);
    String date = simpleDateFormat.format(new Date());

    AWSCredentialsProvider creds = new AWSStaticCredentialsProvider(
            new BasicAWSCredentials(env.AWS_ACCESSKEY, env.AWS_SECRETACCESSKEY)
    );
    AmazonDynamoDB ddbClient = AmazonDynamoDBClientBuilder.standard()
            .withCredentials(creds)
            .withRegion(env.AWS_REGION).build();

    public Optional doAction(Optional i, String action){

        Optional res = null;

        switch(action){

            case "SAVE":
                    try {
                        String usuario = null;
                        TransferResponse t = null;
                        Map<String, TransferResponse> info = (Map<String, TransferResponse>) i.get();
                        for (Map.Entry<String, TransferResponse> entry : info.entrySet()) {
                            usuario = entry.getKey();
                            t = entry.getValue();
                        }
                        InputResponse in = t.getInput();
                        OutputResponse ou = t.getOutput();

                        //saving Transfer
                        DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);
                        saveTransfer(mapper, in, ou, usuario, date);

                        TransferDynamoDBTable data = get(mapper, ou.getId(), date);

                        Input input = new Input();
                        input.setAmount(data.getInput().getAmount());
                        input.setCurrency(data.getInput().getCurrency());
                        input.setDescription(data.getInput().getDescription());
                        input.setDestination_account(data.getInput().getDestination_account());
                        input.setOrigin_account(data.getInput().getOrigin_account());

                        Output output = new Output();
                        output.setId(data.getOutput().getId());
                        output.setCad(data.getOutput().getCAD());
                        output.setTax_collected(data.getOutput().getTax_collected());

                        //saving Origin Account Balance
                        double founds_ori = getRecentFoundByAccount(mapper,in.getOrigin_account());
                        double new_found_ori = founds_ori - (in.getAmount() + ou.getTax_collected());
                        saveBalance(mapper, data.getInput().getOrigin_account(), data.getInput().getCurrency(),
                                            new_found_ori, data.getOutput().getId(), data.getDate());

                        //saving Destination Account Balance
                        double founds_des = getRecentFoundByAccount(mapper,in.getDestination_account());
                        double new_found_des = founds_des + in.getAmount();
                        saveBalance(mapper, data.getInput().getDestination_account(), data.getInput().getCurrency(),
                                            new_found_des, data.getOutput().getId(), data.getDate());

                        res = Optional.of(new Transfer(input,output));
                    }catch(Exception e){
                        LOGGER.log(Level.SEVERE, e.getMessage());
                    }
                break;

            case "VALIDATE_CANT_TRANSFERS":
                try {
                    String[] info = (String[]) i.get();
                    DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);
                    List<TransferDynamoDBTable> list = this.queryByUsuario(mapper, info[0]);
                    Iterator<TransferDynamoDBTable> it= list.iterator();
                    int count = 0;
                    boolean flag = false;
                    while(it.hasNext()) {
                        if(it.next().getDate().equals(info[1])){
                            count++;
                        }
                        if(count >= 3){
                            flag = true;
                            break;
                        }
                    }
                    res = Optional.of(flag);
                }catch(Exception e){
                    LOGGER.log(Level.SEVERE, e.getMessage());
                }
                break;

            case "VALIDATE_FOUNDS":

                long max = 0;
                BalanceAccountDynamoDBTable max_data = null;
                String flag = "false";
                String msg = "";
                try {
                    String account = null;
                    Double ammount = null;
                    Map<String, Double> info = (Map<String, Double>) i.get();
                    for (Map.Entry<String, Double> entry : info.entrySet()) {
                        account = entry.getKey();
                        ammount = entry.getValue();
                    }
                    DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);
                    List<BalanceAccountDynamoDBTable> list = this.queryFoundsByAccount(mapper, account);
                    Iterator<BalanceAccountDynamoDBTable> it= list.iterator();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(env.DATE_FORMAT);
                    if(!list.isEmpty()) {
                        while (it.hasNext()) {
                            BalanceAccountDynamoDBTable data = it.next();
                            LocalDateTime localDateTime = LocalDateTime.from(formatter.parse(data.getUpdate_date()));
                            Timestamp timestamp = Timestamp.valueOf(localDateTime);
                            if (timestamp.getTime() > max) {
                                max_data = data;
                            }
                        }
                    }else{
                        flag = String.valueOf(false);
                        msg = env.ERROR_004_2;
                    }

                    if(max_data != null){
                        if(max_data.getFounds() > ammount){
                            flag = String.valueOf(true);
                            msg = env.OK_VALIDATION;
                        }else{
                            flag = String.valueOf(false);
                            msg = env.ERROR_004_1;
                        }
                    }else{
                        flag = String.valueOf(false);
                        msg = env.ERROR_004_3;
                    }
                    String[] answer = {flag, msg};
                    Map<String, String[]> mapAnswer = new HashMap<String, String[]>();
                    mapAnswer.put(env.VALIDATION, answer);
                    res = Optional.of(mapAnswer);
                }catch(Exception e){
                    LOGGER.log(Level.SEVERE, e.getMessage());
                }
                break;
        }

        return res;
    }

    public void saveBalance(DynamoDBMapper mapper, String account, String currency, double founds, String transaction,
                            String date){
        BalanceAccountDynamoDBTable table = new BalanceAccountDynamoDBTable();
        table.setAccount(account);
        table.setCurrency(currency);
        table.setFounds(founds);
        table.setTransaction(transaction);
        table.setUpdate_date(date);
        mapper.save(table);
    }

    public void saveTransfer(DynamoDBMapper mapper, InputResponse in, OutputResponse ou, String usuario, String date){

        InputDynamoDBItem inp = new InputDynamoDBItem();

        inp.setAmount(in.getAmount());
        inp.setCurrency(in.getCurrency());
        inp.setOrigin_account(in.getOrigin_account());
        inp.setDestination_account(in.getDestination_account());
        inp.setDescription(in.getDescription());

        OutputDynamoDBItem out = new OutputDynamoDBItem();
        out.setId(ou.getId());
        out.setTax_collected(ou.getTax_collected());
        out.setCAD(ou.getCAD());

        TransferDynamoDBTable table = new TransferDynamoDBTable();
        table.setId(ou.getId());
        table.setInput(inp);
        table.setOutput(out);
        table.setDate(date);
        table.setUsuario(usuario);

        mapper.save(table);
    }

    public TransferDynamoDBTable get(DynamoDBMapper mapper, String id, String date){
        TransferDynamoDBTable table = new TransferDynamoDBTable();
        table.setId(id);
        table.setDate(date);
        TransferDynamoDBTable result = mapper.load(table);
        return result;
    }

    public List<TransferDynamoDBTable> queryByUsuario(DynamoDBMapper mapper, String usuario){
        TransferDynamoDBTable t = new TransferDynamoDBTable();
        t.setUsuario(usuario);

        DynamoDBQueryExpression<TransferDynamoDBTable> queryExpression =
                new DynamoDBQueryExpression<TransferDynamoDBTable>()
                        .withHashKeyValues(t)
                        .withIndexName(env.USUARIO_INDEX)
                        .withConsistentRead(false);
        List<TransferDynamoDBTable> result = mapper.query(TransferDynamoDBTable.class, queryExpression);
        return result;
    }

    public List<BalanceAccountDynamoDBTable> queryFoundsByAccount(DynamoDBMapper mapper, String account){
        BalanceAccountDynamoDBTable t = new BalanceAccountDynamoDBTable();
        t.setAccount(account);

        DynamoDBQueryExpression<BalanceAccountDynamoDBTable> queryExpression =
                new DynamoDBQueryExpression<BalanceAccountDynamoDBTable>()
                        .withHashKeyValues(t)
                        .withIndexName(env.ACCOUNT_INDEX)
                        .withConsistentRead(false);
        List<BalanceAccountDynamoDBTable> result = mapper.query(BalanceAccountDynamoDBTable.class, queryExpression);
        return result;
    }

    public double getRecentFoundByAccount(DynamoDBMapper mapper, String account){

        BalanceAccountDynamoDBTable max_data = null;
        long max = 0;
        double founds = 0;
        List<BalanceAccountDynamoDBTable> list = this.queryFoundsByAccount(mapper, account);
        Iterator<BalanceAccountDynamoDBTable> it= list.iterator();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(env.DATE_FORMAT);
        if(!list.isEmpty()) {
            while (it.hasNext()) {
                BalanceAccountDynamoDBTable data = it.next();
                LocalDateTime localDateTime = LocalDateTime.from(formatter.parse(data.getUpdate_date()));
                Timestamp timestamp = Timestamp.valueOf(localDateTime);
                if (timestamp.getTime() > max) {
                    max_data = data;
                }
            }
        }
        if(max_data != null){
            founds = max_data.getFounds();
        }else{
            founds = 0.0;
        }

        return founds;
    }

    public void delete(DynamoDBMapper mapper, String id, String fecha){
        TransferDynamoDBTable table = new TransferDynamoDBTable();
        table.setId(id);
        table.setDate(fecha);

        TransferDynamoDBTable result = mapper.load(table);
        mapper.delete(result);
    }

}
