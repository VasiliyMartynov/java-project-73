package hexlet.code.services;

import hexlet.code.dto.user.UserCreateDTO;
import hexlet.code.dto.user.UserShowDTO;
import hexlet.code.exceptions.ResourceNotFoundException;
import hexlet.code.dto.user.UserMapper;
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

    public UserShowDTO getUser(long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(id + " not found")
        );
        return userMapper.INSTANCE.toUserShowDTO(user);
    }

    public List<UserShowDTO> getUsers() {
        try {
            List<User> users = userRepository.findAll();
            return users
                    .stream()
                    .map(u -> userMapper.INSTANCE.toUserShowDTO(u))
                    .collect(Collectors.toList());
        } catch (NullPointerException e) {
            throw new NullPointerException("There are no any user");
        }
    }

    public UserShowDTO createUser(UserCreateDTO newUser) throws Exception {
        if (userRepository.findByEmail(newUser.getEmail()).isPresent()) {
            throw new Exception(
                    "There is an account with that email address:" + newUser.getEmail());
        }
        try {
            var user = userMapper.INSTANCE.toUser(newUser);
            user.setPassword(passwordEncoder.encode(newUser.getPassword()));
            userRepository.save(user);
            userRepository.flush();
            return userMapper.INSTANCE.toUserShowDTO(user);
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

    public UserShowDTO updateUser(long id, User newUser) {
        try {
            User user = userRepository.findById(id).orElseThrow();
            user.setEmail(newUser.getEmail());
            user.setFirstName(newUser.getFirstName());
            user.setLastName(newUser.getLastName());
            user.setPassword(newUser.getPassword());
            userRepository.save(user);
            return userMapper.INSTANCE.toUserShowDTO(user);
        } catch (NoSuchElementException ex) {
            throw new NoSuchElementException(id + " not found");
        }
    }
}
