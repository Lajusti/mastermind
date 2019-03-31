package alejandro.lajusticia.mastermind.game.application.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class CreationGameRequest {

    @NotNull(message = "Field maxAttepts is required")
    @ApiModelProperty(notes = "Number of the max attempts of the game created")
    private int maxAttempts;

}
