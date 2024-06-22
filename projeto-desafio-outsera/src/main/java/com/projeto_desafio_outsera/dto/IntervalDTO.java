package com.projeto_desafio_outsera.dto;

public class IntervalDTO {
	
	private String producer;
	private int interval;
	private int previousWin;
	private String followingWin;

	public IntervalDTO(String producer, int interval, int previousWin, String followingWin) {
		this.producer = producer;
		this.interval = interval;
		this.previousWin = previousWin;
		this.followingWin = followingWin;
	}

	public String getProducer() {
		return producer;
	}

	public void setProducer(String producer) {
		this.producer = producer;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public int getPreviousWin() {
		return previousWin;
	}

	public void setPreviousWin(int previousWin) {
		this.previousWin = previousWin;
	}

	public String getFollowingWin() {
		return followingWin;
	}

	public void setFollowingWin(String followingWin) {
		this.followingWin = followingWin;
	}

}
