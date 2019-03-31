package alejandro.lajusticia.mastermind.game.domain.service.exception;

public class GameNotFoundException extends ServiceException {

    private static final String MESSAGE = "The game with the id: %s does not exists.";

    public GameNotFoundException(String id) {
        super(String.format(MESSAGE, id));
    }

}
