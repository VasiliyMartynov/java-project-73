package hexlet.code.controllers;

import hexlet.code.dto.Label.LabelCreateDTO;
import hexlet.code.dto.Label.LabelShowDTO;
import hexlet.code.dto.Label.LabelUpdateDTO;
import hexlet.code.models.Label;
import hexlet.code.services.LabelService;
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
import static hexlet.code.controllers.LabelController.LABEL_CONTROLLER_PATH;

@RestController
@RequestMapping("${base-url}" + LABEL_CONTROLLER_PATH)
@AllArgsConstructor
public class LabelController {
    public static final String LABEL_CONTROLLER_PATH = "/labels";
    public static final String ID = "/{id}";
    private LabelService labelService;

    @Operation(summary = "Get item by ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Found item",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Label.class)) }),
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
    LabelShowDTO getLabel(
            @Parameter(description = "id of item to be find")
            @PathVariable long id) {
        return labelService.getLabel(id);
    }

    @Operation(summary = "Get all items")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "List of  items",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Label.class)) }),
            @ApiResponse(
                    responseCode = "403", description = "Unauthorized",
                    content = @Content),
    })
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    List<LabelShowDTO> getLabels() {
        return labelService.getLabels();
    }

    @Operation(summary = "Create item")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "Created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Label.class)) }),
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
    LabelShowDTO createLabel(
            @Parameter(description = "DTO object to create")
            @Valid @RequestBody LabelCreateDTO label) throws Exception {
        return labelService.createLabel(label);
    }

    @Operation(summary = "Update item by Id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Label.class)) }),
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
    @PutMapping(
            value = ID,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    LabelShowDTO updateLabel(
            @Parameter(description = "id and DTOof item to be update")
            @PathVariable long id, @RequestBody LabelUpdateDTO label) {
        return labelService.updateLabel(id, label);
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
    void deleteLabel(
            @Parameter(description = "id of item to be delete")
            @PathVariable long id) {
        labelService.deleteLabel(id);
    }
}
