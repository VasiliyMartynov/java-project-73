package hexlet.code.controller;

import hexlet.code.config.TestConfig;
import hexlet.code.dto.Label.LabelCreateDTO;
import hexlet.code.models.Label;
import hexlet.code.repository.LabelRepository;
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
import static hexlet.code.config.TestConfig.TEST_PROFILE;
import static hexlet.code.utils.TestUtils.LABEL_BASEURL;
import static hexlet.code.utils.TestUtils.asJson;
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
    private LabelRepository labelRepository;
    @Autowired
    private TestUtils utils;
    @Autowired
    private InstansioModelGenerator instansioModelGenerator;

    private Label label;
    @BeforeEach
    public void setUp() {
        labelRepository.deleteAll();
        label = Instancio.of(instansioModelGenerator.getLabelModel())
                .create();
        labelRepository.save(label);
    }
    @Test
    void testGetLabelIfLabelPersist() throws Exception {
        final var response = utils.performAuthorizedRequest(get(LABEL_BASEURL + "/" + label.getId()))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains(label.getName());
    }

    @Test
    void testGetLabelIfLabelNotPersist() throws Exception {
        var nonExistentID  = labelRepository.findAll().size() + 1;
        final var response = utils.performAuthorizedRequest(get(LABEL_BASEURL + "/" + nonExistentID))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(422);
    }

    @Test
    void testGetLabelIfLabelNotPersistAndRequestIsNotCorrect() throws Exception {
        final var response = utils.performAuthorizedRequest(get(LABEL_BASEURL + "/" + "a"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(422);
    }

    @Test
    void testGetLabels() throws Exception {
        final var response = utils.performAuthorizedRequest(get(LABEL_BASEURL))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains(label.getName());
    }

    @Test
    void testCreateLabel() throws Exception {
        final var responsePost = utils.performAuthorizedRequest(
                        post(LABEL_BASEURL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJson(utils.NEW_LABEL))
                )
                .andReturn()
                .getResponse();
        assertThat(responsePost.getStatus()).isEqualTo(201);
        final var response = utils.performAuthorizedRequest(get(LABEL_BASEURL))
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains(utils.NEW_LABEL.getName());
    }

    @Test
    void testCreateLabelWithNull() throws Exception {
        LabelCreateDTO nullLabel = new LabelCreateDTO();
        final var responsePost = utils.performAuthorizedRequest(
                        post(LABEL_BASEURL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJson(nullLabel))
                )
                .andReturn()
                .getResponse();
        assertThat(responsePost.getStatus()).isEqualTo(422);
    }

    @Test
    void testUpdatesLabel() throws Exception {
        final var responsePost = utils.performAuthorizedRequest(
                        put(LABEL_BASEURL + "/" + label.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJson(utils.UPDATED_LABEL))
                )
                .andReturn()
                .getResponse();
        assertThat(responsePost.getStatus()).isEqualTo(200);

        final var response = utils.performAuthorizedRequest(get(LABEL_BASEURL + "/" + label.getId()))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains(utils.UPDATED_LABEL.getName());
    }

    @Test
    void testDeleteLabel() throws Exception {
        final var responsePost = utils.performAuthorizedRequest(delete(LABEL_BASEURL + "/" + label.getId()))
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(200);

        final var response = utils.performAuthorizedRequest(get(LABEL_BASEURL))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).doesNotContain(label.getName());
    }

    @Test
    void testDeleteLabelWithWrongParam() throws Exception {
        final var responsePost = utils.performAuthorizedRequest(delete(LABEL_BASEURL + "/a"))
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(422);
    }
}
