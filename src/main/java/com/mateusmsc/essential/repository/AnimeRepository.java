package com.mateusmsc.essential.repository;

import com.mateusmsc.essential.domain.Anime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Responsável pelas "querys" no banco de dados
// Adiciona no repo a Classe e o atributo do tipo do ID
public interface AnimeRepository extends JpaRepository<Anime, Long> {

    List<Anime> findByName(String name);
}
