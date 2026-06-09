package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.loan.model.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface LoanRepository extends CrudRepository<Loan, Long>, JpaSpecificationExecutor<Loan> {
    Page<Loan> findAll(Pageable pageable);

    List<Loan> findByGameIdAndEndDateGreaterThanEqualAndStartDateLessThanEqual(Long gameId, LocalDate startDate, LocalDate endDate);

    List<Loan> findByClientIdAndEndDateGreaterThanEqualAndStartDateLessThanEqual(Long clientId, LocalDate startDate, LocalDate endDate);

}