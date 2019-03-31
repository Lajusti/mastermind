package alejandro.lajusticia.mastermind.game.infrastructure.db.impl;

import alejandro.lajusticia.mastermind.game.domain.enumeration.FeedbackColor;
import alejandro.lajusticia.mastermind.game.domain.enumeration.GuessColor;
import alejandro.lajusticia.mastermind.game.domain.model.Attempt;
import alejandro.lajusticia.mastermind.game.domain.model.FeedbackBall;
import alejandro.lajusticia.mastermind.game.domain.model.Game;
import alejandro.lajusticia.mastermind.game.domain.model.GuessBall;
import alejandro.lajusticia.mastermind.game.domain.model.exception.*;
import alejandro.lajusticia.mastermind.game.infrastructure.repository.db.entity.AttemptEntity;
import alejandro.lajusticia.mastermind.game.infrastructure.repository.db.entity.GameEntity;
import alejandro.lajusticia.mastermind.game.infrastructure.repository.db.exception.GameUnavailableException;
import alejandro.lajusticia.mastermind.game.infrastructure.repository.db.impl.GameRepositoryDBImpl;
import alejandro.lajusticia.mastermind.game.infrastructure.repository.db.jpa.AttemptJpaRepository;
import alejandro.lajusticia.mastermind.game.infrastructure.repository.db.jpa.GameJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import javax.persistence.EntityNotFoundException;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class GameRepositoryDBImplTest {

    private final String EXPECTED_ID = "EXPECTED_ID";
    private final int EXPECTED_MAX_ATTEMPTS = 15;
    private final String EXPECTED_SECRET = GuessColor.BLUE.name() + ":" + GuessColor.ORANGE.name() +
            ":" + GuessColor.GREEN.name() + ":" + GuessColor.RED.name();

    private final List<GuessBall> EXPECTED_SECRET_AS_LIST = Arrays.asList(
            new GuessBall(GuessColor.BLUE),
            new GuessBall(GuessColor.ORANGE),
            new GuessBall(GuessColor.GREEN),
            new GuessBall(GuessColor.RED)
    );

    private final String EXPECTED_INPUT = GuessColor.BLUE.name() + ":" + GuessColor.ORANGE.name() +
            ":" + GuessColor.YELLOW.name() + ":" + GuessColor.GREEN.name();

    private final List<GuessBall> EXPECTED_INPUT_AS_LIST = Arrays.asList(
            new GuessBall(GuessColor.BLUE),
            new GuessBall(GuessColor.ORANGE),
            new GuessBall(GuessColor.YELLOW),
            new GuessBall(GuessColor.GREEN)
    );

    private final String EXPECTED_FEEDBACK = FeedbackColor.BLACK.name() + ":" + FeedbackColor.BLACK + ":" +
            FeedbackColor.WHITE.name();

    private final List<FeedbackBall> EXPECTED_FEEDBACK_AS_LIST = Arrays.asList(
            new FeedbackBall(FeedbackColor.BLACK),
            new FeedbackBall(FeedbackColor.BLACK),
            new FeedbackBall(FeedbackColor.WHITE)
    );

    @Mock
    private GameJpaRepository gameJpaRepository;

    @Mock
    private AttemptJpaRepository attemptJpaRepository;

    @InjectMocks
    private GameRepositoryDBImpl gameRepositoryDB;

    @Test
    void saveGame_OK() throws EmptySecretException, WrongNumberOfAttemptsException, EmptyUUIDException {
        Game game = new Game(EXPECTED_ID, EXPECTED_SECRET_AS_LIST, EXPECTED_MAX_ATTEMPTS);

        gameRepositoryDB.saveGame(game);

        verify(gameJpaRepository, times(1))
                .save(buildExpectedEntity());
    }

    @Test
    void saveGame_OK_withAttempts() throws ModelException {
        Game game = new Game(EXPECTED_ID, EXPECTED_SECRET_AS_LIST, EXPECTED_MAX_ATTEMPTS);
        Attempt attempt1 = new Attempt(EXPECTED_INPUT_AS_LIST, EXPECTED_FEEDBACK_AS_LIST);
        Attempt attempt2 = new Attempt(EXPECTED_INPUT_AS_LIST, EXPECTED_FEEDBACK_AS_LIST);
        game.addAttempt(attempt1);
        game.addAttempt(attempt2);

        gameRepositoryDB.saveGame(game);

        verify(gameJpaRepository, times(1))
                .save(buildExpectedEntityWithAttempts(2));
    }

    @Test
    void findGameById_OK() throws GameUnavailableException {
        when(gameJpaRepository.getOne(EXPECTED_ID))
                .thenReturn(buildExpectedEntity());

        Optional<Game> optionalGame = gameRepositoryDB.findGameById(EXPECTED_ID);

        verify(gameJpaRepository, times(1))
                .getOne(EXPECTED_ID);

        assertNotNull(optionalGame);
        assertTrue(optionalGame.isPresent());

        Game game = optionalGame.get();

        assertEquals(EXPECTED_ID, game.getUuid());
        assertEquals(EXPECTED_MAX_ATTEMPTS, game.getMaxAttempts());
        assertEquals(EXPECTED_SECRET_AS_LIST, game.getSecret());
        assertTrue(game.getAttempts().isEmpty());
    }

    @Test
    void findGameById_OK_withAttempts() throws GameUnavailableException {
        when(gameJpaRepository.getOne(EXPECTED_ID))
                .thenReturn(buildExpectedEntityWithAttempts(2));

        Optional<Game> optionalGame = gameRepositoryDB.findGameById(EXPECTED_ID);

        verify(gameJpaRepository, times(1))
                .getOne(EXPECTED_ID);

        assertNotNull(optionalGame);
        assertTrue(optionalGame.isPresent());

        Game game = optionalGame.get();

        assertEquals(EXPECTED_ID, game.getUuid());
        assertEquals(EXPECTED_MAX_ATTEMPTS, game.getMaxAttempts());
        assertEquals(EXPECTED_SECRET_AS_LIST, game.getSecret());
        assertFalse(game.getAttempts().isEmpty());
        assertEquals(2, game.getAttempts().size());

        assertEquals(EXPECTED_INPUT_AS_LIST, game.getAttempts().get(0).getInput());
        assertEquals(EXPECTED_INPUT_AS_LIST, game.getAttempts().get(1).getInput());

        validateExpectedFeedbackList(game.getAttempts().get(0).getFeedback());
        validateExpectedFeedbackList(game.getAttempts().get(1).getFeedback());
    }

    @Test
    void findGameById_OK_empty() throws GameUnavailableException {
        when(gameJpaRepository.getOne(EXPECTED_ID))
                .thenThrow(new EntityNotFoundException());

        Optional<Game> optionalGame = gameRepositoryDB.findGameById(EXPECTED_ID);

        verify(gameJpaRepository, times(1))
                .getOne(EXPECTED_ID);

        assertNotNull(optionalGame);
        assertFalse(optionalGame.isPresent());
    }

    @Test
    void findGameById_KO_GameUnavailableException() {
        when(gameJpaRepository.getOne(EXPECTED_ID))
                .thenReturn(buildExpectedEntityWithAttempts(EXPECTED_MAX_ATTEMPTS + 1));

        Assertions.assertThrows(
                GameUnavailableException.class,
                () -> gameRepositoryDB.findGameById(EXPECTED_ID)
        );

        verify(gameJpaRepository, times(1))
                .getOne(EXPECTED_ID);
    }

    @Test
    void findGameByIdAndLock_OK() throws GameUnavailableException {
        when(gameJpaRepository.findById(EXPECTED_ID))
                .thenReturn(Optional.of(buildExpectedEntity()));

        Optional<Game> optionalGame = gameRepositoryDB.findGameByIdAndLock(EXPECTED_ID);

        verify(gameJpaRepository, times(1))
                .findById(EXPECTED_ID);

        assertNotNull(optionalGame);
        assertTrue(optionalGame.isPresent());

        Game game = optionalGame.get();

        assertEquals(EXPECTED_ID, game.getUuid());
        assertEquals(EXPECTED_MAX_ATTEMPTS, game.getMaxAttempts());
        assertEquals(EXPECTED_SECRET_AS_LIST, game.getSecret());
        assertTrue(game.getAttempts().isEmpty());
    }

    @Test
    void findGameByIdAndLock_OK_withAttempts() throws GameUnavailableException {
        when(gameJpaRepository.findById(EXPECTED_ID))
                .thenReturn(Optional.of(buildExpectedEntityWithAttempts(2)));

        Optional<Game> optionalGame = gameRepositoryDB.findGameByIdAndLock(EXPECTED_ID);

        verify(gameJpaRepository, times(1))
                .findById(EXPECTED_ID);

        assertNotNull(optionalGame);
        assertTrue(optionalGame.isPresent());

        Game game = optionalGame.get();

        assertEquals(EXPECTED_ID, game.getUuid());
        assertEquals(EXPECTED_MAX_ATTEMPTS, game.getMaxAttempts());
        assertEquals(EXPECTED_SECRET_AS_LIST, game.getSecret());
        assertFalse(game.getAttempts().isEmpty());
        assertEquals(2, game.getAttempts().size());

        assertEquals(EXPECTED_INPUT_AS_LIST, game.getAttempts().get(0).getInput());
        assertEquals(EXPECTED_INPUT_AS_LIST, game.getAttempts().get(1).getInput());

        validateExpectedFeedbackList(game.getAttempts().get(0).getFeedback());
        validateExpectedFeedbackList(game.getAttempts().get(1).getFeedback());
    }

    @Test
    void findGameByIdAndLock_OK_empty() throws GameUnavailableException {
        when(gameJpaRepository.findById(EXPECTED_ID))
                .thenReturn(Optional.empty());

        Optional<Game> optionalGame = gameRepositoryDB.findGameByIdAndLock(EXPECTED_ID);

        verify(gameJpaRepository, times(1))
                .findById(EXPECTED_ID);

        assertNotNull(optionalGame);
        assertFalse(optionalGame.isPresent());
    }

    @Test
    void findGameByIdAndLock_KO_GameUnavailableException() {
        when(gameJpaRepository.findById(EXPECTED_ID))
                .thenReturn(Optional.of(buildExpectedEntityWithAttempts(EXPECTED_MAX_ATTEMPTS + 1)));

        Assertions.assertThrows(
                GameUnavailableException.class,
                () -> gameRepositoryDB.findGameByIdAndLock(EXPECTED_ID)
        );

        verify(gameJpaRepository, times(1))
                .findById(EXPECTED_ID);
    }

    private final String EXPECTED_INPUT_NEW = GuessColor.BLUE.name() + ":" + GuessColor.ORANGE.name() +
            ":" + GuessColor.GREEN.name() + ":" + GuessColor.YELLOW.name();

    private final List<GuessBall> EXPECTED_INPUT_NEW_AS_LIST = Arrays.asList(
            new GuessBall(GuessColor.BLUE),
            new GuessBall(GuessColor.ORANGE),
            new GuessBall(GuessColor.GREEN),
            new GuessBall(GuessColor.YELLOW)
    );

    private final String EXPECTED_FEEDBACK_NEW = FeedbackColor.BLACK.name() + ":" + FeedbackColor.BLACK + ":" +
            FeedbackColor.BLACK.name();

    private final List<FeedbackBall> EXPECTED_FEEDBACK_NEW_AS_LIST = Arrays.asList(
            new FeedbackBall(FeedbackColor.BLACK),
            new FeedbackBall(FeedbackColor.BLACK),
            new FeedbackBall(FeedbackColor.BLACK)
    );

    @Test
    void saveAttemptToGame_OK() throws ModelException {
        Game game = new Game(EXPECTED_ID, EXPECTED_SECRET_AS_LIST, EXPECTED_MAX_ATTEMPTS);
        Attempt attempt = new Attempt(EXPECTED_INPUT_NEW_AS_LIST, EXPECTED_FEEDBACK_NEW_AS_LIST);

        gameRepositoryDB.saveAttemptToGame(attempt, game);

        verify(attemptJpaRepository, times(1))
                .save(buildEntity(0, EXPECTED_INPUT_NEW, EXPECTED_FEEDBACK_NEW));
    }

    private GameEntity buildExpectedEntity() {
        GameEntity entity = new GameEntity();
        entity.setId(EXPECTED_ID);
        entity.setSecret(EXPECTED_SECRET);
        entity.setMaxAttempts(EXPECTED_MAX_ATTEMPTS);
        return entity;
    }

    private GameEntity buildExpectedEntityWithAttempts(int attempts) {
        GameEntity entity = buildExpectedEntity();
        List<AttemptEntity> attemptEntities = new ArrayList<>();
        for (int i = 0; i < attempts; i++) {
            attemptEntities.add(buildEntity(i, EXPECTED_INPUT, EXPECTED_FEEDBACK));
        }
        entity.setAttempts(attemptEntities);

        return entity;
    }

    private AttemptEntity buildEntity(int id, String input, String feedback) {
        AttemptEntity attemptEntity = new AttemptEntity();
        attemptEntity.setId(id);
        attemptEntity.setInput(input);
        attemptEntity.setFeedback(feedback);
        return attemptEntity;
    }

    private void validateExpectedFeedbackList(List<FeedbackBall> feedback) {
        assertEquals(3, feedback.size());
        assertEquals(FeedbackColor.BLACK.name(), feedback.get(0).getColor());
        assertEquals(FeedbackColor.BLACK.name(), feedback.get(1).getColor());
        assertEquals(FeedbackColor.WHITE.name(), feedback.get(2).getColor());
    }

}
