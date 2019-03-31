package alejandro.lajusticia.mastermind.game.domain.service.impl;

import alejandro.lajusticia.mastermind.game.domain.enumeration.FeedbackColor;
import alejandro.lajusticia.mastermind.game.domain.enumeration.GuessColor;
import alejandro.lajusticia.mastermind.game.domain.model.Attempt;
import alejandro.lajusticia.mastermind.game.domain.model.FeedbackBall;
import alejandro.lajusticia.mastermind.game.domain.model.Game;
import alejandro.lajusticia.mastermind.game.domain.model.GuessBall;
import alejandro.lajusticia.mastermind.game.domain.model.exception.*;
import alejandro.lajusticia.mastermind.game.domain.service.AttemptService;
import alejandro.lajusticia.mastermind.game.domain.service.GuessBallService;
import alejandro.lajusticia.mastermind.game.domain.service.exception.GameNotFoundException;
import alejandro.lajusticia.mastermind.game.domain.service.exception.WrongAttemptLengthException;
import alejandro.lajusticia.mastermind.game.infrastructure.repository.GameRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class GameServiceImplTest {

    private final List<GuessBall> EXPECTED_SECRET = Arrays.asList(
            new GuessBall(GuessColor.BLUE),
            new GuessBall(GuessColor.ORANGE),
            new GuessBall(GuessColor.GREEN),
            new GuessBall(GuessColor.RED)
    );

    private final int EXPECTED_MAX_ATTEMPTS = 10;

    @Mock
    private GameRepository repository;

    @Mock
    private AttemptService attemptService;

    @Mock
    private GuessBallService guessBallService;

    @InjectMocks
    private GameServiceImpl gameService;

    @Test
    void createGame_OK() throws ModelException {
        when(guessBallService.generateRandomGuessBall())
                .thenReturn(
                        EXPECTED_SECRET.get(0),
                        EXPECTED_SECRET.get(1),
                        EXPECTED_SECRET.get(2),
                        EXPECTED_SECRET.get(3)
                );

        Game game = gameService.createGame(EXPECTED_MAX_ATTEMPTS);

        verify(repository, times(1))
                .saveGame(any());

        verify(guessBallService, times(4))
                .generateRandomGuessBall();

        assertNotNull(game);
        assertNotNull(game.getUuid());
        assertNotEquals("", game.getUuid());
        assertEquals(EXPECTED_MAX_ATTEMPTS, game.getMaxAttempts());
        assertTrue(game.getAttempts().isEmpty());
        assertNotNull(game.getSecret());
        assertEquals(EXPECTED_SECRET, game.getSecret());

        Game game2 = gameService.createGame(EXPECTED_MAX_ATTEMPTS);

        assertNotEquals(game.getUuid(), game2.getUuid());
    }

    @Test
    void createGame_KO_ModelException() {
        Assertions.assertThrows(
                ModelException.class,
                () -> gameService.createGame(-1)
        );

        Assertions.assertThrows(
                ModelException.class,
                () -> gameService.createGame(0)
        );
    }

    @Test
    void getGame_OK()
            throws EmptySecretException, WrongNumberOfAttemptsException, EmptyUUIDException, GameNotFoundException
    {
        String id = "EXPECTED_ID";

        when(repository.findGameById(id))
                .thenReturn(Optional.of(new Game(id, EXPECTED_SECRET, EXPECTED_MAX_ATTEMPTS)));

        Game game = gameService.getGame(id);

        verify(repository, times(1))
                .findGameById(id);

        assertNotNull(game);
        assertEquals(id, game.getUuid());
        assertEquals(EXPECTED_SECRET, game.getSecret());
        assertEquals(EXPECTED_MAX_ATTEMPTS, game.getMaxAttempts());
        assertTrue(game.getAttempts().isEmpty());
    }

    @Test
    void getGame_KO() {
        String id = "EXPECTED_ID";

        when(repository.findGameById(id))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                GameNotFoundException.class,
                () -> gameService.getGame(id)
        );

        verify(repository, times(1))
                .findGameById(id);
    }

    @Test
    void addAttemptToGameByGameIdAndAttemptInput_OK()
            throws ModelException, WrongAttemptLengthException, GameNotFoundException {
        String id = "EXPECTED_ID";
        List<FeedbackBall> expectedFeedBack = Arrays.asList(
                new FeedbackBall(FeedbackColor.BLACK),
                new FeedbackBall(FeedbackColor.BLACK),
                new FeedbackBall(FeedbackColor.BLACK),
                new FeedbackBall(FeedbackColor.BLACK)
        );
        Game expectedGame = new Game(id, EXPECTED_SECRET, EXPECTED_MAX_ATTEMPTS);
        Attempt expectedAttempt = new Attempt(EXPECTED_SECRET, expectedFeedBack);

        when(repository.findGameByIdAndLock(id))
                .thenReturn(Optional.of(expectedGame));

        when(attemptService.createAttemptFromSecretAndAttemptInput(EXPECTED_SECRET, EXPECTED_SECRET))
                .thenReturn(expectedAttempt);

        Game game = gameService.addAttemptToGameByGameIdAndAttemptInput(id, EXPECTED_SECRET);

        verify(repository, times(1))
                .findGameByIdAndLock(id);

        verify(attemptService, times(1))
                .createAttemptFromSecretAndAttemptInput(EXPECTED_SECRET, EXPECTED_SECRET);

        verify(repository, times(1))
                .saveAttemptToGame(expectedAttempt, expectedGame);

        assertNotNull(game);
        assertEquals(1, game.getAttempts().size());
        assertEquals(EXPECTED_SECRET, game.getAttempts().get(0).getInput());
        assertEquals(expectedFeedBack, game.getAttempts().get(0).getFeedback());
    }

    @Test
    void addAttemptToGameByGameIdAndAttemptInput_KO_WrongAttemptLengthException() throws ModelException {
        String id = "EXPECTED_ID";
        List<GuessBall> wrongSecret = Arrays.asList(
                new GuessBall(GuessColor.BLUE),
                new GuessBall(GuessColor.ORANGE),
                new GuessBall(GuessColor.GREEN),
                new GuessBall(GuessColor.RED),
                new GuessBall(GuessColor.RED)
        );

        Assertions.assertThrows(
                WrongAttemptLengthException.class,
                () -> gameService.addAttemptToGameByGameIdAndAttemptInput(id, wrongSecret)
        );

        verify(repository, times(0))
                .findGameByIdAndLock(id);

        verify(attemptService, times(0))
                .createAttemptFromSecretAndAttemptInput(wrongSecret, wrongSecret);

        verify(repository, times(0))
                .saveAttemptToGame(any(), any());

        List<GuessBall> wrongSecret2 = Arrays.asList(
                new GuessBall(GuessColor.BLUE),
                new GuessBall(GuessColor.ORANGE),
                new GuessBall(GuessColor.GREEN)
        );

        Assertions.assertThrows(
                WrongAttemptLengthException.class,
                () -> gameService.addAttemptToGameByGameIdAndAttemptInput(id, wrongSecret2)
        );

        verify(repository, times(0))
                .findGameByIdAndLock(id);

        verify(attemptService, times(0))
                .createAttemptFromSecretAndAttemptInput(wrongSecret, wrongSecret);

        verify(repository, times(0))
                .saveAttemptToGame(any(), any());
    }

    @Test
    void addAttemptToGameByGameIdAndAttemptInput_KO_GameNotFoundException() throws ModelException
    {
        String id = "EXPECTED_ID";
        List<FeedbackBall> expectedFeedBack = Arrays.asList(
                new FeedbackBall(FeedbackColor.BLACK),
                new FeedbackBall(FeedbackColor.BLACK),
                new FeedbackBall(FeedbackColor.BLACK),
                new FeedbackBall(FeedbackColor.BLACK)
        );

        when(repository.findGameByIdAndLock(id))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                GameNotFoundException.class,
                () -> gameService.addAttemptToGameByGameIdAndAttemptInput(id, EXPECTED_SECRET)
        );

        verify(repository, times(1))
                .findGameByIdAndLock(id);

        verify(attemptService, times(0))
                .createAttemptFromSecretAndAttemptInput(EXPECTED_SECRET, EXPECTED_SECRET);

        verify(repository, times(0))
                .saveAttemptToGame(any(), any());
    }

}
