package eu.attempto.loanrequest.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Customer {
    int customerId;
    String customerName;
}
