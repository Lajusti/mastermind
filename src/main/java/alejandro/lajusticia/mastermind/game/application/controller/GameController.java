package alejandro.lajusticia.mastermind.game.application.controller;

import alejandro.lajusticia.mastermind.game.application.mapper.AttemptResponseMapper;
import alejandro.lajusticia.mastermind.game.application.request.CreationGameRequest;
import alejandro.lajusticia.mastermind.game.application.response.CreationGameResponse;
import alejandro.lajusticia.mastermind.game.application.response.GetGameResponse;
import alejandro.lajusticia.mastermind.game.domain.model.Game;
import alejandro.lajusticia.mastermind.game.domain.model.exception.ModelException;
import alejandro.lajusticia.mastermind.game.domain.service.GameService;
import alejandro.lajusticia.mastermind.game.domain.service.exception.GameNotFoundException;
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

import java.util.stream.Collectors;

import static alejandro.lajusticia.mastermind.game.application.utils.ErrorResponseBuilder.buildErrorResponse;
import static alejandro.lajusticia.mastermind.game.application.utils.ErrorResponseBuilder.buildUnnexpectedError;

@Slf4j
@Api(value = "Attempt", description = "Operations pertaining to Mastermind attempt of the game")
@RestController
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;

    public GameController(final GameService gameService) {
        this.gameService = gameService;
    }

    @ApiOperation(value = "Create a game in the system", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully."),
            @ApiResponse(code = 400, message = "Wrong request."),
            @ApiResponse(code = 500, message = "Unexpected error.")
        }
    )
    @PostMapping
    public ResponseEntity createGame(@Valid @RequestBody CreationGameRequest gameRequest) {
        try {
            Game game = gameService.createGame(gameRequest.getMaxAttempts());
            return new ResponseEntity<>(
                    new CreationGameResponse(game.getUuid(), game.getMaxAttempts()),
                    HttpStatus.CREATED
            );
        } catch (ModelException e) {
            log.info("Error at create some game: ", e);
            return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Unexpected error on create game with the request: " + gameRequest.toString(), e);
            return buildUnnexpectedError();
        }
    }

    @ApiOperation(value = "Get a game of the system", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok."),
            @ApiResponse(code = 404, message = "Game not found."),
            @ApiResponse(code = 500, message = "Unexpected error.")
        }
    )
    @GetMapping(path = "/{id}")
    public ResponseEntity getGame(@PathVariable String id) {
        try {
            Game game = gameService.getGame(id);
            return new ResponseEntity<>(
                    new GetGameResponse(
                            game.getUuid(),
                            game.getMaxAttempts(),
                            game.isSolved(),
                            game.isEnded(),
                            game.getAttempts().size(),
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
            log.error("Unexpected error on get game with the id: " + id, e);
            return buildUnnexpectedError();
        }
    }

}
