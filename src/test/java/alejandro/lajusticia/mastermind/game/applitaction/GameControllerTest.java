package alejandro.lajusticia.mastermind.game.applitaction;

import alejandro.lajusticia.mastermind.game.application.controller.GameController;
import alejandro.lajusticia.mastermind.game.application.request.CreationGameRequest;
import alejandro.lajusticia.mastermind.game.application.response.*;
import alejandro.lajusticia.mastermind.game.domain.enumeration.FeedbackColor;
import alejandro.lajusticia.mastermind.game.domain.enumeration.GuessColor;
import alejandro.lajusticia.mastermind.game.domain.model.Attempt;
import alejandro.lajusticia.mastermind.game.domain.model.FeedbackBall;
import alejandro.lajusticia.mastermind.game.domain.model.Game;
import alejandro.lajusticia.mastermind.game.domain.model.GuessBall;
import alejandro.lajusticia.mastermind.game.domain.model.exception.EmptyInputException;
import alejandro.lajusticia.mastermind.game.domain.model.exception.ModelException;
import alejandro.lajusticia.mastermind.game.domain.service.GameService;
import alejandro.lajusticia.mastermind.game.domain.service.exception.GameNotFoundException;
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
class GameControllerTest {

    private final List<GuessBall> EXPECTED_SECRET = Arrays.asList(
            new GuessBall(GuessColor.BLUE),
            new GuessBall(GuessColor.ORANGE),
            new GuessBall(GuessColor.GREEN),
            new GuessBall(GuessColor.RED)
    );

    private final List<GuessBallResponse> EXPECTED_SECRET_RESPONSE = Arrays.asList(
            new GuessBallResponse(new GuessBall(GuessColor.BLUE)),
            new GuessBallResponse(new GuessBall(GuessColor.ORANGE)),
            new GuessBallResponse(new GuessBall(GuessColor.GREEN)),
            new GuessBallResponse(new GuessBall(GuessColor.RED))
    );

    private List<GuessBall> EXPECTED_INPUT = Arrays.asList(
            new GuessBall(GuessColor.BLUE),
            new GuessBall(GuessColor.YELLOW),
            new GuessBall(GuessColor.PURPLE),
            new GuessBall(GuessColor.GREEN)
    );

    private List<GuessBallResponse> EXPECTED_INPUT_RESPONSE = Arrays.asList(
            new GuessBallResponse(new GuessBall(GuessColor.BLUE)),
            new GuessBallResponse(new GuessBall(GuessColor.YELLOW)),
            new GuessBallResponse(new GuessBall(GuessColor.PURPLE)),
            new GuessBallResponse(new GuessBall(GuessColor.GREEN))
    );

    private final List<FeedbackBall> EXPECTED_FEEDBACK = Arrays.asList(
            new FeedbackBall(FeedbackColor.BLACK),
            new FeedbackBall(FeedbackColor.WHITE)
    );

    private final List<FeedbackBall> EXPECTED_CORRECT_FEEDBACK = Arrays.asList(
            new FeedbackBall(FeedbackColor.BLACK),
            new FeedbackBall(FeedbackColor.BLACK),
            new FeedbackBall(FeedbackColor.BLACK),
            new FeedbackBall(FeedbackColor.BLACK)
    );

    @Mock
    private GameService gameService;

    @InjectMocks
    private GameController gameController;

    @Test
    void createGame_OK() throws ModelException {
        String expectedId = "ID";
        int expectedAttempts = 10;
        CreationGameRequest request = new CreationGameRequest();
        request.setMaxAttempts(expectedAttempts);
        Game expectedGame = getExpectedGame(expectedId, expectedAttempts);

        when(gameService.createGame(expectedAttempts))
                .thenReturn(expectedGame);

        ResponseEntity response = gameController.createGame(request);

        verify(gameService, times(1))
                .createGame(expectedAttempts);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof CreationGameResponse);

