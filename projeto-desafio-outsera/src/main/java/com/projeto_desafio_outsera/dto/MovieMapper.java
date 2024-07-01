package com.projeto_desafio_outsera.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.projeto_desafio_outsera.model.Film;

@Mapper(componentModel = "spring")
public interface MovieMapper {

	@Mapping(target = "producer", expression = "java(nameProducer != null ? nameProducer : film.getProducers())")
	@Mapping(source = "film.studios", target = "studios")
	@Mapping(source = "film.title", target = "title")
	@Mapping(source = "film.year", target = "year")
	@Mapping(source = "film.winner", target = "winner")
	MovieDto filmNameProducerToMovieDTO(Film film, String nameProducer);
	
}