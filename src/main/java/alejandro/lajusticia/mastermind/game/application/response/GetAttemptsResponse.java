package alejandro.lajusticia.mastermind.game.application.response;

import alejandro.lajusticia.mastermind.game.domain.model.Attempt;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class GetAttemptsResponse {

    @ApiModelProperty(notes = "List of the attempts")
    private final List<Attempt> attempts;

    public GetAttemptsResponse(List<Attempt> attempts) {
        this.attempts = attempts;
    }

}
