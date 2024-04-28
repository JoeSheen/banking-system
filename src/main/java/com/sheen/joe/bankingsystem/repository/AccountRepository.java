package com.sheen.joe.bankingsystem.repository;

import com.sheen.joe.bankingsystem.entity.Account;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@JaversSpringDataAuditable
public interface AccountRepository extends JpaRepository<Account, UUID> {

    @Query("SELECT a FROM Account a WHERE a.user.id = :#{#userId} AND a.closed = :#{#closed}")
    Page<Account> findAllUserAccounts(@Param("closed") boolean closed, @Param("userId") UUID userId, Pageable pageable);

    Optional<Account> findByAccountNumberAndSortCode(String accountNumber, String sortCode);

    Optional<Account> findByIdAndUserId(UUID id, UUID userId);

}
