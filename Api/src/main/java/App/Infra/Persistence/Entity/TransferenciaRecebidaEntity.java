package App.Infra.Persistence.Entity;

import App.Infra.Persistence.Enum.STATUSTRANSFERENCIA;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "Transferencia_Recebida")
public class TransferenciaRecebidaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String payer;

    private String emailPayer;

    private String payee;

    private String emailPayee;

    @Column(unique = true)
    private String codigo;

    private Double valor;

    @Enumerated(EnumType.STRING)
    private STATUSTRANSFERENCIA statustransferencia;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime timeStamp;
}
