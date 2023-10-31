package hexlet.code.dto.Label;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LabelCreateDTO {

    @NotEmpty
    @Size(min = 1)
    private String name;
}
