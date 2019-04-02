package alejandro.lajusticia.mastermind.game.application.response;

import alejandro.lajusticia.mastermind.game.domain.model.GuessBall;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class GuessBallResponse {

    @ApiModelProperty(notes = "Guess ball of the attempt")
    private String color;

    public GuessBallResponse(GuessBall guessBall) {
        color = guessBall.getColor();
    }

}
