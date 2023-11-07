package hexlet.code.dto.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDTO {
    @Email
    private String email;

    @NotEmpty
    @Size(min = 3)
    private String firstName;

    @NotBlank
    @Size(min = 3)
    private String lastName;

    @NotBlank
    @Size(min = 3)
    private String password;
}
