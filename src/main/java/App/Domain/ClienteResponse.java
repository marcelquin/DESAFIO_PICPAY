package App.Domain;

import App.Infra.Persistence.Enum.TIPOCADASTRO;

public record ClienteResponse(

        String nome,
        String documento,
        String email,
        String saldo,
        TIPOCADASTRO tipocadastro
) {
}
