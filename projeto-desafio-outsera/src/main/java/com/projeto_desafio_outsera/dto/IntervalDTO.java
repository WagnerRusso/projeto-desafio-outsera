package com.projeto_desafio_outsera.dto;

import lombok.Data;

@Data
public class IntervalDTO {

	private String producer;
	private int interval;
	private int previousWin;
	private int followingWin;

}
