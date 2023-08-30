package com.mateusmsc.essential.mapper;

import com.mateusmsc.essential.domain.Anime;
import com.mateusmsc.essential.dto.AnimeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AnimeMapper {
    AnimeMapper INSTANCE = Mappers.getMapper(AnimeMapper.class);

    Anime toAnime(AnimeDTO animeDTO);

}
