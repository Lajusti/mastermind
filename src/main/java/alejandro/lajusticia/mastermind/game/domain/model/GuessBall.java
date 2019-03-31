package alejandro.lajusticia.mastermind.game.domain.model;

import alejandro.lajusticia.mastermind.game.domain.enumeration.GuessColor;
import lombok.ToString;

import java.util.Objects;

@ToString
public class GuessBall extends Ball {

    private final GuessColor guessColor;

    public GuessBall(final GuessColor color) {
        super(color.name());
        guessColor = color;
    }

    public GuessBall(final String color) {
        super(color);
        guessColor = GuessColor.valueOf(color);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GuessBall guessBall = (GuessBall) o;
        return guessColor == guessBall.guessColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(guessColor);
    }

}