package hexlet.code.controllers;

import hexlet.code.dto.user.UserDTO;
import hexlet.code.models.user.User;
import hexlet.code.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    //GET USER BY ID
    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    UserDTO getUser(@PathVariable long id) {
            return userService.getUser(id);
    }
    //GET USERS
    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    List<UserDTO> getUsers() {
        return userService.getUsers();
    }
    //CREATE USER
    @PostMapping(
            value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UserDTO createUser(@RequestBody User user) {
        userService.createUser(user);
        return userService.findUserByEmail(user.getEmail());

    }

    //DELETE USER BY ID
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
    }

    //UPDATE USER BY ID
    @PutMapping(
            value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UserDTO updateUser(@PathVariable long id, @RequestBody User user) {
        userService.updateUser(id, user);
        return userService.findUserByEmail(user.getEmail());

    }


}