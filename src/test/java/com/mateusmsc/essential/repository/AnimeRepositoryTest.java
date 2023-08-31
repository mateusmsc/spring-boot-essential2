package com.mateusmsc.essential.repository;

import com.mateusmsc.essential.domain.Anime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.util.Assert;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static com.mateusmsc.essential.util.AnimeSupport.createAnimeToBeSaved;

@DataJpaTest
@DisplayName("Tests for Anime repository")
class AnimeRepositoryTest {

    @Autowired
    private AnimeRepository animeRepository;

    @Test
    public void saveAnime() {
        Anime anime = createAnimeToBeSaved();
        Anime savedAnime = animeRepository.save(anime);

        Assert.notNull(savedAnime, "Anime não foi salvo com sucesso.");
    }

    @Test
    public void updateAnime() {
        Anime anime = createAnimeToBeSaved();
        Anime savedAnime = animeRepository.save(anime);

        Assert.notNull(savedAnime, "Anime não foi salvo com sucesso.");

        savedAnime = animeRepository.findByName("Primeiro anime").get(0);
        savedAnime.setName("Outro nome qualquer");
        Anime updatedAnime = animeRepository.save(savedAnime);

        Assertions.assertEquals("Outro nome qualquer", updatedAnime.getName(),"Nome do anime não foi atualizado.");
    }

    @Test
    public void deleteAnime() {
        Anime anime = createAnimeToBeSaved();
        Anime savedAnime = animeRepository.save(anime);

        Assert.notNull(savedAnime, "Anime não foi salvo com sucesso.");

        animeRepository.delete(savedAnime);

        Optional<Anime> animeOptional = animeRepository.findById(savedAnime.getId());

        Assertions.assertTrue(animeOptional.isEmpty(), "Anime não foi deletado com sucesso.");
    }

    @Test
    public void findByNameAnime() {
        Anime anime = createAnimeToBeSaved();
        Anime savedAnime = animeRepository.save(anime);
        String name = savedAnime.getName();

        Assert.notNull(savedAnime, "Anime não foi salvo com sucesso.");

        List<Anime> animes = animeRepository.findByName(name);

        Assertions.assertFalse(animes.isEmpty(), "Lista de animes vazia.");
        Assertions.assertEquals(1, animes.size(),"Número do anime difere do esperado.");
        Assertions.assertEquals(savedAnime, animes.get(0),"Anime difere do esperado.");
    }

    @Test
    public void trySaveAnime() {
        Anime anime = new Anime();

        Assertions.assertThrows(ConstraintViolationException.class, () -> animeRepository.save(anime), "Exceção difere da esperada.");
    }
}