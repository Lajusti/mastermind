package mastermind.scenario;

import alejandro.lajusticia.mastermind.game.application.request.CreationAttemptRequest;
import alejandro.lajusticia.mastermind.game.application.response.ErrorResponse;
import alejandro.lajusticia.mastermind.game.application.response.FeedbackBallResponse;
import alejandro.lajusticia.mastermind.game.application.response.GetGameResponse;
import alejandro.lajusticia.mastermind.game.application.response.GuessBallResponse;
import alejandro.lajusticia.mastermind.game.domain.enumeration.FeedbackColor;
import alejandro.lajusticia.mastermind.game.domain.enumeration.GuessColor;
import alejandro.lajusticia.mastermind.game.domain.model.FeedbackBall;
import alejandro.lajusticia.mastermind.game.domain.model.GuessBall;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GetSolvedGameScenario {

    public static void doScenario(String url, HttpHeaders httpHeaders) {
        getGame(url);
        tryToAddAttempt(url, httpHeaders);
    }

    private final static String EXPECTED_ID = "SOLVED_GAME";
    private final static int EXPECTED_MAX_ATTEMPTS = 10;
    private final static int EXPECTED_CURRENT_ATTEMPTS = 2;

    private final static List<GuessBallResponse> EXPECTED_FIRST_INPUT = Arrays.asList(
            new GuessBallResponse(new GuessBall(GuessColor.BLUE)),
            new GuessBallResponse(new GuessBall(GuessColor.RED)),
            new GuessBallResponse(new GuessBall(GuessColor.BLUE)),
            new GuessBallResponse(new GuessBall(GuessColor.RED))
    );
    private final static List<GuessBallResponse> EXPECTED_SECOND_INPUT = Arrays.asList(
            new GuessBallResponse(new GuessBall(GuessColor.BLUE)),
            new GuessBallResponse(new GuessBall(GuessColor.BLUE)),
            new GuessBallResponse(new GuessBall(GuessColor.RED)),
            new GuessBallResponse(new GuessBall(GuessColor.RED))
    );
    private final static List<FeedbackBallResponse> EXPECTED_FIRST_FEEDBACK = Arrays.asList(
            new FeedbackBallResponse(new FeedbackBall(FeedbackColor.BLACK)),
            new FeedbackBallResponse(new FeedbackBall(FeedbackColor.BLACK)),
            new FeedbackBallResponse(new FeedbackBall(FeedbackColor.WHITE)),
            new FeedbackBallResponse(new FeedbackBall(FeedbackColor.WHITE))
    );
    private final static List<FeedbackBallResponse> EXPECTED_SECOND_FEEDBACK = Arrays.asList(
            new FeedbackBallResponse(new FeedbackBall(FeedbackColor.BLACK)),
            new FeedbackBallResponse(new FeedbackBall(FeedbackColor.BLACK)),
            new FeedbackBallResponse(new FeedbackBall(FeedbackColor.BLACK)),
            new FeedbackBallResponse(new FeedbackBall(FeedbackColor.BLACK))
    );

    private final static String PATH = "games/" + EXPECTED_ID;

    private final static String SECOND_PATH = PATH + "/attempts";

    private static void getGame(String url) {
        ResponseEntity<GetGameResponse> responseEntity = new TestRestTemplate()
                .exchange(
                        url + PATH,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<GetGameResponse>() {}
                );

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(EXPECTED_ID, responseEntity.getBody().getId());
        assertEquals(EXPECTED_MAX_ATTEMPTS, responseEntity.getBody().getMaxAttempts());
        assertEquals(EXPECTED_CURRENT_ATTEMPTS, responseEntity.getBody().getCurrentAttempt());
        assertFalse(responseEntity.getBody().isEnded());
        assertTrue(responseEntity.getBody().isSolved());
        assertEquals(EXPECTED_CURRENT_ATTEMPTS, responseEntity.getBody().getAttempts().size());
        assertNotNull(responseEntity.getBody().getAttempts().get(0));
        assertEquals(EXPECTED_FIRST_INPUT, responseEntity.getBody().getAttempts().get(0).getInput());
        assertEquals(EXPECTED_SECOND_INPUT, responseEntity.getBody().getAttempts().get(1).getInput());
        assertEquals(EXPECTED_FIRST_FEEDBACK, responseEntity.getBody().getAttempts().get(0).getFeedback());
        assertEquals(EXPECTED_SECOND_FEEDBACK, responseEntity.getBody().getAttempts().get(1).getFeedback());
    }

    private static void tryToAddAttempt(String url, HttpHeaders httpHeaders) {
        CreationAttemptRequest request = new CreationAttemptRequest();
        request.setAttempt(Arrays.asList(
                "BLUE",
                "BLUE",
                "BLUE",
                "BLUE"
        ));

        ResponseEntity<ErrorResponse> responseEntityError = new TestRestTemplate()
                .exchange(
                        url + SECOND_PATH,
                        HttpMethod.PUT,
                        new HttpEntity<>(request, httpHeaders),
                        new ParameterizedTypeReference<ErrorResponse>() {}
                );

        assertEquals(HttpStatus.CONFLICT, responseEntityError.getStatusCode());
    }

    private GetSolvedGameScenario() {

    }

}
