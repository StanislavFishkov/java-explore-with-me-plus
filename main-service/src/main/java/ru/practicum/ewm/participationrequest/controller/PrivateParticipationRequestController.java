package ru.practicum.ewm.participationrequest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.participationrequest.dto.ParticipationRequestDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class PrivateParticipationRequestController {

    @PostMapping("/{userId}/requests")
    public ParticipationRequestDto create(@PathVariable Long userId, @RequestParam Long eventId) {
        return null;
    }

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> get(@PathVariable Long userId) {
        return null;
    }

    @GetMapping("/users/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancel(@PathVariable Long userId, @PathVariable Long requestId) {
        return null;
    }
}
