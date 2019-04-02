package mastermind.scenario;

import alejandro.lajusticia.mastermind.game.application.request.CreationAttemptRequest;
import alejandro.lajusticia.mastermind.game.application.response.*;
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
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetAndAddAttemptsUntilSolveScenario {

    public static void doScenario(String url, HttpHeaders httpHeaders) {
        getGame(url);
        addAttempt(url, httpHeaders, FIRST_INPUT, EXPECTED_FIRST_FEEDBACK);
        getGameAfterAttempt(url, EXPECTED_FIRST_INPUT, EXPECTED_FIRST_FEEDBACK, 1);
        addAttempt(url, httpHeaders, SECOND_INPUT, EXPECTED_SECOND_FEEDBACK);
        getGameAfterSolve(url, EXPECTED_SECOND_INPUT, EXPECTED_SECOND_FEEDBACK, 2);
        tryToAddAttempt(url, httpHeaders);
    }

    private final static String EXPECTED_ID = "NOT_ENDED_GAME_TO_SOLVE";
    private final static int EXPECTED_MAX_ATTEMPTS = 15;

    private final static List<String> FIRST_INPUT = Arrays.asList(
            "RED",
            "YELLOW",
            "ORANGE",
            "GREEN"
    );
    private final static List<String> SECOND_INPUT = Arrays.asList(
            "RED",
            "YELLOW",
            "BLUE",
            "ORANGE"
    );
    private final static List<GuessBallResponse> EXPECTED_FIRST_INPUT = Arrays.asList(
            new GuessBallResponse(new GuessBall(GuessColor.RED)),
            new GuessBallResponse(new GuessBall(GuessColor.YELLOW)),
            new GuessBallResponse(new GuessBall(GuessColor.ORANGE)),
            new GuessBallResponse(new GuessBall(GuessColor.GREEN))
    );
    private final static List<GuessBallResponse> EXPECTED_SECOND_INPUT = Arrays.asList(
            new GuessBallResponse(new GuessBall(GuessColor.RED)),
            new GuessBallResponse(new GuessBall(GuessColor.YELLOW)),
            new GuessBallResponse(new GuessBall(GuessColor.BLUE)),
            new GuessBallResponse(new GuessBall(GuessColor.ORANGE))
    );
    private final static List<FeedbackBallResponse> EXPECTED_FIRST_FEEDBACK = Arrays.asList(
            new FeedbackBallResponse(new FeedbackBall(FeedbackColor.BLACK)),
            new FeedbackBallResponse(new FeedbackBall(FeedbackColor.BLACK)),
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
        assertEquals(0, responseEntity.getBody().getCurrentAttempt());
        assertFalse(responseEntity.getBody().isEnded());
        assertFalse(responseEntity.getBody().isSolved());
        assertEquals(0, responseEntity.getBody().getAttempts().size());
    }

    private static void addAttempt(
            String url, HttpHeaders httpHeaders,
            List<String> input,
            List<FeedbackBallResponse> expectedFeedback) {
        CreationAttemptRequest request = new CreationAttemptRequest();
        request.setAttempt(input);

        ResponseEntity<CreationAttemptResponse> responseEntityError = new TestRestTemplate()
                .exchange(
                        url + SECOND_PATH,
                        HttpMethod.PUT,
                        new HttpEntity<>(request, httpHeaders),
                        new ParameterizedTypeReference<CreationAttemptResponse>() {}
                );

        assertEquals(HttpStatus.OK, responseEntityError.getStatusCode());
        assertNotNull(responseEntityError.getBody());
        assertEquals(expectedFeedback, responseEntityError.getBody().getFeedback());
    }

    private static void getGameAfterAttempt(
            String url,
            List<GuessBallResponse> expectedInput,
            List<FeedbackBallResponse> expectedFeedback,
            int attempts)
    {
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
        assertEquals(attempts, responseEntity.getBody().getCurrentAttempt());
        assertFalse(responseEntity.getBody().isEnded());
        assertFalse(responseEntity.getBody().isSolved());
        assertEquals(attempts, responseEntity.getBody().getAttempts().size());
        assertNotNull(responseEntity.getBody().getAttempts().get(attempts - 1));
        assertEquals(expectedInput, responseEntity.getBody().getAttempts().get(attempts - 1).getInput());
        assertEquals(expectedFeedback, responseEntity.getBody().getAttempts().get(attempts - 1).getFeedback());
    }

    private static void getGameAfterSolve(
            String url,
            List<GuessBallResponse> expectedInput,
            List<FeedbackBallResponse> expectedFeedback,
            int attempts)
    {
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
        assertEquals(attempts, responseEntity.getBody().getCurrentAttempt());
        assertFalse(responseEntity.getBody().isEnded());
        assertTrue(responseEntity.getBody().isSolved());
        assertEquals(attempts, responseEntity.getBody().getAttempts().size());
        assertNotNull(responseEntity.getBody().getAttempts().get(attempts - 1));
        assertEquals(expectedInput, responseEntity.getBody().getAttempts().get(attempts - 1).getInput());
        assertEquals(expectedFeedback, responseEntity.getBody().getAttempts().get(attempts - 1).getFeedback());
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

    private GetAndAddAttemptsUntilSolveScenario() {

    }

}
