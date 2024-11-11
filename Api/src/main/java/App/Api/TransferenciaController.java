package App.Api;

import App.Domain.TransferenciaResponse;
import App.Infra.UseCase.Transferencia.UseCaseTransferenciaPost;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("transferencia")
@Tag(name = "Transferencia",
        description = "Manipula dados referente a entidade"
)
public class TransferenciaController {

    private final UseCaseTransferenciaPost caseTransferenciaPost;

    public TransferenciaController(UseCaseTransferenciaPost caseTransferenciaPost) {
        this.caseTransferenciaPost = caseTransferenciaPost;
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
                                                                   @RequestParam Long payee,
                                                                   @RequestParam Double valor)
    {return caseTransferenciaPost.novaTransferencia(payer, payee, valor);}
}
