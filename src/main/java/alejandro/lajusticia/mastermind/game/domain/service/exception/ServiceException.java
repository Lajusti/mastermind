package alejandro.lajusticia.mastermind.game.domain.service.exception;

public abstract class ServiceException extends Exception {

    ServiceException(String message) {
        super(message);
    }

}
