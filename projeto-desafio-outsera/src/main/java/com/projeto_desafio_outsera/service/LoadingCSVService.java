package com.projeto_desafio_outsera.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projeto_desafio_outsera.model.Film;
import com.projeto_desafio_outsera.repository.FilmRepository;

import jakarta.annotation.PostConstruct;

@Service
public class LoadingCSVService {

	@Autowired
	private FilmRepository filmRepository;

	@PostConstruct
	public void loadCSV() {
		try {
			InputStream inputStream = Files
					.newInputStream(Paths.get(ClassLoader.getSystemResource("data/movielist.csv").toURI()));
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

			String line = reader.readLine();
			while ((line = reader.readLine()) != null) {
				String[] data = line.split(";");

				Film film = new Film();

				film.setYear(Integer.parseInt(data[0]));
				film.setTitle(data[1]);
				film.setStudios(data[2]);
				film.setProducers(data[3]);
				film.setWinner(data.length > 4 && data[4].equalsIgnoreCase("yes"));
				filmRepository.save(film);
			}

			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
