package alejandro.lajusticia.mastermind.game.domain.service.exception;

public class WrongAttemptLengthException extends ServiceException {

    private static final String MESSAGE = "The attempt only can have %d items.";

    public WrongAttemptLengthException(int lengthAttempt) {
        super(String.format(MESSAGE, lengthAttempt));
    }

}
