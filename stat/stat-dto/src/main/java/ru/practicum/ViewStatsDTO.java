package ru.practicum;

import lombok.Data;

@Data
public class ViewStatsDTO {
    private String app;
    private String uri;
    private Integer hits;
}
