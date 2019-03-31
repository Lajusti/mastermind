package alejandro.lajusticia.mastermind.game.domain.model;

import alejandro.lajusticia.mastermind.game.domain.enumeration.FeedbackColor;
import alejandro.lajusticia.mastermind.game.domain.enumeration.GuessColor;
import alejandro.lajusticia.mastermind.game.domain.model.exception.NullFeedbackException;
import alejandro.lajusticia.mastermind.game.domain.model.exception.EmptyInputException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AttemptTest {

    private final List<GuessBall> EXPECTED_INPUT = Arrays.asList(
            new GuessBall(GuessColor.BLUE),
            new GuessBall(GuessColor.ORANGE),
            new GuessBall(GuessColor.GREEN),
            new GuessBall(GuessColor.RED)
    );

    private final List<FeedbackBall> EXPECTED_FEEDBACK = Arrays.asList(
            new FeedbackBall(FeedbackColor.BLACK),
            new FeedbackBall(FeedbackColor.BLACK),
            new FeedbackBall(FeedbackColor.WHITE)
    );

    @Test
    void create_OK() throws EmptyInputException, NullFeedbackException {
        Attempt attempt = new Attempt(EXPECTED_INPUT, EXPECTED_FEEDBACK);

        assertNotNull(attempt);
        assertEquals(EXPECTED_INPUT, attempt.getInput());
        assertEquals(EXPECTED_FEEDBACK, attempt.getFeedback());
    }

    @Test
    void create_KO_EmptyInputExceptionNull() {
        Assertions.assertThrows(
                EmptyInputException.class,
                () -> new Attempt(null, EXPECTED_FEEDBACK)
        );
    }

    @Test
    void create_KO_EmptyInputExceptionEmptyList() {
        Assertions.assertThrows(
                EmptyInputException.class,
                () -> new Attempt(Lists.emptyList(), EXPECTED_FEEDBACK)
        );
    }

    @Test
    void create_KO_EmptyFeedbackExceptionNull() {
        Assertions.assertThrows(
                NullFeedbackException.class,
                () -> new Attempt(EXPECTED_INPUT, null)
        );
    }

}
