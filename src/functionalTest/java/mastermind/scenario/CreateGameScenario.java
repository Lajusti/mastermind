package mastermind.scenario;

import alejandro.lajusticia.mastermind.game.application.request.CreationGameRequest;
import alejandro.lajusticia.mastermind.game.application.response.CreationGameResponse;
import alejandro.lajusticia.mastermind.game.application.response.GetGameResponse;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

public class CreateGameScenario {

    public static void doScenario(String url, HttpHeaders headers) {
        String id = createGame(url, headers);
        getGame(id, url);
    }

    private final static String PATH = "games";
    private final static int EXPECTED_MAX_ATTEMPTS = 77;

    private static String createGame(String url, HttpHeaders httpHeaders) {
        CreationGameRequest request = new CreationGameRequest();
        request.setMaxAttempts(EXPECTED_MAX_ATTEMPTS);

        ResponseEntity<CreationGameResponse> responseEntity = new TestRestTemplate()
                .exchange(
                        url + PATH,
                        HttpMethod.POST,
                        new HttpEntity<>(request, httpHeaders),
                        new ParameterizedTypeReference<CreationGameResponse>() {}
                );

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(EXPECTED_MAX_ATTEMPTS, responseEntity.getBody().getMaxAttempts());
        assertNotNull(responseEntity.getBody().getId());
        return responseEntity.getBody().getId();
    }

    private static void getGame(String id, String url) {
        ResponseEntity<GetGameResponse> responseEntity = new TestRestTemplate()
                .exchange(
                        url + PATH + "/" + id,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<GetGameResponse>() {}
                );

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(id, responseEntity.getBody().getId());
        assertEquals(EXPECTED_MAX_ATTEMPTS, responseEntity.getBody().getMaxAttempts());
        assertEquals(0, responseEntity.getBody().getCurrentAttempt());
        assertFalse(responseEntity.getBody().isEnded());
        assertFalse(responseEntity.getBody().isSolved());
        assertEquals(0, responseEntity.getBody().getAttempts().size());
    }

    private CreateGameScenario() {

    }

}
