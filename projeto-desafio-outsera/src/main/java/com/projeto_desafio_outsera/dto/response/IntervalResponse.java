package com.projeto_desafio_outsera.dto.response;

import java.util.List;

import com.projeto_desafio_outsera.dto.IntervalDTO;

public class IntervalResponse {

	private List<IntervalDTO> min;
	private List<IntervalDTO> max;

	public List<IntervalDTO> getMin() {
		return min;
	}

	public void setMin(List<IntervalDTO> min) {
		this.min = min;
	}

	public List<IntervalDTO> getMax() {
		return max;
	}

	public void setMax(List<IntervalDTO> max) {
		this.max = max;
	}

}
