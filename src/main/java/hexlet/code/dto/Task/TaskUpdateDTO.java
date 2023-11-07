package hexlet.code.dto.Task;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskUpdateDTO {

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
