package hexlet.code.controllers;

import hexlet.code.models.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.InstansioModelGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private InstansioModelGenerator instansioModelGenerator;

	@Autowired
	private UserRepository userRepository;

	private User user;

	@BeforeEach
	public void setUp() {
		user = Instancio.of(instansioModelGenerator.getUserModel())
				.create();
	}

	@Test
	void testGetUserIfUserPersist() throws Exception {
		userRepository.save(user);
		MockHttpServletResponse response = mockMvc
				.perform(get("/users/" + user.getId()))
				.andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
		assertThat(response.getContentAsString()).contains(user.getFirstName(), user.getLastName());
	}

	@Test
	void testGetUserIfUserNotPersist() throws Exception {
		var nonExistentID  = userRepository.findAll().size() + 1;
		MockHttpServletResponse response = mockMvc
				.perform(get("/users/" + nonExistentID))
				.andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(422);
	}

	@Test
	void testGetUserIfUserNotPersistAndRequestIsNotCorrect() throws Exception {
		userRepository.save(user);
		MockHttpServletResponse response = mockMvc
				.perform(get("/users/a"))
				.andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(400);
		assertThat(response.getContentType()).isNotEqualTo(MediaType.APPLICATION_JSON.toString());
		assertThat(response.getContentAsString()).doesNotContain(user.getFirstName(), user.getLastName());
	}

	@Test
	void testGetUsers() throws Exception {
		userRepository.save(user);
		var user2 = Instancio.of(instansioModelGenerator.getUserModel())
				.create();
		userRepository.save(user2);
		MockHttpServletResponse response = mockMvc
				.perform(get("/users"))
				.andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
		assertThat(response.getContentAsString()).contains(user.getFirstName(), user.getLastName());
		assertThat(response.getContentAsString()).contains(user2.getFirstName(), user2.getLastName());
	}


	@Test
	void testCreateUser() throws Exception {
		MockHttpServletResponse responsePost = mockMvc
				.perform(
						post("/users")
								.contentType(MediaType.APPLICATION_JSON)
								.content("{\"email\":\"testEmail@testEmail.com\","
										+ "\"firstName\":\"Biba\","
										+ "\"lastName\":\"Boba\","
										+ "\"password\":\"some-password\"}"
								)
				)
				.andReturn()
				.getResponse();
		assertThat(responsePost.getStatus()).isEqualTo(200);
		MockHttpServletResponse response = mockMvc
				.perform(get("/users"))
				.andReturn()
				.getResponse();
		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
		assertThat(response.getContentAsString()).contains("testEmail@testEmail.com", "Biba", "Boba");
	}

	@Test
	void testCreateUserNotCorrectEmail() throws Exception {
		MockHttpServletResponse responsePost = mockMvc
				.perform(
						post("/users")
								.contentType(MediaType.APPLICATION_JSON)
								.content("{\"email\":\"testEmail@\""
										+ ",\"firstName\":\"Biba\""
										+ ",\"lastName\":\"Boba\""
										+ ",\"password\":\"somepass\"}"
								)
				)
				.andReturn()
				.getResponse();

		assertThat(responsePost.getStatus()).isEqualTo(400);
		MockHttpServletResponse response = mockMvc
				.perform(get("/users"))
				.andReturn()
				.getResponse();
		assertThat(response.getContentAsString()).doesNotContain("testEmail@testEmail.com", "Biba", "Boba");
	}

	@Test
	void testCreateUserNoName() throws Exception {
		MockHttpServletResponse responsePost = mockMvc
				.perform(
						post("/users")
								.contentType(MediaType.APPLICATION_JSON)
								.content("{\"email\":\"testEmail@\""
										+ ",\"firstName\":\"\""
										+ ",\"lastName\":\"Boba\""
										+ ",\"password\":\"somepass\"}"
								)
				)
				.andReturn()
				.getResponse();

		assertThat(responsePost.getStatus()).isEqualTo(400);
		MockHttpServletResponse response = mockMvc
				.perform(get("/users"))
				.andReturn()
				.getResponse();
		assertThat(response.getContentAsString()).doesNotContain("testEmail@testEmail.com", "Biba", "Boba");
	}

	@Test
	void testCreateUserNoLastName() throws Exception {
		MockHttpServletResponse responsePost = mockMvc
				.perform(
						post("/users")
								.contentType(MediaType.APPLICATION_JSON)
								.content("{\"email\":\"testEmail@\""
										+ ",\"firstName\":\"\"Biba"
										+ ",\"lastName\":\"\""
										+ ",\"password\":\"somepass\"}"
								)
				)
				.andReturn()
				.getResponse();

		assertThat(responsePost.getStatus()).isEqualTo(400);
		MockHttpServletResponse response = mockMvc
				.perform(get("/users"))
				.andReturn()
				.getResponse();
		assertThat(response.getContentAsString()).doesNotContain("testEmail@testEmail.com", "Biba", "Boba");
	}

	@Test
	void testCreateUserNoPassword() throws Exception {
		MockHttpServletResponse responsePost = mockMvc
				.perform(
						post("/users")
								.contentType(MediaType.APPLICATION_JSON)
								.content("{\"email\":\"testEmail@\""
										+ ",\"firstName\":\"\"Biba"
										+ ",\"lastName\":\"\""
										+ ",\"password\":\"\"}"
								)
				)
				.andReturn()
				.getResponse();

		assertThat(responsePost.getStatus()).isEqualTo(400);
		MockHttpServletResponse response = mockMvc
				.perform(get("/users"))
				.andReturn()
				.getResponse();
		assertThat(response.getContentAsString()).doesNotContain("testEmail@testEmail.com", "Biba", "Boba");
	}

	@Test
	void testUpdatesUser() throws Exception {
		userRepository.save(user);
		MockHttpServletResponse responsePost = mockMvc
				.perform(
						put("/users/" + user.getId())
								.contentType(MediaType.APPLICATION_JSON)
								.content("{\"email\":\"testEmail@testEmail.com\""
										+ ",\"firstName\":\"Biba\""
										+ ",\"lastName\":\"Boba\""
										+ ",\"password\":\"somepass\"}"
								)
				)
				.andReturn()
				.getResponse();

		assertThat(responsePost.getStatus()).isEqualTo(200);

		MockHttpServletResponse response = mockMvc
				.perform(get("/users"))
				.andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
		assertThat(response.getContentAsString()).contains("testEmail@testEmail.com", "Biba", "Boba");
	}

	@Test
	void testUpdatesUserNotCorrecEmail() throws Exception {
		userRepository.save(user);
		MockHttpServletResponse responsePost = mockMvc
				.perform(
						put("/users/" + user.getId())
								.contentType(MediaType.APPLICATION_JSON)
								.content("{\"email\":\"testEmail@\""
										+ ",\"firstName\":\"\"Biba"
										+ ",\"lastName\":\"\"Boba"
										+ ",\"password\":\"\"}"
								)
				)
				.andReturn()
				.getResponse();

		assertThat(responsePost.getStatus()).isEqualTo(400);

		MockHttpServletResponse response = mockMvc
				.perform(get("/users"))
				.andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
		assertThat(response.getContentAsString()).doesNotContain("testEmail@testEmail.com", "Biba", "Boba");
	}

	@Test
	void testDeletePerson() throws Exception {
		userRepository.save(user);
		MockHttpServletResponse responsePost = mockMvc
				.perform(delete("/users/" + user.getId()))
				.andReturn()
				.getResponse();

		assertThat(responsePost.getStatus()).isEqualTo(200);

		MockHttpServletResponse response = mockMvc
				.perform(get("/users"))
				.andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
		assertThat(response.getContentAsString()).doesNotContain(user.getFirstName(), user.getLastName());
	}
}
