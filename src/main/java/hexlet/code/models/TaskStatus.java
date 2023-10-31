package hexlet.code.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.util.Date;
import java.util.List;

import static jakarta.persistence.TemporalType.TIMESTAMP;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "statuses")
public class TaskStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "status_generator")
    @SequenceGenerator(name = "status_generator", sequenceName = "status_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Size(min = 3)
    @Column(unique = true)
    private String name;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "taskStatus")
    private List<Task> tasks;


    @CreationTimestamp
    @Temporal(TIMESTAMP)
    private Date createdAt;
}
