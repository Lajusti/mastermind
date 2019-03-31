package alejandro.lajusticia.mastermind.game.domain.model.exception;

public class OverrideOutputException extends ModelException {

    private static final String MESSAGE = "The output of attempt cannot be overridden when has value.";

    public OverrideOutputException() {
        super(MESSAGE);
    }

}
