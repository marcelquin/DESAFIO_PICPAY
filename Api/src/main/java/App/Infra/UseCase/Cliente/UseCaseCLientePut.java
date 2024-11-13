package App.Infra.UseCase.Cliente;

import App.Domain.ClienteResponse;
import App.Infra.Gateway.ClienteGateway;
import App.Infra.Persistence.Enum.TIPOCADASTRO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

public class UseCaseCLientePut {

    private final ClienteGateway clienteGateway;


    public UseCaseCLientePut(ClienteGateway clienteGateway) {
        this.clienteGateway = clienteGateway;
    }

    public ResponseEntity<ClienteResponse> EditarCLiente(@RequestParam Long idCliente,
                                                         @RequestParam String nome,
                                                         @RequestParam String cpjCnpj,
                                                         @RequestParam String email,
                                                         @RequestParam TIPOCADASTRO tipoCadastro)
    {return clienteGateway.EditarCLiente(idCliente, nome, cpjCnpj, email, tipoCadastro);}
}
