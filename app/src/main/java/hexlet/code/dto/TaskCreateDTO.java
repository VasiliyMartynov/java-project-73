package hexlet.code.dto;

import hexlet.code.models.Label;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class TaskCreateDTO {

    @NotEmpty
    @Size(min = 1)
    private String name;

    private String description;

    private int executorId;

    private int taskStatusId;

    private Set<Label> labels;

}
