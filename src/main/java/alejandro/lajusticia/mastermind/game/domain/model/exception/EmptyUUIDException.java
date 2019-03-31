package alejandro.lajusticia.mastermind.game.domain.model.exception;

public class EmptyUUIDException extends ModelException {

    private static final String MESSAGE = "The uuid of game can not be empty.";

    public EmptyUUIDException() {
        super(MESSAGE);
    }

}
