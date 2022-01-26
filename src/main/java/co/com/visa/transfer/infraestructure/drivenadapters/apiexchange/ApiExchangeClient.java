package co.com.visa.transfer.infraestructure.drivenadapters.apiexchange;

import co.com.visa.transfer.helpers.EnvVariables;
import com.google.gson.Gson;
import lombok.Data;
import org.apache.http.Header;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.HttpEntity;

import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;

@Data
@RestController
public class ApiExchangeClient {

    private final static Logger LOGGER = Logger.getLogger(EnvVariables.GEN_PACKAGE);

    public TransferAPITemplate getData(String urlBase) throws IOException {

        TransferAPITemplate result = null;
        HttpGet request = new HttpGet(urlBase);
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {
            this.requestInformation(response);
            Header[] header_id = response.getHeaders(EnvVariables.ID_HEADER);
            Header[] headers_date = response.getHeaders(EnvVariables.DATE);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String jsonString = EntityUtils.toString(entity);
                Gson g = new Gson();
                result = g.fromJson(jsonString, TransferAPITemplate.class);;
                result.setId(header_id[0].getValue());
                result.setDate(headers_date[0].getValue());
            }
        }

        return result;
    }

    public void requestInformation(CloseableHttpResponse response) {
        LOGGER.log(Level.INFO, EnvVariables.TITLE_LOG);
        String answer = EnvVariables.ANSWER_REQUEST+response.getStatusLine().toString();
        LOGGER.log(Level.INFO, answer);
        LOGGER.log(Level.INFO, EnvVariables.TRANSACT_LOG);
    }

}