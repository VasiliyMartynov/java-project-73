package hexlet.code.mappers;

import hexlet.code.dto.label.LabelCreateDTO;
import hexlet.code.dto.label.LabelShowDTO;
import hexlet.code.models.Label;
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
public interface LabelMapper {

    LabelMapper INSTANCE = Mappers.getMapper(LabelMapper.class);
    LabelShowDTO toLabelShowDTO(Label l);

    Label toLabel(LabelCreateDTO l);
}
