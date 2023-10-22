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
@Table(name = "statuses")
@Data
@EqualsAndHashCode
public class TaskStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "status_generator")
    @SequenceGenerator(name = "status_generator", sequenceName = "status_seq", allocationSize = 1)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    @Size(min = 1)
    private String name;

    @CreationTimestamp
    private Timestamp createdAt;
}
