package hexlet.code.controllers;

import hexlet.code.dto.User.UserCreateDTO;
import hexlet.code.dto.User.UserShowDTO;
import hexlet.code.dto.User.UserUpdateDTO;
import hexlet.code.models.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import java.util.List;
import static hexlet.code.controllers.UserController.USER_CONTROLLER_PATH;

@RestController
@AllArgsConstructor
@RequestMapping("${base-url}" + USER_CONTROLLER_PATH)
public class UserController {

    public static final String USER_CONTROLLER_PATH = "/users";
    public static final String ID = "/{id}";
    private static final String ONLY_OWNER_BY_ID = """
            @userRepository.findById(#id).get().getEmail() == authentication.getName()
        """;
    private final UserService userService;
    private final UserRepository userRepository;


    //GET userDTO BY ID
    @GetMapping(ID)
    @ResponseStatus(HttpStatus.OK)
    UserShowDTO getUser(@PathVariable long id) {
        return userService.getUser(id);
    }

    @GetMapping(path = "/secret/{id}")
    @ResponseStatus(HttpStatus.OK)
    User getSecret(@PathVariable long id) {
        return userRepository.findById(id).orElseThrow();
    }

    //GET USERS
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    List<UserShowDTO> getUsers() {
        return userService.getUsers();
    }

    //CREATE USER
    @PostMapping(
            value = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    UserShowDTO createUser(@Valid @RequestBody UserCreateDTO u) throws Exception {
        return userService.createUser(u);
    }

    //DELETE USER BY ID
    @DeleteMapping(ID)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize(ONLY_OWNER_BY_ID)
    public void deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
    }

    //UPDATE USER BY ID
    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize(ONLY_OWNER_BY_ID)
    public UserShowDTO updateUser(@PathVariable long id, @RequestBody UserUpdateDTO user) throws Exception {
        return userService.updateUser(id, user);
    }
}
