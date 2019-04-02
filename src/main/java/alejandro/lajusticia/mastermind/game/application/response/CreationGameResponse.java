package alejandro.lajusticia.mastermind.game.application.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreationGameResponse {

    @ApiModelProperty(notes = "Id of the game created")
    private String id;

    @ApiModelProperty(notes = "Number of the max attempts of the game created")
    private int maxAttempts;

}
