package hexlet.code.controllers;

import hexlet.code.AppApplication;
import hexlet.code.dto.Task.TaskCreateDTO;
import hexlet.code.dto.Task.TaskShowDTO;
import hexlet.code.dto.Task.TaskUpdateDTO;
import hexlet.code.services.TaskService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.log.LogMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import java.util.List;

@RestController
@RequestMapping("${base-url}" + TaskController.TASK_CONTROLLER_PATH)
@AllArgsConstructor
public class TaskController {

    private static final Log logger = LogFactory.getLog(TaskController.class);
    private static final String ONLY_OWNER_BY_ID = """
            @userRepository.findById(#id).get().getEmail() == authentication.getName()
        """;
    public static final String TASK_CONTROLLER_PATH = "/tasks";
    private TaskService taskService;

    //GET Task BY ID
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    TaskShowDTO getTask(@PathVariable long id) {
        return taskService.getTask(id);
    }

    //GET Tasks
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    List<TaskShowDTO> getTasks() {
        return taskService.getTasks();
    }

    //CREATE Task
    @PostMapping(
            value = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    TaskShowDTO createTask(@Valid @RequestBody TaskCreateDTO taskDTO) throws Exception {
        return taskService.createTask(taskDTO);
    }

    //UPDATE TASK BY ID
    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    TaskShowDTO updateTask(@PathVariable long id, @RequestBody TaskUpdateDTO task) {
        return taskService.updateTask(id, task);
    }

    //DELETE TASK BY ID
    @DeleteMapping("/{taskControllerId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize(ONLY_OWNER_BY_ID)
    void deleteTask(@PathVariable Long taskControllerId) {
        System.out.println("TASK CONTROLLER ------------------------------------------------------");
        logger.info(LogMessage.format("TASK CONTROLLER trying to delete task with ID: %s", taskControllerId));
        taskService.deleteTask(taskControllerId);
    }
}
