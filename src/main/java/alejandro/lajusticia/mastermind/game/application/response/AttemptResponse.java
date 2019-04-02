package alejandro.lajusticia.mastermind.game.application.response;

import alejandro.lajusticia.mastermind.game.domain.model.FeedbackBall;
import alejandro.lajusticia.mastermind.game.domain.model.GuessBall;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class AttemptResponse {

    @ApiModelProperty(notes = "Input of attempt")
    private List<GuessBallResponse> input;

    @ApiModelProperty(notes = "Feedback of attempt")
    private List<FeedbackBallResponse> feedback;

    public AttemptResponse(List<GuessBall> input, List<FeedbackBall> feedback) {
        this.input = input.stream()
                .map(GuessBallResponse::new)
                .collect(Collectors.toList());
        this.feedback = feedback.stream()
                .map(FeedbackBallResponse::new)
                .collect(Collectors.toList());
    }

}
