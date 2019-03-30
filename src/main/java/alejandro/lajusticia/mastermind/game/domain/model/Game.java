package alejandro.lajusticia.mastermind.game.domain.model;

import alejandro.lajusticia.mastermind.game.domain.model.exception.EmptySecretException;
import alejandro.lajusticia.mastermind.game.domain.model.exception.GameIsOverException;
import alejandro.lajusticia.mastermind.game.domain.model.exception.WrongNumberOfAttemptsException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Game {

    private final String uuid;
    private final List<GuessBall> secret;
    private final int maxAttempts;
    private List<Attempt> attempts = new ArrayList<>();

    public Game(final List<GuessBall> secret, final int maxAttempts)
            throws WrongNumberOfAttemptsException, EmptySecretException
    {
        if (secret == null || secret.isEmpty()) {
            throw new EmptySecretException();
        }

        if (maxAttempts <= 0) {
            throw new WrongNumberOfAttemptsException(maxAttempts);
        }

        uuid = UUID.randomUUID().toString();
        this.secret = secret;
        this.maxAttempts = maxAttempts;
    }

    public void addAttempt(Attempt attempt) throws GameIsOverException {
        if (attempts.size() == maxAttempts) {
            throw new GameIsOverException();
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
