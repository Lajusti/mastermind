package alejandro.lajusticia.mastermind.game.domain.service;

import alejandro.lajusticia.mastermind.game.domain.model.GuessBall;

public interface GuessBallService {

    /**
     * Generate a random GuessBall
     *
     * @return GuessBall generated
     */
    GuessBall generateRandomGuessBall();

}
