package com.mateusmsc.essential.service;

import com.mateusmsc.essential.domain.Anime;
import com.mateusmsc.essential.dto.AnimeDTO;
import com.mateusmsc.essential.exception.BadRequestException;
import com.mateusmsc.essential.mapper.AnimeMapper;
import com.mateusmsc.essential.repository.AnimeRepository;
import com.mateusmsc.essential.util.AnimeSupport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class AnimeServiceTest {

    @InjectMocks
    private AnimeService animeService;
    @Mock
    private AnimeRepository animeRepositoryMock;
    @Mock
    private AnimeMapper animeMapperMock;

    @BeforeEach
    public void setup() {
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeSupport.createAnimeValid()));
        List<Anime> animeList = List.of(AnimeSupport.createAnimeValid());

        BDDMockito.when(animeRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(animePage);

        BDDMockito.when(animeRepositoryMock.findAll())
                .thenReturn(animeList);

        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(AnimeSupport.createAnimeValid()));


        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(animeList);

        BDDMockito.when(animeRepositoryMock.save(ArgumentMatchers.any(Anime.class)))
                .thenReturn(AnimeSupport.createAnimeValid());

        BDDMockito.doNothing().when(animeRepositoryMock).delete(ArgumentMatchers.any(Anime.class));

        BDDMockito.when(animeMapperMock.toAnime(ArgumentMatchers.any(AnimeDTO.class)))
                .thenReturn(AnimeSupport.createAnimeValid());
    }

    @Test
    void listAll() {
        Page<Anime> animePage = animeService.listAll(PageRequest.of(1, 1));

        Assertions.assertNotNull(animePage, "Animes deveria retornar uma lista.");
        Assertions.assertEquals(1, animePage.toList().size(), "Tamanho da lista difere do experado.");
        Assertions.assertEquals("Primeiro anime", animePage.toList().get(0).getName(), "Nome do anime difere do esperado");
    }

    @Test
    void listAllNonPageable() {
        List<Anime> animeList = animeService.listAllNonPageable();

        Assertions.assertNotNull(animeList, "Animes deveria retornar uma lista.");
        Assertions.assertEquals(1, animeList.size(), "Tamanho da lista difere do experado.");
        Assertions.assertEquals("Primeiro anime", animeList.get(0).getName(), "Nome do anime difere do esperado");
    }

    @Test
    void findById() {
        Anime anime = animeService.findByIdOrThrowBadRequestException(1L);

        Assertions.assertNotNull(anime, "Animes deveria retornar uma lista.");
        Assertions.assertEquals("Primeiro anime", anime.getName(), "Nome do anime difere do esperado");
    }

    @Test
    void findByName() {
        List<Anime> animeListByName = animeService.findByName("name");

        Assertions.assertNotNull(animeListByName, "Animes deveria retornar uma lista.");
        Assertions.assertEquals(1, animeListByName.size(), "Tamanho da lista difere do experado.");
        Assertions.assertEquals("Primeiro anime", animeListByName.get(0).getName(), "Nome do anime difere do esperado");
    }

    @Test
    void tryFindByName() {
        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Anime> animeListByName = animeService.findByName("name");

        Assertions.assertNotNull(animeListByName, "Animes deveria retornar uma lista.");
        Assertions.assertTrue(animeListByName.isEmpty(),"Lista de animes deveria está vazia");
    }

    @Test
    void save() {
        AnimeDTO animeDTO = new AnimeDTO();
        animeDTO.setName("Primeiro anime");
        Anime anime = animeService.save(animeDTO);

        Assertions.assertNotNull(anime, "Anime deveria ser salvo.");
        Assertions.assertEquals("Primeiro anime", anime.getName(),"Lista de animes deveria está vazia");
    }

    @Test
    void updateAnime() {
        AnimeDTO animeDTO = new AnimeDTO();
        animeDTO.setName("Segundo anime");

        Assertions.assertDoesNotThrow(() ->  animeService.save(animeDTO));
    }

    @Test
    void deleteAnime() {
        Assertions.assertDoesNotThrow(() ->  animeService.delete(1L));
    }

    @Test
    void tryFindByIdNotFound() {
        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                BadRequestException.class,
                () -> animeService.findByIdOrThrowBadRequestException(1L),
                "Deveria ter dado uma exceção");
    }
}