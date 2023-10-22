package hexlet.code.dto.Mappers;

import hexlet.code.dto.Task.TaskCreateDTO;
import hexlet.code.dto.Task.TaskShowDTO;
import hexlet.code.dto.Task.TaskUpdateDTO;
import hexlet.code.models.Task;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;


@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TaskMapper {

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);
    TaskShowDTO showTask(Task t);


//    Task createTask(TaskCreateDTO t);

//    void updateTask(TaskUpdateDTO dto, @MappingTarget Task task);
}
