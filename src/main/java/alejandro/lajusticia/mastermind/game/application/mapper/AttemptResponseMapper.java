package alejandro.lajusticia.mastermind.game.application.mapper;

import alejandro.lajusticia.mastermind.game.application.response.AttemptResponse;
import alejandro.lajusticia.mastermind.game.domain.model.Attempt;

public class AttemptResponseMapper {

    public static AttemptResponse domainToResponse(Attempt attempt) {
        return new AttemptResponse(attempt.getInput(), attempt.getFeedback());
    }

    private AttemptResponseMapper() {

    }
}
