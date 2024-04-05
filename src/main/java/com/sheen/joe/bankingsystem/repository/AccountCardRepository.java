package com.sheen.joe.bankingsystem.repository;

import com.sheen.joe.bankingsystem.entity.AccountCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountCardRepository extends JpaRepository<AccountCard, UUID> {
}
