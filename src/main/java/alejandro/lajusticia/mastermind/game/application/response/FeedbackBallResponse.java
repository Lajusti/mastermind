package alejandro.lajusticia.mastermind.game.application.response;

import alejandro.lajusticia.mastermind.game.domain.model.FeedbackBall;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class FeedbackBallResponse {

    @ApiModelProperty(notes = "Feed ball of the attempt")
    private String color;

    public FeedbackBallResponse(FeedbackBall feedbackBall) {
        color = feedbackBall.getColor();
    }

}
