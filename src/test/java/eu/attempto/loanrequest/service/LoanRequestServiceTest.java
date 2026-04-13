package eu.attempto.loanrequest.service;

import eu.attempto.loanrequest.controller.LoanRequestRO;
import eu.attempto.loanrequest.model.Customer;
import eu.attempto.loanrequest.persistence.CustomerRepository;
import eu.attempto.loanrequest.persistence.LoanRequestEntity;
import eu.attempto.loanrequest.persistence.LoanRequestRepository;
import eu.attempto.loanrequest.util.MappingHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static eu.attempto.loanrequest.util.MappingHelper.entityFromCustomer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanRequestServiceTest {

    @InjectMocks
    LoanRequestService underTest;

    @Mock
    LoanRequestRepository loanRequestRepositoryMock;
    @Mock
    CustomerRepository customerRepositoryMock;


    @ParameterizedTest
    @MethodSource("createValidAmountData")
    void valid_Amount_Input_Should_Return_True(double amount) {
        var request = LoanRequestRO.builder().amount(amount).customerId(1).customerName("Tessa Testing").build();
        var requestEntity = MappingHelper.entityFromLoanRequestRO(request);
        when(loanRequestRepositoryMock.save(any())).thenReturn(requestEntity);
        assertDoesNotThrow(() -> underTest.createLoanRequest(request));
    }

    @ParameterizedTest
    @MethodSource("createInvalidAmountData")
    void invalid_Amount_Input_Should_Throw_Exception(double amount) {
        var request = LoanRequestRO.builder().amount(amount).customerId(1).customerName("Tessa Test").build();
        assertThrows(IllegalArgumentException.class, () -> underTest.createLoanRequest(request));
    }

    @Test
    void get_Amount_Of_Loans_For_Customer_valid_Id_Should_Succeed() {
        List<LoanRequestEntity> entityList = createMockEntityList();
        when(loanRequestRepositoryMock.findAllByCustomerCustomerId(anyInt())).thenReturn(entityList);
        var result = assertDoesNotThrow(() -> underTest.getLoanRequestsForCustomerId(1));
        assertThat(result).isEqualTo(15000.50);
    }

    @Test
    void get_Amount_Of_Loans_For_Customer_Invalid_Id_Should_Throw_Exception() {
        when(loanRequestRepositoryMock.findAllByCustomerCustomerId(anyInt())).thenReturn(Collections.emptyList());
        assertThrows(NoSuchElementException.class, () -> underTest.getLoanRequestsForCustomerId(1));
    }

    private static List<Arguments> createValidAmountData() {
        return List.of(Arguments.of(500),
                Arguments.of(1000),
                Arguments.of(12000),
                Arguments.of(12000.50));
    }

    private static List<Arguments> createInvalidAmountData() {
        return List.of(Arguments.of(499.99),
                Arguments.of(-500),
                Arguments.of(12500.51));
    }

    private Customer createMockCustomer() {
        return Customer.builder().customerId(1).customerName("Tessa Testing").build();
    }

    private List<LoanRequestEntity> createMockEntityList() {
        return List.of(
                LoanRequestEntity.builder().id(1).amount(1500).customer(entityFromCustomer(createMockCustomer())).build(),
                LoanRequestEntity.builder().id(2).amount(1500).customer(entityFromCustomer(createMockCustomer())).build(),
                LoanRequestEntity.builder().id(3).amount(12000.50).customer(entityFromCustomer(createMockCustomer())).build()
        );
    }
}