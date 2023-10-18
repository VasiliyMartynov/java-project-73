package hexlet.code.controllers;

import hexlet.code.dto.TaskCreateDTO;
import hexlet.code.dto.TaskDTO;
import hexlet.code.repository.TaskRepository;
import hexlet.code.services.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    //GET Task BY ID
    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    TaskDTO getTask(@PathVariable long id) {
        return taskService.getTask(id);
    }

    //GET Tasks
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    List<TaskDTO> getTasks() {
        return taskService.getTasks();
    }

    //CREATE Task
    @PostMapping(
            value = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    TaskDTO createTask(@Valid @RequestBody TaskCreateDTO taskDTO) throws Exception {
        return taskService.createTask(taskDTO);
    }

    //UPDATE TASK BY ID
    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    TaskDTO updateTask(@PathVariable long id, @RequestBody TaskCreateDTO task) {
        return taskService.updateTask(id, task);
    }

    //DELETE TASK BY ID
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    void deleteTask(@PathVariable long id) {
        taskService.deleteTask(id);
    }
}
