package alejandro.lajusticia.mastermind.game.application.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
public class CreationAttemptRequest {

    @NotNull(message = "Field attempt is required")
    @ApiModelProperty(notes = "Balls of the attempt")
    private List<String> attempt;

}
