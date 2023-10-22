package hexlet.code.controllers;

import hexlet.code.models.Label;
import hexlet.code.models.Task;
import hexlet.code.models.TaskStatus;
import hexlet.code.models.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.InstansioModelGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
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

	private Task taskWithOutLabel;
	private Task taskWithLabels;
	private User user1;
	private User user2;
	private Label label1;
	private Label label2;
	private TaskStatus taskStatus1;
	private TaskStatus taskStatus2;

	@BeforeEach
	public void setup() {
		user1 = Instancio.of(instansioModelGenerator.getUserModel())
				.create();
		user2 = Instancio.of(instansioModelGenerator.getUserModel())
				.create();
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

		taskWithOutLabel = Task.builder()
				.author(user1)
				.executor(user2)
				.taskStatus(taskStatus1)
				.name("taskWithOutLabel")
				.build();
		taskWithLabels = Task.builder()
				.author(user1)
				.executor(user2)
				.taskStatus(taskStatus2)
				.labels(Set.of(label1, label2))
				.name("taskWithLabels")
				.build();
	}

	@Test
	void testGetTaskIfTaskPersist() throws Exception {
		taskRepository.save(taskWithLabels);
		MockHttpServletResponse response = mockMvc
				.perform(get("/tasks/" + taskWithLabels.getId()))
				.andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
		assertThat(response.getContentAsString()).contains(taskWithLabels.getName());
	}

	@Test
	void testGetTaskIfTaskNotPersist() throws Exception {
		taskRepository.save(taskWithLabels);
		var nonExistentID  = taskRepository.findAll().size() + 1;
		MockHttpServletResponse response = mockMvc
				.perform(get("/tasks/" + nonExistentID))
				.andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(422);
		assertThat(response.getContentType()).isNotEqualTo(MediaType.APPLICATION_JSON.toString());
		assertThat(response.getContentAsString()).doesNotContain(taskWithLabels.getName());
	}

	@Test
	void testGetTaskIfTaskNotPersistAndRequestIsNotCorrect() throws Exception {
		MockHttpServletResponse response = mockMvc
				.perform(get("/tasks/a"))
				.andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(400);
	}

	@Test
	void testGetTasks() throws Exception {
		taskRepository.save(taskWithLabels);
//		taskRepository.save(taskWithOutLabel);
		MockHttpServletResponse response = mockMvc
				.perform(get("/tasks"))
				.andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
		assertThat(response.getContentAsString()).contains(taskWithLabels.getName());
//		assertThat(response.getContentAsString()).contains();
	}


	@Test
	void testCreateTask() throws Exception {
		MockHttpServletResponse responsePost = mockMvc
				.perform(
						post("/tasks")
								.contentType(MediaType.APPLICATION_JSON)
								.content("{\"name\":\"task name\","
										+ "\"description\":\"task description\","
										+ "\"executorId\":"+ user1.getId() + ","
										+ "\"taskStatusId\":" + user2.getId() + "}"
								)
				)
				.andReturn()
				.getResponse();
		assertThat(responsePost.getStatus()).isEqualTo(200);
		MockHttpServletResponse response = mockMvc
				.perform(get("/tasks"))
				.andReturn()
				.getResponse();
		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
		assertThat(response.getContentAsString()).contains("task name", "task description");
	}

	@Test
	void testCreateTaskNoExecutorId() throws Exception {
		MockHttpServletResponse responsePost = mockMvc
				.perform(
						post("/tasks")
								.contentType(MediaType.APPLICATION_JSON)
								.content("{\"name\":\"task name\","
										+ "\"description\":\"task description\","
										+ "\"executorId\":,"
										+ "\"taskStatusId\":" + user2.getId() + "}"
								)
				)
				.andReturn()
				.getResponse();

		assertThat(responsePost.getStatus()).isEqualTo(400);
		MockHttpServletResponse response = mockMvc
				.perform(get("/tasks"))
				.andReturn()
				.getResponse();
		assertThat(response.getContentAsString()).doesNotContain("testEmail@testEmail.com", "Biba", "Boba");
	}

	@Test
	void testCreateEmptyName() throws Exception {
		MockHttpServletResponse responsePost = mockMvc
				.perform(
						post("/tasks")
								.contentType(MediaType.APPLICATION_JSON)
								.content("{\"name\":\"\","
										+ "\"description\":\"task description\","
										+ "\"executorId\":"+ user1.getId() + ","
										+ "\"taskStatusId\":" + user2.getId() + "}"
								)
				)
				.andReturn()
				.getResponse();

		assertThat(responsePost.getStatus()).isEqualTo(400);
		MockHttpServletResponse response = mockMvc
				.perform(get("/tasks"))
				.andReturn()
				.getResponse();
		assertThat(response.getContentAsString()).doesNotContain("testEmail@testEmail.com", "Biba", "Boba");
	}

	@Test
	void testCreateTaskNoStatus() throws Exception {
		MockHttpServletResponse responsePost = mockMvc
				.perform(
						post("/tasks")
								.contentType(MediaType.APPLICATION_JSON)
								.content("{\"name\":\"task name\","
										+ "\"description\":\"task description\","
										+ "\"executorId\":"+ user1.getId() + ","
										+ "\"taskStatusId\":}"
								)
				)
				.andReturn()
				.getResponse();

		assertThat(responsePost.getStatus()).isEqualTo(400);
		MockHttpServletResponse response = mockMvc
				.perform(get("/tasks"))
				.andReturn()
				.getResponse();
		assertThat(response.getContentAsString()).doesNotContain("testEmail@testEmail.com", "Biba", "Boba");
	}

	@Test
	void testCreateTaskNoJson() throws Exception {
		MockHttpServletResponse responsePost = mockMvc
				.perform(
						post("/tasks")
								.contentType(MediaType.APPLICATION_JSON)
								.content("")
				)
				.andReturn()
				.getResponse();

		assertThat(responsePost.getStatus()).isEqualTo(400);
		MockHttpServletResponse response = mockMvc
				.perform(get("/tasks"))
				.andReturn()
				.getResponse();
		assertThat(response.getContentAsString()).doesNotContain("testEmail@testEmail.com", "Biba", "Boba");
	}

	@Test
	void testUpdatesTask() throws Exception {
		taskRepository.save(taskWithLabels);
		MockHttpServletResponse responsePost = mockMvc
				.perform(
						put("/tasks/" +  taskWithLabels.getId())
								.contentType(MediaType.APPLICATION_JSON)
								.content("{\"name\":\"new tasks name\","
										+ "\"description\":\"new task description\","
										+ "\"executorId\":"+ user1.getId() + ","
										+ "\"taskStatusId\":"+ user1.getId() + "}"
								)
				)
				.andReturn()
				.getResponse();

		assertThat(responsePost.getStatus()).isEqualTo(200);

		MockHttpServletResponse response = mockMvc
				.perform(get("/tasks"))
				.andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
		assertThat(response.getContentAsString()).contains("new tasks name", "new task description");
	}

	@Test
	void testUpdatesTaskNotCorrecTaskStatusId() throws Exception {
		taskRepository.save(taskWithLabels);
		MockHttpServletResponse responsePost = mockMvc
				.perform(
						put("/tasks/" +  taskWithLabels.getId())
								.contentType(MediaType.APPLICATION_JSON)
								.content("{\"name\":\"new tasks name\","
										+ "\"description\":\"new task description\","
										+ "\"executorId\":"+ user2.getId() + ","
										+ "\"taskStatusId\":,}"
								)
				)
				.andReturn()
				.getResponse();

		assertThat(responsePost.getStatus()).isEqualTo(400);

		MockHttpServletResponse response = mockMvc
				.perform(get("/tasks"))
				.andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
		assertThat(response.getContentAsString()).doesNotContain("testEmail@testEmail.com", "Biba", "Boba");
	}

	@Test
	void testDeleteTask() throws Exception {
		taskRepository.save(taskWithLabels);
		MockHttpServletResponse responsePost = mockMvc
				.perform(delete("/tasks/" + taskWithLabels.getId()))
				.andReturn()
				.getResponse();

		assertThat(responsePost.getStatus()).isEqualTo(200);

		MockHttpServletResponse response = mockMvc
				.perform(get("/tasks"))
				.andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
		assertThat(response.getContentAsString()).doesNotContain(taskWithLabels.getName());
	}
}
