package hexlet.code.dto.Task;

import hexlet.code.models.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class TaskUpdateDTO {


    @NotBlank
    private String name;
    private String description;
    @NotNull
    private TaskStatus taskStatus;
    @NotNull
    private Set<Integer> labelIds;
    @NotNull
    private int executorId;
    @NotNull
    private int taskStatusId;
}
