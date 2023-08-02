package ru.practicum.afisha.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.afisha.dto.UserDto;
import ru.practicum.afisha.dto.mappers.UserMapper;
import ru.practicum.afisha.exceptions.ConflictException;
import ru.practicum.afisha.exceptions.NoSuchUser;
import ru.practicum.afisha.repositories.UserRepository;
import ru.practicum.afisha.services.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    public UserDto createUser(UserDto userDto) {
        if (repository.existsUserByName(userDto.getName())) {
            throw new ConflictException("Can't create a user with duplicate name");
        }
        return mapper.toUserDto(repository.save(mapper.toUser(userDto)));
    }

    public List<UserDto> getUser(List<Long> ids, Pageable page) {
        if (ids != null) {
            return repository.findByIdIn(ids, page).stream().map(mapper::toUserDto).collect(Collectors.toList());
        }
        return repository.findAll(page).stream().map(mapper::toUserDto).collect(Collectors.toList());
    }

    public void deleteUser(long userId) {
        repository.findById(userId).orElseThrow(() -> new NoSuchUser("User was not found"));
        repository.deleteById(userId);
    }
}
