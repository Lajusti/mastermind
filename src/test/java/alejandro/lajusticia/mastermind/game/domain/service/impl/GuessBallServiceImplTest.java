package alejandro.lajusticia.mastermind.game.domain.service.impl;

import alejandro.lajusticia.mastermind.game.domain.enumeration.GuessColor;
import alejandro.lajusticia.mastermind.game.domain.model.GuessBall;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class GuessBallServiceImplTest {

    @Mock
    private Random random;

    @InjectMocks
    private GuessBallServiceImpl guessBallService;

    @Test
    void generateRandomGuessBall_OK() {
        for (int i = 0; i < GuessColor.values().length; i++) {
            when(random.nextInt(GuessColor.values().length))
                    .thenReturn(i);

            assertEquals(new GuessBall(GuessColor.values()[i]), guessBallService.generateRandomGuessBall());
        }
    }

}
