package hexlet.code.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import java.sql.Timestamp;

@Entity
@Table(name = "tasks")
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_generator")
    @SequenceGenerator(name = "task_generator", sequenceName = "task_seq", allocationSize = 1)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private Long id;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "executor_id", referencedColumnName = "id")
    private User executor;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "taskStatus_id", referencedColumnName = "id")
    private TaskStatus taskStatus;

    @NotBlank
    @Size(min = 1)
    private String name;

    private String description;

    @CreationTimestamp
    private Timestamp createdAt;
}
