package App.Infra.Gateway;

import App.Domain.ClienteResponse;
import App.Infra.Persistence.Enum.TIPOCADASTRO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ClienteGateway {

    public ResponseEntity<List<ClienteResponse>> ListarCLientes();
    public ResponseEntity<ClienteResponse> BuscarClientePorId(@RequestParam Long idCliente);
    public ResponseEntity<ClienteResponse> NovoCLiente(@RequestParam String nome,
                                                       @RequestParam String cpjCnpj,
                                                       @RequestParam String email,
                                                       @RequestParam String senha,
                                                       @RequestParam TIPOCADASTRO tipoCadastro);
    public ResponseEntity<ClienteResponse> EditarCLiente(@RequestParam Long idCliente,
                                                         @RequestParam String nome,
                                                         @RequestParam String cpjCnpj,
                                                         @RequestParam String email,
                                                         @RequestParam String senha,
                                                         @RequestParam TIPOCADASTRO tipoCadastro);
    public ResponseEntity<ClienteResponse> DeletarCliente(@RequestParam Long idCliente);

}
