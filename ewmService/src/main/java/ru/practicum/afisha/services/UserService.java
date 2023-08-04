package ru.practicum.afisha.services;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.afisha.dto.UserDto;

import java.util.List;

@Service
public interface UserService {
    UserDto createUser(UserDto userDto);

    List<UserDto> getUser(List<Long> ids, Pageable page);

    void deleteUser(long userId);
}
