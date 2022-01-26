package co.com.visa.transfer.helpers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
public class EnvVariables {

    //App Client Parameters
    private String url = "http://api.exchangeratesapi.io/v1/latest?access_key=***";
    private String apikey = "8d414edf4655df126194f91fdd95d342";

    public String getUrl() {return url;}
    public String getApikey() {
        return apikey;
    }

    //App Users Sec
    private String userPass = "9eac535c-j114-409e-b5b6-722db21aff3f";
    private String adminPass = "9eac535c-262c-409e-b5b6-722db21aff3f";

    public String getUserPass() {return userPass;}
    public String getAdminPass() {return adminPass;}

    //AWS DynamoDB Sec
    public static final String AWS_REGION = "us-east-1";
    public static final String AWS_ACCESSKEY = "AKIATLQMPAP55QIBMWMV";
    public static final String AWS_SECRETACCESSKEY = "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";
    public static final String USUARIO_INDEX ="usuario-index";
    public static final String ACCOUNT_INDEX ="account-index";
    public static final String DATE_INDEX ="date-index";


    //API Config
    public static final String GEN_PACKAGE = "co.com.visa.transfer";
    public static final String APP_URI = "/transfer";
    public static final String CONTENT_TYPE_NAME = "Content-Type";
    public static final String CONTENT_TYPE_VALUE = "application/json";
    public static final String ID_HEADER = "x-apilayer-transaction-id";
    public static final String DATE = "Date";
    public static final String AUTHORIZATION = "Authorization";

    //Errors and Exceptions
    public static final String ERROR_001 = "There was a problem trying to save the transaction on DB";
    public static final String ERROR_C0D_001 = "000453";
    public static final String ERROR_002 = "There was an error trying to generate the transaction";
    public static final String ERROR_C0D_002 = "000454";
    public static final String ERROR_003 = "The user exceed the limit number of transfer allowed by day (Max limit 3 per day)";
    public static final String ERROR_C0D_003 = "000455";
    public static final String ERROR_004_1 = "insufficient-funds";
    public static final String ERROR_004_2 = "User does not have transactions registered in the balance account";
    public static final String ERROR_004_3 = "Problems with user transactions. Please, contact with App Admin";
    public static final String ERROR_004_4 = "insufficient-funds";
    public static final String ERROR_C0D_004 = "000456";
    public static final String ERROR_005 = "the amount must be greater than zero";
    public static final String ERROR_C0D_005 = "000457";

    //Logger
    public static final String TITLE_LOG = "*** REQUEST TO datafixer.io ***";
    public static final String ANSWER_REQUEST = "Answer Request: ";
    public static final String TRANSACT_LOG = "***  Transaction Successful ***";

    //Security
    public static final String SEC_USER1 = "user";
    public static final String SEC_ROL1 = "USER";
    public static final String SEC_USER2 = "admin";
    public static final String SEC_ROL2 = "ADMIN";



    //Others
    public static final String CAD_NAME = "CAD";
    public static final String THREE_POINTS = "***";
    public static final double TAX_PERCENTAGE_2 = 0.02;
    public static final double TAX_PERCENTAGE_5 = 0.05;
    public static final String AUTH_BASIC = "Basic";
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String SEPARATOR_1 =":";
    public static final String VALIDATION = "validation";
    public static final String OK_VALIDATION ="Account has founds";



}


