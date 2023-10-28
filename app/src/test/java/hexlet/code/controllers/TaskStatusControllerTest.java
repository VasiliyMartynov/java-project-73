package hexlet.code.controllers;

import hexlet.code.models.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TaskStatusControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TaskStatusRepository taskStatusRepository;

	@Autowired
	private InstansioModelGenerator instansioModelGenerator;

	private TaskStatus taskStatus;

	@BeforeEach
	public void setUp() {
		taskStatus = Instancio.of(instansioModelGenerator.getTaskStatusModel())
				.create();
	}

	@Test
	void testGetStatusIfStatusPersist() throws Exception {
		taskStatusRepository.save(taskStatus);
		MockHttpServletResponse response = mockMvc
				.perform(get("/statuses/" + taskStatus.getId()))
				.andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
		assertThat(response.getContentAsString()).contains(taskStatus.getName());
	}

	@Test
	void testGetStatusIfStatusNotPersist() throws Exception {
		var nonExistentID  = taskStatusRepository.findAll().size() + 1;
		MockHttpServletResponse response = mockMvc
				.perform(get("/statuses/" + nonExistentID))
				.andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(422);
	}

	@Test
	void testGetStatusIfStatusNotPersistAndRequestIsNotCorrect() throws Exception {
		MockHttpServletResponse response = mockMvc
				.perform(get("/statuses/a"))
				.andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(400);
		assertThat(response.getContentType()).isNotEqualTo(MediaType.APPLICATION_JSON.toString());
	}

	@Test
	void testGetStatuses() throws Exception {
		taskStatusRepository.save(taskStatus);
		var taskStatus2 = Instancio.of(instansioModelGenerator.getTaskStatusModel())
				.create();
		taskStatusRepository.save(taskStatus2);
		MockHttpServletResponse response = mockMvc
				.perform(get("/statuses"))
				.andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
		assertThat(response.getContentAsString()).contains(taskStatus.getName());
		assertThat(response.getContentAsString()).contains(taskStatus2.getName());
	}


	@Test
	void testCreateStatus() throws Exception {
		MockHttpServletResponse responsePost = mockMvc
				.perform(
						post("/statuses")
								.contentType(MediaType.APPLICATION_JSON)
								.content("{\"name\":\"test status\"}"
								)
				)
				.andReturn()
				.getResponse();
		assertThat(responsePost.getStatus()).isEqualTo(200);
		MockHttpServletResponse response = mockMvc
				.perform(get("/statuses"))
				.andReturn()
				.getResponse();
		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
		assertThat(response.getContentAsString()).contains("test status");
	}

	@Test
	void testCreateStatusWithNull() throws Exception {
		MockHttpServletResponse responsePost = mockMvc
				.perform(
						post("/statuses")
								.contentType(MediaType.APPLICATION_JSON)
								.content("{\"name\":null}"
								)
				)
				.andReturn()
				.getResponse();
		assertThat(responsePost.getStatus()).isEqualTo(400);
	}

	@Test
	void testUpdatesStatus() throws Exception {
		taskStatusRepository.save(taskStatus);
		MockHttpServletResponse responsePost = mockMvc
				.perform(
						put("/statuses/" + taskStatus.getId())
								.contentType(MediaType.APPLICATION_JSON)
								.content("{\"name\":\"updated status\"}"
								)
				)
				.andReturn()
				.getResponse();

		assertThat(responsePost.getStatus()).isEqualTo(200);

		MockHttpServletResponse response = mockMvc
				.perform(get("/statuses/" + taskStatus.getId()))
				.andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
		assertThat(response.getContentAsString()).contains("updated status");
	}

	@Test
	void testDeletePerson() throws Exception {
		taskStatusRepository.save(taskStatus);
		MockHttpServletResponse responsePost = mockMvc
				.perform(delete("/statuses/" + taskStatus.getId()))
				.andReturn()
				.getResponse();

		assertThat(responsePost.getStatus()).isEqualTo(200);

		MockHttpServletResponse response = mockMvc
				.perform(get("/statuses"))
				.andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
		assertThat(response.getContentAsString()).doesNotContain(taskStatus.getName());
	}
}
