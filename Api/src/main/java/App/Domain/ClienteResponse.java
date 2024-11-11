package App.Domain;

import App.Infra.Persistence.Enum.TIPOCADASTRO;

import java.util.List;

public record ClienteResponse(
        Long id,
        String nome,
        String documento,
        String email,
        String saldo,
        TIPOCADASTRO tipocadastro,

        List<TransferenciaEnviadaDto> transferenciasEnviadas,
        List<TransferenciaRecebidaDto> transferenciasRecebidas
) {
}
