package ru.practicum.ewm.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.core.error.exception.InternalServerException;
import ru.practicum.ewm.core.error.exception.NotFoundException;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserRequestDto;
import ru.practicum.ewm.user.dto.UserShortDto;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserShortDto getById(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(
                String.format("Пользователь с ид %s не найден", userId))
        );
        return userMapper.toShortDto(user);
    }

    @Override
    public List<UserShortDto> getUsers(List<String> ids) {
        return null;
    }

    @Override
    public List<UserDto> getUsers(List<String> ids, int from, int size) {
        return userRepository.findAll().stream().map(userMapper::toDto).toList();
    }

    @Override
    public UserDto registerUser(UserRequestDto userRequestDto) {
        User user = userRepository.save(userMapper.toEntity(userRequestDto));
        if (user.getId() == null) {
            throw new InternalServerException(
                    "Ошибка создания пользователя");
        }
        return userMapper.toDto(user);
    }

    @Override
    public void delete(long userId) {
        userRepository.deleteById(userId);
    }
}
