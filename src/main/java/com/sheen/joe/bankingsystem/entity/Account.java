package com.sheen.joe.bankingsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.javers.core.metamodel.annotation.ShallowReference;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;

    @NotBlank
    private String accountName;

    private String accountNumber;

    @ShallowReference
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private AccountCard accountCard;

    @Column(nullable = false, columnDefinition = "numeric(38,2)")
    private BigDecimal balance;

    private boolean closed;

    @ShallowReference
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ShallowReference
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transfer> transfers;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
