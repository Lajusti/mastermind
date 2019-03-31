package alejandro.lajusticia.mastermind.game.domain.service;

import alejandro.lajusticia.mastermind.game.domain.model.Attempt;
import alejandro.lajusticia.mastermind.game.domain.model.GuessBall;
import alejandro.lajusticia.mastermind.game.domain.model.exception.ModelException;

import java.util.List;

public interface AttemptService {

    /**
     * Create an attempt given the secret of game and the input of attempt
     *
     * @param secret not null list with the secret GuessBalls
     * @param attemptInput not null list with the input of attempt GuessBalls
     * @return the Attempt generated from the secret and the imput
     * @throws ModelException if some data is wrong
     */
    Attempt createAttemptFromSecretAndAttemptInput(List<GuessBall> secret, List<GuessBall> attemptInput)
            throws ModelException;

}
