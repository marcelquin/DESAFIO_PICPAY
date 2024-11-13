package App.Api;

import App.Bussness.TransferenciaService;
import App.Domain.Response;
import App.Domain.TransferenciaResponse;
import App.Infra.UseCase.Transferencia.UseCaseTransferenciaPost;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("transferencia")
@Tag(name = "Transferencia",
        description = "Manipula dados referente a entidade"
)
public class TransferenciaController {

    private final UseCaseTransferenciaPost caseTransferenciaPost;
    private final TransferenciaService service;

    public TransferenciaController(UseCaseTransferenciaPost caseTransferenciaPost, TransferenciaService service) {
        this.caseTransferenciaPost = caseTransferenciaPost;
        this.service = service;
    }

    @Operation(summary = "Executa Transação entre usuarios", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso"),
            @ApiResponse(responseCode = "422", description = "Dados de requisição inválida"),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos"),
            @ApiResponse(responseCode = "500", description = "Ops algoo deu errado"),
    })
    @PostMapping("/novaTransferencia")
    public ResponseEntity<TransferenciaResponse> novaTransferencia(@RequestParam Long payer,
                                                                   @RequestParam String senha,
                                                                   @RequestParam Long payee,
                                                                   @RequestParam Double valor)
    {return caseTransferenciaPost.novaTransferencia(payer, senha, payee, valor);}

    @Operation(summary = "Executa Transação", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso"),
            @ApiResponse(responseCode = "422", description = "Dados de requisição inválida"),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos"),
            @ApiResponse(responseCode = "500", description = "Ops algoo deu errado"),
    })
    @PutMapping("SaqueValor")
    public ResponseEntity<Response> SaqueValor(@RequestParam Long idPayer,
                                               @RequestParam String senha,
                                               @RequestParam Double valor)
    { return caseTransferenciaPost.SaqueValor(idPayer, senha, valor);}

    @Operation(summary = "Executa Transação", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso"),
            @ApiResponse(responseCode = "422", description = "Dados de requisição inválida"),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos"),
            @ApiResponse(responseCode = "500", description = "Ops algoo deu errado"),
    })
    @PutMapping("DepositoValor")
    public ResponseEntity<Response> DepositoValor(@RequestParam Long idPayer,
                                                  @RequestParam String senha,
                                                  @RequestParam Double valor)
    { return caseTransferenciaPost.DepositoValor(idPayer, senha, valor);}

}
