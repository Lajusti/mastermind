package alejandro.lajusticia.mastermind.game.application.controller;

import alejandro.lajusticia.mastermind.game.application.mapper.AttemptResponseMapper;
import alejandro.lajusticia.mastermind.game.application.request.CreationAttemptRequest;
import alejandro.lajusticia.mastermind.game.application.response.CreationAttemptResponse;
import alejandro.lajusticia.mastermind.game.application.response.FeedbackBallResponse;
import alejandro.lajusticia.mastermind.game.application.response.GetAttemptsResponse;
import alejandro.lajusticia.mastermind.game.domain.model.Attempt;
import alejandro.lajusticia.mastermind.game.domain.model.Game;
import alejandro.lajusticia.mastermind.game.domain.model.exception.GameIsOverException;
import alejandro.lajusticia.mastermind.game.domain.model.exception.GameIsSolvedException;
import alejandro.lajusticia.mastermind.game.domain.model.exception.ModelException;
import alejandro.lajusticia.mastermind.game.domain.service.GameService;
import alejandro.lajusticia.mastermind.game.domain.service.exception.GameNotFoundException;
import alejandro.lajusticia.mastermind.game.domain.service.exception.WrongAttemptLengthException;
import alejandro.lajusticia.mastermind.game.infrastructure.repository.exception.RepositoryException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.stream.Collectors;

import static alejandro.lajusticia.mastermind.game.application.utils.ErrorResponseBuilder.buildErrorResponse;
import static alejandro.lajusticia.mastermind.game.application.utils.ErrorResponseBuilder.buildUnnexpectedError;
import static alejandro.lajusticia.mastermind.game.domain.enumeration.utils.ColorConverter.convertGuessStringToGuessBall;
import static alejandro.lajusticia.mastermind.game.domain.enumeration.utils.ColorConverter.getAllColorAvailable;

@Slf4j
@Api(value = "Game", description = "Operations pertaining to Mastermind game")
@RestController
@RequestMapping("/games/{id}/attempts")
public class AttemptController {

    private final GameService gameService;

    public AttemptController(final GameService gameService) {
        this.gameService = gameService;
    }

    @ApiOperation(value = "Get the attempts of a game", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok."),
            @ApiResponse(code = 404, message = "Game not found."),
            @ApiResponse(code = 500, message = "Unexpected error.")
        }
    )
    @GetMapping
    public ResponseEntity getAttempts(@PathVariable String id) {
        try {
            Game game = gameService.getGame(id);
            return new ResponseEntity<>(
                    new GetAttemptsResponse(
                            game.getAttempts().stream()
                                    .map(AttemptResponseMapper::domainToResponse)
                                    .collect(Collectors.toList())
                    ),
                    HttpStatus.OK
            );
        } catch (GameNotFoundException e) {
            log.info("Error at getting the game with id: " + id, e);
            return buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RepositoryException e) {
            log.info("Error at getting the game with id: " + id, e);
            return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error("Unexpected error on get attempts of the game with the id: " + id, e);
            return buildUnnexpectedError();
        }
    }

    @ApiOperation(value = "Do a attempts in a game", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok."),
            @ApiResponse(code = 400, message = "Bad request."),
            @ApiResponse(code = 404, message = "Game not found."),
            @ApiResponse(code = 500, message = "Unexpected error.")
        }
    )
    @PutMapping
    public ResponseEntity doAttempts(
            @PathVariable String id,
            @RequestBody @Valid CreationAttemptRequest creationAttemptRequest)
    {
        try {
            Game game = gameService.addAttemptToGameByGameIdAndAttemptInput(
                    id,
                    convertGuessStringToGuessBall(creationAttemptRequest.getAttempt())
            );
            Optional<Attempt> optionalAttempt = game.getLastAttempt();

            if (optionalAttempt.isPresent()) {
                return new ResponseEntity<>(
                        new CreationAttemptResponse(
                                optionalAttempt.get().getFeedback().stream()
                                    .map(FeedbackBallResponse::new)
                                    .collect(Collectors.toList())
                        ),
                        HttpStatus.OK
                );
            }
            log.error("After create the attempt with the request: " + creationAttemptRequest.toString() +
                    ", for the game with id: " + id + " no attempt was set to the game");
            return buildUnnexpectedError();
        } catch (IllegalArgumentException e) {
            log.info("Error (wrong color) at getting the game with id: " + id, e);
            return buildErrorResponse(
                    "Wrong color introduced in your attempt the available values are: " +
                            getAllColorAvailable(),
                    HttpStatus.BAD_REQUEST
            );
        } catch (GameNotFoundException e) {
            log.info("Error at getting the game with id: " + id, e);
            return buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (GameIsOverException | GameIsSolvedException e) {
            log.info("Error at getting the game with id: " + id, e);
            return buildErrorResponse(e.getMessage(), HttpStatus.CONFLICT);
        } catch (ModelException | WrongAttemptLengthException e) {
            log.info("Error at getting the game with id: " + id, e);
            return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RepositoryException e) {
            log.info("Error at getting the game with id: " + id, e);
            return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error("Unexpected error on get attempts of the game with the id: " + id, e);
            return buildUnnexpectedError();
        }
    }

}
