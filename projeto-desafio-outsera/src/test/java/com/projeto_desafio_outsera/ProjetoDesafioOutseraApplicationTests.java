package com.projeto_desafio_outsera;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProjetoDesafioOutseraApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void testGetAwardIntervals() {
		ResponseEntity<String> response = restTemplate.getForEntity("/api/films/awards", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	public void testGetAwardIntervals_ContentCheck() {
		ResponseEntity<String> response = restTemplate.getForEntity("/api/films/awards", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).contains("producer");
		assertThat(response.getBody()).contains("previousWin");
	}
	
	@Test
	public void testGetAwardIntervals_ErrorHandling() {
	    ResponseEntity<String> response = restTemplate.getForEntity("/api/films/invalidendpoint", String.class);
	    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

}
