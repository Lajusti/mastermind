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
public class ErrorResponse {

    @ApiModelProperty(notes = "Description of the error")
    private String description;

    @ApiModelProperty(notes = "Code of error")
    private int code;

}
