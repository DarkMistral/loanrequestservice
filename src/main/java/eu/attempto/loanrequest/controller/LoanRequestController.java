package eu.attempto.loanrequest.controller;

import eu.attempto.loanrequest.exceptions.CustomerDataMismatchException;
import eu.attempto.loanrequest.exceptions.InvalidAmountException;
import eu.attempto.loanrequest.exceptions.NoEntryForCustomerException;
import eu.attempto.loanrequest.model.LoanRequest;
import eu.attempto.loanrequest.service.LoanRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@RequiredArgsConstructor
@RestController
public class LoanRequestController {

    private final ObjectMapper objectMapper;
    private final LoanRequestService service;

    @PostMapping(path = "/createLoanRequest")
    @ResponseBody
    public LoanRequest createLoanRequest(@RequestBody LoanRequestRO request) {
        log.info("Received loan request: {}", objectMapper.writeValueAsString(request));
        try {
            return service.createLoanRequest(request);
        } catch (CustomerDataMismatchException | InvalidAmountException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }
    }

    @GetMapping(path = "/loanrequestsByCustomer", params = "customerId")
    @ResponseBody
    public Double getLoanRequestsByCustomer(int customerId) {
        try {
            return service.getLoanRequestsForCustomerId(customerId);
        } catch (NoEntryForCustomerException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
