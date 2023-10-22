package hexlet.code.dto.TaskStatus;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TaskStatusCreateDTO {

    @NotEmpty
    @Size(min = 1)
    private String name;
}
