package alejandro.lajusticia.mastermind.game.applitaction;

import alejandro.lajusticia.mastermind.game.application.controller.AttemptController;
import alejandro.lajusticia.mastermind.game.application.request.CreationAttemptRequest;
import alejandro.lajusticia.mastermind.game.application.response.CreationAttemptResponse;
import alejandro.lajusticia.mastermind.game.application.response.ErrorResponse;
import alejandro.lajusticia.mastermind.game.application.response.GetAttemptsResponse;
import alejandro.lajusticia.mastermind.game.domain.enumeration.FeedbackColor;
import alejandro.lajusticia.mastermind.game.domain.enumeration.GuessColor;
import alejandro.lajusticia.mastermind.game.domain.model.Attempt;
import alejandro.lajusticia.mastermind.game.domain.model.FeedbackBall;
import alejandro.lajusticia.mastermind.game.domain.model.Game;
import alejandro.lajusticia.mastermind.game.domain.model.GuessBall;
import alejandro.lajusticia.mastermind.game.domain.model.exception.EmptyUUIDException;
import alejandro.lajusticia.mastermind.game.domain.model.exception.ModelException;
import alejandro.lajusticia.mastermind.game.domain.service.GameService;
import alejandro.lajusticia.mastermind.game.domain.service.exception.GameNotFoundException;
import alejandro.lajusticia.mastermind.game.domain.service.exception.WrongAttemptLengthException;
import alejandro.lajusticia.mastermind.game.infrastructure.repository.exception.GameUnavailableException;
import alejandro.lajusticia.mastermind.game.infrastructure.repository.exception.RepositoryException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AttemptControllerTest {

    private final List<GuessBall> EXPECTED_SECRET = Arrays.asList(
            new GuessBall(GuessColor.BLUE),
            new GuessBall(GuessColor.ORANGE),
            new GuessBall(GuessColor.GREEN),
            new GuessBall(GuessColor.RED)
    );

    private List<GuessBall> EXPECTED_INPUT = Arrays.asList(
            new GuessBall(GuessColor.BLUE),
            new GuessBall(GuessColor.YELLOW),
            new GuessBall(GuessColor.PURPLE),
            new GuessBall(GuessColor.GREEN)
    );

    private final List<String> EXPECTED_INPUT_AS_STRING = Arrays.asList(
            GuessColor.BLUE.name(),
            GuessColor.YELLOW.name(),
            GuessColor.PURPLE.name(),
            GuessColor.GREEN.name()
    );

    private final List<FeedbackBall> EXPECTED_FEEDBACK = Arrays.asList(
            new FeedbackBall(FeedbackColor.BLACK),
            new FeedbackBall(FeedbackColor.WHITE)
    );

    @Mock
    private GameService gameService;

    @InjectMocks
    private AttemptController attemptController;

    @Test
    void getAttempts_OK() throws ModelException, RepositoryException, GameNotFoundException {
        String expectedId = "ID";
        int expectedAttempts = 10;
        Game expectedGame = getExpectedGame(expectedId, expectedAttempts);
        expectedGame.addAttempt(new Attempt(EXPECTED_INPUT, EXPECTED_FEEDBACK));
        expectedGame.addAttempt(new Attempt(EXPECTED_INPUT, EXPECTED_FEEDBACK));

        when(gameService.getGame(expectedId))
                .thenReturn(expectedGame);

        ResponseEntity responseEntity = attemptController.getAttempts(expectedId);

        verify(gameService, times(1))
                .getGame(expectedId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody() instanceof GetAttemptsResponse);

        GetAttemptsResponse getAttemptsResponse = (GetAttemptsResponse) responseEntity.getBody();
        assertEquals(2, getAttemptsResponse.getAttempts().size());

        validateAttempt(getAttemptsResponse.getAttempts().get(0));
        validateAttempt(getAttemptsResponse.getAttempts().get(1));
    }

    @Test
    void getAttempts_KO_404() throws RepositoryException, GameNotFoundException {
        String expectedId = "ID";

        when(gameService.getGame(expectedId))
                .thenThrow(new GameNotFoundException(expectedId));

        ResponseEntity responseEntity = attemptController.getAttempts(expectedId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody() instanceof ErrorResponse);

        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), errorResponse.getCode());
        assertFalse(errorResponse.getDescription().isEmpty());
    }

    @Test
    void getAttempts_KO_500_RepositoryException() throws RepositoryException, GameNotFoundException {
        String expectedId = "ID";

        when(gameService.getGame(expectedId))
                .thenThrow(new GameUnavailableException());

        ResponseEntity responseEntity = attemptController.getAttempts(expectedId);

        verify(gameService, times(1))
                .getGame(expectedId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody() instanceof ErrorResponse);

        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse.getCode());
        assertFalse(errorResponse.getDescription().isEmpty());
    }

    @Test
    void getAttempts_KO_500() throws RepositoryException, GameNotFoundException {
        String expectedId = "ID";

        when(gameService.getGame(expectedId))
                .thenThrow(new NullPointerException());

        ResponseEntity responseEntity = attemptController.getAttempts(expectedId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody() instanceof ErrorResponse);

        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse.getCode());
        assertFalse(errorResponse.getDescription().isEmpty());
    }

    @Test
    void doAttempts_OK()
            throws WrongAttemptLengthException, ModelException, RepositoryException, GameNotFoundException
    {
        String expectedId = "ID";
        int expectedAttempts = 10;

        Game expectedGame = getExpectedGame(expectedId, expectedAttempts);
        expectedGame.addAttempt(new Attempt(EXPECTED_INPUT, EXPECTED_FEEDBACK));

        when(gameService.addAttemptToGameByGameIdAndAttemptInput(expectedId, EXPECTED_INPUT))
                .thenReturn(expectedGame);

        ResponseEntity responseEntity = attemptController.doAttempts(expectedId, createExpectedAttemptRequest());

        verify(gameService, times(1))
                .addAttemptToGameByGameIdAndAttemptInput(expectedId, EXPECTED_INPUT);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody() instanceof CreationAttemptResponse);

        CreationAttemptResponse creationAttemptResponse = (CreationAttemptResponse) responseEntity.getBody();
        assertEquals(2, creationAttemptResponse.getFeedback().size());
        assertEquals(EXPECTED_FEEDBACK.get(0).getColor(), creationAttemptResponse.getFeedback().get(0).getColor());
        assertEquals(EXPECTED_FEEDBACK.get(1).getColor(), creationAttemptResponse.getFeedback().get(1).getColor());
    }

    @Test
    void doAttempts_KO_400_WrongAttemptLengthException()
            throws WrongAttemptLengthException, ModelException, RepositoryException, GameNotFoundException
    {
        String expectedId = "ID";

        when(gameService.addAttemptToGameByGameIdAndAttemptInput(expectedId, EXPECTED_INPUT))
                .thenThrow(new WrongAttemptLengthException(4));

        ResponseEntity responseEntity = attemptController.doAttempts(expectedId, createExpectedAttemptRequest());

        verify(gameService, times(1))
                .addAttemptToGameByGameIdAndAttemptInput(expectedId, EXPECTED_INPUT);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody() instanceof ErrorResponse);

        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getCode());
        assertFalse(errorResponse.getDescription().isEmpty());
    }

    @Test
    void doAttempts_KO_400_IllegalArgumentException()
            throws WrongAttemptLengthException, ModelException, RepositoryException, GameNotFoundException
    {
        String expectedId = "ID";
        CreationAttemptRequest wrongRequest = new CreationAttemptRequest();
        wrongRequest.setAttempt(
                Arrays.asList(
                        GuessColor.BLUE.name(),
                        GuessColor.YELLOW.name(),
                        GuessColor.PURPLE.name(),
                        "WRONG_NAME"
                )
        );

        ResponseEntity responseEntity = attemptController.doAttempts(expectedId, wrongRequest);

        verify(gameService, times(0))
                .addAttemptToGameByGameIdAndAttemptInput(any(), any());

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody() instanceof ErrorResponse);

        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getCode());
        assertFalse(errorResponse.getDescription().isEmpty());
    }

    @Test
    void doAttempts_KO_400_ModelException()
            throws WrongAttemptLengthException, ModelException, RepositoryException, GameNotFoundException
    {
        String expectedId = "ID";

        when(gameService.addAttemptToGameByGameIdAndAttemptInput(expectedId, EXPECTED_INPUT))
                .thenThrow(new EmptyUUIDException());

        ResponseEntity responseEntity = attemptController.doAttempts(expectedId, createExpectedAttemptRequest());

        verify(gameService, times(1))
                .addAttemptToGameByGameIdAndAttemptInput(expectedId, EXPECTED_INPUT);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody() instanceof ErrorResponse);

        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getCode());
        assertFalse(errorResponse.getDescription().isEmpty());
    }

    @Test
    void doAttempts_KO_404()
            throws WrongAttemptLengthException, ModelException, RepositoryException, GameNotFoundException
    {
        String expectedId = "ID";

        when(gameService.addAttemptToGameByGameIdAndAttemptInput(expectedId, EXPECTED_INPUT))
                .thenThrow(new GameNotFoundException(expectedId));

        ResponseEntity responseEntity = attemptController.doAttempts(expectedId, createExpectedAttemptRequest());

        verify(gameService, times(1))
                .addAttemptToGameByGameIdAndAttemptInput(expectedId, EXPECTED_INPUT);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody() instanceof ErrorResponse);

        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), errorResponse.getCode());
        assertFalse(errorResponse.getDescription().isEmpty());
    }

    @Test
    void doAttempts_KO_500_RepositoryException()
            throws WrongAttemptLengthException, ModelException, RepositoryException, GameNotFoundException
    {
        String expectedId = "ID";

        when(gameService.addAttemptToGameByGameIdAndAttemptInput(expectedId, EXPECTED_INPUT))
                .thenThrow(new GameUnavailableException());

        ResponseEntity responseEntity = attemptController.doAttempts(expectedId, createExpectedAttemptRequest());

        verify(gameService, times(1))
                .addAttemptToGameByGameIdAndAttemptInput(expectedId, EXPECTED_INPUT);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody() instanceof ErrorResponse);

        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse.getCode());
        assertFalse(errorResponse.getDescription().isEmpty());}

    @Test
    void doAttempts_KO_500_withoutAttemptFromService()
            throws WrongAttemptLengthException, ModelException, RepositoryException, GameNotFoundException
    {
        String expectedId = "ID";

        when(gameService.addAttemptToGameByGameIdAndAttemptInput(expectedId, EXPECTED_INPUT))
                .thenThrow(new GameUnavailableException());

        ResponseEntity responseEntity = attemptController.doAttempts(expectedId, createExpectedAttemptRequest());

        verify(gameService, times(1))
                .addAttemptToGameByGameIdAndAttemptInput(expectedId, EXPECTED_INPUT);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody() instanceof ErrorResponse);

        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse.getCode());
        assertFalse(errorResponse.getDescription().isEmpty());
    }

    @Test
    void doAttempts_KO_500()
            throws WrongAttemptLengthException, ModelException, RepositoryException, GameNotFoundException
    {
        String expectedId = "ID";
        int expectedAttempts = 10;
        Game expectedGame = getExpectedGame(expectedId, expectedAttempts);

        when(gameService.addAttemptToGameByGameIdAndAttemptInput(expectedId, EXPECTED_INPUT))
                .thenReturn(expectedGame);

        ResponseEntity responseEntity = attemptController.doAttempts(expectedId, createExpectedAttemptRequest());

        verify(gameService, times(1))
                .addAttemptToGameByGameIdAndAttemptInput(expectedId, EXPECTED_INPUT);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody() instanceof ErrorResponse);

        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse.getCode());
        assertFalse(errorResponse.getDescription().isEmpty());
    }

    private Game getExpectedGame(String id, int maxAttempts) throws ModelException {
        return new Game(id, EXPECTED_SECRET, maxAttempts);
    }

    private void validateAttempt(Attempt attempt) {
        assertEquals(EXPECTED_INPUT, attempt.getInput());
        assertEquals(2, attempt.getFeedback().size());
        assertEquals(new FeedbackBall(FeedbackColor.BLACK).getColor(), attempt.getFeedback().get(0).getColor());
        assertEquals(new FeedbackBall(FeedbackColor.WHITE).getColor(), attempt.getFeedback().get(1).getColor());
    }

    private CreationAttemptRequest createExpectedAttemptRequest() {
        CreationAttemptRequest creationAttemptRequest = new CreationAttemptRequest();
        creationAttemptRequest.setAttempt(EXPECTED_INPUT_AS_STRING);
        return creationAttemptRequest;
    }

}
