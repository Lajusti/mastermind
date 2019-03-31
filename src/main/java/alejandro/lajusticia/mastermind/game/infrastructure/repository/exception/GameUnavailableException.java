package alejandro.lajusticia.mastermind.game.infrastructure.repository.exception;

public class GameUnavailableException extends RepositoryException {

    private static final String MESSAGE = "This game can not be recovered from the database, this game will " +
            "be evaluated by our technical team early as possible";

    public GameUnavailableException() {
        super(MESSAGE);
    }

}
