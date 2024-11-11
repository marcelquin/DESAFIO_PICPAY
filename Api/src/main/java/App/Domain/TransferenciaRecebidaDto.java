package App.Domain;

import App.Infra.Persistence.Enum.STATUSTRANSFERENCIA;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record TransferenciaRecebidaDto(
        String peyer,
        String emailPeyer,
        String peyee,
        String emailPeyee,
        String codigo,
        String valor,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime dataTransferencia,
        STATUSTRANSFERENCIA statusTransferencia
) {
}
