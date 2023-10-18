package hexlet.code.controllers;

import hexlet.code.models.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.services.TaskStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/statuses")
@RequiredArgsConstructor
public class TaskStatusController {

    @Autowired
    private TaskStatusService taskStatusService;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    //GET Task Status BY ID
    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    TaskStatus getTaskStatus(@PathVariable long id) {
        return taskStatusService.getTaskStatus(id);
    }

    //GET Task Statuses
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    List<TaskStatus> getTaskStatuses() {
        return taskStatusService.getTaskStatuses();
    }

    //CREATE Task Status
    @PostMapping(
            value = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    TaskStatus createTaskStatus(@Valid @RequestBody TaskStatus task) throws Exception {
        return taskStatusService.createTaskStatus(task);
    }

    //UPDATE Task Status BY ID
    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    TaskStatus updateTaskStatus(@PathVariable long id, @RequestBody TaskStatus task) {
        return taskStatusService.updateTaskStatus(id, task);
    }

    //DELETE Task Status BY ID
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    void deleteTaskStatus(@PathVariable long id) {
        taskStatusService.deleteTaskStatus(id);
    }
}
