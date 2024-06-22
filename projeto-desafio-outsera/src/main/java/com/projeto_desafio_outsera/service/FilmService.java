package com.projeto_desafio_outsera.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projeto_desafio_outsera.dto.IntervalDTO;
import com.projeto_desafio_outsera.dto.response.IntervalResponse;
import com.projeto_desafio_outsera.model.Film;
import com.projeto_desafio_outsera.repository.FilmRepository;

@Service
public class FilmService {

	@Autowired
	private FilmRepository filmRepository;

	public IntervalResponse getProducersWithMaxAndMinInterval1() {
		List<IntervalDTO> intervals = calculateProducersIntervals();

		// Encontrar o mínimo e máximo dos intervalos
		List<IntervalDTO> minIntervals = findMinIntervals(intervals);
		List<IntervalDTO> maxIntervals = findMaxIntervals(intervals);

		IntervalResponse response = new IntervalResponse();
		response.setMin(minIntervals);
		response.setMax(maxIntervals);

		return response;
	}

	private List<IntervalDTO> calculateProducersIntervals() {
		List<Film> filmsWinners = filmRepository.findByWinner(true);

		// Mapear filmes por produtor
		Map<String, List<Film>> moviesByProducer = filmsWinners.stream()
				.collect(Collectors.groupingBy(Film::getProducers));

		// Calcular intervalos para cada produtor
		List<IntervalDTO> intervals = new ArrayList<>();

		moviesByProducer.forEach((producer, producerFilms) -> {
			List<Film> sortedMovies = producerFilms.stream().sorted(Comparator.comparingInt(Film::getYear))
					.collect(Collectors.toList());

			for (int i = 1; i < sortedMovies.size(); i++) {
				int interval = sortedMovies.get(i).getYear() - sortedMovies.get(i - 1).getYear();
				IntervalDTO intervalDTO = new IntervalDTO(producer, interval, sortedMovies.get(i - 1).getYear(),
						sortedMovies.get(i).getTitle() // Exemplo: usar título do filme seguinte
				);
				intervals.add(intervalDTO);
			}
		});
		return intervals;
	}

	private List<IntervalDTO> findMinIntervals(List<IntervalDTO> intervals) {
		int minInterval = intervals.stream().filter(interval -> interval.getInterval() > 0)
				.mapToInt(IntervalDTO::getInterval).min().orElse(Integer.MAX_VALUE);

		return intervals.stream().filter(interval -> interval.getInterval() == minInterval)
				.collect(Collectors.toList());
	}

	private List<IntervalDTO> findMaxIntervals(List<IntervalDTO> intervals) {
		int maxInterval = intervals.stream().filter(interval -> interval.getInterval() < Integer.MAX_VALUE)
				.mapToInt(IntervalDTO::getInterval).max().orElse(0);

		return intervals.stream().filter(interval -> interval.getInterval() == maxInterval)
				.collect(Collectors.toList());
	}

}
