package alejandro.lajusticia.mastermind.game.application.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GetGameResponse extends CreationGameResponse {

    @ApiModelProperty(notes = "The game is solved")
    private boolean solved;

    @ApiModelProperty(notes = "The game is ended")
    private boolean ended;

    @ApiModelProperty(notes = "Number of the current attempt")
    private int currentAttempt;

    @ApiModelProperty(notes = "List of the attempts")
    private List<AttemptResponse> attempts;

    public GetGameResponse(
            String id,
            int maxAttempts,
            boolean solved,
            boolean ended,
            int currentAttempt,
            List<AttemptResponse> attempts)
    {
        super(id, maxAttempts);
        this.solved = solved;
        this.ended = ended;
        this.currentAttempt = currentAttempt;
        this.attempts = attempts;
    }

}
