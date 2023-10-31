package hexlet.code.controllers;

import hexlet.code.dto.TaskStatus.TaskStatusCreateDTO;
import hexlet.code.dto.TaskStatus.TaskStatusShowDTO;
import hexlet.code.dto.TaskStatus.TaskStatusUpdateDTO;
import hexlet.code.services.TaskStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
import static hexlet.code.controllers.TaskStatusController.TASK_STATUS_CONTROLLER_PATH;

@RestController
@RequestMapping("${base-url}" + TASK_STATUS_CONTROLLER_PATH)
@AllArgsConstructor
public class TaskStatusController {
    public static final String TASK_STATUS_CONTROLLER_PATH = "/statuses";
    private TaskStatusService taskStatusService;
    public static final String ID = "/{id}";

    @Operation(summary = "Get item by ID")
    @ApiResponses(value = {
        @ApiResponse(
                    responseCode = "200", description = "Found item",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskStatusShowDTO.class)) }),
        @ApiResponse(
                    responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
        @ApiResponse(
                    responseCode = "403", description = "Unauthorized",
                    content = @Content),
        @ApiResponse(
                    responseCode = "404", description = "not found",
                    content = @Content),
        @ApiResponse(
                    responseCode = "422", description = "Wrong data",
                    content = @Content)
    })
    @GetMapping(ID)
    @ResponseStatus(HttpStatus.OK)
    TaskStatusShowDTO getTaskStatus(
            @Parameter(description = "id of item to be find")
            @PathVariable long id) {
        return taskStatusService.getTaskStatus(id);
    }

    @Operation(summary = "Get all items")
    @ApiResponses(value = {
        @ApiResponse(
                    responseCode = "200", description = "List of  items",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskStatusShowDTO.class)) }),
        @ApiResponse(
                    responseCode = "403", description = "Unauthorized",
                    content = @Content),
    })
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    List<TaskStatusShowDTO> getTaskStatuses() {
        return taskStatusService.getTaskStatuses();
    }

    @Operation(summary = "Create item")
    @ApiResponses(value = {
        @ApiResponse(
                    responseCode = "201", description = "Created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskStatusShowDTO.class)) }),
        @ApiResponse(
                    responseCode = "403", description = "Unauthorized",
                    content = @Content),
        @ApiResponse(
                    responseCode = "422", description = "Wrong data",
                    content = @Content)
    })
    @PostMapping(
//            value = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    TaskStatusShowDTO createTaskStatus(
            @Parameter(description = "DTO object to create")
            @Valid @RequestBody TaskStatusCreateDTO task) throws Exception {
        return taskStatusService.createTaskStatus(task);
    }

    @Operation(summary = "Update item by Id")
    @ApiResponses(value = {
        @ApiResponse(
                    responseCode = "200", description = "updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskStatusShowDTO.class)) }),
        @ApiResponse(
                    responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
        @ApiResponse(
                    responseCode = "403", description = "Unauthorized",
                    content = @Content),
        @ApiResponse(
                    responseCode = "404", description = "Not found",
                    content = @Content),

        @ApiResponse(
                    responseCode = "422", description = "Wrong data",
                    content = @Content)})
    @PutMapping(
            value = ID,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    TaskStatusShowDTO updateTaskStatus(
            @Parameter(description = "id and DTO of item to be update")
            @PathVariable long id, @RequestBody TaskStatusUpdateDTO task) {
        return taskStatusService.updateTaskStatus(id, task);
    }

    @Operation(summary = "Delete item by Id")
    @ApiResponses(value = {
        @ApiResponse(
                    responseCode = "200", description = "Deleted",
                    content = @Content),
        @ApiResponse(
                    responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
        @ApiResponse(
                    responseCode = "403", description = "Unauthorized",
                    content = @Content),
        @ApiResponse(
                    responseCode = "404", description = "Not found",
                    content = @Content),
        @ApiResponse(
                    responseCode = "422", description = "Wrong data",
                    content = @Content)
    })
    @DeleteMapping(ID)
    @ResponseStatus(HttpStatus.OK)
    void deleteTaskStatus(
            @Parameter(description = "id of item to be delete")
            @PathVariable long id) {
        taskStatusService.deleteTaskStatus(id);
    }
}
