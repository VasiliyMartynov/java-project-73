package hexlet.code.controllers;

import hexlet.code.config.TestConfig;
import hexlet.code.models.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.utils.InstansioModelGenerator;
import hexlet.code.utils.TestUtils;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static hexlet.code.config.TestConfig.TEST_PROFILE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@AutoConfigureMockMvc
@Transactional
@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = TestConfig.class)
class TaskStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private InstansioModelGenerator instansioModelGenerator;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    private TaskStatus taskStatus;
    @Autowired
    private TestUtils utils;
    private static final String BASEURL = "/api/statuses";
    @BeforeEach
    public void setUp() {
        taskStatus = Instancio.of(instansioModelGenerator.getTaskStatusModel())
                .create();
    }

    @Test
    void testGetStatusIfStatusPersist() throws Exception {
        taskStatusRepository.save(taskStatus);
        final var response = utils.performAuthorizedRequest(get(BASEURL + "/" + taskStatus.getId()))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains(taskStatus.getName());
    }

    @Test
    void testGetStatusIfStatusNotPersist() throws Exception {
        var nonExistentID  = taskStatusRepository.findAll().size() + 999;
        final var response = utils.performAuthorizedRequest(get(BASEURL + "/" + nonExistentID))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(422);
    }

    @Test
    void testGetStatusIfStatusNotPersistAndRequestIsNotCorrect() throws Exception {
        final var response = utils.performAuthorizedRequest(get(BASEURL + "/" + "a"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(422);
        assertThat(response.getContentType()).isNotEqualTo(MediaType.APPLICATION_JSON.toString());
    }

    @Test
    void testGetStatuses() throws Exception {
        taskStatusRepository.save(taskStatus);
        var taskStatus2 = Instancio.of(instansioModelGenerator.getTaskStatusModel())
                .create();
        taskStatusRepository.save(taskStatus2);
        final var response = utils.performAuthorizedRequest(get(BASEURL))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains(taskStatus.getName());
        assertThat(response.getContentAsString()).contains(taskStatus2.getName());
    }


    @Test
    void testCreateStatus() throws Exception {
        final var responsePost = utils.performAuthorizedRequest(
                        post(BASEURL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "name": "test status"
                                        }
                                        """
                                )
                )
                .andReturn()
                .getResponse();
        assertThat(responsePost.getStatus()).isEqualTo(201);
        final var response = utils.performAuthorizedRequest(get(BASEURL))
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains("test status");
    }

    @Test
    void testCreateStatusWithNull() throws Exception {
        final var responsePost = utils.performAuthorizedRequest(
                        post(BASEURL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "name": null
                                        }
                                        """
                                )
                )
                .andReturn()
                .getResponse();
        assertThat(responsePost.getStatus()).isEqualTo(422);
    }

    @Test
    void testUpdatesStatus() throws Exception {
        taskStatusRepository.save(taskStatus);
        final var responsePost = utils.performAuthorizedRequest(
                        put(BASEURL + "/" + taskStatus.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "name": "updated status"
                                        }
                                        """
                                )
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(200);

        final var response = utils.performAuthorizedRequest(get(BASEURL + "/" + taskStatus.getId()))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains("updated status");
    }

    @Test
    void testDeletePerson() throws Exception {
        taskStatusRepository.save(taskStatus);
        final var responsePost = utils.performAuthorizedRequest(delete(BASEURL + "/" + taskStatus.getId()))
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(200);

        final var response = utils.performAuthorizedRequest(get(BASEURL))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).doesNotContain(taskStatus.getName());
    }
}
