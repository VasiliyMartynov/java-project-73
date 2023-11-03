package hexlet.code.models;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import java.util.Date;
import java.util.Set;
import static jakarta.persistence.TemporalType.TIMESTAMP;
import static org.hibernate.annotations.FetchMode.JOIN;

@Entity
@Getter
@Setter
@Table(name = "tasks")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_generator")
//    @SequenceGenerator(name = "task_generator", sequenceName = "task_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_gen")
    @SequenceGenerator(name = "sequence_gen", sequenceName = "task_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    private User author;

    @ManyToOne
    private User executor;

    @ManyToOne
    private TaskStatus taskStatus;

    @NotBlank
    @Size(min = 3)
    private String name;

    private String description;

    @ManyToMany
    @Fetch(JOIN)
    private Set<Label> labels;

    @CreationTimestamp
    @Temporal(TIMESTAMP)
    private Date createdAt;

}
