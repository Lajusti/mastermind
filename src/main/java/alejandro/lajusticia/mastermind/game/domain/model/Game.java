package alejandro.lajusticia.mastermind.game.domain.model;

import alejandro.lajusticia.mastermind.game.domain.model.exception.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Game {

    private final String uuid;
    private final List<GuessBall> secret;
    private final int maxAttempts;
    private List<Attempt> attempts = new ArrayList<>();

    public Game(final String uuid, final List<GuessBall> secret, final int maxAttempts)
            throws EmptySecretException, WrongNumberOfAttemptsException, EmptyUUIDException
    {
        if (uuid == null || uuid.isEmpty()) {
            throw new EmptyUUIDException();
        }

        if (secret == null || secret.isEmpty()) {
            throw new EmptySecretException();
        }

        if (maxAttempts <= 0) {
            throw new WrongNumberOfAttemptsException(maxAttempts);
        }

        this.uuid = uuid;
        this.secret = secret;
        this.maxAttempts = maxAttempts;
    }

    public void addAttempt(Attempt attempt) throws GameIsOverException, GameIsSolvedException {
        if (attempts.size() == maxAttempts) {
            throw new GameIsOverException();
        }

        if (isSolved()) {
            throw new GameIsSolvedException();
        }

        attempts.add(attempt);
    }

    public boolean isSolved() {
        if (attempts.isEmpty())
            return false;

        Attempt lastAttempt = attempts.get(attempts.size() - 1);
        return secret.equals(lastAttempt.getInput());
    }

    public boolean isEnded() {
        return maxAttempts == attempts.size();
    }

}
