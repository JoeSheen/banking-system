package com.sheen.joe.bankingsystem.entity;

import com.sheen.joe.bankingsystem.annotation.MinAge;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.javers.core.metamodel.annotation.ShallowReference;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "banking_user")
public class User {

    @Id
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Past
    @NotNull
    @MinAge(message = "Bank users must be 16 or over")
    @Column(updatable = false)
    private LocalDate dateOfBirth;

    @NotNull
    @Column(unique = true)
    private String phoneNumber;

    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    @NotBlank
    private String username;

    @NotBlank
    @DiffIgnore
    private String password;

    @DiffIgnore
    private Set<UserRole> authorities;

    @ShallowReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Account> accounts;

}
