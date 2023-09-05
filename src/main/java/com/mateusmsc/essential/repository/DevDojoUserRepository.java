package com.mateusmsc.essential.repository;

import com.mateusmsc.essential.domain.DevDojoUser;
import org.springframework.data.jpa.repository.JpaRepository;

// Responsável pelas "querys" no banco de dados
// Adiciona no repo a Classe e o atributo do tipo do ID
public interface DevDojoUserRepository extends JpaRepository<DevDojoUser, Long> {

    DevDojoUser findByUsername(String username);
}
