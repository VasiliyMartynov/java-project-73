package hexlet.code.dto.Label;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LabelUpdateDTO {

    @NotBlank
    @Size(min = 1)
    private String name;

}
