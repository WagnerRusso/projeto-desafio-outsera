package com.projeto_desafio_outsera.dto.response;

import java.util.List;

import com.projeto_desafio_outsera.dto.IntervalDTO;

import lombok.Data;


@Data
public class IntervalResponse {

	private List<IntervalDTO> min;
	private List<IntervalDTO> max;

}
