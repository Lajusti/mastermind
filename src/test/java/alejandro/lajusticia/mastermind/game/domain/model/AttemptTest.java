package alejandro.lajusticia.mastermind.game.domain.model;

import alejandro.lajusticia.mastermind.game.domain.enumeration.FeedbackColor;
import alejandro.lajusticia.mastermind.game.domain.enumeration.GuessColor;
import alejandro.lajusticia.mastermind.game.domain.model.exception.EmptyInputException;
import alejandro.lajusticia.mastermind.game.domain.model.exception.EmptyOutputException;
import alejandro.lajusticia.mastermind.game.domain.model.exception.OverrideOutputException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AttemptTest {

    private final List<GuessBall> EXPECTED_INPUT = Arrays.asList(
            new GuessBall(GuessColor.BLUE),
            new GuessBall(GuessColor.ORANGE),
            new GuessBall(GuessColor.GREEN),
            new GuessBall(GuessColor.RED)
    );

    private final List<FeedbackBall> EXPECTED_OUTPUT = Arrays.asList(
            new FeedbackBall(FeedbackColor.BLACK),
            new FeedbackBall(FeedbackColor.BLACK),
            new FeedbackBall(FeedbackColor.WHITE)
    );

    @Test
    void create_OK() throws EmptyInputException {
        Attempt attempt = new Attempt(EXPECTED_INPUT);

        assertNotNull(attempt);
        assertEquals(EXPECTED_INPUT, attempt.getInput());
        assertFalse(attempt.getOutput().isPresent());
    }

    @Test
    void create_KO_EmptyInputExceptionNull() {
        Assertions.assertThrows(
                EmptyInputException.class,
                () -> new Attempt(null)
        );
    }

    @Test
    void create_KO_EmptyInputExceptionEmptyList() {
        Assertions.assertThrows(
                EmptyInputException.class,
                () -> new Attempt(Lists.emptyList())
        );
    }

    @Test
    void setOutput_OK() throws EmptyInputException, OverrideOutputException, EmptyOutputException {
        Attempt attempt = new Attempt(EXPECTED_INPUT);

        attempt.setOutput(EXPECTED_OUTPUT);

        assertTrue(attempt.getOutput().isPresent());
        assertEquals(EXPECTED_OUTPUT, attempt.getOutput().get());
    }

    @Test
    void setOutput_KO_EmptyOutputExceptionNull() throws EmptyInputException {
        Attempt attempt = new Attempt(EXPECTED_INPUT);

        Assertions.assertThrows(
                EmptyOutputException.class,
                () -> attempt.setOutput(null)
        );
    }

    @Test
    void setOutput_KO_EmptyOutputExceptionEmptyList() throws EmptyInputException {
        Attempt attempt = new Attempt(EXPECTED_INPUT);

        Assertions.assertThrows(
                EmptyOutputException.class,
                () -> attempt.setOutput(Lists.emptyList())
        );
    }

    @Test
    void setOutput_KO_OverrideOutputException()
            throws EmptyInputException, OverrideOutputException, EmptyOutputException
    {
        Attempt attempt = new Attempt(EXPECTED_INPUT);
        attempt.setOutput(EXPECTED_OUTPUT);

        Assertions.assertThrows(
                OverrideOutputException.class,
                () -> attempt.setOutput(EXPECTED_OUTPUT)
        );
    }

}
