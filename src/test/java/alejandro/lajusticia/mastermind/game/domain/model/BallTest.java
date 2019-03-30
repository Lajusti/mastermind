package alejandro.lajusticia.mastermind.game.domain.model;

import alejandro.lajusticia.mastermind.game.domain.enumeration.FeedbackColor;
import alejandro.lajusticia.mastermind.game.domain.enumeration.GuessColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BallTest {

    @Test
    void getColor_OK() {
        Ball blackBall = new FeedbackBall(FeedbackColor.BLACK);
        Ball whiteBall = new FeedbackBall(FeedbackColor.WHITE);
        Ball redBall = new GuessBall(GuessColor.RED);
        Ball blueBall = new GuessBall(GuessColor.BLUE);
        Ball greenBall = new GuessBall(GuessColor.GREEN);
        Ball yellowBall = new GuessBall(GuessColor.YELLOW);
        Ball orangeBall = new GuessBall(GuessColor.ORANGE);
        Ball purpleBall = new GuessBall(GuessColor.PURPLE);

        assertEquals(FeedbackColor.BLACK.name(), blackBall.getColor());
        assertEquals(FeedbackColor.WHITE.name(), whiteBall.getColor());
        assertEquals(GuessColor.RED.name(), redBall.getColor());
        assertEquals(GuessColor.BLUE.name(), blueBall.getColor());
        assertEquals(GuessColor.GREEN.name(), greenBall.getColor());
        assertEquals(GuessColor.YELLOW.name(), yellowBall.getColor());
        assertEquals(GuessColor.ORANGE.name(), orangeBall.getColor());
        assertEquals(GuessColor.PURPLE.name(), purpleBall.getColor());
    }

}
