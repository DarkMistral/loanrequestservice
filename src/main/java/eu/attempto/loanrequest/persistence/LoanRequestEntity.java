package eu.attempto.loanrequest.persistence;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity(name = "LoanRequest")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoanRequestEntity implements Serializable {
    @Id
    @GeneratedValue
    private int id;

    @Column
    private double amount;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(referencedColumnName = "customerId")
    private CustomerEntity customer;
}
