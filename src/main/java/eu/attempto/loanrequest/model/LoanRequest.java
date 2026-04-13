package eu.attempto.loanrequest.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoanRequest {
    Integer id;
    Customer customer;
    double amount;
}
