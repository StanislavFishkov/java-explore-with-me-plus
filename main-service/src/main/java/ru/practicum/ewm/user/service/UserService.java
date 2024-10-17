package ru.practicum.ewm.user.service;

import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserRequestDto;
import ru.practicum.ewm.user.dto.UserShortDto;

import java.util.List;

public interface UserService {

    UserShortDto getById(long userId);

    List<UserShortDto> getUsers(List<String> ids);

    List<UserDto> getUsers(List<String> ids, int from, int size);

    UserDto registerUser(UserRequestDto userRequestDto);

    void delete(long userId);
}
