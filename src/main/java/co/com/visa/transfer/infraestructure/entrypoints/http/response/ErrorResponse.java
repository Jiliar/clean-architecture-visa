package co.com.visa.transfer.infraestructure.entrypoints.http.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import co.com.visa.transfer.helpers.EnvVariables;

@Data
@NoArgsConstructor
public class ErrorResponse {

    private String error_code;
    private String errors;

    public ResponseEntity getErrorAPI(String error_code, String errors){

        ErrorResponse body = new ErrorResponse();
        EnvVariables env = new EnvVariables();
        HttpHeaders headers = new HttpHeaders();
        headers.add(env.CONTENT_TYPE_NAME, env.CONTENT_TYPE_VALUE);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        body.setError_code(error_code);
        body.setErrors(errors);
        ResponseEntity response = new ResponseEntity<>(this.standardFormat(body), headers, status);

        return response;
    }

    public String standardFormat(ErrorResponse body) {
        String json =  "'output':{" +
                "'error_code':'" + body.getError_code() + "', " +
                "'errors': ['" + body.getErrors() + "']" +
                "}";
        return json.replace("'","\"");
    }


}