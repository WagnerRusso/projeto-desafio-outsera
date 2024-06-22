package com.projeto_desafio_outsera.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projeto_desafio_outsera.dto.response.IntervalResponse;
import com.projeto_desafio_outsera.service.FilmService;

@RestController
@RequestMapping("/api/films")
public class FilmController {

	@Autowired
	private FilmService filmService;

	@GetMapping("/awards")
	public ResponseEntity<IntervalResponse> getAwardIntervals() {
		return ResponseEntity.ok(filmService.getProducersWithMaxAndMinInterval1());
	}

}
