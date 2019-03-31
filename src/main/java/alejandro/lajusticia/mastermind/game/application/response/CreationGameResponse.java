package alejandro.lajusticia.mastermind.game.application.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreationGameResponse {

    @ApiModelProperty(notes = "Id of the game created")
    private final String id;

    @ApiModelProperty(notes = "Number of the max attempts of the game created")
    private final int maxAttempts;

}
