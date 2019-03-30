package alejandro.lajusticia.mastermind.game.domain.model.exception;

public class EmptyInputException extends ModelException {

    private final static String MESSAGE = "The input of attempt can not be empty.";

    public EmptyInputException() {
        super(MESSAGE);
    }

}