package com.sheen.joe.bankingsystem.repository;

import com.sheen.joe.bankingsystem.entity.Transfer;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@JaversSpringDataAuditable
public interface TransferRepository extends JpaRepository<Transfer, UUID> {

    @Query("SELECT t FROM Transfer t WHERE t.id = :#{#id} AND t.senderAccount.user.id = :#{#userId}")
    Optional<Transfer> findByIdAndUserId(@Param("id") UUID id, @Param("userId") UUID userId);
}
