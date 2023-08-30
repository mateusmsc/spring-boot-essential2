package com.mateusmsc.essential.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class AnimeDTO {
    private Long id;

    @NotEmpty(message = "The anime name cannot be empty or null")
    private String name;
}
