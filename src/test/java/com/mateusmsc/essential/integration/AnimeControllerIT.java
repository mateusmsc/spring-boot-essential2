package com.mateusmsc.essential.integration;

import com.mateusmsc.essential.domain.Anime;
import com.mateusmsc.essential.domain.DevDojoUser;
import com.mateusmsc.essential.dto.AnimeDTO;
import com.mateusmsc.essential.repository.AnimeRepository;
import com.mateusmsc.essential.repository.DevDojoUserRepository;
import com.mateusmsc.essential.util.AnimeSupport;
import com.mateusmsc.essential.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
// Reinicia o BD antes de cada teste
// faz a limpeza do env
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AnimeControllerIT {
    @Autowired
    @Qualifier(value = "testRestTemplateRoleUser")
    private TestRestTemplate testRestTemplateUser;
    @Autowired
    @Qualifier(value = "testRestTemplateRoleAdmin")
    private TestRestTemplate testRestTemplateAdmin;
    @Autowired
    private AnimeRepository animeRepository;
    @Autowired
    private DevDojoUserRepository devDojoUserRepository;
    private static final DevDojoUser USER = DevDojoUser.builder()
            .name("devdojo usuário")
                        .username("devdojo")
                        .password("{bcrypt}$2a$10$MhjPZLBT0Q/d.3xKNdsliO5UaB9Q6mz0rXKS3ta6sB3MSVkTQaSN2")
                        .authorities("ROLE_USER")
                        .build();

    private static final DevDojoUser ADMIN = DevDojoUser.builder()
            .name("Mateus usuário")
            .username("mateus")
            .password("{bcrypt}$2a$10$MhjPZLBT0Q/d.3xKNdsliO5UaB9Q6mz0rXKS3ta6sB3MSVkTQaSN2")
            .authorities("ROLE_USER,ROLE_ADMIN")
            .build();

    @TestConfiguration
    @Lazy
    static class Config {
        //
        @Bean(name = "testRestTemplateRoleUser")
        public TestRestTemplate testRestTemplateRoleUserCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:"+port)
                    .basicAuthentication("devdojo", "academy");
            return new TestRestTemplate(restTemplateBuilder);
        }

        @Bean(name = "testRestTemplateRoleAdmin")
        public TestRestTemplate testRestTemplateRoleAdminCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:"+port)
                    .basicAuthentication("mateus", "academy");
            return new TestRestTemplate(restTemplateBuilder);
        }
    }
    @Test
    void listAll() {
        Anime savedAnime = animeRepository.save(AnimeSupport.createAnimeToBeSaved());
        devDojoUserRepository.save(USER);

        String expectedName = savedAnime.getName();

        PageableResponse<Anime> animePage = testRestTemplateUser.exchange("/animes", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Anime>>() {
                }).getBody();

        Assertions.assertThat(animePage).isNotNull();

        Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    void listAllNonPageable() {
        Anime savedAnime = animeRepository.save(AnimeSupport.createAnimeToBeSaved());
        devDojoUserRepository.save(USER);

        String expectedName = savedAnime.getName();

        List<Anime> animes = testRestTemplateUser.exchange("/animes/all", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    void findById() {
        Anime savedAnime = animeRepository.save(AnimeSupport.createAnimeToBeSaved());
        devDojoUserRepository.save(USER);

        Long expectedId = savedAnime.getId();

        Anime anime = testRestTemplateUser.getForObject("/animes/{id}", Anime.class, expectedId);

        org.junit.jupiter.api.Assertions.assertNotNull(anime, "Animes deveria retornar uma lista.");
        org.junit.jupiter.api.Assertions.assertEquals("Primeiro anime", anime.getName(), "Nome do anime difere do esperado");
    }

    @Test
    void findByName() {
        Anime savedAnime = animeRepository.save(AnimeSupport.createAnimeToBeSaved());
        devDojoUserRepository.save(USER);
        String expectedName = savedAnime.getName();
        String url = String.format("/animes/findByName?name=%s", expectedName);

        List<Anime> animes = testRestTemplateUser.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    void tryFindByName() {
        devDojoUserRepository.save(USER);
        String url = String.format("/animes/findByName?name=%s", "erro");

        List<Anime> animes = testRestTemplateUser.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isEmpty();

    }

    @Test
    void save() {
//        devDojoUserRepository.save(USER);
        devDojoUserRepository.save(ADMIN);
        AnimeDTO animeDTO = new AnimeDTO();
        animeDTO.setName("Primeiro anime");
        ResponseEntity<Anime> animeResponseEntity = testRestTemplateAdmin.postForEntity("/animes", animeDTO, Anime.class);

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(animeResponseEntity.getBody()).isNotNull();
        Assertions.assertThat(animeResponseEntity.getBody().getId()).isNotNull();
    }

    @Test
    void updateAnime() {
        Anime savedAnime = animeRepository.save(AnimeSupport.createAnimeToBeSaved());
        devDojoUserRepository.save(USER);
        savedAnime.setName("Segundo alterou");


        ResponseEntity<Void> animeResponseEntity = testRestTemplateUser.exchange(
                "/animes",
                HttpMethod.PUT,
                new HttpEntity<>(savedAnime),
                Void.class
        );

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void deleteAnime() {
        devDojoUserRepository.save(USER);
        Anime savedAnime = animeRepository.save(AnimeSupport.createAnimeToBeSaved());

        ResponseEntity<Void> animeResponseEntity = testRestTemplateUser.exchange(
                "/animes/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                savedAnime.getId()
        );

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

}
