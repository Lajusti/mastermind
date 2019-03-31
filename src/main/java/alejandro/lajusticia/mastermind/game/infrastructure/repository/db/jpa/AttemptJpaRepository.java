package alejandro.lajusticia.mastermind.game.infrastructure.repository.db.jpa;

import alejandro.lajusticia.mastermind.game.infrastructure.repository.db.entity.AttemptEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttemptJpaRepository extends JpaRepository<AttemptEntity, Long> {

}
