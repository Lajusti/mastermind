package alejandro.lajusticia.mastermind.game.domain.model.exception;

public class NullFeedbackException extends ModelException {

    private static final String MESSAGE = "The feedback of attempt can not be null.";

    public NullFeedbackException() {
        super(MESSAGE);
    }

}
