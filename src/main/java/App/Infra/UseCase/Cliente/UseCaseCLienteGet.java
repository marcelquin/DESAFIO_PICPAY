package App.Infra.UseCase.Cliente;

import App.Domain.ClienteResponse;
import App.Infra.Gateway.ClienteGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public class UseCaseCLienteGet {

    private final ClienteGateway clienteGateway;

    public UseCaseCLienteGet(ClienteGateway clienteGateway) {
        this.clienteGateway = clienteGateway;
    }


    public ResponseEntity<List<ClienteResponse>> ListarCLientes()
    { return clienteGateway.ListarCLientes();}
    public ResponseEntity<ClienteResponse> BuscarClientePorId(@RequestParam Long idCliente)
    { return clienteGateway.BuscarClientePorId(idCliente);}
}
