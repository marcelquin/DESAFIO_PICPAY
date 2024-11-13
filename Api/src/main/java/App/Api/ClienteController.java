package App.Api;

import App.Bussness.ClienteService;
import App.Domain.ClienteResponse;
import App.Infra.Persistence.Enum.TIPOCADASTRO;
import App.Infra.UseCase.Cliente.UseCaseCLienteDelete;
import App.Infra.UseCase.Cliente.UseCaseCLienteGet;
import App.Infra.UseCase.Cliente.UseCaseCLientePost;
import App.Infra.UseCase.Cliente.UseCaseCLientePut;
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

    private final UseCaseCLienteGet caseCLienteGet;
    private final UseCaseCLientePost caseCLientePost;
    private final UseCaseCLientePut caseCLientePut;
    private final UseCaseCLienteDelete caseCLienteDelete;

    public ClienteController(UseCaseCLienteGet caseCLienteGet, UseCaseCLientePost caseCLientePost, UseCaseCLientePut caseCLientePut, UseCaseCLienteDelete caseCLienteDelete) {
        this.caseCLienteGet = caseCLienteGet;
        this.caseCLientePost = caseCLientePost;
        this.caseCLientePut = caseCLientePut;
        this.caseCLienteDelete = caseCLienteDelete;
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
    { return caseCLienteGet.ListarCLientes();}

    @Operation(summary = "Busca Registro da tabela Por id", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso"),
            @ApiResponse(responseCode = "422", description = "Dados de requisição inválida"),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos"),
            @ApiResponse(responseCode = "500", description = "Ops algoo deu errado"),
    })
    @GetMapping("/BuscarClientePorId")
    public ResponseEntity<ClienteResponse> BuscarClientePorId(@RequestParam Long idCliente)
    {return caseCLienteGet.BuscarClientePorId(idCliente); }

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
                                                       @RequestParam String senha,
                                                       @RequestParam TIPOCADASTRO tipoCadastro)
    { return caseCLientePost.NovoCLiente(nome, cpjCnpj, email, senha, tipoCadastro);}

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
                                                         @RequestParam TIPOCADASTRO tipoCadastro)
    { return caseCLientePut.EditarCLiente(idCliente, nome, cpjCnpj, email, tipoCadastro); }




    @Operation(summary = "Deleta Registro na tabela", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso"),
            @ApiResponse(responseCode = "422", description = "Dados de requisição inválida"),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos"),
            @ApiResponse(responseCode = "500", description = "Ops algoo deu errado"),
    })
    @DeleteMapping("/DeletarCliente")
    public ResponseEntity<ClienteResponse> DeletarCliente(@RequestParam Long idCliente)
    { return caseCLienteDelete.DeletarCliente(idCliente);}
}
