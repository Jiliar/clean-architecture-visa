package co.com.visa.transfer.infraestructure.drivenadapters.service;

import co.com.visa.transfer.domain.model.Input;
import co.com.visa.transfer.domain.model.Output;
import co.com.visa.transfer.domain.model.Transfer;
import co.com.visa.transfer.domain.model.gateways.TransferRepository;
import co.com.visa.transfer.helpers.EnvVariables;
import co.com.visa.transfer.infraestructure.drivenadapters.apiexchange.ApiExchangeClient;
import co.com.visa.transfer.infraestructure.drivenadapters.apiexchange.TransferAPITemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;


@Service
@Slf4j
public class TransferImplApiExchange implements TransferRepository {

    private final static Logger LOGGER = Logger.getLogger(EnvVariables.GEN_PACKAGE);
    EnvVariables env = new EnvVariables();

    @Override
    public Optional doAction(Optional i, String action) throws IOException {
        Input input = (Input) i.get();
        Output output = null;
        switch(action){
            case "GET_TAX_CAD":
                    ApiExchangeClient api= new ApiExchangeClient();
                    String urlBase = env.getUrl().replace(env.THREE_POINTS, env.getApikey());
                    TransferAPITemplate entity = api.getData(urlBase);
                    double currency_value = this.searchCurrencyValue(entity.getRates(), input.getCurrency());
                    double cad_value = this.searchCurrencyValue(entity.getRates(), env.CAD_NAME);
                    double equivalent_usd_eur =  input.getAmount() / currency_value;
                    double tax = 0.0;
                    if(input.getAmount() >= 100){
                        tax = env.TAX_PERCENTAGE_5;
                    }else if(input.getAmount() < 100){
                        tax = env.TAX_PERCENTAGE_2;
                    }
                    double tax_collected = equivalent_usd_eur * tax;
                    BigDecimal bd1 = new BigDecimal(tax_collected);
                    tax_collected = bd1.setScale(2, RoundingMode.HALF_UP).doubleValue();
                    double tax_converted = cad_value * tax_collected;
                    this.debugProcess(input.getAmount(), input.getCurrency(), currency_value,
                                    equivalent_usd_eur, tax_converted, tax);
                    output = new Output();
                    output.setId(entity.getId());
                    output.setTax_collected(tax_collected);
                    output.setCad(tax_converted);
                break;
        }

        return Optional.of(new Transfer(input, output));
    }

    public double searchCurrencyValue(Map map, String search_data){
        Iterator it = map.keySet().iterator();
        double answer = 0.0;
        while(it.hasNext()){
            String key = (String) it.next();
            if(key.equals(search_data)){
               answer = (Double) map.get(key);
            }
        }
        return answer;
    }

    public void debugProcess(double a, String c, double cv, double e, double t, double tax){
        String d = ZonedDateTime.now().toString();
        LOGGER.log(Level.INFO, "******** START PROCESS DEBUG ********");
        LOGGER.log(Level.INFO, "Date: "+d);
        LOGGER.log(Level.INFO, "Value to convert USD to EUR :"+a);
        LOGGER.log(Level.INFO, "1 "+c+" equals "+cv+" EUR");
        LOGGER.log(Level.INFO, "USD Total to EUROS :"+e);
        LOGGER.log(Level.INFO, "CAD Tax collected ("+tax*100+"%): "+t);
        LOGGER.log(Level.INFO, "******** END PROCESS DEBUG ********");
    }

}