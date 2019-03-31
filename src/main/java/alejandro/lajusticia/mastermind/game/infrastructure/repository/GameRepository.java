package alejandro.lajusticia.mastermind.game.infrastructure.repository;

import alejandro.lajusticia.mastermind.game.domain.model.Attempt;
import alejandro.lajusticia.mastermind.game.domain.model.Game;
import alejandro.lajusticia.mastermind.game.infrastructure.repository.exception.GameUnavailableException;

import java.util.Optional;

public interface GameRepository {

    /**
     * Save the game in the persistence layer
     *
     * @param game to save
     */
    void saveGame(Game game);

    /**
     * Obtain the game with the id of the param from the persistence layer
     *
     * @param id the id of the game to get
     * @return the game obtained if exists
     * @throws GameUnavailableException the data in the persistence layer is wrong and can not be obtained
     */
    Optional<Game> findGameById(String id) throws GameUnavailableException;

    /**
     * Obtain the game with the id of the param from the persistence layer and lock this game until the transactions
     * is committed or set as rollback
     *
     * @param id the id of the game to get
     * @return the game obtained if exists
     * @throws GameUnavailableException the data in the persistence layer is wrong and can not be obtained
     */
    Optional<Game> findGameByIdAndLock(String id) throws GameUnavailableException;

    /**
     * Save the attempt of the game in the persistence layer
     *
     * @param attempt to save
     * @param game of the attempt to save
     */
    void saveAttemptToGame(Attempt attempt, Game game);

}
