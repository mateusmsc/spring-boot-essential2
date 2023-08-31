package com.mateusmsc.essential.controller;

import com.mateusmsc.essential.domain.Anime;
import com.mateusmsc.essential.dto.AnimeDTO;
import com.mateusmsc.essential.service.AnimeService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

// Utilizar o JUnit em vez de utilizarmos direto o BD do docker por exemplo
@ExtendWith(SpringExtension.class)
class AnimeControllerTest {
    //@injectMocks : Utilizar quando queremos testar a classe em sí
    //@Mock : Utilizar quando queremos testar com todas as classes injetadas
    @InjectMocks
    private AnimeController animeController;
    @Mock
    private AnimeService animeServiceMock;

    @BeforeEach
    public void setup() {
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeSupport.createAnimeValid()));
        List<Anime> animeList = List.of(AnimeSupport.createAnimeValid());

        BDDMockito.when(animeServiceMock.listAll(ArgumentMatchers.any()))
                .thenReturn(animePage);

        BDDMockito.when(animeServiceMock.listAllNonPageable())
                .thenReturn(animeList);

        BDDMockito.when(animeServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn(AnimeSupport.createAnimeValid());

        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(animeList);

        //todo
        // Criar mock para quando o nome do anime vier vazio ou nulo
        BDDMockito.when(animeServiceMock.save(ArgumentMatchers.any(AnimeDTO.class)))
                .thenReturn(AnimeSupport.createAnimeValid());

        BDDMockito.doNothing().when(animeServiceMock).replace(ArgumentMatchers.any(AnimeDTO.class));

        BDDMockito.doNothing().when(animeServiceMock).delete(ArgumentMatchers.anyLong());
    }

    @Test
    void listAll() {
        Page<Anime> animePage = animeController.list(null).getBody();

        Assertions.assertNotNull(animePage, "Animes deveria retornar uma lista.");
        Assertions.assertEquals(1, animePage.toList().size(), "Tamanho da lista difere do experado.");
        Assertions.assertEquals("Primeiro anime", animePage.toList().get(0).getName(), "Nome do anime difere do esperado");
    }

    @Test
    void listAllNonPageable() {
        List<Anime> animeList = animeController.listAll().getBody();

        Assertions.assertNotNull(animeList, "Animes deveria retornar uma lista.");
        Assertions.assertEquals(1, animeList.size(), "Tamanho da lista difere do experado.");
        Assertions.assertEquals("Primeiro anime", animeList.get(0).getName(), "Nome do anime difere do esperado");
    }

    @Test
    void findById() {
        Anime anime = animeController.findById(1L).getBody();

        Assertions.assertNotNull(anime, "Animes deveria retornar uma lista.");
        Assertions.assertEquals("Primeiro anime", anime.getName(), "Nome do anime difere do esperado");
    }

    @Test
    void findByName() {
        List<Anime> animeListByName = animeController.findByName("name").getBody();

        Assertions.assertNotNull(animeListByName, "Animes deveria retornar uma lista.");
        Assertions.assertEquals(1, animeListByName.size(), "Tamanho da lista difere do experado.");
        Assertions.assertEquals("Primeiro anime", animeListByName.get(0).getName(), "Nome do anime difere do esperado");
    }

    @Test
    void tryFindByName() {
        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Anime> animeListByName = animeController.findByName("name").getBody();

        Assertions.assertNotNull(animeListByName, "Animes deveria retornar uma lista.");
        Assertions.assertTrue(animeListByName.isEmpty(),"Lista de animes deveria está vazia");
    }

    @Test
    void save() {
        AnimeDTO animeDTO = new AnimeDTO();
        animeDTO.setName("Primeiro anime");
        Anime anime = animeController.save(animeDTO).getBody();

        Assertions.assertNotNull(anime, "Anime deveria ser salvo.");
        Assertions.assertEquals("Primeiro anime", anime.getName(),"Lista de animes deveria está vazia");
    }

    @Test
    void updateAnime() {
        AnimeDTO animeDTO = new AnimeDTO();
        animeDTO.setName("Primeiro anime");
        ResponseEntity<Void> entity = animeController.replace(animeDTO);

        Assertions.assertNotNull(entity, "Anime deveria ser atualizado.");
    }

    @Test
    void deleteAnime() {
        AnimeDTO animeDTO = new AnimeDTO();
        animeDTO.setName("Primeiro anime");
        ResponseEntity<Void> entity = animeController.delete(1);

        Assertions.assertNotNull(entity, "Anime deveria ser atualizado.");
    }
}