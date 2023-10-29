package hexlet.code.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "tasks")
@Data
@EqualsAndHashCode
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_generator")
    @SequenceGenerator(name = "task_generator", sequenceName = "task_seq", allocationSize = 1)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private Long id;

    @NotNull
    @OneToOne
    @JoinColumn(name = "author_id")
    private User author;

    @NotNull
    @OneToOne
//    @JoinColumn(name = "executor_id")
    private User executor;

    @NotNull
    @OneToOne
    @JoinColumn(name = "taskStatus_id")
    private TaskStatus taskStatus;

    @NotBlank
    @Size(min = 1)
    private String name;

    private String description;

    @ManyToMany
    @JoinTable(
            name = "Task_Label",
            joinColumns = { @JoinColumn(name = "task_id") },
            inverseJoinColumns = { @JoinColumn(name = "label_id") }
    )
    @NotNull
    private Set<Label> labels;

    @CreationTimestamp
    private Timestamp createdAt;

}
