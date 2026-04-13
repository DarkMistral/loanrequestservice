package eu.attempto.loanrequest.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRequestRepository extends JpaRepository<LoanRequestEntity, Integer> {

    List<LoanRequestEntity> findAllByCustomerCustomerId(int customerId);
}
