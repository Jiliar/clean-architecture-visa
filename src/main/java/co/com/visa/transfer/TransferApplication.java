package co.com.visa.transfer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;


@SpringBootApplication
public class TransferApplication {


	public static void main(String[] args) {
		SpringApplication.run(TransferApplication.class, args);
	}

}
