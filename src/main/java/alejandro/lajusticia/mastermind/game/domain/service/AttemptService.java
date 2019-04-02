package alejandro.lajusticia.mastermind.game.domain.service;

import alejandro.lajusticia.mastermind.game.domain.model.Attempt;
import alejandro.lajusticia.mastermind.game.domain.model.GuessBall;
import alejandro.lajusticia.mastermind.game.domain.model.exception.EmptyInputException;
import alejandro.lajusticia.mastermind.game.domain.model.exception.ModelException;
import alejandro.lajusticia.mastermind.game.domain.model.exception.NullFeedbackException;

import java.util.List;

public interface AttemptService {

    /**
     * Create an attempt given the secret of game and the input of attempt
     *
     * @param secret not null list with the secret GuessBalls
     * @param attemptInput not null list with the input of attempt GuessBalls
     * @return the Attempt generated from the secret and the imput
     * @throws EmptyInputException if the input is empty
     * @throws NullFeedbackException if the feedback is null
     */
    Attempt createAttemptFromSecretAndAttemptInput(List<GuessBall> secret, List<GuessBall> attemptInput)
            throws EmptyInputException, NullFeedbackException;

}
