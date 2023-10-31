package hexlet.code.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.FetchType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.util.Date;
import java.util.Set;

import static jakarta.persistence.TemporalType.TIMESTAMP;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "labels")
public class Label {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "label_generator")
    @SequenceGenerator(name = "label_generator", sequenceName = "label_seq", allocationSize = 1)
    private Long id;

    @NotBlank
    @Size(min = 3)
    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "labels", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Task> tasks;

    @CreationTimestamp
    @Temporal(TIMESTAMP)
    private Date createdAt;

    public Label(final Long id) {
        this.id = id;
    }
}
