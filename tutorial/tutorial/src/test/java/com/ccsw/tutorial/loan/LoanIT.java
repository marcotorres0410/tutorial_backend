package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.game.model.GameDto;
import com.ccsw.tutorial.loan.model.LoanDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LoanIT {

    public static final String LOCALHOST = "http://localhost:";
    public static final String SERVICE_PATH = "/loan";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    // --- TEST 1: COMPROBAR LOS 14 DÍAS ---
    @Test
    public void saveWithMoreThan14DaysShouldReturnError() {
        LoanDto dto = new LoanDto();

        GameDto gameDto = new GameDto();
        gameDto.setId(1L);
        dto.setGame(gameDto);

        ClientDto clientDto = new ClientDto();
        clientDto.setId(1L);
        dto.setClient(clientDto);

        // Le ponemos 20 días de diferencia (del 1 al 21 de enero)
        dto.setStartDate(LocalDate.of(2026, 1, 1));
        dto.setEndDate(LocalDate.of(2026, 1, 21));

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        // Esperamos que el servidor devuelva un error (500 Internal Server Error)
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // --- TEST 2: COMPROBAR FECHAS INVERTIDAS ---
    @Test
    public void saveWithEndDateBeforeStartDateShouldReturnError() {
        LoanDto dto = new LoanDto();

        GameDto gameDto = new GameDto();
        gameDto.setId(1L);
        dto.setGame(gameDto);

        ClientDto clientDto = new ClientDto();
        clientDto.setId(1L);
        dto.setClient(clientDto);

        // Fecha de fin anterior a fecha de inicio
        dto.setStartDate(LocalDate.of(2026, 1, 10));
        dto.setEndDate(LocalDate.of(2026, 1, 5));

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}