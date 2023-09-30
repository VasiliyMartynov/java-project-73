package hexlet.code.services;

import hexlet.code.dto.user.UserDTO;;
import hexlet.code.exceptions.ResourceNotFoundException;
import hexlet.code.mapper.UserMapper;
import hexlet.code.models.user.User;
import hexlet.code.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRepository userRepository;

    public UserDTO getUser(long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(id + " not found"));
        return userMapper.INSTANCE.userToUerDTO(user);
    }

    public UserDTO findUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        return userMapper.INSTANCE.userToUerDTO(user);
    }

    public List<UserDTO> getUsers() {
        List<User> users = userRepository.findAll();
        return users
                .stream()
                .map(u -> userMapper.INSTANCE.userToUerDTO(u))
                .collect(Collectors.toList());
    }

    public void createUser(User user) {
        this.userRepository.save(user);
    }

    public void deleteUser(long id) {
        this.userRepository.deleteById(id);
    }

    public void updateUser(User user) {
        this.userRepository.save(user);
    }
}
