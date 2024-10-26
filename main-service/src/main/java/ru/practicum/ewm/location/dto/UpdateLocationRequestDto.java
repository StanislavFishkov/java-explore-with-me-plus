package ru.practicum.ewm.location.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.core.validation.NullOrNotBlank;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLocationRequestDto {
    private Double lat;
    private Double lon;
    @NullOrNotBlank
    @Size(min = 3, max = 255)
    private String name;
    @NullOrNotBlank
    @Size(min = 3, max = 1000)
    private String address;
}
