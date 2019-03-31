package alejandro.lajusticia.mastermind.game.application.response;

import alejandro.lajusticia.mastermind.game.domain.model.FeedbackBall;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CreationAttemptResponse {

    @ApiModelProperty(notes = "Feedback of the attempt")
    private final List<FeedbackBall> feedback;

}
