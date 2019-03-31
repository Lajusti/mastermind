package alejandro.lajusticia.mastermind.game.infrastructure.repository;

import alejandro.lajusticia.mastermind.game.domain.model.Attempt;
import alejandro.lajusticia.mastermind.game.domain.model.Game;

import java.util.Optional;

public interface GameRepository {

    void saveGame(Game game);

    Optional<Game> findGameById(String id);

    Optional<Game> findGameByIdAndLock(String id);

    void saveAttemptToGame(Attempt attempt, Game game);

}
