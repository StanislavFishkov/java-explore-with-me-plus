package ru.practicum.ewm.location.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewLocationDto {
    private double lat;
    private double lon;
    @Size(min = 3, max = 255)
    private String name;
    @Size(min = 3, max = 1000)
    private String address;
}