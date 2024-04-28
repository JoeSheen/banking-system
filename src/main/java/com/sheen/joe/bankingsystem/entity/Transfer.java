package com.sheen.joe.bankingsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.javers.core.metamodel.annotation.ShallowReference;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transfer {

    @Id
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;

    @Enumerated(EnumType.ORDINAL)
    private TransferType transferType;

    @NotNull
    @Column(nullable = false, updatable = false, columnDefinition = "numeric(12,2)")
    private BigDecimal amount;

    private String reference;

    @Enumerated(EnumType.ORDINAL)
    private TransferCategory category;

    @NotNull
    @ManyToOne
    @ShallowReference
    private Account senderAccount;

    @ManyToOne
    @ShallowReference
    private Account receiverAccount;

    @CreationTimestamp
    private LocalDateTime timestamp;

}
