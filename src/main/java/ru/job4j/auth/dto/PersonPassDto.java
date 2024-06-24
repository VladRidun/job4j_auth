package ru.job4j.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PersonPassDto {
    @Min(value = 5, message = "Password must be more than 5 symbols")
    private String password;

}
