package alejandro.lajusticia.mastermind.game.domain.model.exception;

public class EmptySecretException extends ModelException {

    private static final String MESSAGE = "The secret of game can not be empty.";

    public EmptySecretException() {
        super(MESSAGE);
    }

}
