package alejandro.lajusticia.mastermind.game.domain.model;

import alejandro.lajusticia.mastermind.game.domain.model.exception.EmptyInputException;
import alejandro.lajusticia.mastermind.game.domain.model.exception.EmptyOutputException;
import alejandro.lajusticia.mastermind.game.domain.model.exception.OverrideOutputException;

import java.util.List;
import java.util.Optional;

public class Attempt {

    private final List<GuessBall> input;
    private List<FeedbackBall> output;

    public Attempt(final List<GuessBall> input) throws EmptyInputException {
        if (input == null || input.isEmpty()) {
            throw new EmptyInputException();
        }

        this.input = input;
    }

    public List<GuessBall> getInput() {
        return this.input;
    }

    public void setOutput(List<FeedbackBall> output) throws OverrideOutputException, EmptyOutputException {
        if (this.output != null) {
            throw new OverrideOutputException();
        }

        if (output == null || output.isEmpty()) {
            throw new EmptyOutputException();
        }

        this.output = output;
    }

    public Optional<List<FeedbackBall>> getOutput() {
        if (output != null) {
            return Optional.of(output);
        }

        return Optional.empty();
    }

}
