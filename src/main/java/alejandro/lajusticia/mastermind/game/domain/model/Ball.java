package alejandro.lajusticia.mastermind.game.domain.model;

import lombok.Getter;

@Getter
public abstract class Ball {

    private final String color;

    Ball(final String color) {
        this.color = color;
    }

}
