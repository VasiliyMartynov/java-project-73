package hexlet.code.app;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import org.springframework.http.MediaType;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AppApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	private static final String BASEURL = "/api/v1.0";
	@Test
	void testRootPage() throws Exception {
		MockHttpServletResponse response = mockMvc
				.perform(get("/"))
				.andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentAsString()).contains("Welcome to Spring");
	}

	@Test
	void testGetUser() throws Exception {
		MockHttpServletResponse response = mockMvc
				.perform(get(BASEURL + "/users/1"))
				.andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
		assertThat(response.getContentAsString()).contains("John", "Smith");
	}

	@Test
	void testGetUsers() throws Exception {
		MockHttpServletResponse response = mockMvc
				.perform(get(BASEURL + "/users/"))
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
						post(BASEURL + "/users/")
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
				.perform(get(BASEURL + "/users/"))
				.andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
		assertThat(response.getContentAsString()).contains("testEmail@testEmail.com","Biba", "Boba");
	}

	@Test
	void testUpdatesUser() throws Exception {
		MockHttpServletResponse responsePost = mockMvc
				.perform(
						patch(BASEURL + "/users/1")
								.contentType(MediaType.APPLICATION_JSON)
								.content("{\"firstName\":"
										+ " \"Johny\", "
										+ "\"lastName\":"
										+ " \"Walker\"}")
				)
				.andReturn()
				.getResponse();

		assertThat(responsePost.getStatus()).isEqualTo(200);

		MockHttpServletResponse response = mockMvc
				.perform(get(BASEURL + "/users/"))
				.andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
		assertThat(response.getContentAsString()).contains("Johny", "Walker");
	}

	@Test
	void testDeletePerson() throws Exception {
		MockHttpServletResponse responsePost = mockMvc
				.perform(delete(BASEURL + "/users/1"))
				.andReturn()
				.getResponse();

		assertThat(responsePost.getStatus()).isEqualTo(200);

		MockHttpServletResponse response = mockMvc
				.perform(get(BASEURL + "/users/"))
				.andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
		assertThat(response.getContentAsString()).doesNotContain("John", "Smith");
	}


}
