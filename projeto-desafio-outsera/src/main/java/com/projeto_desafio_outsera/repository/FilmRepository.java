package com.projeto_desafio_outsera.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projeto_desafio_outsera.model.Film;

@Repository
public interface FilmRepository  extends JpaRepository<Film, Long>{

	List<Film> findByWinner(Boolean winner);
}
