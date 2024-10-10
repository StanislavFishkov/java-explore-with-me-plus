package ru.practicum;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EndpointHitDTO {
    private Integer id;
    private String app;
    private String uri;
    private String ip;
    private LocalDateTime timestamp;
}
