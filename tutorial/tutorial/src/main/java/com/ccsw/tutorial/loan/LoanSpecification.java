package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.loan.model.Loan;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class LoanSpecification {

    public static Specification<Loan> gameId(Long gameId) {
        return (root, query, builder) -> {
            if (gameId == null)
                return builder.conjunction();
            return builder.equal(root.get("game").get("id"), gameId);
        };
    }

    public static Specification<Loan> clientId(Long clientId) {
        return (root, query, builder) -> {
            if (clientId == null)
                return builder.conjunction();
            return builder.equal(root.get("client").get("id"), clientId);
        };
    }

    public static Specification<Loan> date(LocalDate date) {
        return (root, query, builder) -> {
            if (date == null)
                return builder.conjunction();

            return builder.and(builder.lessThanOrEqualTo(root.get("startDate"), date), builder.greaterThanOrEqualTo(root.get("endDate"), date));
        };
    }
}