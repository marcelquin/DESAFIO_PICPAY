package App.Infra.UseCase.Transferencia;

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
                                                                   @RequestParam Long payee,
                                                                   @RequestParam Double valor)
    {return transferenciaGateway.novaTransferencia(payer, payee, valor);}

}
