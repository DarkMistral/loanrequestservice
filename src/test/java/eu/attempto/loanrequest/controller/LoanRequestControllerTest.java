package eu.attempto.loanrequest.controller;

import eu.attempto.loanrequest.exceptions.CustomerDataMismatchException;
import eu.attempto.loanrequest.exceptions.InvalidAmountException;
import eu.attempto.loanrequest.exceptions.NoEntryForCustomerException;
import eu.attempto.loanrequest.model.LoanRequest;
import eu.attempto.loanrequest.service.LoanRequestService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class LoanRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    LoanRequestService serviceMock;

    @Test
    void createLoanRequest_No_Exception_Should_Succeed() throws Exception {
        var request = LoanRequestRO.builder().build();
        var response = LoanRequest.builder().id(1).build();
        when(serviceMock.createLoanRequest(eq(request))).thenReturn(response);
        mockMvc.perform(post("/createLoanRequest")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("createSpecificIllegalArgumentException")
    void createLoanRequest_Exception_Should_Throw_ResponseStatusException(Class<Throwable> exception) throws Exception {
        var request = LoanRequestRO.builder().build();
        when(serviceMock.createLoanRequest(eq(request))).thenThrow(exception);
        mockMvc.perform(post("/createLoanRequest")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    private static List<Arguments> createSpecificIllegalArgumentException() {
        return List.of(Arguments.of(CustomerDataMismatchException.class), Arguments.of(InvalidAmountException.class));
    }

    @Test
    void getLoanRequestsByCustomer_Valid_CustomerId_Should_Succeed() throws Exception {
        Double expectedResult = 20000.50;
        when(serviceMock.getLoanRequestsForCustomerId(anyInt())).thenReturn(expectedResult);
        var response = mockMvc.perform(get("/loanrequestsByCustomer")
            .param("customerId", "1"))
            .andExpect(status().isOk())
            .andReturn();
        assertThat(response.getResponse().getContentAsString()).isEqualTo(expectedResult.toString());
    }

    @Test
    void getLoanRequestsByCustomer_No_Requests_For_Customer_Should_Throw_ResponseStatusException() throws Exception {
        when(serviceMock.getLoanRequestsForCustomerId(anyInt())).thenThrow(NoEntryForCustomerException.class);
        mockMvc.perform(get("/loanrequestsByCustomer")
                .param("customerId", "1"))
            .andExpect(status().isBadRequest());
    }
}