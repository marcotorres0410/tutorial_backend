package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.client.ClientService;
import com.ccsw.tutorial.game.GameService;
import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class LoanServiceImpl implements LoanService {

    @Autowired
    LoanRepository loanRepository;

    @Override
    public Page<Loan> findPage(LoanSearchDto dto) {
        Specification<Loan> spec = LoanSpecification.gameId(dto.getGameId()).and(LoanSpecification.clientId(dto.getClientId())).and(LoanSpecification.date(dto.getDate()));

        return this.loanRepository.findAll(spec, dto.getPageable().getPageable());
    }

    @Autowired
    GameService gameService;

    @Autowired
    ClientService clientService;

    @Override
    public void save(Long id, LoanDto dto) throws Exception {

        // REGLA 1: La fecha de fin NO puede ser anterior a la de inicio
        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new Exception("La fecha de fin no puede ser anterior a la fecha de inicio.");
        }

        // REGLA 2: El periodo de préstamo máximo es de 14 días
        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(dto.getStartDate(), dto.getEndDate());
        if (daysBetween > 14) {
            throw new Exception("El periodo de préstamo máximo solo puede ser de 14 días.");
        }

        // REGLA 3: El mismo juego no puede estar prestado en esas fechas
        List<Loan> gameLoans = this.loanRepository.findOverlappingGameLoans(dto.getGame().getId(), dto.getStartDate(), dto.getEndDate());
        for (Loan loan : gameLoans) {
            // Si estamos editando, ignoramos el préstamo actual para que no "choque" consigo mismo
            if (id == null || !loan.getId().equals(id)) {
                throw new Exception("El juego ya está prestado a otro cliente en esas fechas.");
            }
        }

        // REGLA 4: Un cliente no puede tener más de 2 juegos prestados en esas fechas
        List<Loan> clientLoans = this.loanRepository.findOverlappingClientLoans(dto.getClient().getId(), dto.getStartDate(), dto.getEndDate());
        int count = 0;
        for (Loan loan : clientLoans) {
            if (id == null || !loan.getId().equals(id)) {
                count++;
            }
        }
        if (count >= 2) {
            throw new Exception("El cliente ya tiene 2 juegos prestados en esas fechas.");
        }

        Loan loan;
        if (id == null) {
            loan = new Loan();
        } else {
            loan = this.loanRepository.findById(id).orElse(null);
        }

        loan.setGame(gameService.get(dto.getGame().getId()));
        loan.setClient(clientService.get(dto.getClient().getId()));
        loan.setStartDate(dto.getStartDate());
        loan.setEndDate(dto.getEndDate());

        this.loanRepository.save(loan);
    }

    @Override
    public void delete(Long id) throws Exception {
        this.loanRepository.deleteById(id);
    }
}