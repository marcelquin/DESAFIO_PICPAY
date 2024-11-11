package App.Infra.UseCase.Cliente;

import App.Domain.ClienteResponse;
import App.Infra.Gateway.ClienteGateway;
import App.Infra.Persistence.Enum.TIPOCADASTRO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

public class UseCaseCLientePost {

    private final ClienteGateway clienteGateway;

    public UseCaseCLientePost(ClienteGateway clienteGateway) {
        this.clienteGateway = clienteGateway;
    }


    public ResponseEntity<ClienteResponse> NovoCLiente(@RequestParam String nome,
                                                       @RequestParam String cpjCnpj,
                                                       @RequestParam String email,
                                                       @RequestParam String senha,
                                                       @RequestParam TIPOCADASTRO tipoCadastro)
    { return clienteGateway.NovoCLiente(nome, cpjCnpj, email, senha, tipoCadastro);}

}
