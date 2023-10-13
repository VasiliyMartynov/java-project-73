package hexlet.code.controllers;

import hexlet.code.dto.UserDTO;
import hexlet.code.models.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    //GET userDTO BY ID
    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    UserDTO getUser(@PathVariable long id) {
        return userService.getUser(id);
    }

    @GetMapping(path = "/secret/{id}")
    @ResponseStatus(HttpStatus.OK)
    User getSecret(@PathVariable long id) {
        return userRepository.findById(id).orElseThrow();
    }

    //get user POJO
//    @GetMapping(path = "/secret/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    User getALldata(@PathVariable long id) {
//        return userService.getUserAllData(id);
//    }

    //GET USERS
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    List<UserDTO> getUsers() {
        return userService.getUsers();
    }

    //CREATE USER
    @PostMapping(
            value = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    UserDTO createUser(@Valid @RequestBody User user) throws Exception {
        return userService.createUser(user);
    }

    //DELETE USER BY ID
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
    }

    //UPDATE USER BY ID
    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UserDTO updateUser(@PathVariable long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }
}
