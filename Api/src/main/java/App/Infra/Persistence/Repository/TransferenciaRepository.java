package App.Infra.Persistence.Repository;

import App.Infra.Persistence.Entity.TransferenciaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransferenciaRepository extends JpaRepository<TransferenciaEntity,Long> {

}
