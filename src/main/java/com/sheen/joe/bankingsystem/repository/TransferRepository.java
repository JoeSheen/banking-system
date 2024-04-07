package com.sheen.joe.bankingsystem.repository;

import com.sheen.joe.bankingsystem.entity.Transfer;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@JaversSpringDataAuditable
public interface TransferRepository extends JpaRepository<Transfer, UUID> {
}
