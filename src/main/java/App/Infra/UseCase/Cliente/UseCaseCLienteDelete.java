package App.Infra.UseCase.Cliente;

import App.Domain.ClienteResponse;
import App.Infra.Gateway.ClienteGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

public class UseCaseCLienteDelete {

    private final ClienteGateway clienteGateway;


    public UseCaseCLienteDelete(ClienteGateway clienteGateway) {
        this.clienteGateway = clienteGateway;
    }

    public ResponseEntity<ClienteResponse> DeletarCliente(@RequestParam Long idCliente)
    { return clienteGateway.DeletarCliente(idCliente);}
}
