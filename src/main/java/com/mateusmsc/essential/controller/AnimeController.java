package com.mateusmsc.essential.controller;

import com.mateusmsc.essential.domain.Anime;
import com.mateusmsc.essential.dto.AnimeDTO;
import com.mateusmsc.essential.service.AnimeService;
import com.mateusmsc.essential.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public ResponseEntity<Page<Anime>> list(Pageable pegeable) {
//        log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return ResponseEntity.ok(animeService.listAll(pegeable));
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<Anime>> listAll() {
//        log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return ResponseEntity.ok(animeService.listAllNonPageable());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Anime> findById(@PathVariable long id) {
//        log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return ResponseEntity.ok(animeService.findByIdOrThrowBadRequestException(id));
    }

    // Autenticado -> permite entrar no sistema
    // Autorizado -> permite que vc acesse certos pontos do sistema
    // Segunda anotation serve para que possamos pegar as informações de usuário logado
    @GetMapping(path = "/auth/{id}")
    public ResponseEntity<Anime> findByIdAuthMainUser(@PathVariable long id, @AuthenticationPrincipal UserDetails userDetails) {
//        log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        log.info(userDetails);
        return ResponseEntity.ok(animeService.findByIdOrThrowBadRequestException(id));
    }

    // Exemplo da URL
    // /animes/findByName?name=Dragon Ball GT
    @GetMapping(path = "/findByName")
    public ResponseEntity<List<Anime>> findByName(@RequestParam String name) {
//        log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return ResponseEntity.ok(animeService.findByName(name));
    }

    //Se o Json estiver com os atributos iguais, ele faz o mapeamento automaticamente
    // - id
    // - name
    // Com o @Valid ele valida todas as anotation que são colocadas no AnimeDTO
    // como por exemplo o NotNull
    @PostMapping
    //Garante as roles do usuário para poder autenticar um endpoint
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Anime> save(@RequestBody @Valid AnimeDTO anime) {
        return new ResponseEntity<>(animeService.save(anime), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        animeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping()
    public ResponseEntity<Void> replace(@RequestBody AnimeDTO anime) {
        animeService.replace(anime);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}