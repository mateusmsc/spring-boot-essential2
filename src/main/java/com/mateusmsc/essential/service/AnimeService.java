package com.mateusmsc.essential.service;

import com.mateusmsc.essential.domain.Anime;
import com.mateusmsc.essential.dto.AnimeDTO;
import com.mateusmsc.essential.mapper.AnimeMapper;
import com.mateusmsc.essential.repository.AnimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

//Responsável pela classe de negócio
@Service
@RequiredArgsConstructor
public class AnimeService  {


    private final AnimeRepository animeRepository;
    private final AnimeMapper animeMapper;
    public List<Anime> listAll() {
        return animeRepository.findAll();
    }

    public Anime findByIdOrThrowBadRequestException(long id) {
        return animeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Anime not found!"));
    }

    public Anime save(AnimeDTO animeDTO) {
        Anime anime = animeMapper.toAnime(animeDTO);

        return animeRepository.save(anime);
    }

    public void delete(long id) {
        animeRepository.delete(findByIdOrThrowBadRequestException(id));
    }

    public void replace(AnimeDTO animeDTO) {
        // Validar se o anime existe antes de atualizar;
        // Bad request se o ID não estiver no BD
        findByIdOrThrowBadRequestException(animeDTO.getId());
        Anime anime = animeMapper.toAnime(animeDTO);

        animeRepository.save(anime);
    }
}
