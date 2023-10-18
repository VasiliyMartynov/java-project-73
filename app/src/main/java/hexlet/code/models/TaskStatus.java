package hexlet.code.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import java.sql.Timestamp;

@Entity
@Table(name = "statuses")
@Data
public class TaskStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "statuses_generator")
    @SequenceGenerator(name = "statuses_generator", sequenceName = "statuses_seq", allocationSize = 1)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    @Size(min = 1)
    private String name;

    @CreationTimestamp
    private Timestamp createdAt;
}
