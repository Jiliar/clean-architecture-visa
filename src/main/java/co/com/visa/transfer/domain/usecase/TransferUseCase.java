package co.com.visa.transfer.domain.usecase;

import co.com.visa.transfer.domain.model.Input;
import co.com.visa.transfer.domain.model.Transfer;
import co.com.visa.transfer.domain.model.gateways.TransferRepository;
import co.com.visa.transfer.infraestructure.entrypoints.http.request.TransferRequest;
import co.com.visa.transfer.infraestructure.entrypoints.http.response.InputResponse;
import co.com.visa.transfer.infraestructure.entrypoints.http.response.OutputResponse;
import co.com.visa.transfer.infraestructure.entrypoints.http.response.TransferResponse;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class TransferUseCase {

    private final TransferRepository repository;

    public boolean saveTransfer(TransferResponse transfer, String usuario) throws IOException {
        Map<String, TransferResponse> data = new HashMap();
        data.put(usuario, transfer);
        Optional wrapper = Optional.of(data);
        Optional answer = repository.doAction(wrapper, "SAVE");
        Transfer response = (Transfer) answer.get();
        boolean flag = response != null?true:false;
        return flag;
    }

    public boolean validateCantTransfer(String usuario, String date) throws IOException {
        String[] data = {usuario, date};
        Optional wrapper = Optional.of(data);
        Optional answer = repository.doAction(wrapper, "VALIDATE_CANT_TRANSFERS");
        return (boolean) answer.get();
    }

    public Map<String, String[]> validateFounds(String account, double ammout) throws IOException {
        Map<String, Double> data = new HashMap();
        data.put(account, ammout);
        Optional wrapper = Optional.of(data);
        Optional answer = repository.doAction(wrapper, "VALIDATE_FOUNDS");
        return (Map<String, String[]>) answer.get();
    }

    public TransferResponse generateTransfer(TransferRequest r) throws IOException {

        Transfer request = new Transfer();

        request.setInput(new Input(r.getAmount(),
                                r.getCurrency(),
                                r.getOrigin_account(),
                                r.getDestination_account(),
                                r.getDescription()));

        Optional transfer = Optional.of(request.getInput());
        Optional answer = repository.doAction(transfer, "GET_TAX_CAD");
        Transfer response = (Transfer) answer.get();

        InputResponse inputResponse = InputResponse.builder().amount(response.getInput().getAmount())
                                                    .currency(response.getInput().getCurrency())
                                                    .origin_account(response.getInput().getOrigin_account())
                                                    .destination_account(response.getInput().getDestination_account())
                                                    .description(response.getInput().getDescription()).build();

        OutputResponse outputResponse = OutputResponse.builder().id(response.getOutput().getId())
                                                    .tax_collected(response.getOutput().getTax_collected())
                                                    .CAD(response.getOutput().getCad()).build();
        TransferResponse transferResponse = TransferResponse.builder().input(inputResponse)
                                                                      .output(outputResponse).build();
        return transferResponse;
    }

}
