package eu.attempto.loanrequest.util;

import eu.attempto.loanrequest.controller.LoanRequestRO;
import eu.attempto.loanrequest.model.Customer;
import eu.attempto.loanrequest.model.LoanRequest;
import eu.attempto.loanrequest.persistence.CustomerEntity;
import eu.attempto.loanrequest.persistence.LoanRequestEntity;

public class MappingHelper {

    public static LoanRequestEntity entityFromLoanRequestRO(LoanRequestRO loanRequestRO) {
        return LoanRequestEntity.builder()
                .customer(CustomerEntity.builder()
                        .customerId(loanRequestRO.getCustomerId())
                        .customerName(loanRequestRO.getCustomerName())
                        .build())
                .amount(loanRequestRO.getAmount())
                .build();
    }

    public static LoanRequest loanRequestFromEntity(LoanRequestEntity entity) {
        return LoanRequest.builder()
                .id(entity.getId())
                .customer(customerFromEntity(entity.getCustomer()))
                .amount(entity.getAmount())
                .build();
    }

    public static Customer customerFromEntity(CustomerEntity entity) {
        return Customer.builder()
                .customerId(entity.getCustomerId())
                .customerName(entity.getCustomerName())
                .build();
    }

    public static CustomerEntity entityFromCustomer(Customer customer) {
        return CustomerEntity.builder()
                .customerId(customer.getCustomerId())
                .customerName(customer.getCustomerName())
                .build();
    }
}
