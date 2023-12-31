package hexlet.code.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import hexlet.code.dto.user.UserShowDTO;
import hexlet.code.models.Label;
import hexlet.code.models.TaskStatus;
import lombok.Data;
import java.sql.Timestamp;
//import java.util.List;
import java.util.Set;

@Data
public class TaskShowDTO {

    private Long id;
    private String name;
    private String description;
    private TaskStatus taskStatus;
    private UserShowDTO author;
    private UserShowDTO executor;
    private Set<Label> labels;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
    private Timestamp createdAt;
}
