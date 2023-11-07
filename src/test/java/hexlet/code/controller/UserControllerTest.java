package hexlet.code.controller;

import hexlet.code.config.TestConfig;
import hexlet.code.dto.User.UserCreateDTO;
import hexlet.code.models.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.InstansioModelGenerator;
import hexlet.code.utils.TestUtils;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static hexlet.code.config.TestConfig.TEST_PROFILE;
import static hexlet.code.utils.TestUtils.asJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureMockMvc
@Transactional
@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = TestConfig.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private InstansioModelGenerator instansioModelGenerator;
    @Autowired
    private UserRepository userRepository;
    private User user;
    private User user2;
    private final User testUser = new User();
    @Autowired
    private TestUtils utils;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        user = Instancio.of(instansioModelGenerator.getUserModel())
                .create();
        user2 = Instancio.of(instansioModelGenerator.getUserModel())
                .create();
        testUser.setEmail("test@test.com");
        testUser.setPassword("1234");
        testUser.setFirstName("test");
        testUser.setLastName("test_");
        userRepository.save(testUser);
        userRepository.save(user);
        userRepository.save(user2);
    }
    @Test
    void testGetUsers() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(get(utils.USERS_BASEURL))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains(user.getFirstName(), user.getLastName());
        assertThat(response.getContentAsString()).contains(user2.getFirstName(), user2.getLastName());
    }

    @Test
    public void testGetUserIfUserPersist() throws Exception {
        final var response = utils.performAuthorizedRequest(
                        get(TestUtils.USERS_BASEURL + "/" + user.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains(user.getFirstName(), user.getLastName());
    }

    @Test
    void testGetUserIfUserNotPersist() throws Exception {
        var nonExistentID  = userRepository.findAll().size() + 999;
        final var response = utils.performAuthorizedRequest(get(utils.USERS_BASEURL + "/" + nonExistentID))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(422);
    }

    @Test
    void testGetUserIfUserNotPersistAndRequestIsNotCorrect() throws Exception {
        final var response = utils.performAuthorizedRequest(get(utils.USERS_BASEURL + "/a"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(422);
        assertThat(response.getContentType()).isNotEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).doesNotContain(user.getFirstName(), user.getLastName());
    }

    @Test
    void testCreateUser() throws Exception {
        final var response = utils.performAuthorizedRequest(
                        post(TestUtils.USERS_BASEURL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJson(TestUtils.USER_CREATE_DTO))
                )
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(201);

        final var response2 = utils.performAuthorizedRequest(get(TestUtils.USERS_BASEURL))
                .andReturn()
                .getResponse();
        assertThat(response2.getStatus()).isEqualTo(200);
        assertThat(response2.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response2.getContentAsString()).contains(
                TestUtils.USER_CREATE_DTO.getEmail(),
                TestUtils.USER_CREATE_DTO.getFirstName(),
                TestUtils.USER_CREATE_DTO.getLastName());
    }

    @Test
    void testCreateUserNotCorrectEmail() throws Exception {
        UserCreateDTO userWithNotCorrectEmail = new UserCreateDTO(
                "testCreateUserNotCorrectEmail@",
                "testCreateUserNotCorrectEmail",
                "testCreateUserNotCorrectEmail2",
                "1234");
        final var responsePost = utils.performAuthorizedRequest(
                        post(utils.USERS_BASEURL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJson(userWithNotCorrectEmail))
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(422);

        MockHttpServletResponse response = mockMvc
                .perform(get(utils.USERS_BASEURL))
                .andReturn()
                .getResponse();
        assertThat(response.getContentAsString()).doesNotContain(
                userWithNotCorrectEmail.getFirstName(),
                userWithNotCorrectEmail.getLastName());
    }

    @Test
    void testCreateUserNoName() throws Exception {
        var userWithNoFirstName = new UserCreateDTO("email@email.com", "", "test2", "1234");
        MockHttpServletResponse responsePost = mockMvc
                .perform(
                        post(TestUtils.USERS_BASEURL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJson(userWithNoFirstName))
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(422);

        MockHttpServletResponse response = mockMvc
                .perform(get(TestUtils.USERS_BASEURL))
                .andReturn()
                .getResponse();
        assertThat(response.getContentAsString()).doesNotContain(
                userWithNoFirstName.getEmail(),
                userWithNoFirstName.getLastName());
    }

    @Test
    void testCreateUserNoLastName() throws Exception {
        UserCreateDTO userWithNoLastName = new UserCreateDTO(
                "testCreateUserNoLastName@email.com",
                "testCreateUserNoLastName",
                "",
                "1234");
        MockHttpServletResponse responsePost = mockMvc
                .perform(
                        post(utils.USERS_BASEURL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJson(userWithNoLastName))
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(422);
        MockHttpServletResponse response = mockMvc
                .perform(get(utils.USERS_BASEURL))
                .andReturn()
                .getResponse();
        assertThat(response.getContentAsString()).doesNotContain(
                userWithNoLastName.getEmail(),
                userWithNoLastName.getFirstName());
    }

    @Test
    void testCreateUserNoPassword() throws Exception {
        UserCreateDTO userWithNoPassword = new UserCreateDTO(
                "testCreateUserNoPassword@",
                "testCreateUserNoPassword",
                "testCreateUserNoPassword",
                "");
        MockHttpServletResponse responsePost = mockMvc
                .perform(
                        post(utils.USERS_BASEURL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJson(userWithNoPassword))
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(422);
        MockHttpServletResponse response = mockMvc
                .perform(get(utils.USERS_BASEURL))
                .andReturn()
                .getResponse();
        assertThat(response.getContentAsString()).doesNotContain(
                userWithNoPassword.getEmail(),
                userWithNoPassword.getFirstName(),
                userWithNoPassword.getLastName());
    }

    @Test
    void testUpdatesAnotherUser() throws Exception {
        final var response = utils.performAuthorizedRequest(
                        put(utils.USERS_BASEURL + "/" + user.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJson(utils.USER_UPDATE_DTO))
                )
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    void testUpdateSelf() throws Exception {
        final var response = utils.performAuthorizedRequest(
                        put(utils.USERS_BASEURL + "/" + testUser.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJson(utils.USER_UPDATE_DTO))
                )
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);

        MockHttpServletResponse response2 = mockMvc
                .perform(get(utils.USERS_BASEURL))
                .andReturn()
                .getResponse();

        assertThat(response2.getStatus()).isEqualTo(200);
        assertThat(response2.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response2.getContentAsString()).contains(
                utils.USER_UPDATE_DTO.getEmail(),
                utils.USER_UPDATE_DTO.getFirstName(),
                utils.USER_UPDATE_DTO.getLastName());
    }

    @Test
    void testDeleteAnotherPerson() throws Exception {
        final var responsePost = utils.performAuthorizedRequest(delete(utils.USERS_BASEURL + "/" + user.getId()))
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(403);

        MockHttpServletResponse response = mockMvc
                .perform(get(utils.USERS_BASEURL))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains(user.getFirstName(), user.getLastName());
    }
}
