package App.Domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record Response(
        String payer,
        String emailpayer,
        Double valor,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime dataTransferencia
) {
}
