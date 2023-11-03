package hexlet.code.controller;

import hexlet.code.config.TestConfig;
import hexlet.code.models.Label;
import hexlet.code.models.Task;
import hexlet.code.models.TaskStatus;
import hexlet.code.models.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
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
import java.util.Set;
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
class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private InstansioModelGenerator instansioModelGenerator;
    @Autowired
    private TestUtils utils;
    private Task task1 = new Task();
    private Task task2 = new Task();
    private User user1;
    private User user2;
    private User testUser = new User();
    private Label label1;
    private Label label2;
    private TaskStatus taskStatus1;
    private TaskStatus taskStatus2;

    private static final String BASEURL = "/api/tasks";

    @BeforeEach
    public void setup() {
        user1 = Instancio.of(instansioModelGenerator.getUserModel())
                .create();
        user2 = Instancio.of(instansioModelGenerator.getUserModel())
                .create();
        testUser.setEmail("test@test.com");
        testUser.setPassword("1234");
        testUser.setFirstName("test");
        testUser.setLastName("test_");
        userRepository.save(testUser);

        label1 = Instancio.of(instansioModelGenerator.getLabelModel())
                .create();
        label2 = Instancio.of(instansioModelGenerator.getLabelModel())
                .create();
        taskStatus1 = Instancio.of(instansioModelGenerator.getTaskStatusModel())
                .create();
        taskStatus2 = Instancio.of(instansioModelGenerator.getTaskStatusModel())
                .create();

        userRepository.save(user1);
        userRepository.save(user2);
        labelRepository.save(label1);
        labelRepository.save(label2);
        taskStatusRepository.save(taskStatus1);
        taskStatusRepository.save(taskStatus2);

        task1.setAuthor(testUser);
        task1.setExecutor(testUser);
        task1.setTaskStatus(taskStatus2);
        task1.setLabels(Set.of(label1, label2));
        task1.setName("new test task1");
        task1.setDescription("some description1");


        task2.setAuthor(user2);
        task2.setExecutor(user1);
        task2.setTaskStatus(taskStatus1);
        task2.setLabels(Set.of(label1, label2));
        task2.setName("new test task2");
        task2.setDescription("some description2");

    }

    @Test
    void testGetTaskIfTaskPersist() throws Exception {
        taskRepository.save(task1);
        final var response = utils.performAuthorizedRequest(get(BASEURL + "/" + task1.getId()))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains(task1.getName());
    }

    @Test
    void testGetTaskIfTaskNotPersist() throws Exception {
        taskRepository.save(task1);
        var nonExistentID  = taskRepository.findAll().size() + 1;
        final var response = utils.performAuthorizedRequest(get(BASEURL + "/" + nonExistentID))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(422);
        assertThat(response.getContentType()).isNotEqualTo(MediaType.APPLICATION_JSON.toString());

    }

    @Test
    void testGetTaskIfTaskNotPersistAndRequestIsNotCorrect() throws Exception {
        final var response = utils.performAuthorizedRequest(get(BASEURL + "/" + "a"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(422);
    }

    @Test
    void testGetTasks() throws Exception {
        taskRepository.save(task1);
        taskRepository.save(task2);
        final var response = utils.performAuthorizedRequest(get(BASEURL))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains(task1.getName());
        assertThat(response.getContentAsString()).contains(task2.getName());

    }


    @Test
    void testCreateTask() throws Exception {
        final var responsePost = utils.performAuthorizedRequest(
                post(BASEURL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                            "{\"name\":\"new task\","
                            + "\"description\":\"task description\","
                            + "\"executorId\":" + user1.getId() + ","
                            + "\"taskStatusId\":" + taskStatus1.getId() + ","
                            + "\"labelIds\":["
                            + label1.getId() + "," + label2.getId() + "]}"
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
        assertThat(response.getContentAsString()).contains("new task", "task description");
    }

    @Test
    void testCreateTaskNoExecutorId() throws Exception {
        final var responsePost = utils.performAuthorizedRequest(
                post(BASEURL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\":\"new task\","
                            + "\"description\":\"task description\","
                            + "\"executorId\":,"
                            + "\"taskStatusId\":" + taskStatus1.getId() + ","
                            + "\"labelIds\":["
                            + label1.getId()
                            + "," + label2.getId() + "]}"
                    )
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(400);
    }

    @Test
    void testCreateEmptyName() throws Exception {
        final var responsePost = utils.performAuthorizedRequest(
                post(BASEURL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\":\"\","
                            + "\"description\":\"new task description\","
                            + "\"executorId\":" + user1.getId() + ","
                            + "\"taskStatusId\":" + taskStatus1.getId() + ","
                            + "\"labelIds\":[1,2,3]}"
                    )
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(422);
        final var response = utils.performAuthorizedRequest(get("/tasks"))
                .andReturn()
                .getResponse();
        assertThat(response.getContentAsString()).doesNotContain("new task description");
    }

    @Test
    void testCreateTaskNoStatus() throws Exception {
        final var responsePost = utils.performAuthorizedRequest(
                post(BASEURL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                            "{\"name\":\"new task with\","
                            + "\"description\":\"new task description\","
                            + "\"executorId\":" + user1.getId() + ","
                            + "\"taskStatusId\":,"
                            + "\"labelIds\":[1,2,3]}"
                    )
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(400);
        final var response = utils.performAuthorizedRequest(get(BASEURL))
                .andReturn()
                .getResponse();
        assertThat(response.getContentAsString()).doesNotContain("new task with", "new task description");
    }

    @Test
    void testCreateTaskNoJson() throws Exception {
        final var responsePost = utils.performAuthorizedRequest(
                        post(BASEURL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("")
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(400);
        final var response = utils.performAuthorizedRequest(get("/tasks"))
                .andReturn()
                .getResponse();
        assertThat(response.getContentAsString()).doesNotContain("testEmail@testEmail.com", "Biba", "Boba");
    }

    @Test
    void testUpdatesTask() throws Exception {
        taskRepository.save(task1);
        final var responsePost = utils.performAuthorizedRequest(
                put(BASEURL + "/" + task1.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                            "{\"name\":\"updated task\","
                            + "\"description\":\"updated description\","
                            + "\"executorId\":" + user1.getId() + ","
                            + "\"taskStatusId\":" + taskStatus1.getId() + ","
                            + "\"labelIds\":[" + label1.getId()
                            + "," + label2.getId() + "]}"
                    )
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(200);

        final var response = utils.performAuthorizedRequest(get(BASEURL))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains("updated task", "updated description");
    }

    @Test
    void testDeleteTask() throws Exception {
        taskRepository.save(task1);
        final var response1 = utils.performAuthorizedRequest(delete(BASEURL + "/" + task1.getId()))
                .andReturn()
                .getResponse();
        final var response = utils.performAuthorizedRequest(get(BASEURL))
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).doesNotContain(task1.getName());
    }
}
