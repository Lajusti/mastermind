package alejandro.lajusticia.mastermind.game.domain.service.impl;

import alejandro.lajusticia.mastermind.game.domain.enumeration.GuessColor;
import alejandro.lajusticia.mastermind.game.domain.model.GuessBall;
import alejandro.lajusticia.mastermind.game.domain.service.GuessBallService;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class GuessBallServiceImpl implements GuessBallService {

    private final Random randomGenerator;

    public GuessBallServiceImpl(Random randomGenerator) {
        this.randomGenerator = randomGenerator;
    }

    @Override
    public GuessBall generateRandomGuessBall() {
        GuessColor randomGuessColor = generateRandomGuessColor();
        return new GuessBall(randomGuessColor);
    }

    private GuessColor generateRandomGuessColor() {
        GuessColor[] colors = GuessColor.values();
        int numberOfColors = colors.length;
        return colors[randomGenerator.nextInt(numberOfColors)];
    }

}
