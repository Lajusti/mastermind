package alejandro.lajusticia.mastermind.game.infrastructure.repository.db.jpa;

import alejandro.lajusticia.mastermind.game.infrastructure.repository.db.entity.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface GameJpaRepository extends JpaRepository<GameEntity, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<GameEntity> findById(String id);

}
