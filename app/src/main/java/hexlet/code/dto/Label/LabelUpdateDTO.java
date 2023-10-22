package hexlet.code.dto.Label;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LabelUpdateDTO {

    @NotBlank
    @Size(min = 1)
    private String name;

}
