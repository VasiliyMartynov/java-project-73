package hexlet.code.services;

import hexlet.code.dto.user.UserDTO;
import hexlet.code.exceptions.ResourceNotFoundException;
import hexlet.code.mapper.UserMapper;
import hexlet.code.models.user.User;
import hexlet.code.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
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

    public UserDTO createUser(User user) throws Exception {
        try {
            user.setPassword(generateHash(user.getPassword()));
            userRepository.save(user);
            userRepository.flush();
            return getUser(user.getId());
        } catch (Exception e) {
            throw new Exception("Something where wrong");
        }
    }

    private String generateHash(String passwordToHash) throws NoSuchAlgorithmException {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(salt);
        byte[] hashedPassword = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
        return hashedPassword.toString();
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

    public UserDTO findUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        return convertUserToUserDTO(user);
    }

    //CONVERT user to userDTO
    public static UserDTO convertUserToUserDTO(User user) {
        return userMapper.INSTANCE.userToUserDTO(user);
    }


    //get user POJO
//    public User getUserAllData(long id) {
//        User user = userRepository.findById(id).orElseThrow(
//                () -> new ResourceNotFoundException(id + " not found"));
//        return user;
//    }
}
