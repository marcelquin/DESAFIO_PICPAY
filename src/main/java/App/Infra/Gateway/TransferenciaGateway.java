package App.Infra.Gateway;

import App.Domain.TransferenciaResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

public interface TransferenciaGateway {

    public ResponseEntity<TransferenciaResponse> novaTransferencia(@RequestParam Long payer,
                                                                   @RequestParam Long payee,
                                                                   @RequestParam Double valor);
}
