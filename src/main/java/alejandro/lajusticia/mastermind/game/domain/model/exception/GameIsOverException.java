package alejandro.lajusticia.mastermind.game.domain.model.exception;

public class GameIsOverException extends ModelException {

    private final static String MESSAGE = "The game is over and another attempt can not be made.";

    public GameIsOverException() {
        super(MESSAGE);
    }

}
