package com.mateusmsc.essential.util;

import com.mateusmsc.essential.domain.Anime;

public class AnimeSupport {

    public static Anime createAnimeToBeSaved() {
        return Anime
                .builder()
                .name("Primeiro anime")
                .build();

    }

    public static Anime createAnimeValid() {
        return Anime
                .builder()
                .id(1L)
                .name("Primeiro anime")
                .build();

    }

    public static Anime createAnimeToUpdate() {
        return Anime
                .builder()
                .name("Segundo anime")
                .id(1L)
                .build();

    }

}
