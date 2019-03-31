package alejandro.lajusticia.mastermind.game.domain.model;

import alejandro.lajusticia.mastermind.game.domain.model.exception.NullFeedbackException;
import alejandro.lajusticia.mastermind.game.domain.model.exception.EmptyInputException;
import lombok.Getter;

import java.util.List;

@Getter
public class Attempt {

    private final List<GuessBall> input;
    private final List<FeedbackBall> feedback;

    public Attempt(final List<GuessBall> input, final List<FeedbackBall> feedback)
            throws EmptyInputException, NullFeedbackException
    {
        if (input == null || input.isEmpty()) {
            throw new EmptyInputException();
        }

        if (feedback == null) {
            throw new NullFeedbackException();
        }

        this.input = input;
        this.feedback = feedback;
    }

}
