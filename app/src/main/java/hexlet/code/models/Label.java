package hexlet.code.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Date;
import java.sql.Timestamp;


@Entity
@Table(name = "labels")
@Data
@EqualsAndHashCode
public class Label {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "label_generator")
    @SequenceGenerator(name = "label_generator", sequenceName = "label_seq", allocationSize = 1)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    @Size(min = 1)
    private String name;

    @CreationTimestamp
    private Timestamp createdAt;

//    @ManyToMany(mappedBy = "labels")
//    private Set<Task> tasks = new HashSet<>();
}
