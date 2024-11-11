package App.Infra.Persistence.Repository;

import App.Infra.Persistence.Entity.TransferenciaEnviadaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransferenciaEnviadaRepository extends JpaRepository<TransferenciaEnviadaEntity,Long> {

    Optional<TransferenciaEnviadaEntity> findBycodigo(String codigo);
}
