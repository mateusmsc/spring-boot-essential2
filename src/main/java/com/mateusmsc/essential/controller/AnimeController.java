package com.mateusmsc.essential.controller;

import com.mateusmsc.essential.domain.Anime;
import com.mateusmsc.essential.dto.AnimeDTO;
import com.mateusmsc.essential.service.AnimeService;
import com.mateusmsc.essential.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("animes")
@Log4j2
@RequiredArgsConstructor
public class AnimeController {
    //Responsável por controlar os endpoints
    private final DateUtil dateUtil;
    //usar o final para entrar no "RequiredArgsConstructor"
    private final AnimeService animeService;

    @GetMapping
    public ResponseEntity<List<Anime>> list() {
        log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return ResponseEntity.ok(animeService.listAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Anime> findById(@PathVariable long id) {
        log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return ResponseEntity.ok(animeService.findByIdOrThrowBadRequestException(id));
    }

    // Exemplo da URL
    // /animes/findByName?name=Dragon Ball GT
    @GetMapping(path = "/findByName")
    public ResponseEntity<List<Anime>> findById(@RequestParam String name) {
        log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return ResponseEntity.ok(animeService.findByName(name));
    }

    //Se o Json estiver com os atributos iguais, ele faz o mapeamento automaticamente
    // - id
    // - name
    // Com o @Valid ele valida todas as anotation que são colocadas no AnimeDTO
    // como por exemplo o NotNull
    @PostMapping
    public ResponseEntity<Anime> save(@RequestBody @Valid AnimeDTO anime) {
        return new ResponseEntity<>(animeService.save(anime), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> save(@PathVariable long id) {
        animeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping()
    public ResponseEntity<Void> replace(@RequestBody AnimeDTO anime) {
        animeService.replace(anime);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}