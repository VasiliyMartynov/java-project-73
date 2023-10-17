package hexlet.code.app;

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

	@Test
	void testGetUserIfUserPersist() throws Exception {
		MockHttpServletResponse response = mockMvc
				.perform(get("/users/1"))
				.andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
		assertThat(response.getContentAsString()).contains("John", "Smith");
	}

	@Test
	void testGetUserIfUserNotPersistAndRequestIsCorrect() throws Exception {
		MockHttpServletResponse response = mockMvc
				.perform(get("/users/99"))
				.andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(422);
		assertThat(response.getContentType()).isNotEqualTo(MediaType.APPLICATION_JSON.toString());
		assertThat(response.getContentAsString()).doesNotContain("John", "Smith");
	}

	@Test
	void testGetUserIfUserNotPersistAndRequestIsNotCorrect() throws Exception {
		MockHttpServletResponse response = mockMvc
				.perform(get("/users/a"))
				.andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(400);
		assertThat(response.getContentType()).isNotEqualTo(MediaType.APPLICATION_JSON.toString());
		assertThat(response.getContentAsString()).doesNotContain("John", "Smith");
	}

	@Test
	void testGetUsers() throws Exception {
		MockHttpServletResponse response = mockMvc
				.perform(get("/users"))
				.andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
		assertThat(response.getContentAsString()).contains("John", "Smith");
		assertThat(response.getContentAsString()).contains("Jack", "Doe");
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
		System.out.println("t1");
		assertThat(responsePost.getStatus()).isEqualTo(200);
		System.out.println("t2");
		MockHttpServletResponse response = mockMvc
				.perform(get("/users"))
				.andReturn()
				.getResponse();
		System.out.println("t3");
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
		MockHttpServletResponse responsePost = mockMvc
				.perform(
						put("/users/1")
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
		MockHttpServletResponse responsePost = mockMvc
				.perform(
						put("/users/1")
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
		MockHttpServletResponse responsePost = mockMvc
				.perform(delete("/users/1"))
				.andReturn()
				.getResponse();

		assertThat(responsePost.getStatus()).isEqualTo(200);

		MockHttpServletResponse response = mockMvc
				.perform(get("/users"))
				.andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
		assertThat(response.getContentAsString()).doesNotContain("John", "Smith");
	}
}
