package hexlet.code.services;

import hexlet.code.dto.UserDTO;
import hexlet.code.exceptions.ResourceNotFoundException;
import hexlet.code.mapper.UserMapper;
import hexlet.code.models.User;
import hexlet.code.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private static UserMapper userMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDTO getUser(long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(id + " not found"));
        return convertUserToUserDTO(user);
    }

    public List<UserDTO> getUsers() {
        try {
            List<User> users = userRepository.findAll();
            List<UserDTO> usersDTO = users
                    .stream()
                    .map(u -> userMapper.INSTANCE.userToUserDTO(u))
                    .collect(Collectors.toList());
            return usersDTO;
        } catch (NullPointerException e) {
            throw new NullPointerException("There are no any user");
        }
    }

    public UserDTO createUser(User newUser) throws Exception {
        if (userRepository.findByEmail(newUser.getEmail()).isPresent()) {
            throw new Exception(
                    "There is an account with that email address:" + newUser.getEmail());
        }
        try {
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
            userRepository.save(newUser);
            userRepository.flush();
            return getUser(newUser.getId());
        } catch (Exception e) {
            throw new Exception("Something where wrong");
        }
    }

    public void deleteUser(long id) {
        try {
            Optional<User> user = userRepository.findById(id);
            userRepository.deleteById(user.get().getId());
        } catch (NoSuchElementException ex) {
            throw new NoSuchElementException(id + " not found");
        }
    }

    public UserDTO updateUser(long id, User newUser) {
        try {
            User user = userRepository.findById(id).orElseThrow();
            user.setEmail(newUser.getEmail());
            user.setFirstName(newUser.getFirstName());
            user.setLastName(newUser.getLastName());
            user.setPassword(newUser.getPassword());
            userRepository.save(user);
            return convertUserToUserDTO(user);
        } catch (NoSuchElementException ex) {
            throw new NoSuchElementException(id + " not found");
        }
    }

    //CONVERT user to userDTO
    public static UserDTO convertUserToUserDTO(User user) {
        return userMapper.INSTANCE.userToUserDTO(user);
    }
}
