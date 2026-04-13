package eu.attempto.loanrequest.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanRequestRO {
    int customerId;
    String customerName;
    double amount;

}
