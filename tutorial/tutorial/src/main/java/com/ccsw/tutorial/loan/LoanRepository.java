package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.loan.model.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LoanRepository extends CrudRepository<Loan, Long>, JpaSpecificationExecutor<Loan> {
    Page<Loan> findAll(Pageable pageable);

    @Query("SELECT l FROM Loan l WHERE l.game.id = :gameId AND l.endDate >= :startDate AND l.startDate <= :endDate")
    List<Loan> findOverlappingGameLoans(@Param("gameId") Long gameId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT l FROM Loan l WHERE l.client.id = :clientId AND l.endDate >= :startDate AND l.startDate <= :endDate")
    List<Loan> findOverlappingClientLoans(@Param("clientId") Long clientId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}