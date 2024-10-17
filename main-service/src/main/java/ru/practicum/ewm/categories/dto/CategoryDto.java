package ru.practicum.ewm.categories.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryDto {
    @Length(min = 1, max = 50, message = "Название категории должно быть от 1 до 50 символов")
    @NotBlank(message = "Название категории не может быть пустым")
    String name;
}
