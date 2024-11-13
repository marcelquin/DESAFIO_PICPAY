package App.Infra.Persistence.Repository;

import App.Infra.Persistence.Entity.ClienteEntity;
import App.Infra.Persistence.Entity.ContaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContaRepository extends JpaRepository<ContaEntity,Long> {

    boolean existsByagencia(Long agencia);
    boolean existsByconta(Long conta);

}
