package hexlet.code.dto;

import hexlet.code.models.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Getter
@Setter
public class TaskDTO {


    private Long id;

    @NotBlank
    @Size(min = 1)
    private String name;

    private String description;

    @NotNull
    private TaskStatus taskStatus;

    @NotNull
    private UserDTO author;

    @NotNull
    private UserDTO executor;

    @CreationTimestamp
    private Timestamp createdAt;
}
