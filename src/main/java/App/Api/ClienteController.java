package App.Api;

import App.Bussness.ClienteService;
import App.Domain.ClienteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cliente")
@Tag(name = "cliente",
        description = "Manipula dados referente a entidade"
)
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @Operation(summary = "Lista Registros da tabela", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso"),
            @ApiResponse(responseCode = "422", description = "Dados de requisição inválida"),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos"),
            @ApiResponse(responseCode = "500", description = "Ops algoo deu errado"),
    })
    @GetMapping("/listarCLientes")
    public ResponseEntity<List<ClienteResponse>> listarCLientes()
    { return clienteService.ListarCLientes();}

    @Operation(summary = "Busca Registro da tabela Por id", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso"),
            @ApiResponse(responseCode = "422", description = "Dados de requisição inválida"),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos"),
            @ApiResponse(responseCode = "500", description = "Ops algoo deu errado"),
    })
    @GetMapping("/BuscarClientePorId")
    public ResponseEntity<ClienteResponse> BuscarClientePorId(@RequestParam Long idCliente)
    {return clienteService.BuscarClientePorId(idCliente); }

    @Operation(summary = "Salva novo Registro na tabela", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso"),
            @ApiResponse(responseCode = "422", description = "Dados de requisição inválida"),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos"),
            @ApiResponse(responseCode = "500", description = "Ops algoo deu errado"),
    })
    @PostMapping("/NovoCLiente")
    public ResponseEntity<ClienteResponse> NovoCLiente(@RequestParam String nome,
                                                       @RequestParam String cpjCnpj,
                                                       @RequestParam String email,
                                                       @RequestParam String senha)
    { return clienteService.NovoCLiente(nome, cpjCnpj, email, senha);}

    @Operation(summary = "Edita Registro na tabela", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso"),
            @ApiResponse(responseCode = "422", description = "Dados de requisição inválida"),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos"),
            @ApiResponse(responseCode = "500", description = "Ops algoo deu errado"),
    })
    @PutMapping("/EditarCLiente")
    public ResponseEntity<ClienteResponse> EditarCLiente(@RequestParam Long idCliente,
                                                         @RequestParam String nome,
                                                         @RequestParam String cpjCnpj,
                                                         @RequestParam String email,
                                                         @RequestParam String senha)
    { return clienteService.EditarCLiente(idCliente, nome, cpjCnpj, email, senha); }




    @Operation(summary = "Deleta Registro na tabela", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso"),
            @ApiResponse(responseCode = "422", description = "Dados de requisição inválida"),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos"),
            @ApiResponse(responseCode = "500", description = "Ops algoo deu errado"),
    })
    @DeleteMapping("/DeletarCliente")
    public ResponseEntity<ClienteResponse> DeletarCliente(@RequestParam Long idCliente)
    { return clienteService.DeletarCliente(idCliente);}
}