        CreationGameResponse creationGameResponse = (CreationGameResponse) response.getBody();
        assertEquals(expectedId, creationGameResponse.getId());
        assertEquals(expectedAttempts, creationGameResponse.getMaxAttempts());
    }

    @Test
    void createGame_KO_400() throws ModelException {
        int expectedAttempts = 10;
        CreationGameRequest request = new CreationGameRequest();
        request.setMaxAttempts(expectedAttempts);

        when(gameService.createGame(expectedAttempts))
                .thenThrow(new EmptyInputException());

        ResponseEntity response = gameController.createGame(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ErrorResponse);

        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getCode());
        assertFalse(errorResponse.getDescription().isEmpty());
    }

    @Test
    void createGame_KO_500() throws ModelException {
        int expectedAttempts = 10;
        CreationGameRequest request = new CreationGameRequest();
        request.setMaxAttempts(expectedAttempts);

        when(gameService.createGame(expectedAttempts))
                .thenThrow(new NullPointerException());

        ResponseEntity response = gameController.createGame(request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ErrorResponse);

        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse.getCode());
        assertFalse(errorResponse.getDescription().isEmpty());
    }

    @Test
    void getGame_OK() throws ModelException, RepositoryException, GameNotFoundException {
        String expectedId = "ID";
        int expectedAttempts = 10;

        Game expectedGame = getExpectedGame(expectedId, expectedAttempts);
        expectedGame.addAttempt(new Attempt(EXPECTED_INPUT, EXPECTED_FEEDBACK));
        expectedGame.addAttempt(new Attempt(EXPECTED_INPUT, EXPECTED_FEEDBACK));

        when(gameService.getGame(expectedId))
                .thenReturn(expectedGame);

        ResponseEntity response = gameController.getGame(expectedId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof GetGameResponse);

        GetGameResponse getGameResponse = (GetGameResponse) response.getBody();
        assertEquals(expectedId, getGameResponse.getId());
        assertEquals(expectedAttempts, getGameResponse.getMaxAttempts());
        assertFalse(getGameResponse.isSolved());
        assertFalse(getGameResponse.isEnded());
        assertEquals(2, getGameResponse.getCurrentAttempt());
        assertEquals(2, getGameResponse.getAttempts().size());

        validateAttempt(getGameResponse.getAttempts().get(0));
        validateAttempt(getGameResponse.getAttempts().get(1));
    }

    @Test
    void getGame_OK_solved() throws ModelException, RepositoryException, GameNotFoundException {
        String expectedId = "ID";
        int expectedAttempts = 10;

        Game expectedGame = getExpectedGame(expectedId, expectedAttempts);
        expectedGame.addAttempt(new Attempt(EXPECTED_INPUT, EXPECTED_FEEDBACK));
        expectedGame.addAttempt(new Attempt(EXPECTED_SECRET, EXPECTED_CORRECT_FEEDBACK));

        when(gameService.getGame(expectedId))
                .thenReturn(expectedGame);

        ResponseEntity response = gameController.getGame(expectedId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof GetGameResponse);

        GetGameResponse getGameResponse = (GetGameResponse) response.getBody();
        assertEquals(expectedId, getGameResponse.getId());
        assertEquals(expectedAttempts, getGameResponse.getMaxAttempts());
        assertTrue(getGameResponse.isSolved());
        assertFalse(getGameResponse.isEnded());
        assertEquals(2, getGameResponse.getCurrentAttempt());
        assertEquals(2, getGameResponse.getAttempts().size());

        validateAttempt(getGameResponse.getAttempts().get(0));

        assertEquals(EXPECTED_SECRET_RESPONSE, getGameResponse.getAttempts().get(1).getInput());
        assertEquals(4, getGameResponse.getAttempts().get(1).getFeedback().size());
        assertEquals(
                new FeedbackBall(FeedbackColor.BLACK).getColor(),
                getGameResponse.getAttempts().get(1).getFeedback().get(0).getColor()
        );
        assertEquals(
                new FeedbackBall(FeedbackColor.BLACK).getColor(),
                getGameResponse.getAttempts().get(1).getFeedback().get(1).getColor()
        );
        assertEquals(
                new FeedbackBall(FeedbackColor.BLACK).getColor(),
                getGameResponse.getAttempts().get(1).getFeedback().get(2).getColor()
        );
        assertEquals(
                new FeedbackBall(FeedbackColor.BLACK).getColor(),
                getGameResponse.getAttempts().get(1).getFeedback().get(3).getColor()
        );
    }

    @Test
    void getGame_OK_ended() throws ModelException, RepositoryException, GameNotFoundException {
        String expectedId = "ID";
        int expectedAttempts = 2;

        Game expectedGame = getExpectedGame(expectedId, expectedAttempts);
        expectedGame.addAttempt(new Attempt(EXPECTED_INPUT, EXPECTED_FEEDBACK));
        expectedGame.addAttempt(new Attempt(EXPECTED_INPUT, EXPECTED_FEEDBACK));

        when(gameService.getGame(expectedId))
                .thenReturn(expectedGame);

        ResponseEntity response = gameController.getGame(expectedId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof GetGameResponse);

        GetGameResponse getGameResponse = (GetGameResponse) response.getBody();
        assertEquals(expectedId, getGameResponse.getId());
        assertEquals(expectedAttempts, getGameResponse.getMaxAttempts());
        assertFalse(getGameResponse.isSolved());
        assertTrue(getGameResponse.isEnded());
        assertEquals(2, getGameResponse.getCurrentAttempt());
        assertEquals(2, getGameResponse.getAttempts().size());

        validateAttempt(getGameResponse.getAttempts().get(0));
        validateAttempt(getGameResponse.getAttempts().get(1));
    }

    @Test
    void getGame_KO_404() throws RepositoryException, GameNotFoundException {
        String expectedId = "ID";

        when(gameService.getGame("ID"))
                .thenThrow(new GameNotFoundException(expectedId));

        ResponseEntity response = gameController.getGame(expectedId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ErrorResponse);

        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), errorResponse.getCode());
        assertFalse(errorResponse.getDescription().isEmpty());
    }

    @Test
    void getGame_KO_500_RepositoryException() throws RepositoryException, GameNotFoundException {
        String expectedId = "ID";

        when(gameService.getGame("ID"))
                .thenThrow(new GameUnavailableException());

        ResponseEntity response = gameController.getGame(expectedId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ErrorResponse);

        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse.getCode());
        assertFalse(errorResponse.getDescription().isEmpty());
    }

    @Test
    void getGame_KO_500() throws RepositoryException, GameNotFoundException {
        String expectedId = "ID";

        when(gameService.getGame("ID"))
                .thenThrow(new NullPointerException());

        ResponseEntity response = gameController.getGame(expectedId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ErrorResponse);

        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse.getCode());
        assertFalse(errorResponse.getDescription().isEmpty());
    }

    private Game getExpectedGame(String id, int maxAttempts) throws ModelException {
        return new Game(id, EXPECTED_SECRET, maxAttempts);
    }

    private void validateAttempt(AttemptResponse attempt) {
        assertEquals(EXPECTED_INPUT_RESPONSE, attempt.getInput());
        assertEquals(2, attempt.getFeedback().size());
        assertEquals(new FeedbackBall(FeedbackColor.BLACK).getColor(), attempt.getFeedback().get(0).getColor());
        assertEquals(new FeedbackBall(FeedbackColor.WHITE).getColor(), attempt.getFeedback().get(1).getColor());
    }

}
