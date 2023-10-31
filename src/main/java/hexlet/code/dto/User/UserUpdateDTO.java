package hexlet.code.dto.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
//import org.openapitools.jackson.nullable.JsonNullable;

@Data
public class UserUpdateDTO {

//    @Email
//    private JsonNullable<String> email;
//
//    @NotEmpty
//    @Size(min = 1)
//    private JsonNullable<String> firstName;
//
//    @NotBlank
//    @Size(min = 1)
//    private JsonNullable<String> lastName;
//
//    @NotBlank
//    @Size(min = 3)
//    private JsonNullable<String> password;
    @Email
    private String email;

    @NotEmpty
    @Size(min = 1)
    private String firstName;

    @NotBlank
    @Size(min = 1)
    private String lastName;

    @NotBlank
    @Size(min = 3)
    private String password;
}
