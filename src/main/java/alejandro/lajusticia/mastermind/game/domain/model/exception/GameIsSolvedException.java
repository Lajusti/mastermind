package alejandro.lajusticia.mastermind.game.domain.model.exception;

public class GameIsSolvedException extends ModelException {

    private static final String MESSAGE = "The game is solved and another attempt can not be made.";

    public GameIsSolvedException() {
        super(MESSAGE);
    }

}
