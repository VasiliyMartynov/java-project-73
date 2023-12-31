package hexlet.code.service;

import hexlet.code.dto.label.LabelCreateDTO;
import hexlet.code.mappers.LabelMapper;
import hexlet.code.dto.label.LabelShowDTO;
import hexlet.code.dto.label.LabelUpdateDTO;
import hexlet.code.exceptions.ResourceNotFoundException;
import hexlet.code.exceptions.SameItemException;
import hexlet.code.models.Label;
import hexlet.code.repository.LabelRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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

    /**
     * getLabel return LabelShowDTO object of Label model.
     * @param id
     * @return LabelShowDTO
     */
    public LabelShowDTO getLabel(long id) {
        Label l = labelRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(id + " not found")
        );
        return labelMapper.INSTANCE.toLabelShowDTO(l);
    }

    /**
     * getLabels return list of LabelShowDTO object of Label model.
     * @return List<LabelShowDTO>
     */
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
    /**
     * createLabel return add new Label object into database and return
     * LabelShowDTO object of Label model.
     * @param newLabel
     * @return LabelShowDTO
     */
    public LabelShowDTO createLabel(LabelCreateDTO newLabel) throws Exception {
        if (labelRepository.findByName(newLabel.getName()).isPresent()) {
            throw new SameItemException(
                    "There is an task status:" + newLabel.getName());
        }
        try {
            var label = labelMapper.INSTANCE.toLabel(newLabel);
            labelRepository.save(label);
            labelRepository.flush();
            return labelMapper.INSTANCE.toLabelShowDTO(label);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Something where wrong");
        }
    }
    /**
     * updateLabel update exist Label object in database and return
     * LabelShowDTO object of Label model.
     * @param id
     * @param newLabel
     * @return LabelShowDTO
     */
    public LabelShowDTO updateLabel(long id, LabelUpdateDTO newLabel) throws Exception {
        Label label = labelRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(id + " not found")
        );
        try {
            label.setName(newLabel.getName());
            labelRepository.save(label);
            return labelMapper.INSTANCE.toLabelShowDTO(label);

        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Something where wrong");
        }
    }
    /**
     * deleteLabel delete exist Label object in database and return
     * LabelShowDTO object of Label model.
     * @param id
     */
    public void deleteLabel(long id) {
        try {
            Optional<Label> label = labelRepository.findById(id);
            labelRepository.deleteById(label.get().getId());
        } catch (NoSuchElementException ex) {
            throw new NoSuchElementException(id + " not found");
        }
    }
}
