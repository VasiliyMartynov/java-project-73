package hexlet.code.controllers;

import hexlet.code.models.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.services.LabelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/labels")
@RequiredArgsConstructor
public class LabelController {

    @Autowired
    private LabelService labelService;

    @Autowired
    private LabelRepository labelRepository;

    //GET Label BY ID
    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    Label getLabel(@PathVariable long id) {
        return labelService.getLabel(id);
    }

    //GET Tasks
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    List<Label> getLabels() {
        return labelService.getLabels();
    }

    //CREATE Label
    @PostMapping(
            value = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    Label createLabel(@Valid @RequestBody Label label) throws Exception {
        return labelService.createLabel(label);
    }

    //UPDATE Label BY ID
    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    Label updateLabel(@PathVariable long id, @RequestBody Label label) {
        return labelService.updateLabel(id, label);
    }

    //DELETE Label BY ID
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    void deleteLabel(@PathVariable long id) {
        labelService.deleteLabel(id);
    }
}
