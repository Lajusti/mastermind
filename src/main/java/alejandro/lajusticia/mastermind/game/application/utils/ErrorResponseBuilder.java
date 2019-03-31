package alejandro.lajusticia.mastermind.game.application.utils;

import alejandro.lajusticia.mastermind.game.application.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ErrorResponseBuilder {

    public static ResponseEntity<ErrorResponse> buildErrorResponse(String description, HttpStatus httpstatus) {
        return new ResponseEntity<>(
                new ErrorResponse(description, httpstatus.value()),
                httpstatus
        );
    }

    public static ResponseEntity<ErrorResponse> buildUnnexpectedError() {
        return buildErrorResponse(
                "An error occurred and the game cannot be created, our technical " +
                        "team will try to solve the problem as soon possible",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    private ErrorResponseBuilder() {

    }

}
