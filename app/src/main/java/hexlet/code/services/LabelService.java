package hexlet.code.services;

import hexlet.code.models.Label;
import hexlet.code.repository.LabelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LabelService {

    @Autowired
    private LabelRepository labelRepository;
    public Label getLabel(long id) {
        return labelRepository.findById(id).orElseThrow(
//                () -> new ResourceNotFoundException(id + " not found")
        );
    }

    public List<Label> getLabels() {
        try {
            return labelRepository.findAll();
        } catch (NullPointerException e) {
            throw new NullPointerException("There are no any label");
        }
    }

    public Label createLabel(Label label) throws Exception {
        if (labelRepository.findByName(label.getName()).isPresent()) {
            throw new Exception(
                    "There is an task status:" + label.getName());
        }
        try {
            labelRepository.save(label);
            labelRepository.flush();
            return getLabel(label.getId());
        } catch (Exception e) {
            throw new Exception("Something where wrong");
        }
    }

    public Label updateLabel(long id, Label newLabel) {
        try {
            Label label = labelRepository.findById(id).orElseThrow();
            label.setName(newLabel.getName());
            labelRepository.save(label);
            return label;

        } catch (NoSuchElementException ex) {
            throw new NoSuchElementException(id + " not found");
        }
    }

    public void deleteLabel(long id) {
        try {
            Optional<Label> label = labelRepository.findById(id);
            labelRepository.deleteById(label.get().getId());
        } catch (NoSuchElementException ex) {
            throw new NoSuchElementException(id + " not found");
        }
    }
}
