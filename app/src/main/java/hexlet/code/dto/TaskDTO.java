package hexlet.code.dto;

import hexlet.code.models.Label;
import hexlet.code.models.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.sql.Timestamp;
import java.util.Set;

@Data
public class TaskDTO {

    private Long id;
    @NotBlank
    private String name;
    private String description;
    private TaskStatus taskStatus;
    private UserDTO author;
    private UserDTO executor;
    private Set<Label> labels;
    private Timestamp createdAt;
}
