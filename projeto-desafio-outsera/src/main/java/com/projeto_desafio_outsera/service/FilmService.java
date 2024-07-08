package com.projeto_desafio_outsera.service;

import java.time.LocalDate;
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
import com.projeto_desafio_outsera.dto.MovieMapper;
import com.projeto_desafio_outsera.dto.response.IntervalResponse;
import com.projeto_desafio_outsera.model.Film;
import com.projeto_desafio_outsera.repository.FilmRepository;

@Service
public class FilmService {

	@Autowired
	private FilmRepository filmRepository;
	@Autowired
	private MovieMapper mapper;

	public IntervalResponse getProducersWithMaxAndMinInterval1() {
		return calculateProducersIntervals();
	}

	private IntervalResponse calculateProducersIntervals() {

		int year = LocalDate.now().getYear();

		List<Film> filmsWinners = filmRepository.findByWinner(true);

		List<MovieDto> filmsWinnersByProducer = new ArrayList<>();
		for (Film f : filmsWinners) {
			String[] listNameProducers = null;

			if (f.getYear() <= year) {
				if (f.getProducers().contains(",") || f.getProducers().matches(".*\\band\\b.*")) {
					listNameProducers = f.getProducers().split(", | and ");
					filmsWinnersByProducer.addAll(mapFilmeByProducer(f, listNameProducers));
				} else {
					filmsWinnersByProducer.addAll(mapFilmeByProducer(f, listNameProducers));
				}
			}
		}

		List<String> listProducers = filmsWinnersByProducer.stream()
				.collect(Collectors.groupingBy(p -> p.getProducer(), Collectors.counting())).entrySet().stream()
				.filter(entry -> entry.getValue() > 1).map(Map.Entry::getKey).collect(Collectors.toList());

		List<MovieDto> pwinners = filmsWinnersByProducer.stream().filter(p -> listProducers.contains(p.getProducer()))
				.collect(Collectors.toList());

		IntervalResponse intervalResponse = findMinMaxIntervals(pwinners);

		return intervalResponse;
	}

	private IntervalResponse findMinMaxIntervals(List<MovieDto> pwinners) {
		IntervalResponse intervalResult = new IntervalResponse();

		pwinners.sort(Comparator.comparing(MovieDto::getProducer).thenComparing(MovieDto::getYear));

		List<MovieDto> listMoviesWithInterval = new ArrayList<>();
		MovieDto producer = new MovieDto();
		int count = 0;

		for (MovieDto dto : pwinners) {
			if (producer.getProducer() != null && producer.getProducer().equals(dto.getProducer())) {
				MovieDto cProducer = mapper.movieDtoToMovieDto(producer);
				count++;
				teste(cProducer, dto, count);

				dto.setInterval(!pwinners.get(pwinners.size() - 1).equals(dto) ? cProducer.getInterval() : 0);

				listMoviesWithInterval.add(dto);
				listMoviesWithInterval.add(cProducer);
			}
			producer = dto;
		}

		listMoviesWithInterval.sort(Comparator.comparing(MovieDto::getProducer).thenComparing(MovieDto::getYear));

		Optional<Integer> maxInterval = listMoviesWithInterval.stream().map(m -> m.getInterval())
				.max(Integer::compareTo);

		List<MovieDto> movieMaxInterval = listMoviesWithInterval.stream()
				.filter(m -> m.getInterval() == maxInterval.orElse(Integer.MIN_VALUE)).collect(Collectors.toList());

		Optional<Integer> minInterval = listMoviesWithInterval.stream().filter(m -> m.getInterval() != 0)
				.map(MovieDto::getInterval).min(Integer::compareTo);

		List<MovieDto> movieMinInterval = listMoviesWithInterval.stream()
				.filter(m -> m.getInterval() == minInterval.orElse(null)).collect(Collectors.toList());

		intervalResult.setMax(mapMaxIntervalResult(movieMaxInterval));
		intervalResult.setMin(mapMinIntervalResult(movieMinInterval));

		return intervalResult;
	}

	private void teste(MovieDto producer, MovieDto dto, int count) {
		// @formatter:off
		Integer i = dto.getYear() > producer.getYear()
					? dto.getYear() - producer.getYear()
					: producer.getYear() - dto.getYear();
		// @formatter:on
		producer.setInterval(i);
		dto.setCompareMovieId(count);
		producer.setCompareMovieId(dto.getCompareMovieId());
	}

	private List<MovieDto> mapFilmeByProducer(Film f, String[] listNameProducers) {
		List<MovieDto> movieDtos = new ArrayList<>();

		if (listNameProducers == null) {
			movieDtos.add(mapper.filmNameProducerToMovieDTO(f, null));
			return movieDtos;
		}

		for (String producer : listNameProducers) {
			movieDtos.add(mapper.filmNameProducerToMovieDTO(f, producer));
		}

		return movieDtos;
	}

	private List<IntervalDTO> mapMaxIntervalResult(List<MovieDto> movieMaxInterval) {
		List<IntervalDTO> maxInterval = new ArrayList<>();

		for (MovieDto dto : movieMaxInterval) {
			List<MovieDto> list = movieMaxInterval.stream()
					.filter(m -> m.getCompareMovieId() == dto.getCompareMovieId()).collect(Collectors.toList());

			IntervalDTO intervalDTO = new IntervalDTO();
			intervalDTO.setProducer(dto.getProducer());
			intervalDTO.setInterval(dto.getInterval());

			Integer previousWin = list.stream().map(y -> y.getYear()).min(Integer::compareTo).orElseThrow();
			intervalDTO.setPreviousWin(previousWin);

			Integer followingWin = list.stream().map(y -> y.getYear()).max(Integer::compareTo).orElseThrow();
			intervalDTO.setFollowingWin(followingWin);

			if (!maxInterval.contains(intervalDTO)) {
				maxInterval.add(intervalDTO);
			}
		}

		return maxInterval;
	}

	private List<IntervalDTO> mapMinIntervalResult(List<MovieDto> movieMinInterval) {
		List<IntervalDTO> minInterval = new ArrayList<>();

		for (MovieDto dto : movieMinInterval) {
			List<MovieDto> list = movieMinInterval.stream()
					.filter(m -> m.getCompareMovieId() == dto.getCompareMovieId()).collect(Collectors.toList());

			IntervalDTO intervalDTO = new IntervalDTO();
			intervalDTO.setProducer(movieMinInterval.get(0).getProducer());
			intervalDTO.setInterval(movieMinInterval.get(0).getInterval());

			Integer previousWin = list.stream().map(y -> y.getYear()).min(Integer::compareTo).orElseThrow();
			intervalDTO.setPreviousWin(previousWin);

			Integer followingWin = list.stream().map(y -> y.getYear()).max(Integer::compareTo).orElseThrow();
			intervalDTO.setFollowingWin(followingWin);

			if (!minInterval.contains(intervalDTO)) {
				minInterval.add(intervalDTO);
			}

		}
		return minInterval;
	}
}
