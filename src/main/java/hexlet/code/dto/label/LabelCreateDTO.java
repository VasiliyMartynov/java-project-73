package hexlet.code.dto.label;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LabelCreateDTO {

    @NotEmpty
    @Size(min = 1)
    private String name;
}
