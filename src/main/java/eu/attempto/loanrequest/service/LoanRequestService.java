package eu.attempto.loanrequest.service;

import eu.attempto.loanrequest.controller.LoanRequestRO;
import eu.attempto.loanrequest.exceptions.CustomerDataMismatchException;
import eu.attempto.loanrequest.exceptions.InvalidAmountException;
import eu.attempto.loanrequest.model.LoanRequest;
import eu.attempto.loanrequest.persistence.CustomerEntity;
import eu.attempto.loanrequest.persistence.CustomerRepository;
import eu.attempto.loanrequest.persistence.LoanRequestEntity;
import eu.attempto.loanrequest.persistence.LoanRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

import static eu.attempto.loanrequest.util.MappingHelper.entityFromLoanRequestRO;
import static eu.attempto.loanrequest.util.MappingHelper.loanRequestFromEntity;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanRequestService {

    private final LoanRequestRepository loanRequestRepository;
    private final CustomerRepository customerRepository;

    public LoanRequest createLoanRequest(LoanRequestRO request) {
        if (!isLoanRequestAmountValid(request.getAmount())) {
            log.error("Amount must be between 500 and 12000.50, but was {}", request.getAmount());
            throw new InvalidAmountException();
        }
        LoanRequestEntity entity = entityFromLoanRequestRO(request);
        handleCustomerData(entity);
        var savedRequest = loanRequestRepository.save(entity);
        return loanRequestFromEntity(savedRequest);
    }

    private void handleCustomerData(LoanRequestEntity request) {
        CustomerEntity requestCustomer = request.getCustomer();
        Optional<CustomerEntity> entry = customerRepository.findById(requestCustomer.getCustomerId());
        if (entry.isEmpty()) {
            customerRepository.save(requestCustomer);
        } else {
            var entity = entry.get();
            if (!requestCustomer.getCustomerName().equals(entity.getCustomerName())) {
                log.error("Mismatch of given customer id and customer name");
                throw new CustomerDataMismatchException("Id and name of customer do not match.");
            }
        }
    }

    public double getLoanRequestsForCustomerId(int customerId) {
        var requestEntities = loanRequestRepository.findAllByCustomerCustomerId(customerId);
        if (requestEntities.isEmpty()) {
            log.error("No entry for given customer id was found");
            throw new NoSuchElementException("No entry found for this customer id.");
        }
        return requestEntities.stream().mapToDouble(LoanRequestEntity::getAmount).sum();
    }

    private boolean isLoanRequestAmountValid(double amount) {
        return (amount >= 500 && amount <= 12000.50);
    }
}
