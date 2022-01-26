package co.com.visa.transfer.infraestructure.entrypoints;

import co.com.visa.transfer.domain.usecase.TransferUseCase;
import co.com.visa.transfer.helpers.EnvVariables;
import co.com.visa.transfer.infraestructure.drivenadapters.service.TransferImplApiExchange;
import co.com.visa.transfer.infraestructure.drivenadapters.service.TransferImplDynamoDB;
import co.com.visa.transfer.infraestructure.entrypoints.http.request.TransferRequest;
import co.com.visa.transfer.infraestructure.entrypoints.http.response.ErrorResponse;
import co.com.visa.transfer.infraestructure.entrypoints.http.response.TransferResponse;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.validation.Valid;
import java.io.IOException;

@RestController
public class TransferController{

    private final static Logger LOGGER = Logger.getLogger(EnvVariables.GEN_PACKAGE);

    private TransferUseCase transferUseCase;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(EnvVariables.DATE_FORMAT);
    String date = simpleDateFormat.format(new Date());

    @PostMapping(path = EnvVariables.APP_URI, produces = EnvVariables.CONTENT_TYPE_VALUE)
    public ResponseEntity sendMessage(@Valid @RequestBody TransferRequest request,
                                      @RequestHeader(EnvVariables.AUTHORIZATION) String auth) throws IOException {

        ResponseEntity answer = null;
        TransferResponse respuesta = null;
        String user = this.getUser(auth);
        if(request.getAmount() > 0) {
            transferUseCase = new TransferUseCase(new TransferImplDynamoDB());
            String[] val1;
            String msg1 = null;
            Map<String, String[]> validation1 = transferUseCase.validateFounds(request.getOrigin_account(), request.getAmount());
            val1 = validation1.get(EnvVariables.VALIDATION);
            if (Boolean.valueOf(val1[0])) {
                boolean validation2 = transferUseCase.validateCantTransfer(user, date);
                if (!validation2) {
                    transferUseCase = new TransferUseCase(new TransferImplApiExchange());
                    respuesta = transferUseCase.generateTransfer(request);
                    Gson gson = new Gson();
                    String transaction = gson.toJson(respuesta);
                    String output = gson.toJson(respuesta.getOutput());
                    if (respuesta != null) {
                        transferUseCase = new TransferUseCase(new TransferImplDynamoDB());
                        boolean flag = transferUseCase.saveTransfer(respuesta, user);
                        if (flag) {
                            answer = new ResponseEntity<String>(output, HttpStatus.OK);
                            LOGGER.log(Level.INFO, transaction);
                        } else {
                            answer = new ErrorResponse().getErrorAPI(EnvVariables.ERROR_C0D_001,
                                    EnvVariables.ERROR_001);
                            LOGGER.log(Level.SEVERE, EnvVariables.ERROR_001);
                        }
                    } else {
                        answer = new ErrorResponse().getErrorAPI(EnvVariables.ERROR_C0D_002,
                                EnvVariables.ERROR_002);
                        LOGGER.log(Level.SEVERE, EnvVariables.ERROR_002);
                    }
                } else {
                    answer = new ErrorResponse().getErrorAPI(EnvVariables.ERROR_C0D_003,
                            EnvVariables.ERROR_003);
                    LOGGER.log(Level.SEVERE, EnvVariables.ERROR_003);
                }
            } else {
                answer = new ErrorResponse().getErrorAPI(EnvVariables.ERROR_C0D_004, val1[1]);
                LOGGER.log(Level.SEVERE, val1[1]);
            }
        }else{
            answer = new ErrorResponse().getErrorAPI(EnvVariables.ERROR_C0D_005,
                    EnvVariables.ERROR_005);
            LOGGER.log(Level.SEVERE, EnvVariables.ERROR_005);
        }
        return answer;
    }

    public String getUser(String auth){
        String base64Credentials = auth.substring(EnvVariables.AUTH_BASIC.length()).trim();
        byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(credDecoded, StandardCharsets.UTF_8);
        final String[] values = credentials.split(EnvVariables.SEPARATOR_1, 2);
        return values[0];
    }
}