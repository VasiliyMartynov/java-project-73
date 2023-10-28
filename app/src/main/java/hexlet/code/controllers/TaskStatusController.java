package hexlet.code.controllers;

import hexlet.code.dto.TaskStatus.TaskStatusCreateDTO;
import hexlet.code.dto.TaskStatus.TaskStatusShowDTO;
import hexlet.code.dto.TaskStatus.TaskStatusUpdateDTO;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.services.TaskStatusService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import static hexlet.code.controllers.TaskStatusController.STATUSES_CONTROLLER_PATH;

@RestController
@RequestMapping("${base-url}" + STATUSES_CONTROLLER_PATH)
@AllArgsConstructor
public class TaskStatusController {
    public static final String ID = "/{id}";
    public static final String STATUSES_CONTROLLER_PATH = "/statuses";
    private TaskStatusService taskStatusService;
    private TaskStatusRepository taskStatusRepository;

    //GET Task Status BY ID
    @GetMapping(ID)
    @ResponseStatus(HttpStatus.OK)
    TaskStatusShowDTO getTaskStatus(@PathVariable long id) {
        return taskStatusService.getTaskStatus(id);
    }

    //GET Task Statuses
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    List<TaskStatusShowDTO> getTaskStatuses() {
        return taskStatusService.getTaskStatuses();
    }

    //CREATE Task Status
    @PostMapping(
            value = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    TaskStatusShowDTO createTaskStatus(@Valid @RequestBody TaskStatusCreateDTO task) throws Exception {
        return taskStatusService.createTaskStatus(task);
    }

    //UPDATE Task Status BY ID
    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    TaskStatusShowDTO updateTaskStatus(@PathVariable long id, @RequestBody TaskStatusUpdateDTO task) {
        return taskStatusService.updateTaskStatus(id, task);
    }

    //DELETE Task Status BY ID
    @DeleteMapping(ID)
    @ResponseStatus(HttpStatus.OK)
    void deleteTaskStatus(@PathVariable long id) {
        taskStatusService.deleteTaskStatus(id);
    }
}
