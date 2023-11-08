package hexlet.code.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.component.JWTHelper;
import hexlet.code.dto.label.LabelCreateDTO;
import hexlet.code.dto.label.LabelUpdateDTO;
import hexlet.code.dto.taskStatus.TaskStatusCreateDTO;
import hexlet.code.dto.user.UserCreateDTO;
import hexlet.code.dto.user.UserUpdateDTO;
import hexlet.code.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import java.util.Map;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY;

@Component
public class TestUtils {
    public static final String TEST_USERNAME_1 = "test@test.com";
    public static final String LABEL_BASEURL = "/api/labels";
    public static final String TASK_STATUSES_BASEURL = "/api/statuses";
    public static final String USERS_BASEURL = "/api/users";
    public static final String TASKS_BASEURL = "/api/tasks";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JWTHelper jwtHelper;
    @Autowired
    private LabelService labelService;

    public static final LabelCreateDTO NEW_LABEL = new LabelCreateDTO("newLabel");
    public static final LabelUpdateDTO UPDATED_LABEL = new LabelUpdateDTO("updatedLabel");
    public static final TaskStatusCreateDTO NEW_TASK_STATUS = new TaskStatusCreateDTO("newStatus");
    public static final TaskStatusCreateDTO UPDATED_TASK_STATUS = new TaskStatusCreateDTO("updated status");

    public static final UserCreateDTO USER_CREATE_DTO = new UserCreateDTO(
            "alibaba@test.com",
            "Biba",
            "Boba",
            "1234");
    public static final UserUpdateDTO USER_UPDATE_DTO = new UserUpdateDTO(
            "bububu@test.com",
            "buba",
            "biba",
            "4321");

    public ResultActions performAuthorizedRequest(final MockHttpServletRequestBuilder request) throws Exception {
        final String token = jwtHelper.expiring(Map.of(SPRING_SECURITY_FORM_USERNAME_KEY, TEST_USERNAME_1));
        request.header(AUTHORIZATION, token);
        return perform(request);
    }

    public ResultActions performAuthorizedRequest(
            final MockHttpServletRequestBuilder request, String newUser)
            throws Exception {
        final String token = jwtHelper.expiring(Map.of(SPRING_SECURITY_FORM_USERNAME_KEY, newUser));
        request.header(AUTHORIZATION, token);
        return perform(request);
    }

    public ResultActions perform(final MockHttpServletRequestBuilder request) throws Exception {
        return mockMvc.perform(request);
    }

    private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

    public static String asJson(final Object object) throws JsonProcessingException {
        return MAPPER.writeValueAsString(object);
    }

    public static <T> T fromJson(final String json, final TypeReference<T> to) throws JsonProcessingException {
        return MAPPER.readValue(json, to);
    }
}
