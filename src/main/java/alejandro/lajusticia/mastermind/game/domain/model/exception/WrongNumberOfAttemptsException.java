package alejandro.lajusticia.mastermind.game.domain.model.exception;

public class WrongNumberOfAttemptsException extends ModelException {

    private static final String MESSAGE = "The number: %d is not a valid number of attempts.";

    public WrongNumberOfAttemptsException(int maxAttempts) {
        super(String.format(MESSAGE, maxAttempts));
    }

}
