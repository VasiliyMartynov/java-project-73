package hexlet.code.dto.Mappers;

import hexlet.code.dto.User.UserCreateDTO;
import hexlet.code.dto.User.UserShowDTO;
import hexlet.code.dto.User.UserUpdateDTO;
import hexlet.code.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(
//        uses = { JsonNullableMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserShowDTO showUser(User u);

    User createUser(UserCreateDTO u);

    void updateUser(UserUpdateDTO dto, @MappingTarget User user);
}
