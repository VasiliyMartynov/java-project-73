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
import org.springframework.security.test.context.support.WithMockUser;
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
	private User testUser = new User();
	private static final String BASEURL = "/api/users";

	@BeforeEach
	public void setUp() {
		user = Instancio.of(instansioModelGenerator.getUserModel())
				.create();
		testUser.setEmail("test@test.com");
		testUser.setPassword("1234");
		testUser.setFirstName("test");
		testUser.setLastName("test_");
		userRepository.save(testUser);

	}

	@Test
	@WithMockUser
	void testGetUserIfUserPersist() throws Exception {
		userRepository.save(user);
		MockHttpServletResponse response = mockMvc
				.perform(get(BASEURL + "/" + user.getId()))
				.andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
		assertThat(response.getContentAsString()).contains(user.getFirstName(), user.getLastName());
	}

	@Test
	@WithMockUser
	void testGetUserIfUserNotPersist() throws Exception {
		var nonExistentID  = userRepository.findAll().size() + 1;
		MockHttpServletResponse response = mockMvc
				.perform(get(BASEURL + "/" + nonExistentID))
				.andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(422);
	}

	@Test
	@WithMockUser
	void testGetUserIfUserNotPersistAndRequestIsNotCorrect() throws Exception {
		userRepository.save(user);
		MockHttpServletResponse response = mockMvc
				.perform(get(BASEURL + "/a"))
				.andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(400);
		assertThat(response.getContentType()).isNotEqualTo(MediaType.APPLICATION_JSON.toString());
		assertThat(response.getContentAsString()).doesNotContain(user.getFirstName(), user.getLastName());
	}

	@Test
	@WithMockUser
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
	@WithMockUser
	void testCreateUser() throws Exception {
		MockHttpServletResponse responsePost = mockMvc
				.perform(
						post(BASEURL)
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
				.perform(get(BASEURL))
				.andReturn()
				.getResponse();
		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
		assertThat(response.getContentAsString()).contains("testEmail@testEmail.com", "Biba", "Boba");
	}

	@Test
	@WithMockUser
	void testCreateUserNotCorrectEmail() throws Exception {
		MockHttpServletResponse responsePost = mockMvc
				.perform(
						post(BASEURL)
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
				.perform(get(BASEURL))
				.andReturn()
				.getResponse();
		assertThat(response.getContentAsString()).doesNotContain("testEmail@testEmail.com", "Biba", "Boba");
	}

	@Test
	@WithMockUser
	void testCreateUserNoName() throws Exception {
		MockHttpServletResponse responsePost = mockMvc
				.perform(
						post(BASEURL)
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
				.perform(get(BASEURL))
				.andReturn()
				.getResponse();
		assertThat(response.getContentAsString()).doesNotContain("testEmail@testEmail.com", "Biba", "Boba");
	}

	@Test
	@WithMockUser
	void testCreateUserNoLastName() throws Exception {
		MockHttpServletResponse responsePost = mockMvc
				.perform(
						post(BASEURL)
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
				.perform(get(BASEURL))
				.andReturn()
				.getResponse();
		assertThat(response.getContentAsString()).doesNotContain("testEmail@testEmail.com", "Biba", "Boba");
	}

	@Test
	@WithMockUser
	void testCreateUserNoPassword() throws Exception {
		MockHttpServletResponse responsePost = mockMvc
				.perform(
						post(BASEURL)
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
				.perform(get(BASEURL))
				.andReturn()
				.getResponse();
		assertThat(response.getContentAsString()).doesNotContain("testEmail@testEmail.com", "Biba", "Boba");
	}

	@Test
	@WithMockUser
	void testUpdatesAnotherUser() throws Exception {
		userRepository.save(user);
		MockHttpServletResponse responsePost = mockMvc
				.perform(
						put(BASEURL + "/" + user.getId())
								.contentType(MediaType.APPLICATION_JSON)
								.content("{\"email\":\"testEmail@testEmail.com\""
										+ ",\"firstName\":\"Biba\""
										+ ",\"lastName\":\"Boba\""
										+ ",\"password\":\"somepass\"}"
								)
				)
				.andReturn()
				.getResponse();

		assertThat(responsePost.getStatus()).isEqualTo(422);
	}

	@Test
	@WithMockUser("test@test.com")
	void testUpdateSelf() throws Exception {
//		userRepository.save(user);
		MockHttpServletResponse responsePost = mockMvc
				.perform(
						put(BASEURL + "/" + testUser.getId())
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
				.perform(get(BASEURL))
				.andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
		assertThat(response.getContentAsString()).contains("testEmail@testEmail.com", "Biba", "Boba");
	}

	@Test
	@WithMockUser
	void testUpdatesUserNotCorrecEmail() throws Exception {
		userRepository.save(user);
		MockHttpServletResponse responsePost = mockMvc
				.perform(
						put(BASEURL + "/" + user.getId())
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
				.perform(get(BASEURL))
				.andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
		assertThat(response.getContentAsString()).doesNotContain("testEmail@testEmail.com", "Biba", "Boba");
	}

	@Test
	@WithMockUser
	void testDeleteAnotherPerson() throws Exception {
		userRepository.save(user);
		MockHttpServletResponse responsePost = mockMvc
				.perform(delete(BASEURL + "/" + user.getId()))
				.andReturn()
				.getResponse();

		assertThat(responsePost.getStatus()).isEqualTo(422);

		MockHttpServletResponse response = mockMvc
				.perform(get(BASEURL))
				.andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
		assertThat(response.getContentAsString()).contains(user.getFirstName(), user.getLastName());
	}
}
