package alejandro.lajusticia.mastermind.game.domain.enumeration.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ColorConverterTest {

    @Test
    void wrongColor_OK() {
        assertTrue(ColorConverter.convertStringToGuessBalls("RANDOM_NAME").isEmpty());
        assertTrue(ColorConverter.convertStringToFeedbackBalls("RANDOM_NAME").isEmpty());
    }


}
