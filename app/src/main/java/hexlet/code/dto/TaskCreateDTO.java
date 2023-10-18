package hexlet.code.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCreateDTO {

    @NotEmpty
    @Size(min = 1)
    private String name;

    private String description;

    private int executorId;

    private int taskStatusId;

}
