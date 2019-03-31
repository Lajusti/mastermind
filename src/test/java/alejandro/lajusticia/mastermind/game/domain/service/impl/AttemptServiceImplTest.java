package alejandro.lajusticia.mastermind.game.domain.service.impl;

import alejandro.lajusticia.mastermind.game.domain.enumeration.FeedbackColor;
import alejandro.lajusticia.mastermind.game.domain.enumeration.GuessColor;
import alejandro.lajusticia.mastermind.game.domain.model.Attempt;
import alejandro.lajusticia.mastermind.game.domain.model.FeedbackBall;
import alejandro.lajusticia.mastermind.game.domain.model.GuessBall;
import alejandro.lajusticia.mastermind.game.domain.model.exception.ModelException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AttemptServiceImplTest {

    private final AttemptServiceImpl attemptService = new AttemptServiceImpl();

    @Test
    void createAttemptFromSecretAndAttemptInput_OK_allBlack() throws ModelException {
        List<GuessBall> secret = Arrays.asList(
                new GuessBall(GuessColor.BLUE),
                new GuessBall(GuessColor.ORANGE),
                new GuessBall(GuessColor.GREEN),
                new GuessBall(GuessColor.RED)
        );

        List<GuessBall> firstInput = Lists.newArrayList(secret);

        Attempt attempt = attemptService.createAttemptFromSecretAndAttemptInput(secret, firstInput);

        assertNotNull(attempt);
        assertNotNull(attempt.getInput());

        List<FeedbackBall> feedback = attempt.getFeedback();
        assertNotNull(feedback);

        assertEquals(firstInput, attempt.getInput());
        assertEquals(4, feedback.size());

        for (int i = 0; i < 4; i++) {
            assertEquals(FeedbackColor.BLACK.name(), feedback.get(i).getColor());
        }
    }

    @Test
    void createAttemptFromSecretAndAttemptInput_OK_allWhite() throws ModelException {
        List<GuessBall> secret = Arrays.asList(
                new GuessBall(GuessColor.BLUE),
                new GuessBall(GuessColor.ORANGE),
                new GuessBall(GuessColor.GREEN),
                new GuessBall(GuessColor.RED)
        );

        List<GuessBall> firstInput = Arrays.asList(
                new GuessBall(GuessColor.RED),
                new GuessBall(GuessColor.BLUE),
                new GuessBall(GuessColor.ORANGE),
                new GuessBall(GuessColor.GREEN)
        );

        Attempt attempt = attemptService.createAttemptFromSecretAndAttemptInput(secret, firstInput);

        assertNotNull(attempt);
        assertNotNull(attempt.getInput());

        List<FeedbackBall> feedback = attempt.getFeedback();
        assertNotNull(feedback);

        assertEquals(firstInput, attempt.getInput());
        assertEquals(4, feedback.size());

        for (int i = 0; i < 4; i++) {
            assertEquals(FeedbackColor.WHITE.name(), feedback.get(i).getColor());
        }
    }

    @Test
    void createAttemptFromSecretAndAttemptInput_OK_2Blacks2Whites() throws ModelException {
        List<GuessBall> secret = Arrays.asList(
                new GuessBall(GuessColor.BLUE),
                new GuessBall(GuessColor.ORANGE),
                new GuessBall(GuessColor.GREEN),
                new GuessBall(GuessColor.RED)
        );

        List<GuessBall> firstInput = Arrays.asList(
                new GuessBall(GuessColor.BLUE),
                new GuessBall(GuessColor.GREEN),
                new GuessBall(GuessColor.ORANGE),
                new GuessBall(GuessColor.RED)
        );

        Attempt attempt = attemptService.createAttemptFromSecretAndAttemptInput(secret, firstInput);

        assertNotNull(attempt);
        assertNotNull(attempt.getInput());

        List<FeedbackBall> feedback = attempt.getFeedback();
        assertNotNull(feedback);

        assertEquals(firstInput, attempt.getInput());
        assertEquals(4, feedback.size());

        assertEquals(FeedbackColor.BLACK.name(), feedback.get(0).getColor());
        assertEquals(FeedbackColor.BLACK.name(), feedback.get(1).getColor());
        assertEquals(FeedbackColor.WHITE.name(), feedback.get(2).getColor());
        assertEquals(FeedbackColor.WHITE.name(), feedback.get(3).getColor());
    }

    @Test
    void createAttemptFromSecretAndAttemptInput_OK_empty() throws ModelException {
        List<GuessBall> secret = Arrays.asList(
                new GuessBall(GuessColor.BLUE),
                new GuessBall(GuessColor.ORANGE),
                new GuessBall(GuessColor.GREEN),
                new GuessBall(GuessColor.RED)
        );

        List<GuessBall> firstInput = Arrays.asList(
                new GuessBall(GuessColor.PURPLE),
                new GuessBall(GuessColor.YELLOW),
                new GuessBall(GuessColor.YELLOW),
                new GuessBall(GuessColor.PURPLE)
        );

        Attempt attempt = attemptService.createAttemptFromSecretAndAttemptInput(secret, firstInput);

        assertNotNull(attempt);
        assertNotNull(attempt.getInput());

        List<FeedbackBall> feedback = attempt.getFeedback();
        assertNotNull(feedback);

        assertEquals(firstInput, attempt.getInput());
        assertTrue(feedback.isEmpty());
    }


    @Test
    void createAttemptFromSecretAndAttemptInput_OK_sameColor1Black1White()
            throws ModelException {
        List<GuessBall> secret = Arrays.asList(
                new GuessBall(GuessColor.BLUE),
                new GuessBall(GuessColor.BLUE),
                new GuessBall(GuessColor.GREEN),
                new GuessBall(GuessColor.RED)
        );

        List<GuessBall> firstInput = Arrays.asList(
                new GuessBall(GuessColor.BLUE),
                new GuessBall(GuessColor.YELLOW),
                new GuessBall(GuessColor.BLUE),
                new GuessBall(GuessColor.PURPLE)
        );

        Attempt attempt = attemptService.createAttemptFromSecretAndAttemptInput(secret, firstInput);

        assertNotNull(attempt);
        assertNotNull(attempt.getInput());

        List<FeedbackBall> feedback = attempt.getFeedback();
        assertNotNull(feedback);

        assertEquals(firstInput, attempt.getInput());
        assertEquals(2, feedback.size());

        assertEquals(FeedbackColor.BLACK.name(), feedback.get(0).getColor());
        assertEquals(FeedbackColor.WHITE.name(), feedback.get(1).getColor());
    }

}
