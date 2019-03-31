package alejandro.lajusticia.mastermind.game.infrastructure.repository.db.impl;

import alejandro.lajusticia.mastermind.game.domain.model.Attempt;
import alejandro.lajusticia.mastermind.game.domain.model.Game;
import alejandro.lajusticia.mastermind.game.domain.model.exception.ModelException;
import alejandro.lajusticia.mastermind.game.infrastructure.repository.GameRepository;
import alejandro.lajusticia.mastermind.game.infrastructure.repository.db.entity.AttemptEntity;
import alejandro.lajusticia.mastermind.game.infrastructure.repository.db.entity.GameEntity;
import alejandro.lajusticia.mastermind.game.infrastructure.repository.exception.GameUnavailableException;
import alejandro.lajusticia.mastermind.game.infrastructure.repository.db.jpa.AttemptJpaRepository;
import alejandro.lajusticia.mastermind.game.infrastructure.repository.db.jpa.GameJpaRepository;
import alejandro.lajusticia.mastermind.game.infrastructure.repository.db.mapper.AttemptEntityDomainMapper;
import alejandro.lajusticia.mastermind.game.infrastructure.repository.db.mapper.GameEntityDomainMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@Slf4j
public class GameRepositoryDBImpl implements GameRepository {

    private final GameJpaRepository gameJpaRepository;
    private final AttemptJpaRepository attemptJpaRepository;

    public GameRepositoryDBImpl(
            final GameJpaRepository gameJpaRepository,
            final AttemptJpaRepository attemptJpaRepository)
    {
        this.gameJpaRepository = gameJpaRepository;
        this.attemptJpaRepository = attemptJpaRepository;
    }

    @Override
    public void saveGame(Game game) {
        gameJpaRepository.save(GameEntityDomainMapper.domainToEntity(game));
    }

    @Override
    public Optional<Game> findGameById(String id) throws GameUnavailableException {
        try {
            return Optional.of(GameEntityDomainMapper.entityToDomain(gameJpaRepository.getOne(id)));
        } catch (EntityNotFoundException entityNotFoundException) {
            return Optional.empty();
        } catch (ModelException e) {
            log.error("Error at recover the game with id: " + id + " from the database: ", e);
            throw new GameUnavailableException();
        }
    }

    @Override
    public Optional<Game> findGameByIdAndLock(String id) throws GameUnavailableException {
        Optional<GameEntity> entity = gameJpaRepository.findById(id);
        if (entity.isPresent()) {
            try {
                return Optional.of(GameEntityDomainMapper.entityToDomain(entity.get()));
            } catch (ModelException e) {
                log.error("Error at recover the game with id: " + id + " from the database: ", e);
                throw new GameUnavailableException();
            }
        }

        return Optional.empty();
    }

    @Override
    public void saveAttemptToGame(Attempt attempt, Game game) {
        AttemptEntity entity = AttemptEntityDomainMapper.domainToEntity(attempt);
        entity.setGame(GameEntityDomainMapper.domainToEntity(game));
        attemptJpaRepository.save(entity);
    }

}
