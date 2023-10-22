package hexlet.code.dto.Task;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class TaskCreateDTO {

    @NotEmpty
    @Size(min = 1)
    private String name;

    private String description;

    @NotNull
    private int executorId;

    @NotNull
    private int taskStatusId;

    private Set<Integer> labelIds = new HashSet<>();
}
