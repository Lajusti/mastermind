package alejandro.lajusticia.mastermind.game.domain.model.exception;

public class EmptyOutputException extends ModelException {

    private final static String MESSAGE = "The output of attempt can not be empty.";

    public EmptyOutputException() {
        super(MESSAGE);
    }

}
