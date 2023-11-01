package hexlet.code.controllers;

import hexlet.code.config.TestConfig;
import hexlet.code.models.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.InstansioModelGenerator;
import hexlet.code.utils.TestUtils;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static hexlet.code.config.TestConfig.TEST_PROFILE;
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
    private final User testUser = new User();

    @Autowired
    private TestUtils utils;
    private static final String BASEURL = "/api/users";

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        user = Instancio.of(instansioModelGenerator.getUserModel())
                .create();
        testUser.setEmail("test@test.com");
        testUser.setPassword("1234");
        testUser.setFirstName("test");
        testUser.setLastName("test_");
        userRepository.save(testUser);

    }
    @Test
    void testGetUsers() throws Exception {
        userRepository.save(user);
        var user2 = Instancio.of(instansioModelGenerator.getUserModel())
                .create();
        userRepository.save(user2);
        MockHttpServletResponse response = mockMvc
                .perform(get(BASEURL))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains(user.getFirstName(), user.getLastName());
        assertThat(response.getContentAsString()).contains(user2.getFirstName(), user2.getLastName());
    }

    @Test
    public void testGetUserIfUserPersist() throws Exception {
        userRepository.save(user);
        final var response = utils.performAuthorizedRequest(
                        get(BASEURL + "/" + user.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains(user.getFirstName(), user.getLastName());
    }

    @Test
    void testGetUserIfUserNotPersist() throws Exception {
        var nonExistentID  = userRepository.findAll().size() + 999;
        final var response = utils.performAuthorizedRequest(get(BASEURL + "/" + nonExistentID))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(422);
    }

    @Test
    void testGetUserIfUserNotPersistAndRequestIsNotCorrect() throws Exception {
        userRepository.save(user);
        final var response = utils.performAuthorizedRequest(get(BASEURL + "/a"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(422);
        assertThat(response.getContentType()).isNotEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).doesNotContain(user.getFirstName(), user.getLastName());
    }

    @Test
    void testCreateUser() throws Exception {
        final var response = utils.performAuthorizedRequest(
                        post(BASEURL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                            {
                                            "email": "alibaba@test.com",
                                            "firstName": "Biba",
                                            "lastName": "Boba",
                                            "password": "1234"
                                            }
                                        """
                                )
                )
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(201);
        final var response2 = utils.performAuthorizedRequest(get(BASEURL))
                .andReturn()
                .getResponse();
        assertThat(response2.getStatus()).isEqualTo(200);
        assertThat(response2.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response2.getContentAsString()).contains("alibaba@test.com", "Biba", "Boba");
    }

    @Test
    void testCreateUserNotCorrectEmail() throws Exception {
        final var responsePost = utils.performAuthorizedRequest(
                        post(BASEURL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                            {
                                            "email": "alibaba@",
                                            "firstName": "Biba",
                                            "lastName": "Boba",
                                            "password": "1234"
                                            }
                                        """
                                )
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(422);
        MockHttpServletResponse response = mockMvc
                .perform(get(BASEURL))
                .andReturn()
                .getResponse();
        assertThat(response.getContentAsString()).doesNotContain("Biba", "Boba");
    }

    @Test
    void testCreateUserNoName() throws Exception {
        MockHttpServletResponse responsePost = mockMvc
                .perform(
                        post(BASEURL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                            {
                                            "email": "alibaba@user.com",
                                            "firstName": "",
                                            "lastName": "Boba",
                                            "password": "1234"
                                            }
                                        """)
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(422);
        MockHttpServletResponse response = mockMvc
                .perform(get(BASEURL))
                .andReturn()
                .getResponse();
        assertThat(response.getContentAsString()).doesNotContain("alibaba@user.com", "Boba");
    }

    @Test
    void testCreateUserNoLastName() throws Exception {
        MockHttpServletResponse responsePost = mockMvc
                .perform(
                        post(BASEURL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                            {
                                            "email": "alibaba@user.com",
                                            "firstName": "Biba",
                                            "lastName": "",
                                            "password": "1234"
                                            }
                                        """)
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(422);
        MockHttpServletResponse response = mockMvc
                .perform(get(BASEURL))
                .andReturn()
                .getResponse();
        assertThat(response.getContentAsString()).doesNotContain("alibaba@user.com", "Biba");
    }

    @Test
    void testCreateUserNoPassword() throws Exception {
        MockHttpServletResponse responsePost = mockMvc
                .perform(
                        post(BASEURL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                            {
                                            "email": "alibaba@test.com",
                                            "firstName": "Biba",
                                            "lastName": "Boba",
                                            "password": ""
                                            }
                                        """)
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(422);
        MockHttpServletResponse response = mockMvc
                .perform(get(BASEURL))
                .andReturn()
                .getResponse();
        assertThat(response.getContentAsString()).doesNotContain("testEmail@testEmail.com", "Biba", "Boba");
    }

    @Test
    void testUpdatesAnotherUser() throws Exception {
        userRepository.save(user);
        final var response = utils.performAuthorizedRequest(
                        put(BASEURL + "/" + user.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                            {
                                            "email": "alibaba@test.com",
                                            "firstName": "Biba",
                                            "lastName": "Boba",
                                            "password": "1234"
                                            }
                                        """
                                )
                )
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    void testUpdateSelf() throws Exception {
        final var response = utils.performAuthorizedRequest(
                        put(BASEURL + "/" + testUser.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                            {
                                            "email": "alibaba@test.com",
                                            "firstName": "Biba",
                                            "lastName": "Boba",
                                            "password": "1234"
                                            }
                                        """
                                )
                )
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);

        MockHttpServletResponse response2 = mockMvc
                .perform(get(BASEURL))
                .andReturn()
                .getResponse();

        assertThat(response2.getStatus()).isEqualTo(200);
        assertThat(response2.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response2.getContentAsString()).contains("alibaba@test.com", "Biba", "Boba");
    }

    @Test
    void testUpdatesUserNotCorrectEmail() throws Exception {
        final var responsePost = utils.performAuthorizedRequest(
                        put(BASEURL + "/" + user.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                            {
                                            "email": "alibaba@",
                                            "firstName": "Biba",
                                            "lastName": "Boba",
                                            "password": "1234"
                                            }
                                        """
                                )
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(422);

        MockHttpServletResponse response = mockMvc
                .perform(get(BASEURL))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).doesNotContain("Biba", "Boba");
    }

    @Test
    void testDeleteAnotherPerson() throws Exception {
        userRepository.save(user);
        final var responsePost = utils.performAuthorizedRequest(delete(BASEURL + "/" + user.getId()))
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(403);

        MockHttpServletResponse response = mockMvc
                .perform(get(BASEURL))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains(user.getFirstName(), user.getLastName());
    }
}
