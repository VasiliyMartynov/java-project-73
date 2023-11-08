package hexlet.code.dto.taskStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TaskStatusUpdateDTO {

    @NotBlank
    @Size(min = 1)
    private String name;

}
