package alejandro.lajusticia.mastermind.game.domain.service;

import alejandro.lajusticia.mastermind.game.domain.model.Game;
import alejandro.lajusticia.mastermind.game.domain.model.GuessBall;
import alejandro.lajusticia.mastermind.game.domain.model.exception.*;
import alejandro.lajusticia.mastermind.game.domain.service.exception.GameNotFoundException;
import alejandro.lajusticia.mastermind.game.domain.service.exception.WrongAttemptLengthException;
import alejandro.lajusticia.mastermind.game.infrastructure.repository.exception.RepositoryException;

import java.util.List;

public interface GameService {

    /**
     * Create a new game
     *
     * @param maxAttempts number of max attempts of the game
     * @return the Game created
     * @throws ModelException if some data is wrong
     */
    Game createGame(int maxAttempts) throws ModelException;

    /**
     * Get a game from the id of game
     *
     * @param id of the game to obtain
     * @return the Game with the id
     * @throws GameNotFoundException if the game with the id is not in our system
     * @throws RepositoryException if the data in the repository is wrong
     */
    Game getGame(String id) throws GameNotFoundException, RepositoryException;

    /**
     * Add attempt to game from the input of the attempt
     *
     * @param id of the game to add the attempt
     * @param attemptInput input of the attempt with the list of GuessBalls
     * @return the Game with the attempt added
     * @throws WrongAttemptLengthException if the length of the attemptInput is wrong
     * @throws GameNotFoundException if the game with the id is not in our system
     * @throws RepositoryException if the data in the repository is wrong
     * @throws EmptyInputException if the input of attempt is empty
     * @throws NullFeedbackException if the feedback of attempt is null
     * @throws GameIsSolvedException if the game is solved
     * @throws GameIsOverException if the game is over
     */
    Game addAttemptToGameByGameIdAndAttemptInput(String id, List<GuessBall> attemptInput)
            throws WrongAttemptLengthException, GameNotFoundException, RepositoryException, EmptyInputException,
            NullFeedbackException, GameIsSolvedException, GameIsOverException;

}
