package alejandro.lajusticia.mastermind.game.application.response;

import alejandro.lajusticia.mastermind.game.domain.model.Attempt;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class GetGameResponse extends CreationGameResponse {

    @ApiModelProperty(notes = "Number of the current attempt")
    private final int currentAttempt;

    @ApiModelProperty(notes = "List of the attempts")
    private final List<Attempt> attempts;

    public GetGameResponse(String id, int maxAttempts, List<Attempt> attempts) {
        super(id, maxAttempts);
        currentAttempt = attempts.size();
        this.attempts = attempts;
    }

}
