package hexlet.code.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.HashSet;
import java.util.Set;

@Data

public class TaskCreateDTO {

    @NotEmpty
    @Size(min = 1)
    private String name;

    private String description;

    private int executorId;

    private int taskStatusId;

    private Set<Integer> labelsId = new HashSet<>();

}
