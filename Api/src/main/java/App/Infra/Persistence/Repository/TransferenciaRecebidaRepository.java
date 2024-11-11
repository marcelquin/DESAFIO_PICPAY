package App.Infra.Persistence.Repository;

import App.Infra.Persistence.Entity.TransferenciaEnviadaEntity;
import App.Infra.Persistence.Entity.TransferenciaRecebidaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransferenciaRecebidaRepository extends JpaRepository<TransferenciaRecebidaEntity,Long> {

    Optional<TransferenciaRecebidaEntity> findBycodigo(String codigo);
}
