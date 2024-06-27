package com.projeto_desafio_outsera.dto;

import lombok.Data;

@Data
public class MovieDto {

	private int year;
	private String title;
	private String studios;
	private String producer;
	private Boolean winner;
	private Integer interval;
	
}
