package hexlet.code.services;

import hexlet.code.dto.User.UserCreateDTO;
import hexlet.code.dto.User.UserShowDTO;
import hexlet.code.dto.User.UserUpdateDTO;
import hexlet.code.exceptions.ResourceNotFoundException;
import hexlet.code.dto.Mappers.UserMapper;
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

    public UserShowDTO getUser(long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(id + " not found")
        );
        return userMapper.INSTANCE.showUser(user);
    }

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

    public UserShowDTO createUser(UserCreateDTO newUser) throws Exception {
        if (userRepository.findByEmail(newUser.getEmail()).isPresent()) {
            throw new Exception(
                    "There is an account with that email address:" + newUser.getEmail());
        }
        try {
            var user = userMapper.INSTANCE.createUser(newUser);
            user.setPassword(passwordEncoder.encode(newUser.getPassword()));
//            user.setPassword(newUser.getPassword());
            userRepository.save(user);
            userRepository.flush();
            return userMapper.INSTANCE.showUser(user);
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

    public UserShowDTO updateUser(long id, UserUpdateDTO dataThatShouldBeUpdated) {
        try {
            var user = userRepository.findById(id).orElseThrow();
            userMapper.INSTANCE.updateUser(dataThatShouldBeUpdated, user);
            userRepository.save(user);
            return userMapper.INSTANCE.showUser(user);
        } catch (NoSuchElementException ex) {
            throw new NoSuchElementException(id + " not found");
        }
    }

    public String getCurrentUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }


    public User getCurrentUser() {
        return userRepository.findByEmail(getCurrentUserName()).get();
    }

}