package com.projeto_desafio_outsera.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projeto_desafio_outsera.dto.IntervalDTO;
import com.projeto_desafio_outsera.dto.MovieDto;
import com.projeto_desafio_outsera.dto.response.IntervalResponse;
import com.projeto_desafio_outsera.model.Film;
import com.projeto_desafio_outsera.repository.FilmRepository;

@Service
public class FilmService {

	@Autowired
	private FilmRepository filmRepository;

	public IntervalResponse getProducersWithMaxAndMinInterval1() {
		return calculateProducersIntervals();
	}

	private IntervalResponse calculateProducersIntervals() {
		List<Film> filmsWinners = filmRepository.findByWinner(true);

		List<MovieDto> filmsWinnersByProducer = new ArrayList<>();
		for (Film f : filmsWinners) {
			String[] listNameProducers = null;

			if (f.getProducers().contains(",") || f.getProducers().matches(".*\\band\\b.*")) {
				listNameProducers = f.getProducers().split(", | and ");
				filmsWinnersByProducer.addAll(mapFilmeByProducer(f, listNameProducers));
			} else {
				filmsWinnersByProducer.addAll(mapFilmeByProducer(f, listNameProducers));
			}
		}

		// Pesquisando nomes de produtores que aparecem mais de uma vez
		List<String> listProducers = filmsWinnersByProducer.stream()
				.collect(Collectors.groupingBy(p -> p.getProducer(), Collectors.counting())).entrySet().stream()
				.filter(entry -> entry.getValue() > 1).map(Map.Entry::getKey).collect(Collectors.toList());

		// Pegar os objetos correspondente ao producer
		List<MovieDto> pwinners = filmsWinnersByProducer.stream().filter(p -> listProducers.contains(p.getProducer()))
				.collect(Collectors.toList());

		IntervalResponse intervalResponse = findMinMaxIntervals(pwinners);

		return intervalResponse;
	}

	private IntervalResponse findMinMaxIntervals(List<MovieDto> pwinners) {
		IntervalResponse intervalResult = new IntervalResponse();

		// Ordenar os filmes vencedores por ano
		pwinners.sort(Comparator.comparing(MovieDto::getProducer));
		List<MovieDto> listMoviesWithInterval = new ArrayList<>();
		MovieDto producer = new MovieDto();

		for (MovieDto dto : pwinners) {
			if (producer.getProducer() != null && producer.getProducer().equals(dto.getProducer())) {
				Integer i = dto.getYear() - producer.getYear();
				dto.setInterval(i);
				producer.setInterval(i);
				listMoviesWithInterval.add(dto);
				listMoviesWithInterval.add(producer);
			}

			producer = dto;
		}

		Optional<Integer> maxInterval = listMoviesWithInterval.stream().map(m -> m.getInterval())
				.max(Integer::compareTo);

		List<MovieDto> movieMaxInterval = listMoviesWithInterval.stream()
				.filter(m -> m.getInterval() == maxInterval.orElse(Integer.MIN_VALUE)).collect(Collectors.toList());

		Optional<Integer> minInterval = listMoviesWithInterval.stream().map(m -> m.getInterval())
				.min(Integer::compareTo);

		List<MovieDto> movieMinInterval = listMoviesWithInterval.stream()
				.filter(m -> m.getInterval() == minInterval.orElse(null)).collect(Collectors.toList());

		intervalResult.setMax(mapMaxIntervalResult(movieMaxInterval));
		intervalResult.setMin(mapMinIntervalResult(movieMinInterval));

		return intervalResult;
	}

	private List<MovieDto> mapFilmeByProducer(Film f, String[] listNameProducers) {
		List<MovieDto> movieDtos = new ArrayList<>();

		if (listNameProducers == null) {
			MovieDto movie = new MovieDto();
			movie.setProducer(f.getProducers());
			movie.setStudios(f.getStudios());
			movie.setTitle(f.getTitle());
			movie.setYear(f.getYear());
			movie.setWinner(f.getWinner());

			movieDtos.add(movie);

			return movieDtos;
		}

		for (String producer : listNameProducers) {
			MovieDto movie = new MovieDto();
			movie.setProducer(producer);
			movie.setStudios(f.getStudios());
			movie.setTitle(f.getTitle());
			movie.setYear(f.getYear());
			movie.setWinner(f.getWinner());

			movieDtos.add(movie);
		}

		return movieDtos;
	}

	private List<IntervalDTO> mapMaxIntervalResult(List<MovieDto> movieMaxInterval) {
		IntervalDTO intervalDTO = new IntervalDTO(null, 0, 0, 0);
		List<IntervalDTO> maxInterval = new ArrayList<>();

		intervalDTO.setProducer(movieMaxInterval.get(0).getProducer());
		intervalDTO.setInterval(movieMaxInterval.get(0).getInterval());

		Integer previousWin = movieMaxInterval.stream().map(y -> y.getYear()).min(Integer::compareTo).orElseThrow();
		intervalDTO.setPreviousWin(previousWin);

		Integer followingWin = movieMaxInterval.stream().map(y -> y.getYear()).max(Integer::compareTo).orElseThrow();
		intervalDTO.setFollowingWin(followingWin);

		maxInterval.add(intervalDTO);

		return maxInterval;
	}

	private List<IntervalDTO> mapMinIntervalResult(List<MovieDto> movieMinInterval) {
		IntervalDTO intervalDTO = new IntervalDTO(null, 0, 0, 0);
		List<IntervalDTO> minInterval = new ArrayList<>();

		intervalDTO.setProducer(movieMinInterval.get(0).getProducer());
		intervalDTO.setInterval(movieMinInterval.get(0).getInterval());

		Integer previousWin = movieMinInterval.stream().map(y -> y.getYear()).min(Integer::compareTo).orElseThrow();
		intervalDTO.setPreviousWin(previousWin);

		Integer followingWin = movieMinInterval.stream().map(y -> y.getYear()).max(Integer::compareTo).orElseThrow();
		intervalDTO.setFollowingWin(followingWin);

		minInterval.add(intervalDTO);

		return minInterval;
	}
}
