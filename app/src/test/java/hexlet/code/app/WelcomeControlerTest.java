package hexlet.code.app;

import hexlet.code.controllers.WelcomeController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(WelcomeController.class)
class WelcomeControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Test
	void testRootPage() throws Exception {

		MockHttpServletResponse response = mockMvc
				.perform(get("/"))
				.andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentAsString()).contains("Welcome to Spring");
	}
}
