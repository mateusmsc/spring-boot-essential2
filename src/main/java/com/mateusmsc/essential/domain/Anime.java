package com.mateusmsc.essential.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
// Classe do lombok para poder buildar as coisas
@Builder
public class Anime {
    //Reflete o modelo no banco de dados
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

//    @JsonProperty
    // utilizado para mapear do request para o atributo
    @NotEmpty(message = "The anime name cannot be empty or null")
    private String name;
}