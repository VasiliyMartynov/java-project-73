package hexlet.code.services;

import hexlet.code.dto.Label.LabelCreateDTO;
import hexlet.code.dto.Mappers.LabelMapper;
import hexlet.code.dto.Label.LabelShowDTO;
import hexlet.code.dto.Label.LabelUpdateDTO;
import hexlet.code.exceptions.ResourceNotFoundException;
import hexlet.code.models.Label;
import hexlet.code.repository.LabelRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class LabelService {

    private LabelRepository labelRepository;
    private LabelMapper labelMapper;

    public LabelShowDTO getLabel(long id) {
        Label l = labelRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(id + " not found")
        );
        return labelMapper.INSTANCE.toLabelShowDTO(l);
    }

    public List<LabelShowDTO> getLabels() {
        try {
            List<Label> labels = labelRepository.findAll();
            return labels
                    .stream()
                    .map(l -> labelMapper.INSTANCE.toLabelShowDTO(l))
                    .collect(Collectors.toList());
        } catch (NullPointerException e) {
            throw new NullPointerException("There are no any label");
        }
    }

    public LabelShowDTO createLabel(LabelCreateDTO newLabel) throws Exception {
        if (labelRepository.findByName(newLabel.getName()).isPresent()) {
            throw new Exception(
                    "There is an task status:" + newLabel.getName());
        }
        try {
            var label = labelMapper.INSTANCE.toLabel(newLabel);
            labelRepository.save(label);
            labelRepository.flush();
            return labelMapper.INSTANCE.toLabelShowDTO(label);
        } catch (Exception e) {
            throw new Exception("Something where wrong");
        }
    }

    public LabelShowDTO updateLabel(long id, LabelUpdateDTO newLabel) {
        try {
            Label label = labelRepository.findById(id).orElseThrow();
            label.setName(newLabel.getName());
            labelRepository.save(label);
            return labelMapper.INSTANCE.toLabelShowDTO(label);

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
