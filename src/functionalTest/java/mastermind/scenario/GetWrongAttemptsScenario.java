package mastermind.scenario;

import alejandro.lajusticia.mastermind.game.application.response.ErrorResponse;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GetWrongAttemptsScenario {

    public static void doScenario(String url) {
        tryToGetGame(url);
    }

    private final static String PATH = "games/WRONG_GAME_ID/attempts";

    private static void tryToGetGame(String url) {
        ResponseEntity<ErrorResponse> responseEntity = new TestRestTemplate()
                .exchange(
                        url + PATH,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<ErrorResponse>() {}
                );

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getBody().getCode());
    }

    private GetWrongAttemptsScenario() {

    }

}
