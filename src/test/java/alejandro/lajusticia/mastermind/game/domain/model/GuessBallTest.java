package alejandro.lajusticia.mastermind.game.domain.model;

import alejandro.lajusticia.mastermind.game.domain.enumeration.FeedbackColor;
import alejandro.lajusticia.mastermind.game.domain.enumeration.GuessColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GuessBallTest {

    @Test
    void equals_OK() {
        GuessBall guessBallRed1 = new GuessBall(GuessColor.RED);
        assertTrue(guessBallRed1.equals(guessBallRed1));
        assertFalse(guessBallRed1.equals(null));

        GuessBall guessBallRed2 = new GuessBall(GuessColor.RED);
        assertEquals(guessBallRed1, guessBallRed2);

        GuessBall guessBallBlue = new GuessBall(GuessColor.BLUE);
        assertNotEquals(guessBallRed1, guessBallBlue);

        FeedbackBall whiteBall = new FeedbackBall(FeedbackColor.WHITE);

        assertFalse(guessBallRed1.equals(whiteBall));
    }

    @Test
    void hashCode_OK() {
        GuessBall guessBallRed1 = new GuessBall(GuessColor.RED);
        GuessBall guessBallRed2 = new GuessBall(GuessColor.RED);

        assertEquals(guessBallRed1.hashCode(), guessBallRed2.hashCode());

        GuessBall guessBallBlue = new GuessBall(GuessColor.BLUE);

        assertNotEquals(guessBallRed1.hashCode(), guessBallBlue.hashCode());
    }

}
