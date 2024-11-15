package App.Infra.Persistence.Entity;

import App.Infra.Persistence.Enum.TIPOCADASTRO;
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
@Table(name = "Cliente")
public class ClienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeCompleto;

    @JoinColumn(unique = true)
    private String email;

    @JoinColumn(unique = true)
    private String documento;

    @Enumerated(EnumType.STRING)
    private TIPOCADASTRO tipocadastro;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contaEntity_id", referencedColumnName = "id")
    private ContaEntity contaEntity;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime timeStamp;

}
