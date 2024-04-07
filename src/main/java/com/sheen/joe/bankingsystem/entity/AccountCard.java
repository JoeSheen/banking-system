package com.sheen.joe.bankingsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.javers.core.metamodel.annotation.ShallowReference;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountCard {

    @Id
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;

    private String cardNumber;

    private String cvc;

    private boolean isActive;

    private LocalDate dateIssued;

    private LocalDate expirationDate;

    private String cardholderName;

    @ShallowReference
    @OneToOne(mappedBy = "accountCard")
    @JoinColumn(name = "account_id")
    private Account account;
}
