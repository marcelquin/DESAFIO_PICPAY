package App.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record TransferenciaResponse(
        String payer,
        String emailpayer,
        String payee,
        String emailpayee,
        String codigo,
        Double valor
) {
}
