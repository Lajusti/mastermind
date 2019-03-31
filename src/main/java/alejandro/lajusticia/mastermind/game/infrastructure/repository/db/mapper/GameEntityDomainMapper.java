package alejandro.lajusticia.mastermind.game.infrastructure.repository.db.mapper;

import alejandro.lajusticia.mastermind.game.domain.model.Game;
import alejandro.lajusticia.mastermind.game.domain.model.exception.ModelException;
import alejandro.lajusticia.mastermind.game.infrastructure.repository.db.entity.AttemptEntity;
import alejandro.lajusticia.mastermind.game.infrastructure.repository.db.entity.GameEntity;

import java.util.stream.Collectors;

import static alejandro.lajusticia.mastermind.game.domain.enumeration.utils.ColorConverter.convertGuessBallsToString;
import static alejandro.lajusticia.mastermind.game.domain.enumeration.utils.ColorConverter.convertStringToGuessBalls;

public class GameEntityDomainMapper {

    public static Game entityToDomain(GameEntity entity) throws ModelException {
        Game domainObject = new Game(
                entity.getId(),
                convertStringToGuessBalls(entity.getSecret()),
                entity.getMaxAttempts()
        );

        for (AttemptEntity attemptEntity : entity.getAttempts()) {
            domainObject.addAttempt(AttemptEntityDomainMapper.entityToDomain(attemptEntity));
        }

        return domainObject;
    }

    public static GameEntity domainToEntity(Game domainObject) {
        GameEntity entity = new GameEntity();
        entity.setId(domainObject.getUuid());
        entity.setSecret(convertGuessBallsToString(domainObject.getSecret()));
        entity.setMaxAttempts(domainObject.getMaxAttempts());
        entity.setAttempts(
                domainObject.getAttempts().stream()
                        .map(AttemptEntityDomainMapper::domainToEntity)
                        .collect(Collectors.toList())
        );
        entity.getAttempts().forEach(attempt -> attempt.setGame(entity));
        return entity;
    }

    private GameEntityDomainMapper() {

    }

}
