package hexlet.code.service;

import hexlet.code.dto.user.UserCreateDTO;
import hexlet.code.dto.user.UserShowDTO;
import hexlet.code.dto.user.UserUpdateDTO;
import hexlet.code.exceptions.ResourceNotFoundException;
import hexlet.code.mappers.UserMapper;
import hexlet.code.models.User;
import hexlet.code.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class UserService {
    private static UserMapper userMapper;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    /**
     * getUser return exist UserShowDTO object of User model.
     * @param id
     * @return UserShowDTO
     */
    public UserShowDTO getUser(long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(id + " not found")
        );
        return userMapper.INSTANCE.showUser(user);
    }
    /**
     * getUsers return list of UserShowDTO objects of User model.
     * @return UserShowDTO
     */
    public List<UserShowDTO> getUsers() {
        try {
            List<User> users = userRepository.findAll();
            return users
                    .stream()
                    .map(u -> userMapper.INSTANCE.showUser(u))
                    .collect(Collectors.toList());
        } catch (NullPointerException e) {
            throw new NullPointerException("There are no any user");
        }
    }
    /**
     * createUser create new User object and save it to database. Return UserShowDTO object of User model.
     * @param newUser
     * @return UserShowDTO
     */
    public UserShowDTO createUser(UserCreateDTO newUser) throws Exception {
        if (userRepository.findByEmail(newUser.getEmail()).isPresent()) {
            throw new Exception(
                    "There is an account with that email address:" + newUser.getEmail());
        }
        try {
            var user = userMapper.INSTANCE.createUser(newUser);
            user.setPassword(passwordEncoder.encode(newUser.getPassword()));
            userRepository.save(user);
            userRepository.flush();
            return userMapper.INSTANCE.showUser(user);
        } catch (Exception e) {
            throw new Exception("Something where wrong");
        }

    }
    /**
     * deleteUser delete exist User object from database. Return void.
     * @param id
     */
    public void deleteUser(long id) {
        try {
            Optional<User> user = userRepository.findById(id);
            userRepository.deleteById(user.get().getId());
        } catch (NoSuchElementException ex) {
            throw new NoSuchElementException(id + " not found");
        }
    }
    /**
     * updateUser update  exist User object in database. Return void UserShowDTO object of User model.
     * @param id
     * @param dataThatShouldBeUpdated
     * @return UserShowDTO
     */
    public UserShowDTO updateUser(long id, UserUpdateDTO dataThatShouldBeUpdated) {
        try {
            var user = userRepository.findById(id).orElseThrow();
            userMapper.INSTANCE.updateUser(dataThatShouldBeUpdated, user);
            user.setPassword(passwordEncoder.encode(dataThatShouldBeUpdated.getPassword()));
            userRepository.save(user);
            return userMapper.INSTANCE.showUser(user);
        } catch (NoSuchElementException ex) {
            throw new NoSuchElementException(id + " not found");
        }
    }

    /**
     * getCurrentUserName return authenticated username from security context.
     * @return String
     */
    public String getCurrentUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
    /**
     * getCurrentUser return authenticated user object from user repository.
     * @return User
     */
    public User getCurrentUser() {
        return userRepository.findByEmail(getCurrentUserName()).get();
    }
}
