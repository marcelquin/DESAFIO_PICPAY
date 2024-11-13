package App.Infra.Gateway;

import App.Domain.Response;
import App.Domain.TransferenciaResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

public interface TransferenciaGateway {

    public ResponseEntity<TransferenciaResponse> novaTransferencia(@RequestParam Long payer,
                                                                   @RequestParam Long senha,
                                                                   @RequestParam Long payee,
                                                                   @RequestParam Double valor);

    public ResponseEntity<Response> SaqueValor(@RequestParam Long idPayer,
                                               @RequestParam Long senha,
                                               @RequestParam Double valor);


    public ResponseEntity<Response> DepositoValor(@RequestParam Long idPayer,
                                                  @RequestParam Long senha,
                                                  @RequestParam Double valor);
}
