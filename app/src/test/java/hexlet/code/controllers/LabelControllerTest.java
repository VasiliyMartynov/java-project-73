package hexlet.code.controllers;

import hexlet.code.models.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.utils.InstansioModelGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.instancio.Instancio;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class LabelControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private LabelRepository labelRepository;

	@Autowired
	private InstansioModelGenerator instansioModelGenerator;

	private Label label;

	@BeforeEach
	public void setUp() {
		label = Instancio.of(instansioModelGenerator.getLabelModel())
				.create();
	}

	@Test
	void testGetLabelIfLabelPersist() throws Exception {
		labelRepository.save(label);
		MockHttpServletResponse response = mockMvc
				.perform(get("/labels/" + label.getId()))
				.andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
		assertThat(response.getContentAsString()).contains(label.getName());
	}

	@Test
	void testGetLabelIfLabelNotPersist() throws Exception {
		labelRepository.save(label);
		var nonExistentID  = labelRepository.findAll().size() + 1;
		MockHttpServletResponse response = mockMvc
				.perform(get("/labels/" + nonExistentID))
				.andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(422);
	}

	@Test
	void testGetLabelIfLabelNotPersistAndRequestIsNotCorrect() throws Exception {
		MockHttpServletResponse response = mockMvc
				.perform(get("/labels/a"))
				.andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(400);
	}

	@Test
	void testGetLabels() throws Exception {
		labelRepository.save(label);
		MockHttpServletResponse response = mockMvc
				.perform(get("/labels"))
				.andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
		assertThat(response.getContentAsString()).contains(label.getName());
	}


	@Test
	void testCreateLabel() throws Exception {
		MockHttpServletResponse responsePost = mockMvc
				.perform(
						post("/labels")
								.contentType(MediaType.APPLICATION_JSON)
								.content("{\"name\":\"black\"}"
								)
				)
				.andReturn()
				.getResponse();
		assertThat(responsePost.getStatus()).isEqualTo(200);
		MockHttpServletResponse response = mockMvc
				.perform(get("/labels"))
				.andReturn()
				.getResponse();
		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
		assertThat(response.getContentAsString()).contains("black");
	}

	@Test
	void testCreateLabelWithNull() throws Exception {
		MockHttpServletResponse responsePost = mockMvc
				.perform(
						post("/labels")
								.contentType(MediaType.APPLICATION_JSON)
								.content("{\"name\":null}"
								)
				)
				.andReturn()
				.getResponse();
		assertThat(responsePost.getStatus()).isEqualTo(400);
	}

	@Test
	void testUpdatesLabel() throws Exception {
		labelRepository.save(label);
		MockHttpServletResponse responsePost = mockMvc
				.perform(
						put("/labels/" + label.getId())
								.contentType(MediaType.APPLICATION_JSON)
								.content("{\"name\":\"yellow\"}"
								)
				)
				.andReturn()
				.getResponse();

		assertThat(responsePost.getStatus()).isEqualTo(200);

		MockHttpServletResponse response = mockMvc
				.perform(get("/labels/" + label.getId()))
				.andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
		assertThat(response.getContentAsString()).contains("yellow");
	}

	@Test
	void testDeleteLabel() throws Exception {
		labelRepository.save(label);
		MockHttpServletResponse responsePost = mockMvc
				.perform(delete("/labels/" + label.getId()))
				.andReturn()
				.getResponse();

		assertThat(responsePost.getStatus()).isEqualTo(200);

		MockHttpServletResponse response = mockMvc
				.perform(get("/labels"))
				.andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
		assertThat(response.getContentAsString()).doesNotContain(label.getName());
	}
}
