package App.Infra.UseCase.Transferencia;

import App.Domain.Response;
import App.Domain.TransferenciaResponse;
import App.Infra.Gateway.TransferenciaGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

public class UseCaseTransferenciaPost {

    private final TransferenciaGateway transferenciaGateway;

    public UseCaseTransferenciaPost(TransferenciaGateway transferenciaGateway) {
        this.transferenciaGateway = transferenciaGateway;
    }

    public ResponseEntity<TransferenciaResponse> novaTransferencia(@RequestParam Long payer,
                                                                   @RequestParam String senha,
                                                                   @RequestParam Long payee,
                                                                   @RequestParam Double valor)
    {return transferenciaGateway.novaTransferencia(payer,senha, payee, valor);}

    public ResponseEntity<Response> SaqueValor(@RequestParam Long idPayer,
                                               @RequestParam String senha,
                                               @RequestParam Double valor)
    { return transferenciaGateway.SaqueValor(idPayer, senha, valor);}

    public ResponseEntity<Response> DepositoValor(@RequestParam Long idPayer,
                                                  @RequestParam String senha,
                                                  @RequestParam Double valor)
    {return transferenciaGateway.DepositoValor(idPayer, senha, valor);}

}
