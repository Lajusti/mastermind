package alejandro.lajusticia.mastermind.game.application.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    @ApiModelProperty(notes = "Description of the error")
    private final String description;

    @ApiModelProperty(notes = "Code of error")
    private final int code;

}
