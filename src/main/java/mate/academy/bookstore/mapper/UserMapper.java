package mate.academy.bookstore.mapper;

import mate.academy.bookstore.dto.UserRegistrationRequestDto;
import mate.academy.bookstore.dto.UserResponseDto;
import mate.academy.bookstore.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User toModel(UserRegistrationRequestDto requestDto);
}
