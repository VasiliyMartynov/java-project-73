package hexlet.code.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
    @SequenceGenerator(name = "user_generator", sequenceName = "user_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Size(min = 3)
    private String firstName;

    @NotBlank
    @Size(min = 3)
    private String lastName;

    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    @Size(min = 3)
    private String password;

    @CreationTimestamp
    private Date createdAt;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "author")
    private List<Task> listTaskAuthor;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "executor")
    private List<Task> listTaskExecutor;
}
