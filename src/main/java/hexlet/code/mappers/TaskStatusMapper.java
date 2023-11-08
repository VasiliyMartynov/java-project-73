package hexlet.code.mappers;

import hexlet.code.dto.taskStatus.TaskStatusCreateDTO;
import hexlet.code.dto.taskStatus.TaskStatusShowDTO;
import hexlet.code.models.TaskStatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;


@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TaskStatusMapper {

    TaskStatusMapper INSTANCE = Mappers.getMapper(TaskStatusMapper.class);
    TaskStatusShowDTO toTaskStatusShowDTO(TaskStatus ts);

    TaskStatus toTaskStatus(TaskStatusCreateDTO ts);
}
