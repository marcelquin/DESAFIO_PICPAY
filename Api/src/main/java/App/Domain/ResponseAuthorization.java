package App.Domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ResponseAuthorization(
        String payer,
        String emailpayer,
        String payee,
        String emailpayee,
        String codigo,
        Double valor,
        Boolean authorization
) {
}
