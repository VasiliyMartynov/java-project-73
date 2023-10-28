package hexlet.code.dto.Mappers;

import hexlet.code.dto.Task.TaskShowDTO;
import hexlet.code.models.Task;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
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
