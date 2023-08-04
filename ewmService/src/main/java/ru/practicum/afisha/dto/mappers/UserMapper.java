package ru.practicum.afisha.dto.mappers;

import org.mapstruct.Mapper;
import ru.practicum.afisha.dto.UserDto;
import ru.practicum.afisha.models.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(User user);

    User toUser(UserDto userDto);
}
