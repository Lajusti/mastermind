package alejandro.lajusticia.mastermind.game.domain.model;

import alejandro.lajusticia.mastermind.game.domain.enumeration.FeedbackColor;

public class FeedbackBall extends Ball {

    public FeedbackBall(final FeedbackColor color) {
        super(color.name());
    }

}

