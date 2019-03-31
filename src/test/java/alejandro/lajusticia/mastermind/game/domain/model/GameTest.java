package alejandro.lajusticia.mastermind.game.domain.model;

import alejandro.lajusticia.mastermind.game.domain.enumeration.GuessColor;
import alejandro.lajusticia.mastermind.game.domain.model.exception.*;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class GameTest {

    private final List<GuessBall> EXPECTED_SECRET = Arrays.asList(
            new GuessBall(GuessColor.BLUE),
            new GuessBall(GuessColor.ORANGE),
            new GuessBall(GuessColor.GREEN),
            new GuessBall(GuessColor.RED)
    );

    private final int EXPECTED_MAX_ATTEMPTS = 10;

    private final String EXPECTED_UUID = UUID.randomUUID().toString();

    @Mock
    private Attempt attempt;

    @Test
    void new_OK() throws WrongNumberOfAttemptsException, EmptySecretException, EmptyUUIDException {
        Game game = new Game(EXPECTED_UUID, EXPECTED_SECRET, EXPECTED_MAX_ATTEMPTS);

        assertNotNull(game);
        assertEquals(EXPECTED_UUID, game.getUuid());
        assertEquals(EXPECTED_SECRET, game.getSecret());
        assertEquals(EXPECTED_MAX_ATTEMPTS, game.getMaxAttempts());
        assertTrue(game.getAttempts().isEmpty());
        assertFalse(game.isEnded());
        assertFalse(game.isSolved());
    }

    @Test
    void newFromExistentGame_EmptyUUIDExceptionWithNull() {
        Assertions.assertThrows(
                EmptyUUIDException.class,
                () -> new Game(null, EXPECTED_SECRET, EXPECTED_MAX_ATTEMPTS)
        );

    }

    @Test
    void newFromExistentGame_EmptyUUIDExceptionWithEmpty() {
        Assertions.assertThrows(
                EmptyUUIDException.class,
                () -> new Game("", EXPECTED_SECRET, EXPECTED_MAX_ATTEMPTS)
        );

    }

    @Test
    void new_KO_EmptySecretExceptionWithNull() {
        Assertions.assertThrows(
                EmptySecretException.class,
                () -> new Game(EXPECTED_UUID, null, EXPECTED_MAX_ATTEMPTS)
        );
    }

    @Test
    void new_KO_EmptySecretExceptionWithEmptyList() {
        Assertions.assertThrows(
                EmptySecretException.class,
                () -> new Game(EXPECTED_UUID, Lists.emptyList(), EXPECTED_MAX_ATTEMPTS)
        );
    }

    @Test
    void new_KO_WrongNumberOfAttemptsException() {
        Assertions.assertThrows(
                WrongNumberOfAttemptsException.class,
                () -> new Game(EXPECTED_UUID, EXPECTED_SECRET, -10)
        );

        Assertions.assertThrows(
                WrongNumberOfAttemptsException.class,
                () -> new Game(EXPECTED_UUID, EXPECTED_SECRET, 0)
        );
    }

    @Test
    void addAttempt_OK() throws ModelException {
        Game game = new Game(EXPECTED_UUID, EXPECTED_SECRET, EXPECTED_MAX_ATTEMPTS);

        assertTrue(game.getAttempts().isEmpty());

        game.addAttempt(attempt);

        assertEquals(1, game.getAttempts().size());
        assertEquals(attempt, game.getAttempts().get(0));
    }

    @Test
    void addAttempt_KO_GameIsOverException() throws ModelException {
        Game game = new Game(EXPECTED_UUID, EXPECTED_SECRET, 1);

        assertTrue(game.getAttempts().isEmpty());

        game.addAttempt(attempt);

        Assertions.assertThrows(
                GameIsOverException.class,
                () -> game.addAttempt(attempt)
        );
    }

    @Test
    void addAttempt_KO_GameIsSolvedException() throws ModelException {
        Game game = new Game(EXPECTED_UUID, EXPECTED_SECRET, 2);

        assertTrue(game.getAttempts().isEmpty());

        when(attempt.getInput())
                .thenReturn(EXPECTED_SECRET);

        game.addAttempt(attempt);

        Assertions.assertThrows(
                GameIsSolvedException.class,
                () -> game.addAttempt(attempt)
        );

        verify(attempt, times(1))
                .getInput();
    }

    @Test
    void isSolved_OK() throws ModelException {
        Game game = new Game(EXPECTED_UUID, EXPECTED_SECRET, EXPECTED_MAX_ATTEMPTS);

        assertFalse(game.isSolved());

        when(attempt.getInput())
                .thenReturn(EXPECTED_SECRET);

        game.addAttempt(attempt);

        assertTrue(game.isSolved());

        verify(attempt, times(1))
                .getInput();
    }

    @Test
    void isEnded_OK() throws ModelException {
        Game game = new Game(EXPECTED_UUID, EXPECTED_SECRET, 1);

        assertFalse(game.isEnded());

        game.addAttempt(attempt);

        assertTrue(game.isEnded());
    }


}
