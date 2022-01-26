package co.com.visa.transfer.domain.model.gateways;

import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Optional;

@Repository
public interface TransferRepository {

    public Optional doAction(Optional transfer, String action) throws IOException;

}
