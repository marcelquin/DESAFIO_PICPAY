package App.Infra.Persistence.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "Conta")
public class ContaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long agencia;

    @JoinColumn(unique = true)
    private Long conta;

    private Double saldo;

    private Long senhaTransacao;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "transferenciaEntity_id", referencedColumnName = "id")
    private TransferenciaEntity transferencia;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime timeStamp;
}
