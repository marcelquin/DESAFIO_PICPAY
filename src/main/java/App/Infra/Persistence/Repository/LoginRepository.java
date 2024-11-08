package App.Infra.Persistence.Repository;

import App.Infra.Persistence.Entity.LoginEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<LoginEntity,Long> {

    Optional<LoginEntity> findBylogin(String login);
    Optional<LoginEntity> findBypassword(String password);
}
