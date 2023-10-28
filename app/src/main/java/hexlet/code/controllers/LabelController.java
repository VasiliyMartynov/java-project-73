package hexlet.code.controllers;

import hexlet.code.dto.Label.LabelCreateDTO;
import hexlet.code.dto.Label.LabelShowDTO;
import hexlet.code.dto.Label.LabelUpdateDTO;
import hexlet.code.repository.LabelRepository;
import hexlet.code.services.LabelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequiredArgsConstructor
public class LabelController {

    public static final String ID = "/{id}";
    public static final String LABEL_CONTROLLER_PATH = "/labels";
    @Autowired
    private LabelService labelService;

    @Autowired
    private LabelRepository labelRepository;

    //GET Label BY ID
    @GetMapping(ID)
    @ResponseStatus(HttpStatus.OK)
    LabelShowDTO getLabel(@PathVariable long id) {
        return labelService.getLabel(id);
    }

    //GET Tasks
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    List<LabelShowDTO> getLabels() {
        return labelService.getLabels();
    }

    //CREATE Label
    @PostMapping(
            value = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    LabelShowDTO createLabel(@Valid @RequestBody LabelCreateDTO label) throws Exception {
        return labelService.createLabel(label);
    }

    //UPDATE Label BY ID
    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    LabelShowDTO updateLabel(@PathVariable long id, @RequestBody LabelUpdateDTO label) {
        return labelService.updateLabel(id, label);
    }

    //DELETE Label BY ID
    @DeleteMapping(ID)
    @ResponseStatus(HttpStatus.OK)
    void deleteLabel(@PathVariable long id) {
        labelService.deleteLabel(id);
    }
}
