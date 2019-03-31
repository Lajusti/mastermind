package alejandro.lajusticia.mastermind.game.infrastructure.repository.db.mapper;

import alejandro.lajusticia.mastermind.game.domain.model.Attempt;
import alejandro.lajusticia.mastermind.game.domain.model.exception.EmptyInputException;
import alejandro.lajusticia.mastermind.game.domain.model.exception.NullFeedbackException;
import alejandro.lajusticia.mastermind.game.infrastructure.repository.db.entity.AttemptEntity;

import static alejandro.lajusticia.mastermind.game.domain.enumeration.utils.ColorConverter.*;

public class AttemptEntityDomainMapper {

    public static Attempt entityToDomain(AttemptEntity entity) throws EmptyInputException, NullFeedbackException {
        return new Attempt(
                convertStringToGuessBalls(entity.getInput()),
                convertStringToFeedbackBalls(entity.getFeedback())
        );
    }

    public static AttemptEntity domainToEntity(Attempt domainObject) {
        AttemptEntity entity = new AttemptEntity();
        entity.setInput(convertGuessBallsToString(domainObject.getInput()));
        entity.setFeedback(convertFeedbackBallsToString(domainObject.getFeedback()));
        return entity;
    }

    private AttemptEntityDomainMapper() {

    }

}
