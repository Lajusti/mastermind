package alejandro.lajusticia.mastermind.game.application.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreationAttemptResponse {

    @ApiModelProperty(notes = "Feedback of the attempt")
    private List<FeedbackBallResponse> feedback;

}
