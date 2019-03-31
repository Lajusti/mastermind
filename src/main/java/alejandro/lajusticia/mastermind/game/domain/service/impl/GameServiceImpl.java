package alejandro.lajusticia.mastermind.game.domain.service.impl;

import alejandro.lajusticia.mastermind.game.domain.model.Attempt;
import alejandro.lajusticia.mastermind.game.domain.model.Game;
import alejandro.lajusticia.mastermind.game.domain.model.GuessBall;
import alejandro.lajusticia.mastermind.game.domain.model.exception.ModelException;
import alejandro.lajusticia.mastermind.game.domain.service.AttemptService;
import alejandro.lajusticia.mastermind.game.domain.service.GameService;
import alejandro.lajusticia.mastermind.game.domain.service.GuessBallService;
import alejandro.lajusticia.mastermind.game.domain.service.exception.GameNotFoundException;
import alejandro.lajusticia.mastermind.game.domain.service.exception.WrongAttemptLengthException;
import alejandro.lajusticia.mastermind.game.infrastructure.repository.GameRepository;
import alejandro.lajusticia.mastermind.game.infrastructure.repository.exception.RepositoryException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class GameServiceImpl implements GameService {

    private final int SECRET_LENGTH = 4;

    private final GameRepository repository;
    private final GuessBallService guessBallService;
    private final AttemptService attemptService;

    public GameServiceImpl(
            final GameRepository repository,
            final GuessBallService guessBallService,
            final AttemptService attemptService)
    {
        this.repository = repository;
        this.guessBallService = guessBallService;
        this.attemptService = attemptService;
    }

    @Override
    public Game createGame(int maxAttempts) throws ModelException {
        String uuid = generateIdForNewGame();
        List<GuessBall> secret = generateSecretForNewGame();

        Game newGame = new Game(uuid, secret, maxAttempts);
        repository.saveGame(newGame);

        return newGame;
    }

    @Override
    public Game getGame(String id) throws GameNotFoundException, RepositoryException {
        return repository.findGameById(id)
                .orElseThrow(() -> new GameNotFoundException(id));
    }

    @Override
    @Transactional
    public Game addAttemptToGameByGameIdAndAttemptInput(String id, List<GuessBall> attemptInput)
            throws ModelException, WrongAttemptLengthException, GameNotFoundException, RepositoryException {
        if (attemptInput.size() != SECRET_LENGTH) {
            throw new WrongAttemptLengthException(SECRET_LENGTH);
        }

        Game game = getGameAndLock(id);
        Attempt attempt = attemptService.createAttemptFromSecretAndAttemptInput(game.getSecret(), attemptInput);
        game.addAttempt(attempt);
        repository.saveAttemptToGame(attempt, game);

        return game;
    }

    private Game getGameAndLock(String id) throws GameNotFoundException, RepositoryException {
        return repository.findGameByIdAndLock(id)
                .orElseThrow(() -> new GameNotFoundException(id));
    }

    private String generateIdForNewGame() {
        return UUID.randomUUID().toString();
    }

    private List<GuessBall> generateSecretForNewGame() {
        List<GuessBall> secret = new ArrayList<>();
        for (int secretElementNumber = 0; secretElementNumber < SECRET_LENGTH; secretElementNumber++) {
            secret.add(guessBallService.generateRandomGuessBall());
        }
        return secret;
    }

}
