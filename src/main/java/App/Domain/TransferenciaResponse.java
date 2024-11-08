package App.Domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record TransferenciaResponse(
        String depositante,
        String receptor,
        Double valor,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime dataTransferencia
) {
}
