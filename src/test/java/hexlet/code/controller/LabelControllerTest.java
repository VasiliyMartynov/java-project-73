package hexlet.code.controller;

import hexlet.code.config.TestConfig;
import hexlet.code.models.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.utils.InstansioModelGenerator;
import hexlet.code.utils.TestUtils;
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
import org.instancio.Instancio;
import static hexlet.code.config.TestConfig.TEST_PROFILE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@AutoConfigureMockMvc
@Transactional
@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = TestConfig.class)
class LabelControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private InstansioModelGenerator instansioModelGenerator;
    @Autowired
    private LabelRepository labelRepository;
    private Label label;
    @Autowired
    private TestUtils utils;
    private static final String BASEURL = "/api/labels";
    @BeforeEach
    public void setUp() {
        label = Instancio.of(instansioModelGenerator.getLabelModel())
                .create();
    }

    @Test
    void testGetLabelIfLabelPersist() throws Exception {
        labelRepository.save(label);
        final var response = utils.performAuthorizedRequest(get(BASEURL + "/" + label.getId()))
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
        final var response = utils.performAuthorizedRequest(get(BASEURL + "/" + nonExistentID))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(422);
    }

    @Test
    void testGetLabelIfLabelNotPersistAndRequestIsNotCorrect() throws Exception {
        final var response = utils.performAuthorizedRequest(get(BASEURL + "/" + "a"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(422);
    }

    @Test
    void testGetLabels() throws Exception {

        labelRepository.save(label);
        final var response = utils.performAuthorizedRequest(get(BASEURL))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains(label.getName());
    }

    @Test
    void testCreateLabel() throws Exception {
        final var responsePost = utils.performAuthorizedRequest(
                        post(BASEURL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "name": "black"
                                        }
                                        """)
                )
                .andReturn()
                .getResponse();
        assertThat(responsePost.getStatus()).isEqualTo(201);
        final var response = utils.performAuthorizedRequest(get(BASEURL))
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains("black");
    }

    @Test
    void testCreateLabelWithNull() throws Exception {
        final var responsePost = utils.performAuthorizedRequest(
                        post(BASEURL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "name": null
                                        }
                                        """)
                )
                .andReturn()
                .getResponse();
        assertThat(responsePost.getStatus()).isEqualTo(422);
    }

    @Test
    void testUpdatesLabel() throws Exception {
        labelRepository.save(label);
        labelRepository.flush();
        final var responsePost = utils.performAuthorizedRequest(
                        put(BASEURL + "/" + label.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "name": "yellow"
                                        }
                                        """)
                )
                .andReturn()
                .getResponse();
        assertThat(responsePost.getStatus()).isEqualTo(200);

        final var response = utils.performAuthorizedRequest(get(BASEURL + "/" + label.getId()))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains("yellow");
    }

    @Test
    void testDeleteLabel() throws Exception {
        labelRepository.save(label);
        final var responsePost = utils.performAuthorizedRequest(delete(BASEURL + "/" + label.getId()))
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(200);

        final var response = utils.performAuthorizedRequest(get(BASEURL))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).doesNotContain(label.getName());
    }

    @Test
    void testDeleteLabelWithWrongParam() throws Exception {

        final var responsePost = utils.performAuthorizedRequest(delete(BASEURL + "/" + label.getId()))
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(422);
    }
}
